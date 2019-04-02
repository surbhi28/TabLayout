package com.example.android.tablayout;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TabOne extends Fragment {

    public static final String LOG_TAG = TabOne.class.getName();
    ConnectionClass connectionClass;

    List<String> list = new ArrayList<>();

    ProgressBar pbbar;
    RecyclerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        View rootView = inflater.inflate(R.layout.tab1, container, false);

        connectionClass = new ConnectionClass();
        list = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        pbbar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        recyclerView.setHasFixedSize(true);
        //to use RecycleView, you need a layout manager. default is LinearLayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapter(getContext());
        recyclerView.setAdapter(adapter);

        new DoLogin().execute("");

        return rootView;
    }

    public class DoLogin extends AsyncTask<String, Void, List<String>> {

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<String> doInBackground(String... params) {
            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    String z = "Error in connection with SQL server";
                    Toast.makeText(getContext(), z, Toast.LENGTH_SHORT).show();
                } else {
                    String query = "select * from article where category = 'News' Order By lastupdatedate desc";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        String str = rs.getString("descriptionshort");
                        str = str.substring(30, str.length());
                        list.add(str);
                    }
                }
            } catch (Exception ex) {
                Log.d(LOG_TAG, ex.getMessage());
            }
            return list;
        }
        @Override
        protected void onPostExecute(List<String> list) {
            if (!(list.isEmpty())) {
                Log.d(LOG_TAG,list.get(0));
                pbbar.setVisibility(View.GONE);
                adapter.setData(list);
            }
        }
    }
}
