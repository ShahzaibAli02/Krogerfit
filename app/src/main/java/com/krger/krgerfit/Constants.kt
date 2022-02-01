package com.krger.krgerfit

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Constants
{

    companion object{

        final val FIREBASE_REF_USERS = Firebase.firestore.collection("Users")
        final val FIREBASE_REF_APPOINTMENTS = Firebase.firestore.collection("Appointments")
    }
}