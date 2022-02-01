package com.krger.krgerfit.Activities.Client

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.krger.krgerfit.Activities.Client.BookAppointment.BookAppointmentActivity
import com.krger.krgerfit.Utils.SharedPref
import com.krger.krgerfit.databinding.ActivityClientDashBinding
import com.squareup.picasso.Picasso

class ClientDashActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding:ActivityClientDashBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding=ActivityClientDashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lytHeader.txtHeaderTitle.text="DashBoard"
        val user=SharedPref.getUser(this);
        if(user!=null && !user.imageLink.isNullOrEmpty())
        {
            Picasso.get().load(user.imageLink).into( binding.lytHeader.imgUser)
        }
        setclickListeners()
    }

    private fun setclickListeners() {
        binding.btnBookAp.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        if(view==binding.btnBookAp)
        {
            startActivity(Intent(this, BookAppointmentActivity::class.java))
        }
    }


}