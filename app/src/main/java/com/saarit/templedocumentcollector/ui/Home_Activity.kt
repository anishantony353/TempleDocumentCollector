package com.saarit.templedocumentcollector.ui

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.saarit.templedocumentcollector.R
import com.saarit.templedocumentcollector.databinding.ActivityHomeBinding
import com.saarit.templedocumentcollector.utils.Util
import com.saarit.templedocumentcollector.viewModels.Home_ViewModel

class Home_Activity : AppCompatActivity() {

    private val TAG:String = Home_Activity::class.java.simpleName
    lateinit  var binding:ActivityHomeBinding
    lateinit var viewmodel:Home_ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpBinding(savedInstanceState)
        setUpObservers()
    }

    private fun setUpObservers() {
        viewmodel.observeClickType().observe(
            this,
            Observer{
                    type->Util.log(TAG,"Type:$type")
                    Util.log(TAG,"Temple Id:${viewmodel.temple.temple_id}")

                    startActivity(
                        Intent(this,List_Activity::class.java)
                        .putExtra("temple_id",viewmodel.temple.temple_id)
                        .putExtra("type",type)
                    )
            }
        )
    }
    private fun setUpBinding(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        viewmodel = ViewModelProviders.of(this).get(Home_ViewModel::class.java)

        if(savedInstanceState == null){
            viewmodel.init()
        }

       binding.viewmodel = viewmodel

    }

    private fun showLogoutDialog() {

        val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    val intent = Intent(this, Login_Activity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(intent)
                }

                DialogInterface.BUTTON_NEGATIVE -> {
                }
            }
        }

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Logout ?").setPositiveButton("Yes", dialogClickListener)
            .setNegativeButton("No", dialogClickListener).show()

    }

    override fun onBackPressed() {
        showLogoutDialog()
    }
}
