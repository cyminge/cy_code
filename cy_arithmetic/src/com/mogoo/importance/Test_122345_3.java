package com.mogoo.importance;


/**
 * 原题如下：用1、2、2、3、4、5这六个数字，用java写一个main函数，打印出所有不同的排列，如：512234、412345等，要求："4"
 * 不能在第三位，"3"与"5"不能相连. 算法思路：显然是递归，初始序列122345，先从末两位(45)变化(45,54),然后末三位(345) ...
 * 直到最后六位.怎样解决重复问题？很简单，由于是递增序列，每生成新序列可与前一生成序列比较，如<放弃当前序列。当然有更好效率，如预先预测。代码如下：
 */
public class Test_122345_3 {

	// 当前固定部分
	private String CurFixPart;
	private String PreGenNum;

	public static void main(String[] args) {
		Test_122345_3 t = new Test_122345_3();
		t.GenControll("1111111111");
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
		while (true) {
			if (curPos == varPart.length() - 1)
				break;
			// 预测是否重复
			curPos++;
			if (Integer.valueOf(CurFixPart + shift(varPart, curPos)) <= Integer.valueOf(PreGenNum))
				continue;

			GenNext(varPart, curPos);
			break;
		}
	}
}
