package com.saarit.templedocumentcollector.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.saarit.templedocumentcollector.R
import com.saarit.templedocumentcollector.databinding.ActivityLoginBinding
import com.saarit.templedocumentcollector.utils.Util
import com.saarit.templedocumentcollector.viewModels.Login_ViewModel

class Login_Activity : AppCompatActivity() {

    private val TAG = Login_Activity::class.java.simpleName

    lateinit var viewmodel:Login_ViewModel
    lateinit var binding:ActivityLoginBinding


     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Util.log(TAG,"from OnCreate()...")

         setupBindings(savedInstanceState)

         setUpObservers()
    }

    private fun setupBindings(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        viewmodel = ViewModelProviders.of(this).get(Login_ViewModel::class.java)

        if(savedInstanceState == null){
            viewmodel.init()
        }

        binding.viewmodel = viewmodel

    }

    private fun setUpObservers() {
        viewmodel.observeServerResponse().observe(
            this,
            Observer {
                    baseResponse ->
                Util.log(TAG,baseResponse.msg)
                Util.showToast(baseResponse.msg,Toast.LENGTH_LONG,applicationContext)
                if(baseResponse.success == 1){
                    startActivity(Intent(this,Home_Activity::class.java))
                }



            }


            /*object : Observer<BaseResponse> {
                override fun onChanged(t: BaseResponse?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            }*/

        )

        viewmodel.observeLockScreenRequest().observe(
            this,
            Observer {
                shouldLockScreen ->
                Util.log(TAG,"Should Lock Screen:$shouldLockScreen")
                if(shouldLockScreen){
                    lockScreen()
                }else{
                    unlockScreen()
                }

            }
        )
    }

    fun lockScreen(){
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    fun unlockScreen(){
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

}
