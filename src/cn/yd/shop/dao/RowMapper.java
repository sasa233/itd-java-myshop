package cn.yd.shop.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

//接口里面的方法都是抽象方法，而且都是public类型
public interface RowMapper<T> {
	
	public abstract T mapRow(ResultSet rs) throws SQLException; 

}
