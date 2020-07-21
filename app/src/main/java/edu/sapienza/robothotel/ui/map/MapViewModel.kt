package edu.sapienza.robothotel.ui.map

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
import edu.sapienza.robothotel.user.UserManager
import edu.sapienza.robothotel.vo.Booking
import edu.sapienza.robothotel.vo.BookingWithRoom
import edu.sapienza.robothotel.vo.Room
import edu.sapienza.robothotel.vo.RoomType
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import javax.inject.Inject

class MapViewModel @Inject constructor(private var userManager: UserManager,
                                           private var roomDao: RoomDao,
                                           private var userDao: UserDao,
                                           private var bookingDao: BookingDao,
                                           private var pepperManager: PepperManager ) : ViewModel() {

    private val roomsList: LiveData<PagedList<Room>> =
        roomDao.findRooms().toLiveData(pageSize = 50)

    private val activeBookingsList: LiveData<List<BookingWithRoom>> =
        bookingDao.findActiveBookings(LocalDate.now())

    private var userActiveBookingWithRoom: LiveData<List<BookingWithRoom>> =
        bookingDao.findActiveBookingWithRoom(userManager.authenticatedUser!!.id, LocalDate.now())

    fun getRoomsList(): LiveData<PagedList<Room>> {
        return roomsList
    }

    fun getUserActiveBookingWithRoom(): LiveData<List<BookingWithRoom>> {
        return userActiveBookingWithRoom
    }

    fun getActiveBookingsList(): LiveData<List<BookingWithRoom>> {
        return activeBookingsList
    }

    fun deauthenticate() {
        userManager.deauthenticateUser()
    }

    fun getPepperState(): MutableLiveData<PepperState> {
        return pepperManager.state
    }
}