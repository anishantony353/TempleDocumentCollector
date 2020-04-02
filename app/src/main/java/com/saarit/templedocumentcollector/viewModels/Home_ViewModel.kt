package com.saarit.templedocumentcollector.viewModels

import android.app.Application
import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.saarit.templedocumentcollector.models.objects.Temple_master
import com.saarit.templedocumentcollector.utils.Constant
import com.saarit.templedocumentcollector.utils.Util
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService



class Home_ViewModel(application:Application): AndroidViewModel(application){

    private val TAG = Home_ViewModel::class.java.simpleName
    var temple = Temple_master()
    var observableAdapter = ObservableField<ArrayAdapter<Temple_master>>()
    var observableListener = ObservableField<AdapterView.OnItemClickListener>()
    var observableTemple = ObservableField<Temple_master>()

    var mutableLiveData_ClickType = MutableLiveData<String>()

    fun init(){
        observableAdapter.set(ArrayAdapter(getApplication(), android.R.layout.simple_list_item_1, Constant.dto_temples.temple_details))

        observableListener.set(
            AdapterView.OnItemClickListener{
                adapterView, view, pos, id ->
                /*val inp:InputMethodManager = (getApplication() as Context).getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inp.hideSoftInputFromWindow(view.getWindowToken(), 0)*/
                temple  = adapterView.getItemAtPosition(pos) as Temple_master
                observableTemple.set(temple)
                /*Util.log(TAG,"Clicked:"+temple.toString())
                Util.log(TAG,"Temple Id:${temple.temple_id}"+"\n"+"Temple Name:${temple.temple_name}"+"\n"+
                        "Village Id:"+temple.village_id+"\n"+"Village Name:"+temple.village_name+"\n"+
                        "Taluka Id:"+temple.taluka_id+"\n"+"Taluka Name:"+temple.taluka_name+"\n"+
                        "District Id:"+temple.district_id+"\n"+"District Name:"+temple.district_name)*/

            }
        )
    }

    fun onBtnClick(type:String){
        Util.log(TAG,"onClick()..Type:"+type)
        if(temple.temple_id == 0){
            Toast.makeText(getApplication(),"Select Temple",Toast.LENGTH_SHORT).show()
            return
        }
        mutableLiveData_ClickType.value = type

    }

     fun observeClickType():LiveData<String> = mutableLiveData_ClickType


}