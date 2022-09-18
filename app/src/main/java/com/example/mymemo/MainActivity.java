package com.example.mymemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mymemo.dao.Memo;
import com.example.mymemo.dao.MemoDB;
import com.example.mymemo.dao.MemoDao;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MemoAdapter.ItemClickListener {

    public final static int REQUEST_MEMO_DETAIL_MOVE = 3333;

    private Context context = null;
    private MemoAdapter memoAdapter;
    private MemoDao memoDao;



    private Button btAdd,btDelete;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        context = getApplicationContext();
        loadData();
    }

    private void initView() {
        btAdd = findViewById(R.id.bt_add);
        btDelete = findViewById(R.id.bt_delete);
        rv = findViewById(R.id.rv_memoList);
        memoAdapter = new MemoAdapter(MainActivity.this);
        rv.setAdapter(memoAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
        rv.setLayoutManager(linearLayoutManager);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                startActivityForResult(intent, REQUEST_MEMO_DETAIL_MOVE);
            }
        });


        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<Integer> checkedList = memoAdapter.getCheckedMemoList();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                getMemoDao().deleteFromIds(checkedList);
                                loadData();
                            }
                        }).start();
                    }
                }).start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_MEMO_DETAIL_MOVE:
                Log.e("MainActivity", "onActivityResult, notify");
                loadData();
                break;
        }
    }

    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Memo> tempList = getMemoDao().getAll();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        memoAdapter.addAll(tempList);
                        memoAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MemoDB.destroyInstance();
    }
    private MemoDao getMemoDao() {
        if (memoDao == null) {
            memoDao = MemoDB.getInstance(getApplicationContext()).memoDao();
        }
        return memoDao;
    }

    @Override
    public void onClick(View v, int position, boolean isChecked) {
        if (isChecked) {
            Toast.makeText(this, position + "번 이 Check 되었습니다", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Check 가 취소 되었습니다", Toast.LENGTH_SHORT).show();
        }
        memoAdapter.getMemo(position).setChecked(isChecked);
        memoAdapter.notifyItemChanged(position);

        for (Memo memo : memoAdapter.getMemoList()) {
            System.out.println("id : " + memo.getId() + ", isChecked : " + memo.isChecked());
        }
    }
}