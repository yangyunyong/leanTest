package com.lean.cn.xiancheng;

public class CopyOfA {
	public static void main(String[] args) {
		mythread2 myt = new mythread2();
		Thread t = new Thread(myt);
		t.start();
		mythread3 myt1 = new mythread3();
		Thread t1 = new Thread(myt1);
		t1.start();
		for(int i = 0; i < 1000; i++) {  
            System.out.println("bbbbbbbbbbsdfsdfsdb");  
        } 
	}
}

class mythread3 implements Runnable{
	@Override
	public void run() {
	        for(int i = 0; i < 1000; i++) {      //3,将要执行的代码写在run方法中  
	            System.out.println("aaa");  
	            
	        }  
	}
}

class mythread2 implements Runnable{
	@Override
	public void run() {
	        for(int i = 0; i < 1000; i++) {      //3,将要执行的代码写在run方法中  
	            System.out.println("aaa111111");  
	        }  
	}
}