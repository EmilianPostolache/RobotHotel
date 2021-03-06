package edu.sapienza.robothotel.vo

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index("name", unique = true)])
data class Room(
    val name: String,
    val type: RoomType
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

enum class RoomType {
    SINGLE, DOUBLE, DELUXE
}

