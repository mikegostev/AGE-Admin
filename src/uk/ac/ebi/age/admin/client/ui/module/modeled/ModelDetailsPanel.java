package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.model.ModelImprint;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;

public class ModelDetailsPanel extends VLayout
{
 private AnnotationPanel annotPanel;
 private ModelImprint model;
 
 public ModelDetailsPanel()
 {
  setMembersMargin(20);
 }
 
 public void setModel(ModelImprint mod)
 {
  if( model == null )
  {
   model = mod;
   
   DynamicForm form = new DynamicForm();
   form.setNumCols(3);
   form.setWidth100();
   
   final TextItem nameField = new TextItem("Name");
   nameField.setValue(mod.getName());

   ButtonItem saveBt = new ButtonItem("save","Save");
   saveBt.setStartRow(false);
   
   form.setFields( nameField, saveBt);
  
   annotPanel = new AnnotationPanel(mod);
   annotPanel.setWidth100();

   setMembers(form,annotPanel);
  }
  
  
 }

}
