package com.lean.cn.xiancheng;

public class Mysource {
	public  void a(){
		System.out.println("Thread-0,:synchronized in a()");
	}
	public  void b(){
		System.out.println("Thread-0,:synchronized in b()");
	}
	public  void c(){
		System.out.println("Thread-1,:synchronized in c()");
	}
	
	public static void main(String[] args) {
			final Mysource rs = new Mysource();
			for(int i=0;i<10;i++){
				new Thread() {
					public void run() {
					rs.a();
					rs.b();
					}
					}.start();
			}
			
			
			new Thread() {
			public void run() {
			rs.c();
			}
			}.start();
	}
	
}
