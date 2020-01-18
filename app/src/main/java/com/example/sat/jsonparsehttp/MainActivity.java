package com.example.sat.jsonparsehttp;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.sat.jsonparsehttp.model.ModelCountry;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {
    private static final int DEBUG = 1;

    CountryAdapter adapter;
    ListView lsCountry;
    Context context = this;
    AQuery aQuery;
    ArrayList<ModelCountry> dataCountry;
    // original http://www.androidbegin.com/tutorial/jsonparsetutorial.txt
    public static final String API_URL = "https://rawcdn.githack.com/sachiotomita/JSONParseHTTPSample/ef3dfdb94ab1fc741dab06e02fc308db9a1dbbfb/sample-data.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lsCountry = (ListView) findViewById(R.id.listview);

        aQuery = new AQuery(context);

        dataCountry = new ArrayList<>();

        getDataCountry();
        lsCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // ModelCountry m = new ModelCountry();
                Intent kirimData = new Intent(MainActivity.this, DetailActivity.class);
                kirimData.putExtra("rank", dataCountry.get(i).getRank());
                kirimData.putExtra("country", dataCountry.get(i).getCountry());
                kirimData.putExtra("population", dataCountry.get(i).getPopulation());
                kirimData.putExtra("flag", dataCountry.get(i).getFlag());
                startActivity(kirimData);

            }
        });
    }

    public void getDataCountry() {
        String url = API_URL;
        Map<String, String> paramps = new HashMap<>();
        //menambahkan progress dialog

        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setMessage("Loading");

        try {
            pre("url : " + url + ", params " + paramps.toString());
            aQuery.progress(dialog).ajax(url, String.class, new AjaxCallback<String>() {
                @Override
                public void callback(String url, String hasil, AjaxStatus status) {
                    if (hasil != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(hasil);
//                           String result = jsonObject.getString("result");

                            JSONArray jsonArray = jsonObject.getJSONArray("worldpopulation");
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject object = jsonArray.getJSONObject(a);
                                ModelCountry modelCountry = new ModelCountry();
                                modelCountry.setRank(object.getString("rank"));
                                modelCountry.setCountry(object.getString("country"));
                                modelCountry.setPopulation(object.getString("population"));
                                modelCountry.setFlag(object.getString("flag"));

                                dataCountry.add(modelCountry);
                                adapter = new CountryAdapter(context, dataCountry);
                                lsCountry.setAdapter(adapter);


                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error Parsing Data", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Error no Data", Toast.LENGTH_SHORT).show();

                    }

                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error get  Data", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    public class CountryAdapter extends BaseAdapter {

        private Context c;
        private ArrayList<ModelCountry> countries;
        private LayoutInflater inflater;

        public CountryAdapter(Context c, ArrayList<ModelCountry> country) {
            this.c = c;
            countries = new ArrayList<>();
            this.countries = country;
        }


        @Override
        public int getCount() {
            return countries.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public class ViewHolder {
            public TextView itemRank, itemPopulation, itemCountry;
            public ImageView imgFlag;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;

            if (view == null) {
                holder = new ViewHolder();
                inflater = (LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_item, null);
                holder.itemRank = (TextView) view.findViewById(R.id.tvRankItem);
                holder.itemCountry = (TextView) view.findViewById(R.id.tvCountryItem);
                holder.itemPopulation = (TextView) view.findViewById(R.id.tvPopulationItem);
                holder.imgFlag = (ImageView) view.findViewById(R.id.imgList);
                view.setTag(holder);

            } else {
                holder = (ViewHolder) view.getTag();
            }

            ModelCountry modelCountry = countries.get(i);
            //menampilkan data ke widget
            holder.itemCountry.setText(modelCountry.getCountry());
            holder.itemRank.setText("Rank: " + modelCountry.getRank());
            holder.itemPopulation.setText("Population: " + modelCountry.getPopulation());
            Picasso.with(getApplicationContext()).load(modelCountry.getFlag()).placeholder(R.drawable.imgno).
                    into(holder.imgFlag);
            return view;
        }
    }

    public static void pre(String pesan) {
        try {
            if (DEBUG == 1) {
                System.out.println(pesan);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

}
