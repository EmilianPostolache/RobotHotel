package edu.sapienza.robothotel.db

import androidx.paging.DataSource
import androidx.room.*
import edu.sapienza.robothotel.vo.Room
import edu.sapienza.robothotel.vo.User

@Dao
interface RoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRooms(vararg room: Room?)

    // ------------------ Query -------------------
    @Transaction
    @Query("SELECT * FROM room ORDER BY id DESC")
    fun findRooms(): DataSource.Factory<Int, Room>

}

