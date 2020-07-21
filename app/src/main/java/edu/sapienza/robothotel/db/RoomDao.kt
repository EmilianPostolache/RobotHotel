package edu.sapienza.robothotel.db

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import edu.sapienza.robothotel.vo.BookingWithRoom
import edu.sapienza.robothotel.vo.Room
import java.time.LocalDate

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

    @Transaction
    @Query("SELECT id, name, type FROM room" +
            " EXCEPT SELECT room.id, room.name, room.type FROM room INNER JOIN booking ON room.id = booking.roomId " +
            " WHERE (booking.checkedIn = 1 AND booking.checkedOut = 0) OR (booking.checkinDate >= :date)")
    fun findFreeRooms(date: org.threeten.bp.LocalDate?): LiveData<List<Room>>
}

