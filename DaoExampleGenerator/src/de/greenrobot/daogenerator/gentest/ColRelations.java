package de.greenrobot.daogenerator.gentest;

public class ColRelations {

	public final static int TYPE_TOONE = 1;
	public final static int TYPE_TOMANY = 2;
	
	int m_nType = 0;
	String m_strName = null;
	String m_ToTableName = null;
	
	public void setName(String name) {
		m_strName = new String(name);
	}
	
	public void setToTableName(String table) {
		m_ToTableName = new String(table);
	}
	
	public void setType(int type) {
		m_nType = type;
	}
	
	public String getName() {
		return m_strName;
	}
	
	public String getToTableName() {
		return m_ToTableName;
	}
	
	public int getType() {
		return m_nType;
	}
	
}
