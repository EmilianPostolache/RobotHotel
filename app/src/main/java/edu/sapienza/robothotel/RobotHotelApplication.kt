package edu.sapienza.robothotel

import android.app.Application
import edu.sapienza.robothotel.di.AppComponent
import edu.sapienza.robothotel.di.DaggerAppComponent

class RobotHotelApplication: Application(){
    val appComponent: AppComponent by lazy {
        // Creates an instance of AppComponent using its Factory constructor
        // We pass the applicationContext that will be used as Context in the graph
        DaggerAppComponent.factory().create(applicationContext)
    }
}