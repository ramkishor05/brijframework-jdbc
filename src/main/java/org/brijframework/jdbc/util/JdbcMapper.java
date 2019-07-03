package org.brijframework.jdbc.util;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.brijframework.util.validator.ValidationUtil;

public enum JdbcMapper {

	CLOB("CLOB",false),
	BLOB("BLOB",false),
	LONGBLOB("LONGBLOB",false),
	TINYBLOB("TINYBLOB",false),
	DOUBLE("DOUBLE",false),
	DATETIME("DATETIME",false),
	TIME("TIME",false),
	TIMESTAMP("TIMESTAMP",false),
	TIME_WITH_TIMEZONE("TIME_WITH_TIMEZONE",false),
	TIMESTAMP_WITH_TIMEZONE("TIMESTAMP_WITH_TIMEZONE",false);

	String id;
	boolean arg;
	
	JdbcMapper(String id,boolean arg){
		this.id=id;
		this.arg=arg;
	}
	
	public static JdbcMapper typeOf(String type) {
		for (JdbcMapper mapper : values()) {
			if(mapper.id.equalsIgnoreCase(type)) {
				return mapper;
			}
		}
		return null;
	}
	
	static SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
	public static Object valueOf(Object value,String type) {
		if(value==null) {
			return null;
		}
		if(type.equalsIgnoreCase("DATE")) {
			if(ValidationUtil.isNumber(value.toString())) {
				Date date=new Date(Long.valueOf(value.toString()));
				return "'"+dateFormat.format(date)+"'";
			}else {
				return "'"+value.toString()+"'";
			}
		}
		
		if(type.equalsIgnoreCase("TIME") ) {
			if(ValidationUtil.isNumber(value.toString())) {
				Time time= new Time(Long.valueOf(value.toString()));
				return "'"+dateFormat.format(time)+"'";
			}else {
				return "'"+value.toString()+"'";
			}
		}
		if(type.equalsIgnoreCase("DATETIME") ) {
			if(ValidationUtil.isNumber(value.toString())) {
				Date date=new Date(Long.valueOf(value.toString()));
				return "'"+dateFormat.format(date)+"'";
			}else {
				return "'"+value.toString()+"'";
			}
		}
		if(type.equalsIgnoreCase("TIMESTAMP") ) {
			if(ValidationUtil.isNumber(value.toString())) {
				Timestamp timestamp= new Timestamp(Long.valueOf(value.toString()));
				return "'"+dateFormat.format(timestamp)+"'";
			}else {
				return "'"+value.toString()+"'";
			}
		}
		if(type.equalsIgnoreCase("TIME_WITH_TIMEZONE") ) {
			if(ValidationUtil.isNumber(value.toString())) {
				Timestamp timestamp= new Timestamp(Long.valueOf(value.toString()));
				return "'"+dateFormat.format(timestamp)+"'";
			}else {
				return "'"+value.toString()+"'";
			}
		}
		if(type.equalsIgnoreCase("TIMESTAMP_WITH_TIMEZONE") ) {
			if(ValidationUtil.isNumber(value.toString())) {
				Timestamp timestamp= new Timestamp(Long.valueOf(value.toString()));
				return "'"+dateFormat.format(timestamp)+"'";
			}else {
				return "'"+value.toString()+"'";
			}
		}
		if (type.equalsIgnoreCase("VARCHAR")||type.equalsIgnoreCase("VARCHAR2")) {
			return "'"+value+"'";
		}
		return value;
	}

	public static Object valueOf(Object value, JdbcMapper type) {
		if(value==null) {
			return null;
		}
		if(type.toString().equalsIgnoreCase("DATE")) {
			if(ValidationUtil.isNumber(value.toString())) {
				Date date=new Date(Long.valueOf(value.toString()));
				return "'"+dateFormat.format(date)+"'";
			}else {
				return "'"+value.toString()+"'";
			}
		}
		
		if(type.toString().equalsIgnoreCase("TIME") ) {
			if(ValidationUtil.isNumber(value.toString())) {
				Time time= new Time(Long.valueOf(value.toString()));
				return "'"+dateFormat.format(time)+"'";
			}else {
				return "'"+value.toString()+"'";
			}
		}
		if(type.toString().equalsIgnoreCase("DATETIME") ) {
			if(ValidationUtil.isNumber(value.toString())) {
				Date date=new Date(Long.valueOf(value.toString()));
				return "'"+dateFormat.format(date)+"'";
			}else {
				return "'"+value.toString()+"'";
			}
		}
		if(type.toString().equalsIgnoreCase("TIMESTAMP") ) {
			if(ValidationUtil.isNumber(value.toString())) {
				Timestamp timestamp= new Timestamp(Long.valueOf(value.toString()));
				return "'"+dateFormat.format(timestamp)+"'";
			}else {
				return "'"+value.toString()+"'";
			}
		}
		if(type.toString().equalsIgnoreCase("TIME_WITH_TIMEZONE") ) {
			if(ValidationUtil.isNumber(value.toString())) {
				Timestamp timestamp= new Timestamp(Long.valueOf(value.toString()));
				return "'"+dateFormat.format(timestamp)+"'";
			}else {
				return "'"+value.toString()+"'";
			}
		}
		if(type.toString().equalsIgnoreCase("TIMESTAMP_WITH_TIMEZONE") ) {
			if(ValidationUtil.isNumber(value.toString())) {
				Timestamp timestamp= new Timestamp(Long.valueOf(value.toString()));
				return "'"+dateFormat.format(timestamp)+"'";
			}else {
				return "'"+value.toString()+"'";
			}
		}
		if (type.toString().equalsIgnoreCase("VARCHAR")||type.toString().equalsIgnoreCase("VARCHAR2")) {
			return "'"+value+"'";
		}
		return value;
	}
}
