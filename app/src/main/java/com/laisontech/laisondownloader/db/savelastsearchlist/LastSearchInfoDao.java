package com.laisontech.laisondownloader.db.savelastsearchlist;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.laisontech.laisondownloader.db.DBHelper;
import com.laisontech.laisondownloader.db.IDBDao;
import com.laisontech.laisondownloader.utils.ViewUtils;
import com.laisontech.laisondownloader.utils.ViewUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by admin on 2018/1/10.
 */

public class LastSearchInfoDao implements IDBDao<LastSearchInfo> {
    private Dao<LastSearchInfo, Integer> mDao;

    //初始化
    public LastSearchInfoDao(Context context) {
        DBHelper helper = DBHelper.getInstance(context);
        try {
            mDao = helper.getDao(LastSearchInfo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert(LastSearchInfo lastSearchInfo) {
        try {
            mDao.create(lastSearchInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createOrUpdate(LastSearchInfo lastSearchInfo) {
        try {
            mDao.createOrUpdate(lastSearchInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(LastSearchInfo lastSearchInfo) {
        try {
            mDao.update(lastSearchInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateById(LastSearchInfo lastSearchInfo, Integer id) {

    }

    @Override
    public void delete(LastSearchInfo lastSearchInfo) {

    }

    @Override
    public void deleteList(List<LastSearchInfo> list) {

    }

    @Override
    public List<LastSearchInfo> listAll() {
        try {
            return mDao
                    .queryBuilder()
                    .orderBy("queryDate", false)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<LastSearchInfo> queryInfoList(long index, long page) {
        try {
            return mDao
                    .queryBuilder()
                    .orderBy("queryDate", false)
                    .offset(index)
                    .limit(page)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //根据关键字进行查询
    public List<LastSearchInfo> listForQuery(String queryCode) {
        try {
            return mDao
                    .queryBuilder()
                    .where()
                    .eq("queryCode", queryCode)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<LastSearchInfo> listFuzzyAll(String tex) {
        return null;
    }

    //更新数据库数据，如果没有则插入数据库中，如果有，更新
    public void updateSearchInfo(String searchResult) {
        List<LastSearchInfo> lastSearchInfos = listForQuery(searchResult);
        long time = System.currentTimeMillis();
        LastSearchInfo searchInfo;
        if (lastSearchInfos == null || lastSearchInfos.size() < 1) {
            //无则插入新的数据
            searchInfo = new LastSearchInfo();
            searchInfo.setQueryCode(searchResult);
            searchInfo.setQueryDate(time);
            insert(searchInfo);
        } else {
            searchInfo = lastSearchInfos.get(0);
            searchInfo.setQueryDate(time);
            update(searchInfo);
        }
    }

    public boolean saveSearchResult(String record) {
        if (ViewUtils.isEmptyStr(record)) return false;
        List<LastSearchInfo> lastSearchInfos = listForQuery(record);
        long time = System.currentTimeMillis();
        LastSearchInfo searchInfo;
        if (lastSearchInfos == null || lastSearchInfos.size() < 1) {
            //无则插入新的数据
            searchInfo = new LastSearchInfo();
            searchInfo.setQueryCode(record);
            searchInfo.setQueryDate(time);
            insert(searchInfo);
        } else {
            searchInfo = lastSearchInfos.get(0);
            searchInfo.setQueryDate(time);
            update(searchInfo);
        }
        return true;
    }
}
