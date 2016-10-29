package cy.structural.bridge;

/**
 * 人认为，Bridge模式的核心就是组合，把描述一个事物的各个维度用组合的方式放在一个基础的维里面，这样就不必生成M*N*L...
 * 这么多个子类，另外就是需要保证当某一个维度的长度变长的时候不能更改其它类。总结起来就是两点：
 * 
 * 1.把描述一个事物的所有维度放在一个类里面；
 * 
 * 2.在某个或者某些维度的长度增加或者减少或者改变时，不能更改原有的类。
 * 
 * 满足了上面两个条件，也就实现了Bridge模式。
 * 
 * @author wkl
 * 
 */
public class BridgeTest {
	public static void main(String[] args) {
		Student s = new Master();
		s.doMethod(new Method2());
		/* 使用方法2为硕士生发助学金 */
	}
}

/* 学生抽象类 */
abstract class Student {
	public abstract void doMethod(Method method);
}

/* 本科生 */
class Granduate extends Student {
	public Granduate() {
	}

	public void doMethod(Method method) {
		System.out.println("本科生");
		method.method();
	}
}

/* 硕士生 */
class Master extends Student {
	public Master() {
	}

	public void doMethod(Method method) {
		System.out.println("硕士生");
		method.method();
	}
}

/* 发放办法接口 */
interface Method {
	public void method();
}

/* 方法实现1（Method1） */
class Method1 implements Method {
	public void method() {
		System.out.println("助学金发放办法1");
	}
}

/* 方法实现2（Method2） */
class Method2 implements Method {
	public void method() {
		System.out.println("助学金发放办法2");
	}
}
