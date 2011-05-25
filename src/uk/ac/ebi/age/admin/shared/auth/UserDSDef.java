package uk.ac.ebi.age.admin.shared.auth;

import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.admin.shared.ds.DSDef;
import uk.ac.ebi.age.admin.shared.ds.DSField;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.FieldType;

public class UserDSDef extends DSDef
{
 public static UserDSDef instance;
 public static DSField userIdField = new DSField("userid",FieldType.TEXT,"User ID", 150);
 public static DSField userNameField = new DSField("username",FieldType.TEXT,"User Name");
 
 public UserDSDef()
 {
  addField( userIdField );
  addField( userNameField );
 }

 public static UserDSDef getInstance()
 {
  if( instance == null )
   instance = new UserDSDef();
  
  return instance;
 }
 
 public DataSource createDataSource()
 {
  DataSource ds = super.createDataSource();
  
  ds.setID(Constants.userListServiceName);
  
  return ds;
 }
}
