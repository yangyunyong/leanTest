package com.lean.cn;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class AppTest {
	public static void main(String[] args) throws Exception {
		/*PushBlockQueue.getInstance().start();
		
		for(int i=0; i<100; i++){
			PushBlockQueue.getInstance().put(i+100);
		}*/
		Class<?> bs = Base.class;
		Field[] f = bs.getFields();
		Method[] m =  bs.getMethods();
		System.out.println(m[0].getName());
		
		Array.getLength(m);
		System.out.println(Array.getLength(m));
	}
}
class Base{
	public int a;
	public void getValues(){
		System.out.println("aa");
	}
}