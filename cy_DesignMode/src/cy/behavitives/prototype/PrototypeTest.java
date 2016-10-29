package cy.behavitives.prototype;

/**
 * Prototype是很容易理解也很容易使用的一个设计模式，他的意思就是说，我给你一个原型，你照着这个原型给我做一个就行了，
 * 至于做好之后我要怎么去修改它让他符合新的需求
 * ，这就不管Prototype模式的事了。如果按照这么理解，我们只需要克隆一个一模一样的对象，返回给客户端就行了，
 * 重要的问题就是如何克隆。幸运的是，Java已经给我们提供了一个现成的函数，它就叫做clone()。
 * 
 * @author wkl
 * 
 */
public class PrototypeTest {
	public static void main(String args[]) {
		// Spoon spoon1 = new Spoon("Tom's Spoon");
		// Spoon spoon2 = (Spoon) spoon1.clone();
		// System.out.println("spoon 1 reference : " + spoon1);
		// System.out.println("spoon 1 name      : " + spoon1.getSpoonName());
		// System.out.println("spoon 2 reference : " + spoon2);
		// System.out.println("spoon 2 name      : " + spoon2.getSpoonName());
		// spoon2.setSpoonName("Jerry's Spoon");
		// System.out.println("spoon 1 reference : " + spoon1);
		// System.out.println("spoon 1 name      : " + spoon1.getSpoonName());
		// System.out.println("spoon 2 reference : " + spoon2);
		// System.out.println("spoon 2 name      : " + spoon2.getSpoonName());
	}
}

/**
 * 浅拷贝
 * 
 * 可以看到虽然两个spoon的地址不同，但是他们的成员变量数组spoonName的地址是相同的，所以对于spoon2的更改影响到了spoon1，
 * 这就不符合克隆的要求了，所以需要深度克隆，深度克隆就是说连spoonName也要克隆，
 * 
 * @author wkl
 * 
 */
class Spoon implements Cloneable {
	String spoonName;

	public Spoon(String name) {
		this.spoonName = name;
	}

	public void setSpoonName(String spoonName) {
		this.spoonName = spoonName;
	}

	public String getSpoonName() {
		return this.spoonName;
	}

	public Object clone() {
		Object object = null;
		try {
			object = super.clone();
		} catch (CloneNotSupportedException exception) {
			System.err.println("AbstractSpoon is not Cloneable");
		}
		return object;
	}
}

/**
 * 深拷贝
 * 
 * 对深度克隆进行一些说明，当你要克隆的对象包含了非基本数据类型（包含String）的时候，就要进去这个对象里面把不是基本数据类型的也克隆了，
 * 如果他下面还有不是基本数据类型了，再进去，直到全部都是基本数据类型了才截止。
 * 
 * @author wkl
 * 
 */
class Spoon1 implements Cloneable {
	String spoonName[];

	public Spoon1() {
		this.spoonName = new String[2];
	}

	public Object clone() {
		Spoon1 object = null;
		try {
			object = (Spoon1) super.clone();
			object.spoonName = (String[]) spoonName.clone();
		} catch (CloneNotSupportedException exception) {
			System.err.println("AbstractSpoon is not Cloneable");
		}
		return object;
	}
}
