package com.mogoo.importance;

/**
 * 1.编程：用java 语言实现，输入一个数，就相应地输出的几维数组，例如：输入3，就输出这样的3维数组： 000 011 012
 * 若是输入4，就打印出4维的数组： 0000 0111 0122 0123 如此类推：
 */
public class Test_122345_1 {

	// 当前固定部分
	private String CurFixPart;
	private String PreGenNum;

	public static void main(String[] args) {
		Test_122345_1 t = new Test_122345_1();
		t.GenControll("122345");
	}

	// 调整字符串s位置pos字符到最前
	private String shift(String s, int pos) {
		String newStr;
		if (s.length() > pos + 1)
			newStr = s.substring(pos, pos + 1) + s.substring(0, pos) + s.substring(pos + 1);
		else
			newStr = s.substring(pos) + s.substring(0, pos);
		return newStr;
	}

	protected int Validate(String newNum) {
		String newGenNum = CurFixPart + newNum;
		if (Integer.valueOf(newGenNum) <= Integer.valueOf(PreGenNum))
			return 0;
		if (newGenNum.substring(2, 3).equals("4") || (newGenNum.indexOf("35") != -1) || (newGenNum.indexOf("53") != -1))
			return 0;

		PreGenNum = newGenNum;
		System.out.println(newGenNum);
		return 0;
	}

	public void GenControll(String Base) {
		PreGenNum = "0";
		CurFixPart = "";
		GenNext(Base, 0);
	}

	void GenNext(String varPart, int curPos) {
		if (varPart.length() == 2) {
			Validate(varPart);
			Validate(shift(varPart, 1));
			return;
		}
		// Next Layer
		String newGen = shift(varPart, curPos);
		String SavedFixPart = CurFixPart;
		CurFixPart = CurFixPart + newGen.substring(0, 1);
		GenNext(newGen.substring(1), 0);
		CurFixPart = SavedFixPart;
		// 同层递增
		if (curPos == varPart.length() - 1)
			return;
		GenNext(varPart, curPos + 1);
	}

}
