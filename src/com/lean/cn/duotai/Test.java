package com.lean.cn.duotai;

public class Test {
	public void doSomething(Shape shape){
		shape.draw();
		shape.erace();
	}
	public static void main(String[] args) {
		Cricle cr = new Cricle();
		Line li = new Line();
		Test t = new Test();
		t.doSomething(cr);
		//t.doSomething(li);
		
		
		int i=1;
		int a = ++i;
		System.out.println(a);
		
		int j=5;
		/*while(j>0){
			j--;
			System.out.println(j);
		}*/
		
		do{
			j--;
			System.out.println(j);
		}while(j>0);
	}
}
