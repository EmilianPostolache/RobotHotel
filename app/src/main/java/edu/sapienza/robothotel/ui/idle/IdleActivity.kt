package edu.sapienza.robothotel.ui.idle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.aldebaran.qi.Future
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.`object`.actuation.Animation
import com.aldebaran.qi.sdk.`object`.actuation.Frame
import com.aldebaran.qi.sdk.`object`.actuation.GoTo
import com.aldebaran.qi.sdk.`object`.human.Human
import com.aldebaran.qi.sdk.`object`.humanawareness.EngageHuman
import com.aldebaran.qi.sdk.`object`.humanawareness.HumanAwareness
import com.aldebaran.qi.sdk.builder.*
import com.aldebaran.qi.sdk.design.activity.RobotActivity
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy
import edu.sapienza.robothotel.R
import edu.sapienza.robothotel.RobotHotelApplication
import edu.sapienza.robothotel.pepper.PepperManager
import edu.sapienza.robothotel.pepper.PepperState
import edu.sapienza.robothotel.ui.checkin.CheckinActivity
import edu.sapienza.robothotel.ui.welcome.WelcomeActivity
import edu.sapienza.robothotel.utils.LogHelp
import javax.inject.Inject

class IdleActivity : RobotActivity(), RobotLifecycleCallbacks {
    private var humanAwareness: HumanAwareness? = null
    private var qiContext: QiContext? = null
    private val TAG = "pepper"

    @Inject
    lateinit var pepperManager: PepperManager

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as RobotHotelApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE)
        setContentView(R.layout.activity_idle)
        QiSDK.register(this, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        QiSDK.unregister(this, this)
    }

    override fun onRobotFocusGained(qiContext: QiContext) {
        this.qiContext = qiContext

        humanAwareness = qiContext.humanAwareness
        humanAwareness?.addOnRecommendedHumanToEngageChangedListener {
            val engageHuman = EngageHumanBuilder.with(qiContext)
                .withHuman(it)
                .build()
            engageHuman.addOnHumanIsEngagedListener {
                pepperManager.state.value = PepperState.ENGAGED
                pepperManager.human = it
                pepperManager.engageHuman = engageHuman
                welcome()
            }
            engageHuman.addOnHumanIsDisengagingListener {
                val say = SayBuilder.with(qiContext).
                withText(resources.getString(R.string.pepper_disengage)).build()
                say.run()
                pepperManager.state.value = PepperState.IDLE
                pepperManager.human = null
                pepperManager.engageHuman = null
            }
            engageHuman.async().run()
        }
    }

    override fun onRobotFocusLost() {
        qiContext = null
    }

    override fun onRobotFocusRefused(reason: String) {
        qiContext = null
    }


    fun onHumanArrival(view: View){
        pepperManager.state.value = PepperState.ENGAGED
        welcome()
    }

    private fun welcome() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}