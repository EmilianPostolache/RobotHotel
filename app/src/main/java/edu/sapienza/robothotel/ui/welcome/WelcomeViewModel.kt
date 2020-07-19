package edu.sapienza.robothotel.ui.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.sapienza.robothotel.user.UserManager
import kotlinx.coroutines.launch
import javax.inject.Inject

class WelcomeViewModel @Inject constructor(private val userManager: UserManager) : ViewModel() {

    private val _authenticationState = MutableLiveData<Boolean>(false)
    val authenticationState: LiveData<Boolean>
        get() = _authenticationState

    fun authenticate(name: String, surname: String) {
        viewModelScope.launch {
            userManager.authenticateUser(name, surname)
            _authenticationState.value = true
        }
    }

}