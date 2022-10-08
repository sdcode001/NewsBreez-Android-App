package eu.deysouvik.newsbreeze.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eu.deysouvik.newsbreeze.ArticleData
import eu.deysouvik.newsbreeze.NewsDataFile.NewsData
import eu.deysouvik.newsbreeze.R
import kotlinx.android.synthetic.main.item_news.view.*

class NewsListAdapter(private val context:Context,private var list:ArrayList<ArticleData>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? =null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_news,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model=list[position]
        if(holder is MyViewHolder){
            Glide
                .with(context)
                .load(model.urlToImage)
                .centerCrop()
                .placeholder(R.drawable.news_img_viewholder)
                .into(holder.itemView.iv_news)
            holder.itemView.tv_title.text=model.title
            holder.itemView.tv_description.text=model.description
            holder.itemView.tv_date.text=model.publishedAt
            if(model.isSaved==1){
                holder.itemView.btn_save.text="Saved"
            }
            holder.itemView.btn_save.setOnClickListener {
               if(model.isSaved==0){
                   NewsData.newslist[position].isSaved=1
                   list[position].isSaved=1
                   model.isSaved=1
                   holder.itemView.btn_save.text="Saved"
                   Toast.makeText(context, "Saved to watch later", Toast.LENGTH_SHORT).show()
               }
               else{
                   NewsData.newslist[position].isSaved=0
                   list[position].isSaved=0
                   model.isSaved=0
                   holder.itemView.btn_save.text="Save"
                   Toast.makeText(context, "Removed from watch later", Toast.LENGTH_SHORT).show()
               }
            }

            holder.itemView.btn_read.setOnClickListener {
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