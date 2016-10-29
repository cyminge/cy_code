package com.cy;

import java.util.ArrayList;
import java.util.Iterator;

public class ListRemove {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		arrayList.add(1);
		arrayList.add(2);
		arrayList.add(3);
		arrayList.add(5);
		arrayList.add(4);
		arrayList.add(6);
		arrayList.add(9);
		
		ListRemove l = new ListRemove();
		System.out.println("删除前打印如下：");
		l.printArrayList(arrayList);
		//循环删除元素
//		l.deleteEven(arrayList);
//		System.out.println("循环删除偶数后打印如下：");
//		l.printArrayList(arrayList);
		//遍历删除元素
		l.removeByIterate(arrayList);
		System.out.println("遍历删除偶数后打印如下：");
		l.printArrayList(arrayList);
	}
	//循环删除偶数
	public void deleteEven(ArrayList<Integer> a){
		for(int i=0;i<a.size();i++){
			if(a.get(i)%2==0){
				a.remove(i);
			}
		}
	}
	//利用遍历删除偶数元素
	public void removeByIterate(ArrayList<Integer> a){
		Iterator<Integer> array = a.iterator(); 
		int i=0;
		while(array.hasNext()){
			if(i>=a.size()){//避免获取元素时下标溢出
				break;
			}else if(a.get(i)%2==0){
				a.remove(i);
			}
			i++;
		}
	}
	//打印arrayList
	public void  printArrayList(ArrayList<Integer> a){
		for(int i=0;i<a.size();i++){
			System.out.print(a.get(i));
			if(i!=a.size()-1){
				System.out.print(",");
			}
		}
		System.out.println();
	}
}
