package com.krger.krgerfit.Utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.google.gson.Gson
import com.krger.krgerfit.Model.User

class SharedPref {

    companion object{



        fun saveUser(context:Context,user:User)
        {

            val sharedPreferences=context.getSharedPreferences(context.packageName, MODE_PRIVATE)
            sharedPreferences.edit().putString("User", Gson().toJson(user)).apply()
        }

        fun saveToken(context:Context, token:String?)
        {

            val sharedPreferences=context.getSharedPreferences(context.packageName, MODE_PRIVATE)

            sharedPreferences.edit().putString("Token", token).apply()
        }
        fun removeToken(context:Context)
        {
            val sharedPreferences=context.getSharedPreferences(context.packageName, MODE_PRIVATE)
            sharedPreferences.edit().putString("Token", "").apply()
        }
        fun getoken(context:Context):String?
        {
            val sharedPreferences=context.getSharedPreferences(context.packageName, MODE_PRIVATE)
            return sharedPreferences.getString("Token", "")
        }
        fun removeUser(context:Context)
        {
            val sharedPreferences=context.getSharedPreferences(context.packageName, MODE_PRIVATE)
            sharedPreferences.edit().putString("User", "").apply()
        }
        fun getUser(context:Context):User?
        {

            val sharedPreferences=context.getSharedPreferences(context.packageName, MODE_PRIVATE)
            val strUser=sharedPreferences.getString("User", "")
            return if(strUser.isNullOrEmpty())
                null
            else
                Gson().fromJson(strUser,User::class.java)
        }


    }
}