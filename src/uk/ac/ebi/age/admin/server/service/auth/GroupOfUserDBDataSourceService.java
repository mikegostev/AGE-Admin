package uk.ac.ebi.age.admin.server.service.auth;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import uk.ac.ebi.age.admin.server.service.ds.DataSourceBackendService;
import uk.ac.ebi.age.admin.server.service.ds.DataSourceRequest;
import uk.ac.ebi.age.admin.server.service.ds.DataSourceResponse;
import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.admin.shared.auth.GroupDSDef;
import uk.ac.ebi.age.admin.shared.ds.DSField;
import uk.ac.ebi.age.authz.AuthDB;
import uk.ac.ebi.age.authz.UserGroup;
import uk.ac.ebi.age.authz.exception.AuthException;
import uk.ac.ebi.age.transaction.ReadLock;
import uk.ac.ebi.age.transaction.Transaction;
import uk.ac.ebi.age.transaction.TransactionException;

import com.pri.util.collection.MapIterator;

public class GroupOfUserDBDataSourceService implements DataSourceBackendService
{
 private AuthDB db;
 
 public GroupOfUserDBDataSourceService(AuthDB authDB)
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
  
  String grpId = vmap.get(GroupDSDef.grpIdField);
  
  if( grpId == null )
  {
   resp.setErrorMessage(GroupDSDef.grpIdField.getFieldTitle()+" should not be null");
   return resp;
  }
  
  String userId = dsr.getRequestParametersMap().get(Constants.userIdParam);
  
  if( userId == null )
  {
   resp.setErrorMessage("No user ID");
   return resp;
  }

  
  Transaction trn = db.startTransaction();

  try
  {
   db.removeUserFromGroup( trn, grpId, userId );
   
   try
   {
    db.commitTransaction(trn);
   }
   catch(TransactionException e)
   {
    resp.setErrorMessage("Transaction error: "+e.getMessage());
   }
  
  }
  catch(AuthException e)
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
  String grpId = vmap.get(GroupDSDef.grpIdField);
  
  if( grpId == null )
  {
   resp.setErrorMessage(GroupDSDef.grpIdField.getFieldTitle()+" should not be null");
   return resp;
  }
  
  String userId = dsr.getRequestParametersMap().get(Constants.userIdParam);
  
  if( userId == null )
  {
   resp.setErrorMessage("No user ID");
   return resp;
  }
  
  Transaction trn = db.startTransaction();

  try
  {
   db.addUserToGroup( trn, grpId, userId );
   
   try
   {
    db.commitTransaction(trn);
   }
   catch(TransactionException e)
   {
    resp.setErrorMessage("Transaction error: "+e.getMessage());
   }
  
  }
  catch(AuthException e)
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

  resp.setErrorMessage("Operation not supported");
  
  return resp;
 }

 private DataSourceResponse processFetch(DataSourceRequest dsr)
 {
  ReadLock rl = db.getReadLock();
  DataSourceResponse resp = new DataSourceResponse(rl);
  
  String userId = dsr.getRequestParametersMap().get(Constants.userIdParam);
  
  if( userId == null )
  {
   resp.setErrorMessage("No user ID");
   return resp;
  }
  
  
  Collection< ? extends UserGroup> res;
  try
  {
   res = db.getGroupsOfUser( rl, userId );

   resp.setTotal( res.size() );
   resp.setSize(res.size());
   resp.setIterator( new GroupMapIterator(res) );
  }
  catch(AuthException e)
  {
   resp.setErrorMessage("Error");
  }
 
  return resp;
 }

 @Override
 public GroupDSDef getDSDefinition()
 {
  return GroupDSDef.getInstance();
 }
 
 class GroupMapIterator implements MapIterator<DSField, String>
 {
  private Iterator<? extends UserGroup> grpIter;
  private UserGroup cGrp;
  
  GroupMapIterator( Collection<? extends UserGroup> lst )
  {
   grpIter = lst.iterator();
  }

  @Override
  public boolean next()
  {
   if( ! grpIter.hasNext() )
    return false;

   cGrp = grpIter.next();
   
   return true;
  }

  @Override
  public String get(DSField key)
  {
   if( key.equals(GroupDSDef.grpIdField) )
    return cGrp.getId();
   
   if( key.equals(GroupDSDef.grpDescField) )
    return cGrp.getDescription();

   return null;
  }
  
 }
}
