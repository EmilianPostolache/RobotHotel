package edu.sapienza.robothotel.pepper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aldebaran.qi.sdk.`object`.actuation.Animate
import com.aldebaran.qi.sdk.`object`.human.Human
import com.aldebaran.qi.sdk.`object`.humanawareness.EngageHuman
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PepperManager @Inject constructor() {
    var state: MutableLiveData<PepperState> = MutableLiveData()
    var human : Human? = null
    var engageHuman : EngageHuman? = null
    var animate : Animate? = null
}

enum class PepperState {
    IDLE, ENGAGED
}