package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;

public class ClassCommonsPanel extends DynamicForm
{

 public ClassCommonsPanel( final AgeAbstractClassImprint cls, final XEditorPanel editor )
 {
  setTitle("Common properties");
  
  final TextItem nameField = new TextItem("Name");
  nameField.setValue( cls.getName() );
  nameField.setDisabled(cls.getParents()==null);
  
  nameField.addKeyPressHandler( new KeyPressHandler()
  {
   
   @Override
   public void onKeyPress(KeyPressEvent event)
   {
    if( "Enter".equals(event.getKeyName()) )
     nameField.blurItem();
   }

  });
  
  nameField.addBlurHandler(new BlurHandler()
  {
   
   @Override
   public void onBlur(BlurEvent event)
   {
    editor.updateClassName( cls, (String)event.getItem().getValue() );
   }
  });

  
  CheckboxItem abstractCB = new CheckboxItem("Abstract");
  abstractCB.setValue(cls.isAbstract());
  abstractCB.setDisabled(cls.getParents()==null);
  abstractCB.addChangedHandler( new ChangedHandler()
  {
   @Override
   public void onChanged(ChangedEvent event)
   {
    cls.setAbstract((Boolean)event.getItem().getValue());
    
    editor.updateClassType( cls );
   }
  });
  
  setFields(nameField, abstractCB );
 }
 
}
