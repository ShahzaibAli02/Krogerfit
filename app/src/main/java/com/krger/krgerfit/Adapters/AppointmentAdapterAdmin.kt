package com.krger.krgerfit.Adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.krger.krgerfit.Enums.AppointmentState
import com.krger.krgerfit.Interfaces.onItemSelectedListener
import com.krger.krgerfit.Model.Appointment
import com.krger.krgerfit.R
import com.krger.krgerfit.Utils.Util
import com.krger.krgerfit.databinding.LytAppointment2Binding
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import com.squareup.picasso.Target


class AppointmentAdapterAdmin(val listData: MutableList<Appointment>, val context: Context, val onItemSelectedListener: onItemSelectedListener) : RecyclerView.Adapter<AppointmentAdapterAdmin.ViewHolder>()
{



    @NonNull
    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, i: Int): ViewHolder {
        val binding: LytAppointment2Binding=LytAppointment2Binding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, i: Int)
    {


        val appointment=listData[holder.adapterPosition]
        val binding=holder.binding

        if(appointment.imageLink.isNotEmpty())
        {
            Picasso.get().load(appointment.imageLink).placeholder(R.drawable.ic_loading).error(R.drawable.ic_avatar).into(binding.imgUser);
        }
        else
            Picasso.get().load(R.drawable.ic_avatar).into(binding.imgUser);


        binding.txtDate.text=Util.getFormattedDate(appointment.date)
        binding.txtTime.text=appointment.getFormattedTime()
        binding.txtName.text=appointment.name
        if(appointment.appointmentState==AppointmentState.CANCELED)
            binding.btnCancel.visibility=View.GONE
        else
        {
            binding.btnCancel.visibility=View.VISIBLE
            binding.btnCancel.setOnClickListener {


                AlertDialog.Builder(context)
                    .setMessage("Are you to cancel this appointment ?")
                    .setPositiveButton("Yes",object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            onItemSelectedListener.onItemSelected( binding.btnCancel,holder.adapterPosition)
                        }
                    })
                    .setNegativeButton("No",null).show()

            }



        }


        if(appointment.appointmentState==AppointmentState.APPROVED)
            binding.btnAdd.visibility=View.GONE
        else
        {
            binding.btnAdd.visibility=View.VISIBLE
            binding.btnAdd.setOnClickListener {


                AlertDialog.Builder(context)
                    .setMessage("Are you sure to approve this appointment ?")
                    .setPositiveButton("Yes",object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            onItemSelectedListener.onItemSelected( binding.btnAdd,holder.adapterPosition)
                        }
                    })
                    .setNegativeButton("No",null).show()

            }



        }





    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class ViewHolder(itemView: LytAppointment2Binding) : RecyclerView.ViewHolder(itemView.root)
    {
        var binding: LytAppointment2Binding=itemView
    }

}
