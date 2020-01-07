package com.example.sat.jsonparsehttp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends Activity {

    @BindView(R.id.imgDetail)
    ImageView imgDetail;
    @BindView(R.id.tvRank)
    TextView tvRank;
    @BindView(R.id.tvCountry)
    TextView tvCountry;
    @BindView(R.id.tvPopulation)
    TextView tvPopulation;
    String rank1, country, pop, nFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent dapat = getIntent();
        rank1 = dapat.getStringExtra("rank");
        country = dapat.getStringExtra("country");
        pop = dapat.getStringExtra("population");
        nFlag = dapat.getStringExtra("flag");

        tvRank.setText(rank1);
        tvCountry.setText(country);
        tvPopulation.setText(pop);
        Picasso.with(getApplicationContext()).load(nFlag).placeholder(R.drawable.imgno).into(imgDetail);

    }
}
