package de.greenrobot.daogenerator.gentest;

import java.util.ArrayList;

public class DefSchema {

	int m_Version = 0;
	String m_strName = null;
	
	//ArrayList<DataBase> m_DataBases;
	
	public DefSchema() {
		//m_DataBases = new ArrayList<DataBase>();
	}
	
	//public void setDatabaseList(ArrayList<DataBase> databases) {
	//	m_DataBases = databases;
	//}
	
	public void setVersion(int version) {
		m_Version = version;
	}
	
	public void setName(String name) {
		m_strName = new String(name);
	}
	
	////////////////////////////////////////////////////////////////////
	
	public int getVersion() {
		return m_Version;
	}
	
	public String getName() {
		return m_strName;
	}
	
	//public ArrayList<DataBase> getDataBases() {
	//	return m_DataBases;
	//}
	
}
