package edu.sapienza.robothotel.ui.checkin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.design.activity.RobotActivity
import edu.sapienza.robothotel.R
import edu.sapienza.robothotel.RobotHotelApplication
import edu.sapienza.robothotel.ui.welcome.WelcomeViewModel
import edu.sapienza.robothotel.viewmodel.ViewModelProviderFactory
import javax.inject.Inject

class CheckinActivity : RobotActivity(), RobotLifecycleCallbacks {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private val viewModel: CheckinViewModel by viewModels {
        providerFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkin)
        val userManager = (application as RobotHotelApplication).appComponent.userManager()
        userManager.userComponent!!.inject(this)
        val adapter = CheckinAdapter {itemView, room -> }

        viewModel.getRoomsList().observe(this, Observer(adapter::submitList))
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = adapter

        viewModel.getUserBooking().observe(this, Observer {

            (recyclerView.layoutManager as GridLayoutManager).
            findViewByPosition(it.roomId.toInt())!!
                .setBackgroundColor(resources.getColor(R.color.design_default_color_background))
        })
    }

    override fun onRobotFocusGained(qiContext: QiContext?) {
        TODO("Not yet implemented")
    }

    override fun onRobotFocusLost() {
        TODO("Not yet implemented")
    }

    override fun onRobotFocusRefused(reason: String?) {
        TODO("Not yet implemented")
    }
}