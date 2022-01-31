package com.krger.krgerfit.Activities.Client

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.krger.krgerfit.Adapters.TimeSlotAdapter
import com.krger.krgerfit.Model.TimeSlot
import com.krger.krgerfit.R
import com.krger.krgerfit.Utils.SharedPref
import com.krger.krgerfit.databinding.ActivityBookAppointmentBinding
import com.squareup.picasso.Picasso

class BookAppointmentActivity : AppCompatActivity() {


    private lateinit var binding:ActivityBookAppointmentBinding
    private lateinit var timeSlots:MutableList<TimeSlot>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityBookAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lytHeader.txtHeaderTitle.text="Book Appointment"
        val user=SharedPref.getUser(this);
        if(user!=null && !user.imageLink.isNullOrEmpty())
        {
            Picasso.get().load(user.imageLink).into( binding.lytHeader.imgUser)
        }


        loadList()
        val horizontalLayoutManagaer=LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
        binding.recyclerView.layoutManager=horizontalLayoutManagaer
        binding.recyclerView.adapter=TimeSlotAdapter(timeSlots,this)

        binding.imgBack.setOnClickListener {

            if (horizontalLayoutManagaer.findFirstVisibleItemPosition() > 0) {
                binding.recyclerView.smoothScrollToPosition(horizontalLayoutManagaer.findFirstVisibleItemPosition() - 1);
            } else {
                binding.recyclerView.smoothScrollToPosition(0);
            }

        }
        binding.imgNext.setOnClickListener {

            binding.recyclerView.smoothScrollToPosition(horizontalLayoutManagaer.findLastVisibleItemPosition() + 1);
        }
    }

    fun loadList()
    {


        timeSlots=ArrayList()
        timeSlots.add(TimeSlot(0,"0:00 AM - 1:00 AM", R.color.purple_200,true));
        timeSlots.add(TimeSlot(1,"1:00 AM - 2:00 AM", R.color.purple_200,true));
        timeSlots.add(TimeSlot(2,"2:00 AM - 3:00 AM", R.color.purple_200,true));
        timeSlots.add(TimeSlot(3,"3:00 AM - 4:00 AM", R.color.purple_200,true));
        timeSlots.add(TimeSlot(4,"4:00 AM - 5:00 AM", R.color.purple_200,true));
        timeSlots.add(TimeSlot(5,"5:00 AM - 6:00 AM", R.color.purple_200,true));
        timeSlots.add(TimeSlot(6,"6:00 AM - 7:00 AM", R.color.purple_200,true));
        timeSlots.add(TimeSlot(7,"7:00 AM - 8:00 AM", R.color.purple_200,true));
        timeSlots.add(TimeSlot(8,"8:00 AM - 9:00 AM", R.color.purple_200,true));
        timeSlots.add(TimeSlot(9,"9:00 AM - 10:00 AM", R.color.purple_200,true));
        timeSlots.add(TimeSlot(10,"10:00 AM - 11:00 AM", R.color.purple_200,true));
        timeSlots.add(TimeSlot(11,"11:00 AM - 12:00 PM", R.color.purple_200,true));

        timeSlots.add(TimeSlot(12,"12:00 PM - 1:00 PM", R.color.purple_200,true));
        timeSlots.add(TimeSlot(13,"1:00 PM - 2:00 PM", R.color.purple_200,true));
        timeSlots.add(TimeSlot(14,"2:00 PM - 3:00 PM", R.color.purple_200,true));
        timeSlots.add(TimeSlot(15,"3:00 PM - 4:00 PM", R.color.purple_200,true));
        timeSlots.add(TimeSlot(16,"4:00 PM - 5:00 PM", R.color.purple_200,true));
        timeSlots.add(TimeSlot(17,"5:00 PM - 6:00 PM", R.color.purple_200,true));
        timeSlots.add(TimeSlot(18,"6:00 PM - 7:00 PM", R.color.purple_200,true));
        timeSlots.add(TimeSlot(19,"7:00 PM - 8:00 PM", R.color.purple_200,true));
        timeSlots.add(TimeSlot(20,"8:00 PM - 9:00 PM", R.color.purple_200,true));
        timeSlots.add(TimeSlot(21,"9:00 PM - 10:00 PM", R.color.purple_200,true));
        timeSlots.add(TimeSlot(22,"10:00 PM - 11:00 PM", R.color.purple_200,true));
        timeSlots.add(TimeSlot(23,"11:00 PM - 12:00 AM", R.color.purple_200,true));


    }


}