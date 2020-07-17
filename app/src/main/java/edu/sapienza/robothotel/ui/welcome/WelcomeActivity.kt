package edu.sapienza.robothotel.ui.welcome

import android.os.Bundle
import androidx.activity.viewModels
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.design.activity.RobotActivity
import edu.sapienza.robothotel.R
import edu.sapienza.robothotel.RobotHotelApplication
import edu.sapienza.robothotel.viewmodel.ViewModelProviderFactory
import javax.inject.Inject

class WelcomeActivity : RobotActivity(), RobotLifecycleCallbacks {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private val viewModel: WelcomeViewModel by viewModels {
        providerFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val component = (application as RobotHotelApplication).appComponent
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        QiSDK.register(this, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        QiSDK.unregister(this, this)
    }

    override fun onRobotFocusGained(qiContext: QiContext) {}
    override fun onRobotFocusLost() {}
    override fun onRobotFocusRefused(reason: String) {}
}