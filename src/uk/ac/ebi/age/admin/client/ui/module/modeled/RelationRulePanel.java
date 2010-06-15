package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.model.restriction.RelationRule;

import com.smartgwt.client.widgets.layout.VLayout;

public abstract class RelationRulePanel extends VLayout 
{

 public abstract boolean updateRule();

 public abstract RelationRule getRule();

 public abstract void setRule(RelationRule rule);


}
