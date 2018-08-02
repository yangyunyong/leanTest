package com.lean.cn.xiancheng;

public class ThreadA {
	public static int count = 15;  
	    public static void main(String args[]){  
	        final byte a[] = {0};//以该对象为共享资源  
	        new Thread(new NumberPrint((1),a),"1").start();  
	        //new Thread(new NumberPrint2((2),a),"2").start();  
	        new Thread(new NumberPrint((2),a),"2").start();  
	    }  
}
class NumberPrint implements Runnable{  
    private int number;  
    public byte res[];  
    
    public NumberPrint(int number, byte a[]){  
        this.number = number;  
        res = a;  
    }  
    public void run(){  
        synchronized (res){  
            while(ThreadA.count-- > 0){  
                try {  
                    res.notify();//唤醒等待res资源的线程，把锁交给线程（该同步锁执行完毕自动释放锁）  
                    System.out.println(" "+number+"count:"+ThreadA.count);  
                    res.wait();//释放CPU控制权，释放res的锁，本线程阻塞，等待被唤醒。  
                    System.out.println("------线程"+Thread.currentThread().getName()+"获得锁，wait()后的代码继续运行："+number);  
                } catch (InterruptedException e) {  
                    // TODO Auto-generated catch block  
                    e.printStackTrace();  
                }  
            }//end of while  
            return;  
        }//synchronized  
          
    }  
}  
class NumberPrint2 implements Runnable{  
    private int number;  
    public byte res[];  
    public NumberPrint2(int number, byte a[]){  
        this.number = number;  
        res = a;  
    }  
    public void run(){  
        synchronized (res){  
            while(ThreadA.count-- > 0){  
                res.notify();//唤醒等待res资源的线程，把锁交给线程（该同步锁执行完毕自动释放锁）  
				System.out.println(" "+number+"count:"+ThreadA.count);  
				try {
					res.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//释放CPU控制权，释放res的锁，本线程阻塞，等待被唤醒。  
				System.out.println("------线程"+Thread.currentThread().getName()+"获得锁，wait()后的代码继续运行："+number);  
            }//end of while  
            return;  
        }//synchronized  
          
    }  
}  