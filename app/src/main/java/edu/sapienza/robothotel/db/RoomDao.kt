package edu.sapienza.robothotel.db

import androidx.paging.DataSource
import androidx.room.*
import edu.sapienza.robothotel.vo.Room

@Dao
interface RoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRooms(vararg room: Room?)

    // ------------------ Query -------------------
    @Transaction
    @Query("SELECT * FROM room ORDER BY id DESC")
    fun findRooms(): DataSource.Factory<Int, Room>

    @Query("SELECT * FROM room ORDER BY id DESC LIMIT 1")
    suspend fun findRoom(): Room?
}

