package edu.sapienza.robothotel.ui.idle

import android.os.Bundle
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.`object`.humanawareness.HumanAwareness
import com.aldebaran.qi.sdk.design.activity.RobotActivity
import edu.sapienza.robothotel.R

class IdleActivity : RobotActivity(), RobotLifecycleCallbacks {
    private var humanAwareness: HumanAwareness? = null
    private var qiContext: QiContext? = null
    private val TAG = "pepper"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        findHuman()
    }

    override fun onRobotFocusLost() {
        qiContext = null
    }

    override fun onRobotFocusRefused(reason: String) {
        qiContext = null
    }

    private fun findHuman() {
        // val humanAround: Future<Human> = humanAwareness.async().getRecommendedHumanToEngage()
    }
}