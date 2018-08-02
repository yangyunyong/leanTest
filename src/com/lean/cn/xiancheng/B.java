package com.lean.cn.xiancheng;

public class B {
	public static void main(String[] args) {
		/*MyThread mt = new MyThread();
		MyThread mt2 = new MyThread();
		MyThread mt3 = new MyThread();
		mt.start();
		mt2.start();
		mt3.start();*/
		MyThread1 mt = new MyThread1();
		Thread t1 = new Thread(mt);
		Thread t2 = new Thread(mt);
		Thread t3 = new Thread(mt);
		t1.start();
		t2.start();
		t3.start();
	}
}
class MyThread extends Thread{        // 继承Thread类，作为线程的实现类
	          private  int ticket = 5 ;                // 表示一共有5张票
	          public void run(){        // 覆写run()方法，作为线程 的操作主体
	                  for(int i=0;i<100;i++){
	                          if(this.ticket>0){
	                                  System.out.println(Thread.currentThread().getName()+"卖票：ticket = " + ticket--) ;
	                          }
	                  }
	          }
	 };
	 
	  class MyThread1 implements Runnable{        // 继承Thread类，作为线程的实现类
		          //volatile 无法保证i++这种操作的线程安全
		          private volatile int ticket = 20;                // 表示一共有5张票
		          public void run(){        // 覆写run()方法，作为线程 的操作主体
		                  for(int i=0;i<100;i++){
		                		  if(this.ticket>0){
		                        	  System.out.println(Thread.currentThread().getName()+"卖票：ticket = " + ticket--) ;
		                          }
		                          
		                  }
		          }
		  };