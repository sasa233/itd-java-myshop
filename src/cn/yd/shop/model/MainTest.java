package cn.yd.shop.model;

public class MainTest { //方法执行，入栈
	/*static静态方法是类方法（只能类来调用）；*/
	public static void main(String[] args) {
		/*
		//JVM首先通过类加载器，加载了Student.class，创建一个Student对象A
		//student是引用类型（类似指针），存储对象A的地址，对象的非静态的属性与属性值存储于堆中（无方法）
		Student student = new Student(); 
		Student student3 = new Student(); //stu3指向另一个对象B
		// stu stu2指向相同的对象A，存储了对象的地址
		Student student2 = student;  //student2也指向了对象A的地址
		System.out.println(student);
		System.out.println(student2);
		student.setAge(18);  //调用方法，方法存储于方法区，哪个对象调用该方法，该方法就向该对象赋值
		int age = student.getAge();
		System.out.println("年龄为：" + age);
		System.out.println("年龄为：" + student2.getAge());
		System.out.println("年龄为：" + student3.getAge());
		*/
		
		//静态属性（类变量）属于类，存储于类中，位于方法区；静态属性仅一份，普通属性每个对象有一份
		//应用类访问，可用对象访问，访问的都是同一个变量或方法
		//静态属性与方法可引用于配置属性使用场景
		Student student = new Student();
		student.setAge(18);
		//静态方法属于类，存储于类中，位于方法区；静态方法由类调用，普通方法由对象调用
		//普通方法中可对静态属性赋值，因其只有一份；静态方法中不可对普通属性赋值，因为普通属性有多份
		Student.setName("admin"); 
		
		Student student2 = new Student();
		student2.setAge(20);
		student2.setName("admin2");
		
		System.out.println(student.getAge() + "," + student.getName());
		System.out.println(student2.getAge() + "," + student2.getName());
	}
}

