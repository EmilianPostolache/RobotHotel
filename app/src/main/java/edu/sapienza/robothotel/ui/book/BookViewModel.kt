package edu.sapienza.robothotel.ui.book

import android.content.Intent
import android.util.Log
import androidx.lifecycle.*
import edu.sapienza.robothotel.db.BookingDao
import edu.sapienza.robothotel.db.RoomDao
import edu.sapienza.robothotel.pepper.PepperManager
import edu.sapienza.robothotel.pepper.PepperState
import edu.sapienza.robothotel.ui.action.BookingState
import edu.sapienza.robothotel.ui.checkin.CheckinActivity
import edu.sapienza.robothotel.user.UserManager
import edu.sapienza.robothotel.utils.LogHelp
import edu.sapienza.robothotel.vo.Booking
import edu.sapienza.robothotel.vo.Room
import edu.sapienza.robothotel.vo.RoomType
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class BookViewModel @Inject constructor(private val userManager: UserManager,
                                        private val bookingDao: BookingDao,
                                        private val roomDao: RoomDao,
                                        private val pepperManager: PepperManager
) : ViewModel(){

    val freeRooms: LiveData<List<Room>> =
        roomDao.findFreeRooms(LocalDate.now())

    val deluxeAvailable: LiveData<Boolean> =
        Transformations.map(freeRooms) {
            it.any {room ->  room.type == RoomType.DELUXE}
        }

    val singleAvailable: LiveData<Boolean> =
        Transformations.map(freeRooms) {
            it.any {room ->  room.type == RoomType.SINGLE}
        }
    val doubleAvailable: LiveData<Boolean> =
        Transformations.map(freeRooms) {
            it.any {room ->  room.type == RoomType.DOUBLE}
        }

    fun book(roomId: Long, days: Int, context: BookActivity) {
        viewModelScope.launch {
            val booking = Booking(userManager.authenticatedUser!!.id, roomId, LocalDate.now(),
                LocalDate.now().plusDays(days.toLong()), checkedIn = false, checkedOut = false)
            bookingDao.insertBooking(booking)
            val intent = Intent(context, CheckinActivity::class.java)
            context.startActivity(intent)
            context.finish()
        }
    }

    fun deauthenticate() {
        userManager.deauthenticateUser()
    }

    fun getPepperState(): MutableLiveData<PepperState> {
        return pepperManager.state
    }
}