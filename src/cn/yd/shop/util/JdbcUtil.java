package cn.yd.shop.util;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.sql.Connection;

//此工具类用于完成数据库的连接与示范操作
//数据库的驱动jar包只需加载一次
public class JdbcUtil {
	// 在java中，配置文件、驱动通常指只加载一次，这些代码可放在静态块中
	static { // 此块在JdbcUtil.class文件加载到JVM虚拟机中会执行，且只执行一次
		System.out.println("-static-");
		try {
			Class.forName("com.mysql.jdbc.Driver"); // 加载驱动jar包中的此类
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Connection getConnection() {
		String url = "jdbc:mysql://localhost:3306/myshop?useSSL=false&characterEncoding=utf8";
		//jdbc:mysql://localhost:3306/myshop?characterEncoding=utf8
		java.sql.Connection connection;
		try {
			// JDBC4Connection实现了Collection接口
			connection = DriverManager.getConnection(url, "root", "root");
			System.out.println(connection);
		} catch (SQLException e) {
			// 异常不用打印，只能向上抛出
			throw new RuntimeException(e);
		}
		return connection;
	}

	// 编写一个方法来实现数据库资源释放, Connection、PreparedStatement、ResultSet数据库三大对象的资源都需要关闭
	// 文件IO读取也要关
	public static void close(Connection connection, PreparedStatement pre, ResultSet rs) {
		
		try {
			if (rs != null && !rs.isClosed()) {
				System.out.println(rs);
				rs.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (pre != null && !pre.isClosed()) {
					pre.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} finally {
				try {
					if (connection != null && !connection.isClosed()) {
						connection.close();
					}
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		}
		
		
		//如下写法可能会出现connection没有关闭的情况，会导致资源泄露
//		try {
//			pre.close();
//			connection.close();
//		} catch (SQLException e) {
//			throw new RuntimeException(e);
//		}
		
	}
	
	public static void close(Connection connection, PreparedStatement pre) {
		close(connection, pre, null);
	}

	// main方法是静态方法，执行时需由类执行，加载类时会执行上方的静态块，详见打印输出
	public static void main(String[] args) {

	}
}
