package uk.ac.ebi.age.admin.server.service.auth;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import uk.ac.ebi.age.admin.server.service.ds.DataSourceBackendService;
import uk.ac.ebi.age.admin.server.service.ds.DataSourceRequest;
import uk.ac.ebi.age.admin.server.service.ds.DataSourceResponse;
import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.admin.shared.auth.GroupPartsDSDef;
import uk.ac.ebi.age.admin.shared.ds.DSField;
import uk.ac.ebi.age.authz.AuthDB;
import uk.ac.ebi.age.authz.User;
import uk.ac.ebi.age.authz.UserGroup;
import uk.ac.ebi.age.authz.exception.AuthException;

import com.pri.util.collection.MapIterator;

public class GroupPartsDBDataSourceService implements DataSourceBackendService
{
 private AuthDB db;
 
 public GroupPartsDBDataSourceService(AuthDB authDB)
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

  
  String grpId = dsr.getRequestParametersMap().get(Constants.groupIdParam);
  
  if( grpId == null )
  {
   resp.setErrorMessage(Constants.groupIdParam+" should not be null");
   return resp;
  }

  Map<DSField, String> vmap = dsr.getValueMap();
  
  String partId = vmap.get(GroupPartsDSDef.partIdField);
  
  if( partId == null )
  {
   resp.setErrorMessage("No part ID");
   return resp;
  }
  
  String type = vmap.get(GroupPartsDSDef.partTypeField);
  
  if( type == null )
  {
   resp.setErrorMessage(GroupPartsDSDef.partTypeField+" should not be null");
   return resp;
  }

  if( "user".equals(type) )
  {
   
   try
   {
    db.removeUserFromGroup( grpId, partId );
   }
   catch(AuthException e)
   {
    resp.setErrorMessage("Error");
   }

  }
  else if( "group".equals(type) )
  {
   try
   {
    db.removeGroupFromGroup( grpId, partId );
   }
   catch(AuthException e)
   {
    resp.setErrorMessage("Error");
   }

  }
  else
   resp.setErrorMessage("Invalid type");  
  return resp;
 }

 private DataSourceResponse processAdd(DataSourceRequest dsr)
 {
  DataSourceResponse resp = new DataSourceResponse();

  String grpId = dsr.getRequestParametersMap().get(Constants.groupIdParam);
  
  if( grpId == null )
  {
   resp.setErrorMessage(Constants.groupIdParam+" should not be null");
   return resp;
  }

  Map<DSField, String> vmap = dsr.getValueMap();
  
  String partId = vmap.get(GroupPartsDSDef.partIdField);
  
  if( partId == null )
  {
   resp.setErrorMessage("No part ID");
   return resp;
  }
  
  String type = vmap.get(GroupPartsDSDef.partTypeField);
  
  if( type == null )
  {
   resp.setErrorMessage(GroupPartsDSDef.partTypeField+" should not be null");
   return resp;
  }

  if( "user".equals(type) )
  {
   
   try
   {
    db.addUserToGroup( grpId, partId );
   }
   catch(AuthException e)
   {
    resp.setErrorMessage("Error: "+e.getMessage());
   }

  }
  else if( "group".equals(type) )
  {
   try
   {
    db.addGroupToGroup( grpId, partId );
   }
   catch(AuthException e)
   {
    resp.setErrorMessage("Error");
   }

  }
  else
   resp.setErrorMessage("Invalid type");
  
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
  
  String groupId = dsr.getRequestParametersMap().get(Constants.groupIdParam);
  
  if( groupId == null )
  {
   resp.setErrorMessage("No group ID");
   return resp;
  }
  
  
  
  try
  {
   Collection< ? extends User> ures = db.getUsersOfGroup( groupId );
   Collection< ? extends UserGroup> gres = db.getGroupsOfGroup( groupId );

   resp.setTotal( ures.size() + gres.size() );
   resp.setSize(ures.size() + gres.size() );
   resp.setIterator( new PartsMapIterator( ures, gres) );
  }
  catch(AuthException e)
  {
   resp.setErrorMessage("Error");
  }
 
  return resp;
 }

 @Override
 public GroupPartsDSDef getDSDefinition()
 {
  return GroupPartsDSDef.getInstance();
 }
 
 class PartsMapIterator implements MapIterator<DSField, String>
 {
  private Iterator<? extends UserGroup> grpIter;
  private Iterator<? extends User> userIter;
  private User cUsr;
  private UserGroup cGrp;
  
  PartsMapIterator( Collection< ? extends User> ures, Collection<? extends UserGroup> gres )
  {
   userIter = ures.iterator();
   grpIter  = gres.iterator();
  }

  @Override
  public boolean next()
  {
   if( userIter.hasNext() )
   {
    cUsr = userIter.next();
    return true;
   }
   
   cUsr = null;
   
   if( ! grpIter.hasNext() )
    return false;

   cGrp = grpIter.next();
   
   return true;
  }

  @Override
  public String get(DSField key)
  {
   if( cUsr != null )
   {
    if( key.equals(GroupPartsDSDef.keyField) )
     return "user"+cUsr.getId();

    if( key.equals(GroupPartsDSDef.partTypeField) )
     return "user";

    if( key.equals(GroupPartsDSDef.partIdField) )
     return cUsr.getId();
    
    if( key.equals(GroupPartsDSDef.partDescField) )
     return cUsr.getName();
   }
   else
   {
    if( key.equals(GroupPartsDSDef.keyField) )
     return "group"+cGrp.getId();

    if( key.equals(GroupPartsDSDef.partTypeField) )
     return "group";

    if( key.equals(GroupPartsDSDef.partIdField) )
     return cGrp.getId();
    
    if( key.equals(GroupPartsDSDef.partDescField) )
     return cGrp.getDescription();
   }
   

   return null;
  }
  
 }
}