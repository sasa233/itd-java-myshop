package cn.yd.shop.dao;

import static org.hamcrest.CoreMatchers.nullValue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import java.lang.reflect.Field;

import cn.yd.shop.model.Product;
import cn.yd.shop.util.JdbcUtil;

//所有dao的父类，主要用于抽取共性代码
public abstract class BaseDaoImpl<T> {
	// 抽取update insert delete
	// protected 使得此方法只有子类能够访问

	// 父类可以定义一个抽象方法，让不同的子类去实现不同的方法
	// 类中有抽象方法，类必须声明为抽象类
	// 接口中所有的方法都是接口方法、都public，可不用写abstract；抽象类中可以有部分抽象方法、部分实现方法；类中所有方法都是实现方法
	// T代表未知的类型
	// protected abstract T getRow(ResultSet rs) throws SQLException; // 子类实现这方法时处理抛出的异常
	// 此处SQLException可以改为Exception，子类的此方法仍可使用SQLException
	// 因为父类的引用，可以指向子类的对象；反之不行
	// 对于多态，可以总结以下几点：
	// 1、使用父类类型的引用指向子类的对象；
	// 2、该引用只能调用父类中定义的方法和变量；
	// 3、如果子类中重写了父类中的一个方法，那么在调用这个方法的时候，将会调用子类中的这个方法；（动态连接、动态调用）
	// 4、变量不能被重写（覆盖），”重写“的概念只针对方法，如果在子类中”重写“了父类中的变量，那么在编译时会报错。

	protected T getByID(String sql, Object id, Class<T> clazz) throws Exception{
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
			ResultSetMetaData metaData = rs.getMetaData();
			if (rs.next()) {
				System.out.println(this);
				// 每条记录对应Product对象
				T model = clazz.newInstance();
				// 1、获取结果集的字段名称（if外）
				// 2、通过for循环获取列/字段的名称
				for (int i = 1; i <= metaData.getColumnCount(); i++) { //列从第一列开始
					String colName = metaData.getColumnName(i); //根据列的索引获取列的名称
					// 4、根据列名获取当前类的属性名
					Field field = clazz.getDeclaredField(colName);
					// 5、取消安全检查，使得private被访问
					field.setAccessible(true);
					field.set(model, rs.getObject(colName));
				}
				t = model;
			}			
			// 5、释放connection连接对象, 调用工具类的方法
			// connection.close();
			return t;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(connection, pre, rs);
		}
	}
	
	
	protected T getByID(String sql, Object id, RowMapper<T> mapper) {
		// String sql = "select * from product where id = ?";
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
				//t = this.getRow(rs);
				t = mapper.mapRow(rs);
			}
			
			// 5、释放connection连接对象, 调用工具类的方法
			// connection.close();
			return t;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(connection, pre, rs);
		}
	}
	
	protected ArrayList<T> queryByName(String sql, Object[] param, Class<T> clazz) throws Exception {
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
			ResultSetMetaData metaData = rs.getMetaData();
			while (rs.next()) {
				// 每条记录对应Product对象
				T model = clazz.newInstance();
				// 通过反射进行对象的属性动态赋值
				// 1、获取结果集的字段名称（while外）
				// 2、通过for循环获取列/字段的名称
				for (int i = 1; i <= metaData.getColumnCount(); i++) { //列从第一列开始
					String colName = metaData.getColumnName(i); //根据列的索引获取列的名称
					// 4、根据列名获取当前类的属性名
					Field field = clazz.getDeclaredField(colName);
					// 5、取消安全检查，使得private被访问
					field.setAccessible(true);
					field.set(model, rs.getObject(colName));
				}
				tList.add(model);
			}
			// 5、释放connection连接对象, 调用工具类的方法
			// connection.close();
			return tList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(connection, pre, rs);
		}
	}

	protected ArrayList<T> queryByName(String sql, Object[] param, RowMapper<T> mapper) {
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
				// tList.add(this.getRow(rs));
				tList.add(mapper.mapRow(rs));
				// Product product = null;
				// product = new Product();
				// product.setId(rs.getInt("id"));
				// product.setName(rs.getString("name"));
				// product.setPrice(rs.getDouble("price"));
				// product.setRemark(rs.getString("remark"));
				// proList.add(product);
			}
			// 5、释放connection连接对象, 调用工具类的方法
			// connection.close();
			return tList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(connection, pre, rs);
		}
	}

	// jdk 1.5新特性，可变参数int[]可写为int...
	// 可变参数只能放在方法传入参数的最后一个
	protected void update(String sql, Object... param) {
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
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(connection, pre);
		}
	}
}
