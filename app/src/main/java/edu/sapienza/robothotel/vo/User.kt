package edu.sapienza.robothotel.vo

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(indices = [Index("name", "surname", unique = true)])
data class User(
    @PrimaryKey val id: Long,
    val name: String,
    val surname: String
)
