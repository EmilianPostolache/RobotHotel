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
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.design.activity.RobotActivity
import edu.sapienza.robothotel.R
import edu.sapienza.robothotel.RobotHotelApplication
import edu.sapienza.robothotel.db.AppDatabase
import edu.sapienza.robothotel.ui.action.ActionActivity
import edu.sapienza.robothotel.viewmodel.ViewModelProviderFactory
import javax.inject.Inject

class WelcomeActivity : RobotActivity(), RobotLifecycleCallbacks {

    private lateinit var nameText: EditText
    private lateinit var surnameText: EditText

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

        nameText = findViewById(R.id.editTextTextPersonName)
        surnameText = findViewById(R.id.editTextTextPersonSurname)


        viewModel.authenticationState.observe(this, Observer {
            if (it) {
                changeActivity()
            }
        })

        QiSDK.register(this, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        QiSDK.unregister(this, this)
    }

    override fun onRobotFocusGained(qiContext: QiContext) {}
    override fun onRobotFocusLost() {}
    override fun onRobotFocusRefused(reason: String) {}

    fun authenticateUser(view: View) {
        // TODO: gestire il caso in cui il teso e' formattato male
        viewModel.authenticate(nameText.text.toString(), surnameText.text.toString())
    }

    private fun changeActivity() {
        // Now change activity
        val intent = Intent(this, ActionActivity::class.java)
        startActivity(intent)
        finish()
    }
}