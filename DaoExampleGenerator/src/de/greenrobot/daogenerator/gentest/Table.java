package de.greenrobot.daogenerator.gentest;

import java.util.ArrayList;

public class Table {

	String m_strName = null;
	ArrayList<Column> m_arrColumn = null;
	
	public Table() {
		m_arrColumn = new ArrayList<Column>();
	}
	
	public void setColumnList(ArrayList<Column> columns) {
		m_arrColumn = columns;
	}

	public void setName(String name) {
		m_strName = new String(name);
	}
	
	
	public ArrayList<Column> getColumnList() {
		return m_arrColumn;
	}

	public String getName() {
		return m_strName;
	}
	
}
