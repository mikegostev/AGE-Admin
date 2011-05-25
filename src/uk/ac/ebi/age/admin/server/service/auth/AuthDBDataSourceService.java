package uk.ac.ebi.age.admin.server.service.auth;

import java.util.Iterator;
import java.util.List;

import uk.ac.ebi.age.admin.server.service.ds.DataSourceBackendService;
import uk.ac.ebi.age.admin.server.service.ds.DataSourceRequest;
import uk.ac.ebi.age.admin.server.service.ds.DataSourceResponse;
import uk.ac.ebi.age.admin.shared.auth.UserDSDef;
import uk.ac.ebi.age.admin.shared.ds.DSField;
import uk.ac.ebi.age.authz.AuthDB;
import uk.ac.ebi.age.authz.User;

import com.pri.util.collection.ListFragment;
import com.pri.util.collection.MapIterator;

public class AuthDBDataSourceService implements DataSourceBackendService
{
 private static UserDSDef dsDefinition;
 
 private AuthDB db;
 
 public AuthDBDataSourceService(AuthDB authDB)
 {
  db = authDB;
 }

 @Override
 public DataSourceResponse processRequest(DataSourceRequest dsr)
 {
  switch( dsr.getRequestType() )
  {
   case FETCH:
    return processFetch(dsr);
  }
  
  return null;
 }

 private DataSourceResponse processFetch(DataSourceRequest dsr)
 {
  DataSourceResponse resp = new DataSourceResponse();
  
  if( dsr.getValueMap() == null || dsr.getValueMap().size() == 0 )
  {
   List<User> res=db.getUsers( dsr.getBegin(), dsr.getEnd() );
   
   resp.setTotal( db.getUsersTotal() );
   resp.setSize(res.size());
   resp.setIterator( new UserMapIterator(res) );
  }
  else
  {
   ListFragment<User> res=db.getUsers( dsr.getValueMap().get(UserDSDef.userIdField), dsr.getValueMap().get(UserDSDef.userNameField), dsr.getBegin(), dsr.getEnd() );
  
   resp.setTotal(res.getTotalLength());
   resp.setSize(res.getList().size());
   resp.setIterator( new UserMapIterator(res.getList()) );
  }
  
  return resp;
 }

 @Override
 public UserDSDef getDSDefinition()
 {
  if( dsDefinition == null )
   dsDefinition = new UserDSDef();

  return dsDefinition;
 }
 
 class UserMapIterator implements MapIterator<DSField, String>
 {
  private Iterator<User> userIter;
  private User cUser;
  
  UserMapIterator( List<User> lst )
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
   if( key.equals(getDSDefinition().userIdField) )
    return cUser.getId();
   
   if( key.equals(getDSDefinition().userNameField) )
    return cUser.getName();

   return null;
  }
  
 }
}
