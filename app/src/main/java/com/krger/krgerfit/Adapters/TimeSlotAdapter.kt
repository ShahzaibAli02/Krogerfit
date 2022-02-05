package com.krger.krgerfit.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.krger.krgerfit.Activities.Client.BookAppointment.BookAppointmentViewModel
import com.krger.krgerfit.Interfaces.onItemSelectedListener
import com.krger.krgerfit.Model.TimeSlot
import com.krger.krgerfit.R
import com.krger.krgerfit.databinding.LytTimeslotBinding
import java.util.*


class TimeSlotAdapter(val listData: MutableList<TimeSlot>, val context: Context,val viewModel: BookAppointmentViewModel,val onItemSelectedListener: onItemSelectedListener) : RecyclerView.Adapter<TimeSlotAdapter.ViewHolder>()
{

    val calender: Calendar=Calendar.getInstance()
    var prevbtnSelected:com.google.android.material.button.MaterialButton?=null
    var  selectedID=-1
    @NonNull
    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, i: Int): ViewHolder {
        val binding: LytTimeslotBinding=LytTimeslotBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, i: Int)
    {


        val timeSlot=listData[holder.adapterPosition]

        var disabled=false
        if(viewModel.mYear==calender.get(Calendar.YEAR) && viewModel.mMonth==calender.get(Calendar.MONDAY) && viewModel.mDay==calender.get(Calendar.DAY_OF_MONTH))
        {
            if(timeSlot.id<calender.get(Calendar.HOUR_OF_DAY))
            {
                disabled=true
            }
        }


        if(timeSlot.count>=4 || disabled || !timeSlot.available)
        {
            holder.binding.btnTimeSlot.setBackgroundColor(context.resources.getColor(R.color.not_available))
            holder.binding.btnTimeSlot.isEnabled=false
            disabled=true
        }
        else
        {
            holder.binding.btnTimeSlot.isEnabled=true
            disabled=false
            holder.binding.btnTimeSlot.setBackgroundColor(context.resources.getColor(R.color.purple_200))
        }




        holder.binding.btnTimeSlot.text=timeSlot.time

        if(!disabled)
        {
            if(selectedID==holder.adapterPosition)
            {
                holder.binding.btnTimeSlot.setBackgroundColor(context.resources.getColor(R.color.selected))
            }
            else
            {
                holder.binding.btnTimeSlot.setBackgroundColor(context.resources.getColor(R.color.purple_200))
            }
        }


        holder.binding.btnTimeSlot.setOnClickListener {

            selectedID=holder.adapterPosition
            if(prevbtnSelected!=null)
            {
                prevbtnSelected?.setBackgroundColor(context.resources.getColor(R.color.purple_200))
            }
            prevbtnSelected=holder.binding.btnTimeSlot
            holder.binding.btnTimeSlot.setBackgroundColor(context.resources.getColor(R.color.selected))
            onItemSelectedListener.onItemSelected(holder.binding.btnTimeSlot,holder.adapterPosition)
        }

    }

    override fun getItemCount(): Int {
        return listData.size
    }

    fun update() {
        selectedID=-1
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: LytTimeslotBinding) : RecyclerView.ViewHolder(itemView.root)
    {
        var binding: LytTimeslotBinding=itemView
    }

}
