package edu.sapienza.robothotel.ui.checkout

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.toLiveData
import edu.sapienza.robothotel.db.BookingDao
import edu.sapienza.robothotel.db.RoomDao
import edu.sapienza.robothotel.db.UserDao
import edu.sapienza.robothotel.pepper.PepperManager
import edu.sapienza.robothotel.pepper.PepperState
import edu.sapienza.robothotel.ui.checkin.CheckinActivity
import edu.sapienza.robothotel.user.UserManager
import edu.sapienza.robothotel.vo.BookingWithRoom
import edu.sapienza.robothotel.vo.Room
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import javax.inject.Inject

class CheckoutViewModel @Inject constructor(private var userManager: UserManager,
                                            private var roomDao: RoomDao,
                                            private var userDao: UserDao,
                                            private var bookingDao: BookingDao,
                                            private var pepperManager: PepperManager
) : ViewModel() {

     var userActiveBookingWithRoom: LiveData<List<BookingWithRoom>> =
        bookingDao.findActiveBookingWithRoom(userManager.authenticatedUser!!.id, LocalDate.now())

    fun checkout(context: CheckoutActivity) {
        viewModelScope.launch{
            var userBooking = userActiveBookingWithRoom.value!![0].booking
            userBooking.checkedOut = true
            bookingDao.updateBooking(userBooking)
            context.action()
        }
    }

    fun deauthenticate() {
        userManager.deauthenticateUser()
    }

    fun getPepperState(): MutableLiveData<PepperState> {
        return pepperManager.state
    }
}