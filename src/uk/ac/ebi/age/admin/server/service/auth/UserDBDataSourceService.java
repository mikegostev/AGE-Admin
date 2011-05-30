package uk.ac.ebi.age.admin.server.service.auth;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import uk.ac.ebi.age.admin.server.service.ds.DataSourceBackendService;
import uk.ac.ebi.age.admin.server.service.ds.DataSourceRequest;
import uk.ac.ebi.age.admin.server.service.ds.DataSourceResponse;
import uk.ac.ebi.age.admin.shared.auth.UserDSDef;
import uk.ac.ebi.age.admin.shared.ds.DSField;
import uk.ac.ebi.age.authz.AuthDB;
import uk.ac.ebi.age.authz.AuthException;
import uk.ac.ebi.age.authz.User;

import com.pri.util.collection.ListFragment;
import com.pri.util.collection.MapIterator;

public class UserDBDataSourceService implements DataSourceBackendService
{
 private AuthDB db;
 
 public UserDBDataSourceService(AuthDB authDB)
 {
  db = authDB;
 }

 @Override
 public DataSourceResponse processRequest(DataSourceRequest dsr)
 {
  switch( dsr.getRequestType() )
  {
   case FETCH:
    return processFetch( dsr );
   case UPDATE:
    return processUpdate( dsr );
   case ADD:
    return processAdd( dsr );
   case DELETE:
    return processDelete( dsr );
  }
  
  return null;
 }

 private DataSourceResponse processDelete(DataSourceRequest dsr)
 {
  DataSourceResponse resp = new DataSourceResponse();

  Map<DSField, String> vmap = dsr.getValueMap();
  
  String userId = vmap.get(UserDSDef.userIdField);
  
  if( userId == null )
  {
   resp.setErrorMessage(UserDSDef.userIdField.getFieldTitle()+" should not be null");
   return resp;
  }
  
  
  try
  {
   db.deleteUser( userId );
  }
  catch(AuthException e)
  {
   resp.setErrorMessage("User with ID '"+userId+"' doesn't exist");
  }
  
  return resp;
 }

 private DataSourceResponse processAdd(DataSourceRequest dsr)
 {
  DataSourceResponse resp = new DataSourceResponse();

  Map<DSField, String> vmap = dsr.getValueMap();
  
  String userId = vmap.get(UserDSDef.userIdField);
  String userName = vmap.get(UserDSDef.userNameField);
  String userPass = vmap.get(UserDSDef.userPassField);
  
  if( userId == null )
  {
   resp.setErrorMessage(UserDSDef.userIdField.getFieldTitle()+" should not be null");
   return resp;
  }
  
  
  try
  {
   db.addUser( userId, userName, userPass );
  }
  catch(AuthException e)
  {
   resp.setErrorMessage("User with ID '"+userId+"' exists");
  }
  
  return resp;
  
 }

 private DataSourceResponse processUpdate(DataSourceRequest dsr)
 {
  DataSourceResponse resp = new DataSourceResponse();

  Map<DSField, String> vmap = dsr.getValueMap();
  
  String userId = vmap.get(UserDSDef.userIdField);
  String userName = vmap.get(UserDSDef.userNameField);
  String userPass = vmap.get(UserDSDef.userPassField);
  
  if( userId == null )
  {
   resp.setErrorMessage(UserDSDef.userIdField.getFieldTitle()+" should not be null");
   return resp;
  }
  
  
  try
  {
   db.updateUser( userId, userName, userPass );
  }
  catch(AuthException e)
  {
   resp.setErrorMessage("Error");
  }
  
  return resp;
 }

 private DataSourceResponse processFetch(DataSourceRequest dsr)
 {
  DataSourceResponse resp = new DataSourceResponse();
  
  Map<DSField, String> vmap = dsr.getValueMap();
  
  if( vmap == null || vmap.size() == 0 )
  {
   List<? extends User> res=db.getUsers( dsr.getBegin(), dsr.getEnd() );
   
   resp.setTotal( db.getUsersTotal() );
   resp.setSize(res.size());
   resp.setIterator( new UserMapIterator(res) );
  }
  else
  {
   ListFragment<User> res=db.getUsers( vmap.get(UserDSDef.userIdField), vmap.get(UserDSDef.userNameField), dsr.getBegin(), dsr.getEnd() );
  
   resp.setTotal(res.getTotalLength());
   resp.setSize(res.getList().size());
   resp.setIterator( new UserMapIterator(res.getList()) );
  }
  
  return resp;
 }

 @Override
 public UserDSDef getDSDefinition()
 {
  return UserDSDef.getInstance();
 }
 
 class UserMapIterator implements MapIterator<DSField, String>
 {
  private Iterator<? extends User> userIter;
  private User cUser;
  
  UserMapIterator( List<? extends User> lst )
  {
   userIter = lst.iterator();
  }

  @Override
  public boolean next()
  {
   if( ! userIter.hasNext() )
    return false;

   cUser = userIter.next();
   
   return true;
  }

  @Override
  public String get(DSField key)
  {
   if( key.equals(UserDSDef.userIdField) )
    return cUser.getId();
   
   if( key.equals(UserDSDef.userNameField) )
    return cUser.getName();

   return null;
  }
  
 }
}
