package cy.behavitives.command;

/**
 * Command模式就像linux的shell命令一样，输入一条命令，它就能执行相应的操作。例如，我们想要打开电视机，我们不需要用tv.turnOn()
 * 这样的方法来打开 ，而是类似command.exe("turn on the TV")这样的语句。把命令封装起来有一个好处，我们可以记录下执行过哪些命令，
 * 在什么时间执行的 ，是谁执行的。当然，不使用命令模式也可以实现这样的功能，我们只需要在类似tv.turnOn()这个方法内部加上log就行了，
 * 或者是我们在客户端加上log。 虽然这是一个解决方案
 * ，但是不是最好的，因为它违背了JAVA设计原则中的类只实现跟自己相关的功能，也就是说记录log不是tv这个类该做的，也不是客户端该做的，那么应该怎么办呢。
 * 
 * 各个类只做了与自己相关的工作，Control这个类负责接收客户端的命令，然后来执行这个命令，在执行的时候，它记录了正在执行的是什么命令。在这个例子里面，
 * 客户端传给Control的是一个命令的对象
 * ，实际上客户端直接传一个命令的字符串给Control也是可以的，然后Control这个类去分析这个字符串，然后在调用相应的命令对象向去执行
 * ，这样，客户端的代码就会更加容易。
 * 
 * @author wkl
 * 
 */
public class CommandTest { // 命令发送者
	public static void main(String[] args) {
		// 命令接收者
		Tv myTv = new Tv();
		// 开机命令
		CommandOn on = new CommandOn(myTv);
		// 关机命令
		CommandOff off = new CommandOff(myTv);
		// 频道切换命令
		CommandChannel channel = new CommandChannel(myTv, 2);
		// 命令控制对象
		Control control = null;
		// 开机
		control = new Control(on);
		control.execute();
		// 切换频道
		control.changeCommand(channel);
		control.execute();
		// 关机
		control.changeCommand(off);
		control.execute();
	}
}

// 命令控制者
class Control {
	private Command command;

	public Control(Command com) {
		this.command = com;
	}

	public void changeCommand(Command com) {
		this.command = com;
	}

	public void execute() {
		command.execute();
		System.out.println("command:" + command);
	}
}

// 命令接收者
class Tv {
	public int currentChannel = 0;
	public boolean isOn = false;

	public void turnOn() {
		isOn = true;
	}

	public void turnOff() {
		isOn = false;
	}

	public void changeChannel(int channel) {
		this.currentChannel = channel;
	}
}

// 命令接口
interface Command {
	void execute();
}

// 频道切换命令
class CommandChannel implements Command {
	private Tv myTv;
	private int channel;

	public CommandChannel(Tv tv, int channel) {
		myTv = tv;
		this.channel = channel;
	}

	@Override
	public void execute() {
		myTv.changeChannel(channel);
	}

	public String toString() {
		String com = "Change Channel, now TV channel is " + myTv.currentChannel;
		return com;
	}
}

// 关机命令
class CommandOff implements Command {
	private Tv myTv;

	public CommandOff(Tv tv) {
		myTv = tv;
	}

	@Override
	public void execute() {
		myTv.turnOff();
	}

	public String toString() {
		String com = "Trun Off the TV";
		return com;
	}
}

// 开机命令
class CommandOn implements Command {
	private Tv myTv;

	public CommandOn(Tv tv) {
		myTv = tv;
	}

	@Override
	public void execute() {
		myTv.turnOn();
	}

	public String toString() {
		String com = "Trun On the TV";
		return com;
	}
}
