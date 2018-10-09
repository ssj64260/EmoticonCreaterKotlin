package com.android.enoticoncreaterkotlin.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.android.enoticoncreaterkotlin.R
import com.android.enoticoncreaterkotlin.model.FunctionInfo

class FunctionListAdapter(context: Context, datas: ArrayList<FunctionInfo>) : RecyclerView.Adapter<FunctionListAdapter.BaseViewHolder>() {

    private var mDatas: ArrayList<FunctionInfo> = datas
    private var mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mListClick: IOnListClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(mInflater.inflate(R.layout.item_function_list, parent, false))
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        bindItem(holder, position)
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }

    private fun bindItem(holder: BaseViewHolder, position: Int) {
        val data = mDatas[position]
        val name = data.getName()

        holder.btnFunction.text = name

        holder.itemView.tag = position
        holder.itemView.setOnClickListener(mClick)
    }

    fun setListClick(listClick: IOnListClickListener) {
        this.mListClick = listClick
    }

    class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var btnFunction: Button = itemView.findViewById(R.id.btn_function)
    }

    private val mClick = View.OnClickListener {
        val position = it.tag as Int
        mListClick?.onItemClick(position)
    }
}