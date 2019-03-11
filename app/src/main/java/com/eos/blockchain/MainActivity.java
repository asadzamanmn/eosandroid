package com.eos.blockchain;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    private List<String> listHeader = new ArrayList<>();

    private HashMap<String, List<String> > listChild =
            new HashMap<String, List<String>>();

    private ExpandableListView listView;

    private ProgressBar mProgressbar;

    private CustomAdapter customAdapter;

    private Button mLoad;

    private ArrayList<String> JsonArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = findViewById(R.id.mListView);
        mLoad  = findViewById(R.id.mLoad);
        mProgressbar = findViewById(R.id.mProgressbar);

        customAdapter = new CustomAdapter(this, listHeader, listChild);
        mLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Clear data and List
                listHeader.clear();
                listChild.clear();
                JsonArrayList.clear();
                customAdapter.notifyDataSetChanged();
                mProgressbar.setVisibility(View.VISIBLE);

                //Call the getLastUpdateBlockId
                getLastUpdatedBlockID();

            }
        });

    }


    private void getLastUpdatedBlockID(){

        /*
         *
         *this method gets Last UPDATED BLOCK ID
         *
         * */

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_HEAD_BLOCK_NUMBER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj = null;
                        try {

                            obj = new JSONObject(response);
                            int head_block_num = obj.getInt("head_block_num");
                            String time = obj.getString("head_block_time");

                            for (int i=0; i<10; i++){
                                getBlock(head_block_num-i , String.valueOf(head_block_num-i));
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch(JSONException e){
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        Volley.newRequestQueue(this).add(stringRequest);

    }


    private void getBlock(final int id, String block_num){
        StringRequest myReq = new StringRequest(Request.Method.POST,
                Constants.URL_GET_BLOCKS,
                createMyReqSuccessListener(block_num),
                createMyReqErrorListener()) {

            @Override
            public byte[] getBody() {
                String str = "{\"block_num_or_id\":\""+id+"\"}";
                return str.getBytes();
            }

            public String getBodyContentType()
            {
                return "application/json; charset=utf-8";
            }

        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(myReq);
    }

    private Response.Listener<String> createMyReqSuccessListener(final String block_num) {
        final List<List<String>> lists = new ArrayList<List<String>>();

        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    listHeader.add(block_num + "," + obj.getString("timestamp"));
                    JsonArrayList.add(
                        "{"+
                        "\n\"previous\""+":"+ "\""+obj.getString("previous")+"\",\n"+
                        "\"timestamp\""+":"+ "\""+obj.getString("timestamp")+"\",\n"+
                        "\"transaction_mroot\""+":"+ "\""+obj.getString("transaction_mroot")+"\",\n"+
                        "\"action_mroot\""+":"+ "\""+obj.getString("action_mroot")+"\",\n"+
                        "\"producer\""+":"+ "\""+obj.getString("producer")+"\",\n"+
                        "\"schedule_version\""+":"+ "\""+obj.getString("schedule_version")+"\",\n"+
                        "\"producer_signature\""+":"+ "\""+obj.getString("producer_signature")+"\",\n"+
                        "}"
                    );

                    for (int i=0; i<listHeader.size(); i++){
                        final List<String> list = new ArrayList<>();
                        lists.add(list);
                    }

                    for (int i=0; i<lists.size(); i++){
                        lists.get(i).add(JsonArrayList.get(i));
                    }

                    for (int i=0; i<listHeader.size(); i++){
                        listChild.put(listHeader.get(i), lists.get(i));
                    }

                    mProgressbar.setVisibility(View.GONE);
                    listView.setAdapter(customAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };
    }


}
