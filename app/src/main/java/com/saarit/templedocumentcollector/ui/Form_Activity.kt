package com.saarit.templedocumentcollector.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.saarit.templedocumentcollector.R
import com.saarit.templedocumentcollector.databinding.ActivityFormBinding
import com.saarit.templedocumentcollector.utils.Util
import com.saarit.templedocumentcollector.viewModels.Form_ViewModel

class Form_Activity : AppCompatActivity() {

    private val TAG = Form_Activity::class.java.simpleName

    lateinit var binding:ActivityFormBinding
    lateinit var viewmodel:Form_ViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpBinding(savedInstanceState)
        setUpObservers()
    }


    private fun setUpBinding(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_form)
        viewmodel = ViewModelProviders.of(this).get(Form_ViewModel::class.java)
        if(savedInstanceState == null){
            viewmodel.init(
                intent.getStringExtra("type"),
                intent.getIntExtra("temple_id",0),
                intent.getIntExtra("form_id",0)
            )
        }
        binding.viewmodel = viewmodel

    }

    private fun setUpObservers() {
        viewmodel.captureImage().observe(
            this,
            Observer {
                    value->
                Util.log(TAG, "Observed getImage Request")
                startActivityForResult(viewmodel.takePictureIntent, viewmodel.image_no)

            }
        )

        viewmodel.finishActivity().observe(
            this,
            Observer {
                    value->
                Util.log(TAG, "Observed finishActivity Request")
                finish()
            }
        )

        viewmodel.observeServerResponse().observe(
            this,
            Observer {
                    baseResponse ->
                Util.log(TAG,baseResponse.msg)
                Util.showToast(baseResponse.msg,Toast.LENGTH_LONG,applicationContext)


            }
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

    public override fun onActivityResult(image_no: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(image_no, resultCode, data)
        Log.i(TAG, "onActivityResult()")

        if (resultCode == Activity.RESULT_OK) {

            viewmodel.compressImage(image_no)

        } else if (resultCode == Activity.RESULT_CANCELED) {

            Util.showToast("Could not capture Image", Toast.LENGTH_SHORT, application)


        }

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
