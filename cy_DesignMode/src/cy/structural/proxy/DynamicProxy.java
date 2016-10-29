package cy.structural.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * subject1.request()和subject2.request()这两句话，虽然看似简单的调用（
 * 因为subject1和subject2都是Subject的对象
 * ），实际上，这两个调用已经被DynamicSubject截获了，这里截获这个词很能表达代理的意思，理解了截获这个词，就能理解Proxy模式了
 * 
 * @author wkl
 * 
 */
public class DynamicProxy {
	@SuppressWarnings("rawtypes")
	static public void main(String[] args) throws Throwable {
		RealSubject1 rs = new RealSubject1(); // 在这里指定被代理类
		InvocationHandler ds = new DynamicSubject(rs); // 初始化代理类
		Class cls = rs.getClass();
		// 以下是分解步骤
		Class<?> c = Proxy.getProxyClass(cls.getClassLoader(), cls.getInterfaces());
		Constructor ct = c.getConstructor(new Class[] { InvocationHandler.class });
		Subject1 subject1 = (Subject1) ct.newInstance(new Object[] { ds });
		subject1.request();

		// 以下是一次性生成
		Subject1 subject2 = (Subject1) Proxy.newProxyInstance(cls.getClassLoader(), cls.getInterfaces(), ds);
		subject2.request();
	}
}

interface Subject1 {
	public void request();
}

// 具体角色RealSubject：实现了Subject接口的request()方法。
class RealSubject1 implements Subject1 {
	public RealSubject1() {

	}

	public void request() {
		System.out.println("From real subject.");
	}
}

// 代理角色：
class DynamicSubject implements InvocationHandler {
	private Object sub;

	public DynamicSubject(Object sub) {
		this.sub = sub;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("before calling " + method);
		method.invoke(sub, args);
		System.out.println("after calling " + method);
		return null;
	}
}
