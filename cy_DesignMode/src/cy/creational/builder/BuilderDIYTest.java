package cy.creational.builder;


/**
 * 自定义car的组成部分
 * @author JLB6088
 *
 */
public class BuilderDIYTest {

    private static DIYCar mDIYCar;

    public static void main(String[] args) {
        mDIYCar = new DIYCar.Builder()
                .buildEngine(new DIYCar.FranceEngine())
                .buildGlass(new DIYCar.JapaneseGlass())
                .buildWheel(new DIYCar.JapaneseWheel())
                .builder();
        
        mDIYCar.toString();
    }

}

class DIYCar {

    public Glass glass;
    public Wheel wheel;
    public Engine engine;

    public DIYCar(Builder builder) {
        glass = builder.glass;
        wheel = builder.wheel;
        engine = builder.engine;
    }
    
    @Override
    public String toString() {
        
        System.out.println(glass.toString());
        System.out.println(wheel.toString());
        System.out.println(engine.toString());
        
        return super.toString();
    }

    public static class Builder {

        private Glass glass = new AmericanGlass();
        private Wheel wheel = new AmericanWheel();
        private Engine engine = new ChineseEngine();

        public Builder buildGlass(Glass glassImpl) {
            glass = glassImpl;

            return this;
        }

        public Builder buildWheel(Wheel whellImpl) {
            wheel = whellImpl;

            return this;
        }

        public Builder buildEngine(Engine engineImpl) {
            engine = engineImpl;

            return this;
        }

        public DIYCar builder() {
            return new DIYCar(this);
        }
    }

    /**
     * 定义部件glass的抽象类Glass 和两个具体类AmericanGlass、JapaneseGlass
     */
    static abstract class Glass {
    }

    static class AmericanGlass extends Glass {
        public String toString() {
            return "American Glass, ";
        }
    }

    static class JapaneseGlass extends Glass {
        public String toString() {
            return "Japanese Glass, ";
        }
    }

    /**
     * 定义部件wheel的抽象类Wheel 和两个具体类AmericanWheel、JapaneseWheel
     */
    static abstract class Wheel {
    }

    static class AmericanWheel extends Wheel {
        public String toString() {
            return "American Wheel, ";
        }
    }

    static class JapaneseWheel extends Wheel {
        public String toString() {
            return "Japanese Wheel, ";
        }
    }

    /**
     * 定义部件engine的抽象类Engine 和两个具体类ChineseEngine、FranceEngine
     */
    static abstract class Engine {
    }

    static class ChineseEngine extends Engine {
        public String toString() {
            return "Chinese Engine, ";
        }
    }

    static class FranceEngine extends Engine {
        public String toString() {
            return "France Engine, ";
        }
    }
}
