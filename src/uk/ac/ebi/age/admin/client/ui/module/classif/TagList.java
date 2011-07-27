package uk.ac.ebi.age.admin.client.ui.module.classif;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

public class TagList extends ListGrid
{
 public static final String classifierFieldName =  "clsf";
 public static final String tagFieldName =  "tag";
 public static final String tagValueFieldName =  "value";
 
 public TagList()
 {

  final ListGrid list = this;
  list.setCanDragRecordsOut(true);
  list.setDragDataAction(DragDataAction.COPY);
  
  list.setSelectionType(SelectionStyle.SINGLE);
  
  ListGridField icnField = new ListGridField("grpIcon", "");
  icnField.setWidth(30);
  icnField.setAlign(Alignment.CENTER);
  icnField.setType(ListGridFieldType.ICON);
  icnField.setIcon("icons/classif/tag_blue.png");

  ListGridField classifField = new ListGridField(classifierFieldName, "Clasifier");
  classifField.setWidth(200);

  ListGridField tagField = new ListGridField(tagFieldName, "Tag");
  tagField.setCanEdit(false);
 
  ListGridField tagValField = new ListGridField(tagValueFieldName, "Value");
  tagValField.setCanEdit(true);

  list.setFields(icnField, classifField, tagField, tagValField);

  list.setWidth100();
  list.setHeight100();
 // list.setAutoFetchData(true);

 }
}
