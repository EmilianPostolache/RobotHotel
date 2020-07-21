package edu.sapienza.robothotel

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import edu.sapienza.robothotel.di.AppComponent
import edu.sapienza.robothotel.di.DaggerAppComponent

class RobotHotelApplication: Application(){

    val TAG = "robothotel"

    val appComponent: AppComponent by lazy {
        // Creates an instance of AppComponent using its Factory constructor
        // We pass the applicationContext that will be used as Context in the graph
        DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this);
    }
}