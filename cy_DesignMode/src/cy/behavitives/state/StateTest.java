package cy.behavitives.state;

/**
 * 在这个例子里面，士兵有两个状态。不同的状态，fire()方法是不同的，真正的fire()方法是定义在状态里的，因此，士兵类里的fire()
 * 方法只需要调用state
 * .fire()就行了。这样设计的好处，就是让系统具有了可扩展性，当士兵有了新的状态的时候，只需要实现一个新的继承与State类的类就行了
 * 。那么士兵就自动具有了这个状态，不需要改动原有的代码。
 * 
 * @author wkl
 * 
 */
public class StateTest {
	public static void main(String[] args) {
		Marine marine = new Marine(new NormalState()); // 创建一个机枪兵的实例：
		marine.fire(); // 调用fire()方法：
		marine.setState(new ExcitedState()); // 设置为兴奋状态：
		marine.fire(); // 再调用fire()方法：
	}
}

interface State {
	public void fire();
}

class NormalState implements State {
	public void fire() {
		System.out.println("普通状态每秒开枪1次。");
	}
}

class ExcitedState implements State {
	public void fire() {
		System.out.println("兴奋状态每秒开枪2次。");
	}
}

class Marine {
	private State state;

	public Marine(State state) {
		this.state = state;
	}

	public void setState(State state) {
		this.state = state;
	}

	// fire()方法，实际调用的是state变量的fire()方法：
	public void fire() {
		state.fire();
	}
}