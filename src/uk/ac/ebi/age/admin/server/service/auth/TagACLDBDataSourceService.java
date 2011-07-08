package uk.ac.ebi.age.admin.server.service.auth;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import uk.ac.ebi.age.admin.server.service.ds.DataSourceBackendService;
import uk.ac.ebi.age.admin.server.service.ds.DataSourceRequest;
import uk.ac.ebi.age.admin.server.service.ds.DataSourceResponse;
import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.admin.shared.cassif.TagACLDSDef;
import uk.ac.ebi.age.admin.shared.ds.DSField;
import uk.ac.ebi.age.authz.ACR;
import uk.ac.ebi.age.authz.AuthDB;
import uk.ac.ebi.age.authz.Permission;
import uk.ac.ebi.age.authz.PermissionProfile;
import uk.ac.ebi.age.authz.PermissionUnit;
import uk.ac.ebi.age.authz.Subject;
import uk.ac.ebi.age.authz.User;
import uk.ac.ebi.age.authz.UserGroup;
import uk.ac.ebi.age.authz.exception.TagException;
import uk.ac.ebi.age.ext.authz.SystemAction;
import uk.ac.ebi.age.transaction.ReadLock;
import uk.ac.ebi.age.transaction.Transaction;
import uk.ac.ebi.age.transaction.TransactionException;

import com.pri.util.collection.MapIterator;

public class TagACLDBDataSourceService implements DataSourceBackendService
{

 private AuthDB db;
 
 public TagACLDBDataSourceService(AuthDB classifierDB)
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
  String classifId;
  String tagId;
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
  
  resp.tagId = dsr.getRequestParametersMap().get(Constants.tagIdParam);
  
  if( resp.tagId == null )
  {
   resp.error = Constants.tagIdParam+" should not be null";
   return resp;
  }

  resp.classifId = dsr.getRequestParametersMap().get(Constants.classifIdParam);
  
  if( resp.classifId == null )
  {
   resp.error = Constants.classifIdParam+" should not be null";
   return resp;
  }

  
  Map<DSField, String> vmap = dsr.getValueMap();
  
  
  String ptype = vmap.get(TagACLDSDef.pTypeField);
  
  if( ptype == null )
  {
   resp.error = TagACLDSDef.pTypeField.getFieldId()+" should not be null";
   return resp;
  }

  String actId = vmap.get(TagACLDSDef.pIdField);
  
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

  String stype = vmap.get(TagACLDSDef.sTypeField);
  
  if( stype == null )
  {
   resp.error = TagACLDSDef.sTypeField.getFieldId()+" should not be null";
   return resp;
  }

  resp.subjId = vmap.get(TagACLDSDef.sIdField);
  
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
     db.removeProfileForGroupACR( trn, r.classifId, r.tagId, r.subjId, r.profileId );
    else
     db.removePermissionForGroupACR( trn, r.classifId, r.tagId, r.subjId, r.action, r.allow );
   }
   else
   {
    if( r.profileId != null )
     db.removeProfileForUserACR( trn, r.classifId, r.tagId, r.subjId, r.profileId );
    else
     db.removePermissionForUserACR( trn, r.classifId, r.tagId, r.subjId, r.action, r.allow );
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
  catch(TagException e)
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
     db.addProfileForGroupACR( trn, r.classifId, r.tagId, r.subjId, r.profileId );
    else
     db.addActionForGroupACR( trn, r.classifId, r.tagId, r.subjId, r.action, r.allow );
   }
   else
   {
    if( r.profileId != null )
     db.addProfileForUserACR( trn, r.classifId, r.tagId, r.subjId, r.profileId );
    else
     db.addActionForUserACR( trn, r.classifId, r.tagId, r.subjId, r.action, r.allow );
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
  ReadLock rl = db.getReadLock();
  DataSourceResponse resp = new DataSourceResponse(rl);
  
  String tagId = dsr.getRequestParametersMap().get(Constants.tagIdParam);
  
  if( tagId == null )
  {
   resp.setErrorMessage("No tag ID");
   return resp;
  }
  
  String classifId = dsr.getRequestParametersMap().get(Constants.classifIdParam);
  
  if( classifId == null )
  {
   resp.setErrorMessage(Constants.classifIdParam+" should not be null");
   return resp;
  }

  
  try
  {
   Collection<? extends ACR> acrs = db.getACL( rl, classifId, tagId );
   
   resp.setTotal( acrs.size() );
   resp.setSize( acrs.size() );
   resp.setIterator( new ACLMapIterator( acrs ) );

  }
  catch(TagException e)
  {
   resp.setErrorMessage(e.getMessage());
  }
 
  return resp;
 }

 @Override
 public TagACLDSDef getDSDefinition()
 {
  return TagACLDSDef.getInstance();
 }
 
 
 class ACLMapIterator implements MapIterator<DSField, String>
 {
  private Iterator<? extends ACR> acrIter;
  private ACR cACR;
  
  ACLMapIterator( Collection<? extends ACR> lst )
  {
   acrIter = lst.iterator();
  }

  @Override
  public boolean next()
  {
   if( ! acrIter.hasNext() )
    return false;

   cACR = acrIter.next();
   
   return true;
  }

  @Override
  public String get(DSField key)
  {
   if( key.equals(TagACLDSDef.keyField) )
   {
    String id="";
    
    Subject s = cACR.getSubject();
    
    if( s instanceof User )
     id+= "u"+((User)s).getId();
    else
     id+= "g"+((UserGroup)s).getId();
    
    PermissionUnit pu = cACR.getPermissionUnit();
    
    if( pu instanceof Permission )
    {
     Permission p = (Permission)pu;
     id+= "^"+(p.isAllow()?"A":"D")+p.getAction().name();
    }
    else
     id+="^P"+((PermissionProfile)pu).getId();
    
    return id;
   }
   
   if( key.equals(TagACLDSDef.pIdField) )
   {
    PermissionUnit pu = cACR.getPermissionUnit();
    
    if( pu instanceof Permission )
     return ((Permission)pu).getAction().name();
    else
     return ((PermissionProfile)pu).getId();
   }
   
   if( key.equals(TagACLDSDef.pTypeField ) )
   {
    PermissionUnit pu = cACR.getPermissionUnit();
    
    if( pu instanceof Permission )
     return ((Permission)pu).isAllow()?"allow":"deny";
    else
     return "profile";
   }
   
   if( key.equals(TagACLDSDef.sIdField) )
   {
    Subject s = cACR.getSubject();
    
    if( s instanceof User )
     return ((User)s).getId();
    else
     return ((UserGroup)s).getId();
   }
   
   if( key.equals(TagACLDSDef.sTypeField) )
   {
    Subject s = cACR.getSubject();
    
    if( s instanceof User )
     return "user";
    else
     return "group";
   }
   
   return null;
  }
  
 }
 
}
