package eu.deysouvik.newsbreeze.Activitys

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import eu.deysouvik.newsbreeze.ArticleData
import eu.deysouvik.newsbreeze.NewsDataFile.NewsData
import eu.deysouvik.newsbreeze.R
import eu.deysouvik.newsbreeze.Adapters.SavedNewsListAdapter
import kotlinx.android.synthetic.main.activity_saved_news.*
import java.util.*
import kotlin.collections.ArrayList

class SavedNewsActivity : AppCompatActivity() {

    lateinit var searchBar2: SearchView
    var savedlist=ArrayList<ArticleData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_news)

        setupActionBar("Saved")
        setupUI()
        searchBar2=findViewById(R.id.search_bar2)
        closeKeyboard()
        searchBar2.clearFocus()
        searchBar2.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchText=newText!!.toLowerCase(Locale.getDefault())
                if(!searchText.isNullOrEmpty()){filterText(searchText)}
                else{(rv_savednews.adapter as SavedNewsListAdapter).setFilteredList(savedlist)}
                return true
            }

        })


    }

    override fun onResume() {
        super.onResume()
        setupUI()
    }


    fun setupUI(){
        savedlist.clear()
        for(item in NewsData.newslist){
            if(item.isSaved==1){
                savedlist.add(item)
            }
        }
        tv_count.text="${savedlist.size} Saved News"
        rv_savednews.layoutManager=LinearLayoutManager(this)
        rv_savednews.setHasFixedSize(true)
        val adapter= SavedNewsListAdapter(this,savedlist)
        rv_savednews.adapter=adapter

        adapter.setOnClickListener(object: SavedNewsListAdapter.OnClickListener {
            override fun onClick(model: ArticleData) {
                val intent= Intent(this@SavedNewsActivity, ReadNewsActivity::class.java)
                val data_str= Gson().toJson(model)
                intent.putExtra("NEWSDATA",data_str)
                intent.putExtra("NEWSPOSITION",model.id)
                startActivity(intent)
            }

        })

    }


    fun setupActionBar(title:String){
        setSupportActionBar(toolbar_savednews_activity)
        val actionbar=supportActionBar
        if (actionbar != null) {
            actionbar.title=title
            actionbar!!.setDisplayHomeAsUpEnabled(true)
            actionbar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }
        toolbar_savednews_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }


    private fun closeKeyboard(){
        val view= this.currentFocus
        if(view!=null){
            val imm=getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken,0)
        }
    }


    private fun filterText(text:String){
        val filteredList=ArrayList<ArticleData>()
        for(item in savedlist){
            if(item.title.toLowerCase(Locale.getDefault()).contains(text)){
                filteredList.add(item)
            }
        }
        if(filteredList.isEmpty()){
            (rv_savednews.adapter as SavedNewsListAdapter).setFilteredList(filteredList)
            Toast.makeText(this, "No news find.", Toast.LENGTH_LONG).show()
        }
        else{
            (rv_savednews.adapter as SavedNewsListAdapter).setFilteredList(filteredList)
        }
    }


}