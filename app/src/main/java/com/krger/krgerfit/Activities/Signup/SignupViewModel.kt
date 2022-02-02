package com.krger.krgerfit.Activities.Signup

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.widget.EditText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.krger.krgerfit.DataBaseOperations
import com.krger.krgerfit.Interfaces.ImageListener
import com.krger.krgerfit.Interfaces.ImageUploadListener
import com.krger.krgerfit.Interfaces.onDataBaseResult
import com.krger.krgerfit.Model.mResult
import com.krger.krgerfit.R
import com.krger.krgerfit.Utils.Util
import com.krger.krgerfit.databinding.ActivitySignUpBinding

class SignupViewModel( private  val activity: Activity,private  val binding: ActivitySignUpBinding) : ViewModel()
{

     var muri: Uri?=null


    fun handleClick(id: Int)
    {
        if(id== R.id.imageAdd)
        {
            loadImage()
        }
        if(id== R.id.txtLogin)
        {
            activity.finish()
        }
        if(id== R.id.btnSignup)
        {
            if(isDataValid())
            {
                uploadData();
            }

        }

    }
    private fun uploadData()
    {

        val progressDialog=Util.getProgressDialog(activity)
        DataBaseOperations.uploadImage(muri,object :ImageUploadListener{
            override fun onUpload(error: Boolean, Message: String?, url: String?)
            {
                if (error)
                {
                    progressDialog!!.dismiss()
                    Util.showMessage(activity,Message)
                }
                else
                {

                    FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(binding.editTextEmail.text.toString(),binding.editTextPass.text.toString())
                        .addOnCompleteListener {

                            if(it.isSuccessful)
                            {


                                DataBaseOperations.addUser(FirebaseAuth.getInstance().currentUser!!.uid,url!!,binding.editTextName.text.toString(),binding.editTextEmail.text.toString(),onDataBaseResult =object :
                                    onDataBaseResult<Task<Void>> {
                                    override fun onResult(task: mResult<Task<Void>>) {

                                        progressDialog!!.dismiss()
                                        val result1=task.getResult()
                                        if(result1!!.isSuccessful)
                                        {
                                            activity.finish()
                                            Util.showMessage(activity,"Account Created Successfully");
                                        }
                                        else
                                        {
                                            Util.showMessage(activity,"Error : "+result1.exception!!.message);
                                        }

                                    }
                                })

                            }
                            else
                            {
                                progressDialog!!.dismiss()
                                Util.showMessage(activity,"Error : "+it.exception!!.message);
                            }

                        }





                }
            }

        })

    }

    private fun isDataValid(): Boolean {

        for (editext in listOf<EditText>(binding.editTextName,binding.editTextEmail,binding.editTextPass))
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

    private fun loadImage() {
        Util.readImageFromGallery(activity,object :ImageListener{
            override fun onImageLoaded(error: Boolean, uri: Uri?, bitmap: Bitmap?)
            {
                if(error)
                {
                    Util.showMessage(activity,"Failed To Load Image")
                }
                else
                {
                    muri=uri!!
                    binding.imageUser.setImageBitmap(bitmap)
                }
            }
        })
    }


    class SignupViewModelFactory(private val activity: Activity,private  val  binding: ActivitySignUpBinding) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SignupViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SignupViewModel(
                    activity,binding
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }


}