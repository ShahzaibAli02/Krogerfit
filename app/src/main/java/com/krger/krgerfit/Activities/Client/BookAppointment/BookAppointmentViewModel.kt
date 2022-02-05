package com.krger.krgerfit.Activities.Client.BookAppointment

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.view.View
import android.window.SplashScreen
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.QuerySnapshot
import com.krger.krgerfit.Activities.Client.AppointmentSuccessActivity
import com.krger.krgerfit.Activities.Login.LoginActivity
import com.krger.krgerfit.Activities.SplashActivity
import com.krger.krgerfit.Adapters.TimeSlotAdapter
import com.krger.krgerfit.AlarmReceiver
import com.krger.krgerfit.Constants
import com.krger.krgerfit.Enums.AppointmentState
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

    val timeSlotAdapter:TimeSlotAdapter
    var  mDay=0
    var  mMonth=0
    var  mYear=0
    var  mHour=0
    init
    {

        loadList()
        horizontalLayoutManagaer=LinearLayoutManager(activity, RecyclerView.HORIZONTAL,false)
        binding.recyclerView.layoutManager=horizontalLayoutManagaer

        timeSlotAdapter=TimeSlotAdapter(timeSlots,activity,this,object :onItemSelectedListener{
            override fun onItemSelected(view : View,position: Int)
            {

                if(!timeSlots[position].available)
                {
                    Util.showMessage(activity,"Sorry this slot is not available")
                }
                else
                {
                    timeSelected=timeSlots[position].id
                    mHour=timeSelected
                    binding.txtSelectedTime.text=timeSlots[position].time
                }

            }

        })

        binding.recyclerView.adapter=timeSlotAdapter
        binding.calenderView.minDate=Calendar.getInstance().timeInMillis
        binding.calenderView.setOnDateChangeListener { calendarView, year, month, day ->

            mYear=year
            mMonth=month
            mDay=day
            setSelectedDate(year, month, day)
        }
        val calendar=Calendar.getInstance()
        calendar.timeInMillis=Calendar.getInstance().timeInMillis
        mYear=calendar.get(Calendar.YEAR)
        mMonth=calendar.get(Calendar.MONTH)
        mDay=calendar.get(Calendar.DAY_OF_MONTH)
        setSelectedDate(mYear,mMonth,mDay)

    }
    fun  setvalsonHeader()
    {
        Util.putValsOnHeader(activity,
            "Book Appointment",
            binding.lytHeader.txtHeaderTitle,
            binding.lytHeader.imgUser,
            binding.lytHeader.imgMenu
        )
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

        timeSelected=-1;
        binding.txtSelectedTime.text="NOT SELECTED"
        val progressDialog=Util.getProgressDialog(activity)
        DataBaseOperations.getAppointmentsAtDate(dateSelectedLong,object :onDataBaseResult<Task<QuerySnapshot>>
        {
            override fun onResult(task: mResult<Task<QuerySnapshot>>)
            {
                clearCount()
                progressDialog!!.dismiss()
                if(task.getResult()!!.isSuccessful)
                {
                    val snapshot=task.getResult()!!.result
                    for(document in snapshot!!.documents)
                    {

                        val appointment=document.toObject(Appointment::class.java)
                        if(appointment!!.uid==FirebaseAuth.getInstance().currentUser!!.uid)
                        {
                            timeSlots[appointment!!.time].available=false
                        }
                        timeSlots[appointment!!.time].count++

                    }
                    timeSlotAdapter.update()



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
        timeSlots.addAll(Util.getTimeSlotsList())

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
                            val appointment=Appointment("",user.uid,user.userName,user.imageLink,timeSelected,dateSelectedLong,
                                AppointmentState.PENDING)
                            DataBaseOperations.addAppointment(appointment,object :onDataBaseResult<Task<Void>>
                            {
                                override fun onResult(task: mResult<Task<Void>>)
                                {
                                    progressDialog?.dismiss()
                                    if(task.isSuccess)
                                    {

                                        setAlarm()
                                        val intent=Intent(activity,AppointmentSuccessActivity::class.java)
                                        intent.putExtra(Constants.EXTRA_KEY_DATE,dateSelectedLong)
                                        intent.putExtra(Constants.EXTRA_KEY_TIME,timeSelected)
                                        activity.startActivity(intent)
                                        activity.finish()
                                        //Util.showMessage(activity,"Appointment Created Successfully")
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


    fun setAlarm()
    {
        val alarmMgr: AlarmManager?
        val alarmIntent: PendingIntent

        alarmMgr=activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        val intent=Intent(activity, AlarmReceiver::class.java)
        intent.putExtra(AlarmReceiver.NOTIFICATION_ID, Random().nextInt())
        intent.putExtra(AlarmReceiver.NOTIFICATION, getNotification())
        alarmIntent=PendingIntent.getBroadcast(activity, Random().nextInt(), intent, 0)
        val calendar: Calendar=Calendar.getInstance()
        calendar.timeInMillis=System.currentTimeMillis()
        //calendar.set(mYear , mMonth , mDay , mHour , mMinute);
        calendar.set(Calendar.YEAR, mYear)
        calendar.set(Calendar.DAY_OF_MONTH, mDay)
        calendar.set(Calendar.MONTH, mMonth)
        calendar.set(Calendar.HOUR_OF_DAY, mHour)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val time=calendar.timeInMillis-Constants.ALARM_BEFORE


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmMgr!!.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP , (calendar.timeInMillis-Constants.ALARM_BEFORE), alarmIntent)
        };
        /*alarmMgr!!.setRepeating(AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            5000,
            alarmIntent
        )

         */
    }


    private fun getNotification(): Notification
    {
        val defaultSound: Uri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val intent=Intent(activity, SplashActivity::class.java)
        val pendingIntent=
            PendingIntent.getActivity(activity,
                Random().nextInt(100),
                intent,
                Random().nextInt(100)
            )
        val notificationManager=activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        val NOTIFICATION_CHANNEL_ID: String=java.lang.String.valueOf(Random().nextInt(100))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") val notificationChannel=NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Notification",
                NotificationManager.IMPORTANCE_MAX
            )

            //Configure Notification Channel
            notificationChannel.enableLights(true)
            notificationChannel.vibrationPattern=longArrayOf(0, 1000, 500, 1000)
            notificationChannel.enableVibration(true)
            notificationManager!!.createNotificationChannel(notificationChannel)
        }
        val notificationBuilder: NotificationCompat.Builder=
            NotificationCompat.Builder(activity, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_menu)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentTitle(activity.getString(R.string.app_name))
                .setContentText("Gym Appointment...")
                .setContentIntent(pendingIntent)
                .setStyle(NotificationCompat.BigTextStyle()
                    .bigText("Reminder: Gym Appointment At  : ${binding.txtSelectedDate.text} from ${binding.txtSelectedTime.text}"))
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX)

        return notificationBuilder.build()
    }

}