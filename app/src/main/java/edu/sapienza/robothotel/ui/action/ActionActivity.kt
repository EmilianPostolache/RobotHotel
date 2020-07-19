package edu.sapienza.robothotel.ui.action

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.design.activity.RobotActivity
import edu.sapienza.robothotel.R
import edu.sapienza.robothotel.RobotHotelApplication
import edu.sapienza.robothotel.ui.checkin.CheckinActivity
import edu.sapienza.robothotel.ui.info.InfoActivity
import edu.sapienza.robothotel.viewmodel.ViewModelProviderFactory
import org.threeten.bp.LocalDate
import javax.inject.Inject

class ActionActivity : RobotActivity(), RobotLifecycleCallbacks {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private val viewModel: ActionViewModel by viewModels {
        providerFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_action)
        val userManager = (application as RobotHotelApplication).appComponent.userManager()
        userManager.userComponent!!.inject(this)
        val cardCheckin = findViewById<CardView>(R.id.card1)
        val cardCheckout = findViewById<CardView>(R.id.card2)
        val cardBook = findViewById<CardView>(R.id.card3)
        val imageCheckin = findViewById<ImageView>(R.id.image1)
        val imageCheckout = findViewById<ImageView>(R.id.image2)
        val imageBook = findViewById<ImageView>(R.id.image3)

        viewModel.getBooking().observe(this, Observer{
            if (it != null) {
                if (it.checkinDate!!.isEqual(LocalDate.now()) && !it.checkedIn ) {
                    cardBook.isClickable = false
                    cardCheckout.isClickable = false
                    imageBook.setImageResource(R.drawable.ic_book_gray)
                    imageCheckout.setImageResource(R.drawable.ic_checkout_gray)
                    // TODO: pepper must say that it has seen it has a booking so it must checkin.
                } else if (it.checkedIn && !it.checkedOut) {
                    cardBook.isClickable = false
                    cardCheckin.isClickable = false
                    imageBook.setImageResource(R.drawable.ic_book_gray)
                    imageCheckin.setImageResource(R.drawable.ic_checkin_gray)
                    // TODO: pepper recognize it as staying at the hotel, it can checkout.
                }
            } else {
                cardCheckin.isClickable = false
                cardCheckout.isClickable = false
                imageCheckin.setImageResource(R.drawable.ic_checkin_gray)
                imageCheckout.setImageResource(R.drawable.ic_checkout_gray)
                // TODO: pepper recognize it user has not a booking so it has to book.
            }
        })
        viewModel.createDb()
    }

    override fun onDestroy() {
        super.onDestroy()
        QiSDK.unregister(this, this)
    }

    override fun onRobotFocusGained(qiContext: QiContext) {}
    override fun onRobotFocusLost() {}
    override fun onRobotFocusRefused(reason: String) {}

    fun onCheckinClicked(view: View) {
        val intent = Intent(this, CheckinActivity::class.java)
        startActivity(intent)
    }

    fun onCheckoutClicked(view: View) {
        // TODO: need to be added
    }

    fun onBookClicked(view: View) {
        // TODO: need to be added
    }

    fun onInformationClicked(view: View) {
        val intent = Intent(this, InfoActivity::class.java)
        startActivity(intent)
    }
}