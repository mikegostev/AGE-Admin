package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeRelationClassImprint;
import uk.ac.ebi.age.admin.client.ui.ClassSelectedAdapter;
import uk.ac.ebi.age.admin.client.ui.RelationMetaClassDef;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.layout.HLayout;

public class RelationCommonsPanel extends HLayout
{

 public RelationCommonsPanel(final AgeRelationClassImprint cls, final XEditorPanel editor)
 {
  super(8);
  
  setMembersMargin(8);
  setLayoutMargin(8);
  
  setTitle("Common properties");

  DynamicForm form = new DynamicForm();

  final TextItem nameField = new TextItem("Name");
  nameField.setValue(cls.getName());
  nameField.setDisabled(cls.getParents() == null);

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
    editor.updateClassName(cls, (String) event.getItem().getValue());
   }
  });

  CheckboxItem abstractCB = new CheckboxItem("Abstract");
  abstractCB.setValue(cls.isAbstract());
  abstractCB.setDisabled(cls.getParents() == null);
  abstractCB.addChangedHandler(new ChangedHandler()
  {
   @Override
   public void onChanged(ChangedEvent event)
   {
    cls.setAbstract((Boolean) event.getItem().getValue());

    editor.updateClassType(cls);
   }
  });

  
  CheckboxItem symmetricCB = new CheckboxItem("Symmetric");
  symmetricCB.setValue(cls.isSymmetric());
  symmetricCB.setDisabled(cls.getParents() == null);
  symmetricCB.addChangedHandler(new ChangedHandler()
  {
   @Override
   public void onChanged(ChangedEvent event)
   {
    cls.setSymmetric((Boolean) event.getItem().getValue());

    editor.updateClassType(cls);
   }
  });

  CheckboxItem transCB = new CheckboxItem("Transitive");
  transCB.setValue(cls.isTransitive());
  transCB.setDisabled(cls.getParents() == null);
  transCB.addChangedHandler(new ChangedHandler()
  {
   @Override
   public void onChanged(ChangedEvent event)
   {
    cls.setTransitive((Boolean) event.getItem().getValue());

    editor.updateClassType(cls);
   }
  });  
  
  CheckboxItem functionalCB = new CheckboxItem("Functional");
  functionalCB.setValue(cls.isFunctional());
  functionalCB.setDisabled(cls.getParents() == null);
  functionalCB.addChangedHandler(new ChangedHandler()
  {
   @Override
   public void onChanged(ChangedEvent event)
   {
    cls.setFunctional((Boolean) event.getItem().getValue());

    editor.updateClassType(cls);
   }
  });

  CheckboxItem invFuncCB = new CheckboxItem("InvFunctional");
  invFuncCB.setTitle("Inverse Functional");
  invFuncCB.setValue(cls.isInverseFunctional());
  invFuncCB.setDisabled(cls.getParents() == null);
  invFuncCB.addChangedHandler(new ChangedHandler()
  {
   @Override
   public void onChanged(ChangedEvent event)
   {
    cls.setInverseFunctional((Boolean) event.getItem().getValue());

    editor.updateClassType(cls);
   }
  });

  
  form.setFields(nameField, abstractCB, symmetricCB, transCB, functionalCB, invFuncCB);

  addMember(form);
  addMember( new InvRelForm(cls) );
  addMember(new AliasesPanel(cls));
  addMember( new AnnotationPanel(cls) );
  
 }

 static class InvRelForm extends DynamicForm
 {
  public InvRelForm( final AgeRelationClassImprint relCls )
  {
   setGroupTitle("Inverse Relation");
   setIsGroup(true);
   setPadding(5);
   
   final StaticTextItem relClassItm = new StaticTextItem();
   relClassItm.setTitle("Relation class");
   relClassItm.setShowTitle(false);
   relClassItm.setWidth(50);
   relClassItm.setAlign(Alignment.RIGHT);
   
   FormItemIcon icn = new FormItemIcon();
   icn.setSrc("../images/icons/relation/selbt.png");
   icn.addFormItemClickHandler(new FormItemClickHandler()
   {
    @Override
    public void onFormItemClick(FormItemIconClickEvent event)
    {
     new XSelectDialog<AgeRelationClassImprint>(relCls.getModel().getRootRelationClass(), RelationMetaClassDef.getInstance(), new ClassSelectedAdapter()
     {
      
      @Override
      public void classSelected(AgeAbstractClassImprint cls)
      {
       relClassItm.setValue("<span class='relRef'>"+cls.getName()+"</span>");
       relCls.setInverseRelation((AgeRelationClassImprint)cls);
      }
     }).show();

     
    }
   });
   relClassItm.setIcons(icn);
   
   setItems(relClassItm);

  }
 }
 
}
