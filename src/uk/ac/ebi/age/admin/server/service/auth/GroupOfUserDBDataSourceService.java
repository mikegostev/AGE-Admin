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
import uk.ac.ebi.age.authz.AuthException;
import uk.ac.ebi.age.authz.UserGroup;

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

  try
  {
   db.removeUserFromGroup( grpId, userId );
  }
  catch(AuthException e)
  {
   resp.setErrorMessage("Group with ID '"+grpId+"' doesn't exist");
  }
  
  return resp;
 }

 private DataSourceResponse processAdd(DataSourceRequest dsr)
 {
  DataSourceResponse resp = new DataSourceResponse();

  Map<DSField, String> vmap = dsr.getValueMap();
  
  String grpId = vmap.get(GroupDSDef.grpIdField);
  String grpDesc = vmap.get(GroupDSDef.grpDescField);
  
  if( grpId == null )
  {
   resp.setErrorMessage(GroupDSDef.grpIdField.getFieldTitle()+" should not be null");
   return resp;
  }
  
  
  try
  {
   db.addGroup( grpId, grpDesc );
  }
  catch(AuthException e)
  {
   resp.setErrorMessage("Group with ID '"+grpId+"' exists");
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
  DataSourceResponse resp = new DataSourceResponse();
  
  String userId = dsr.getRequestParametersMap().get(Constants.userIdParam);
  
  if( userId == null )
  {
   resp.setErrorMessage("No user ID");
   return resp;
  }
  
  
  Collection< ? extends UserGroup> res;
  try
  {
   res = db.getGroupsOfUser( userId );

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
