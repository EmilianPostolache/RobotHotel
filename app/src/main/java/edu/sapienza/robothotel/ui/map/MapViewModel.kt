package edu.sapienza.robothotel.ui.map

import androidx.lifecycle.ViewModel
import edu.sapienza.robothotel.db.BookingDao
import edu.sapienza.robothotel.db.RoomDao
import edu.sapienza.robothotel.db.UserDao
import javax.inject.Inject

class MapViewModel @Inject constructor(roomDao: RoomDao,
                                       userDao: UserDao,
                                       bookingDao: BookingDao) : ViewModel() {

}