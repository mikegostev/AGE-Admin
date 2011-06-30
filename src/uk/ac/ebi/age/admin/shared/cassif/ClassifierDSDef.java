package uk.ac.ebi.age.admin.shared.cassif;

import uk.ac.ebi.age.admin.shared.ds.DSDef;
import uk.ac.ebi.age.admin.shared.ds.DSField;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.types.FieldType;

public class ClassifierDSDef extends DSDef
{
 public static ClassifierDSDef instance;
 public static DSField idField = new DSField("id",FieldType.TEXT,"Classifier ID");
 public static DSField descField = new DSField("desc",FieldType.TEXT,"Description");
 
 static
 {
  idField.setPrimaryKey( true );
  idField.setWidth(150);
  
  descField.setEditable( true );
 }
 
 public ClassifierDSDef()
 {
  addField( idField );
  addField( descField );
 }

 public static ClassifierDSDef getInstance()
 {
  if( instance == null )
   instance = new ClassifierDSDef();
  
  return instance;
 }
 
 public DataSource createDataSource()
 {
  RestDataSource ds = new RestDataSource();
  
  ds.setAutoCacheAllData(false);
  ds.setCacheAllData(false);
  
  DataSourceField idF = new DataSourceField(idField.getFieldId(), idField.getType(), idField.getFieldTitle());
  DataSourceField descF = new DataSourceField(descField.getFieldId(), descField.getType(), descField.getFieldTitle());
  idF.setPrimaryKey(true);
  descF.setCanEdit(true);
  
  ds.setFields(idF,descF);
  
  
  return ds;
 }
}
