package com.krger.krgerfit.Activities.Login

import android.app.Activity
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.krger.krgerfit.Activities.Admin.AdminDashboardActivity
import com.krger.krgerfit.Activities.Client.ClientDashBoard.ClientDashActivity
import com.krger.krgerfit.DataBaseOperations
import com.krger.krgerfit.Interfaces.onDataBaseResult
import com.krger.krgerfit.Model.User
import com.krger.krgerfit.Model.mResult
import com.krger.krgerfit.R
import com.krger.krgerfit.Utils.SharedPref
import com.krger.krgerfit.Utils.Util
import com.krger.krgerfit.databinding.ActivityLoginBinding

class LoginViewModel (private  val activity: Activity, private  val binding: ActivityLoginBinding) : ViewModel()
{



    private fun isDataValid(): Boolean {


        for (editext in listOf<EditText>(binding.editTextEmail,binding.editTextPass))
        {
            if(editext.text.isEmpty())
            {
                editext.error="Required Field"
                editext.requestFocus()
                return  false
            }

        }
        if(!Util.isValidEmail(binding.editTextEmail.text.toString()))
        {
            binding.editTextEmail.error="Invalid Email!!"
            binding.editTextEmail.requestFocus()
            return false
        }
        return true
    }

    fun login()
    {

        if(isDataValid())
        {

            val progressDialog=Util.getProgressDialog(activity)
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(binding.editTextEmail.text.toString().trim(),binding.editTextPass.text.toString())
                .addOnCompleteListener {


                    if(it.isSuccessful)
                    {

                        DataBaseOperations.authenticate(FirebaseAuth.getInstance().currentUser!!.uid,object :onDataBaseResult<Task<DocumentSnapshot>>
                        {
                            override fun onResult(result: mResult<Task<DocumentSnapshot>>) {

                                val it=result.getResult()!!

                                progressDialog!!.dismiss()
                                if(it.isSuccessful)
                                {
                                    val result=it.result
                                    val user=result!!.toObject(User::class.java)
                                    if(user==null)
                                    {
                                        Util.showMessage(activity,"Your Account Removed By Admin")
                                        return
                                    }
                                    SharedPref.saveUser(activity,user!!)
                                    Util.showMessage(activity,"Welcome : "+ user.userName)

                                    activity.finish()
                                    if(user.admin)
                                    {
                                        activity.startActivity(Intent( activity,
                                            AdminDashboardActivity::class.java))
                                    }
                                    else
                                    {
                                        activity.startActivity(Intent( activity,
                                            ClientDashActivity::class.java))
                                    }


                                    SharedPref.saveToken(activity,binding.editTextPass.text.toString())
                                }
                                else
                                {
                                    Util.showMessage(activity," Error : "+it.exception!!.message)
                                }

                            }
                        })



                    }
                    else
                    {
                        progressDialog!!.dismiss()
                        Util.showMessage(activity,it.exception!!.message)
                    }
                }
        }
    }

    fun resetPass()
    {

        val dialog=Util.getDialog(activity, R.layout.lyt_forgetpass)
        dialog!!.show()

        val editTextEmail=dialog.findViewById<EditText>(R.id.editTextEmail)
        val btnReset=dialog.findViewById<Button>(R.id.btnReset)

        btnReset.setOnClickListener {

            if (editTextEmail.text.isEmpty())
            {
                editTextEmail.error="Put Your Email"
                editTextEmail.requestFocus()

            }
            else
            if (! Util.isValidEmail(editTextEmail.text.toString()))
            {
                editTextEmail.error="Invalid Email"
                editTextEmail.requestFocus()
            }
            else
            {
                dialog.dismiss()


                val progressDialog=Util.getProgressDialog(activity)
                FirebaseAuth.getInstance().sendPasswordResetEmail(editTextEmail.text.toString()).addOnCompleteListener {

                    progressDialog!!.dismiss()
                    if(it.isSuccessful)
                    {
                        Util.showMessage(activity,"Password Reset Email has Been Sent Check you mail box")
                    }
                    else
                    {
                        Util.showMessage(activity,it.exception!!.message)
                    }

                };
            }

        }


    }


    class LoginViewModelFactory(private val activity: Activity, private  val  binding: ActivityLoginBinding) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(
                    activity,binding
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }


}