package edu.sapienza.robothotel.ui.map

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aldebaran.qi.Future
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.`object`.actuation.Animation
import com.aldebaran.qi.sdk.`object`.conversation.*
import com.aldebaran.qi.sdk.builder.*
import com.aldebaran.qi.sdk.design.activity.RobotActivity
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy
import edu.sapienza.robothotel.R
import edu.sapienza.robothotel.RobotHotelApplication
import edu.sapienza.robothotel.pepper.PepperState
import edu.sapienza.robothotel.ui.action.*
import edu.sapienza.robothotel.ui.idle.IdleActivity
import edu.sapienza.robothotel.utils.LogHelp
import edu.sapienza.robothotel.viewmodel.ViewModelProviderFactory
import edu.sapienza.robothotel.vo.Room
import edu.sapienza.robothotel.vo.RoomType
import java.util.*
import javax.inject.Inject

class MapActivity : RobotActivity(), RobotLifecycleCallbacks {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private val viewModel: MapViewModel by viewModels {
        providerFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        QiSDK.register(this, this)
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE)
        setContentView(R.layout.activity_map)

        val userManager = (application as RobotHotelApplication).appComponent.userManager()
        userManager.userComponent!!.inject(this)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = GridLayoutManager(this, 3)

        viewModel.getUserActiveBookingWithRoom().observe(this, Observer {
            val adapter = if (it.isEmpty()){
                MapAdapter(0)
            } else{
                MapAdapter(it[0].room.id)
            }
            recyclerView.adapter = adapter
            viewModel.getRoomsList().observe(this, Observer(adapter::submitList))
        })

        viewModel.getPepperState().observe(this, Observer{
            if (it == PepperState.IDLE) {
                viewModel.deauthenticate()
                idle()
            }
        })

        viewModel.getActiveBookingsList().observe(this, Observer{
            Log.i(LogHelp.TAG, "Active bookings list: " + it.size.toString())

        })

    }

    override fun onDestroy() {
        super.onDestroy()
        QiSDK.unregister(this, this)
    }

    private fun idle() {
        // Now change activity
        val intent = Intent(this, IdleActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onRobotFocusGained(qiContext: QiContext?) {
        while (viewModel.getUserActiveBookingWithRoom().value == null) {
        }
        if (viewModel.getUserActiveBookingWithRoom().value!!.isEmpty()){
            robotActNoRoom(qiContext)
        } else {
            robotActRoom(viewModel.getUserActiveBookingWithRoom().value!![0].room, qiContext)
        }
    }

    private fun robotActRoom(room: Room, qiContext: QiContext?) {
        val corridor = when(room.id) {
            1L, 4L, 7L -> "right"
            else -> "left"
        }

        val leftright = when(room.id) {
            2L, 5L, 8L -> "right"
            else -> "left"
        }
        val row = when(room.id) {
            in 1..3 -> "first"
            in 4..6 -> "second"
            else    -> "third" }

        val say1 = SayBuilder.with(qiContext).
        withText(getString(R.string.pepper_map_1)).build()
        say1.run()
        val say2 = SayBuilder.with(qiContext).
        withText(getString(R.string.pepper_map_2, corridor,row, leftright)).build()
        say2.run()
        listenForFinish(qiContext)
    }

    private fun robotActNoRoom(qiContext: QiContext?) {
        while (viewModel.getActiveBookingsList().value == null) {

        }
        Log.i(LogHelp.TAG, "In robotActNoRoom")

        val numRooms = viewModel.getRoomsList().value!!.size
        val numRoomsDeluxe = viewModel.getRoomsList().value!!.count { room -> room.type == RoomType.DELUXE }
        val numRoomsSingle = viewModel.getRoomsList().value!!.count { room -> room.type == RoomType.SINGLE }
        val numRoomsDouble = viewModel.getRoomsList().value!!.count { room -> room.type == RoomType.DOUBLE }
        val numRoomsOccupied = viewModel.getActiveBookingsList().value!!.size

        val say1 = SayBuilder.with(qiContext).
        withText(getString(R.string.pepper_map_1)).build()
        say1.run()
        val say2 = SayBuilder.with(qiContext).
        withText(getString(R.string.pepper_stats,
            numRooms.toString(),
            numRoomsSingle.toString(),
            numRoomsDouble.toString(),
            numRoomsDeluxe.toString(),
            numRoomsOccupied.toString())).build()
        say2.run()
        listenForFinish(qiContext)
    }

    private fun listenForFinish(qiContext: QiContext?) {
        val say = SayBuilder.with(qiContext).
        withText(getString(R.string.pepper_map_confirm)).build()
        say.run()

        // let the user talk with pepper
        val topic: Topic = TopicBuilder.with(qiContext)
            .withResource(R.raw.map)
            .build()

        // Create a qiChatbot
        val qiChatbot: QiChatbot = QiChatbotBuilder.with(qiContext).withTopic(topic).build()

        val executors = HashMap<String, QiChatExecutor>()

        // Map the executor name from the topic to our qiChatbotExecutor
        executors["okExecutor"] = OkChatExecutor(qiContext!!, this)

        // Set the executors to the qiChatbot
        qiChatbot.executors = executors

        // Build chat with the chatbotBuilder
        val chat: Chat = ChatBuilder.with(qiContext).withChatbot(qiChatbot).build()

        // Run an action asynchronously.
        chat.async().run()
    }


    override fun onRobotFocusLost() {
    }

    override fun onRobotFocusRefused(reason: String?) {
    }

    fun action() {
        val intent = Intent(this, ActionActivity::class.java)
        intent.putExtra("first", false)
        startActivity(intent)
        finish()
    }
}