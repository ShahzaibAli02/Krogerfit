package com.krger.krgerfit.Model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import java.text.SimpleDateFormat
import java.util.*

data class User(val uid:String,val imageLink:String,val userName:String,val email:String,var admin:Boolean,val date:Long)
{

    constructor() : this("", "", "","",false,0L) {}

}
