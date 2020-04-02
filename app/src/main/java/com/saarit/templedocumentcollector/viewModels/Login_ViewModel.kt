package com.saarit.templedocumentcollector.viewModels

import android.app.Application
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.saarit.templedocumentcollector.binding.BindingAdapter
import com.saarit.templedocumentcollector.models.objects.BaseResponse
import com.saarit.templedocumentcollector.models.objects.LoginFeilds
import com.saarit.templedocumentcollector.models.repositories.server.Repo_server
import com.saarit.templedocumentcollector.utils.Constant
import com.saarit.templedocumentcollector.utils.PrefManager
import com.saarit.templedocumentcollector.utils.Util
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class Login_ViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = Login_ViewModel::class.java.simpleName
    private var mutableLiveData_BaseResponse = MutableLiveData<BaseResponse>()
    private var mutableLiveData_LockScreen = MutableLiveData<Boolean>()

    var loginFeilds = LoginFeilds()
    var loginFeildsObservable = ObservableField<LoginFeilds>()
    var progressVisibility = ObservableInt(View.GONE)
    var errorMsg_id = ObservableField<String>("")
    var errorMsg_password = ObservableField<String>("")
    var ischecked = ObservableBoolean(false)

    /*var errorMsg_id:String= ""
    var errorMsg_password:String = ""*/

    var disposable = CompositeDisposable()


    fun init(){
       /* Util.log(TAG,"init...")*/
        loginFeildsObservable.set(loginFeilds)
        setRememberMeCheckBox()

    }

     fun setRememberMeCheckBox() {
        if(PrefManager.getRemember(getApplication())){
            ischecked.set(true)

            loginFeilds.id = PrefManager.getLoginId(getApplication())
            loginFeilds.password = PrefManager.getLoginPassword(getApplication())
            loginFeildsObservable.set(loginFeilds)

        }
    }

    fun onLoginClick(view: View){
        Util.log(TAG,"IS Checked:${ischecked.get()}")
        Util.log(TAG,"onLoginClick...ID:${loginFeilds.id}  Pass:${loginFeilds.password}")

        if(isValid()){
            getTemplesMaster()
        }
    }

    private fun isValid(): Boolean {
        if(loginFeilds.id == null || loginFeilds.id!!.trim().length == 0){
            Util.log(TAG, "ID not valid")
            errorMsg_id.set("Enter Id")
            errorMsg_id.notifyChange()
            return false
        }
        if(loginFeilds.password == null || loginFeilds.password!!.trim().length == 0){
            Util.log(TAG, "Pass not valid")
            errorMsg_password.set("Enter Password")
            errorMsg_password.notifyChange()
            return false
        }

        return true
    }

    private fun getTemplesMaster() {
        disposable.add(


            Repo_server.apiClient.login(loginFeilds.id!!.trim(),loginFeilds.password!!.trim())
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(

                        {
                            dto->Util.log(TAG,"onNext..Success:${dto.success}")
                            if(dto.success == 0){
                                Util.log(TAG,"About to thow exception")
                                throw Exception(dto.msg)
                            }

                            Util.log(TAG,"About to initialize DTO")
                            Constant.dto_temples = dto
                            PrefManager.setUserId(dto.user_detail.user_id, getApplication())
                            mutableLiveData_BaseResponse.value = BaseResponse(dto.success, dto.msg)
                        },
                        {
                            error-> Util.log(TAG,"onError.."+error.message)
                            mutableLiveData_LockScreen.value = false
                            progressVisibility.set(View.GONE)
                            if(error is SocketTimeoutException){
                                mutableLiveData_BaseResponse.value = BaseResponse(0, "Connection Timeout")
                            }else{
                                mutableLiveData_BaseResponse.value = BaseResponse(0, ""+error.message)
                            }

                        },
                        {
                            Util.log(TAG,"onComplete")
                            mutableLiveData_LockScreen.value = false
                            progressVisibility.set(View.GONE)
                            PrefManager.setRemember(ischecked.get(),getApplication())
                            PrefManager.setLoginId(loginFeilds.id!!,getApplication())
                            PrefManager.setLoginPassword(loginFeilds.password!!,getApplication())
                        },
                        {
                            disposable->Util.log(TAG,"onSubscribe")
                            if(!Util.isNetworkAvailable(getApplication())){
                                throw Exception("Check Internet")
                            }
                            mutableLiveData_LockScreen.value = true
                            progressVisibility.set(View.VISIBLE)
                        }
                )
        )

    }

    fun observeServerResponse():LiveData<BaseResponse> = mutableLiveData_BaseResponse
    fun observeLockScreenRequest():LiveData<Boolean> = mutableLiveData_LockScreen


    override fun onCleared() {
        Util.log(TAG,"onCleared")
        super.onCleared()
        disposable.clear()
    }



}