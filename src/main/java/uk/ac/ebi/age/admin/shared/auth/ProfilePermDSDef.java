package uk.ac.ebi.age.admin.shared.auth;

import uk.ac.ebi.age.admin.shared.ds.DSDef;
import uk.ac.ebi.age.admin.shared.ds.DSField;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.types.FieldType;

public class ProfilePermDSDef extends DSDef
{
 public static ProfilePermDSDef instance;
 public static DSField keyField = new DSField("key",FieldType.TEXT,"Key");
 public static DSField typeField = new DSField("type",FieldType.TEXT,"Type");
 public static DSField idField = new DSField("id",FieldType.TEXT,"Permission");
 public static DSField descField = new DSField("desc",FieldType.TEXT,"Description");
 
 static
 {
  keyField.setHidden(true);
  keyField.setPrimaryKey( true );
  idField.setWidth(150);
  
  descField.setEditable( false );
 }
 
 public ProfilePermDSDef()
 {
  addField( keyField );
  addField( typeField );
  addField( idField );
  addField( descField );
 }

 public static ProfilePermDSDef getInstance()
 {
  if( instance == null )
   instance = new ProfilePermDSDef();
  
  return instance;
 }
 
 public DataSource createDataSource()
 {
  RestDataSource ds = new RestDataSource();
  
  ds.setAutoCacheAllData(false);
  ds.setCacheAllData(false);
  
  DataSourceField keyF = new DataSourceField(keyField.getFieldId(), keyField.getType(), keyField.getFieldTitle());
  DataSourceField typeF = new DataSourceField(typeField.getFieldId(), typeField.getType(), typeField.getFieldTitle());
  DataSourceField idF = new DataSourceField(idField.getFieldId(), idField.getType(), idField.getFieldTitle());
  DataSourceField descF = new DataSourceField(descField.getFieldId(), descField.getType(), descField.getFieldTitle());
  keyF.setPrimaryKey(true);
  
  ds.setFields(keyF, typeF, idF, descF);
  
  
  return ds;
 }
}
