package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.model.ModelImprint;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class ModelDetailsPanel extends VLayout
{
 private AnnotationPanel annotPanel;
 private ModelImprint model;
 private ModelMngr mngr;
 
 public ModelDetailsPanel( ModelMngr mng )
 {
  mngr = mng;
  
  setMembersMargin(20);
  setMargin(20);
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
   nameField.setWidth(200);
   nameField.setValue(mod.getName());

   nameField.addKeyPressHandler(new KeyPressHandler()
   {
    @Override
    public void onKeyPress(KeyPressEvent event)
    {
     if("Enter".equals(event.getKeyName()))
      nameField.blurItem();
    }
   });

   nameField.addBlurHandler(new BlurHandler()
   {
    @Override
    public void onBlur(BlurEvent event)
    {
     model.setName((String) event.getItem().getValue());
    }
   });
   
   ButtonItem saveBt = new ButtonItem("save","Save");
   saveBt.setStartRow(false);
   saveBt.addClickHandler(new ClickHandler()
   {
    @Override
    public void onClick(ClickEvent event)
    {
     mngr.saveModel(model);
    }
   });
   
   form.setFields( nameField, saveBt);
  
   annotPanel = new AnnotationPanel(mod);
   annotPanel.setWidth100();

   setMembers(form,annotPanel);
  }
  
  
 }

}
