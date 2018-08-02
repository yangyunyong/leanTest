package com.lean.cn;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
/**
 * linkedBlockingQueue   链式阻塞队列
 * @author yyy
 *
 */
public class PushBlockQueue extends LinkedBlockingDeque<Object>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static ExecutorService es = Executors.newFixedThreadPool(10);
	private static PushBlockQueue pbq = new PushBlockQueue();
	private boolean flag = false;
	private PushBlockQueue(){
		
	}
	public static PushBlockQueue getInstance(){
		return pbq;
	}
	
	public void start(){
		if(!this.flag){
			this.flag = true;
		}else{
			throw new IllegalArgumentException("已经启动");
		}
		new Thread(new Runnable() {
			public void run() {
				
				while(flag){
					try {
						Object obj = take();
						es.execute(new PushBlockQueueHandler(obj));
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}).start();
	}
}
