package uk.ac.ebi.age.admin.client.model;

import java.util.ArrayList;
import java.util.Collection;

import uk.ac.ebi.age.admin.client.model.restriction.RestrictionImprint;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AgeAttributeClassImprint implements AgeAbstractClassImprint,IsSerializable
{
 public enum Type
 {
  ABSTRACT,
  BOOLEAN,
  STRING,
  INTEGER,
  REAL,
  URI, 
  TEXT
 }
 
 private String name;
 private String id;
 
 private Collection<AgeAttributeClassImprint> parents;
 private Collection<AgeAttributeClassImprint> children;
 
 private Collection<RestrictionImprint> qualifierRestrictions;
 private ModelImprint model;

 private Type type;
 
 private transient Object auxData;

 
 AgeAttributeClassImprint()
 {}
 
 public String getName()
 {
  return name;
 }

 public void setName(String name)
 {
  this.name = name;
 }

 public String getId()
 {
  return id;
 }

 public void setId(String id)
 {
  this.id = id;
 }

 public AgeAttributeClassImprint createSubClass()
 {
  if(children == null)
   children = new ArrayList<AgeAttributeClassImprint>(10);

  AgeAttributeClassImprint simp = model.createAgeAttributClassImprint();
  
  simp.addSuperClass(this);
  
  return simp;
 }

 
 public Collection<AgeAttributeClassImprint> getParents()
 {
  return parents;
 }

 public Collection<AgeAttributeClassImprint> getChildren()
 {
  return children;
 }



 public Collection<RestrictionImprint> getQualifierRestrictions()
 {
  return qualifierRestrictions;
 }

 public void setQualifierRestrictions(Collection<RestrictionImprint> attributeRestrictions)
 {
  this.qualifierRestrictions = attributeRestrictions;
 }

 void addSubClass(AgeAttributeClassImprint simp)
 {
  if( children == null )
   children = new ArrayList<AgeAttributeClassImprint>(10);
  
  children.add(simp);
 }

 public void addSuperClass(AgeAttributeClassImprint simp)
 {
  if( parents == null )
   parents = new ArrayList<AgeAttributeClassImprint>(10);
  
  parents.add(simp);
  simp.addSubClass(this);
 }

 public void addAttributeRestriction(RestrictionImprint rst)
 {
  if( qualifierRestrictions == null )
   qualifierRestrictions = new ArrayList<RestrictionImprint>(10);
  
  qualifierRestrictions.add(rst);
 }
 
 public void setModel(ModelImprint modelImprint)
 {
  model = modelImprint;
 }

 public ModelImprint getModel()
 {
  return model;
 }

 public boolean isAbstract()
 {
  return type==Type.ABSTRACT;
 }
 
 public void setAbstract(boolean b)
 {
  type=Type.ABSTRACT;
 }

 public void setType( Type t )
 {
  type=t;
 }

 public Object getAuxData()
 {
  return auxData;
 }

 public void setAuxData(Object auxData)
 {
  this.auxData = auxData;
 }
 
 @Override
 public void removeChild(AgeAbstractClassImprint cimp)
 {
  if( children != null )
   children.remove(cimp);
 }

 @Override
 public void removeParent(AgeAbstractClassImprint cimp)
 {
  if( parents != null )
  {
   parents.remove(cimp);
  
   if( parents.size() == 0 )
    delete();
  }
 }

 @Override
 public void delete()
 {
  if( getParents() != null )
  {
   for( AgeAbstractClassImprint pcls : getParents() )
    pcls.removeChild( this );
  }
  
  if( getChildren() != null )
  {
   for( AgeAbstractClassImprint pcls : getChildren() )
    pcls.removeParent( this );
  } 
  
  model.removeAttributeClassImprint( this );
 }
 
 @Override
 public void addSuperClass(AgeAbstractClassImprint superClass)
 {
  addSuperClass((AgeAttributeClassImprint)superClass);
 }
}
