package uk.ac.ebi.age.admin.client.model;

import uk.ac.ebi.age.admin.client.model.restriction.RestrictionImprint;

import com.google.gwt.user.client.rpc.IsSerializable;

public interface AgeAbstractClassImprint extends IsSerializable
{
// void addSubclass( AgeAbstractClassImprint cls );
// void addSuperclass( AgeAbstractClassImprint cls );
 
 void addAttributeRestriction( RestrictionImprint restr );

 String getName();
 
 Object getAuxData();
}
