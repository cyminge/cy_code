package de.greenrobot.daogenerator.gentest;


import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


public class PullDataBaseParser {
	
    public ArrayList<DataBase> parse() throws Exception {
    	
    	//DefSchema schema = new DefSchema();
    	
		ArrayList<DataBase> databases = null;
		DataBase database = null;
		Table table = null;
		Column column = null;
		ColRelations relations = null;
		int nStatus = 0;
		
		String strTemp = null;
		int nAttrCount = 0;
		
        //XmlPullParserFactory
        try {
    		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
    		XmlPullParser parser = factory.newPullParser();
    		
//    		XmlPullParser parser = Xml.newPullParser();	//
        	//parser.setInput(Thread.currentThread().getContextClassLoader().getResourceAsStream("databases.xml"), "UTF-8");
    		parser.setInput(getClass().getResourceAsStream("/databases.xml"), "UTF-8");
        	//parser.setInput(new FileReader("./databases.xml"));

    		int eventType = parser.getEventType();
    		
        	try {
        		while (eventType != XmlPullParser.END_DOCUMENT) {
        			switch (eventType) {
        			case XmlPullParser.START_DOCUMENT:
        				databases = new ArrayList<DataBase>();
        				nStatus = 0;
        				break;
        				
        			case XmlPullParser.START_TAG:
        				nAttrCount = parser.getAttributeCount();
        				if (parser.getName().equals("schema")) {
        					for (int i = 0; i < nAttrCount; i++) {
            					strTemp = parser.getAttributeName(i);
            					
            					if ("version".equals(strTemp)) {
            						database.setVersion(Integer.valueOf(parser.getAttributeValue(i)));
    							}
            					else if ("package".equals(strTemp)) {
            						database.setPackageName(parser.getAttributeValue(i));
    							}
            					else if ("entity".equals(strTemp)) {
            						database.setEntityName(parser.getAttributeValue(i));
    							}
            					else if ("dao".equals(strTemp)) {
            						database.setDaoName(parser.getAttributeValue(i));
    							}
							}
        				}
        				else if (parser.getName().equals("database")) {
        					database = new DataBase();
        					database.setName(parser.getAttributeValue(0));
        					nStatus = 1;
        				} else if (parser.getName().equals("table")) {
        					table = new Table();
        					table.setName(parser.getAttributeValue(0));
        					nStatus = 2;
        				}  else if (parser.getName().equals("column")) {
        					column = new Column();
            				nAttrCount = parser.getAttributeCount();
        					
        					for (int i = 0; i < nAttrCount; i++) {
            					strTemp = parser.getAttributeName(i);
            					if ("name".equals(strTemp)) {
            						column.setName(parser.getAttributeValue(i));
    							}
            					else if ("type".equals(strTemp)) {
            						column.setType(parser.getAttributeValue(i));
    							}
            					else if ("primary".equals(strTemp)) {
            						column.setIsPrimary(Boolean.valueOf(parser.getAttributeValue(i)));
    							}
            					else if ("notnull".equals(strTemp)) {
            						column.setIsNotNull(Boolean.valueOf(parser.getAttributeValue(i)));
    							}
            					else if ("unique".equals(strTemp)) {
            						column.setIsUnique(Boolean.valueOf(parser.getAttributeValue(i)));
    							}
            					else if ("autoincrement".equals(strTemp)) {
            						column.setIsAutoIncrement(Boolean.valueOf(parser.getAttributeValue(i)));
    							}
							}
        					
        					nStatus = 3;
        				}  else if (parser.getName().equals("relation")) {
        					relations = new ColRelations();
        					
        					for (int i = 0; i < nAttrCount; i++) {
            					strTemp = parser.getAttributeName(i);
            					if ("name".equals(strTemp)) {
                					relations.setName(parser.getAttributeValue(i));
    							}
            					else if ("totable".equals(strTemp)) {
                					relations.setToTableName(parser.getAttributeValue(i));
    							}
            					else if ("type".equals(strTemp)) {
                					String type = parser.getAttributeValue(i);
                					if ("toone".equals(type)) {
                    					relations.setType(ColRelations.TYPE_TOONE);
									}
                					else if ("tomany".equals(type)) {
                    					relations.setType(ColRelations.TYPE_TOMANY);
									}
    							}
							}
        				}
        				break;
        				
        			case XmlPullParser.END_TAG:
        				if (parser.getName().equals("database")) {
        					databases.add(database);
        					database = null;
        				}
        				else if (parser.getName().equals("table")) {
        					database.getTableList().add(table);
        					table = null;
        				}
        				else if (parser.getName().equals("column")) {
        					table.getColumnList().add(column);
        					column = null;
        				}
        				else if (parser.getName().equals("relation")) {
        					column.getPropertyList().add(relations);
        					relations = null;
        				}
        				break;
        			}
        			
        			eventType = parser.next();
        		}
        		
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        	
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        
        //schema.setDatabaseList(databases);
        
		return databases;
	}
	
}
