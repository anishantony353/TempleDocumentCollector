package com.saarit.templedocumentcollector.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.saarit.templedocumentcollector.R
import com.saarit.templedocumentcollector.databinding.ActivityListBinding
import com.saarit.templedocumentcollector.utils.Util
import com.saarit.templedocumentcollector.viewModels.List_ViewModel

class List_Activity : AppCompatActivity() {

    private val TAG = List_Activity::class.java.simpleName
    lateinit var binding:ActivityListBinding
    lateinit var viewmodel:List_ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpBinding(savedInstanceState)
        setUpObservers()
    }

    private fun setUpBinding(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_list)
        viewmodel = ViewModelProviders.of(this).get(List_ViewModel::class.java)

        if(savedInstanceState == null){
         viewmodel.init()
        }
        binding.viewmodel = viewmodel
    }

    private fun setUpObservers() {
        viewmodel.observeClick().observe(
            this,
            Observer {
                    isClicked -> Util.log(TAG,"isClicked:"+isClicked)
                startActivity(
                    Intent(this,Form_Activity::class.java)
                        .putExtra("temple_id",intent.getIntExtra("temple_id",0))
                        .putExtra("type",intent.getStringExtra("type"))
                )
            }
        )
    }

    override fun onResume() {
        Util.log(TAG,"onResume")
        super.onResume()
    }

    override fun onPause() {
        Util.log(TAG,"onPause")
        super.onPause()
    }

    override fun onStop() {
        Util.log(TAG,"onStop")
        super.onStop()
    }

    override fun onRestart() {
        Util.log(TAG,"onRestart")
        super.onRestart()
    }

}
