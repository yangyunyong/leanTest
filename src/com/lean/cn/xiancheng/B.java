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
class MyThread extends Thread{        // �̳�Thread�࣬��Ϊ�̵߳�ʵ����
	          private  int ticket = 5 ;                // ��ʾһ����5��Ʊ
	          public void run(){        // ��дrun()��������Ϊ�߳� �Ĳ�������
	                  for(int i=0;i<100;i++){
	                          if(this.ticket>0){
	                                  System.out.println(Thread.currentThread().getName()+"��Ʊ��ticket = " + ticket--) ;
	                          }
	                  }
	          }
	 };
	 
	  class MyThread1 implements Runnable{        // �̳�Thread�࣬��Ϊ�̵߳�ʵ����
		          //volatile �޷���֤i++���ֲ������̰߳�ȫ
		          private volatile int ticket = 20;                // ��ʾһ����5��Ʊ
		          public void run(){        // ��дrun()��������Ϊ�߳� �Ĳ�������
		                  for(int i=0;i<100;i++){
		                		  if(this.ticket>0){
		                        	  System.out.println(Thread.currentThread().getName()+"��Ʊ��ticket = " + ticket--) ;
		                          }
		                          
		                  }
		          }
		  };