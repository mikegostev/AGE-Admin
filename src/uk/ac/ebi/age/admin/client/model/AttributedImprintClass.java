package uk.ac.ebi.age.admin.client.model;

import java.util.Collection;

public interface AttributedImprintClass
{
 ModelImprint getModel();
 
 Collection<AttributeRuleImprint> getAttributeRules();

 void addAttributeRule(AttributeRuleImprint rst);

 void removeAttribiteRule(AttributeRuleImprint rule);
}
