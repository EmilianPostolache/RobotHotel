package edu.sapienza.robothotel.vo

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(indices = [Index("userId", "roomId", unique = true)])
data class Booking(
    @PrimaryKey val id: Long,
    val userId: Long,
    val roomId: Long,
    val checkinDate: Date?,
    val checkedIn: Boolean,
    val checkedOut: Boolean
)