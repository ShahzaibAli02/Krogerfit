package com.krger.krgerfit

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Constants
{

    companion object{

        val EXTRA_KEY_DATE="EXTRA_KEY_DATE"
        val EXTRA_KEY_TIME="EXTRA_KEY_TIME"

        //MODIFY TO HOW MANY HOURS BEFORE ACTUAL TIME  YOU WANT NOTIFICATION
        final val HOURS_BEFORE:Long=4


        //DONT MODIFY
        final  val ALARM_BEFORE: Long=(HOURS_BEFORE*60*60*1000)
        final val FIREBASE_REF_USERS = Firebase.firestore.collection("Users")
        final val FIREBASE_REF_APPOINTMENTS = Firebase.firestore.collection("Appointments")
    }
}