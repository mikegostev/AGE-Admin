package uk.ac.ebi.age.admin.server.service.ontology;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import uk.ac.ebi.age.admin.server.service.ds.DataSourceBackendService;
import uk.ac.ebi.age.admin.server.service.ds.DataSourceRequest;
import uk.ac.ebi.age.admin.server.service.ds.DataSourceResponse;
import uk.ac.ebi.age.admin.shared.auth.UserDSDef;
import uk.ac.ebi.age.admin.shared.ds.DSField;
import uk.ac.ebi.age.admin.shared.ontology.OntologyDSDef;
import uk.ac.ebi.age.ontology.Ontology;
import uk.ac.ebi.age.ontology.OntologyDB;
import uk.ac.ebi.age.transaction.ReadLock;
import uk.ac.ebi.age.transaction.Transaction;
import uk.ac.ebi.age.transaction.TransactionException;

import com.pri.util.collection.ListFragment;
import com.pri.util.collection.MapIterator;

public class OntologyDBDataSourceService implements DataSourceBackendService
{
 private final OntologyDB db;
 
 public OntologyDBDataSourceService(OntologyDB oDB)
 {
  db = oDB;
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
  
  String ontoId = vmap.get(OntologyDSDef.ontologyNameField);
  
  if( ontoId == null )
  {
   resp.setErrorMessage(OntologyDSDef.ontologyNameField.getFieldTitle()+" should not be null");
   return resp;
  }
  
  
  Transaction trn = db.startTransaction();

  try
  {
   db.deleteOntology( trn, ontoId );
   
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

 private DataSourceResponse processAdd(DataSourceRequest dsr)
 {
  DataSourceResponse resp = new DataSourceResponse();

  Map<DSField, String> vmap = dsr.getValueMap();
  
  String ontoId = vmap.get(OntologyDSDef.ontologyNameField);
  String ontoDesc = vmap.get(OntologyDSDef.ontologyDescriptionField);
  String ontoURL = vmap.get(OntologyDSDef.ontologyURLField);
  
  if( ontoId == null )
  {
   resp.setErrorMessage(UserDSDef.userIdField.getFieldTitle()+" should not be null");
   return resp;
  }
  
  
  
  Transaction trn = db.startTransaction();

  try
  {
   db.addOntology( trn, ontoId, ontoDesc, ontoURL );
   
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

  Map<DSField, String> vmap = dsr.getValueMap();
  
  String ontoId = vmap.get(OntologyDSDef.ontologyNameField);
  String ontoDesc = vmap.get(OntologyDSDef.ontologyDescriptionField);
  String ontoURL = vmap.get(OntologyDSDef.ontologyURLField);
  
  if( ontoId == null )
  {
   resp.setErrorMessage(OntologyDSDef.ontologyNameField.getFieldTitle()+" should not be null");
   return resp;
  }
 
  
  Transaction trn = db.startTransaction();

  try
  {

    db.updateOntology( trn,  ontoId, ontoDesc, ontoURL );
   
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

 private DataSourceResponse processFetch(DataSourceRequest dsr)
 {
  final ReadLock lck = db.getReadLock();
  
  DataSourceResponse resp = new DataSourceResponse(db,lck);
  
  Map<DSField, String> vmap = dsr.getValueMap();
  
  if( vmap == null || vmap.size() == 0 )
  {
   List<? extends Ontology> res=db.getOntologies(lck, dsr.getBegin(), dsr.getEnd() );
   
   resp.setTotal( db.getOntologiesTotal(lck) );
   resp.setSize(res.size());
   resp.setIterator( new OntologyMapIterator(res) );
  }
  else
  {
   ListFragment<Ontology> res=db.getOntologies(lck, vmap.get(OntologyDSDef.ontologyNameField), vmap.get(OntologyDSDef.ontologyDescriptionField), dsr.getBegin(), dsr.getEnd() );
  
   resp.setTotal(res.getTotalLength());
   resp.setSize(res.getList().size());
   resp.setIterator( new OntologyMapIterator(res.getList()) );
  }
  
  return resp;
 }

 @Override
 public OntologyDSDef getDSDefinition()
 {
  return OntologyDSDef.getInstance();
 }
 
 class OntologyMapIterator implements MapIterator<DSField, String>
 {
  private final Iterator<? extends Ontology> ontoIter;
  private Ontology cOnto;
  
  OntologyMapIterator( List<? extends Ontology> lst )
  {
   ontoIter = lst.iterator();
  }

  @Override
  public boolean next()
  {
   if( ! ontoIter.hasNext() )
    return false;

   cOnto = ontoIter.next();
   
   return true;
  }

  @Override
  public String get(DSField key)
  {
   if( key.equals(OntologyDSDef.ontologyNameField) )
    return cOnto.getName();
   
   if( key.equals(OntologyDSDef.ontologyDescriptionField) )
    return cOnto.getDescription();

   return null;
  }
  
 }
}
