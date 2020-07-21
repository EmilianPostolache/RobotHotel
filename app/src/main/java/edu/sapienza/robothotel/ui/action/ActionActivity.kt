package edu.sapienza.robothotel.ui.action

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.`object`.conversation.*
import com.aldebaran.qi.sdk.builder.ChatBuilder
import com.aldebaran.qi.sdk.builder.QiChatbotBuilder
import com.aldebaran.qi.sdk.builder.SayBuilder
import com.aldebaran.qi.sdk.builder.TopicBuilder
import com.aldebaran.qi.sdk.design.activity.RobotActivity
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy
import edu.sapienza.robothotel.R
import edu.sapienza.robothotel.RobotHotelApplication
import edu.sapienza.robothotel.pepper.PepperState
import edu.sapienza.robothotel.ui.checkin.CheckinActivity
import edu.sapienza.robothotel.ui.idle.IdleActivity
import edu.sapienza.robothotel.ui.map.MapActivity
import edu.sapienza.robothotel.viewmodel.ViewModelProviderFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ActionActivity : RobotActivity(), RobotLifecycleCallbacks {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private val viewModel: ActionViewModel by viewModels {
        providerFactory
    }

    private var first: Boolean? = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        first = intent.extras?.getBoolean("first")


        QiSDK.register(this, this)
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE)
        setContentView(R.layout.activity_action)
        val userManager = (application as RobotHotelApplication).appComponent.userManager()
        userManager.userComponent!!.inject(this)
        val cardCheckin = findViewById<CardView>(R.id.card1)
        val cardCheckout = findViewById<CardView>(R.id.card2)
        val cardBook = findViewById<CardView>(R.id.card3)
        val imageCheckin = findViewById<ImageView>(R.id.image1)
        val imageCheckout = findViewById<ImageView>(R.id.image2)
        val imageBook = findViewById<ImageView>(R.id.image3)

        viewModel.bookingState.observe(this, Observer{
           when(it) {
               BookingState.CHECKIN -> {
                   cardBook.isClickable = false
                   cardCheckout.isClickable = false
                   cardCheckin.isClickable = true
                   imageBook.setImageResource(R.drawable.ic_book_gray)
                   imageCheckout.setImageResource(R.drawable.ic_checkout_gray)
                   imageCheckin.setImageResource(R.drawable.ic_checkin)
               }
               BookingState.CHECKOUT -> {
                   cardBook.isClickable = false
                   cardCheckin.isClickable = false
                   cardCheckout.isClickable = true
                   imageBook.setImageResource(R.drawable.ic_book_gray)
                   imageCheckin.setImageResource(R.drawable.ic_checkin_gray)
                   imageCheckout.setImageResource(R.drawable.ic_checkout)
               }
               BookingState.BOOK -> {
                   cardCheckin.isClickable = false
                   cardCheckout.isClickable = false
                   cardBook.isClickable = true
                   imageCheckin.setImageResource(R.drawable.ic_checkin_gray)
                   imageCheckout.setImageResource(R.drawable.ic_checkout_gray)
                   imageBook.setImageResource(R.drawable.ic_book)
               }
           }
        })


        viewModel.getPepperState().observe(this, Observer{
            if (it == PepperState.IDLE) {
                viewModel.deauthenticate()
                idle()
            }
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
        while (viewModel.bookingState.value == null) {
        }
        robotAct(viewModel.bookingState.value!!, qiContext)
    }

    private fun robotAct(bookingState: BookingState, qiContext: QiContext) {
        // say initial phrase
        sayActions(bookingState, qiContext)

        // let the user talk with pepper
        val topic: Topic = TopicBuilder.with(qiContext)
            .withResource(R.raw.actions)
            .build()

        // Create a qiChatbot
        val qiChatbot: QiChatbot = QiChatbotBuilder.with(qiContext).withTopic(topic).build()

        val executors = HashMap<String, QiChatExecutor>()

        // Map the executor name from the topic to our qiChatbotExecutor
        executors["checkinExecutor"] = CheckinChatExecutor(qiContext, bookingState, this )
        executors["checkoutExecutor"] = CheckoutChatExecutor(qiContext, bookingState, this)
        executors["bookExecutor"] = BookChatExecutor(qiContext, bookingState, this)
        executors["mapExecutor"] = MapChatExecutor(qiContext, this)

        // Set the executors to the qiChatbot
        qiChatbot.executors = executors

        // Build chat with the chatbotBuilder
        val chat: Chat = ChatBuilder.with(qiContext).withChatbot(qiChatbot).build()

        // Run an action asynchronously.
        chat.async().run()
    }

    private fun sayActions(state: BookingState, qiContext: QiContext) {
        when (state) {
            BookingState.CHECKIN ->
                SayBuilder.with(qiContext).
                withText(
                    if (first == true) getString(R.string.pepper_action_checkin, viewModel.getUser()?.name)
                    else getString(R.string.pepper_action_help1, viewModel.getUser()?.name)
                ).build().run()
            BookingState.CHECKOUT ->
                SayBuilder.with(qiContext).withText(
                    if (first == true) getString(R.string.pepper_action_checkout, viewModel.getUser()?.name)
                    else getString(R.string.pepper_action_help2, viewModel.getUser()?.name)
                ).build().run()
            BookingState.BOOK ->
                SayBuilder.with(qiContext).
                withText(
                    if (first == true) getString(R.string.pepper_action_book, viewModel.getUser()?.name)
                    else getString(R.string.pepper_action_help3, viewModel.getUser()?.name)
                ).build().run()
        }
    }

    override fun onRobotFocusLost() {

    }

    override fun onRobotFocusRefused(reason: String) {}

    fun onCheckinClicked(view: View?) {
        val intent = Intent(this, CheckinActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun onCheckoutClicked(view: View?) {
        // TODO: need to be added
    }

    fun onBookClicked(view: View?) {
        // TODO: need to be added
    }

    fun onMapClicked(view: View?) {
        val intent = Intent(this, MapActivity::class.java)
        startActivity(intent)
        finish()
    }
}