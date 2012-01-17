package uk.ac.ebi.age.admin.shared.auth;

import uk.ac.ebi.age.admin.shared.ds.DSDef;
import uk.ac.ebi.age.admin.shared.ds.DSField;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.types.FieldType;

public class GroupDSDef extends DSDef
{
 public static GroupDSDef instance;
 public static DSField grpIdField = new DSField("grpid",FieldType.TEXT,"Group ID");
 public static DSField grpDescField = new DSField("grpdesc",FieldType.TEXT,"Description");
 
 static
 {
  grpIdField.setPrimaryKey( true );
  grpIdField.setWidth(150);
  
  grpDescField.setEditable( true );
 }
 
 public GroupDSDef()
 {
  addField( grpIdField );
  addField( grpDescField );
 }

 public static GroupDSDef getInstance()
 {
  if( instance == null )
   instance = new GroupDSDef();
  
  return instance;
 }
 
 public DataSource createDataSource()
 {
  RestDataSource ds = new RestDataSource();
  
  ds.setAutoCacheAllData(false);
  ds.setCacheAllData(false);
  
  DataSourceField idF = new DataSourceField(grpIdField.getFieldId(), grpIdField.getType(), grpIdField.getFieldTitle());
  DataSourceField descF = new DataSourceField(grpDescField.getFieldId(), grpDescField.getType(), grpDescField.getFieldTitle());
  idF.setPrimaryKey(true);
  descF.setCanEdit(true);
  
  ds.setFields(idF,descF);
  
  
  return ds;
 }
}
