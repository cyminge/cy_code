package cy.structural.facade;

/**
 * Facade模式翻译成零售商模式我想更加容易理解。当你想要买一块香皂的的时候，你肯定不会去香皂厂买，而是去超市买，同样，买衣服买手机都不会去厂家，
 * 而是去零售商家
 * 
 * @author wkl
 */
public class FacadeTest {

	public static void main(String[] args) {
		Store store = new Store();
		// 买衣服
		System.out.println(store.saleCoat().getClass().getName());
		// 买电脑
		System.out.println(store.saleComputer().getClass().getName());
		// 买手机
		System.out.println(store.saleMobile().getClass().getName());
	}
}

class Coat {
}

class Computer {
}

class Mobile {
}

class CoatFactory {
	public Coat saleCoat() {
		return new Coat();
	}
}

class ComputerFactory {
	public Computer saleComputer() {
		return new Computer();
	}
}

class MobileFactory {
	public Mobile saleMobile() {
		return new Mobile();
	}
}

class Store {
	public Coat saleCoat() {
		CoatFactory coatFactory = new CoatFactory();
		return coatFactory.saleCoat();
	}

	public Computer saleComputer() {
		ComputerFactory computerFactory = new ComputerFactory();
		return computerFactory.saleComputer();
	}

	public Mobile saleMobile() {
		MobileFactory mobileFactory = new MobileFactory();
		return mobileFactory.saleMobile();
	}
}
