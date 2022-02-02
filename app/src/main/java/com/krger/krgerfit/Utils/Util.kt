package com.krger.krgerfit.Utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.Patterns
import android.widget.*
import androidx.fragment.app.FragmentActivity
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.github.ybq.android.spinkit.style.Wave
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.krger.krgerfit.Activities.ProfileActivity
import com.krger.krgerfit.Interfaces.ImageListener
import com.krger.krgerfit.Interfaces.ImageUploadListener
import com.krger.krgerfit.Model.Appointment
import com.krger.krgerfit.Model.TimeSlot
import com.krger.krgerfit.R
import com.squareup.picasso.Picasso
import com.vansuita.pickimage.dialog.PickImageDialog
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class Util {


    companion object
    {
        fun getProgressDialog(context: Context): AlertDialog?
        {
            val builder=AlertDialog.Builder(context)
            val progressBar=ProgressBar(context)
            val doubleBounce: Sprite=Wave()
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


        fun  putValsOnHeader(context: Context,
                             text: String,
                             txtHeaderTitle: TextView,
                             imageView: ImageView,
                             imgMenu: ImageView)
        {
            txtHeaderTitle.text=text
            val user=SharedPref.getUser(context);

            imgMenu.setOnClickListener{
                context.startActivity(Intent(context,ProfileActivity::class.java))
            }
            imageView.setOnClickListener {
                context.startActivity(Intent(context,ProfileActivity::class.java))
            }
            if(user!=null && !user.imageLink.isNullOrEmpty())
            {
                Picasso.get().load(user.imageLink).resize(40,40).into( imageView)
            }
        }
        fun getFormattedDate(date: Long):String
        {
            return DateFormat.getDateInstance(DateFormat.SHORT).format(Date(date))
                //  return SimpleDateFormat("dd-mm-yyyy",Locale.ENGLISH).format()
        }



        fun getTimeSlotsList(): MutableList<TimeSlot> {

            val timeSlots:MutableList<TimeSlot> = ArrayList()
            timeSlots.add(TimeSlot(0,"0:00 AM - 1:00 AM", R.color.purple_200,0,true))
            timeSlots.add(TimeSlot(1,"1:00 AM - 2:00 AM", R.color.purple_200,0,true))
            timeSlots.add(TimeSlot(2,"2:00 AM - 3:00 AM", R.color.purple_200,0,true))
            timeSlots.add(TimeSlot(3,"3:00 AM - 4:00 AM", R.color.purple_200,0,true))
            timeSlots.add(TimeSlot(4,"4:00 AM - 5:00 AM", R.color.purple_200,0,true))
            timeSlots.add(TimeSlot(5,"5:00 AM - 6:00 AM", R.color.purple_200,0,true))
            timeSlots.add(TimeSlot(6,"6:00 AM - 7:00 AM", R.color.purple_200,0,true))
            timeSlots.add(TimeSlot(7,"7:00 AM - 8:00 AM", R.color.purple_200,0,true))
            timeSlots.add(TimeSlot(8,"8:00 AM - 9:00 AM", R.color.purple_200,0,true))
            timeSlots.add(TimeSlot(9,"9:00 AM - 10:00 AM", R.color.purple_200,0,true))
            timeSlots.add(TimeSlot(10,"10:00 AM - 11:00 AM", R.color.purple_200,0,true))
            timeSlots.add(TimeSlot(11,"11:00 AM - 12:00 PM", R.color.purple_200,0,true))

            timeSlots.add(TimeSlot(12,"12:00 PM - 1:00 PM", R.color.purple_200,0,true))
            timeSlots.add(TimeSlot(13,"1:00 PM - 2:00 PM", R.color.purple_200,0,true))
            timeSlots.add(TimeSlot(14,"2:00 PM - 3:00 PM", R.color.purple_200,0,true))
            timeSlots.add(TimeSlot(15,"3:00 PM - 4:00 PM", R.color.purple_200,0,true))
            timeSlots.add(TimeSlot(16,"4:00 PM - 5:00 PM", R.color.purple_200,0,true))
            timeSlots.add(TimeSlot(17,"5:00 PM - 6:00 PM", R.color.purple_200,0,true))
            timeSlots.add(TimeSlot(18,"6:00 PM - 7:00 PM", R.color.purple_200,0,true))
            timeSlots.add(TimeSlot(19,"7:00 PM - 8:00 PM", R.color.purple_200,0,true))
            timeSlots.add(TimeSlot(20,"8:00 PM - 9:00 PM", R.color.purple_200,0,true))
            timeSlots.add(TimeSlot(21,"9:00 PM - 10:00 PM", R.color.purple_200,0,true))
            timeSlots.add(TimeSlot(22,"10:00 PM - 11:00 PM", R.color.purple_200,0,true))
            timeSlots.add(TimeSlot(23,"11:00 PM - 12:00 AM", R.color.purple_200,0,true))

            return timeSlots

        }
    }

}