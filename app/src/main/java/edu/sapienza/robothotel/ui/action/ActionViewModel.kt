package edu.sapienza.robothotel.ui.action

import androidx.lifecycle.*
import androidx.paging.toLiveData
import edu.sapienza.robothotel.db.BookingDao
import edu.sapienza.robothotel.db.RoomDao
import edu.sapienza.robothotel.pepper.PepperManager
import edu.sapienza.robothotel.pepper.PepperState
import edu.sapienza.robothotel.user.UserManager
import edu.sapienza.robothotel.vo.Booking
import edu.sapienza.robothotel.vo.Room
import edu.sapienza.robothotel.vo.RoomType
import edu.sapienza.robothotel.vo.User
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import java.util.*
import javax.inject.Inject

class ActionViewModel @Inject constructor(private val userManager: UserManager,
                                          private val bookingDao: BookingDao,
                                          private val roomDao: RoomDao,
                                          private val pepperManager: PepperManager) : ViewModel(){

    private val booking: LiveData<List<Booking>> =
        bookingDao.findBookingForDateOrActive(userManager.authenticatedUser!!.id, LocalDate.now())

    val bookingState: LiveData<BookingState> =
        Transformations.map(booking) {
            if (it.isNotEmpty()) {
                if (it[0].checkinDate!!.isEqual(LocalDate.now()) && !it[0].checkedIn ) {
                    BookingState.CHECKIN
                }
                else {
                    BookingState.CHECKOUT
                }
            }
            else {
                BookingState.BOOK
            }
        }

    fun getUser(): User? {
        return userManager.authenticatedUser
    }

    fun createDb() {

        viewModelScope.launch {
            if (roomDao.findRoom() != null) {
                return@launch
            }

            val room1 = Room("A1", RoomType.SINGLE)
            val room2 = Room("A2", RoomType.SINGLE)
            val room3 = Room("A3", RoomType.DELUXE)

            val room4 = Room("A4", RoomType.DOUBLE)
            val room5 = Room("A5", RoomType.DOUBLE)
            val room6 = Room("A6", RoomType.SINGLE)

            val room7 = Room("A7", RoomType.SINGLE)
            val room8 = Room("A8", RoomType.SINGLE)
            val room9 = Room("A9", RoomType.DOUBLE)

            val booking = Booking(1,3, LocalDate.now(),
                LocalDate.now().plusDays(2), checkedIn = false, checkedOut = false)

            roomDao.insertRooms(room1, room2, room3, room4, room5, room6, room7, room8, room9)
            bookingDao.insertBooking(booking)
        }
    }

    fun deauthenticate() {
        userManager.deauthenticateUser()
    }

    fun getPepperState(): MutableLiveData<PepperState> {
        return pepperManager.state
    }

}

enum class BookingState {
    BOOK, CHECKIN, CHECKOUT
}