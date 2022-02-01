package com.krger.krgerfit

import android.net.Uri
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.krger.krgerfit.Interfaces.ImageUploadListener
import com.krger.krgerfit.Interfaces.onDataBaseResult
import com.krger.krgerfit.Model.Appointment
import com.krger.krgerfit.Model.User
import com.krger.krgerfit.Model.mResult
import com.krger.krgerfit.Utils.Util
import java.util.*

class DataBaseOperations
{
    companion object{



        fun uploadImage(uri: Uri?, imageUploadListener: ImageUploadListener)
        {
            if (uri == null) {
                imageUploadListener.onUpload(true, "No Image Provided", "")
                return
            }
            val storageRef: StorageReference=FirebaseStorage.getInstance().getReference()
            val ImagePath=
                String.format("images/%s.jpg", Calendar.getInstance().timeInMillis.toString())
            val imageRef: StorageReference=storageRef.child(ImagePath)

            imageRef.putFile(uri).addOnCompleteListener(OnCompleteListener {
                if (it.isSuccessful)
                {
                    imageRef.downloadUrl
                        .addOnCompleteListener(OnCompleteListener<Uri>
                        { task ->
                            if (task.isSuccessful)
                            {
                                imageUploadListener.onUpload(false,
                                    ImagePath,
                                    task.result.toString()
                                )
                            } else {
                                imageUploadListener.onUpload(true, task.exception!!.message, "")
                            }
                        })
                } else {
                    imageUploadListener.onUpload(true, it.exception!!.message, "")
                }

            })

        }



        fun addUser(uid:String,url:String,email:String,pass:String,onDataBaseResult: onDataBaseResult<Task<Void>>)
        {
            val user=User(uid,url,email,pass,false)
            Constants.FIREBASE_REF_USERS.document(user.uid).set(user).addOnCompleteListener {

                val result=mResult<Task<Void>>()
                result.setResult(it)
                result.isSuccess=it.isSuccessful
                onDataBaseResult.onResult(result)
            }
        }


        fun authenticate(uid:String,onDataBaseResult: onDataBaseResult<Task<DocumentSnapshot>>)
        {
            val db = Constants.FIREBASE_REF_USERS
            db.document(uid).get().addOnCompleteListener {


                val mResult=mResult<Task<DocumentSnapshot>>()
                mResult.setResult(it)
                mResult.isSuccess=it.isSuccessful
                onDataBaseResult.onResult(mResult)




            }

        }

        fun getUserAppointmentsCount(uid: String,onDbResult: onDataBaseResult<Task<QuerySnapshot>>)
        {
            val db = Constants.FIREBASE_REF_APPOINTMENTS
            val calendar=Calendar.getInstance()
            val currentDate=Util.getDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH))
            val whereEqualTo=db.whereEqualTo("uid",uid).whereGreaterThanOrEqualTo("date",currentDate)
            whereEqualTo.get().addOnCompleteListener {
                val mResult=mResult<Task<QuerySnapshot>>()
                mResult.setResult(it)
                mResult.isSuccess=it.isSuccessful
                onDbResult.onResult(mResult)
            }

        }


        fun getAppointmentsAtDate(date: Long,onDbResult: onDataBaseResult<Task<QuerySnapshot>>)
        {
            val db = Constants.FIREBASE_REF_APPOINTMENTS
           val whereEqualTo=db.whereEqualTo("date",date)
            whereEqualTo.get().addOnCompleteListener {
                val mResult=mResult<Task<QuerySnapshot>>()
                mResult.setResult(it)
                mResult.isSuccess=it.isSuccessful
                onDbResult.onResult(mResult)
            }

        }



        fun addAppointment(appointment: Appointment,onDataBaseResult: onDataBaseResult<Task<Void>>)
        {
            val db = Constants.FIREBASE_REF_APPOINTMENTS
            val doc=db.document()
            appointment.id=doc.id
            doc.set(appointment).addOnCompleteListener {
                val result=mResult<Task<Void>>()
                result.setResult(it)
                result.isSuccess=it.isSuccessful
                onDataBaseResult.onResult(result)
            }
        }


    }
}