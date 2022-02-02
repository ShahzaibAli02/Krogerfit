package com.krger.krgerfit.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.krger.krgerfit.Activities.Login.LoginActivity
import com.krger.krgerfit.Utils.SharedPref
import com.krger.krgerfit.Utils.Util
import com.krger.krgerfit.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity(), View.OnClickListener {


    private  lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setClickListeners()
    }

    private fun setClickListeners() {
        binding.cardLogout.setOnClickListener(this)
        Util.putValsOnHeader(this,"Profile",binding.lytHeader.txtHeaderTitle,binding.lytHeader.imgUser,binding.lytHeader.imgMenu)
    }

    override fun onClick(p0: View?) {

        ActivityCompat.finishAffinity(this)
        finish()
        SharedPref.removeToken(this)
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this,LoginActivity::class.java))

    }
}