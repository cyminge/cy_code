package cy.structural.proxy;

/**
 * 代理意思就是说有人帮你做一件事情。举个例子，现在有一份文件想要领导签字，但是领导不是随便就能见的，那么，就只得先去找领导的秘书，告诉秘书想要找领导签字，
 * 然后秘书会代替你去找领导签字，然后将签好字的文件还给你。这么做有两个原因，第一个，领导不是随便就能调用的，在这个例子里面，领导的签字功能只有秘书可以调用，
 * 当客户端调用领导签字这个功能的时候
 * ，秘书会把这个调用截获了，再通过自己去调用领导签字；第二个，有些字可以签，有些字签了是要出问题的，那么什么可以签什么不可以签
 * ，秘书会进行判断，秘书会把能签的给领导
 * ，不能签的直接返回给客户并告诉他签不了。讲完了上面的例子，回到JAVA的设计上来，使用代理的两个原因，一，是有些类不能被直接调用
 * ，例如它在另一台机器上，调用的话需要处理网络
 * ，编码解码等繁杂的但是跟业务逻辑无关的操作，如果客户端不用代理直接调用就会写很多跟业务逻辑无关的代码，这个和JAVA设计中的功能单一性原则相违背
 * ，因为一旦这个类的位置换了
 * ，那么客户端就得改，那么就有可能会影响到业务逻辑，所以这么做是不行的；第二，有可能这个类某些用户可以访问，某些用户没权限访问，那么如果不使用代理
 * ，那么这个被调用的类不仅要实现自己的功能，还要自己来实现权限判断的功能，这明显违背了降低耦合性的原则，于是，就有了代理。
 * 
 * @author wkl
 * 
 */
public class StaticProxy {
	public static void main(String args[]) {
		// 客户端调用：
		Subject sub = new ProxySubject();
		sub.request();
	}
}

abstract class Subject {
	abstract public void request();
}

// 真实角色：实现了Subject的request()方法。
class RealSubject extends Subject {
	public RealSubject() {
	}

	public void request() {
		System.out.println(" From real subject. ");
	}
}

// 代理角色：
class ProxySubject extends Subject {
	private RealSubject realSubject; // 以真实角色作为代理角色的属性

	public ProxySubject() {
	}

	public void request() { // 该方法封装了真实对象的request方法
		preRequest();
		if (realSubject == null) {
			realSubject = new RealSubject();
		}
		realSubject.request(); // 此处执行真实对象的request方法
		postRequest();
	}

	private void preRequest() {
		// something you want to do before requesting
	}

	private void postRequest() {
		// something you want to do after requesting
	}
}
