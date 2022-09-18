package com.example.mymemo;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymemo.dao.Memo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MemoListHolder extends RecyclerView.ViewHolder {

    TextView tvTitle, tvContents, tvDate;
    CheckBox cb;
    boolean checkBox = false;
    List<Memo> memoList = new ArrayList<>();


    public MemoListHolder(@NonNull View itemView) {
        super(itemView);
        initView();
    }

    private void initView() {
        tvTitle = itemView.findViewById(R.id.tv_title);
        tvContents = itemView.findViewById(R.id.tv_contents);
        tvDate = itemView.findViewById(R.id.tv_date);
        cb = itemView.findViewById(R.id.cb_item);
        checkBox = cb.isChecked();
    }

    public void inData(MemoAdapter.ItemClickListener clickListener, Memo memo, int position) {

        tvTitle.setText(memo.getTitle());
        tvContents.setText(memo.getContents());

        Long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String getTime = sdf.format(date);

        tvDate.setText(getTime);

        cb.setChecked(memo.isChecked());
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null) {
                    clickListener.onClick(view, position, !memo.isChecked());
                }
            }
        });
    }

}
