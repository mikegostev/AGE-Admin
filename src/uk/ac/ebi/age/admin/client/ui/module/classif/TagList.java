package uk.ac.ebi.age.admin.client.ui.module.classif;

import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.admin.shared.cassif.TagsDSDef;
import uk.ac.ebi.age.ext.authz.TagRef;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.EventHandler;
import com.smartgwt.client.widgets.events.DropEvent;
import com.smartgwt.client.widgets.events.DropHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.tree.TreeGrid;

public class TagList extends ListGrid
{
 public static final String classifierFieldName =  "clsf";
 public static final String tagFieldName =  "tag";
 public static final String tagValueFieldName =  "value";
 public static final String tagRefFieldName =  "__tagRef";
 
 public TagList()
 {

  final ListGrid list = this;
  list.setCanDragRecordsOut(false);
  list.setDragDataAction(DragDataAction.COPY);
  
  list.setCanAcceptDroppedRecords(true);
  
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

  list.addDropHandler( new DropHandler() 
  {
   
   @Override
   public void onDrop(DropEvent event)
   {
    Object src = EventHandler.getDragTarget();
    
    if( ! (src instanceof TreeGrid) )
    {
     event.cancel();
     return;
    }
    
    Record[] recs = ((TreeGrid)src).getDragData();
    
    for( Record r : recs )
    {
     TagRef tr = new TagRef(
       ((TreeGrid)src).getDataSource().getDefaultParams().get(Constants.classifIdParam).toString(),
       r.getAttribute(TagsDSDef.idField.getFieldId()));
     
     addTagRef(tr);
    }
    
    event.cancel();
   }
  });
 }
 
 public void addTagRef( TagRef tr )
 {
  for(Record exr : getRecords())
  {
   TagRef etr = (TagRef) exr.getAttributeAsObject(TagList.tagRefFieldName);

   if(etr.getClassiferName().equals(tr.getClassiferName()) && etr.getTagName().equals(tr.getTagName()))
    return;
  }
  
  ListGridRecord rec = new ListGridRecord();
  
  rec.setAttribute(TagList.classifierFieldName, tr.getClassiferName());
  rec.setAttribute(TagList.tagFieldName, tr.getTagName());
  rec.setAttribute(TagList.tagRefFieldName, tr);
  
  addData(rec);
 }
}
