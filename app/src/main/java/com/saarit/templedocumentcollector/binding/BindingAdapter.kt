package com.saarit.templedocumentcollector.binding

import android.net.Uri
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.saarit.templedocumentcollector.models.objects.Temple_master
import com.saarit.templedocumentcollector.ui.adapters.ImagesListAdapter
import com.saarit.templedocumentcollector.ui.adapters.ListAdapter

import com.saarit.templedocumentcollector.utils.Util

object BindingAdapter {

    private val TAG: String = BindingAdapter::class.java.simpleName

    private val options = RequestOptions()
        .fitCenter()
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
        .priority(Priority.HIGH)
        .signature(ObjectKey(System.currentTimeMillis().toString()))

    @BindingAdapter("error_msg")
    @JvmStatic
    fun setErrorMsg(et: EditText, msg: String) {
        Util.log(TAG, "setErrorMsg()")
        if (msg == null || msg == "") {
            return
        }
        Util.log(TAG, "About to set Error Msg to EditText ")

        et.getText().clear()
        et.requestFocus()
        et.setError(msg)
    }

    @BindingAdapter("adapter_auto_textview", "onItemClicklistner")
    @JvmStatic
    public fun setAdapter(
        view: AutoCompleteTextView,
        adapter: ArrayAdapter<Temple_master>,
        listner: AdapterView.OnItemClickListener)
    {

        Util.log(TAG, "setAdapter()")

        view.setAdapter(adapter)
        view.setOnItemClickListener(listner)

    }

    @BindingAdapter("rv_adapter")
    @JvmStatic
    fun setRvAdapter(rv:RecyclerView, adapter:ListAdapter) {
        rv.layoutManager = LinearLayoutManager(rv.context)

        rv.adapter = adapter
    }

    @BindingAdapter("images_rv_adapter")
    @JvmStatic
    fun setImagesRvAdapter(rv:RecyclerView, adapter:ImagesListAdapter) {
        rv.layoutManager = LinearLayoutManager(rv.context)
        rv.itemAnimator = DefaultItemAnimator()
        rv.adapter = adapter
    }

    @BindingAdapter("setImgOnImageView")
    @JvmStatic
    fun setImgOnImageView(view: ImageView, path: String) {
        Util.log(TAG, "setImgOnImageView()..Path:$path")

        Glide.with(view.context.applicationContext).load(path).apply(options).into(view)

    }


}