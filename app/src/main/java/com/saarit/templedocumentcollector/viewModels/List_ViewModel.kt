package com.saarit.templedocumentcollector.viewModels

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saarit.templedocumentcollector.ui.adapters.ListAdapter
import com.saarit.templedocumentcollector.utils.Util
import java.util.*
import kotlin.collections.ArrayList

class List_ViewModel :ViewModel(){

    private val TAG:String = List_ViewModel::class.java.simpleName

    var list = ArrayList<String>(Arrays.asList("1675","6754","1463","9734"))


    var listAdapter = ListAdapter(this)

    var observableAdapter = ObservableField<ListAdapter>()

    var mutableLiveData_click = MutableLiveData<Boolean>()

    fun init(){
        Util.log(TAG,"init()")
        listAdapter.setData(list)
        observableAdapter.set(listAdapter)
    }

    fun onClick(){
        Util.log(TAG,"onClick()")
        mutableLiveData_click.value = true
    }

    fun observeClick():LiveData<Boolean> = mutableLiveData_click

}