package cy.behavitives.mediator;

import java.util.ArrayList;

/**
 * 中介模式(调停者模式)实际就像是房屋中介一样，中介维护了一个列表，这个列表包含了各个客户的房屋信息，当某一个客户的房屋信息有了改变之后，
 * 这个中介就负责通知自己维护的那个列表中的所有用户某某某进行了改变。
 * 
 * @author wkl
 * 
 */
public class MediatorTest {
	public static void main(String[] args) {
		AbstractMediator mediator = new ConcreteMediator();
		AbstractColleague colleagueA = new ConcreteColleagueA(mediator);
		AbstractColleague colleagueB = new ConcreteColleagueB(mediator);
		AbstractColleague colleagueC = new ConcreteColleagueC(mediator);
		colleagueA.changed();
		colleagueB.changed();
		colleagueC.changed();
	}
}

abstract class AbstractMediator {
	public abstract void register(AbstractColleague ac);

	public abstract void ColleagueChanged(AbstractColleague ac);
}

abstract class AbstractColleague {
	protected AbstractMediator med;

	public AbstractColleague(AbstractMediator mediator) {
		this.med = mediator;
	}

	public abstract void action();

	public void changed() {
		med.ColleagueChanged(this);
	}
}

class ConcreteMediator extends AbstractMediator {

	private ArrayList<AbstractColleague> colleagueList = new ArrayList<AbstractColleague>();

	public void register(AbstractColleague ac) {
		colleagueList.add(ac);
	}

	public void ColleagueChanged(AbstractColleague ac) {
		for (int i = 0; i < colleagueList.size(); i++) {
			if (colleagueList.get(i) != ac) {
				colleagueList.get(i).action();
			}
		}
	}
}

class ConcreteColleagueA extends AbstractColleague {
	public ConcreteColleagueA(AbstractMediator mediator) {
		super(mediator);
		super.med.register(this);
	}

	public void action() {
		System.out.println("AAAAAAAAAAAAAAA");
	}
}

class ConcreteColleagueB extends AbstractColleague {
	public ConcreteColleagueB(AbstractMediator mediator) {
		super(mediator);
		super.med.register(this);
	}

	public void action() {
		System.out.println("BBBBBBBBBBBBBBB");
	}
}

class ConcreteColleagueC extends AbstractColleague {
	public ConcreteColleagueC(AbstractMediator mediator) {
		super(mediator);
		super.med.register(this);
	}

	public void action() {
		System.out.println("CCCCCCCCCCCCCCC");
	}
}