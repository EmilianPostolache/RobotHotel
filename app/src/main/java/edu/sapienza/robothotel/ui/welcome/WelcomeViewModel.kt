package edu.sapienza.robothotel.ui.welcome

import androidx.lifecycle.ViewModel
import edu.sapienza.robothotel.db.UserDao
import edu.sapienza.robothotel.user.UserManager
import javax.inject.Inject

class WelcomeViewModel @Inject constructor(userManager: UserManager) : ViewModel() {

}