package com.example.mymemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mymemo.dao.Memo;
import com.example.mymemo.dao.MemoDB;
import com.example.mymemo.dao.MemoDao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AddActivity extends AppCompatActivity {

    final static String EXTRA_MEMO_ID = "EXTRA_MEMO_ID";

    MemoDao memoDao = null;
    EditText etTitle,etContents;
    Button btAdd,btDelete;

    int memoId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        memoId = intent.getIntExtra(EXTRA_MEMO_ID, -1);
        if (memoId >= 0) {
            // memo click move
            // db select
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Memo memo = getMemoDao().getMemoFromId(memoId);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            setMemoData(memo);
                        }
                    });
                }
            }).start();
            btDelete.setVisibility(View.VISIBLE);
            btAdd.setText("UPDATE");
        } else {
            btDelete.setVisibility(View.GONE);
            btAdd.setText("SAVE");
        }
    }

    private MemoDao getMemoDao() {
        if (memoDao == null) {
            memoDao = MemoDB.getInstance(getApplicationContext()).memoDao();
        }
        return memoDao;
    }

    private void setMemoData(Memo memo) {
        etTitle.setText(memo.getTitle());
        etContents.setText(memo.getContents());
    }

    private void initView() {
        etTitle = findViewById(R.id.et_title);
        etContents = findViewById(R.id.et_contents);
        btAdd = findViewById(R.id.bt_contentsAdd);
        btDelete = findViewById(R.id.bt_contentsDelete);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = etTitle.getText().toString();
                String content = etContents.getText().toString();
                
                if (title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(AddActivity.this, "input Text!!!!!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                if (memoId >= 0) {
                    /**
                     * update
                     */
                    Memo memo = new Memo(memoId, title, content);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getMemoDao().updateMemo(memo);
                            updateOrSave();
                        }
                    }).start();

                } else {
                    /**
                     * insert
                     */
                    Memo memo = new Memo(title, content);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getMemoDao().insertAll(memo);
                            updateOrSave();
                        }
                    }).start();
                }
            }
        });

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getMemoDao().delete(memoId);
                        updateOrSave();
                    }
                }).start();
            }
        });
    }

    private void updateOrSave() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    class InsertRunnable implements Runnable {

        @Override
        public void run() {
            Memo memo = new Memo();
            memo.setTitle(etTitle.getText().toString());
            memo.setContents(etContents.getText().toString());
            getMemoDao().insertAll(memo);
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        MemoDB.destroyInstance();
    }
}