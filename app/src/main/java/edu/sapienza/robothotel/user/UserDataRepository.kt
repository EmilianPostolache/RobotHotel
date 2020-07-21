package edu.sapienza.robothotel.user

import edu.sapienza.robothotel.di.UserScope
import javax.inject.Inject

@UserScope
class UserDataRepository @Inject constructor(private val userManager: UserManager){
}