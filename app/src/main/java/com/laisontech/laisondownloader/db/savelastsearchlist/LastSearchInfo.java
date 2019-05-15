package com.laisontech.laisondownloader.db.savelastsearchlist;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by lu on 2018/1/10.
 * 保存历史搜索记录
 */
@DatabaseTable(tableName = "LastSearchInfo")
public class LastSearchInfo implements Serializable{

    @DatabaseField(generatedId = true, dataType = DataType.INTEGER)
    private int id;//自增序列

    @DatabaseField(columnName = "queryCode",dataType = DataType.STRING)
    private String queryCode;//搜索关键字

    @DatabaseField(columnName = "queryDate",dataType = DataType.LONG)
    private long queryDate; //搜索时间
    public LastSearchInfo(){

    }
    public LastSearchInfo(String queryCode,Long queryDate){
        this.queryCode = queryCode;
        this.queryDate = queryDate;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQueryCode() {
        return queryCode;
    }

    public void setQueryCode(String queryCode) {
        this.queryCode = queryCode;
    }

   public long getQueryDate() {
       return queryDate;
   }

    public void setQueryDate(Long queryDate) {
        this.queryDate = queryDate;
    }

    @Override
    public String toString() {
        return "LastSearchInfo{" +
                "id=" + id +
                ", queryCode='" + queryCode + '\'' +
                ", queryDate=" + queryDate +
                '}';
    }
}
