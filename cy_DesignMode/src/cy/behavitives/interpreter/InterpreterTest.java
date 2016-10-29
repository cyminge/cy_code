package cy.behavitives.interpreter;

import java.util.HashMap;

/**
 * 解释器模式是类的行为模式。给定一个语言之后，解释器模式可以定义出其文法的一种表示，并同时提供一个解释器。客户端可以使用这个解释器来解释这个语言中的句子。
 * 
 * @author wkl
 * 
 */
public class InterpreterTest {
	private static Context ctx;
	private static Expression exp;

	public static void main(String args[]) {
		ctx = new Context();
		Variable x = new Variable("x");
		Variable y = new Variable("y");
		Constant c = new Constant(true);
		ctx.assign(x, false);
		ctx.assign(y, true);
		exp = new Or(new And(c, x), new And(y, new Not(x)));
		System.out.println("x = " + x.interpret(ctx));
		System.out.println("y = " + y.interpret(ctx));
		System.out.println(exp.toString() + "=" + exp.interpret(ctx));
	}
}

abstract class Expression {
	// 以环境类为准，本方法解释给定的任何一个表达式
	public abstract boolean interpret(Context ctx);

	// 检验两个表达式在结构上是否相同
	public abstract boolean equals(Object o);

	// 返回表达式的hash code
	public abstract int hashCode();

	// 将表达式转换成字符串
	public abstract String toString();
}

class Constant extends Expression {
	private boolean value;

	public Constant(boolean value) {
		this.value = value;
	}

	// 解释操作
	public boolean interpret(Context ctx) {
		return value;
	}

	// 检验两个表达式在结构上是否相同
	public boolean equals(Object o) {
		if (o != null && o instanceof Constant) {
			return this.value == ((Constant) o).value;
		}
		return false;
	}

	// 返回表达式的hash code
	public int hashCode() {
		return (this.toString()).hashCode();
	}

	// 将表达式转换成字符串
	public String toString() {
		return new Boolean(value).toString();
	}
}

class Variable extends Expression {
	private String name;

	public Variable(String name) {
		this.name = name;
	}

	public boolean interpret(Context ctx) {
		return ctx.lookup(this);
	}

	public boolean equals(Object o) {
		if (o != null && o instanceof Variable) {
			return this.name.equals(((Variable) o).name);
		}
		return false;
	}

	public int hashCode() {
		return (this.toString()).hashCode();
	}

	public String toString() {
		return name;
	}
}

class And extends Expression {
	public Expression left, right;

	public And(Expression left, Expression right) {
		this.left = left;
		this.right = right;
	}

	public boolean interpret(Context ctx) {
		return left.interpret(ctx) && right.interpret(ctx);
	}

	public boolean equals(Object o) {
		if (o != null && o instanceof And) {
			return this.left.equals(((And) o).left) && this.right.equals(((And) o).right);
		}
		return false;
	}

	public int hashCode() {
		return (this.toString()).hashCode();
	}

	public String toString() {
		return "(" + left.toString() + " AND " + right.toString() + ")";
	}
}

class Or extends Expression {
	private Expression left, right;

	public Or(Expression left, Expression right) {
		this.left = left;
		this.right = right;
	}

	public boolean interpret(Context ctx) {
		return left.interpret(ctx) || right.interpret(ctx);
	}

	public boolean equals(Object o) {
		if (o != null && o instanceof Or) {
			return this.left.equals(((And) o).left) && this.right.equals(((And) o).right);
		}
		return false;
	}

	public int hashCode() {
		return (this.toString()).hashCode();
	}

	public String toString() {
		return "(" + left.toString() + " OR " + right.toString() + ")";
	}
}

class Not extends Expression {
	private Expression exp;

	public Not(Expression exp) {
		this.exp = exp;
	}

	public boolean interpret(Context ctx) {
		return !exp.interpret(ctx);
	}

	public boolean equals(Object o) {
		if (o != null && o instanceof Not) {
			return this.exp.equals(((Not) o).exp);
		}
		return false;
	}

	public int hashCode() {
		return (this.toString()).hashCode();
	}

	public String toString() {
		return "(NOT " + exp.toString() + ")";
	}
}

class Context {
	private HashMap<Variable, Boolean> map = new HashMap<Variable, Boolean>();

	public void assign(Variable var, boolean value) {
		map.put(var, new Boolean(value));
	}

	public boolean lookup(Variable var) throws IllegalArgumentException {
		Boolean value = (Boolean) map.get(var);
		if (value == null) {
			throw new IllegalArgumentException();
		}
		return value.booleanValue();
	}
}