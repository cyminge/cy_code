/*
 * Copyright (C) 2011 Markus Junginger, greenrobot (http://greenrobot.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.greenrobot.daogenerator.gentest;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Property.PropertyBuilder;
import de.greenrobot.daogenerator.Schema;

/**
 * Generates entities and DAOs for the example project DaoExample.
 * 
 * Run it as a Java application (not Android).
 * 
 * @author Markus
 */
public class ExampleDaoGenerator {
	/**
	 * 	public int prov_id;
		public int city_id;
		public String city_name;
	 * @param schema
	 */

    /*public ArrayList<DataBase> parse1() throws Exception {
		ArrayList<DataBase> databases = null;
		DataBase database = null;
		Table table = null;
		Column column = null;
		ColRelations relations = null;
		int nStatus = 0;
		
		String toTableName = null;
		String toType = null;
		
        //XmlPullParserFactory
        try {
    		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
    		XmlPullParser parser = factory.newPullParser();
    		
//    		XmlPullParser parser = Xml.newPullParser();	//
        	parser.setInput(getClass().getResourceAsStream("Student.xml"), "UTF-8");
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
        				if (parser.getName().equals("database")) {
        					database = new DataBase();
        					database.setName(parser.getAttributeValue(0));
        					nStatus = 1;
        				} else if (parser.getName().equals("table")) {
        					table = new Table();
        					table.setName(parser.getAttributeValue(0));
        					nStatus = 2;
        				}  else if (parser.getName().equals("column")) {
        					column = new Column();
        					column.setName(parser.getAttributeValue(0));
        					nStatus = 3;
        				}  else if (parser.getName().equals("relation")) {
        					relations = new ColRelations();
        					relations.setName(parser.getAttributeValue(0));
        					relations.setToTableName(parser.getAttributeValue(1));
        					relations.setToType(parser.getAttributeValue(2));
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
		return databases;
	}*/
	
	
	public static Schema addDataBases(ArrayList<DataBase> dataBases) {
		Schema schema = null;
		String strTemp = null;
		
        try {
    		for(DataBase db:dataBases) {
    			String strPackage = db.getPackageName();
    	        strTemp = strPackage+"."+db.getEntityName();
    	        schema = new Schema(db.getVersion(), strTemp);
    	        schema.setDefaultJavaPackageTest(strTemp);
    	        strTemp = strPackage+"."+db.getDaoName();
    	        schema.setDefaultJavaPackageDao(strTemp);
    			addEntity(schema, db.getTableList());
    			
    			new DaoGenerator().generateAll(schema, "./gen");
    		}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return schema;
	}
	
	public static void addEntity(Schema schema, ArrayList<Table> table) throws ClassNotFoundException {
		if (null == table) {
			return;
		}
		
		@SuppressWarnings("rawtypes")
		Class [] paraTypes = new Class[1];
		paraTypes[0] = "".getClass();
		
		ArrayList<Column> column = null;
		Entity entity = null;
		String type = null;
        PropertyBuilder builder = null;//entity.addIntProperty();
        
        ArrayList<Entity> fromTable = new ArrayList<Entity>();
        ArrayList<Property> toProperty = new ArrayList<Property>();
        ArrayList<ColRelations> relations = new ArrayList<ColRelations>();
        
        // add table
		for(Table tb:table) {
			entity = schema.addEntity(tb.getName());
			column = tb.m_arrColumn;
			for(Column col:column) {
				/*Create key*/
		        type = col.getType();
		        if (null == type || 0 == type.length()) {
					continue;
				}
		        if ("Id".equals(type)) {
		        	builder = entity.addIdProperty();
					continue;
				}
		        
		        try {
		        	String strMethod = "add"+type+"Property";
					Method method = Entity.class.getMethod(strMethod, paraTypes);
					builder = (PropertyBuilder) method.invoke(entity, col.getName());
					if (col.getIsPrimary()) {
						builder.primaryKey();
					}
					if (col.getIsNotNull()) {
						builder.notNull();
					}
					if (col.getIsUnique()) {
						builder.unique();
					}
					if (col.getIsAutoIncrement()) {
						builder.autoincrement();
					}
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
		        
		        for(ColRelations rel:col.getPropertyList()) {
		        	fromTable.add(entity);
		        	toProperty.add(builder.getProperty());
		        	relations.add(rel);
		        }
			}
			entity.implementsSerializable();
		}
		
		//add relations
		int ntype = 0;
		int ncount = relations.size();
		List<Entity> entityList = schema.getEntities();
		//List<Property> propertyList = null;
		//Property curProperty = null;
		ColRelations curRel = null;
		
		for(int i=0; i < ncount; i++) {
			curRel = relations.get(i);
			ntype = curRel.getType();
			for(Entity ent:entityList) {
				if (ent.getClassName().equals(curRel.getToTableName())) {
					entity = ent;
					/*if (ntype == ColRelations.TYPE_TOMANY) {
						propertyList = ent.getProperties();
						for(Property pro:propertyList) {
							if (pro.getColumnName().equals(toProperty.get(i).getColumnName())) {
								curProperty = pro;
							}
						}
					}*/
				}
			}
			
			if (ntype == ColRelations.TYPE_TOONE) {
				fromTable.get(i).addToOne(entity, toProperty.get(i), curRel.getName());
			}
			else if (ntype == ColRelations.TYPE_TOMANY) {
				entity.addToMany(fromTable.get(i), toProperty.get(i), curRel.getName());
			}
		}
	}

    public static void main(String[] args) throws Exception {
        PullDataBaseParser parser = new PullDataBaseParser();
        ArrayList<DataBase> databases = parser.parse();
        
        Schema schema = addDataBases(databases);
        //Schema schema = new Schema(3, "com.wkl.chat.entity");
        //schema.setDefaultJavaPackageTest("com.wkl.chat.entity");
        //schema.setDefaultJavaPackageDao("com.wkl.chat.dao");
        
        //new ExampleDaoGenerator().parse1();

//        addNote(schema);
//        addCustomerOrder(schema);
        
        //addCityInfo(schema);
        //addEntity(schema, databases);

        //new DaoGenerator().generateAll(schema, "./gen");
    }


}
