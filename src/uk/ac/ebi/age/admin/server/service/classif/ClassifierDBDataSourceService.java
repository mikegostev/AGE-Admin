package uk.ac.ebi.age.admin.server.service.classif;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import uk.ac.ebi.age.admin.server.service.ds.DataSourceBackendService;
import uk.ac.ebi.age.admin.server.service.ds.DataSourceRequest;
import uk.ac.ebi.age.admin.server.service.ds.DataSourceResponse;
import uk.ac.ebi.age.admin.shared.cassif.ClassifierDSDef;
import uk.ac.ebi.age.admin.shared.ds.DSField;
import uk.ac.ebi.age.authz.Classifier;
import uk.ac.ebi.age.authz.exception.TagException;
import uk.ac.ebi.age.classif.ClassifierDB;

import com.pri.util.collection.ListFragment;
import com.pri.util.collection.MapIterator;

public class ClassifierDBDataSourceService implements DataSourceBackendService
{
 private ClassifierDB db;
 
 public ClassifierDBDataSourceService(ClassifierDB authDB)
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
  
  
  try
  {
   db.deleteClassifier( csfId );
  }
  catch(TagException e)
  {
   resp.setErrorMessage("Classifier with ID '"+csfId+"' doesn't exist");
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
  
  
  try
  {
   db.addClassifier( csfId, csfDesc );
  }
  catch(TagException e)
  {
   resp.setErrorMessage("Classifier with ID '"+csfId+"' exists");
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
  
  try
  {
   db.updateClassifier( csfId, csfDesc );
  }
  catch(TagException e)
  {
   resp.setErrorMessage(e.getMessage());
  }
  
  return resp;
 }

 private DataSourceResponse processFetch(DataSourceRequest dsr)
 {
  DataSourceResponse resp = new DataSourceResponse();
  
  Map<DSField, String> vmap = dsr.getValueMap();
  
  if( vmap == null || vmap.size() == 0 )
  {
   List<? extends Classifier> res=db.getClassifiers( dsr.getBegin(), dsr.getEnd() );
   
   resp.setTotal( db.getClassifiersTotal() );
   resp.setSize(res.size());
   resp.setIterator( new ClassifierMapIterator(res) );
  }
  else
  {
   ListFragment<Classifier> res=db.getClassifiers( vmap.get(ClassifierDSDef.idField), vmap.get(ClassifierDSDef.descField), dsr.getBegin(), dsr.getEnd() );
  
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
