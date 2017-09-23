package de.greenrobot.daogenerator.gentest;

import java.util.ArrayList;

public class Column {

	ArrayList<ColRelations> m_arrRelations;
	String m_strName = null;
	String m_strType = null;
	boolean m_bPrimary = false;
	boolean m_bNotNull = false;
	boolean m_bUnique = false;
	boolean m_bAutoIncrement = false;
	
	public Column() {
		m_arrRelations = new ArrayList<ColRelations>();
	}
	
	public void setPropertyList(ArrayList<ColRelations> properties) {
		m_arrRelations = properties;
	}
	
	public void setName(String name) {
		m_strName = new String(name);
	}
	
	public void setType(String type) {
		m_strType = new String(type);
	}
	
	public void setIsPrimary(boolean primary) {
		m_bPrimary = primary;
	}
	
	public void setIsUnique(boolean unique) {
		m_bUnique = unique;
	}
	
	public void setIsNotNull(boolean notnull) {
		m_bNotNull = notnull;
	}
	
	public void setIsAutoIncrement(boolean autoincrement) {
		m_bAutoIncrement = autoincrement;
	}
	
	/////////////////////////////////////////////////////////////////
	
	public ArrayList<ColRelations> getPropertyList() {
		return m_arrRelations;
	}
	
	public String getName() {
		return m_strName;
	}
	
	public String getType() {
		return m_strType;
	}
	
	public boolean getIsPrimary() {
		return m_bPrimary;
	}
	
	public boolean getIsNotNull() {
		return m_bNotNull;
	}
	
	public boolean getIsUnique() {
		return m_bUnique;
	}
	
	public boolean getIsAutoIncrement() {
		return m_bAutoIncrement;
	}
	
}
