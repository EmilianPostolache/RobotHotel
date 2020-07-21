package edu.sapienza.robothotel.ui.checkout

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
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
import edu.sapienza.robothotel.viewmodel.ViewModelProviderFactory
import javax.inject.Inject

class CheckoutActivity : RobotActivity(), RobotLifecycleCallbacks {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private val viewModel: CheckoutViewModel by viewModels {
        providerFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        QiSDK.register(this, this)
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE)
        setContentView(R.layout.activity_checkout)

        val userManager = (application as RobotHotelApplication).appComponent.userManager()
        userManager.userComponent!!.inject(this)

        viewModel.userActiveBookingWithRoom.observe(this, Observer {

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
        while (viewModel.userActiveBookingWithRoom.value == null){
        }
        SayBuilder.with(qiContext).
        withText(getString(R.string.pepper_checkout1)).build().run()
        val animation: Animation = AnimationBuilder.with(qiContext) // Create the builder with the context.
            .withResources(R.raw.raise_left_hand_b007) // Set the animation resource.
            .build() // Build the animation.

        val animate = AnimateBuilder.with(qiContext) // Create the builder with the context.
            .withAnimation(animation) // Set the animation.
            .build() // Build the animate action

        val animateFuture: Future<Void>? = animate?.async()?.run()

        animateFuture!!.andThenConsume{
            viewModel.checkout(this)
        }
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