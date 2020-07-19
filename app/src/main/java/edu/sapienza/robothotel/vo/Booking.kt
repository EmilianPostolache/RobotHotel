package edu.sapienza.robothotel.vo

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDate
import java.util.*

@Entity(indices = [Index("userId", "checkinDate", unique = true)])
data class Booking(
    val userId: Long,
    val roomId: Long,
    val checkinDate: LocalDate?,
    val checkoutDate: LocalDate?,
    var checkedIn: Boolean,
    val checkedOut: Boolean
){
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}