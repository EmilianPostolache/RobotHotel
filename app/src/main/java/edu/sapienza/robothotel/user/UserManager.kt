package edu.sapienza.robothotel.user

import edu.sapienza.robothotel.di.UserComponent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager @Inject constructor(
    private val userComponentFactory: UserComponent.Factory
) {
    var userComponent: UserComponent? = null
        private set

    fun isUserActive() = userComponent != null

    // fun isUserRegistered() = storage.getString(REGISTERED_USER).isNotEmpty()

    private fun userActivated() {
        // When the user logs in, we create a new instance of UserComponent
        userComponent = userComponentFactory.create()
    }
}