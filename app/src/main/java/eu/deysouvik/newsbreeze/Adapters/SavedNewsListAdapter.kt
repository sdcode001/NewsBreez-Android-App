package eu.deysouvik.newsbreeze.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eu.deysouvik.newsbreeze.ArticleData
import eu.deysouvik.newsbreeze.R
import kotlinx.android.synthetic.main.item_savednews.view.*

class SavedNewsListAdapter(private val context: Context, private var list:ArrayList<ArticleData>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_savednews,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model=list[position]
        if(holder is MyViewHolder){
            Glide
                .with(context)
                .load(model.urlToImage)
                .centerCrop()
                .placeholder(R.drawable.news_img_viewholder)
                .into(holder.itemView.iv_savednews)
            holder.itemView.tv_publishdate.text=model.publishedAt
            holder.itemView.tv_title_savednews.text=model.title
            if(model.author.isNullOrEmpty()){
                holder.itemView.tv_authorname.text="Author unknown"
            }
            else{
                holder.itemView.tv_authorname.text=model.author
            }
            holder.itemView.setOnClickListener {
                if(onClickListener!=null){
                    onClickListener!!.onClick(model)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnClickListener{
        fun onClick(model: ArticleData)
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener=onClickListener
    }

    fun setFilteredList(filteredList:ArrayList<ArticleData>){
        this.list=filteredList
        notifyDataSetChanged()
    }


    private class MyViewHolder(view: View):RecyclerView.ViewHolder(view)

}