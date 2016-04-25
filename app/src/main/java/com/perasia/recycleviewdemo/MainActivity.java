package com.perasia.recycleviewdemo;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BaseRecycleViewAdapter.RequestLoadMoreListener {

    private RecyclerView mRecyclerView;

    private Context mContext;

    private MainAdapter mainAdapter;

    private ArrayList<String> mDatas;

    private boolean isFirstReq = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleview);

        initView();
    }

    private void initView() {
        mDatas = new ArrayList<>();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST));

        mainAdapter = new MainAdapter(mContext, mDatas);

        mRecyclerView.setAdapter(mainAdapter);

        mainAdapter.setOnRecycleViewItemClickListener(new BaseRecycleViewAdapter.OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(mContext, "click=" + position, Toast.LENGTH_SHORT).show();
            }
        });

        isFirstReq = true;

        getData();

    }

    @Override
    public void onLoadMoreRequested() {
        getData();
    }

    private void getData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    mDatas.add("title" + i);
                }
                mainAdapter.clear();
                mainAdapter.appendToList(mDatas);

                if (isFirstReq) {
                    mainAdapter.setOnLoadMoreListener(10, MainActivity.this);
                    isFirstReq = false;
                    mainAdapter.setHasFooter(true);
                }
            }
        }, 1000);
    }
}
