package cy.behavitives.chainofresponsibility;

/**
 * 职责链模式顾名思义是一个链条，在这个链条上的所有节点都可以处理请求，但是在某一种情况下，哪一个节点来处理这个请求是运行时决定的，
 * 但是客户端不需要知道具体是谁来处理
 * ，他只需要将请求交给一个节点就行了，当这个节点可以处理时，它就处理并返回给客户端，当它不能处理时，将这个请求传给职责链上的下一个节点
 * ，依次下去，直到请求被处理或者达到链条的最后一个节点。
 * 
 * 实际中的应用：mima的filter概念
 * 
 * @author wkl
 * 
 */
public class ChainOfResponsibility {
	public static void main(String[] args) {

		CommonManager commonManager = new CommonManager("Tom");
		Majordomo majordomo = new Majordomo("Jerry");
		GeneralManager generalManager = new GeneralManager("Thomas");

		// 设置上下级
		commonManager.SetSuperior(majordomo);
		majordomo.SetSuperior(generalManager);

		Request request = new Request();
		request.setName("Vincent");
		request.setRequestType("请假");
		request.setNumber(1);
		commonManager.requestApplications(request);
	}
}

/**
 * 请求 ， 客户端发起
 * 
 * @author wkl
 * 
 */
class Request {

	private String name;
	private String requestType;
	private int number;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
}

/**
 * 管理者
 * 
 * @author wkl
 * 
 */
abstract class Manager {

	protected String name;
	// 管理者的上级
	protected Manager superior;

	public Manager(String name) {
		this.name = name;
	}

	// 设置管理者的上级
	public void SetSuperior(Manager superior) {
		this.superior = superior;
	}

	// 申请请求
	abstract public void requestApplications(Request requset);
}

// 总经理
class GeneralManager extends Manager {

	public GeneralManager(String name) {
		super(name);
	}

	@Override
	public void requestApplications(Request request) {
		if (request.getRequestType().equals("请假") && request.getNumber() <= 7) {
			System.out.println(request.getName() + request.getRequestType() + request.getNumber() + "被批准" + ",GeneralManager," + this.name);
		} else if (request.getRequestType().equals("加薪") && request.getNumber() <= 800) {
			System.out.println(request.getName() + request.getRequestType() + request.getNumber() + "被批准" + ",GeneralManager," + this.name);
		} else {
			System.out.println(request.getName() + request.getRequestType() + request.getNumber() + "不批准" + ",GeneralManager," + this.name);
		}
	}
}

// 总监
class Majordomo extends Manager {
	public Majordomo(String name) {
		super(name);
	}

	@Override
	public void requestApplications(Request request) {
		if (request.getRequestType().equals("请假") && request.getNumber() <= 5) {
			System.out.println(request.getName() + request.getRequestType() + request.getNumber() + "被批准" + ",Majordomo," + this.name);
		} else {
			if (this.superior != null) {
				this.superior.requestApplications(request);
			}
		}
	}
}

// 经理
class CommonManager extends Manager {
	public CommonManager(String name) {
		super(name);
	}

	@Override
	public void requestApplications(Request request) {
		if (request.getRequestType().equals("请假") && request.getNumber() <= 2) {
			System.out.println(request.getName() + request.getRequestType() + request.getNumber() + "被批准" + ",CommonManager," + this.name);
		} else {
			if (this.superior != null) {
				this.superior.requestApplications(request);
			}
		}
	}
}