package edu.sapienza.robothotel.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import edu.sapienza.robothotel.ui.action.ActionViewModel
import edu.sapienza.robothotel.ui.checkin.CheckinViewModel
import edu.sapienza.robothotel.ui.map.MapViewModel
import edu.sapienza.robothotel.ui.welcome.WelcomeViewModel
import edu.sapienza.robothotel.viewmodel.ViewModelProviderFactory

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(vmp: ViewModelProviderFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(WelcomeViewModel::class)
    abstract fun bindWelcomeViewModel(viewModel: WelcomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel::class)
    abstract fun bindMapViewModel(viewModel: MapViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CheckinViewModel::class)
    abstract fun bindCheckinViewModel(viewModel: CheckinViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ActionViewModel::class)
    abstract fun bindActionViewModel(viewModel: ActionViewModel): ViewModel
}

