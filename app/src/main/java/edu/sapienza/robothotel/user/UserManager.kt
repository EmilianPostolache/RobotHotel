package edu.sapienza.robothotel.user

import edu.sapienza.robothotel.db.UserDao
import edu.sapienza.robothotel.di.UserComponent
import edu.sapienza.robothotel.vo.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager @Inject constructor(private val userDao: UserDao,
    private val userComponentFactory: UserComponent.Factory
) {
    var userComponent: UserComponent? = null
        private set

    var authenticatedUser : User? = null

    fun isUserAuthenticated() = userComponent != null

    suspend fun authenticateUser(name: String, surname: String) {
        val user = userDao.getUser(name, surname)
        if (user != null) {
            authenticatedUser = user
            // Log.i("robothotel", user.name)
        } else {
            registerUser(name, surname)
            //Log.i("robothotel", "Registered new user!")
        }
        userComponent = userComponentFactory.create()
    }

    private suspend fun registerUser(name: String, surname: String) {
        val user = User(name=name, surname=surname)
        userDao.insertUsers(user)
    }
}