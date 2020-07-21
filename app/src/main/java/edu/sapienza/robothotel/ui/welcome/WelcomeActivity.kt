package edu.sapienza.robothotel.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.room.Room
import com.aldebaran.qi.Future
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.`object`.actuation.Animation
import com.aldebaran.qi.sdk.`object`.conversation.Chat
import com.aldebaran.qi.sdk.`object`.conversation.QiChatbot
import com.aldebaran.qi.sdk.`object`.conversation.Topic
import com.aldebaran.qi.sdk.builder.*
import com.aldebaran.qi.sdk.design.activity.RobotActivity
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy
import edu.sapienza.robothotel.R
import edu.sapienza.robothotel.RobotHotelApplication
import edu.sapienza.robothotel.db.AppDatabase
import edu.sapienza.robothotel.pepper.PepperManager
import edu.sapienza.robothotel.pepper.PepperState
import edu.sapienza.robothotel.ui.action.ActionActivity
import edu.sapienza.robothotel.ui.idle.IdleActivity
import edu.sapienza.robothotel.viewmodel.ViewModelProviderFactory
import javax.inject.Inject

class WelcomeActivity : RobotActivity(), RobotLifecycleCallbacks {

    private lateinit var nameText: EditText
    private lateinit var surnameText: EditText
    private var chat: Chat? = null

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private val viewModel: WelcomeViewModel by viewModels {
        providerFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val component = (application as RobotHotelApplication).appComponent
        component.inject(this)

        super.onCreate(savedInstanceState)
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE)
        setContentView(R.layout.activity_welcome)

        nameText = findViewById(R.id.editTextTextPersonName)
        surnameText = findViewById(R.id.editTextTextPersonSurname)


        viewModel.authenticationState.observe(this, Observer {
            if (it) {
                selectAction()
            }
        })

        viewModel.getPepperState().observe(this, Observer{
            if (it == PepperState.IDLE) {
                viewModel.deauthenticate()
                idle()
            }
        })

        QiSDK.register(this, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        QiSDK.unregister(this, this)
    }

    override fun onRobotFocusGained(qiContext: QiContext) {

        val say1 = SayBuilder.with(qiContext).
        withText(resources.getString(R.string.pepper_welcome)).build()
        say1.async().run()

        val animation: Animation = AnimationBuilder.with(qiContext) // Create the builder with the context.
            .withResources(R.raw.hello_a001) // Set the animation resource.
            .build() // Build the animation.

        val animate = AnimateBuilder.with(qiContext) // Create the builder with the context.
            .withAnimation(animation) // Set the animation.
            .build() // Build the animate action

        val animateFuture: Future<Void>? = animate?.async()?.run()

        animateFuture!!.andThenConsume{
            val say2 = SayBuilder.with(qiContext).
            withText(resources.getString(R.string.pepper_entername)).build()
            say2.run()
        }

    }

    override fun onRobotFocusLost() {
    }

    override fun onRobotFocusRefused(reason: String) {}

    fun authenticateUser(view: View) {
        // TODO: gestire il caso in cui il teso e' formattato male
        viewModel.authenticate(nameText.text.toString(), surnameText.text.toString())
    }

    private fun selectAction() {
        // Now change activity
        val intent = Intent(this, ActionActivity::class.java)
        intent.putExtra("first", true)
        startActivity(intent)
        finish()
    }

    private fun idle() {
        // Now change activity
        val intent = Intent(this, IdleActivity::class.java)
        startActivity(intent)
        finish()
    }
}