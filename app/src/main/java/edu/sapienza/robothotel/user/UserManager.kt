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
        authenticatedUser = if (user != null) {
            user
        } else {
            registerUser(name, surname)
            userDao.getUser(name, surname)
        }
        userComponent = userComponentFactory.create()
    }

    fun deauthenticateUser() {
        if (isUserAuthenticated()){
            userComponent = null
            authenticatedUser = null
        }
    }

    private suspend fun registerUser(name: String, surname: String): User {
        val user = User(name=name, surname=surname)
        userDao.insertUsers(user)
        return user
    }
}