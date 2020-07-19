package edu.sapienza.robothotel.db

import androidx.lifecycle.LiveData
import androidx.room.*
import edu.sapienza.robothotel.vo.User

@Dao
interface UserDao {
    // ------------------ Insert -------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(vararg user: User?)

    // ------------------ Query -------------------
    @Query("SELECT * FROM user WHERE name = :name AND surname = :surname")
    suspend fun getUser(name: String, surname: String): User?
}