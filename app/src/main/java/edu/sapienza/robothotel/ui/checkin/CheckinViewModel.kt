package edu.sapienza.robothotel.ui.checkin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.toLiveData
import edu.sapienza.robothotel.db.BookingDao
import edu.sapienza.robothotel.db.RoomDao
import edu.sapienza.robothotel.db.UserDao
import edu.sapienza.robothotel.user.UserManager
import edu.sapienza.robothotel.vo.Booking
import edu.sapienza.robothotel.vo.Room
import edu.sapienza.robothotel.vo.RoomType
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import javax.inject.Inject

class CheckinViewModel @Inject constructor(private var userManager: UserManager,
                                           private var roomDao: RoomDao,
                                           private var userDao: UserDao,
                                           private var bookingDao: BookingDao) : ViewModel() {

    private val roomsList: LiveData<PagedList<Room>> =
        roomDao.findRooms().toLiveData(pageSize = 50)

    private var userBooking: LiveData<Booking> =
        bookingDao.findBookingForDate(userManager.authenticatedUser!!.id, LocalDate.now())

    fun getRoomsList(): LiveData<PagedList<Room>> {
        return roomsList
    }

    fun getUserBooking(): LiveData<Booking> {
        return userBooking
    }

    suspend fun checkin() {
        userBooking.value!!.checkedIn = true
        bookingDao.updateBooking(userBooking.value!!)
    }


}