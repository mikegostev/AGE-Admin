package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.model.RelationRuleImprint;
import uk.ac.ebi.age.admin.client.ui.SelectedRelationRule;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public abstract class RelationRuleDialog extends Window
{
 private RelationRulePanel ruleForm;
 private ModelImprint model;
 private SelectedRelationRule listener;

 
 public RelationRuleDialog(ModelImprint mod)
 {
  setWidth(600);
  setHeight(630);
  setTitle("Edit rule");
  setShowMinimizeButton(false);
  setIsModal(true);
  setShowModalMask(true);
  centerInPage();

  addCloseClickHandler( new CloseClickHandler()
  {
   @Override
   public void onCloseClick(CloseClickEvent event)
   {
    close();
   }
  });
  
  model=mod;
  
  VLayout winInter = new VLayout();
  winInter.setWidth100();
  winInter.setHeight100();

  ruleForm = getRulePanel();
  ruleForm.setWidth100();
  ruleForm.setHeight("*");

  winInter.addMember(ruleForm);

  HLayout btnPanel = new HLayout();
  btnPanel.setLayoutAlign(Alignment.CENTER);
  btnPanel.setWidth("1%");
  btnPanel.setHeight("40");
  btnPanel.setMembersMargin(30);

  IButton button;

  button = new IButton("OK");
  button.setLayoutAlign(VerticalAlignment.CENTER);
  button.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    if( ! ruleForm.updateRule() )
     return;

    close();
    
    if( listener != null )
     listener.relationRuleSelected(ruleForm.getRule());

   }
  });
  btnPanel.addMember(button);

  button = new IButton("Cancel");
  button.setLayoutAlign(VerticalAlignment.CENTER);
  button.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    close();
   }
  });
  btnPanel.addMember(button);

  winInter.addMember(btnPanel);

  addItem(winInter);

 }

 public abstract RelationRulePanel getRulePanel();
 @Override
 public abstract void close();
 
 public ModelImprint getModel()
 {
  return model;
 }
 

 public void setListener(SelectedRelationRule selectedRelRule)
 {
  listener = selectedRelRule;
 }

 public void setRule(RelationRuleImprint rule)
 {
  ruleForm.setRule(rule);
 }


}
