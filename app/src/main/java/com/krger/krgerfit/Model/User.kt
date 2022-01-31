package com.krger.krgerfit.Model

import com.google.firebase.firestore.Exclude

data class User(val uid:String,val imageLink:String,val userName:String,val email:String,val isAdmin:Boolean=false)
{

    constructor() : this("", "", "","",false) {}
}
