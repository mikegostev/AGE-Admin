package uk.ac.ebi.age.admin.client.ui.module.modeled;

import java.util.LinkedHashMap;

import uk.ac.ebi.age.admin.client.model.AgeAttributeClassImprint;
import uk.ac.ebi.age.admin.client.model.AttributeType;
import uk.ac.ebi.age.admin.client.ui.AttributeMetaClassDef;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.layout.HLayout;

public class AttributeCommonsPanel extends HLayout
{

 public AttributeCommonsPanel( final AgeAttributeClassImprint cls, final XEditorPanel editor )
 {
  super(8);
  
  setMembersMargin(8);
  setLayoutMargin(8);
  
  setTitle("Common properties");
  
  DynamicForm form = new DynamicForm();

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

  
  SelectItem typeSelect = new SelectItem();  
  typeSelect.setTitle("Select datatype");  
  typeSelect.setDisabled(cls.getParents()==null);
  LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();  

  for( AttributeType atyp : AttributeType.values() )
   valueMap.put(atyp.name(), atyp.name());  
  
  typeSelect.setValueMap(valueMap); 
  
  LinkedHashMap<String, String> iconMap = new LinkedHashMap<String, String>();  

  for( AttributeType atyp : AttributeType.values() )
   iconMap.put(atyp.name(), AttributeMetaClassDef.getIcon(atyp));  
  
  typeSelect.setValueIcons(iconMap); 

  typeSelect.setValue(cls.getType().name());
  
  typeSelect.addChangedHandler(new ChangedHandler()
  {
   @Override
   public void onChanged(ChangedEvent event)
   {
    cls.setType( AttributeType.valueOf(event.getValue().toString()) );
    editor.updateClassType( cls );
   }
  });
  
  form.setFields(nameField, typeSelect );
  
  addMember(form);
  addMember(new AliasesPanel(cls));
  addMember( new AnnotationPanel(cls) ); 
 }
 
}
