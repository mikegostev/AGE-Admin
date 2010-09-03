package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.model.AttributeRule;

import com.smartgwt.client.widgets.layout.VLayout;

public abstract class AttributeRulePanel extends VLayout 
{

 public abstract boolean updateRule();

 public abstract AttributeRule getRule();

 public abstract void setRule(AttributeRule rule);


}
