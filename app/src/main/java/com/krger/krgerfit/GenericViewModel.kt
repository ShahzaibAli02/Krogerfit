package com.krger.krgerfit
import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.krger.krgerfit.databinding.ActivityBookAppointmentBinding

class GenericViewModel (private  val activity: Activity, private  val binding: ActivityBookAppointmentBinding) : ViewModel()
{
    

    class Generic_ViewModelFactory(private val activity: Activity, private  val  binding: ActivityBookAppointmentBinding) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GenericViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return GenericViewModel(
                    activity,binding
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }


}