package edu.sapienza.robothotel.ui.book

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.sapienza.robothotel.db.BookingDao
import edu.sapienza.robothotel.db.RoomDao
import edu.sapienza.robothotel.pepper.PepperManager
import edu.sapienza.robothotel.pepper.PepperState
import edu.sapienza.robothotel.user.UserManager
import javax.inject.Inject

class BookViewModel @Inject constructor(private val userManager: UserManager,
                                        private val bookingDao: BookingDao,
                                        private val roomDao: RoomDao,
                                        private val pepperManager: PepperManager
) : ViewModel(){


    fun deauthenticate() {
        userManager.deauthenticateUser()
    }

    fun getPepperState(): MutableLiveData<PepperState> {
        return pepperManager.state
    }
}