package com.example.doig_connor_s1823609;
// Connor Doig S1823609
import static com.example.doig_connor_s1823609.Constant.getCurrentDate;
import static com.example.doig_connor_s1823609.Constant.getDate;
import static com.example.doig_connor_s1823609.Constant.getEarthQuakesList;
import static com.example.doig_connor_s1823609.Constant.setDate;
import static com.example.doig_connor_s1823609.Constant.setEarthQuakesList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class MainActivity extends AppCompatActivity {

   ArrayAdapter arrayAdapter;
    EditText search_date;
    RecyclerView recyclerView;
    private Dialog loadingDialog;
    private String urlSource = "http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";



    // create array list
    ArrayList<Earthquake> earthquakeArrayList=new ArrayList<Earthquake>();
    ArrayList<Earthquake> earthquakeFinalArrayList=new ArrayList<Earthquake>();
    public  static   ArrayList<Earthquake> earthquakes=new ArrayList<Earthquake>();
    ArrayList<String> titleArrayList =new ArrayList<String>();
    ArrayList<String> dateArrayList =new ArrayList<String>();
    ArrayList<String> linkArrayList =new ArrayList<String>();
    // create list of colors
    public String[] mColors = {"FFFF00", "FFA500", "FF0000"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

         // get data form function that is stored in sharedPreference
          // if app is launch first time it return the empty text so we download the data
           // This will update the data every 24 hours
        if(getDate(this).equals("")||(!getDate(this).equals(getCurrentDate()))){
            startProgress();
        }
          // if current date is same with stored date we just load the data from sharedPreference
        else if(getDate(this).equals(getCurrentDate())){
               // call function to get data from sharedPreference
             loadDataFromPreference();
        }

        search_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }

            private void filter(String text) {
                ArrayList<Earthquake> filterlist=new ArrayList<>();
                for(Earthquake item: earthquakeFinalArrayList){
                    if(item.getPubDate().toLowerCase().contains(text.toLowerCase())){
                        filterlist.add(item);
                    }
                }
                arrayAdapter.filteredList(filterlist);
            }
        });

    }
    public void initView(){
        search_date=findViewById(R.id.search_date);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //loading dialog
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        // clear the array list data when app is start
        earthquakeArrayList.clear();
        earthquakeFinalArrayList.clear();
    }
    public void startProgress(){
        new Thread(new Task(urlSource)).start();
    }

    private class Task implements Runnable
    {
        private String url;
        public Task(String aurl)
        {
            url = aurl;
        }
        @Override
        public void run()
        {
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";
            Log.e("MyTag","in run");
            try
            {
                Log.e("MyTag","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new
                        InputStreamReader(yc.getInputStream()));
                Log.e("MyTag","after ready");

                while ((inputLine = in.readLine()) != null)
                {
                    //  result = result + inputLine;
                    Log.e("MyTag",inputLine);
                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception in run");
            }
            //
            // Now that you have the xml data you can parse it
            //
            // Now update the TextView to display raw XML data
            // Probably not the best way to update TextView
            // but we are just getting started !


            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");
                    new processInBackground().execute();
                }
            });
        }
    }







    public InputStream getInputStream(URL url){

        try {
            return url.openConnection().getInputStream();
        }
        catch (IOException e){
            return null;
        }
    }



    public class processInBackground extends AsyncTask<Integer ,Void ,Exception> {
        Exception exception =null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
              loadingDialog.show();
        }

        @Override
        protected Exception doInBackground(Integer... integers) {

            try
            {
                URL url=new URL(urlSource);
                XmlPullParserFactory xmlPullParserFactory=XmlPullParserFactory.newInstance();
                xmlPullParserFactory.setNamespaceAware(false);
                XmlPullParser xmlPullParser=xmlPullParserFactory.newPullParser();
                xmlPullParser.setInput(getInputStream(url),"UTF_8");
                boolean insideItem =false;
                int eventType =xmlPullParser.getEventType();
                while (eventType!=XmlPullParser.END_DOCUMENT){
                    if(eventType ==XmlPullParser.START_TAG){
                        if(xmlPullParser.getName().equalsIgnoreCase("item")){
                            insideItem =true;
                        }
                        else if(xmlPullParser.getName().equalsIgnoreCase("title")){
                            if(insideItem){
                                String currentString = xmlPullParser.nextText();
                                // split the title from sting
                                String[] separated = currentString.split(":");
                                // save in arraylist
                                titleArrayList.add( separated[0]);
                            }
                        }
                        else if(xmlPullParser.getName().equalsIgnoreCase("link")){
                            if(insideItem){
                                // save link in array list
                                linkArrayList.add(xmlPullParser.nextText());
                            }
                        }
                        else if(xmlPullParser.getName().equalsIgnoreCase("pubDate")){
                            if(insideItem){
                                // save date in array list
                                dateArrayList.add(xmlPullParser.nextText());
                            }
                        }
                        else if(xmlPullParser.getName().equalsIgnoreCase("description")){
                            if(insideItem){
                                String description = xmlPullParser.nextText();
                                // get depth from description
                                String[] separated = description.split("Depth:");
                                String[] part2=separated[1].split(" km");
                                String depthString =part2[0];
                                  // get Magnitude from description
                                String[] magnitudeSpread = description.split("Magnitude:");
                                 String stringMagnitude=magnitudeSpread[1];
                                // get Lat/long from description
                                String[] locationSpread = description.split("Lat/long:");
                                String[] locationSpread1=locationSpread[1].split("; Depth");
                                String stringlocation=locationSpread1[0];
                                 // save the depth,magnitude and location in array list
                                earthquakeArrayList.add(new Earthquake(stringlocation,stringMagnitude.replace(" ", ""),depthString));
                            }
                        }
                    }
                    else if(eventType ==XmlPullParser.END_TAG && xmlPullParser.getName().equalsIgnoreCase("item")){
                        insideItem =false;
                    }
                    eventType =xmlPullParser.next();
                }

            }
            catch (MalformedURLException e){
                exception=e;
            }
            catch (XmlPullParserException e){
                exception=e;
            }
            catch (IOException e){
                exception=e;
            }
            return exception;
        }

        @Override
        protected void onPostExecute(Exception s) {
            super.onPostExecute(s);
            loadingDialog.dismiss();
            // when all processes are completed the record is saved in sharedPreference
           saveData();
        }
    }

    public void loadDataFromPreference(){
         // get data from sharedPreference and set to array list
        earthquakeFinalArrayList=getEarthQuakesList(this);
          // initialize the arrayadapter
        arrayAdapter=new ArrayAdapter();
         // set adapter to recyclerview
        recyclerView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }
 public void saveData(){
     arrayAdapter=new ArrayAdapter();
     recyclerView.setAdapter(arrayAdapter);
     // start the loop on titlearray list
        for(int i=0;i<titleArrayList.size();i++){
            String location = earthquakeArrayList.get(i).getLocation();
            String magnitude = earthquakeArrayList.get(i).getMagnitude();
            String depth = earthquakeArrayList.get(i).getDepth();
            String title=titleArrayList.get(i);
            String link=linkArrayList.get(i);
            String pubDate=dateArrayList.get(i);
               // set all the data from different arraylist into final array list
            earthquakeFinalArrayList.add(new Earthquake(title,pubDate,link,location, magnitude,depth));
            arrayAdapter.notifyDataSetChanged();
        }
        // save arraylist data in sharedPreference
        setEarthQuakesList(this,earthquakeFinalArrayList);
        // update the date and save in sharedPreference
        setDate(this,getCurrentDate());


 }
    public class ArrayAdapter extends RecyclerView.Adapter<ArrayAdapter.ImageViewHolder> {

        public ArrayAdapter(){
            earthquakes=earthquakeFinalArrayList;
        }
        @NonNull
        @Override
        public ArrayAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(MainActivity.this).inflate(R.layout.item_row,parent,false);
            return  new ImageViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final ArrayAdapter.ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.title.setText("Title: "+earthquakes.get(position).getTitle());
            holder.magnitude.setText("Magnitude: "+earthquakes.get(position).getMagnitude());
            holder.date.setText("Date: "+earthquakes.get(position).getPubDate());

             if(!search_date.getText().toString().isEmpty()){

                 double mangitude = Double.parseDouble(earthquakes.get(position).getMagnitude());
                 if(mangitude<=1.0){

                     // makes the text yellow for low magnitude
                     holder.magnitude.setTextColor(Color.parseColor ("#"+mColors[0]));
                     holder.date.setTextColor(Color.parseColor ("#"+mColors[0]));
                     holder.title.setTextColor(Color.parseColor ("#"+mColors[0]));
                 }
                 else if(mangitude>=1.1||mangitude<=1.9){
                     // makes the text orange for medium magnitude
                     holder.magnitude.setTextColor(Color.parseColor ("#"+mColors[1]));
                     holder.date.setTextColor(Color.parseColor ("#"+mColors[1]));
                     holder.title.setTextColor(Color.parseColor ("#"+mColors[1]));
                 }
                 else if(mangitude>=2.0){
                     // makes the text red for high magnitude
                     holder.magnitude.setTextColor(Color.parseColor ("#"+mColors[2]));
                     holder.date.setTextColor(Color.parseColor ("#"+mColors[2]));
                     holder.title.setTextColor(Color.parseColor ("#"+mColors[2]));
                 }


             }

        holder.cardView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,DetailActivity.class)
                        .putExtra("position",position));
            }
        });

        }
        // function to filter the data when user is searching
        public void filteredList(ArrayList<Earthquake> filterlist) {
            earthquakes=filterlist;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return earthquakes.size();
        }

        public class ImageViewHolder extends RecyclerView.ViewHolder {
            TextView title,magnitude,date;

            CardView cardView;
            public ImageViewHolder(@NonNull View itemView) {
                super(itemView);
                // create references from item_row xml to show the data in recyclerview
                title=itemView.findViewById(R.id.title);
                magnitude=itemView.findViewById(R.id.magnitude);
                date=itemView.findViewById(R.id.date);
                cardView=itemView.findViewById(R.id.carView);
            }
        }
    }

}
