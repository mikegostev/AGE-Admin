package uk.ac.ebi.age.admin.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;


public interface ModeledIcons extends ConstantsWithLookup
{
 static ModeledIcons get = GWT.create(ModeledIcons.class);

 
 String attributeRules();

 String rangeDomain();

 String hierarchy();

 String commonProperties();

 String relationRules();

 String ageClass();

 String ageAbstractClass();
 
 String classAddChild();
 String attributeAddChild();
 String relationAddChild();
 String annotationAddChild();

 String classAddSibling();
 String attributeAddSibling();
 String relationAddSibling();
 String annotationAddSibling();

 String classDelete();
 String attributeDelete();
 String relationDelete();
 String annotationDelete();

 String attributeABSTRACT();
 String attributeBOOLEAN();
 String attributeSTRING();
 String attributeINTEGER();
 String attributeREAL();
 String attributeURI();
 String attributeTEXT();
 String attributeOBJECT();
 String attributeFILE();
 String attributeTERM();

 String aliasAdd();
 String aliasDelete();
 String alias();

 String annotationAttach();
 String annotationDetach();

 String relationAddMayRule();
 String relationAddMustRule();
 String relationAddMustnotRule();
 String relationEditRule();
 String relationDeleteRule();

 String relRuleMAY();
 String relRuleMUST();
 String relRuleMUSTNOT();

 String attributeAddMayRule();
 String attributeAddMustRule();
 String attributeAddMustnotRule();
 String attributeEditRule();
 String attributeDeleteRule();

 String attrRuleMAY();
 String attrRuleMUST();
 String attrRuleMUSTNOT();


 String relation();
 String relationAbstract();
 
 String annotation();
 String annotationAbstract();
 
 String classAddSubclass();
 String classAddSuperclass();
 String classDeleteSubclass();
 String classDeleteSuperclass();
 
 String attributeAddSubclass();
 String attributeAddSuperclass();
 String attributeDeleteSubclass();
 String attributeDeleteSuperclass();

 String relationAddSubclass();
 String relationAddSuperclass();
 String relationDeleteSubclass();
 String relationDeleteSuperclass();

 String annotationAddSubclass();
 String annotationAddSuperclass();
 String annotationDeleteSubclass();
 String annotationDeleteSuperclass();

 String qualifierAdd();
 String qualifierDelete();

 String inverseRelationAdd();
 String inverseRelationDelete();

 String newModel();
 String loadModel();
 String installModel();

 String addRangeClass();
 String deleteRangeClass();
 String addDomainClass();
 String deleteDomainClass();

}
