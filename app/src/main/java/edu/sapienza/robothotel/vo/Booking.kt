package edu.sapienza.robothotel.vo

import androidx.room.*
import org.threeten.bp.LocalDate
import java.util.*

@Entity(indices = [Index("userId", "checkinDate", unique = true)])
data class Booking(
    val userId: Long,
    val roomId: Long,
    val checkinDate: LocalDate?,
    val checkoutDate: LocalDate?,
    var checkedIn: Boolean,
    var checkedOut: Boolean
){
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

data class BookingWithRoom (
    @Embedded val booking: Booking,

    @Relation(
        parentColumn = "roomId",
        entityColumn = "id",
        entity = Room::class
    )
    val room: Room
)

