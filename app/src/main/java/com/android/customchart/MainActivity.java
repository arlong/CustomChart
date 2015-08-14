package com.android.customchart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.customchart.widget.CustomChartView;

public class MainActivity extends AppCompatActivity {
    private static int[] VALUES={ 50,10,30,50,0,80,100,90,8,10};
    private static  String[] AXISTEXT = { "柱形图一","柱形图二","柱形图三","柱形图四","柱形图五","柱形图六","柱形图七","柱形图八","柱形图九","柱形图十"};
    private CustomChartView mChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initViews();
    }

    private void initViews() {
        mChartView = (CustomChartView) findViewById(R.id.custom_chart);
        mChartView.init(VALUES,AXISTEXT);
    }


}
