package uk.ac.ebi.ageview.client.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.ebi.ageview.client.shared.AttributeReport;
import uk.ac.ebi.ageview.client.shared.Pair;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AttributedImprint implements IsSerializable
{
 private String id;
 private String title;
 private List<AttributeReport> attr = new ArrayList<AttributeReport>();
 
 private List<Pair<String,String>> othr ;
 
 private Map<String,List<AttributedImprint>> attachedObjects;
 
 public void addAttribute(String nm, String val, boolean cust, int ord)
 {
  AttributeReport atr = new AttributeReport();
  
  atr.setName(nm);
  atr.setValue(val);
  atr.setOrder(ord);
  atr.setCustom(cust);
  
  attr.add(atr);
 }
 
 public List<AttributeReport> getAttributes()
 {
  return attr;
 }

 public String getId()
 {
  return id;
 }

 public void setId(String id)
 {
  this.id = id;
 }

 public String getTitle()
 {
  return title;
 }

 public void setTitle(String title)
 {
  this.title = title;
 }
 
 public void addOtherInfo(String name, String val)
 {
  if( othr == null )
   othr = new ArrayList<Pair<String,String>>(5);
  
  othr.add( new Pair<String, String>(name, val) );
 }
 
 public List< Pair<String, String> > getOtherInfo()
 {
  return othr;
 }
 
 public void attachObjects( String cls, AttributedImprint obj )
 {
  List<AttributedImprint> objList=null;
  
  if( attachedObjects == null )
  {
   attachedObjects = new HashMap<String, List<AttributedImprint>>();
   attachedObjects.put(cls, objList = new ArrayList<AttributedImprint>(4) );
  }
  else
  {
   objList = attachedObjects.get(cls);
   
   if( objList == null  )
    attachedObjects.put(cls, objList = new ArrayList<AttributedImprint>(4) );
  }
  
  objList.add(obj);
 }
 
 public Collection<String> getAttachedClasses()
 {
  if( attachedObjects == null )
   return null;
  
  return attachedObjects.keySet();
 }
 
 public List<AttributedImprint> getAttachedObjects( String cls )
 {
  return attachedObjects.get(cls);
 }
}
