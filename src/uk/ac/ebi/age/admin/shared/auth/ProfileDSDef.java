package uk.ac.ebi.age.admin.shared.auth;

import uk.ac.ebi.age.admin.shared.ds.DSDef;
import uk.ac.ebi.age.admin.shared.ds.DSField;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.types.FieldType;

public class ProfileDSDef extends DSDef
{
 public static ProfileDSDef instance;
 public static DSField profIdField = new DSField("profid",FieldType.TEXT,"Profile name");
 public static DSField profDescField = new DSField("profdesc",FieldType.TEXT,"Description");
 
 static
 {
  profIdField.setPrimaryKey( true );
  profIdField.setWidth(150);
  
  profDescField.setEditable( true );
 }
 
 public ProfileDSDef()
 {
  addField( profIdField );
  addField( profDescField );
 }

 public static ProfileDSDef getInstance()
 {
  if( instance == null )
   instance = new ProfileDSDef();
  
  return instance;
 }
 
 public DataSource createDataSource()
 {
  RestDataSource ds = new RestDataSource();
  
  ds.setAutoCacheAllData(false);
  ds.setCacheAllData(false);
  
  DataSourceField idF = new DataSourceField(profIdField.getFieldId(), profIdField.getType(), profIdField.getFieldTitle());
  DataSourceField descF = new DataSourceField(profDescField.getFieldId(), profDescField.getType(), profDescField.getFieldTitle());
  idF.setPrimaryKey(true);
  descF.setCanEdit(true);
  
  ds.setFields(idF,descF);
  
  return ds;
 }
}
