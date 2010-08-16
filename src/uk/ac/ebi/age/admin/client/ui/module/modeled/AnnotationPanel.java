package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeAnnotationClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeAnnotationImprint;
import uk.ac.ebi.age.admin.client.ui.AnnotationMetaClassDef;
import uk.ac.ebi.age.admin.client.ui.ClassSelectedCallback;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditorExitEvent;
import com.smartgwt.client.widgets.grid.events.EditorExitHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class AnnotationPanel extends VLayout
{
 private ListGrid lst;
 private AgeAbstractClassImprint classImp;
 
 public AnnotationPanel(AgeAbstractClassImprint cls)
 {
  super(0);
  
  setWidth("*");
  
  classImp = cls;
  
  ToolStrip superTS = new ToolStrip();
  superTS.setWidth100();
  
  Label lbl = new Label("Annotations");
  lbl.setMargin(5);
  superTS.addMember( lbl );
  superTS.addFill();
  
  ToolStripButton btadd = new ToolStripButton();
  btadd.setIcon("../images/icons/add.png");
  btadd.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    addAnnotation();
   }
  });
  superTS.addButton(btadd);

  ToolStripButton btdel = new ToolStripButton();
  btdel.setIcon("../images/icons/delete.png");
  btdel.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    removeAnnotation();
   }
  });
  superTS.addButton(btdel);

  lst = new ListGrid();
  lst.setShowHeader(false);
  lst.setWidth100();
  lst.setHeight(30);
  lst.setBodyOverflow(Overflow.VISIBLE);
  lst.setOverflow(Overflow.VISIBLE);

  lst.setCanEdit(true);  
  lst.setEditEvent(ListGridEditEvent.CLICK);
  lst.setWrapCells(true); 
  
  ListGridField iconField = new ListGridField("icon", "", 40);
  iconField.setAlign(Alignment.CENTER);
  iconField.setType(ListGridFieldType.IMAGE);
  iconField.setImageURLPrefix("../images/icons/annotation/");
  iconField.setImageURLSuffix(".png");
  iconField.setCanEdit(false); 
  
  ListGridField annotField = new ListGridField("annotation", "Annotation",120);
  annotField.setCanEdit(false); 

  ListGridField textField = new ListGridField("text", "Text");

  TextAreaItem textAreaItem = new TextAreaItem();  
  textAreaItem.setHeight(70);  
  textField.setEditorType(textAreaItem);
  
  textField.addEditorExitHandler( new EditorExitHandler()
  {
   @Override
   public void onEditorExit(EditorExitEvent event)
   {
//    System.out.println( "New value: "+event.getNewValue()+"  "+event.getRecord().getClass().getName());
    ((AnnotationRecord)event.getRecord()).getAnnotation().setText(event.getNewValue().toString());
   }
  });
  
  lst.setFields(iconField,annotField,textField);
  
  addMember(superTS);
  addMember(lst);
  
  if( cls.getAnnotations() != null )
  {
   for(AgeAnnotationImprint ant : cls.getAnnotations() )
    lst.addData( new AnnotationRecord(ant) );
  }
 }
 
 private void addAnnotation()
 {
  new XSelectDialog<AgeAnnotationClassImprint>(AnnotationMetaClassDef.getInstance().getRoot(classImp.getModel()), AnnotationMetaClassDef.getInstance(), new ClassSelectedCallback()
  {
   @Override
   public void classSelected(AgeAbstractClassImprint cls)
   {
    if( cls.isAbstract() )
    {
     SC.warn("You can't annotate by an abstract annotation class");
     return;
    }
    
    AgeAnnotationImprint annt = new AgeAnnotationImprint();
    
    annt.setAnnotationClass( (AgeAnnotationClassImprint)cls );
    annt.setText("");
    
    lst.addData( new AnnotationRecord(annt) );

   }
  }).show();

 }
 
 private void removeAnnotation()
 {
  AnnotationRecord cr = (AnnotationRecord)lst.getSelectedRecord();
  
  if( cr == null )
   return;
  
  lst.removeData(cr);
  classImp.removeAnnotation( cr.getAnnotation() );
 }

 class AnnotationRecord extends ListGridRecord
 {
  private AgeAnnotationImprint annot;
  
  AnnotationRecord( AgeAnnotationImprint ant )
  {
   super();
   
   annot=ant;
   
   setAttribute("icon", "regular" );
   setAttribute("annotation", ant.getAnnotationClass().getName() );
   setAttribute("text", ant.getText());
  }

  public AgeAnnotationImprint getAnnotation()
  {
   return annot;
  }


 }
}
