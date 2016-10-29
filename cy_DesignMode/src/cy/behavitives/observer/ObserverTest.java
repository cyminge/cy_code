package cy.behavitives.observer;

import java.util.Observable;
import java.util.Observer;

/**
 * 观察者模式：观察者模式是对象的行为模式，又叫发布-订阅(Publish/Subscribe)模式、模型-视图(Model/View)模式、源-监听器(
 * Source/Listener)模式或从属者(Dependents)模式
 * 
 * Good这个类里的serPrice()这个方法，在这个方法里面，调用this.notifyObservers(price)之前一定要调用this.
 * setChanged()方法，因为只有这样，this.notifyObservers(price)这个方法才会去调用update()。
 * 
 * @author wkl
 * 
 */
public class ObserverTest {
	public static void main(String[] args) {
		Good good = new Good("洗衣粉", 3.5f);
		Customer tom = new Customer("Tom");
		Customer jerry = new Customer("Jerry");
		good.addObserver(tom);
		good.addObserver(jerry);
		good.setPrice(2.5f);
		good.setPrice(3.0f);
	}
}

class Good extends Observable {
	private String name;
	private float price;

	public Good(String name, float price) {
		this.name = name;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public float getPrice() {
		this.notifyObservers(price);
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
		/*
		 * 注意 ：在通知 所有观察者之前 一定要调用 setChanged()方法来设置被观察者的状态已经被改变，
		 */
		this.setChanged();
		/*
		 * notifyObservers 方法在通知完所有吗 观察者 后， 会自动调用 clearChanged 方法来清除 被观察者 的状态改变。
		 */
		this.notifyObservers(price);
	}
}

class Customer implements Observer {
	private String name;

	public Customer(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void update(Observable observable, Object arg) {
		if (observable instanceof Good && arg instanceof Float) {
			System.out.println("客户<" + this.name + "> : " + ((Good) observable).getName() + "的价格变了，" + arg + "元了 ！");
		}
	}
}