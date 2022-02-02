package com.krger.krgerfit.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.krger.krgerfit.Activities.Admin.AdminDashboardActivity
import com.krger.krgerfit.Activities.Client.ClientDashBoard.ClientDashActivity
import com.krger.krgerfit.Activities.Login.LoginActivity
import com.krger.krgerfit.DataBaseOperations
import com.krger.krgerfit.Interfaces.onDataBaseResult
import com.krger.krgerfit.Model.User
import com.krger.krgerfit.Model.mResult
import com.krger.krgerfit.R
import com.krger.krgerfit.Utils.SharedPref
import com.krger.krgerfit.Utils.Util

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if(SharedPref.getoken(this)!!.isNotEmpty())
        {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(SharedPref.getUser(this)!!.email.trim(),SharedPref.getoken(this)!!).addOnCompleteListener {

                if(it.isSuccessful)
                {
                    DataBaseOperations.authenticate(FirebaseAuth.getInstance().currentUser!!.uid,object :
                        onDataBaseResult<Task<DocumentSnapshot>>
                    {
                        override fun onResult(result: mResult<Task<DocumentSnapshot>>) {
                            finish()
                            val it=result.getResult()!!

                            if(it.isSuccessful)
                            {
                                val result=it.result


                                val user=result!!.toObject(User::class.java)
                                if(user==null)
                                {
                                    Util.showMessage(this@SplashActivity,"Your Account Removed By Admin")
                                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                                    return
                                }
                                SharedPref.saveUser(this@SplashActivity,user!!)
                                if(user.admin)
                                {
                                    startActivity(Intent( this@SplashActivity, AdminDashboardActivity::class.java))
                                }
                                else
                                {
                                    startActivity(Intent( this@SplashActivity, ClientDashActivity::class.java))
                                }


                            }
                            else
                            {
                                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                            }

                        }
                    })

                }
                else
                {
                    finish()
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                }
            }

        }
        else
        {
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }
}