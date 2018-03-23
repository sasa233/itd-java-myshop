package cn.yd.shop.dao;

// cmd + shift + o 导出/导入包？？？？
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sun.org.apache.bcel.internal.generic.NEW;

import cn.yd.shop.model.Product;
import cn.yd.shop.util.JdbcUtil;

//ProductDaoImpl可使用BaseDaoImpl提供的方法
//java是单一继承的，super指向唯一的父类
//继承是代码复用的手段
public class ProductDaoImpl extends BaseDaoImpl<Product> { // 此处由子类确定了了父类BaseDapImpl的<T>的类型

	private int num;

	@Override
	protected Product getRow(ResultSet rs) throws SQLException {
		Product product = new Product();
		product.setId(rs.getInt("id"));
		product.setName(rs.getString("name"));
		product.setPrice(rs.getDouble("price"));
		product.setRemark(rs.getString("remark"));
		return product;
	}

	// java中每个类默认由缺省的构造方法，只有构造方法不需要写返回值
	// 如果显示声明了构造方法，则缺省构造失效
	// public ProductDaoImpl() {
	// // TODO Auto-generated constructor stub
	// }
	//
	// //同一类中，方法名称相同，参数的数量或者类型不同成为多态（重载）
	// public ProductDaoImpl(int num) {
	// // TODO Auto-generated constructor stub
	// this.num = num;
	//
	// }

	// shift + alt +A :进入/退出列模式
	// 测试代码编写入main方法中缺点：具侵入性；不能保留测试的痕迹；建议使用java中专用的测试框Junit-单元测试，一个类就是一个单元
	public static void main(String[] args) {
		// ProductDaoImpl daoImpl = new ProductDaoImpl();
		//
		// // java web中通过表单来进行数据输入
		// Product product = new Product();
		// product.setName("联想笔记本电脑");
		// product.setRemark("IBM");
		// product.setPrice(10099.99);
		// product.setId(8);
		// daoImpl.save(product);
		// daoImpl.update(product);
		// daoImpl.delete(product);
		// daoImpl.delete(7);
		// product = daoImpl.getByID(8);
		// System.out.println(product.toString());

		// ArrayList<Product> proList = daoImpl.queryByName("");
		//// for (int i = 0; i < proList.size(); i++) {
		//// System.out.println(proList.get(i));
		//// }
		// for (Product temp : proList) {
		// System.out.println(temp);
		// }

	}

	// java中集合不限大小
	// 如果没有给集合制定类型，则默认为object类型，可以指定泛型
	// 数组和集合的区别：数组限制大小、类型（需指定），集合不限大小、类型，泛型集合不限大小、限类型
	public ArrayList<Product> queryByName(String name) throws SQLException {
		//ArrayList<Product> proList = new ArrayList<Product>();
		String sql = "select * from product where name like ?";
		return super.queryByName(sql, new Object[]{"%" + name + "%"});
//		Connection connection = null; // 先声明后赋值
//		PreparedStatement pre = null;
//		ResultSet rs = null;
//		Product product = null;
//		// 1、获得数据库的连接对象
//		JdbcUtil.getConnection(); // cmd + 2 + R 出现变量类型和名字
//		// 2、创建执行SQL语句prepareStatement对象
//		try {
//			pre = connection.prepareStatement(sql); // cmd + shift + F 格式化快捷键
//			// 3、对每个？进行赋值操作
//			pre.setString(1, "%" + name + "%");
//			// 4、执行SQL语句(在java中 insert update delete 都称为update)
//			// pre.executeUpdate();
//			rs = pre.executeQuery(); // 用来存储查询返回的结果集
//			while (rs.next()) {
//				product = new Product();
//				product.setId(rs.getInt("id"));
//				product.setName(rs.getString("name"));
//				product.setPrice(rs.getDouble("price"));
//				product.setRemark(rs.getString("remark"));
//				proList.add(product);
//			}
//			// 5、释放connection连接对象, 调用工具类的方法
//			// connection.close();
//			return proList;
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			throw new RuntimeException(e);
//		} finally {
//			JdbcUtil.close(connection, pre, rs);
//		}
	}

	// 通过id获取Product
	public Product getByID(int id) {
		String sql = "select * from product where id = ?";
		return super.getByID(sql, id);
//		Connection connection = null; // 先声明后赋值
//		PreparedStatement pre = null;
//		ResultSet rs = null;
//		Product product = null;
//		// 1、获得数据库的连接对象
//		connection = JdbcUtil.getConnection(); // Ctrl + L???
//		// 2、创建执行SQL语句prepareStatement对象
//		try {
//			pre = connection.prepareStatement(sql); // cmd + shift + F 格式化快捷键
//			// 3、对每个？进行赋值操作
//			pre.setInt(1, id);
//			// 4、执行SQL语句(在java中 insert update delete 都称为update)
//			// pre.executeUpdate();
//			rs = pre.executeQuery(); // 用来存储查询返回的结果集
//			if (rs.next()) {
//				product = new Product();
//				product.setName(rs.getString("name"));
//				product.setPrice(rs.getDouble("price"));
//				product.setRemark(rs.getString("remark"));
//			}
//			// 5、释放connection连接对象, 调用工具类的方法
//			// connection.close();
//			return product;
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			throw new RuntimeException(e);
//		} finally {
//			JdbcUtil.close(connection, pre, rs);
//		}

	}

	// 完成数据的插入操作
	public void save(Product product) {
		String sql = "insert into product (name, price, remark) values (?, ?, ?)";
		super.update(sql, new Object[] { product.getName(), product.getPrice(), product.getRemark() });
		// // 1、获得数据库的连接对象
		// Connection connection = JdbcUtil.getConnection(); // Ctrl + L???
		// // 2、创建执行SQL语句prepareStatement对象
		// PreparedStatement pre;
		// try {
		// pre = connection.prepareStatement(sql); // cmd + shift + F 格式化快捷键
		// // 3、对每个？进行赋值操作
		// pre.setString(1, product.getName());
		// pre.setDouble(2, product.getPrice());
		// pre.setString(3, product.getRemark());
		// // 4、执行SQL语句(在java中 insert update delete 都称为update)
		// pre.executeUpdate();
		// // 5、释放connection连接对象
		// connection.close();
		// } catch (SQLException e) {
		// // TODO Auto-generated catch block
		// throw new RuntimeException(e);
		// }
	}

	public void update(Product product) {
		String sql = "update product set price = ?, name = ?, remark = ? where id = ?";
		super.update(sql, new Object[] { product.getPrice(), product.getName(), product.getRemark(), product.getId() });
		// 1、获得数据库的连接对象
		// Connection connection = JdbcUtil.getConnection(); // Ctrl + L???
		// // 2、创建执行SQL语句prepareStatement对象
		// PreparedStatement pre;
		// try {
		// pre = connection.prepareStatement(sql); // cmd + shift + F 格式化快捷键
		// // 3、对每个？进行赋值操作
		// pre.setString(2, product.getName());
		// pre.setDouble(1, product.getPrice());
		// pre.setString(3, product.getRemark());
		// pre.setInt(4, product.getId());
		// // 4、执行SQL语句(在java中 insert update delete 都称为update)
		// pre.executeUpdate();
		// // 5、释放connection连接对象
		// connection.close();
		// } catch (SQLException e) {
		// // TODO Auto-generated catch block
		// throw new RuntimeException(e);
		// }
	}

	// public void delete(Product product) {
	// 方法参数超过1-2个，需要进行封装；只有1个的话，就直接传入——参数设计越简单越好
	public void delete(int id) {
		String sql = "delete from product where id = ?";
		super.update(sql, new Object[] { id });
		// // 1、获得数据库的连接对象
		// Connection connection = JdbcUtil.getConnection(); // Ctrl + L???
		// // 2、创建执行SQL语句prepareStatement对象
		// PreparedStatement pre;
		// try {
		// pre = connection.prepareStatement(sql); // cmd + shift + F 格式化快捷键
		// // 3、对每个？进行赋值操作
		// //pre.setInt(1, product.getId());
		// pre.setInt(1, id);
		// // 4、执行SQL语句(在java中 insert update delete 都称为update)
		// pre.executeUpdate();
		// // 5、释放connection连接对象
		// connection.close();
		// } catch (SQLException e) {
		// // TODO Auto-generated catch block
		// throw new RuntimeException(e);
		// }
	}

}
