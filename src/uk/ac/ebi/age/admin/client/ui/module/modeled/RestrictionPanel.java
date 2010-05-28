package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.model.AgeClassImprint;
import uk.ac.ebi.age.admin.client.model.restriction.RestrictionImprint;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

public class RestrictionPanel extends VLayout
{
 public RestrictionPanel( final AgeClassImprint cls, XEditorPanel editor )
 {
  setWidth100();
  setMargin(3);
  setTitle("Relationships");
  
  
  ToolStrip relTS = new ToolStrip();
  relTS.setWidth100();

  addMember(relTS);

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

  addMember(relList);
 }
 
 static class RestrictionRecord extends ListGridRecord
 {
  RestrictionRecord( RestrictionImprint ri )
  {
   super();
   
   setAttribute("type", ri.isObligatory()?"must":"may" );
   setAttribute("name", ri.toString() );
  }
 }
}
