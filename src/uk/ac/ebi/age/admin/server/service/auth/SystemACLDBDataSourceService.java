package uk.ac.ebi.age.admin.server.service.auth;

import java.util.Collection;
import java.util.Map;

import uk.ac.ebi.age.admin.server.service.ds.DataSourceBackendService;
import uk.ac.ebi.age.admin.server.service.ds.DataSourceRequest;
import uk.ac.ebi.age.admin.server.service.ds.DataSourceResponse;
import uk.ac.ebi.age.admin.shared.cassif.ACLDSDef;
import uk.ac.ebi.age.admin.shared.ds.DSField;
import uk.ac.ebi.age.authz.ACR;
import uk.ac.ebi.age.authz.AuthDB;
import uk.ac.ebi.age.authz.exception.AuthDBException;
import uk.ac.ebi.age.ext.authz.SystemAction;
import uk.ac.ebi.age.transaction.ReadLock;
import uk.ac.ebi.age.transaction.Transaction;
import uk.ac.ebi.age.transaction.TransactionException;

public class SystemACLDBDataSourceService implements DataSourceBackendService
{

 private AuthDB db;
 
 public SystemACLDBDataSourceService(AuthDB classifierDB)
 {
  db = classifierDB;
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

 private static class Req
 {
  String subjId;
  boolean isGroup;
  String profileId;
  SystemAction action;
  boolean allow;
  String error;
 }
 
 private Req getReq( DataSourceRequest dsr )
 {
  Req resp = new Req();
  
 
  Map<DSField, String> vmap = dsr.getValueMap();
  
  
  String ptype = vmap.get(ACLDSDef.pTypeField);
  
  if( ptype == null )
  {
   resp.error = ACLDSDef.pTypeField.getFieldId()+" should not be null";
   return resp;
  }

  String actId = vmap.get(ACLDSDef.pIdField);
  
  if(actId == null)
  {
   resp.error = "No action ID";
   return resp;
  }

  if( "allow".equals(ptype) || "deny".equals(ptype) )
  {

   try
   {
    resp.action = SystemAction.valueOf(actId);
   }
   catch(Exception e)
   {
    resp.error = "Invalid action: " + actId;
    return resp;
   }

   resp.allow = "allow".equals(ptype);
   
  }
  else if( "profile".equals(ptype) )
  {
   resp.profileId = actId;
  }
  else
  {
   resp.error = "Invalid permission type: " + ptype;
   return resp;
  }

  String stype = vmap.get(ACLDSDef.sTypeField);
  
  if( stype == null )
  {
   resp.error = ACLDSDef.sTypeField.getFieldId()+" should not be null";
   return resp;
  }

  resp.subjId = vmap.get(ACLDSDef.sIdField);
  
  if( "user".equals(stype) )
   resp.isGroup = false;
  else if( ! "group".equals(stype) )
  {
   resp.error = "Invalid subject type: " + stype;
   return resp;
  }
  else
   resp.isGroup =true;
  
  return resp;
 }
 
 private DataSourceResponse processDelete(DataSourceRequest dsr)
 {
  DataSourceResponse resp = new DataSourceResponse();

  Req r = getReq(dsr);

  if( r.error != null )
  {
   resp.setErrorMessage(r.error);
   return resp;
  }
  
  
  Transaction trn = db.startTransaction();

  try
  {
   if( r.isGroup )
   {
    if( r.profileId != null )
     db.removeSysProfileForGroupACR( trn, r.subjId, r.profileId );
    else
     db.removeSysPermissionForGroupACR( trn,  r.subjId, r.action, r.allow );
   }
   else
   {
    if( r.profileId != null )
     db.removeSysProfileForUserACR( trn, r.subjId, r.profileId );
    else
     db.removeSysPermissionForUserACR( trn, r.subjId, r.action, r.allow );
   }   
  
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

  Req r = getReq(dsr);

  if( r.error != null )
  {
   resp.setErrorMessage(r.error);
   return resp;
  }
  
  
  
  Transaction trn = db.startTransaction();

  try
  {
   if( r.isGroup )
   {
    if( r.profileId != null )
     db.addSysProfileForGroupACR( trn, r.subjId, r.profileId );
    else
     db.addSysActionForGroupACR( trn, r.subjId, r.action, r.allow );
   }
   else
   {
    if( r.profileId != null )
     db.addSysProfileForUserACR( trn, r.subjId, r.profileId );
    else
     db.addSysActionForUserACR( trn, r.subjId, r.action, r.allow );
   }
  
   try
   {
    db.commitTransaction(trn);
   }
   catch(TransactionException e)
   {
    resp.setErrorMessage("Transaction error: "+e.getMessage());
   }
  
  }
  catch(Exception e)
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
  final ReadLock lck = db.getReadLock();
  
  DataSourceResponse resp = new DataSourceResponse(db,lck);
  
  try
  {
   Collection<? extends ACR> acrs = db.getSysACL( lck );
   
   resp.setTotal( acrs.size() );
   resp.setSize( acrs.size() );
   resp.setIterator( new ACLMapIterator( acrs ) );

  }
  catch(AuthDBException e)
  {
   resp.setErrorMessage(e.getMessage());
  }
 
  return resp;
 }

 @Override
 public ACLDSDef getDSDefinition()
 {
  return ACLDSDef.getInstance();
 }
 
 
 
}
