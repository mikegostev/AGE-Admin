package uk.ac.ebi.age.admin.server.service.auth;

import java.util.Collection;
import java.util.Iterator;

import uk.ac.ebi.age.admin.shared.cassif.ACLDSDef;
import uk.ac.ebi.age.admin.shared.ds.DSField;
import uk.ac.ebi.age.authz.ACR;
import uk.ac.ebi.age.authz.Permission;
import uk.ac.ebi.age.authz.PermissionProfile;
import uk.ac.ebi.age.authz.PermissionUnit;
import uk.ac.ebi.age.authz.Subject;
import uk.ac.ebi.age.authz.User;
import uk.ac.ebi.age.authz.UserGroup;

import com.pri.util.collection.MapIterator;

public class ACLMapIterator implements MapIterator<DSField, String>
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
  if( key.equals(ACLDSDef.keyField) )
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
  
  if( key.equals(ACLDSDef.pIdField) )
  {
   PermissionUnit pu = cACR.getPermissionUnit();
   
   if( pu instanceof Permission )
    return ((Permission)pu).getAction().name();
   else
    return ((PermissionProfile)pu).getId();
  }
  
  if( key.equals(ACLDSDef.pTypeField ) )
  {
   PermissionUnit pu = cACR.getPermissionUnit();
   
   if( pu instanceof Permission )
    return ((Permission)pu).isAllow()?"allow":"deny";
   else
    return "profile";
  }
  
  if( key.equals(ACLDSDef.sIdField) )
  {
   Subject s = cACR.getSubject();
   
   if( s instanceof User )
    return ((User)s).getId();
   else
    return ((UserGroup)s).getId();
  }
  
  if( key.equals(ACLDSDef.sTypeField) )
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

