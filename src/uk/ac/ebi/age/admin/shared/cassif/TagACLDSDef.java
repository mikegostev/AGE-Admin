package uk.ac.ebi.age.admin.shared.cassif;

import uk.ac.ebi.age.admin.shared.ds.DSDef;
import uk.ac.ebi.age.admin.shared.ds.DSField;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.types.FieldType;

public class TagACLDSDef extends DSDef
{
 public static TagACLDSDef instance;
 public static DSField keyField = new DSField("key",FieldType.TEXT,"Key");
 public static DSField pTypeField = new DSField("ptype",FieldType.TEXT,"Type");
 public static DSField sTypeField = new DSField("stype",FieldType.TEXT,"U/G");
 public static DSField sIdField = new DSField("sid",FieldType.TEXT,"Subject");
 public static DSField pIdField = new DSField("pid",FieldType.TEXT,"Permission");
 
 static
 {
  keyField.setHidden(true);
  keyField.setPrimaryKey( true );
 }
 
 public TagACLDSDef()
 {
  addField( keyField );
  addField( pTypeField );
  addField( sTypeField );
  addField( sIdField );
  addField( pIdField );
 }

 public static TagACLDSDef getInstance()
 {
  if( instance == null )
   instance = new TagACLDSDef();
  
  return instance;
 }
 
 public DataSource createDataSource()
 {
  RestDataSource ds = new RestDataSource();
  
  ds.setAutoCacheAllData(false);
  ds.setCacheAllData(false);
  
  DataSourceField keyF = new DataSourceField(keyField.getFieldId(), keyField.getType(), keyField.getFieldTitle());
  DataSourceField ptypeF = new DataSourceField(pTypeField.getFieldId(), pTypeField.getType(), pTypeField.getFieldTitle());
  DataSourceField stypeF = new DataSourceField(sTypeField.getFieldId(), sTypeField.getType(), sTypeField.getFieldTitle());
  DataSourceField sidF = new DataSourceField(sIdField.getFieldId(), sIdField.getType(), sIdField.getFieldTitle());
  DataSourceField pidF = new DataSourceField(pIdField.getFieldId(), pIdField.getType(), pIdField.getFieldTitle());
  keyF.setPrimaryKey(true);
  
  ds.setFields(keyF, ptypeF, stypeF, sidF, pidF);
  
  
  return ds;
 }
}
