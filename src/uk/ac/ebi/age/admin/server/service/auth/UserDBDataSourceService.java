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
import uk.ac.ebi.age.authz.User;
import uk.ac.ebi.age.authz.exception.AuthDBException;
import uk.ac.ebi.age.transaction.ReadLock;
import uk.ac.ebi.age.transaction.Transaction;
import uk.ac.ebi.age.transaction.TransactionException;

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
  
  
  Transaction trn = db.startTransaction();

  try
  {
   db.deleteUser( trn, userId );
   
   try
   {
    db.commitTransaction(trn);
   }
   catch(TransactionException e)
   {
    resp.setErrorMessage("Transaction error: "+e.getMessage());
   }
  
  }
  catch(AuthDBException e)
  {
   try
   {
    db.rollbackTransaction(trn);
    resp.setErrorMessage(e.getMessage());
   }
   catch(TransactionException e1)
   {
    resp.setErrorMessage("Transaction error: "+e1.getMessage());
   }

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
  
  
  
  Transaction trn = db.startTransaction();

  try
  {
   db.addUser( trn, userId, userName, userPass );
   
   try
   {
    db.commitTransaction(trn);
   }
   catch(TransactionException e)
   {
    resp.setErrorMessage("Transaction error: "+e.getMessage());
   }
  
  }
  catch(AuthDBException e)
  {
   try
   {
    db.rollbackTransaction(trn);
    resp.setErrorMessage(e.getMessage());
   }
   catch(TransactionException e1)
   {
    resp.setErrorMessage("Transaction error: "+e1.getMessage());
   }

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
  
  
  
  Transaction trn = db.startTransaction();

  try
  {
   if( userPass != null )
    db.setUserPassword(  trn, userId, userPass  );
   else
    db.updateUser( trn, userId, userName);
   
   try
   {
    db.commitTransaction(trn);
   }
   catch(TransactionException e)
   {
    resp.setErrorMessage("Transaction error: "+e.getMessage());
   }
  
  }
  catch(AuthDBException e)
  {
   try
   {
    db.rollbackTransaction(trn);
    resp.setErrorMessage(e.getMessage());
   }
   catch(TransactionException e1)
   {
    resp.setErrorMessage("Transaction error: "+e1.getMessage());
   }

  }
  
  return resp;
 }

 private DataSourceResponse processFetch(DataSourceRequest dsr)
 {
  ReadLock lck = db.getReadLock();
  DataSourceResponse resp = new DataSourceResponse(lck);
  
  Map<DSField, String> vmap = dsr.getValueMap();
  
  if( vmap == null || vmap.size() == 0 )
  {
   List<? extends User> res=db.getUsers(lck, dsr.getBegin(), dsr.getEnd() );
   
   resp.setTotal( db.getUsersTotal(lck) );
   resp.setSize(res.size());
   resp.setIterator( new UserMapIterator(res) );
  }
  else
  {
   ListFragment<User> res=db.getUsers(lck, vmap.get(UserDSDef.userIdField), vmap.get(UserDSDef.userNameField), dsr.getBegin(), dsr.getEnd() );
  
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
