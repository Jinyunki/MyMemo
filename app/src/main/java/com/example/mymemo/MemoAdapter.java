package com.example.mymemo;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymemo.dao.Memo;

import java.util.ArrayList;
import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<MemoListHolder> {

    interface ItemClickListener {
        void onClick(View v, int position, boolean isChecked);
    }

    private ItemClickListener clickListener = null;
    private ArrayList<Memo> memoList = new ArrayList<>();
    private Activity activity;

    private MemoAdapter() { }
    public MemoAdapter(Activity activity) {
        this.activity = activity;
        this.clickListener = (ItemClickListener) activity;
    }

    @NonNull
    @Override
    public MemoListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_memo, parent, false);
        MemoListHolder memoListHolder = new MemoListHolder(view);
        return memoListHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MemoListHolder holder, int position) {
        Memo item = memoList.get(position);
        holder.inData(this.clickListener, item, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(activity, AddActivity.class);
                intent.putExtra(AddActivity.EXTRA_MEMO_ID, item.getId());

                /**
                 * startActivityForResult 를 사용하기 위해서는 Activity 가 필요하기때문에
                 * Context 를 Activity 로 변경
                 */
                activity.startActivityForResult(intent, MainActivity.REQUEST_MEMO_DETAIL_MOVE);
            }
        });
    }

    public void addAll(List<Memo> memoList) {
        this.memoList.clear();
        this.memoList.addAll(memoList);
    }

    public void Clear(int position){
        this.memoList.get(position);
        this.memoList.clear();
    }

    public List<Memo> getMemoList() {
        return this.memoList;
    }

    public Memo getMemo(int position) {
        return this.memoList.get(position);
    }

    // 체크된 메모의 아이디의 리스트들을 반환
    public List<Integer> getCheckedMemoList() {
        ArrayList<Integer> tempList = new ArrayList<>();
        for (Memo memo : this.memoList) {
            if (memo.isChecked()) {
                tempList.add(memo.getId());
            }
        }
        return tempList;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return memoList.size();
    }
}
