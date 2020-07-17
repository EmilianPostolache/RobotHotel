package edu.sapienza.robothotel.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import edu.sapienza.robothotel.vo.Room

@Dao
interface RoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRooms(vararg location: Room?)
}