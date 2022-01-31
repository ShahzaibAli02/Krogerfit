package com.krger.krgerfit.Activities.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.krger.krgerfit.Activities.Client.ClientDashActivity
import com.krger.krgerfit.Activities.Signup.SignUpActivity
import com.krger.krgerfit.Activities.Signup.SignupViewModel
import com.krger.krgerfit.Model.Result
import com.krger.krgerfit.Model.User
import com.krger.krgerfit.R
import com.krger.krgerfit.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(), View.OnClickListener {


    private lateinit var binding: ActivityLoginBinding
    lateinit var   loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel();
        setClickListeners();

    }

    override fun onStart() {
        super.onStart()
        if(FirebaseAuth.getInstance().currentUser!=null)
        {
            finish()
            startActivity(Intent(this,ClientDashActivity::class.java))
        }
    }

    private fun setClickListeners() {
        binding.btnLogin.setOnClickListener(this)
        binding.lytSignup.setOnClickListener(this)
        binding.txtForgotPass.setOnClickListener(this)
    }
    private fun initViewModel()
    {
        loginViewModel = ViewModelProvider(this, LoginViewModel.LoginViewModelFactory(this,binding)).get(LoginViewModel::class.java)
    }

    override fun onClick(view: View?)
    {
        if(view==binding.lytSignup)
        {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
        if(view==binding.btnLogin)
        {
            loginViewModel.login()
        }
        if(view==binding.txtForgotPass)
        {
            loginViewModel.resetPass()
        }

    }


}