package com.krger.krgerfit.Utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.Patterns
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.krger.krgerfit.Interfaces.ImageListener
import com.krger.krgerfit.Interfaces.ImageUploadListener
import com.krger.krgerfit.R
import com.vansuita.pickimage.dialog.PickImageDialog
import java.util.*
import java.util.regex.Pattern

class Util {


    companion object
    {
        fun getProgressDialog(context: Context): AlertDialog?
        {
            val builder=AlertDialog.Builder(context)
            val progressBar=ProgressBar(context)
            val doubleBounce: Sprite=DoubleBounce()
            doubleBounce.setColor(context.resources.getColor(R.color.purple_200))
            progressBar.indeterminateDrawable=doubleBounce
            builder.setView(progressBar)
            builder.setCancelable(false)
            val alertDialog=builder.create()
            alertDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            alertDialog.show()
            return alertDialog
        }

        fun getDialog(context: Context?, layout: Int): Dialog? {
            val dialog=Dialog(context!!)
            dialog.setContentView(layout)
            val window=dialog.window
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            return dialog
        }


        fun uploadImage(uri: Uri?, imageUploadListener: ImageUploadListener) {
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


        fun showMessage(activity: Activity?, message : String?)
        {
            Toast.makeText(activity,message,Toast.LENGTH_LONG).show()
        }
        fun readImageFromGallery(activity: Activity?, imageListener: ImageListener)
        {
            PickImageDialog.build { r ->
                if (r.error == null) {
                    imageListener.onImageLoaded(false, r.uri, r.bitmap)
                } else {
                    imageListener.onImageLoaded(true, null, null)
                }
            }.show(activity as FragmentActivity?)
        }

        fun isValidEmail(text: String?): Boolean
        {
            return  Patterns.EMAIL_ADDRESS.matcher(text).matches()
        }
    }

}