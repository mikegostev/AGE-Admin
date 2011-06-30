package uk.ac.ebi.age.admin.shared.auth;

import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.admin.shared.ds.DSDef;
import uk.ac.ebi.age.admin.shared.ds.DSField;

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
  RestDataSource ds = new RestDataSource();
//   { protected  void  transformResponse(DSResponse response, DSRequest request, Object data) 
//   {
//    System.out.println( "<**>"+   xmlSerialize(request.getData()) +"<**>");
//    JSOHelper.setAttribute(request.getData(), "##username", "XXX");
//    JSOHelper.setAttribute(request.getData(), "##userpass", (String)null);
//    System.out.println("Attr="+JSOHelper.getAttribute(request.getData(), "##userid"));
//
//    System.out.println(data.getClass().getName());
//    
//    super.transformResponse(response, request, data);
//   }
//  };
  
  ds.setAutoCacheAllData(false);
  ds.setCacheAllData(false);
  
  DataSourceField idF = new DataSourceField(userIdField.getFieldId(), userIdField.getType(), userIdField.getFieldTitle());
  DataSourceField nameF = new DataSourceField(userNameField.getFieldId(), userNameField.getType(), userNameField.getFieldTitle());
  idF.setPrimaryKey(true);
  nameF.setCanEdit(true);
  
  
  ds.setFields(idF,nameF);
  
  ds.setID(Constants.userListServiceName);
  
  return ds;
 }
}
