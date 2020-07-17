package edu.sapienza.robothotel.di

import dagger.Subcomponent
import edu.sapienza.robothotel.ui.action.ActionActivity
import edu.sapienza.robothotel.ui.checkin.CheckinActivity
import edu.sapienza.robothotel.ui.info.InfoActivity

@UserScope
@Subcomponent
interface UserComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): UserComponent
    }

    // Classes that can be injected by this Component
    fun inject(activity: ActionActivity)
    fun inject(activity: CheckinActivity)
    fun inject(activity: InfoActivity)
}