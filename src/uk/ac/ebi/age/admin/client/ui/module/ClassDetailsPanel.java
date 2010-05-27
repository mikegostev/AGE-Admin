package uk.ac.ebi.age.admin.client.ui.module;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeClassImprint;
import uk.ac.ebi.age.admin.client.model.restriction.RestrictionImprint;
import uk.ac.ebi.age.admin.client.ui.ClassSelectedCallback;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
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
 private ModelClassesPanel classesPanel;
 
 public ClassDetailsPanel( AgeClassImprint cls, ModelClassesPanel clsPanel )
 {
  classesPanel = clsPanel;
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
  nameField.setDisabled(classImprint.getParents()==null);
  
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
    classesPanel.updateClassName( classImprint, (String)event.getItem().getValue() );
   }
  });

  
  CheckboxItem abstractCB = new CheckboxItem("Abstract");
  abstractCB.setValue(classImprint.isAbstract());
  abstractCB.setDisabled(classImprint.getParents()==null);
  abstractCB.addChangedHandler( new ChangedHandler()
  {
   @Override
   public void onChanged(ChangedEvent event)
   {
    classesPanel.updateClassType( classImprint, (Boolean)event.getItem().getValue() );
   }
  });
  
  commonForm.setFields(nameField, abstractCB );
  commonSection.setItems(commonForm);
  
  {
   VLayout superClsSec = new VLayout();
   superClsSec.setWidth100();
   superClsSec.setMargin(3);

   ToolStrip superTS = new ToolStrip();
   superTS.setWidth100();

   final RelativesListPanel superClsList = new RelativesListPanel("class", cls.getParents());

   ToolStripButton btadd = new ToolStripButton();
   btadd.setIcon("../images/icons/class/add.png");
   btadd.addClickHandler(new ClickHandler()
   {
    @Override
    public void onClick(ClickEvent event)
    {
     classesPanel.addSuperclass(classImprint, new ClassSelectedCallback()
     {
      @Override
      public void classSelected(AgeClassImprint cls)
      {
       superClsList.addNode(cls);
      }
     });
    }
   });
   superTS.addButton(btadd);

   ToolStripButton btdel = new ToolStripButton();
   btdel.setIcon("../images/icons/class/del.png");
   btdel.addClickHandler(new ClickHandler()
   {
    @Override
    public void onClick(ClickEvent event)
    {
     final AgeAbstractClassImprint cimp  = superClsList.getSelectedClass();
     
     if( cimp == null )
      return;
     
     if( classImprint.getParents().size() == 1 )
      SC.warn("You can't delete the last superclass");
     else
     {
      classesPanel.unlink(cimp, classImprint);
      superClsList.deleteNode(cimp);
     }
    }
   });
   superTS.addButton(btdel);

   superClsSec.addMember(superTS);

   superClsSec.addMember(superClsList);
   superclassSection.addItem(superClsSec);

   if(cls.getParents() != null)
    expandSection(1);
  }
  
  {
   VLayout subClsSec = new VLayout();
   subClsSec.setWidth100();
   subClsSec.setMargin(3);

   ToolStrip subTS = new ToolStrip();
   subTS.setWidth100();

   final RelativesListPanel subClsList = new RelativesListPanel("class", cls.getChildren());
   
   ToolStripButton btadd = new ToolStripButton();
   btadd.setIcon("../images/icons/class/add.png");
   btadd.addClickHandler( new ClickHandler()
   {
    @Override
    public void onClick(ClickEvent event)
    {
     classesPanel.addSubclass(classImprint, new ClassSelectedCallback()
     {
      @Override
      public void classSelected(AgeClassImprint cls)
      {
       subClsList.addNode( cls );
      }
     });
    }
   });
   subTS.addButton(btadd);
  
   ToolStripButton btdel = new ToolStripButton();
   btdel.setIcon("../images/icons/class/del.png");
   btdel.addClickHandler(new ClickHandler()
   {
    @Override
    public void onClick(ClickEvent event)
    {
     final AgeAbstractClassImprint cimp  = subClsList.getSelectedClass();
     
     if( cimp == null )
      return;
    
     if( cimp.getParents().size() == 1 )
     {
      SC.confirm("Class '"+cimp.getName()+"' will be deleted", new BooleanCallback()
      {
       
       @Override
       public void execute(Boolean value)
       {
        if( ! value )
         return;

        classesPanel.unlink( classImprint, cimp);
        subClsList.deleteNode(cimp);
       }
      });
     }
     else
     {
      classesPanel.unlink( classImprint, cimp);
      subClsList.deleteNode(cimp);
     }
     

    }
   });
   subTS.addButton(btdel);

   subClsSec.addMember(subTS);

   subClsSec.addMember(subClsList);
   subclassSection.addItem(subClsSec);

   if(cls.getChildren() != null)
    expandSection(2);
  }
  
  {
   VLayout relSec = new VLayout();
   relSec.setWidth100();
   relSec.setMargin(3);

   ToolStrip relTS = new ToolStrip();
   relTS.setWidth100();

   relSec.addMember(relTS);

   ListGrid relList = new ListGrid();
   relList.setHeight(30);
   relList.setBodyOverflow(Overflow.VISIBLE);
   relList.setOverflow(Overflow.VISIBLE);
   relList.setShowHeader(false);
   
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
