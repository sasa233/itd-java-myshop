package cn.yd.shop.dao;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import cn.yd.shop.model.Product;

public class ProductDaoImplTest {
	
	private static ProductDaoImpl daoImpl = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("测试方法前执行，一般用来初始化测试对象。");
		daoImpl = new ProductDaoImpl();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("在所有测试方法后执行，用来销毁资源。");
		daoImpl = null;  //java对象会自动被垃圾回收
	}

	@Test
	public void testQueryByName() throws SQLException {
		ArrayList<Product> proList = daoImpl.queryByName("");
		for (Product temp : proList) {
			System.out.println(temp.toString());
		}
	}

	@Test
	public void testGetByID() {
		System.out.println("......");
		Product product = daoImpl.getByID(3);
		System.out.println(product);
	}

	@Test
	public void testSave() {
		Product product = new Product();
		product.setName("黄明昊笔记本电脑");
		product.setRemark("Dream");
		product.setPrice(999999.99);
		daoImpl.save(product);
	}

	@Test
	public void testUpdateProduct() {
		Product product = new Product();
		product.setName("黄明昊");
		product.setRemark("Heart");
		product.setPrice(999999.99);
		product.setId(15);
		daoImpl.update(product);
	}

	@Test
	public void testDelete() {
		daoImpl.delete(12);
	}

}
