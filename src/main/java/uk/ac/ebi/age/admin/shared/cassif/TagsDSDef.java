package uk.ac.ebi.age.admin.shared.cassif;

import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.admin.shared.ds.DSDef;
import uk.ac.ebi.age.admin.shared.ds.DSField;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.types.FieldType;

public class TagsDSDef extends DSDef
{
 public static TagsDSDef instance;
 public static DSField idField = new DSField("id",FieldType.TEXT,"Tag ID");
 public static DSField descField = new DSField("desc",FieldType.TEXT,"Description");
 public static DSField parentField = new DSField("parent",FieldType.TEXT,"Parent");
 
 static
 {
  idField.setPrimaryKey( true );
  idField.setWidth(150);
  
  descField.setEditable( true );
 }
 
 public TagsDSDef()
 {
  addField( idField );
  addField( descField );
  addField( parentField );
 }

 public static TagsDSDef getInstance()
 {
  if( instance == null )
   instance = new TagsDSDef();
  
  return instance;
 }
 
 public DataSource createDataSource()
 {
  RestDataSource ds = new RestDataSource();
  
  ds.setID(Constants.tagTreeServiceName);

  ds.setAutoCacheAllData(false);
  ds.setCacheAllData(false);
  
  DataSourceField idF = new DataSourceField(idField.getFieldId(), idField.getType(), idField.getFieldTitle());
  DataSourceField descF = new DataSourceField(descField.getFieldId(), descField.getType(), descField.getFieldTitle());
  idF.setPrimaryKey(true);
  idF.setRequired(true);
  descF.setCanEdit(true);


  DataSourceField prnF = new DataSourceField(parentField.getFieldId(), parentField.getType(), parentField.getFieldTitle());  
  prnF.setRequired(true);  
  prnF.setForeignKey(Constants.tagTreeServiceName + "."+idField.getFieldId());
  prnF.setRootValue(Constants.rootTagId);
 
  
  ds.setFields(idF,descF,prnF);
  
  
  return ds;
 }
}
