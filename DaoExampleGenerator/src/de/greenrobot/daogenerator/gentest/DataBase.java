package de.greenrobot.daogenerator.gentest;

import java.util.ArrayList;

public class DataBase {

	String m_strName = "";
	String m_strJavaPackage = "com.wkl.db";
	String m_strPackageEntity = "entity";
	String m_strPackageDao = "dao";
	int m_nVersion = 0;
	
	ArrayList<Table> m_arrTables = null;
	
	public DataBase() {
		m_arrTables = new ArrayList<Table>();
	}
	
	public void setTableList(ArrayList<Table> tables) {
		m_arrTables = tables;
	}
	
	public void setVersion(int version) {
		m_nVersion = version;
	}
	
	public void setName(String name) {
		m_strName = new String(name);
	}
	
	public void setPackageName(String name) {
		m_strJavaPackage = new String(name);
	}
	
	public void setEntityName(String name) {
		m_strPackageEntity = new String(name);
	}
	
	public void setDaoName(String name) {
		m_strPackageDao = new String(name);
	}
	
	
	public ArrayList<Table> getTableList() {
		return m_arrTables;
	}
	
	public int getVersion() {
		return m_nVersion;
	}
	
	public String getName() {
		return m_strName;
	}
	
	public String getPackageName() {
		return m_strJavaPackage;
	}
	
	public String getEntityName() {
		return m_strPackageEntity;
	}
	
	public String getDaoName() {
		return m_strPackageDao;
	}
	
}
