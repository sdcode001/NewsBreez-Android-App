package eu.deysouvik.newsbreeze.Activitys

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import eu.deysouvik.newsbreeze.*
import eu.deysouvik.newsbreeze.Adapters.NewsListAdapter
import eu.deysouvik.newsbreeze.NewsDataFile.NewsData
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_progress_bar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

 class MainActivity : AppCompatActivity() {

    lateinit var progressBar_dialog:Dialog
    lateinit var searchBar:SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBar("NewsBreeze")
        showProgressBar("Loading...")
        searchBar=findViewById(R.id.search_bar)
        closeKeyboard()
        searchBar.clearFocus()
        searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchText=newText!!.toLowerCase(Locale.getDefault())
                if(!searchText.isNullOrEmpty()){filterText(searchText)}
                else{(rv_news.adapter as NewsListAdapter).setFilteredList(NewsData.newslist)}
                return true
            }

        })

        if(isNetworkAvailable()){
           val apicall= NewsService.interfaceApi.getDataByCountry("in")
            apicall.enqueue(object : Callback<News>{
                override fun onResponse(call: Call<News>, response: Response<News>) {
                   if(response.isSuccessful){
                       cancel_progressBar()
                       closeKeyboard()
                       NewsData.newslist =response.body()!!.articles
                       for(i in 0..NewsData.newslist.size-1){
                          NewsData.newslist[i].id=i
                       }
                       setupRecyclerView()
                   }
                    else{
                        setupRecyclerView()
                        cancel_progressBar()
                       Toast.makeText(this@MainActivity, "${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<News>, t: Throwable) {
                    setupRecyclerView()
                    cancel_progressBar()
                    Toast.makeText(this@MainActivity, "Connection Failure $t", Toast.LENGTH_SHORT).show()
                }

            })
        }
        else{
            setupRecyclerView()
            cancel_progressBar()
            Toast.makeText(this, "check network", Toast.LENGTH_SHORT).show()
        }


    }

     override fun onResume() {
         super.onResume()
         setupRecyclerView()
     }

     private fun setupRecyclerView(){
         rv_news.layoutManager=LinearLayoutManager(this@MainActivity)
         rv_news.setHasFixedSize(true)
         val adapter= NewsListAdapter(this@MainActivity, NewsData.newslist)
         rv_news.adapter=adapter

         adapter.setOnClickListener(object: NewsListAdapter.OnClickListener {
             override fun onClick( model: ArticleData) {
                 val intent=Intent(this@MainActivity, ReadNewsActivity::class.java)
                 val data_str=Gson().toJson(model)
                 intent.putExtra("NEWSDATA",data_str)
                 intent.putExtra("NEWSPOSITION",model.id)
                 startActivity(intent)
             }
         })
     }





    private fun closeKeyboard(){
        val view= this.currentFocus
        if(view!=null){
           val imm=getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken,0)
        }
    }

    private fun isNetworkAvailable():Boolean{
        val connectivityManager=this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            val network=connectivityManager.activeNetwork?: return false
            val activeNetwork=connectivityManager.getNetworkCapabilities(network)?: return false
            return when{
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)-> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)-> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)-> true
                else -> false
            }
        }
        else{
            val networkinfo=connectivityManager.activeNetworkInfo
            return networkinfo!=null && networkinfo.isConnectedOrConnecting
        }
    }






    fun setupActionBar(title:String){
        setSupportActionBar(toolbar_main_activity)
        val actionbar=supportActionBar
        if (actionbar != null) {
            actionbar.title=title
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_saved_items,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_saved_items ->{
                val intent=Intent(this, SavedNewsActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


   private fun filterText(text:String){
       val filteredList=ArrayList<ArticleData>()
          for(item in NewsData.newslist){
              if(item.title.toLowerCase(Locale.getDefault()).contains(text)){
                  filteredList.add(item)
              }
          }
          if(filteredList.isEmpty()){
              (rv_news.adapter as NewsListAdapter).setFilteredList(filteredList)
              Toast.makeText(this, "No news find.", Toast.LENGTH_LONG).show()
          }
          else{
              (rv_news.adapter as NewsListAdapter).setFilteredList(filteredList)
          }
   }


    fun showProgressBar(txt:String){
        progressBar_dialog= Dialog(this)
        progressBar_dialog.setContentView(R.layout.custom_progress_bar)
        progressBar_dialog.tv_progress_text.text=txt
        progressBar_dialog.show()

    }
    fun cancel_progressBar(){
        progressBar_dialog.cancel()
    }







}