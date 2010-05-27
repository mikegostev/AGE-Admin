package uk.ac.ebi.age.admin.client.ui.module;

import java.util.Collection;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class RelativesListPanel extends ListGrid
{
 private String metaClsName;
 
 RelativesListPanel( String metaClassName, Collection<? extends AgeAbstractClassImprint> nodes )
 {
  metaClsName=metaClassName;
  
  setHeight(30);
  setBodyOverflow(Overflow.VISIBLE);
  setOverflow(Overflow.VISIBLE);
  setShowHeader(false);

  ListGridField subclassIconField = new ListGridField("type", "Type", 40);
  subclassIconField.setAlign(Alignment.CENTER);
  subclassIconField.setType(ListGridFieldType.IMAGE);
  subclassIconField.setImageURLPrefix("../images/icons/"+metaClsName+"/");
  subclassIconField.setImageURLSuffix(".png");

  ListGridField subclassNameField = new ListGridField("name", "Class");

  setFields(subclassIconField, subclassNameField);

  if(nodes != null)
  {
   for(AgeAbstractClassImprint sc : nodes )
    addData(new ClassRecord(sc));
  }

 }
 
 public void addNode(AgeAbstractClassImprint cls)
 {
  addData(new ClassRecord(cls));
 }

 
 public void deleteNode(AgeAbstractClassImprint cls)
 {
  for(ListGridRecord rc : getRecords() )
  {
   if( ((ClassRecord)rc).getAgeAbstractClassImprint() == cls  )
   {
    removeData(rc);
    return;
   }
  }
 }


 public AgeAbstractClassImprint getSelectedClass()
 {
  ClassRecord cr = (ClassRecord)getSelectedRecord();
  
  if( cr == null )
   return null;
  
  return cr.getAgeAbstractClassImprint();
 }
 
 
 class ClassRecord extends ListGridRecord
 {
  private AgeAbstractClassImprint cls;
  
  ClassRecord( AgeAbstractClassImprint ci )
  {
   super();
   
   cls=ci;
   
   setAttribute("type", ci.isAbstract()?"abstract":"regular" );
   setAttribute("name", ci.getName() );
  }
  
  AgeAbstractClassImprint getAgeAbstractClassImprint()
  {
   return cls;
  }
 }



}
