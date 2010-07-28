package uk.ac.ebi.age.admin.client.model;

import java.util.ArrayList;
import java.util.Collection;

import uk.ac.ebi.age.admin.client.model.restriction.RestrictionImprint;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AgeAnnotationClassImprint implements AgeAbstractClassImprint,IsSerializable
{
 private String name;
 private String id;
 private Collection<AgeAnnotationClassImprint> children;
 private Collection<AgeAnnotationClassImprint> parents;
 
 private ModelImprint model;
 private boolean isAbstract;

 private transient Object auxData;

 AgeAnnotationClassImprint()
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

 public AgeAnnotationClassImprint createSubClass()
 {
  if(children == null)
   children = new ArrayList<AgeAnnotationClassImprint>(10);

  AgeAnnotationClassImprint simp = model.createAgeAnnotationClassImprint();
  
  simp.addSuperClass(this);
  
  return simp;
 }

 
 void addSubClass(AgeAnnotationClassImprint simp)
 {
  if( children == null )
   children = new ArrayList<AgeAnnotationClassImprint>(10);
  
  children.add(simp);
 }

 public void addSuperClass(AgeAnnotationClassImprint simp)
 {
  if( parents == null )
   parents = new ArrayList<AgeAnnotationClassImprint>(10);
  
  parents.add(simp);
  
  simp.addSubClass(this);
 }
 
 
 public void setModel(ModelImprint modelImprint)
 {
  model = modelImprint;
 }

 public ModelImprint getModel()
 {
  return model;
 }

 public void setAbstract(boolean b)
 {
  isAbstract=b;
 }

 public boolean isAbstract()
 {
  return isAbstract;
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
 public Collection<AgeAnnotationClassImprint> getChildren()
 {
  return children;
 }

 @Override
 public Collection< AgeAnnotationClassImprint> getParents()
 {
  return parents;
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
  
  model.removeAnnotationClassImprint( this );
 }

 @Override
 public void addSuperClass(AgeAbstractClassImprint superClass)
 {
  addSuperClass((AgeAnnotationClassImprint)superClass);
 }
}
