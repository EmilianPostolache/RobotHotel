package edu.sapienza.robothotel.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import edu.sapienza.robothotel.vo.Booking
import edu.sapienza.robothotel.vo.Room
import edu.sapienza.robothotel.vo.RoomType
import edu.sapienza.robothotel.vo.User
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import java.sql.Timestamp

@Database(entities = [User::class, Room::class, Booking::class],
    version = 6)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun roomDao(): RoomDao
    abstract fun bookingDao(): BookingDao
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDate? {
        return value?.let {  LocalDate.parse(value) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): String? {
        return date.toString()
    }

    @TypeConverter
    fun toRoom(value: String) = enumValueOf<RoomType>(value)

    @TypeConverter
    fun fromRoom(value: RoomType) = value.name
}