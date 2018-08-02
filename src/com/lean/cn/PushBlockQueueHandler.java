package com.lean.cn;

public class PushBlockQueueHandler implements Runnable{
	private Object obj;
	public PushBlockQueueHandler(Object obj){
		this.obj = obj;
	}
	@Override
	public void run() {
		dobussiness();
	}
	public void dobussiness(){
		System.out.println("ÇëÇó³öÀ´£º"+obj);
	}
	
}
