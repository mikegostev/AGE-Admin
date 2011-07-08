package uk.ac.ebi.age.admin.server.service.auth;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import uk.ac.ebi.age.admin.server.service.ds.DataSourceBackendService;
import uk.ac.ebi.age.admin.server.service.ds.DataSourceRequest;
import uk.ac.ebi.age.admin.server.service.ds.DataSourceResponse;
import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.admin.shared.cassif.TagsDSDef;
import uk.ac.ebi.age.admin.shared.ds.DSField;
import uk.ac.ebi.age.authz.AuthDB;
import uk.ac.ebi.age.authz.Tag;
import uk.ac.ebi.age.authz.exception.TagException;
import uk.ac.ebi.age.transaction.ReadLock;
import uk.ac.ebi.age.transaction.Transaction;
import uk.ac.ebi.age.transaction.TransactionException;

import com.pri.util.collection.MapIterator;

public class TagDBDataSourceService implements DataSourceBackendService
{
 private AuthDB db;
 
 public TagDBDataSourceService(AuthDB classDB)
 {
  db = classDB;
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

  String clsId = dsr.getRequestParametersMap().get(Constants.classifIdParam);

  if(clsId == null)
  {
   resp.setErrorMessage(Constants.classifIdParam + " should not be null");
   return resp;
  }

  Map<DSField, String> vmap = dsr.getValueMap();

  String tagId = vmap.get(TagsDSDef.idField);

  if(tagId == null)
  {
   resp.setErrorMessage("No tag ID");
   return resp;
  }
  
  
  
  Transaction trn = db.startTransaction();

  try
  {
   db.removeTagFromClassifier(trn, clsId, tagId);
   
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

  String clsId = dsr.getRequestParametersMap().get(Constants.classifIdParam);
  
  if( clsId == null )
  {
   resp.setErrorMessage(Constants.classifIdParam+" should not be null");
   return resp;
  }

  Map<DSField, String> vmap = dsr.getValueMap();
  
  String tagId = vmap.get(TagsDSDef.idField);
  String parentTagId = vmap.get(TagsDSDef.parentField);
  String desc = vmap.get(TagsDSDef.descField);
  
  
  Transaction trn = db.startTransaction();

  try
  {
   db.addTagToClassifier(trn, clsId, tagId, desc, Constants.rootTagId.equals(parentTagId)?null:parentTagId);
   
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

 private DataSourceResponse processUpdate(DataSourceRequest dsr)
 {
  DataSourceResponse resp = new DataSourceResponse();

  String clsId = dsr.getRequestParametersMap().get(Constants.classifIdParam);
  
  if( clsId == null )
  {
   resp.setErrorMessage(Constants.classifIdParam+" should not be null");
   return resp;
  }

  Map<DSField, String> vmap = dsr.getValueMap();
  
  String tagId = vmap.get(TagsDSDef.idField);
  String parentTagId = vmap.get(TagsDSDef.parentField);
  String desc = vmap.get(TagsDSDef.descField);
  
  Transaction trn = db.startTransaction();

  try
  {
   db.updateTag(trn, clsId, tagId, desc, Constants.rootTagId.equals(parentTagId)?null:parentTagId);
   
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

 private DataSourceResponse processFetch(DataSourceRequest dsr)
 {
  ReadLock lck = db.getReadLock();
  DataSourceResponse resp = new DataSourceResponse( lck );
  
  String clsId = dsr.getRequestParametersMap().get(Constants.classifIdParam);
  
  if( clsId == null )
  {
   resp.setErrorMessage(Constants.classifIdParam+" should not be null");
   return resp;
  }
  
  Map<DSField, String> vmap = dsr.getValueMap();
//  String parentTagId = vmap.get(TagsDSDef.parentField); parentTagId.equals("_root")?null:parentTagId
  
  try
  {
   Collection< ? extends Tag> tags = db.getTagsOfClassifier( lck, clsId );

   resp.setTotal( tags.size() );
   resp.setSize( tags.size() );
   resp.setIterator( new TagMapIterator( tags ) );

  }
  catch(TagException e)
  {
   resp.setErrorMessage(e.getMessage());
  }
 
  return resp;
 }

 @Override
 public TagsDSDef getDSDefinition()
 {
  return TagsDSDef.getInstance();
 }
 
 class TagMapIterator implements MapIterator<DSField, String>
 {
  private Iterator<? extends Tag> tagIter;
  private Tag cTag;
  
  TagMapIterator( Collection< ? extends Tag> ures )
  {
   tagIter = ures.iterator();
  }

  @Override
  public boolean next()
  {
   if( ! tagIter.hasNext() )
    return false;

   cTag = tagIter.next();
   
   return true;
   
  }

  @Override
  public String get(DSField key)
  {
   if( key.equals(TagsDSDef.idField) )
    return cTag.getId();
   
   if( key.equals(TagsDSDef.descField) )
    return cTag.getDescription();

   if( key.equals(TagsDSDef.parentField) )
   {
    Tag prnt = cTag.getParent();
    
    if( prnt == null )
     return Constants.rootTagId;
    
    return prnt.getId();
   }
   
   return null;
   
  }  
 }
 
}
