package eu.deysouvik.newsbreeze.Activitys

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.gson.Gson
import eu.deysouvik.newsbreeze.ArticleData
import eu.deysouvik.newsbreeze.NewsDataFile.NewsData
import eu.deysouvik.newsbreeze.R
import kotlinx.android.synthetic.main.activity_read_news.*

class ReadNewsActivity : AppCompatActivity() {

    private var position=0
    private lateinit var iv_news_pic:ImageView
    private lateinit var myNews: ArticleData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_news)

        if(intent.hasExtra("NEWSDATA")){
            val data_str=intent.getStringExtra("NEWSDATA")
            myNews=Gson().fromJson(data_str, ArticleData::class.java)
        }
        if(intent.hasExtra("NEWSPOSITION")){
           position=intent.getIntExtra("NEWSPOSITION",0)
        }

        iv_news_pic=findViewById(R.id.iv_news_pic)

        Glide
            .with(this)
            .load(myNews.urlToImage)
            .centerCrop()
            .placeholder(R.drawable.news_img_viewholder)
            .into(iv_news_pic)

        if(myNews.author.isNullOrEmpty()){
            tv_author.text="Author unknown"
        }
        else{
            tv_author.text=myNews.author
        }
        tv_title_news.text=myNews.title
        tv_date_news.text=myNews.publishedAt
        tv_description_news.text=myNews.description
        tv_content_news.text=myNews.content
        if(myNews.isSaved==1){
            btn_save_news.text="Saved"
        }
        else{
            btn_save_news.text="Save"
        }
        btn_back.setOnClickListener {
            onBackPressed()
        }

        btn_save_news.setOnClickListener {
            if(myNews.isSaved==0){
                NewsData.newslist[position].isSaved=1
                myNews.isSaved=1
                btn_save_news.text="Saved"
                Toast.makeText(this, "Saved to watch later", Toast.LENGTH_SHORT).show()
            }
            else{
                val dialog= AlertDialog.Builder(this)
                dialog.setMessage("Are you sure to remove this news from saved news?")
                dialog.setPositiveButton("Yes"){dialoginterface,which->
                    dialoginterface.dismiss()
                    NewsData.newslist[position].isSaved=0
                    myNews.isSaved=0
                    btn_save_news.text="Save"
                    Toast.makeText(this, "Removed from watch later", Toast.LENGTH_SHORT).show()
                }
                dialog.setNegativeButton("No"){dialoginterface,which->
                    dialoginterface.dismiss()
                }
                val alertdialog=dialog.create()
                alertdialog.setCancelable(false)
                alertdialog.show()
            }
        }

    }


}