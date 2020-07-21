package edu.sapienza.robothotel.ui.checkin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aldebaran.qi.Future
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.`object`.actuation.Animation
import com.aldebaran.qi.sdk.builder.AnimateBuilder
import com.aldebaran.qi.sdk.builder.AnimationBuilder
import com.aldebaran.qi.sdk.builder.SayBuilder
import com.aldebaran.qi.sdk.design.activity.RobotActivity
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy
import edu.sapienza.robothotel.R
import edu.sapienza.robothotel.RobotHotelApplication
import edu.sapienza.robothotel.pepper.PepperState
import edu.sapienza.robothotel.ui.action.ActionActivity
import edu.sapienza.robothotel.ui.idle.IdleActivity
import edu.sapienza.robothotel.ui.welcome.WelcomeActivity
import edu.sapienza.robothotel.ui.welcome.WelcomeViewModel
import edu.sapienza.robothotel.viewmodel.ViewModelProviderFactory
import edu.sapienza.robothotel.vo.Room
import java.util.*
import javax.inject.Inject

class CheckinActivity : RobotActivity(), RobotLifecycleCallbacks {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private val viewModel: CheckinViewModel by viewModels {
        providerFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        QiSDK.register(this, this)
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE)
        setContentView(R.layout.activity_checkin)

        val userManager = (application as RobotHotelApplication).appComponent.userManager()
        userManager.userComponent!!.inject(this)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = GridLayoutManager(this, 3)

        viewModel.getUserBookingWithRoom().observe(this, Observer {
            val adapter = CheckinAdapter(it[0].room.id)
            recyclerView.adapter = adapter
            viewModel.getRoomsList().observe(this, Observer(adapter::submitList))
            viewModel.checkin()
        })

        viewModel.getPepperState().observe(this, Observer{
            if (it == PepperState.IDLE) {
                viewModel.deauthenticate()
                idle()
            }
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
        if (viewModel.getUserBookingWithRoom().value == null) {
            viewModel.getUserBookingWithRoom().observe(this, Observer {
                robotAct(it[0].room, qiContext)
            })
        }
        else {
            robotAct(viewModel.getUserBookingWithRoom().value!![0].room, qiContext)
        }
    }

    fun robotAct(room: Room, qiContext: QiContext?) {
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
        withText(getString(R.string.pepper_checkin_1, room.name,
            room.type.toString().toLowerCase(Locale.ROOT))).build()
        say1.run()
        val say2 = SayBuilder.with(qiContext).
        withText(getString(R.string.pepper_checkin_2, corridor,row, leftright)).build()
        say2.run()
        val say3 = SayBuilder.with(qiContext).
        withText(getString(R.string.pepper_checkin_3)).build()
        say3.async().run()

        val animation: Animation = AnimationBuilder.with(qiContext) // Create the builder with the context.
            .withResources(R.raw.raise_left_hand_b007) // Set the animation resource.
            .build() // Build the animation.

        val animate = AnimateBuilder.with(qiContext) // Create the builder with the context.
            .withAnimation(animation) // Set the animation.
            .build() // Build the animate action

        val animateFuture: Future<Void>? = animate?.async()?.run()

        animateFuture!!.andThenConsume{
            action()
        }
    }

    override fun onRobotFocusLost() {
        TODO("Not yet implemented")
    }

    override fun onRobotFocusRefused(reason: String?) {
        TODO("Not yet implemented")
    }

    private fun action() {
        val intent = Intent(this, ActionActivity::class.java)
        intent.putExtra("first", false)
        startActivity(intent)
        finish()
    }
}