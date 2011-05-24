package uk.ac.ebi.age.admin.shared.auth;

import uk.ac.ebi.age.admin.shared.ds.DSDef;
import uk.ac.ebi.age.admin.shared.ds.DSField;

import com.smartgwt.client.types.FieldType;

public class UserDSDef extends DSDef
{
 public static UserDSDef instance;
 
 public UserDSDef()
 {
  addField( new DSField("userid",FieldType.TEXT,"User ID", 150) );
  addField( new DSField("username",FieldType.TEXT,"User Name") );
 }

 public static UserDSDef getInstance()
 {
  if( instance == null )
   instance = new UserDSDef();
  
  return instance;
 }
}
