package com.krger.krgerfit.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.krger.krgerfit.Model.TimeSlot
import com.krger.krgerfit.databinding.LytTimeslotBinding


class TimeSlotAdapter(val listData: MutableList<TimeSlot>, val context: Context) : RecyclerView.Adapter<TimeSlotAdapter.ViewHolder>()
{

    @NonNull
    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, i: Int): ViewHolder {
        val binding: LytTimeslotBinding=LytTimeslotBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, i: Int)
    {
        holder.binding.btnTimeSlot.text=listData[holder.adapterPosition].time
            //   holder.binding.btnTimeSlot.background

    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class ViewHolder(itemView: LytTimeslotBinding) : RecyclerView.ViewHolder(itemView.root)
    {
        var binding: LytTimeslotBinding=itemView
    }

}
