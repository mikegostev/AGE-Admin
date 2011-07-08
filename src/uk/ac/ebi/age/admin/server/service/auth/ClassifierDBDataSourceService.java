package uk.ac.ebi.age.admin.server.service.auth;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import uk.ac.ebi.age.admin.server.service.ds.DataSourceBackendService;
import uk.ac.ebi.age.admin.server.service.ds.DataSourceRequest;
import uk.ac.ebi.age.admin.server.service.ds.DataSourceResponse;
import uk.ac.ebi.age.admin.shared.cassif.ClassifierDSDef;
import uk.ac.ebi.age.admin.shared.ds.DSField;
import uk.ac.ebi.age.authz.AuthDB;
import uk.ac.ebi.age.authz.Classifier;
import uk.ac.ebi.age.authz.exception.TagException;
import uk.ac.ebi.age.transaction.ReadLock;
import uk.ac.ebi.age.transaction.Transaction;
import uk.ac.ebi.age.transaction.TransactionException;

import com.pri.util.collection.ListFragment;
import com.pri.util.collection.MapIterator;

public class ClassifierDBDataSourceService implements DataSourceBackendService
{
 private AuthDB db;
 
 public ClassifierDBDataSourceService(AuthDB authDB)
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
  
  String csfId = vmap.get(ClassifierDSDef.idField);
  
  if( csfId == null )
  {
   resp.setErrorMessage(ClassifierDSDef.idField.getFieldTitle()+" should not be null");
   return resp;
  }
  
  
  Transaction trn = db.startTransaction();

  try
  {
   db.deleteClassifier(trn, csfId );
   
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
    resp.setErrorMessage("Classifier with ID '"+csfId+"' doesn't exist");
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
  
  String csfId = vmap.get(ClassifierDSDef.idField);
  String csfDesc = vmap.get(ClassifierDSDef.descField);
  
  if( csfId == null )
  {
   resp.setErrorMessage(ClassifierDSDef.idField.getFieldTitle()+" should not be null");
   return resp;
  }
  
  
  Transaction trn = db.startTransaction();
  
  try
  {
   db.addClassifier( trn, csfId, csfDesc );   

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
    resp.setErrorMessage("Classifier with ID '"+csfId+"' exists");
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
  
  String csfId = vmap.get(ClassifierDSDef.idField);
  String csfDesc = vmap.get(ClassifierDSDef.descField);
  
  if( csfId == null )
  {
   resp.setErrorMessage(ClassifierDSDef.idField.getFieldTitle()+" should not be null");
   return resp;
  }
  
  
  Transaction trn = db.startTransaction();
  
  try
  {
   db.updateClassifier( trn, csfId, csfDesc );

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
  
  Map<DSField, String> vmap = dsr.getValueMap();
  
  if( vmap == null || vmap.size() == 0 )
  {
   List<? extends Classifier> res=db.getClassifiers( lck, dsr.getBegin(), dsr.getEnd() );
   
   resp.setTotal( db.getClassifiersTotal( lck ) );
   resp.setSize(res.size());
   resp.setIterator( new ClassifierMapIterator(res) );
  }
  else
  {
   ListFragment<Classifier> res=db.getClassifiers( lck, vmap.get(ClassifierDSDef.idField), vmap.get(ClassifierDSDef.descField), dsr.getBegin(), dsr.getEnd() );
  
   resp.setTotal(res.getTotalLength());
   resp.setSize(res.getList().size());
   resp.setIterator( new ClassifierMapIterator(res.getList()) );
  }
  
  return resp;
 }

 @Override
 public ClassifierDSDef getDSDefinition()
 {
  return ClassifierDSDef.getInstance();
 }
 
 class ClassifierMapIterator implements MapIterator<DSField, String>
 {
  private Iterator<? extends Classifier> grpIter;
  private Classifier cGrp;
  
  ClassifierMapIterator( List<? extends Classifier> lst )
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
   if( key.equals(ClassifierDSDef.idField) )
    return cGrp.getId();
   
   if( key.equals(ClassifierDSDef.descField) )
    return cGrp.getDescription();

   return null;
  }
  
 }
}
