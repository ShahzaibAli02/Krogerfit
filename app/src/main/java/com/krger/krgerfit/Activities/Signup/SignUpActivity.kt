package com.krger.krgerfit.Activities.Signup


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider

import com.krger.krgerfit.databinding.ActivitySignUpBinding


class SignUpActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var   signupViewModel :SignupViewModel;
    lateinit var  binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding=ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        initClickListeners()

    }

    private fun initClickListeners()
    {
        binding.btnSignup.setOnClickListener(this)
        binding.txtTermsAndConditions.setOnClickListener(this)
        binding.lytLogin.setOnClickListener(this)
        binding.imageAdd.setOnClickListener(this)
    }

    private fun initViewModel()
    {
        signupViewModel = ViewModelProvider(this, SignupViewModel.SignupViewModelFactory(this,binding)).get(SignupViewModel::class.java)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onClick(p0: View?) {
        signupViewModel.handleClick(p0!!.id)
    }



}