package com.saarit.templedocumentcollector.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saarit.templedocumentcollector.R
import com.saarit.templedocumentcollector.databinding.ListRowBinding
import com.saarit.templedocumentcollector.viewModels.List_ViewModel
import com.saarit.templedocumentcollector.utils.Util

class ListAdapter(vm: List_ViewModel) : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {
    private val TAG = ListAdapter::class.java.simpleName
    var vm = vm
    lateinit var list:ArrayList<String>

    /*var count_oncreateVH = 0
    var count_onBndVH = 0
    var count_getItemCnt = 0*/


    fun setData(data:ArrayList<String>){
       list = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        /*Util.log(TAG,"onCreateViewHolder()..Calls:".plus(++count_oncreateVH))*/
        var view:View = LayoutInflater.from(parent.context).inflate(R.layout.list_row,parent,false)
        var binding:ListRowBinding = ListRowBinding.bind(view)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        /*Util.log(TAG,"getItemCount()..Calls:".plus(++count_getItemCnt))*/
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        /*Util.log(TAG,"onBindViewHolder()..Calls:".plus(++count_onBndVH))*/
        holder.bind(position)
    }

    inner class MyViewHolder(binding: ListRowBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding = binding
        fun bind(position: Int) {
            binding.viewmodel = vm
            binding.pos = position
            binding.executePendingBindings()
        }

    }

}