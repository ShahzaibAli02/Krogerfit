package com.krger.krgerfit.Model

import com.krger.krgerfit.Enums.AppointmentState
import com.krger.krgerfit.R
import com.krger.krgerfit.Utils.Util
import java.text.SimpleDateFormat
import java.util.*

data class Appointment(var id:String, val uid:String, val name:String,val imageLink:String, val time:Int, val date:Long,
                       var appointmentState: AppointmentState) {

    constructor() : this("", "", "","",0,0L, AppointmentState.PENDING) {}



    fun getColor():Int
    {
        if(appointmentState==AppointmentState.APPROVED)
            return R.color.color_accepted
        if(appointmentState==AppointmentState.PENDING)
            return R.color.color_pending
        if(appointmentState==AppointmentState.CANCELED)
            return R.color.color_rejected

        return  R.color.color_pending
    }
    fun getFormattedTime():String
    {
        return Util.getTimeSlotsList()[time].time
    }
}