package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * actor 晴天 create 2019/5/17
 * 封装一个kotlin下的通用adapter
 */

class KotlinDataAdapter<T> private constructor() : RecyclerView.Adapter<KotlinDataAdapter<T>.MyViewHolder>() {

    //数据
    private var mDataList = mutableListOf<T>()
    //布局id
    private var mLayoutId: Int? = null
    //绑定事件的lambda放发
    private var addBindView: ((itemView: View, itemData: T) -> Unit)? = null
    private var addBindView2: ((itemView: View, position: Int) -> Unit)? = null

    private var onItemClickListener: ((view: View, position: Int) -> Unit)? = null
    fun setOnItemClickListener(onItemClickListener: ((view: View, position: Int) -> Unit)?) {
        this.onItemClickListener = onItemClickListener
    }

    private var onItemLongClickListener: ((view: View, position: Int) -> Boolean)? = null
    fun setOnItemLongClickListener(onItemLongClickListener: ((view: View, position: Int) -> Boolean)?) {
        this.onItemLongClickListener = onItemLongClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val holder = MyViewHolder(LayoutInflater.from(parent.context).inflate(mLayoutId!!, parent, false))
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener { onItemClickListener?.invoke(it, holder.absoluteAdapterPosition) }
        }
        if (onItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener { onItemLongClickListener?.invoke(it, holder.absoluteAdapterPosition)!! }
        }
        return holder
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        addBindView?.invoke(holder.itemView, mDataList[position])
        addBindView2?.invoke(holder.itemView, position)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    /**
     * 建造者，用来完成adapter的数据组合
     */
    class Builder<B> {

        private var adapter: KotlinDataAdapter<B> = KotlinDataAdapter()

        /**
         * 设置数据
         */
        fun setData(lists: MutableList<B>): Builder<B> {
            adapter.mDataList = lists
            return this
        }

        /**
         * 设置布局id
         */
        fun setLayoutId(layoutId: Int): Builder<B> {
            adapter.mLayoutId = layoutId
            return this
        }

        /**
         * 绑定View和数据
         */
        fun addBindView(itemBind: ((itemView: View, itemData: B) -> Unit)): Builder<B> {
            adapter.addBindView = itemBind
            return this
        }
        fun addBindView2(itemBind: ((itemView: View, position: Int) -> Unit)): Builder<B> {
            adapter.addBindView2 = itemBind
            return this
        }

        fun create(): KotlinDataAdapter<B> {
            return adapter
        }
    }

}

//作者：晴天大帅逼
//链接：https://juejin.im/post/5cde5d5e518825259919395b
//来源：掘金
//著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。