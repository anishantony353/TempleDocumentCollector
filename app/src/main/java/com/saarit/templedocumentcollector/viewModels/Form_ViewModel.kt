package com.saarit.templedocumentcollector.viewModels

import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.saarit.templedocumentcollector.models.objects.BaseResponse
import com.saarit.templedocumentcollector.models.repositories.server.Repo_server
import com.saarit.templedocumentcollector.ui.adapters.ImagesListAdapter
import com.saarit.templedocumentcollector.utils.PrefManager
import com.saarit.templedocumentcollector.utils.Util
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.FileNotFoundException
import java.lang.Exception
import java.net.SocketTimeoutException
import kotlin.collections.ArrayList

class Form_ViewModel(application: Application) :AndroidViewModel(application){

    private val TAG = Form_ViewModel::class.java.simpleName

    var imageList=ArrayList<String>()
    lateinit var fileUri: Uri
    var takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    var image_no:Int = 0

    var observableField_type = ObservableField<String>()
    var observableField_templeId = ObservableField<String>()
    var observableField_formId = ObservableField<String>()
    var observableField_noImages = ObservableField<String>("0")
    var observableInt_formId_visibility = ObservableInt(View.GONE)
    var observableInt_progress_visibility = ObservableInt(View.GONE)


    var observableField_adapter = ObservableField<ImagesListAdapter>()
    var adapter = ImagesListAdapter(this)

    var mutableLiveData_takeImage = MutableLiveData<Boolean>()
    var mutableLiveData_finishActivity = MutableLiveData<Boolean>()
    private var mutableLiveData_BaseResponse = MutableLiveData<BaseResponse>()
    private var mutableLiveData_LockScreen = MutableLiveData<Boolean>()

    var disposable = CompositeDisposable()

    fun init(type: String, templeId: Int, formId: Int) {
        Util.emptyImageDir(getApplication())
        observableField_type.set(type)
        observableField_templeId.set(""+templeId)

        if(formId != 0 ){
            observableInt_formId_visibility.set(View.VISIBLE)
            observableField_formId.set(""+formId)
        }

        adapter.setData(imageList)
        observableField_adapter.set(adapter)

    }

    fun getImage(){
        /*imageList.add("abc")
        adapter.notifyItemInserted(imageList.size-1)*/

        dispatchTakePictureIntent(++image_no)

    }

    fun removeImage(pos:Int){
        Util.log(TAG,"removeImage()..Pos:$pos")
        try{
            Util.deleteImageByPath(getApplication(), imageList.get(pos))
        }catch(exception:FileNotFoundException){
            Util.showToast(exception.message.toString(),Toast.LENGTH_SHORT,getApplication())
        }

        imageList.removeAt(pos)
        adapter.notifyItemRemoved(pos)
        adapter.notifyItemRangeChanged(pos,imageList.size)
        observableField_noImages.set(""+imageList.size)

    }

    fun dispatchTakePictureIntent(image_number: Int) {
        val options = BitmapFactory.Options()

        options.inSampleSize = 8

        fileUri = Util.getMediaFileUri(getApplication(), image_number)
        Util.log(TAG, "URI:$fileUri")
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)

        /*var list:List<ResolveInfo> = getApplication<Application>().
            packageManager.queryIntentActivities(takePictureIntent,PackageManager.MATCH_DEFAULT_ONLY)

        for(n in list){
            var packageName = n.activityInfo.packageName
            getApplication<Application>().grantUriPermission(packageName,fileUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }*/

        if (takePictureIntent.resolveActivity(getApplication<Application>().packageManager) != null) {
            mutableLiveData_takeImage.setValue(true)
        }
    }

    fun compressImage(image_no: Int) {

        //Compress the image in background thread using RxJava
        disposable.add(

            Observable.fromCallable{

                val out = Util.rotateImage(fileUri, getApplication())
                Util.log(TAG,"Rotated Image")
                val fOut = getApplication<Application>().contentResolver.openOutputStream(fileUri)
                out.compress(Bitmap.CompressFormat.JPEG, 15, fOut)
                fOut.flush()
                fOut.close()

                true
            }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { value ->
                        imageList.add(fileUri.path)
                        adapter.notifyItemInserted(imageList.size-1)

                        observableField_noImages.set(""+imageList.size)
                    },
                    { error -> Util.showToast(""+error.message, Toast.LENGTH_LONG, getApplication()) }

                )
        )

    }

    fun submit(){
        Util.log(TAG,"submit()")

        upload()

    }

    private fun upload() {

        var requestBody_type = RequestBody.create(MediaType.parse("text/*"),observableField_type.get())
        var requestBody_templeId = RequestBody.create(MediaType.parse("text/*"),observableField_templeId.get())
        var requestBody_userId = RequestBody.create(MediaType.parse("text/*"),""+PrefManager.getUserId(getApplication()))
        var requestBody_noImages = RequestBody.create(MediaType.parse("text/*"),""+imageList.size)
        var imageFiles = ArrayList<MultipartBody.Part>()


        disposable.add(

            Repo_server.apiClient.
                uploadData(requestBody_type,requestBody_templeId,requestBody_userId,requestBody_noImages,imageFiles).
                toObservable().
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                    subscribe (
                        {
                                baseResponse ->  if(baseResponse.success == 0){
                                    throw Exception(baseResponse.msg)
                                }
                            Util.emptyImageDir(getApplication())
                            mutableLiveData_BaseResponse.value = BaseResponse(baseResponse.success, baseResponse.msg)
                            mutableLiveData_finishActivity.value = true

                        },
                        {

                                error->
                            mutableLiveData_LockScreen.value = false
                            observableInt_progress_visibility.set(View.GONE)
                            if(error is SocketTimeoutException){
                                mutableLiveData_BaseResponse.value = BaseResponse(0, "Connection Timeout")
                            }else{
                                mutableLiveData_BaseResponse.value = BaseResponse(0, ""+error.message)
                            }

                        },
                        {
                            mutableLiveData_LockScreen.value = false
                            observableInt_progress_visibility.set(View.GONE)

                        },
                        {
                            disposable-> if(imageList.size <= 0){
                                throw Exception("Get Image")
                            }
                            if(!Util.isNetworkAvailable(getApplication())){
                                throw Exception("Check Internet")
                            }

                            try{
                                for(i in imageList.indices){
                                    var no=i
                                    val reqBody = RequestBody.create(
                                        MediaType.parse("image/*"), Util.getImageByPath(getApplication(),imageList.get(i))
                                    )
                                    imageFiles.add(
                                        MultipartBody.Part.createFormData(
                                            "${++no}",
                                            "$no.jpg",
                                            reqBody
                                        ))
                                }
                            }catch(exception:Exception){
                                throw exception
                            }

                            mutableLiveData_LockScreen.value = true
                            observableInt_progress_visibility.set(View.VISIBLE)


                        }
                    )

        )

    }

    // LiveDatas
    fun captureImage():LiveData<Boolean> = mutableLiveData_takeImage
    fun finishActivity():LiveData<Boolean> = mutableLiveData_finishActivity
    fun observeServerResponse():LiveData<BaseResponse> = mutableLiveData_BaseResponse
    fun observeLockScreenRequest():LiveData<Boolean> = mutableLiveData_LockScreen

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
        Util.log(TAG,"onCleared()")
    }

}