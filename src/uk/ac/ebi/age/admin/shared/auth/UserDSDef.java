package uk.ac.ebi.age.admin.shared.auth;

import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.admin.shared.ds.DSDef;
import uk.ac.ebi.age.admin.shared.ds.DSField;

import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.types.FieldType;

public class UserDSDef extends DSDef
{
 public static UserDSDef instance;
 public static DSField userIdField = new DSField("userid",FieldType.TEXT,"User ID");
 public static DSField userNameField = new DSField("username",FieldType.TEXT,"User Name");
 public static DSField userPassField = new DSField("userpass",FieldType.PASSWORD,"Password");
 
 static
 {
  userIdField.setPrimaryKey( true );
  userIdField.setWidth(150);
  
  userNameField.setEditable( true );
  userPassField.setHidden( true );
 }
 
 public UserDSDef()
 {
  addField( userIdField );
  addField( userNameField );
  addField( userPassField );
 }

 public static UserDSDef getInstance()
 {
  if( instance == null )
   instance = new UserDSDef();
  
  return instance;
 }
 
 public DataSource createDataSource()
 {
 // DataSource ds = super.createDataSource();
  RestDataSource ds = new RestDataSource() {
   protected  void  transformResponse(DSResponse response, DSRequest request, Object data) 
   {
    super.transformResponse(response, request, data);
   }
  };
  
  ds.setAutoCacheAllData(false);
  ds.setCacheAllData(false);
  
  DataSourceField fArr[] = new DataSourceField[getFields().size()];
  
  int i=0;
  for( DSField f : getFields() )
  {
   DataSourceField dsf=null;
   
   if( f.getWidth() >  0 )
    dsf = new DataSourceField(f.getFieldId(), f.getType(), f.getFieldTitle(), f.getWidth());
   else
    dsf = new DataSourceField(f.getFieldId(), f.getType(), f.getFieldTitle());
   
   dsf.setPrimaryKey( f.isPrimaryKey() );
   dsf.setCanEdit(f.isEditable());
   dsf.setHidden( f.isHidden() );
   
   fArr[i++]=dsf;
  }
  
  ds.setFields(fArr);
  
  ds.setID(Constants.userListServiceName);
  
  return ds;
 }
}
