package cy.creational.builder;

/**
 * 当要生产的一种产品具有相同的结构，并且每个构件的生产都很繁杂，就可以用Builder模式将具体构件的生产与整个成品的组装分离开来。还是拿本文的代码来举例，
 * 生产一辆汽车
 * ，生产汽车的厂家不需要知道引擎怎么生产的，不需要关心轮胎怎么生产的，也不需要关心玻璃怎么生产的。当他在生产一辆车的时候，只会说，我要一块日本产的引擎
 * ，于是就有了日本产的引擎
 * （至于日本引擎怎么生产的他不关心，他只要一个引擎的成品），他又说我要一块美国产的玻璃，于是就有了美国产的玻璃，这样直到他得到了所有的构件
 * ，然后他把这些构件组装起来，组成一个成品（汽车）卖给客户。这就是一个典型的Builder模式。
 * 
 * 客户端代码，使用Director创建两种不同型别的CarA和CarB
 * 
 * 这种方式更多的是选择已有的方式进行buidler，还有一种是diy
 */
public class BuilderTest {
	public static void main(String[] args) {
		Car carA, carB;
//		CarBuilder builderA = new CarABuilder();
//		CarBuilder builderB = new CarBBuilder();
		Director director;
//		director = new Director(builderA);
		director = new DirectorACar();
		carA = director.construct();
//		director = new Director(builderB);
		director = new DirectorBCar();
		carB = director.construct();
		System.out.println("Car A is made by:" + carA.glass + carA.wheel + carA.engine);
		System.out.println("Car B is made by:" + carB.glass + carB.wheel + carB.engine);
		
		
		Car carAA = new CarABuilder().getProduct();
	}
}

/**
 * design pattern in java name:builder 目的：利用builder模式创建两种汽车carA和carB
 * car=glass+wheel+engine carA=AmericanGlass+JapaneseWheel+ChinaEngine
 * carB=JapaneseGlass+AmericanWheel+FranceEngine author:blackphoenix create
 * date:2002-08-19 modifier:Anyuan Zhao modify date:2011-02-16
 */

/**
 * 定义部件glass的抽象类Glass 和两个具体类AmericanGlass、JapaneseGlass
 */
abstract class Glass {
}

class AmericanGlass extends Glass {
	public String toString() {
		return "American Glass, ";
	}
}

class JapaneseGlass extends Glass {
	public String toString() {
		return "Japanese Glass, ";
	}
}

/**
 * 定义部件wheel的抽象类Wheel 和两个具体类AmericanWheel、JapaneseWheel
 */
abstract class Wheel {
}

class AmericanWheel extends Wheel {
	public String toString() {
		return "American Wheel, ";
	}
}

class JapaneseWheel extends Wheel {
	public String toString() {
		return "Japanese Wheel, ";
	}
}

/**
 * 定义部件engine的抽象类Engine 和两个具体类ChineseEngine、FranceEngine
 */
abstract class Engine {
}

class ChineseEngine extends Engine {
	public String toString() {
		return "Chinese Engine, ";
	}
}

class FranceEngine extends Engine {
	public String toString() {
		return "France Engine, ";
	}
}

/**
 * 定义产品类Car
 */
class Car {
	Glass glass;
	Wheel wheel;
	Engine engine;
	
//	public Car(Director director) {
//	    director.construct();
//	}
}

/**
 * 定义抽象建造器接口Builder
 */

interface CarBuilder {
	public void buildGlass();

	public void buildWheel();

	public void buildEngine();

	public Car getProduct();
}

/**
 * 具体建造器类CarABuilder CarA=AmericanGlass+JapaneseWheel+ChineseEngine
 */
class CarABuilder implements CarBuilder {
	private Car product = null;

	public CarABuilder() {
		product = new Car();
	}

	/**
	 * 将建造部件的工作封装在getProduct()操作中,主要是为了向客户隐藏实现细节 这样，具体建造类同时又起到了一个director的作用
	 */
	@Override
	public void buildEngine() {
		// TODO Auto-generated method stub
		product.engine = new ChineseEngine();
	}

	@Override
	public void buildGlass() {
		// TODO Auto-generated method stub
		product.glass = new AmericanGlass();
	}

	@Override
	public void buildWheel() {
		// TODO Auto-generated method stub
		product.wheel = new JapaneseWheel();
	}

	@Override
	public Car getProduct() {
		// TODO Auto-generated method stub
		buildGlass();
		buildWheel();
		buildEngine();
		return product;
	}
}

/**
 * 具体建造器类CarABuilder CarB=JapaneseGlass+AmericanWheel+FranceEngine
 */
class CarBBuilder implements CarBuilder {
	private Car product;

	public CarBBuilder() {
		product = new Car();
	}

	/**
	 * 将建造部件的工作封装在getProduct()操作中,主要是为了向客户隐藏实现细节 这样，具体建造类同时又起到了一个director的作用
	 */
	@Override
	public void buildEngine() {
		// TODO Auto-generated method stub
		product.engine = new FranceEngine();
	}

	@Override
	public void buildGlass() {
		// TODO Auto-generated method stub
		product.glass = new JapaneseGlass();
	}

	@Override
	public void buildWheel() {
		// TODO Auto-generated method stub
		product.wheel = new AmericanWheel();
	}

	@Override
	public Car getProduct() {
		// TODO Auto-generated method stub
		buildGlass();
		buildWheel();
		buildEngine();
		return product;
	}
}

interface Director {
    public Car construct();
}

class DirectorACar implements Director {

    @Override
    public Car construct() {
        return new CarABuilder().getProduct();
    }
    
}

class DirectorBCar implements Director {

    @Override
    public Car construct() {
        return new CarBBuilder().getProduct();
    }
    
}
    

//class Director {
//	private CarBuilder builder;
//
//	public Director(CarBuilder builder) {
//		this.builder = builder;
//	}
//
//	public Car construct() {
//		return builder.getProduct();
//	}
//}