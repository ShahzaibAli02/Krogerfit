package com.krger.krgerfit.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.krger.krgerfit.Interfaces.onItemSelectedListener
import com.krger.krgerfit.Model.TimeSlot
import com.krger.krgerfit.R
import com.krger.krgerfit.databinding.LytTimeslotBinding


class TimeSlotAdapter(val listData: MutableList<TimeSlot>, val context: Context,val onItemSelectedListener: onItemSelectedListener) : RecyclerView.Adapter<TimeSlotAdapter.ViewHolder>()
{

    @NonNull
    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, i: Int): ViewHolder {
        val binding: LytTimeslotBinding=LytTimeslotBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, i: Int)
    {


        val timeSlot=listData[holder.adapterPosition]

        if(timeSlot.count>=4)
        {
            holder.binding.btnTimeSlot.setBackgroundColor(context.resources.getColor(R.color.not_available))

            holder.binding.btnTimeSlot.isEnabled=false
        }
        else
        {
            holder.binding.btnTimeSlot.isEnabled=true
            holder.binding.btnTimeSlot.setBackgroundColor(context.resources.getColor(R.color.purple_200))
        }
        holder.binding.btnTimeSlot.text=timeSlot.time



        holder.binding.btnTimeSlot.setOnClickListener {
            onItemSelectedListener.onItemSelected(holder.adapterPosition)
        }

    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class ViewHolder(itemView: LytTimeslotBinding) : RecyclerView.ViewHolder(itemView.root)
    {
        var binding: LytTimeslotBinding=itemView
    }

}
