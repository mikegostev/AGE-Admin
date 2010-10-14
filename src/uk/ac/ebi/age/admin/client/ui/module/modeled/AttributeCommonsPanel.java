package uk.ac.ebi.age.admin.client.ui.module.modeled;

import java.util.LinkedHashMap;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeAttributeClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeClassImprint;
import uk.ac.ebi.age.admin.client.model.AttributeType;
import uk.ac.ebi.age.admin.client.ui.AttributeMetaClassDef;
import uk.ac.ebi.age.admin.client.ui.ClassMetaClassDef;
import uk.ac.ebi.age.admin.client.ui.ClassSelectedAdapter;
import uk.ac.ebi.age.admin.client.ui.ClassSelectedCallback;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class AttributeCommonsPanel extends HLayout
{

 public AttributeCommonsPanel( final AgeAttributeClassImprint cls, final XEditorPanel editor )
 {
  super(8);
  
  setMembersMargin(8);
  setLayoutMargin(8);
  
  setTitle("Common properties");
  
  VLayout controlPanel = new VLayout();
  final VLayout auxPanel = new VLayout();
  
  controlPanel.setWidth("1%");
  auxPanel.setWidth100();
  controlPanel.setMembersMargin(8);
  
  final DynamicForm form = new DynamicForm();

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

  
  final SelectItem typeSelect = new SelectItem();  
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
  
  
  typeSelect.addChangeHandler(new ChangeHandler()
  {
   @Override
   public void onChange( ChangeEvent event)
   {
    final AttributeType newType = AttributeType.valueOf(event.getValue().toString());

    Canvas[] chld = auxPanel.getChildren();
    if( chld.length > 0 )
     auxPanel.removeMember(chld[0]);

    if( newType == AttributeType.OBJECT )
    {
     final Object oldValue = event.getOldValue();

     new XSelectDialog<AgeClassImprint>(cls.getModel().getRootClass(), ClassMetaClassDef.getInstance(), new ClassSelectedCallback(){
      
      @Override
      public void classSelected(AgeAbstractClassImprint selcls)
      {
       cls.setTargetClass((AgeClassImprint)selcls);
       
       TargetForm tf = new TargetForm( cls, (AgeClassImprint)selcls );
       
       auxPanel.addMember( tf );

       cls.setType( newType );
       editor.updateClassType( cls );
     }
      
      @Override
      public void selectionCanceled()
      {
       typeSelect.setValue(oldValue.toString());
      }}).show();
     
     return;
    }

    cls.setType( newType );
    editor.updateClassType( cls );
    cls.setTargetClass(null);
   }
  });
  
  if( cls.getType() == AttributeType.OBJECT )
   auxPanel.addMember( new TargetForm(cls, cls.getTargetClass()) );
  
  form.setFields(nameField, typeSelect );
  
  controlPanel.addMember(form);
  controlPanel.addMember(auxPanel);
  
  addMember(controlPanel);
  addMember(new AliasesPanel(cls));
  addMember( new AnnotationPanel(cls) ); 
 }
 
 static class TargetForm extends DynamicForm
 {
  private AgeClassImprint targetClass;
  private AgeAttributeClassImprint attributeClass;
  
  TargetForm( AgeAttributeClassImprint attrClass, AgeClassImprint cls )
  {
   setGroupTitle("Target class");
   setIsGroup(true);
   setPadding(4);
   setWidth(200);
   
   targetClass = cls;
   attributeClass = attrClass;
   
   final StaticTextItem tagClassItm = new StaticTextItem();
   tagClassItm.setTitle("Target class");
   tagClassItm.setWidth(50);
   tagClassItm.setAlign(Alignment.CENTER);
   tagClassItm.setShowTitle(false);
   
   tagClassItm.setValue("<span class='clsRef'>" + cls.getName() + "</span>");
   
   FormItemIcon icn = new FormItemIcon();
   icn.setSrc("../images/icons/class/selbt.png");
   icn.addFormItemClickHandler(new FormItemClickHandler()
   {
    @Override
    public void onFormItemClick(FormItemIconClickEvent event)
    {
     new XSelectDialog<AgeClassImprint>(targetClass.getModel().getRootClass(), ClassMetaClassDef.getInstance(), new ClassSelectedAdapter()
     {

      @Override
      public void classSelected(AgeAbstractClassImprint cls)
      {
       tagClassItm.setValue("<span class='clsRef'>" + cls.getName() + "</span>");
       targetClass = (AgeClassImprint)cls;
       attributeClass.setTargetClass(targetClass);
      }
     }).show();

    }
   });
   tagClassItm.setIcons(icn);
   
   setItems(tagClassItm);

  }
 }
 
}
