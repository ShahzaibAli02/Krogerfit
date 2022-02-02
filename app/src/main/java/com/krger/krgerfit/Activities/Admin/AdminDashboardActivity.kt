package com.krger.krgerfit.Activities.Admin

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.krger.krgerfit.databinding.ActivityAdminDashboardBinding

class AdminDashboardActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityAdminDashboardBinding

    lateinit var   viewModel: AdminDashboardViewModel
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding=ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setclickListeners()
        initViewModel()
        initHeader()
    }



    private fun initHeader() {
        viewModel.setvalsonHeader()
    }



    private fun initViewModel()
    {
        viewModel = ViewModelProvider(this, AdminDashboardViewModel.AdminDashboardViewModelFactory(this,binding)).get(
            AdminDashboardViewModel::class.java)
    }


    override fun onResume() {
        super.onResume()
    }
    private fun setclickListeners() {
      //  binding.btnBookAp.setOnClickListener(this)
    }

    override fun onClick(view: View?)
    {
       /* if(view==binding.btnBookAp)
        {
            startActivity(Intent(this, BookAppointmentActivity::class.java))
        }

        */
    }


}