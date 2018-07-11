package midascash.indonesia.optima.prima.midascash.extramenuactivity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import midascash.indonesia.optima.prima.midascash.R;
import midascash.indonesia.optima.prima.midascash.formula.MyMarkerView;
import midascash.indonesia.optima.prima.midascash.generator;

public class accounttransactions extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener,
        OnChartGestureListener, OnChartValueSelectedListener {

    DecimalFormat formatter = new DecimalFormat("###,###,###.00");

    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    FirebaseFirestore db;
    LineChart mChart;

    TextView balance,createdate,lastused,initial;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_account_eye);

        mChart = findViewById(R.id.chart1);

        balance = findViewById(R.id.balance_current);
        createdate = findViewById(R.id.created);
        lastused = findViewById(R.id.lastused);
        initial = findViewById(R.id.initial);

        Bundle bundle = getIntent().getExtras();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        db = FirebaseFirestore.getInstance();

        db.collection("account")
                .whereEqualTo("account_name",bundle.getString("account_name"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                balance.setText(formatter.format(generator.makedouble(document.getData().get("account_balance_current").toString())) + " "+ document.getData().get("account_currency").toString() );
                                createdate.setText(df.format((Date) document.getData().get("account_createdate")));
                                if(document.getData().get("account_lastused")==null){
                                    lastused.setText("-");
                                }else {
                                    lastused.setText(document.getData().get("account_lastused").toString());
                                }
                                initial.setText(formatter.format(generator.makedouble(document.getData().get("account_balance").toString())));

                            }


                            mChart.setOnChartGestureListener(accounttransactions.this);
                            mChart.setOnChartValueSelectedListener(accounttransactions.this);
                            mChart.setDrawGridBackground(false);

                            // no description text
                            mChart.getDescription().setEnabled(false);

                            // enable touch gestures
                            mChart.setTouchEnabled(true);

                            // enable scaling and dragging
                            mChart.setDragEnabled(true);
                            mChart.setScaleEnabled(true);
                            // mChart.setScaleXEnabled(true);
                            // mChart.setScaleYEnabled(true);

                            // if disabled, scaling can be done on x- and y-axis separately
                            mChart.setPinchZoom(true);

                            // set an alternative background color
                            // mChart.setBackgroundColor(Color.GRAY);

                            // create a custom MarkerView (extend MarkerView) and specify the layout
                            // to use for it
                            MyMarkerView mv = new MyMarkerView(accounttransactions.this, R.layout.custom_marker_view);
                            mv.setChartView(mChart); // For bounds control
                            mChart.setMarker(mv); // Set the marker to the chart

                            // x-axis limit line
                            LimitLine llXAxis = new LimitLine(10f, "Index 10");
                            llXAxis.setLineWidth(4f);
                            llXAxis.enableDashedLine(10f, 10f, 0f);
                            llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
                            llXAxis.setTextSize(10f);

                            XAxis xAxis = mChart.getXAxis();
                            xAxis.enableGridDashedLine(10f, 10f, 0f);
                            //xAxis.setValueFormatter(new MyCustomXAxisValueFormatter());
                            //xAxis.addLimitLine(llXAxis); // add x-axis limit line


                            LimitLine ll1 = new LimitLine(150f, "Upper Limit");
                            ll1.setLineWidth(4f);
                            ll1.enableDashedLine(10f, 10f, 0f);
                            ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                            ll1.setTextSize(10f);

                            LimitLine ll2 = new LimitLine(-30f, "Lower Limit");
                            ll2.setLineWidth(4f);
                            ll2.enableDashedLine(10f, 10f, 0f);
                            ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
                            ll2.setTextSize(10f);

                            YAxis leftAxis = mChart.getAxisLeft();
                            leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
                            leftAxis.addLimitLine(ll1);
                            leftAxis.addLimitLine(ll2);
                            leftAxis.setAxisMaximum(200f);
                            leftAxis.setAxisMinimum(-50f);
                            //leftAxis.setYOffset(20f);
                            leftAxis.enableGridDashedLine(10f, 10f, 0f);
                            leftAxis.setDrawZeroLine(false);

                            // limit lines are drawn behind data (and not on top)
                            leftAxis.setDrawLimitLinesBehindData(true);

                            mChart.getAxisRight().setEnabled(false);

                            //mChart.getViewPortHandler().setMaximumScaleY(2f);
                            //mChart.getViewPortHandler().setMaximumScaleX(2f);

                            // add data
                            setData(45, 100);

//        mChart.setVisibleXRange(20);
//        mChart.setVisibleYRange(20f, AxisDependency.LEFT);
//        mChart.centerViewTo(20, 50, AxisDependency.LEFT);

                            mChart.animateX(2500);
                            //mChart.invalidate();

                            // get the legend (only possible after setting data)
                            Legend l = mChart.getLegend();

                            // modify the legend ...
                            l.setForm(Legend.LegendForm.LINE);

                            // // dont forget to refresh the drawing
                            // mChart.invalidate();
                            
                        } else {
                            Log.w("Get account error", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home) {
            finish();
        }
        return  true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.line, menu);
        return true;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mChart.invalidate();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    private void setData(int count, float range) {

        ArrayList<Entry> values = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {

            float val = (float) (Math.random() * range) + 3;
            values.add(new Entry(i, val, getResources().getDrawable(R.drawable.star)));
        }

        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "DataSet 1");

            set1.setDrawIcons(false);

            // set the line to be drawn like this "- - - - - -"
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                set1.setFillDrawable(drawable);
            }
            else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            mChart.setData(data);
        }
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "START, x: " + me.getX() + ", y: " + me.getY());
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "END, lastGesture: " + lastPerformedGesture);

        // un-highlight values after the gesture is finished and no single-tap
        if(lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
            mChart.highlightValues(null); // or highlightTouch(null) for callback to onNothingSelected(...)
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i("LongPress", "Chart longpressed.");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());
        Log.i("LOWHIGH", "low: " + mChart.getLowestVisibleX() + ", high: " + mChart.getHighestVisibleX());
        Log.i("MIN MAX", "xmin: " + mChart.getXChartMin() + ", xmax: " + mChart.getXChartMax() + ", ymin: " + mChart.getYChartMin() + ", ymax: " + mChart.getYChartMax());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }
}
