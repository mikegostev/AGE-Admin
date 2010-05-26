package uk.ac.ebi.age.admin.client.model;

import java.util.ArrayList;
import java.util.Collection;

import uk.ac.ebi.age.admin.client.model.restriction.RestrictionImprint;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AgeRelationClassImprint implements AgeAbstractClassImprint,IsSerializable
{
 private String name;
 private String id;
 private Collection<AgeRelationClassImprint> children;
 private Collection<AgeRelationClassImprint> parents;
 
 private Collection<RestrictionImprint> qualifierRestrictions;
 private ModelImprint model;
 private boolean isAbstract;

 private transient Object auxData;

 AgeRelationClassImprint()
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

 public AgeRelationClassImprint createSubClass()
 {
  if(children == null)
   children = new ArrayList<AgeRelationClassImprint>(10);

  AgeRelationClassImprint simp = model.createAgeRelationClassImprint();
  
  simp.addSuperClass(this);
  
  return simp;
 }

 
 void addSubClass(AgeRelationClassImprint simp)
 {
  if( children == null )
   children = new ArrayList<AgeRelationClassImprint>(10);
  
  children.add(simp);
 }

 public void addSuperClass(AgeRelationClassImprint simp)
 {
  if( parents == null )
   parents = new ArrayList<AgeRelationClassImprint>(10);
  
  parents.add(simp);
  
  simp.addSubClass(this);
 }
 
 public void addAttributeRestriction(RestrictionImprint rst)
 {
  if( qualifierRestrictions == null )
   qualifierRestrictions = new ArrayList<RestrictionImprint>(10);
  
  qualifierRestrictions.add(rst);
 }
 
 public Collection<RestrictionImprint> getQualifierRestrictions()
 {
  return qualifierRestrictions;
 }

 public void setQualifierRestrictions(Collection<RestrictionImprint> attributeRestrictions)
 {
  this.qualifierRestrictions = attributeRestrictions;
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
 public Collection<AgeRelationClassImprint> getChildren()
 {
  return children;
 }

 @Override
 public Collection< AgeRelationClassImprint> getParents()
 {
  return parents;
 }
}
