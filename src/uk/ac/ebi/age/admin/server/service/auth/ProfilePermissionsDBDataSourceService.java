package uk.ac.ebi.age.admin.server.service.auth;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import uk.ac.ebi.age.admin.server.service.ds.DataSourceBackendService;
import uk.ac.ebi.age.admin.server.service.ds.DataSourceRequest;
import uk.ac.ebi.age.admin.server.service.ds.DataSourceResponse;
import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.admin.shared.auth.ProfilePermDSDef;
import uk.ac.ebi.age.admin.shared.ds.DSField;
import uk.ac.ebi.age.authz.AuthDB;
import uk.ac.ebi.age.authz.Permission;
import uk.ac.ebi.age.authz.PermissionProfile;
import uk.ac.ebi.age.authz.exception.AuthException;
import uk.ac.ebi.age.ext.authz.SystemAction;
import uk.ac.ebi.age.transaction.ReadLock;
import uk.ac.ebi.age.transaction.Transaction;
import uk.ac.ebi.age.transaction.TransactionException;

import com.pri.util.collection.MapIterator;

public class ProfilePermissionsDBDataSourceService implements DataSourceBackendService
{
 private AuthDB db;
 
 public ProfilePermissionsDBDataSourceService(AuthDB authDB)
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

  String profId = dsr.getRequestParametersMap().get(Constants.profileIdParam);
  
  if( profId == null )
  {
   resp.setErrorMessage(Constants.profileIdParam+" should not be null");
   return resp;
  }

  Map<DSField, String> vmap = dsr.getValueMap();
  
  
  String type = vmap.get(ProfilePermDSDef.typeField);
  
  if( type == null )
  {
   resp.setErrorMessage(ProfilePermDSDef.typeField.getFieldId()+" should not be null");
   return resp;
  }

  if( "allow".equals(type) || "deny".equals(type) )
  {

   String actId = vmap.get(ProfilePermDSDef.idField);

   if(actId == null)
   {
    resp.setErrorMessage("No action ID");
    return resp;
   }

   SystemAction actn = null;
   try
   {
    actn = SystemAction.valueOf(actId);
   }
   catch(Exception e)
   {
    resp.setErrorMessage("Invalid action: " + actId);
   }

   
   Transaction trn = db.startTransaction();

   try
   {
    db.removePermissionFromProfile(trn, profId, actn, "allow".equals(type));
    
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
   
  }
  else if( "profile".equals(type) )
  {
   Transaction trn = db.startTransaction();

   try
   {
    db.removeProfileFromProfile(trn, profId, vmap.get(ProfilePermDSDef.idField) );
    
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

  }
  else
  {
   resp.setErrorMessage("Invalid permission type: " + type);
  }

  return resp;
 }

 private DataSourceResponse processAdd(DataSourceRequest dsr)
 {
  DataSourceResponse resp = new DataSourceResponse();

  String profId = dsr.getRequestParametersMap().get(Constants.profileIdParam);
  
  if( profId == null )
  {
   resp.setErrorMessage(Constants.profileIdParam+" should not be null");
   return resp;
  }

  Map<DSField, String> vmap = dsr.getValueMap();
  
  
  String type = vmap.get(ProfilePermDSDef.typeField);
  
  if( type == null )
  {
   resp.setErrorMessage(ProfilePermDSDef.typeField.getFieldId()+" should not be null");
   return resp;
  }

  if( "allow".equals(type) || "deny".equals(type) )
  {

   String actId = vmap.get(ProfilePermDSDef.idField);

   if(actId == null)
   {
    resp.setErrorMessage("No action ID");
    return resp;
   }

   SystemAction actn = null;
   try
   {
    actn = SystemAction.valueOf(actId);
   }
   catch(Exception e)
   {
    resp.setErrorMessage("Invalid action: " + actId);
   }

   
   Transaction trn = db.startTransaction();

   try
   {
    db.addPermissionToProfile( trn, profId, actn, "allow".equals(type));
    
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
  }
  else if( "profile".equals(type) )
  {
   
   Transaction trn = db.startTransaction();

   try
   {
    db.addProfileToProfile( trn, profId, vmap.get(ProfilePermDSDef.idField) );
    
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

  }
  else
  {
   resp.setErrorMessage("Invalid permission type: " + type);
   return resp;
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
  ReadLock lck = db.getReadLock();
  DataSourceResponse resp = new DataSourceResponse( lck );
  
  String profId = dsr.getRequestParametersMap().get(Constants.profileIdParam);
  
  if( profId == null )
  {
   resp.setErrorMessage("No profile ID");
   return resp;
  }
  
  try
  {
   Collection< ? extends Permission> perms = db.getPermissionsOfProfile( lck, profId );

   Collection< ? extends PermissionProfile> prof = db.getProfilesOfProfile( lck, profId );

   resp.setTotal( perms.size()+prof.size() );
   resp.setSize( perms.size()+prof.size() );
   resp.setIterator( new PermMapIterator( perms, prof ) );

  }
  catch(AuthException e)
  {
   resp.setErrorMessage(e.getMessage());
  }
 
  return resp;
 }

 @Override
 public ProfilePermDSDef getDSDefinition()
 {
  return ProfilePermDSDef.getInstance();
 }
 
 class PermMapIterator implements MapIterator<DSField, String>
 {
  private Iterator<? extends PermissionProfile> profIter;
  private Iterator<? extends Permission> permIter;
  private Permission cPerm;
  private PermissionProfile cProf;
  
  PermMapIterator( Collection< ? extends Permission> ures, Collection<? extends PermissionProfile> gres )
  {
   permIter = ures.iterator();
   profIter  = gres.iterator();
  }

  @Override
  public boolean next()
  {
   if( permIter.hasNext() )
   {
    cPerm = permIter.next();
    return true;
   }
   
   cPerm = null;
   
   if( ! profIter.hasNext() )
    return false;

   cProf = profIter.next();
   
   return true;
  }

  @Override
  public String get(DSField key)
  {
   if( cPerm != null )
   {
    if( key.equals(ProfilePermDSDef.keyField) )
     return cPerm.getAction().name()+cPerm.isAllow();

    if( key.equals(ProfilePermDSDef.idField) )
     return cPerm.getAction().name();
    
    if( key.equals(ProfilePermDSDef.descField) )
     return cPerm.getDescription();

    if( key.equals(ProfilePermDSDef.typeField) )
     return cPerm.isAllow()?"allow":"deny";
   }
   else
   {
    if( key.equals(ProfilePermDSDef.keyField) )
     return cProf.getId()+"prof";

    if( key.equals(ProfilePermDSDef.idField) )
     return cProf.getId();
    
    if( key.equals(ProfilePermDSDef.descField) )
     return cProf.getDescription();

    if( key.equals(ProfilePermDSDef.typeField) )
     return "profile";
   }
   

   return null;
  }
  
 }
 
}
