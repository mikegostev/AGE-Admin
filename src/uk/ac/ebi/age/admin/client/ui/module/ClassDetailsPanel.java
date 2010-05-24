package uk.ac.ebi.age.admin.client.ui.module;

import uk.ac.ebi.age.admin.client.model.AgeClassImprint;
import uk.ac.ebi.age.admin.client.model.restriction.RestrictionImprint;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class ClassDetailsPanel extends SectionStack
{
 private AgeClassImprint classImprint;
 
 public ClassDetailsPanel( AgeClassImprint cls )
 {
  classImprint = cls;
  
  setWidth100();
  setHeight100();
  
  setVisibilityMode(VisibilityMode.MULTIPLE);
  
  SectionStackSection commonSection = new SectionStackSection("Common");
  SectionStackSection superclassSection = new SectionStackSection("Superclasses");
  SectionStackSection subclassSection = new SectionStackSection("Subclasses");
  SectionStackSection restrictionSection = new SectionStackSection("Relationships");
  SectionStackSection attribSection = new SectionStackSection("Attributes");

  setSections(commonSection, superclassSection, subclassSection, restrictionSection, attribSection);

  
  DynamicForm commonForm = new DynamicForm();
  
  final TextItem nameField = new TextItem("Name");
  nameField.setValue( cls.getName() );
  
  nameField.addKeyPressHandler( new KeyPressHandler()
  {
   
   @Override
   public void onKeyPress(KeyPressEvent event)
   {
    System.out.println("Key pressed: "+event.getKeyName());
    
    if( "Enter".equals(event.getKeyName()) )
     nameField.blurItem();
   }
  });
  
  nameField.addBlurHandler(new BlurHandler()
  {
   
   @Override
   public void onBlur(BlurEvent event)
   {
    System.out.println("Field blur: "+event.getType());
   }
  });

  
  CheckboxItem abstractCB = new CheckboxItem("Abstract");
  
  abstractCB.addChangedHandler( new ChangedHandler()
  {
   @Override
   public void onChanged(ChangedEvent event)
   {
    System.out.println("Class is now "+(event.getValue())+"abstract");
   }
  });
  
  commonForm.setFields(nameField, abstractCB );
  commonSection.setItems(commonForm);
  
  
  VLayout superClsSec = new VLayout();
  superClsSec.setWidth100();
  superClsSec.setMargin(3);
  
  ToolStrip superTS = new ToolStrip();
  superTS.setWidth100();
  
  ToolStripButton btadd = new ToolStripButton();
  btadd.setIcon("../images/icons/class/add.png");
  superTS.addButton(btadd);
 
  ToolStripButton btdel = new ToolStripButton();
  btdel.setIcon("../images/icons/class/del.png");
  superTS.addButton(btdel);
  
  superClsSec.addMember(superTS);
  
  ListGrid superClsList = new ListGrid();
  superClsList.setHeight(30);
  superClsList.setBodyOverflow(Overflow.VISIBLE);  
  superClsList.setOverflow(Overflow.VISIBLE);  

  ListGridField superclassIconField = new ListGridField("type", "Type", 40);
  superclassIconField.setAlign(Alignment.CENTER);
  superclassIconField.setType(ListGridFieldType.IMAGE);
  superclassIconField.setImageURLPrefix("../images/icons/class/");
  superclassIconField.setImageURLSuffix(".png");

  ListGridField superclassNameField = new ListGridField("name", "Class");
  
  superClsList.setFields(superclassIconField,superclassNameField);
  
  if( cls.getParents() != null )
  {
   for( AgeClassImprint sc : cls.getParents() )
    superClsList.addData(new ClassRecord(sc) );
  }
  
  superClsSec.addMember(superClsList);
  superclassSection.addItem(superClsSec);
  
  if( cls.getParents() != null )
   expandSection(1);
  
  
  {
   VLayout subClsSec = new VLayout();
   subClsSec.setWidth100();

   ToolStrip subTS = new ToolStrip();
   subTS.setWidth100();

   subClsSec.addMember(subTS);

   ListGrid subClsList = new ListGrid();
   subClsList.setHeight(30);
   subClsList.setBodyOverflow(Overflow.VISIBLE);
   subClsList.setOverflow(Overflow.VISIBLE);

   ListGridField subclassIconField = new ListGridField("type", "Type", 40);
   subclassIconField.setAlign(Alignment.CENTER);
   subclassIconField.setType(ListGridFieldType.IMAGE);
   subclassIconField.setImageURLPrefix("../images/icons/class/");
   subclassIconField.setImageURLSuffix(".png");

   ListGridField subclassNameField = new ListGridField("name", "Class");

   subClsList.setFields(subclassIconField, subclassNameField);

   if(cls.getChildren() != null)
   {
    for(AgeClassImprint sc : cls.getChildren())
     subClsList.addData(new ClassRecord(sc));
   }

   subClsSec.addMember(subClsList);
   subclassSection.addItem(subClsSec);

   if(cls.getChildren() != null)
    expandSection(2);
  }
  
  {
   VLayout relSec = new VLayout();
   relSec.setWidth100();

   ToolStrip relTS = new ToolStrip();
   relTS.setWidth100();

   relSec.addMember(relTS);

   ListGrid relList = new ListGrid();
   relList.setHeight(30);
   relList.setBodyOverflow(Overflow.VISIBLE);
   relList.setOverflow(Overflow.VISIBLE);

   ListGridField relIconField = new ListGridField("type", "Type", 40);
   relIconField.setAlign(Alignment.CENTER);
   relIconField.setType(ListGridFieldType.IMAGE);
   relIconField.setImageURLPrefix("../images/icons/restriction/");
   relIconField.setImageURLSuffix(".png");

   ListGridField subclassNameField = new ListGridField("name", "Expression");

   relList.setFields(relIconField, subclassNameField);

   if(cls.getObjectRestrictions() != null)
   {
    for(RestrictionImprint rimp : cls.getObjectRestrictions())
     relList.addData(new RestrictionRecord(rimp));
   }

   relSec.addMember(relList);
   restrictionSection.addItem(relSec);

   if(cls.getObjectRestrictions() != null)
    expandSection(3);
  }

  
 }
 
 class ClassRecord extends ListGridRecord
 {
  ClassRecord( AgeClassImprint ci )
  {
   super();
   
   setAttribute("type", ci.isAbstract()?"abstract":"regular" );
   setAttribute("name", ci.getName() );
  }
 }
 
 class RestrictionRecord extends ListGridRecord
 {
  RestrictionRecord( RestrictionImprint ri )
  {
   super();
   
   setAttribute("type", ri.isObligatory()?"must":"may" );
   setAttribute("name", ri.toString() );
  }
 }

}
