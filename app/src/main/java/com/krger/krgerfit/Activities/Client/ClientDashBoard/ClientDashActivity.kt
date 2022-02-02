package com.krger.krgerfit.Activities.Client.ClientDashBoard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.krger.krgerfit.Activities.Client.BookAppointment.BookAppointmentActivity
import com.krger.krgerfit.Activities.Client.BookAppointment.BookAppointmentViewModel
import com.krger.krgerfit.Utils.SharedPref
import com.krger.krgerfit.databinding.ActivityClientDashBinding
import com.squareup.picasso.Picasso

class ClientDashActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding:ActivityClientDashBinding

    lateinit var   viewModel: ClientDashboardViewModel
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding=ActivityClientDashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setclickListeners()
        initViewModel()
        initHeader()
        initAppointmentsRecyclerView()

    }

    private fun initHeader() {
        viewModel.setvalsonHeader()
    }

    private fun initAppointmentsRecyclerView() {
        viewModel.initRecyclerView()
    }

    private fun initViewModel()
    {
        viewModel = ViewModelProvider(this, ClientDashboardViewModel.ClientDashboardViewModelFactory(this,binding)).get(ClientDashboardViewModel::class.java)
    }


    override fun onResume() {
        super.onResume()
        viewModel.loadAppointments()
    }
    private fun setclickListeners() {
        binding.btnBookAp.setOnClickListener(this)
    }

    override fun onClick(view: View?)
    {
        if(view==binding.btnBookAp)
        {
            startActivity(Intent(this, BookAppointmentActivity::class.java))
        }
    }


}