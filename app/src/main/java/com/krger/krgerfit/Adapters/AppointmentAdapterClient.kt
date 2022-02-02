package com.krger.krgerfit.Adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.krger.krgerfit.Enums.AppointmentState
import com.krger.krgerfit.Interfaces.onItemSelectedListener
import com.krger.krgerfit.Model.Appointment
import com.krger.krgerfit.Utils.Util
import com.krger.krgerfit.databinding.LytAppointment1Binding


class AppointmentAdapterClient(val listData: MutableList<Appointment>, val context: Context, val onItemSelectedListener: onItemSelectedListener) : RecyclerView.Adapter<AppointmentAdapterClient.ViewHolder>()
{



    @NonNull
    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, i: Int): ViewHolder {
        val binding: LytAppointment1Binding=LytAppointment1Binding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, i: Int)
    {


        val appointment=listData[holder.adapterPosition]
        val binding=holder.binding

        binding.txtDate.text=Util.getFormattedDate(appointment.date)
        binding.txtTime.text=appointment.getFormattedTime()
        binding.txtStatus.text=appointment.appointmentState.toString()
        binding.txtStatus.setTextColor(context.resources.getColor(appointment.getColor()))
        if(appointment.appointmentState==AppointmentState.CANCELED)
            binding.btnCancel.visibility=View.GONE
        else
        {
            binding.btnCancel.visibility=View.VISIBLE
            binding.btnCancel.setOnClickListener {


                AlertDialog.Builder(context)
                    .setMessage("Are you sure  to cancel this appointment ?")
                    .setPositiveButton("Yes",object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            onItemSelectedListener.onItemSelected( binding.btnCancel,holder.adapterPosition)
                        }
                    })
                    .setNegativeButton("No",null).show()

            }
        }





    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class ViewHolder(itemView: LytAppointment1Binding) : RecyclerView.ViewHolder(itemView.root)
    {
        var binding: LytAppointment1Binding=itemView
    }

}
