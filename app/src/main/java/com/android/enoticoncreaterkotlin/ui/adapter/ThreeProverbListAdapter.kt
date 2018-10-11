package com.android.enoticoncreaterkotlin.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.enoticoncreaterkotlin.R
import com.android.enoticoncreaterkotlin.model.ThreeProverbInfo
import java.util.*

class ThreeProverbListAdapter(context: Context, mDatas: ArrayList<ThreeProverbInfo>)
    : RecyclerView.Adapter<ThreeProverbListAdapter.BaseViewHolder>() {

    private var mListClick: IOnListClickListener? = null
    private val mDatas: List<ThreeProverbInfo> = mDatas
    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(mInflater.inflate(R.layout.item_three_proverb_list, parent, false))
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        bindItem(holder, position)
    }

    private fun bindItem(holder: BaseViewHolder, position: Int) {
        val model = mDatas[position]
        val title = model.title
        val first = model.firstProverb
        val second = model.secondProverb
        val third = model.thirdProverb
        val times = "使用次数：" + model.useTimes

        holder.tvTitle.text = title
        holder.tvFirst.text = first
        holder.tvSecond.text = second
        holder.tvThird.text = third
        holder.tvUsedTimes.text = times

        holder.itemView.tag = position
        holder.itemView.setOnClickListener(mClick)

        holder.tvDelete.tag = position
        holder.tvDelete.setOnClickListener(mClick)
    }

    fun setListClick(listClick: IOnListClickListener) {
        this.mListClick = listClick
    }

    class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvFirst: TextView = itemView.findViewById(R.id.tv_first)
        val tvSecond: TextView = itemView.findViewById(R.id.tv_second)
        val tvThird: TextView = itemView.findViewById(R.id.tv_third)
        val tvUsedTimes: TextView = itemView.findViewById(R.id.tv_used_times)
        val tvDelete: TextView = itemView.findViewById(R.id.tv_delete)
    }

    private val mClick = View.OnClickListener { v ->
        if (mListClick != null) {
            val position = v.tag as Int
            when (v.id) {
                R.id.tv_delete -> mListClick!!.onTagClick(IOnListClickListener.ITEM_TAG0, position)
                else -> mListClick!!.onItemClick(position)
            }
        }
    }

}