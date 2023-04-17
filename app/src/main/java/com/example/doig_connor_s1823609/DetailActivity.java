package com.example.doig_connor_s1823609;
// Connor Doig S1823609
import static com.example.doig_connor_s1823609.MainActivity.earthquakes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
       TextView title_location,title_depth,title_date,title_magnitude,title_link;
       int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        // call function to initialize the references
          initView();
          // call function to load data
          loadData();


    }
    public void initView(){
        // create references
        title_location=findViewById(R.id.title_location);
        title_depth=findViewById(R.id.title_depth);
        title_date=findViewById(R.id.title_date);
        title_magnitude=findViewById(R.id.title_magnitude);
        title_link=findViewById(R.id.title_link);
    }
    public  void loadData(){
        // get position number from intent
        position=getIntent().getIntExtra("position",-1);
          // load data on the base of position
           // earthquakes is the array list that is defined in MainActivity

        title_date.setText("Date: "+earthquakes.get(position).getPubDate());
        title_location.setText("Location: "+earthquakes.get(position).getLocation());
        title_depth.setText("Depth: "+earthquakes.get(position).getDepth()+" Km");
        title_magnitude.setText("Magnitude: "+earthquakes.get(position).getMagnitude());
        title_link.setText(earthquakes.get(position).getLink());
    }


}