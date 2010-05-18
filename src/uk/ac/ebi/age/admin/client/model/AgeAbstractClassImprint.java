package uk.ac.ebi.age.admin.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public interface AgeAbstractClassImprint extends IsSerializable
{
 void addSubclass( AgeAbstractClassImprint cls );
 void addSuperclass( AgeAbstractClassImprint cls );
}
