package cy.structural.flyWeight;

import java.util.Hashtable;

/**
 * 享元模式FlyWeight模式在两种情况下适用，一是只读对象，二是共享对象。
 * 
 * 一. 只读对象。例如，一篇英文的文档是由26个英文字母构成的，每个字母就是一个只读的对象，多篇文档可以使用同样的26个字母对象来组成，
 * 没必要每篇文档都生成自己的字母对象，这样做的目的就是为了节省内存。
 * 
 * 二. 共享对象。例如，打印机对象，那么就必须是用FlyWeight模式了，因为所有计算机都只能用那一个打印机，也许你会说这应该用单例模式，
 * 但是想想如果公司又买了一个打印机
 * ，那么这个打印机也是共享的对象，也就是说，单例模式的目的是同一个类只能生成一个对象，享元模式的目的是同一个类可以实例化不同的对象
 * ，但是同样的对象只能有一个。
 * 
 * 下面的例子，我们把所有不同的对象放到一个散列表里面，然后用get(key)的形式来返回一个对象，当用key能在散列表里面找到相应的对象的时候就直接返回
 * ，如果找不到，就实例化一个对象，把它放在散列表里面，并且返回这个对象，下一次get这个对象的时候就不会再实例化而是直接从散列表里面返回这个对象的引用。
 * 下面是代码：
 * 
 * 代码最核心的部分不是Flyweight和ConcreteFlyweight这两个类，
 * 而是FlyweightFactory这个类里面的getFlyWeight()这个方法，这个方法实现了前面所描述的功能。
 * 
 * 总的来说，其实就一句话，FlyWeight保证没有相同的对象，Singleton保证只有一个对象。
 * 
 * @author wkl
 * 
 */
public class FlyWeightTest {
	public static void main(String[] args) {
		System.out.println("The FlyWeight Pattern!");
		FlyweightPattern fp = new FlyweightPattern();
		fp.showFlyweight();
	}
}

abstract class Flyweight {
	public abstract void operation();
}

class ConcreteFlyweight extends Flyweight {
	private String string;

	public ConcreteFlyweight(String str) {
		string = str;
	}

	public void operation() {
		System.out.println("Concrete---Flyweight : " + string);
	}
}

class FlyweightFactory {
	private Hashtable<String, Flyweight> flyweights = new Hashtable<String, Flyweight>();

	public FlyweightFactory() {
	}

	public Flyweight getFlyWeight(Object obj) {
		Flyweight flyweight = (Flyweight) flyweights.get(obj);
		if (flyweight == null) {
			flyweight = new ConcreteFlyweight((String) obj);
			flyweights.put((String) obj, flyweight);
		}
		return flyweight;
	}

	public int getFlyweightSize() {
		return flyweights.size();
	}
}

class FlyweightPattern {
	FlyweightFactory factory = new FlyweightFactory();
	Flyweight fly1;
	Flyweight fly2;
	Flyweight fly3;
	Flyweight fly4;
	Flyweight fly5;
	Flyweight fly6;

	public FlyweightPattern() {
		fly1 = factory.getFlyWeight("Google");
		fly2 = factory.getFlyWeight("Qutr");
		fly3 = factory.getFlyWeight("Google");
		fly4 = factory.getFlyWeight("Google");
		fly5 = factory.getFlyWeight("Google");
		fly6 = factory.getFlyWeight("Google");
	}

	public void showFlyweight() {
		fly1.operation();
		fly2.operation();
		fly3.operation();
		fly4.operation();
		fly5.operation();
		fly6.operation();
		int objSize = factory.getFlyweightSize();
		System.out.println("objSize = " + objSize);
	}
}