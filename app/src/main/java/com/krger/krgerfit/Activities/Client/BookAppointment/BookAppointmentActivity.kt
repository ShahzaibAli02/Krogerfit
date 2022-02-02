package com.krger.krgerfit.Activities.Client.BookAppointment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.krger.krgerfit.databinding.ActivityBookAppointmentBinding

class BookAppointmentActivity : AppCompatActivity(), View.OnClickListener {


    private lateinit var binding:ActivityBookAppointmentBinding
    lateinit var   bookAppointmentViewModel: BookAppointmentViewModel

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding=ActivityBookAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewModel()
        initHeader()
        initClickListeners()
    }

    private fun initHeader() {
        bookAppointmentViewModel.setvalsonHeader()
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