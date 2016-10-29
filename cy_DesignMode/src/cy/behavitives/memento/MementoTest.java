package cy.behavitives.memento;

import java.io.Serializable;

/**
 * 备忘录对象是一个用来存储另外一个对象内部状态的快照的对象。备忘录模式的用意是在不破坏封装的条件下，将一个对象的状态捕捉(Capture)住，并外部化，
 * 存储起来，从而可以在将来合适的时候把这个对象还原到存储起来的状态。备忘录模式常常与命令模式和迭代子模式一同使用。
 * 
 * Memento设计模式用来备份一个对象的当前状态，当需要的时候，用这个备份来恢复这个对象在某一个时刻的状态。
 * 
 * @author wkl
 * 
 */
public class MementoTest {
	public static void main(String[] args) {
		Originator o = new Originator(10);
		System.out.println("original:" + o.getNumber());
		Memento mement = o.getMemento();
		o.setNumber(20);
		System.out.println("changed:" + o.getNumber());
		o.setMemento(mement);
		System.out.println("recovered:" + o.getNumber());
	}
}

class Originator {
	public int number;

	public Originator(int num) {
		this.number = num;
	}

	public void setNumber(int num) {
		this.number = num;
	}

	public int getNumber() {
		return this.number;
	}

	// 创建一个Memento
	public Memento getMemento() {
		return new Memento(this);
	}

	// 恢复到原始值
	public void setMemento(Memento m) {
		number = m.number;
	}
}

class Memento implements Serializable {
	private static final long serialVersionUID = 1L;
	public int number;

	public Memento(Originator o) {
		number = o.number;
	}
}