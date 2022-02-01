package com.krger.krgerfit.Model

import com.krger.krgerfit.AppointmentState

data class Appointment(var id:String,val uid:String,val name:String,val time:Int,val date:Long,val appointmentState: AppointmentState) {

    constructor() : this("", "", "",0,0,AppointmentState.PENDING) {}
}