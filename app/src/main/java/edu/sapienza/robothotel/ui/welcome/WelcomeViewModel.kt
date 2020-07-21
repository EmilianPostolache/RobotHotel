package edu.sapienza.robothotel.ui.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.sapienza.robothotel.db.BookingDao
import edu.sapienza.robothotel.db.RoomDao
import edu.sapienza.robothotel.pepper.PepperManager
import edu.sapienza.robothotel.pepper.PepperState
import edu.sapienza.robothotel.user.UserManager
import edu.sapienza.robothotel.vo.Booking
import edu.sapienza.robothotel.vo.Room
import edu.sapienza.robothotel.vo.RoomType
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import javax.inject.Inject

class WelcomeViewModel @Inject constructor(private val userManager: UserManager,
                                           private val pepperManager: PepperManager,
                                           private val roomDao: RoomDao,
                                           private val bookingDao: BookingDao
) : ViewModel() {

    private val _authenticationState = MutableLiveData<Boolean>(false)
    val authenticationState: LiveData<Boolean>
        get() = _authenticationState

    fun authenticate(name: String, surname: String) {
        viewModelScope.launch {
            userManager.authenticateUser(name, surname)
            createDb()
            _authenticationState.value = true
        }
    }

    fun deauthenticate() {
        userManager.deauthenticateUser()
    }

    fun getPepperState(): MutableLiveData<PepperState>{
        return pepperManager.state
    }

    suspend fun createDb() {

        if (roomDao.findRoom() != null) {
            return
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