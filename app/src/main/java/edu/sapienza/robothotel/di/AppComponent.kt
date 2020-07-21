package edu.sapienza.robothotel.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import edu.sapienza.robothotel.ui.welcome.WelcomeActivity
import edu.sapienza.robothotel.pepper.PepperManager
import edu.sapienza.robothotel.ui.idle.IdleActivity
import edu.sapienza.robothotel.user.UserManager
import javax.inject.Singleton

@Singleton
@Component(modules = [AppSubcomponents::class, AppModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun userManager(): UserManager
    fun pepperManager(): PepperManager
    fun inject(activity: WelcomeActivity)
    fun inject(activity: IdleActivity)
}