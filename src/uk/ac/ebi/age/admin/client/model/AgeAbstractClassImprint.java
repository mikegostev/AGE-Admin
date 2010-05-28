package uk.ac.ebi.age.admin.client.model;

import java.util.Collection;

import uk.ac.ebi.age.admin.client.model.restriction.RestrictionImprint;

import com.google.gwt.user.client.rpc.IsSerializable;

public interface AgeAbstractClassImprint extends IsSerializable
{
// void addSubclass( AgeAbstractClassImprint cls );
// void addSuperclass( AgeAbstractClassImprint cls );
 
 void addAttributeRestriction( RestrictionImprint restr );

 String getName();
 
 Object getAuxData();

 boolean isAbstract();

 Collection<? extends AgeAbstractClassImprint> getChildren();
 Collection<? extends AgeAbstractClassImprint> getParents();

 void setName(String newName);

 void setAbstract(boolean abstr);

 void removeChild(AgeAbstractClassImprint cimp);
 void removeParent(AgeAbstractClassImprint cimp);

 void delete();

 ModelImprint getModel();

 AgeAbstractClassImprint createSubClass();

 void addSuperClass(AgeAbstractClassImprint superClass);
 
}
