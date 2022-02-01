package com.krger.krgerfit.Utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.Patterns
import android.widget.*
import androidx.fragment.app.FragmentActivity
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.krger.krgerfit.Interfaces.ImageListener
import com.krger.krgerfit.Interfaces.ImageUploadListener
import com.krger.krgerfit.R
import com.squareup.picasso.Picasso
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

        fun getDate(year:Int,month:Int,date:Int): Long {
            val calendar=Calendar.getInstance()
            calendar.set(Calendar.YEAR,year)
            calendar.set(Calendar.DAY_OF_MONTH,date)
            calendar.set(Calendar.MONTH,month)

            calendar.set(Calendar.HOUR,0)
            calendar.set(Calendar.MINUTE,0)
            calendar.set(Calendar.MILLISECOND,0)
            calendar.set(Calendar.SECOND,0)
            return calendar.timeInMillis
        }

        fun isValidEmail(text: String?): Boolean
        {
            return  Patterns.EMAIL_ADDRESS.matcher(text).matches()
        }


        fun  putValsOnHeader(context: Context,text:String,txtHeaderTitle:TextView,imageView:ImageView)
        {
            txtHeaderTitle.text=text
            val user=SharedPref.getUser(context);
            if(user!=null && !user.imageLink.isNullOrEmpty())
            {
                Picasso.get().load(user.imageLink).resize(40,40).into( imageView)
            }
        }

    }

}