package cn.yd.shop.dao;

import static org.hamcrest.CoreMatchers.nullValue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import cn.yd.shop.model.Product;
import cn.yd.shop.util.JdbcUtil;

//所有dao的父类，主要用于抽取共性代码
public abstract class BaseDaoImpl<T> {
	// 抽取update insert delete
	// protected 使得此方法只有子类能够访问

	// 父类可以定义一个抽象方法，让不同的子类去实现不同的方法
	// 类中有抽象方法，类必须声明为抽象类
	// 接口中所有的方法都是接口方法，可不用写abstract；抽象类中可以有部分抽象方法、部分实现方法；类中所有方法都是实现方法
	protected abstract T getRow(ResultSet rs) throws SQLException; //子类实现这方法时处理抛出的异常

	protected T getByID(String sql, Object id) {
		//String sql = "select * from product where id = ?";
		T t = null;
		Connection connection = null; // 先声明后赋值
		PreparedStatement pre = null;
		ResultSet rs = null;
		Product product = null;
		// 1、获得数据库的连接对象
		connection = JdbcUtil.getConnection(); // Ctrl + L???
		// 2、创建执行SQL语句prepareStatement对象
		try {
			pre = connection.prepareStatement(sql); // cmd + shift + F 格式化快捷键
			// 3、对每个？进行赋值操作
			pre.setObject(1, id);
			// 4、执行SQL语句(在java中 insert update delete 都称为update)
			// pre.executeUpdate();
			rs = pre.executeQuery(); // 用来存储查询返回的结果集
			if (rs.next()) {
				System.out.println(this);
				t = this.getRow(rs);
			}
			// 5、释放connection连接对象, 调用工具类的方法
			// connection.close();
			return t;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(connection, pre, rs);
		}
	}
	
	protected ArrayList<T> queryByName(String sql, Object[] param) throws SQLException {
		// ArrayList<Product> proList = new ArrayList<Product>();
		// String sql = "select * from product where name like ?";
		ArrayList<T> tList = new ArrayList<T>();
		Connection connection = null; // 先声明后赋值
		PreparedStatement pre = null;
		ResultSet rs = null;
		Product product = null;
		// 1、获得数据库的连接对象
		connection = JdbcUtil.getConnection(); // cmd + 2 + R 出现变量类型和名字
		// 2、创建执行SQL语句prepareStatement对象
		try {
			pre = connection.prepareStatement(sql); // cmd + shift + F 格式化快捷键
			// 3、对每个？进行赋值操作
			for (int i = 0; i < param.length; i++) {
				pre.setObject(i + 1, param[i]); // 把数组第i个值放到第i+1个占位符中去
			}
			// 4、执行SQL语句(在java中 insert update delete 都称为update)
			// pre.executeUpdate();
			rs = pre.executeQuery(); // 用来存储查询返回的结果集

			
			while (rs.next()) {
				// 此处代码只有子类才知道怎么写，父类不知道如下这些函数，父类实现不了的部分需要交给子类去实现
				// 父类可以定义一个抽象方法，让不同的子类去实现不同的方法
				// this.方法，谁调用方法this就指向谁
				tList.add(this.getRow(rs));
//				product = new Product();
//				product.setId(rs.getInt("id"));
//				product.setName(rs.getString("name"));
//				product.setPrice(rs.getDouble("price"));
//				product.setRemark(rs.getString("remark"));
//				proList.add(product);
			}
			// 5、释放connection连接对象, 调用工具类的方法
			// connection.close();
			return tList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(connection, pre, rs);
		}
	}

	protected void update(String sql, Object[] param) {
		// String sql = "update product set price = ?, name = ?, remark = ?
		// where id = ?";
		// 1、获得数据库的连接对象
		Connection connection = JdbcUtil.getConnection(); // Ctrl + L???
		// 2、创建执行SQL语句prepareStatement对象
		PreparedStatement pre = null;
		try {
			pre = connection.prepareStatement(sql); // cmd + shift + F 格式化快捷键
			// 3、对每个？进行赋值操作
			// pre.setString(2, product.getName());
			// pre.setDouble(1, product.getPrice());
			// pre.setString(3, product.getRemark());
			// pre.setInt(4, product.getId());
			for (int i = 0; i < param.length; i++) {
				pre.setObject(i + 1, param[i]); // 把数组第i个值放到第i+1个占位符中去
			}
			// 4、执行SQL语句(在java中 insert update delete 都称为update)
			pre.executeUpdate();
			// 5、释放connection连接对象
			// connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(connection, pre);
		}
	}
}
