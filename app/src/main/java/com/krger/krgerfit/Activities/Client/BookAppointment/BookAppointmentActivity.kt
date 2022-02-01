package com.krger.krgerfit.Activities.Client.BookAppointment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.krger.krgerfit.Activities.Signup.SignupViewModel
import com.krger.krgerfit.Adapters.TimeSlotAdapter
import com.krger.krgerfit.Model.TimeSlot
import com.krger.krgerfit.R
import com.krger.krgerfit.Utils.SharedPref
import com.krger.krgerfit.databinding.ActivityBookAppointmentBinding
import com.squareup.picasso.Picasso
import java.util.*

class BookAppointmentActivity : AppCompatActivity(), View.OnClickListener {


    private lateinit var binding:ActivityBookAppointmentBinding
    lateinit var   bookAppointmentViewModel: BookAppointmentViewModel

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding=ActivityBookAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewModel()
        initClickListeners()
    }

    private fun initClickListeners()
    {

        binding.imgBack.setOnClickListener(this)
        binding.imgNext.setOnClickListener(this)
        binding.btnBookAp.setOnClickListener(this)

    }

    private fun initViewModel()
    {
        bookAppointmentViewModel = ViewModelProvider(this, BookAppointmentViewModel.BookAppointmentViewModelFactory(this,binding)).get(BookAppointmentViewModel::class.java)
    }

    override fun onClick(view: View?)
    {

        if(view==binding.imgBack)
        {
            bookAppointmentViewModel.handleScrollBack()
        }
        else
        if(view==binding.imgNext)
        {
            bookAppointmentViewModel.handleScrollNext()
        }
        else
        if(view==binding.btnBookAp)
        {
            bookAppointmentViewModel.bookApp()
        }
    }


}