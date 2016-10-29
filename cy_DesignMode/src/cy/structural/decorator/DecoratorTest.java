package cy.structural.decorator;

/**
 * Decorator装饰器，顾名思义，就是动态地给一个对象添加一些额外的职责，就好比为房子进行装修一样。 因此，装饰器模式具有如下的特征：
 * 它必须具有一个装饰的对象。 它必须拥有与被装饰对象相同的接口。 它可以给被装饰对象添加额外的功能。 用一句话总结就是：保持接口，增强性能。
 * 装饰器通过包装一个装饰对象来扩展其功能，而又不改变其接口，这实际上是基于对象的适配器模式的一种变种。它与对象的适配器模式的异同点如下:
 * 
 * 相同点：都拥有一个目标对象。 不同点：适配器模式需要实现另外一个接口，而装饰器模式必须实现该对象的接口。
 * 
 * @author wkl
 */
public class DecoratorTest {
    /**
     * @param args
     */
    public static void main(String[] args) {
        Sourcable source = new Source();
        // 装饰类对象
        Sourcable obj = new Decorator1(new Decorator2(new Decorator3(source)));
        obj.operation();
    }
}

interface Sourcable {
    public void operation();
}

class Source implements Sourcable {
    public void operation() {
        System.out.println("原始类的方法");
    }
}

class Decorator1 implements Sourcable {
    private Sourcable sourcable;

    public Decorator1(Sourcable sourcable) {
        super();
        this.sourcable = sourcable;
    }

    public void operation() {
        System.out.println("第一个装饰器前");
        sourcable.operation();
        System.out.println("第一个装饰器后");
    }
}

class Decorator2 implements Sourcable {
    private Sourcable sourcable;

    public Decorator2(Sourcable sourcable) {
        super();
        this.sourcable = sourcable;
    }

    public void operation() {
        System.out.println("第二个装饰器前");
        sourcable.operation();
        System.out.println("第二个装饰器后");
    }
}

class Decorator3 implements Sourcable {
    private Sourcable sourcable;

    public Decorator3(Sourcable sourcable) {
        super();
        this.sourcable = sourcable;
    }

    public void operation() {
        System.out.println("第三个装饰器前");
        sourcable.operation();
        System.out.println("第三个装饰器后");
    }
}
