package com.saarit.templedocumentcollector.ui.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saarit.templedocumentcollector.R
import com.saarit.templedocumentcollector.databinding.ImageListRowBinding
import com.saarit.templedocumentcollector.utils.Util
import com.saarit.templedocumentcollector.viewModels.Form_ViewModel

class ImagesListAdapter(vm: Form_ViewModel) : RecyclerView.Adapter<ImagesListAdapter.MyViewHolder>() {
    private val TAG = ImagesListAdapter::class.java.simpleName
    var vm = vm
    lateinit var list:ArrayList<String>

    /*var count_oncreateVH = 0
    var count_onBndVH = 0
    var count_getItemCnt = 0*/


    fun setData(data: ArrayList<String>){
       list = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        /*Util.log(TAG,"onCreateViewHolder()..Calls:".plus(++count_oncreateVH))*/
        var view:View = LayoutInflater.from(parent.context).inflate(R.layout.image_list_row,parent,false)
        var binding: ImageListRowBinding = ImageListRowBinding.bind(view)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        /*Util.log(TAG,"getItemCount()..Calls:".plus(++count_getItemCnt))*/
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        /*Util.log(TAG,"onBindViewHolder()..Calls:".plus(++count_onBndVH))*/
        Util.log(TAG,"onBindViewHolder()..Pos:$position ")

        holder.bind(position)
    }

    inner class MyViewHolder(binding: ImageListRowBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding = binding
        fun bind(position: Int) {
            binding.viewmodel = vm
            binding.pos = position
            binding.executePendingBindings()
        }

    }

}