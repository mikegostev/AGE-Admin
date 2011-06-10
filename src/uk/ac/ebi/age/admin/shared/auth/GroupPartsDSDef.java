package uk.ac.ebi.age.admin.shared.auth;

import uk.ac.ebi.age.admin.shared.ds.DSDef;
import uk.ac.ebi.age.admin.shared.ds.DSField;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.types.FieldType;

public class GroupPartsDSDef extends DSDef
{
 public static GroupPartsDSDef instance;
 public static DSField keyField = new DSField("key",FieldType.TEXT,"Key");
 public static DSField partIdField = new DSField("partid",FieldType.TEXT,"Participant ID");
 public static DSField partTypeField = new DSField("parttype",FieldType.TEXT,"Type");
 public static DSField partDescField = new DSField("partdesc",FieldType.TEXT,"Description");
 
 static
 {
  keyField.setHidden(true);
  keyField.setPrimaryKey( true );
  partIdField.setWidth(150);
  
  partDescField.setEditable( false );
 }
 
 public GroupPartsDSDef()
 {
  addField( keyField );
  addField( partTypeField );
  addField( partIdField );
  addField( partDescField );
 }

 public static GroupPartsDSDef getInstance()
 {
  if( instance == null )
   instance = new GroupPartsDSDef();
  
  return instance;
 }
 
 public DataSource createDataSource()
 {
  RestDataSource ds = new RestDataSource();
  
  ds.setAutoCacheAllData(false);
  ds.setCacheAllData(false);
  
  DataSourceField keyF = new DataSourceField(keyField.getFieldId(), keyField.getType(), keyField.getFieldTitle());
  DataSourceField typeF = new DataSourceField(partTypeField.getFieldId(), partTypeField.getType(), partTypeField.getFieldTitle());
  DataSourceField idF = new DataSourceField(partIdField.getFieldId(), partIdField.getType(), partIdField.getFieldTitle());
  DataSourceField descF = new DataSourceField(partDescField.getFieldId(), partDescField.getType(), partDescField.getFieldTitle());
  keyF.setPrimaryKey(true);
  
  ds.setFields(keyF, typeF, idF, descF);
  
  
  return ds;
 }
}
