package com.krger.krgerfit.Activities.Client.BookAppointment

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.QuerySnapshot
import com.krger.krgerfit.Adapters.TimeSlotAdapter
import com.krger.krgerfit.AppointmentState
import com.krger.krgerfit.DataBaseOperations
import com.krger.krgerfit.Interfaces.onDataBaseResult
import com.krger.krgerfit.Interfaces.onItemSelectedListener
import com.krger.krgerfit.Model.Appointment
import com.krger.krgerfit.Model.TimeSlot
import com.krger.krgerfit.Model.mResult
import com.krger.krgerfit.R
import com.krger.krgerfit.Utils.SharedPref
import com.krger.krgerfit.Utils.Util
import com.krger.krgerfit.databinding.ActivityBookAppointmentBinding
import java.util.*
import kotlin.collections.ArrayList

class BookAppointmentViewModel (private  val activity: Activity, private  val binding: ActivityBookAppointmentBinding) : ViewModel()
{


    private lateinit var timeSlots:MutableList<TimeSlot>
    private  var  horizontalLayoutManagaer:LinearLayoutManager
    private   var timeSelected:Int=-1
    private     var dateSelected:String?=null
    private    var dateSelectedLong:Long=-1
    init
    {

        loadList()
        horizontalLayoutManagaer=LinearLayoutManager(activity, RecyclerView.HORIZONTAL,false)
        binding.recyclerView.layoutManager=horizontalLayoutManagaer

        binding.recyclerView.adapter=TimeSlotAdapter(timeSlots,activity,object :onItemSelectedListener{
            override fun onItemSelected(position: Int)
            {

                if(!timeSlots[position].available)
                {
                    Util.showMessage(activity,"Sorry this slot is not available")
                }
                else
                {
                    timeSelected=timeSlots[position].id
                    binding.txtSelectedTime.text=timeSlots[position].time
                }

            }

        })

        binding.calenderView.minDate=Calendar.getInstance().timeInMillis+86400000
        binding.calenderView.setOnDateChangeListener { calendarView, year, month, day ->
            setSelectedDate(year, month, day)
        }
        val calendar=Calendar.getInstance()
        calendar.timeInMillis=Calendar.getInstance().timeInMillis+86400000
        setSelectedDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH))

    }

    private fun setSelectedDate(year: Int, month: Int, day: Int) {
        dateSelected="$year/$month/$day"
        dateSelectedLong=Util.getDate(year, month, day)
        binding.txtSelectedDate.text=dateSelected
        updateAppointmentsList()
       // Util.showMessage(activity, "Date Selected : $dateSelected")
    }

    private fun updateAppointmentsList()
    {
        clearCount()
        val progressDialog=Util.getProgressDialog(activity)
        DataBaseOperations.getAppointmentsAtDate(dateSelectedLong,object :onDataBaseResult<Task<QuerySnapshot>>
        {
            override fun onResult(task: mResult<Task<QuerySnapshot>>)
            {
                progressDialog!!.dismiss()
                if(task.getResult()!!.isSuccessful)
                {
                    val snapshot=task.getResult()!!.result
                    for(document in snapshot!!.documents)
                    {
                        val appointment=document.toObject(Appointment::class.java)
                        timeSlots[appointment!!.time].count++;


                    }
                    binding.recyclerView.adapter!!.notifyDataSetChanged()


                }
            }
        })


    }

    private fun clearCount() {
        for(timeslot in timeSlots)
            timeslot.count=0
    }


    fun handleScrollBack()
    {
        if (horizontalLayoutManagaer.findFirstVisibleItemPosition() > 0) {
            binding.recyclerView.smoothScrollToPosition(horizontalLayoutManagaer.findFirstVisibleItemPosition() - 1)
        } else {
            binding.recyclerView.smoothScrollToPosition(0)
        }
    }

    fun handleScrollNext() {
        binding.recyclerView.smoothScrollToPosition(horizontalLayoutManagaer.findLastVisibleItemPosition() + 1)
    }


    //FACTORY
    class BookAppointmentViewModelFactory(private val activity: Activity, private  val  binding: ActivityBookAppointmentBinding) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BookAppointmentViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return BookAppointmentViewModel(
                    activity,binding
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    private fun loadList()
    {
        
        timeSlots=ArrayList()
        timeSlots.add(TimeSlot(0,"0:00 AM - 1:00 AM", R.color.purple_200,0,true))
        timeSlots.add(TimeSlot(1,"1:00 AM - 2:00 AM", R.color.purple_200,0,true))
        timeSlots.add(TimeSlot(2,"2:00 AM - 3:00 AM", R.color.purple_200,0,true))
        timeSlots.add(TimeSlot(3,"3:00 AM - 4:00 AM", R.color.purple_200,0,true))
        timeSlots.add(TimeSlot(4,"4:00 AM - 5:00 AM", R.color.purple_200,0,true))
        timeSlots.add(TimeSlot(5,"5:00 AM - 6:00 AM", R.color.purple_200,0,true))
        timeSlots.add(TimeSlot(6,"6:00 AM - 7:00 AM", R.color.purple_200,0,true))
        timeSlots.add(TimeSlot(7,"7:00 AM - 8:00 AM", R.color.purple_200,0,true))
        timeSlots.add(TimeSlot(8,"8:00 AM - 9:00 AM", R.color.purple_200,0,true))
        timeSlots.add(TimeSlot(9,"9:00 AM - 10:00 AM", R.color.purple_200,0,true))
        timeSlots.add(TimeSlot(10,"10:00 AM - 11:00 AM", R.color.purple_200,0,true))
        timeSlots.add(TimeSlot(11,"11:00 AM - 12:00 PM", R.color.purple_200,0,true))

        timeSlots.add(TimeSlot(12,"12:00 PM - 1:00 PM", R.color.purple_200,0,true))
        timeSlots.add(TimeSlot(13,"1:00 PM - 2:00 PM", R.color.purple_200,0,true))
        timeSlots.add(TimeSlot(14,"2:00 PM - 3:00 PM", R.color.purple_200,0,true))
        timeSlots.add(TimeSlot(15,"3:00 PM - 4:00 PM", R.color.purple_200,0,true))
        timeSlots.add(TimeSlot(16,"4:00 PM - 5:00 PM", R.color.purple_200,0,true))
        timeSlots.add(TimeSlot(17,"5:00 PM - 6:00 PM", R.color.purple_200,0,true))
        timeSlots.add(TimeSlot(18,"6:00 PM - 7:00 PM", R.color.purple_200,0,true))
        timeSlots.add(TimeSlot(19,"7:00 PM - 8:00 PM", R.color.purple_200,0,true))
        timeSlots.add(TimeSlot(20,"8:00 PM - 9:00 PM", R.color.purple_200,0,true))
        timeSlots.add(TimeSlot(21,"9:00 PM - 10:00 PM", R.color.purple_200,0,true))
        timeSlots.add(TimeSlot(22,"10:00 PM - 11:00 PM", R.color.purple_200,0,true))
        timeSlots.add(TimeSlot(23,"11:00 PM - 12:00 AM", R.color.purple_200,0,true))


    }

    fun bookApp()
    {

        if(isDataValid())
        {

            val progressDialog=Util.getProgressDialog(activity)
            DataBaseOperations.getUserAppointmentsCount(FirebaseAuth.getInstance().currentUser!!.uid,object :onDataBaseResult<Task<QuerySnapshot>>
            {
                override fun onResult(task: mResult<Task<QuerySnapshot>>)
                {


                    if(task.isSuccess)
                    {

                        val appointmentsCount=task.getResult()!!.result!!.count()
                        if(appointmentsCount<8)
                        {
                            val user=SharedPref.getUser(activity)!!
                            val appointment=Appointment("",user.uid,user.userName,timeSelected,dateSelectedLong,AppointmentState.PENDING)
                            DataBaseOperations.addAppointment(appointment,object :onDataBaseResult<Task<Void>>
                            {
                                override fun onResult(task: mResult<Task<Void>>)
                                {
                                    progressDialog?.dismiss()
                                    if(task.isSuccess)
                                    {
                                        activity.finish()
                                        Util.showMessage(activity,"Appointment Created Successfully")
                                    }
                                    else
                                    {
                                        Util.showMessage(activity,"Error : "+task.getResult()?.exception?.message)
                                    }

                                }
                            })

                        }
                        else
                        {
                            progressDialog?.dismiss()
                            Util.showMessage(activity,"Sorry : You have reached you appointments limit which is 8")

                        }
                    }
                    else
                    {
                        progressDialog?.dismiss()
                        Util.showMessage(activity,"Error : "+task.getResult()!!.exception!!.message)
                    }
                }

            })

        }

    }

    private fun isDataValid(): Boolean
    {

        if(dateSelected.isNullOrEmpty() || timeSelected==-1)
        {
            Util.showMessage(activity,"Select Time And Date")
            return  false
        }
        return true
    }

}