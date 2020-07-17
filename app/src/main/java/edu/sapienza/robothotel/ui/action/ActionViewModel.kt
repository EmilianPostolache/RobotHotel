package edu.sapienza.robothotel.ui.action

import androidx.lifecycle.ViewModel
import edu.sapienza.robothotel.db.BookingDao
import edu.sapienza.robothotel.db.RoomDao
import edu.sapienza.robothotel.db.UserDao
import javax.inject.Inject

class ActionViewModel @Inject constructor(userDao: UserDao) : ViewModel(){
}