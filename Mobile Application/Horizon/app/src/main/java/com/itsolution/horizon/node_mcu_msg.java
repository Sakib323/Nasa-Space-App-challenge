package com.itsolution.horizon;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.L;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class node_mcu_msg extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    public TextInputEditText editText;
    public ImageView send;
    private List< Message > mMessages;
    private MessageAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_mcu_msg);



        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.hide();

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.top_msg_bar));
        }

        mRecyclerView = findViewById(R.id.recycler_view);
        mMessages = new ArrayList< >();
        mAdapter = new MessageAdapter(mMessages);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);


        editText=findViewById(R.id.msg);
        send=findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString()!=null){

                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
                        String ssid = wifiInfo.getSSID();

                        int ip=wifiInfo.getIpAddress();
                        Log.e("ip",String.valueOf(ip));

                        mMessages.add(new Message(editText.getText().toString(), true));
                        mAdapter.notifyItemInserted(mMessages.size() - 1);
                        editText.getText().clear();

                        String url = "http://192.168.4.1/get?data="+editText.getText().toString();
                        RequestQueue queue = Volley.newRequestQueue(node_mcu_msg.this);
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        mMessages.add(new Message(response, false));
                                        mAdapter.notifyItemInserted(mMessages.size() - 1);
                                        editText.getText().clear();


                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("error","error here");
                            }
                        });
                        queue.add(stringRequest);

                    }else {
                        Dialog communication=new Dialog(node_mcu_msg.this);
                        communication.setContentView(R.layout.no_blackbox);
                        communication.getWindow().setBackgroundDrawableResource(R.color.transparent);
                        communication.setCancelable(true);
                        communication.show();
                    }
                }
            }
        });
    }

}