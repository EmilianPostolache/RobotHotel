package edu.sapienza.robothotel.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import edu.sapienza.robothotel.db.AppDatabase
import edu.sapienza.robothotel.db.BookingDao
import edu.sapienza.robothotel.db.RoomDao
import edu.sapienza.robothotel.db.UserDao
import javax.inject.Singleton

@Module(includes=[ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideDb(context: Context): AppDatabase {
        return Room
            .databaseBuilder(context, AppDatabase::class.java, "robothotel.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideBookingDao(db: AppDatabase): BookingDao {
        return db.bookingDao()
    }

    @Singleton
    @Provides
    fun provideUserDao(db: AppDatabase): UserDao {
        return db.userDao()
    }

    @Singleton
    @Provides
    fun provideReviewDao(db: AppDatabase): RoomDao {
        return db.roomDao()
    }
}