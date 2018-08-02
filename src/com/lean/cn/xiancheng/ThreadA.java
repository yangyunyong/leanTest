package com.lean.cn.xiancheng;

public class ThreadA {
	public static int count = 15;  
	    public static void main(String args[]){  
	        final byte a[] = {0};//�Ըö���Ϊ������Դ  
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
                    res.notify();//���ѵȴ�res��Դ���̣߳����������̣߳���ͬ����ִ������Զ��ͷ�����  
                    System.out.println(" "+number+"count:"+ThreadA.count);  
                    res.wait();//�ͷ�CPU����Ȩ���ͷ�res���������߳��������ȴ������ѡ�  
                    System.out.println("------�߳�"+Thread.currentThread().getName()+"�������wait()��Ĵ���������У�"+number);  
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
                res.notify();//���ѵȴ�res��Դ���̣߳����������̣߳���ͬ����ִ������Զ��ͷ�����  
				System.out.println(" "+number+"count:"+ThreadA.count);  
				try {
					res.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//�ͷ�CPU����Ȩ���ͷ�res���������߳��������ȴ������ѡ�  
				System.out.println("------�߳�"+Thread.currentThread().getName()+"�������wait()��Ĵ���������У�"+number);  
            }//end of while  
            return;  
        }//synchronized  
          
    }  
}  