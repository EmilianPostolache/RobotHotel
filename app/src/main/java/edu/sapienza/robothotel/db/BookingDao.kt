package edu.sapienza.robothotel.db

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import edu.sapienza.robothotel.vo.Booking

interface BookingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(vararg request: Booking?)
}