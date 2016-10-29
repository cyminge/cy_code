package cy.behavitives.template;

public class TemplateTest {
	public static void main(String[] args) {
		// 建立1个有'A'的CharDisplay的对象
		AbstractDisplay d1 = new CharDisplay('A');
		// 建立1个有"Hello world"的StringDisplay的对象
		AbstractDisplay d2 = new StringDisplay("Hello World");
		// d1,d2都是AbstractDisplay的子类对象,可以调用继承到的display()方法
		d1.display();
		d2.display();
	}
}

abstract class AbstractDisplay {
	// 由子类实现的抽象方法
	public abstract void open();

	public abstract void print();

	public abstract void close();

	// 抽象类实现的方法,final可以保证在子类不会被修改
	public final void display() {
		open(); // 先open...
		for (int i = 0; i < 5; i++) { // 反复输出5次
			print();
		}
		close(); // 输出完毕,close
	}
}

class CharDisplay extends AbstractDisplay {
	private char ch; // 应输出的字符

	public CharDisplay(char ch) { // 把构造函数传递过来的字符ch,存储在字段内
		this.ch = ch;
	}

	public void open() {
		System.out.print("<<"); // 输出"<<"作为开始字符串
	}

	public void close() {
		System.out.println(">>"); // 输出 ">>"作为结束字符串
	}

	public void print() {
		System.out.print(ch); // 输出存储在字段的字符
	}
}

class StringDisplay extends AbstractDisplay {
	private String string; // 应输出的字符串
	private int width; // 以byte为单位所求出的字符串的"长度"

	public StringDisplay(String string) {
		this.string = string;
		width = string.getBytes().length;
	}

	public void open() { // 打印头装饰字符串
		printLine();
	}

	public void print() { // 打印内容
		System.out.println("|" + string + "|");
	}

	public void close() { // 打印尾装饰字符串
		printLine();
	}

	public void printLine() {
		System.out.print("+"); // 输出"+"号表示边框位置
		for (int i = 0; i < width; ++i) {
			System.out.print("-"); // 当作线段
		}
		System.out.println("+"); // 输出"+"号表示边框位置
	}
}