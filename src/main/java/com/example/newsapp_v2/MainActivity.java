package com.example.newsapp_v2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    String API_KEY="39c00ec07c834803b3e5b9cd138d027c";
    String url_category="https://newsapi.org/v2/top-headlines?country=in";
    ListView listView;
    ProgressBar progressBar;
    ActionBar actionBar;
    String category_list[]={"Category","General","Business","Technology","Entertainment","Sports"};
    ArrayList<HashMap<String,String>> dataList=new ArrayList<>();
    SharedPrefrenceConfig sharedPrefrenceConfig;
    static final String KEY_AUTHOR = "author";
    static final String KEY_TITLE = "title";
    static final String KEY_DESCRIPTION = "description";
    static final String KEY_URL = "url";
    static final String KEY_URLTOIMAGE = "urlToImage";
    static final String KEY_PUBLISHEDAT = "publishedAt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.listNews);
        progressBar=findViewById(R.id.loader);
        listView.setEmptyView(progressBar);
        actionBar=getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        sharedPrefrenceConfig=new SharedPrefrenceConfig(this);
        if(Function.isNetworkAvailable(getApplicationContext())){
            DownloadNews newTask=new DownloadNews();
            newTask.execute();
        }
        else{
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    class DownloadNews extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String xml="";
            String urlParams= "";//+news_source+ //https://newsapi.org/v2/top-headlines?country=in
            xml=Function.executeGet(url_category+sharedPrefrenceConfig.getCategory()+"&apiKey="+API_KEY, urlParams);
            return xml;
        }
        @Override
        protected void onPostExecute(String xml) {
            if(xml.length()>0){
                try {
                    JSONObject jsonResponse=new JSONObject(xml);
                    JSONArray jsonArray=jsonResponse.optJSONArray("articles");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        HashMap<String,String> map=new HashMap<>();
                        map.put(KEY_AUTHOR,jsonObject.optString(KEY_AUTHOR).toString());
                        map.put(KEY_DESCRIPTION,jsonObject.optString(KEY_DESCRIPTION).toString());
                        map.put(KEY_PUBLISHEDAT,jsonObject.optString(KEY_PUBLISHEDAT).toString());
                        map.put(KEY_TITLE,jsonObject.optString(KEY_TITLE).toString());
                        map.put(KEY_URL,jsonObject.optString(KEY_URL).toString());
                        map.put(KEY_URLTOIMAGE,jsonObject.optString(KEY_URLTOIMAGE).toString());
                        dataList.add(map);
                    }
                }
                catch (Exception e){
                    Toast.makeText(MainActivity.this, "Unexpected error", Toast.LENGTH_SHORT).show();
                }
                ListNewsAdapter adapter=new ListNewsAdapter(MainActivity.this,dataList);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                listView.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent i=new Intent(MainActivity.this,DetailsActivity.class);
                                i.putExtra("url",dataList.get(+position).get(KEY_URL));
                                startActivity(i);
                            }
                        }
                );
            }
            else
                Toast.makeText(MainActivity.this, "No Data found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);

        MenuItem item = menu.findItem(R.id.spinner);
        final Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
        final ArrayAdapter<String> adapter=new ArrayAdapter<>(this,R.layout.spinnerdesign,category_list);
       // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        DownloadNews object=new DownloadNews();
                        String name=spinner.getSelectedItem().toString();
                        if(name.equalsIgnoreCase("general")) {
                            sharedPrefrenceConfig.writeCategory("&category=general");
                            recreate();
                        }
                        else if(name.equalsIgnoreCase("technology")) {
                            sharedPrefrenceConfig.writeCategory("&category=technology");
                            recreate();
                        }
                        else if(name.equalsIgnoreCase("sports")) {
                            sharedPrefrenceConfig.writeCategory("&category=sports");
                            recreate();
                        }
                        else if(name.equalsIgnoreCase("entertainment")) {
                            sharedPrefrenceConfig.writeCategory("&category=entertainment");
                            recreate();
                        }
                        else if(name.equalsIgnoreCase("business")) {
                            sharedPrefrenceConfig.writeCategory("&category=business");
                            recreate();
                        }
                        object.execute();
                        spinner.setSelection(adapter.getPosition(name));
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                }
        );

        return true;
    }
}

