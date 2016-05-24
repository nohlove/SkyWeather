package com.skyweather.app.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.skyweather.app.model.City;
import com.skyweather.app.model.County;
import com.skyweather.app.model.Province;

public class SkyWeatherDB {
	/**
	 * 数据库名
	 */
	public static final String DB_NAME = "sky_weather";
	/**
	 * 数据库版本
	 */
	public static final int VERSION = 1;
	private static SkyWeatherDB skyWeatherDB;
	private SQLiteDatabase db;
	
	/**
	 * 私有化构造方法
	 * @param context
	 */
	private SkyWeatherDB(Context context){
		SkyWeatherOpenHelper  dbHelper = new SkyWeatherOpenHelper
				(context, DB_NAME, null, VERSION);
		db=dbHelper.getWritableDatabase();
	}
	
	/**
	 * 获取SkyWeatherDB的实例--单例模式，确保全局只有一个SkyWeatherDB的实例
	 * @param context
	 * @return
	 */
	public synchronized static SkyWeatherDB getInstance(Context context){
		if(skyWeatherDB == null){
			skyWeatherDB = new SkyWeatherDB(context);
		}
		return skyWeatherDB;		
	}
	
	/**
	 * 将Province实例存储到数据库
	 * @param province
	 */
	public void saveProvince(Province province){
		if(province != null){
			ContentValues values = new ContentValues();
			values.put("province_name",province.getProvinceName());
			values.put("province_code",province.getProvinceCode());
			db.insert("Province", null, values);
		}
	}
	
	/**
	 * 从数据库读取全国所有的省份信息
	 * @return
	 */
	public List<Province> loadProvinces(){
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query("Province",null,null,null,null,null,null);
		if(cursor.moveToNext()){
			do{
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(province);
			}while(cursor.moveToNext());
			
			if(cursor != null){
				cursor.close();
			}
			}
			return list;
		}
	
		/**
		 * 将City实例存储到数据库
		 * @param city
		 */
		public void saveCity(City city){
			if(city != null){
				ContentValues values = new ContentValues();
				values.put("city_name",city.getCityName());
				values.put("city_code",city.getCityCode());
				values.put("province_id",city.getProvinceId());
			    db.insert("City", null, values);
			}
		}
		
		/**
		 * 从数据库中读取某省下所有城市的信息
		 * @return
		 */
		public List<City> loadCity(int provinceId){
			List<City> list=new ArrayList<City>();
			Cursor cursor = db.query("City", null,"province_id = ?",new String[] {String.valueOf(provinceId) },null, null, null);
			if(cursor.moveToNext()){
			do{
				City city=new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(provinceId);
				list.add(city);
			}while(cursor.moveToNext());
			}
			
		if(cursor != null){
			cursor.close();
		}
		return list;
		}
		/**
		 * 将County实例存储到数据库中
		 * @param county
		 */
		public void saveCounty(County county){
			if(county !=null ){
				ContentValues values = new ContentValues();
				values.put("county_name",county.getCountyName());
				values.put("county_code",county.getCountyCode());
				values.put("city_id",county.getCityId());
				db.insert("County", null, values);
			}
		}
		
		/**
		 * 从数据库中读取某城市下所有县信息
		 * @param cityId
		 * @return
		 */
		public List<County> loadCounty(int cityId){
			List<County> list = new ArrayList<County>();
			Cursor cursor = db.query("County", null, "city_id = ?",new String[]{String.valueOf(cityId)},null,null,null);
			if(cursor.moveToNext()){
				do{
					County county = new County();
					county.setCityId(cityId);
					county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
					county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
					county.setId(cursor.getInt(cursor.getColumnIndex("id")));
					list.add(county);
				}while(cursor.moveToNext());
			}
			
			if(cursor != null){
				cursor.close();
			}
			
			return list;
		}
		
	
	
	
	
	}
	

