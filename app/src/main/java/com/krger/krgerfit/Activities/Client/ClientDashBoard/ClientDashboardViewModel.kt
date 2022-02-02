package com.krger.krgerfit.Activities.Client.ClientDashBoard

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.krger.krgerfit.Activities.Login.LoginActivity
import com.krger.krgerfit.Adapters.AppointmentAdapterClient
import com.krger.krgerfit.AlarmReceiver
import com.krger.krgerfit.DataBaseOperations
import com.krger.krgerfit.Enums.AppointmentState
import com.krger.krgerfit.Interfaces.onDataBaseResult
import com.krger.krgerfit.Interfaces.onItemSelectedListener
import com.krger.krgerfit.Model.Appointment
import com.krger.krgerfit.Model.mResult
import com.krger.krgerfit.R
import com.krger.krgerfit.Utils.SharedPref
import com.krger.krgerfit.Utils.Util
import com.krger.krgerfit.databinding.ActivityClientDashBinding
import java.util.*
import kotlin.collections.ArrayList


class ClientDashboardViewModel (private  val activity: Activity, private  val binding: ActivityClientDashBinding) : ViewModel()
{


    var appointmentsList:MutableList<Appointment> = ArrayList()
    lateinit var appointmentsAdapterClient:AppointmentAdapterClient

    fun loadAppointments()
    {

        binding.recyclerView.visibility=View.GONE
        binding.txtErrorMessage.visibility=View.GONE
        binding.progressBar.visibility=View.VISIBLE
        DataBaseOperations.getAppointmentsAtUid(SharedPref.getUser(activity)!!.uid,object :onDataBaseResult<Task<QuerySnapshot>>
        {
            override fun onResult(task: mResult<Task<QuerySnapshot>>)
            {
                appointmentsList.clear()
                binding.progressBar.visibility=View.GONE
                val result=task.getResult()
                val snapShots=result!!.result!!
                if(snapShots.isEmpty)
                {
                    binding.txtErrorMessage.visibility=View.VISIBLE
                }
                else
                {
                    for(doc in snapShots.documents)
                    {
                        appointmentsList.add(doc.toObject(Appointment::class.java)!!)
                    }

                    binding.recyclerView.visibility=View.VISIBLE
                    appointmentsAdapterClient.notifyDataSetChanged()


                }
            }
        })

    }
    fun  setvalsonHeader()
    {
        Util.putValsOnHeader(activity,
            "DashBoard",
            binding.lytHeader.txtHeaderTitle,
            binding.lytHeader.imgUser,
            binding.lytHeader.imgMenu
        )
    }
    fun initRecyclerView() {

        appointmentsAdapterClient=AppointmentAdapterClient(appointmentsList,activity,object :onItemSelectedListener{

            override fun onItemSelected(view: View, position: Int)
            {
                val appointment=appointmentsList[position]
                val progressDialog=Util.getProgressDialog(activity)
                DataBaseOperations.updateAppointmentState(appointment.id,AppointmentState.CANCELED,object :onDataBaseResult<Task<Void>>
                {
                    override fun onResult(task: mResult<Task<Void>>)
                    {
                        progressDialog!!.dismiss()

                        if(task.isSuccess)
                        {
                            Util.showMessage(activity,"Appointment Cancelled")
                            appointment.appointmentState=AppointmentState.CANCELED
                            appointmentsAdapterClient.notifyDataSetChanged()
                        }
                        else
                        {
                            Util.showMessage(activity,"Failed To Update")
                        }

                    }
                })
            }
        })
        binding.recyclerView.adapter=appointmentsAdapterClient

    }


    class ClientDashboardViewModelFactory(private val activity: Activity, private  val  binding: ActivityClientDashBinding) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ClientDashboardViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ClientDashboardViewModel(
                    activity,binding
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }




}