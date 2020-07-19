package edu.sapienza.robothotel.db

import androidx.lifecycle.LiveData
import androidx.room.*
import edu.sapienza.robothotel.vo.Booking
import org.threeten.bp.LocalDate
import java.util.*

@Dao
interface BookingDao {
    // ----------------- Insert ---------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(vararg booking: Booking?)

    // ------------------ Update -------------------

    @Update
    suspend fun updateBooking(vararg booking: Booking?)

    // ------------------ Query -------------------
    @Query("SELECT * FROM booking WHERE userId = :userId AND (checkinDate = :date OR" +
            "(checkedIn = 1 AND checkedOut = 0))")
    fun findBookingForDateOrActive(userId: Long, date: LocalDate?): LiveData<Booking>

    @Query("SELECT * FROM booking WHERE userId = :userId AND checkinDate = :date")
    fun findBookingForDate(userId: Long, date: LocalDate?): LiveData<Booking>
}