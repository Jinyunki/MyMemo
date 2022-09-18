package com.example.mymemo.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MemoDao {

    @Query("SELECT * FROM Memo")
    List<Memo> getAll();

    @Query("SELECT * FROM Memo WHERE id = :memoId")
    Memo getMemoFromId(int memoId);

    @Query("SELECT * FROM Memo WHERE id IN (:memoDataId)")
    List<Memo> loadAllByIds(int[] memoDataId);

    // 삽입
    @Insert
    void insertAll(Memo... memoList);

    @Update
    void updateMemo(Memo memo);

    // 지우다
    @Delete
    void delete(Memo memo);

    @Query("DELETE FROM Memo WHERE id in (:memoList)")
    void deleteFromIds(List<Integer> memoList);

    @Query("DELETE FROM Memo WHERE id = :memoId")
    void delete(int memoId);

}
