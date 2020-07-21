package edu.sapienza.robothotel.ui.book

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.`object`.conversation.*
import com.aldebaran.qi.sdk.builder.*
import com.aldebaran.qi.sdk.design.activity.RobotActivity
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy
import com.aldebaran.qi.sdk.util.PhraseSetUtil
import edu.sapienza.robothotel.R
import edu.sapienza.robothotel.RobotHotelApplication
import edu.sapienza.robothotel.pepper.PepperState
import edu.sapienza.robothotel.ui.action.*
import edu.sapienza.robothotel.ui.idle.IdleActivity
import edu.sapienza.robothotel.viewmodel.ViewModelProviderFactory
import edu.sapienza.robothotel.vo.Booking
import edu.sapienza.robothotel.vo.RoomType
import kotlinx.android.synthetic.main.activity_book.*
import javax.inject.Inject

class BookActivity : RobotActivity(), RobotLifecycleCallbacks {



    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private val viewModel: BookViewModel by viewModels {
        providerFactory
    }

    private lateinit var imageSingle: ImageView
    private lateinit var imageDouble: ImageView
    private lateinit var imageDeluxe: ImageView
    private lateinit var daysTextBox: EditText

    private val activeImages = mutableMapOf<ImageView, Int>()

    private var currentRoomType: RoomType? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        QiSDK.register(this, this)
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE)
        setContentView(R.layout.activity_book)
        val userManager = (application as RobotHotelApplication).appComponent.userManager()
        userManager.userComponent!!.inject(this)

        viewModel.getPepperState().observe(this, Observer{
            if (it == PepperState.IDLE) {
                viewModel.deauthenticate()
                idle()
            }
        })

        val cardSingle = findViewById<CardView>(R.id.card1)
        val cardDouble = findViewById<CardView>(R.id.card2)
        val cardDeluxe = findViewById<CardView>(R.id.card3)
        daysTextBox = findViewById(R.id.editDaysNumber)
        imageSingle = findViewById(R.id.image1)
        imageDouble = findViewById(R.id.image2)
        imageDeluxe = findViewById(R.id.image3)

        viewModel.deluxeAvailable.observe(this, Observer{
            if (it == true) {
                cardDeluxe.isClickable = true
                cardDeluxe.isFocusable = true
                imageDeluxe.setImageResource(R.drawable.ic_room_deluxe_blue)
                activeImages[imageDeluxe] = R.drawable.ic_room_deluxe_blue
                cardDeluxe.setOnClickListener(this::onDeluxeRoomClicked)
            }
        })

        viewModel.singleAvailable.observe(this, Observer{
            if (it == true) {
                cardSingle.isClickable = true
                cardSingle.isFocusable = true
                imageSingle.setImageResource(R.drawable.ic_room_single_blue)
                activeImages[imageSingle] = R.drawable.ic_room_single_blue
                cardSingle.setOnClickListener(this::onSingleRoomClicked)
            }
        })

        viewModel.doubleAvailable.observe(this, Observer{
            if (it == true) {
                cardDouble.isClickable = true
                cardDouble.isFocusable = true
                imageDouble.setImageResource(R.drawable.ic_room_double_blue)
                activeImages[imageDouble] = R.drawable.ic_room_double_blue
                cardDouble.setOnClickListener(this::onDoubleRoomClicked)
            }
        })

        viewModel.freeRooms.observe(this, Observer{

        })
    }

    private fun idle() {
        // Now change activity
        val intent = Intent(this, IdleActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        QiSDK.unregister(this, this)
    }


    override fun onRobotFocusGained(qiContext: QiContext) {
        while (viewModel.deluxeAvailable.value == null
            || viewModel.doubleAvailable.value == null
            || viewModel.singleAvailable.value == null
            || viewModel.freeRooms.value == null) {
        }

        SayBuilder.with(qiContext).
        withText(getString(R.string.pepper_book_description)).build().run()
        robotListen(qiContext)
//        // let the user talk with pepper
//        val topic: Topic = TopicBuilder.with(qiContext)
//            .withResource(R.raw.book)
//            .build()
//
//        // Create a qiChatbot
//        val qiChatbot: QiChatbot = QiChatbotBuilder.with(qiContext).withTopic(topic).build()
//
//        val executors = HashMap<String, QiChatExecutor>()
//
//        // Map the executor name from the topic to our qiChatbotExecutor
//        executors["deluxeExecutor"] = DeluxeChatExecutor(qiContext, viewModel.deluxeAvailable.value!!, this)
//        executors["singleExecutor"] = SingleChatExecutor(qiContext, viewModel.singleAvailable.value!!, this)
//        executors["doubleExecutor"] = DoubleChatExecutor(qiContext, viewModel.doubleAvailable.value!!, this)
//        executors["daysExecutor"] = DaysChatExecutor(qiContext, this)
//        executors["okExecutor"] = OkChatExecutor(qiContext, this)
//        executors["noExecutor"] = NoChatExecutor(qiContext, this)
//
//        // Set the executors to the qiChatbot
//        qiChatbot.executors = executors
//
//        // Build chat with the chatbotBuilder
//        val chat: Chat = ChatBuilder.with(qiContext).withChatbot(qiChatbot).build()
//
//        // Run an action asynchronously.
//        chat.async().run()


    }

    private fun robotListen(qiContext: QiContext) {
        val phraseSetDeluxe: PhraseSet = PhraseSetBuilder.with(qiContext)
            .withTexts("deluxe", "deluxe room")
            .build() // Build the PhraseSet.

        val prhaseSetSingle: PhraseSet = PhraseSetBuilder.with(qiContext)
            .withTexts("single", "single room")
            .build() // Build the PhraseSet.

        val prhaseSetDouble: PhraseSet = PhraseSetBuilder.with(qiContext)
            .withTexts("double", "double room")
            .build() // Build the PhraseSet.

        val prhaseSetOk: PhraseSet = PhraseSetBuilder.with(qiContext)
            .withTexts("proceede", "approve", "ok", "accept", "confirm")
            .build() // Build the PhraseSet.

        val prhaseSetNo: PhraseSet = PhraseSetBuilder.with(qiContext)
            .withTexts("return", "back", "cancel", "cancel the booking")
            .build() // Build the PhraseSet.

        val listen: Listen = ListenBuilder.with(qiContext)
            .withPhraseSets(phraseSetDeluxe, prhaseSetSingle,
                prhaseSetDouble, prhaseSetOk, prhaseSetNo)
            .build()

        val listenResult: ListenResult = listen.run()
        val matchedPhraseSet: PhraseSet = listenResult.matchedPhraseSet
        if (PhraseSetUtil.equals(matchedPhraseSet, phraseSetDeluxe)) {
            if (viewModel.deluxeAvailable.value!!) {
            SayBuilder.with(qiContext).withText(
                getString(R.string.pepper_book_aggreement1)).build().run()
                runOnUiThread{ onDeluxeRoomClicked(null) }
            } else {
                SayBuilder.with(qiContext).withText(
                getString(R.string.pepper_book_fail)).build().run()
            }
        }
        if (PhraseSetUtil.equals(matchedPhraseSet, prhaseSetSingle)) {
            if (viewModel.singleAvailable.value!!) {
                SayBuilder.with(qiContext).withText(
                    getString(R.string.pepper_book_aggreement2)).build().run()
                runOnUiThread{ onSingleRoomClicked(null) }
            } else {
                SayBuilder.with(qiContext).withText(
                    getString(R.string.pepper_book_fail)).build().run()
            }
        }

        if (PhraseSetUtil.equals(matchedPhraseSet, prhaseSetDouble)) {
            if (viewModel.doubleAvailable.value!!) {
                SayBuilder.with(qiContext).withText(
                    getString(R.string.pepper_book_aggreement3)).build().run()
                runOnUiThread{ onDoubleRoomClicked(null) }
            } else {
                SayBuilder.with(qiContext).withText(
                    getString(R.string.pepper_book_fail)).build().run()
            }
        }

        if(PhraseSetUtil.equals(matchedPhraseSet, prhaseSetOk)) {
            if (editDaysNumber.text.isEmpty() || editDaysNumber.text.isBlank()
                || currentFocus == null)
                SayBuilder.with(qiContext).withText(
                getString(R.string.pepper_book_incomplete)).build().async().run()
            else {
                book()
            }
        }

        if(PhraseSetUtil.equals(matchedPhraseSet, prhaseSetNo)) {
            action()
        }

        robotListen(qiContext)
    }

    override fun onRobotFocusLost() {
    }

    override fun onRobotFocusRefused(reason: String?) {
    }

    private fun onSingleRoomClicked(view: View?) {
        resetActiveImages()
        imageSingle.setImageResource(R.drawable.ic_room_single_green)
        currentRoomType = RoomType.SINGLE
    }

    private fun onDoubleRoomClicked(view: View?) {
        resetActiveImages()
        imageDouble.setImageResource(R.drawable.ic_room_double_green)
        currentRoomType = RoomType.DOUBLE
    }

    private fun onDeluxeRoomClicked(view: View?) {
        resetActiveImages()
        imageDeluxe.setImageResource(R.drawable.ic_room_deluxe_green)
        currentRoomType = RoomType.DELUXE
    }

    private fun resetActiveImages(){
        for ((image, res) in activeImages) {
            image.setImageResource(res)
        }
    }

    fun action() {
        val intent = Intent(this, ActionActivity::class.java)
        intent.putExtra("first", false)
        startActivity(intent)
        finish()
    }

    fun book() {
        val roomId = viewModel.freeRooms.value!!.filter { room -> room.type == currentRoomType}[0].id
        viewModel.book(roomId, editDaysNumber.text.toString().toInt(), this)
    }
}
