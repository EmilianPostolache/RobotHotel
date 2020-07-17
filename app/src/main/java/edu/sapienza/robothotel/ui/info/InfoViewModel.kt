package edu.sapienza.robothotel.ui.info

import androidx.lifecycle.ViewModel
import edu.sapienza.robothotel.db.BookingDao
import edu.sapienza.robothotel.db.RoomDao
import edu.sapienza.robothotel.db.UserDao
import javax.inject.Inject

class InfoViewModel @Inject constructor(roomDao: RoomDao,
                                        userDao: UserDao,
                                        bookingDao: BookingDao) : ViewModel() {
}