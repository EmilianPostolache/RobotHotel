package edu.sapienza.robothotel.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import edu.sapienza.robothotel.vo.User

@Dao
interface UserDao {
    // ------------------ Insert -------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(vararg user: User?)
}