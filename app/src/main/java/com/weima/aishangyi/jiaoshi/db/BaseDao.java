package com.weima.aishangyi.jiaoshi.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.weima.aishangyi.jiaoshi.app.WMApplication;

public class BaseDao {

	
	/**
	 * function: 查询
	 *
	 * @param sql
	 * @param arg
	 * @return
	 * 
	 * @ author:wangbin 2011-12-26 下午3:43:17
	 */
	public Cursor rawQuery(String sql,String[] arg){
		SQLiteDatabase db = WMApplication.getSQLiteDataBase();
		Cursor	cursor = db.rawQuery(sql, arg);
		return cursor;
	}
	
	/**
	 * function: 单线程插入删除操作
	 *
	 * @param sql
	 * @param arg
	 * 
	 * @ author:wangbin 2011-12-24 下午02:39:11
	 */
	public synchronized void execSQL(String sql,String[] arg){
		SQLiteDatabase db = WMApplication.getSQLiteDataBase();
		//db.beginTransaction();
		db.execSQL(sql, arg);
		//db.setTransactionSuccessful();
		//db.endTransaction();
	}
	
	
	/**
	 * function: 多线程专用
	 *
	 * @param sql
	 * 
	 * @ author:wangbin 2011-12-24 下午02:39:00
	 */
	public  void execSQL(String sql){
		SQLiteDatabase db = WMApplication.getSQLiteDataBase();
		db.execSQL(sql);
	}

	
}
