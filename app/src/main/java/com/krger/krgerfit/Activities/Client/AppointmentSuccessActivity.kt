package com.krger.krgerfit.Activities.Client

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.QuerySnapshot
import com.krger.krgerfit.Constants
import com.krger.krgerfit.DataBaseOperations
import com.krger.krgerfit.Interfaces.onDataBaseResult
import com.krger.krgerfit.Model.Appointment
import com.krger.krgerfit.Model.mResult
import com.krger.krgerfit.R
import com.krger.krgerfit.Utils.Util
import com.krger.krgerfit.databinding.ActivityAppointSuccessBinding


class AppointmentSuccessActivity : AppCompatActivity() {


    lateinit var binding:ActivityAppointSuccessBinding
    var list: MutableList<String> = ArrayList()
    lateinit var adapter:ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAppointSuccessBinding.inflate(layoutInflater)
        Util.putValsOnHeader(this,"Appointment Details",binding.lytHeader.txtHeaderTitle,binding.lytHeader.imgUser,binding.lytHeader.imgMenu)
        setContentView(binding.root)


        val selectedDate=intent.extras!!.getLong(Constants.EXTRA_KEY_DATE)
        val selectedTIme=intent.extras!!.getInt(Constants.EXTRA_KEY_TIME)

        binding.txtDate.text=Util.getFormattedDate(selectedDate)
        binding.txtTime.text=Util.getTimeSlotsList()[selectedTIme].time

        adapter=ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,list)
        binding.listView.adapter=adapter
        loadUsersAppointments(selectedDate,selectedTIme)

    }

    private fun loadUsersAppointments(selectedDate: Long, selectedTIme: Int)
    {

        binding.listView.visibility=View.GONE
        binding.txtErrorMessage.visibility=View.GONE
        binding.progressBar.visibility=View.VISIBLE
        DataBaseOperations.getAppointmentsAtDateTime(selectedDate,selectedTIme,object:onDataBaseResult<Task<QuerySnapshot>>
        {
            override fun onResult(task: mResult<Task<QuerySnapshot>>)
            {
                list.clear()
                binding.progressBar.visibility=View.GONE
                if(task.isSuccess)
                {
                    val result=task.getResult()!!.result
                    if(! result!!.isEmpty)
                    {
                        for(doc in result.documents)
                        {
                            val appoinment=doc.toObject(Appointment::class.java)
                            if(appoinment!!.uid!=FirebaseAuth.getInstance().currentUser!!.uid)
                            {
                                var userName=appoinment.name
                                if(userName.length>3)
                                {
                                    userName=userName.substring(0,3)
                                }
                                list.add("$userName...")
                            }

                        }

                    }

                    if(list.isEmpty())
                    {
                        binding.txtErrorMessage.visibility=View.VISIBLE
                        binding.txtErrorMessage.text="No other users have appointment on same time/day"
                    }
                    else
                    {
                        binding.listView.visibility=View.VISIBLE
                        adapter.notifyDataSetChanged()
                    }
                }
                else
                {
                    binding.txtErrorMessage.visibility=View.VISIBLE
                    binding.txtErrorMessage.text="ERROR OCCURRED"
                }

            }
        })


    }
}