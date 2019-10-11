package com.example.newsapp_v2;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class ListNewsAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String,String>> data;

    public ListNewsAdapter(Activity activity, ArrayList<HashMap<String, String>> data) {
        this.activity = activity;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListNewsViewHolder holder=null;
        if(convertView==null){
            holder=new ListNewsViewHolder();
            convertView= LayoutInflater.from(activity).inflate(R.layout.list_row,parent,false);
            holder.galleryImage=convertView.findViewById(R.id.galleryImage);
            holder.author=convertView.findViewById(R.id.author);
            holder.sdetails=convertView.findViewById(R.id.sdetails);
            holder.title=convertView.findViewById(R.id.title);
            holder.time=convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        }
        else
            holder= (ListNewsViewHolder) convertView.getTag();

        holder.galleryImage.setId(position);
        holder.author.setId(position);
        holder.title.setId(position);
        holder.sdetails.setId(position);
        holder.time.setId(position);

        HashMap<String,String> song=new HashMap<>();
        song=data.get(position);
        try{
            holder.author.setText(song.get(MainActivity.KEY_AUTHOR));
            holder.title.setText(song.get(MainActivity.KEY_TITLE));
            holder.time.setText(song.get(MainActivity.KEY_PUBLISHEDAT));
            holder.sdetails.setText(song.get(MainActivity.KEY_DESCRIPTION));

            if(song.get(MainActivity.KEY_URLTOIMAGE).toString().length() < 5)
            {
                holder.galleryImage.setVisibility(View.GONE);
            }else{
                Picasso.with(activity)
                        .load(song.get(MainActivity.KEY_URLTOIMAGE).toString())
                        .resize(300, 200)
                        .into(holder.galleryImage);
            }
        }catch(Exception e) {}
        return convertView;
    }
}

class ListNewsViewHolder {
    ImageView galleryImage;
    TextView author, title, sdetails, time;
}
