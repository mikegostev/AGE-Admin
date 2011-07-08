package uk.ac.ebi.age.admin.server.service.auth;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import uk.ac.ebi.age.admin.server.service.ds.DataSourceBackendService;
import uk.ac.ebi.age.admin.server.service.ds.DataSourceRequest;
import uk.ac.ebi.age.admin.server.service.ds.DataSourceResponse;
import uk.ac.ebi.age.admin.shared.auth.ProfileDSDef;
import uk.ac.ebi.age.admin.shared.ds.DSField;
import uk.ac.ebi.age.authz.AuthDB;
import uk.ac.ebi.age.authz.PermissionProfile;
import uk.ac.ebi.age.authz.exception.AuthException;
import uk.ac.ebi.age.transaction.ReadLock;
import uk.ac.ebi.age.transaction.Transaction;
import uk.ac.ebi.age.transaction.TransactionException;

import com.pri.util.collection.ListFragment;
import com.pri.util.collection.MapIterator;

public class ProfileDBDataSourceService implements DataSourceBackendService
{
 private AuthDB db;
 
 public ProfileDBDataSourceService(AuthDB authDB)
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
  
  String profId = vmap.get(ProfileDSDef.profIdField);
  
  if( profId == null )
  {
   resp.setErrorMessage(ProfileDSDef.profIdField.getFieldTitle()+" should not be null");
   return resp;
  }
  
  Transaction trn = db.startTransaction();

  try
  {
   db.deleteProfile( trn, profId );
   
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
    resp.setErrorMessage("Profile with ID '"+profId+"' doesn't exist");
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
  
  String profId = vmap.get(ProfileDSDef.profIdField);
  String profDesc = vmap.get(ProfileDSDef.profDescField);
  
  if( profId == null )
  {
   resp.setErrorMessage(ProfileDSDef.profIdField.getFieldTitle()+" should not be null");
   return resp;
  }
  
  
  Transaction trn = db.startTransaction();

  try
  {
   db.addProfile( trn, profId, profDesc );
   
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
    resp.setErrorMessage("Profile with ID '"+profId+"' exists");
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
  
  String profId = vmap.get(ProfileDSDef.profIdField);
  String profDesc = vmap.get(ProfileDSDef.profDescField);
  
  if( profId == null )
  {
   resp.setErrorMessage(ProfileDSDef.profIdField.getFieldTitle()+" should not be null");
   return resp;
  }
  
  
  Transaction trn = db.startTransaction();

  try
  {
   db.updateProfile( trn, profId, profDesc );
   
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

 private DataSourceResponse processFetch(DataSourceRequest dsr)
 {
  ReadLock lck = db.getReadLock();
  DataSourceResponse resp = new DataSourceResponse( lck );
  
  Map<DSField, String> vmap = dsr.getValueMap();
  
  if( vmap == null || vmap.size() == 0 )
  {
   List<? extends PermissionProfile> res=db.getProfiles(lck, dsr.getBegin(), dsr.getEnd() );
   
   resp.setTotal( db.getProfilesTotal(lck) );
   resp.setSize(res.size());
   resp.setIterator( new ProfileMapIterator(res) );
  }
  else
  {
   ListFragment<PermissionProfile> res=db.getProfiles(lck, vmap.get(ProfileDSDef.profIdField), vmap.get(ProfileDSDef.profDescField), dsr.getBegin(), dsr.getEnd() );
  
   resp.setTotal(res.getTotalLength());
   resp.setSize(res.getList().size());
   resp.setIterator( new ProfileMapIterator(res.getList()) );
  }
  
  return resp;
 }

 @Override
 public ProfileDSDef getDSDefinition()
 {
  return ProfileDSDef.getInstance();
 }
 
 class ProfileMapIterator implements MapIterator<DSField, String>
 {
  private Iterator<? extends PermissionProfile> profIter;
  private PermissionProfile cProf;
  
  ProfileMapIterator( List<? extends PermissionProfile> lst )
  {
   profIter = lst.iterator();
  }

  @Override
  public boolean next()
  {
   if( ! profIter.hasNext() )
    return false;

   cProf = profIter.next();
   
   return true;
  }

  @Override
  public String get(DSField key)
  {
   if( key.equals(ProfileDSDef.profIdField) )
    return cProf.getId();
   
   if( key.equals(ProfileDSDef.profDescField) )
    return cProf.getDescription();

   return null;
  }
  
 }
}
