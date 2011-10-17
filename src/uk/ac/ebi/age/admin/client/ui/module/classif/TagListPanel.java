package uk.ac.ebi.age.admin.client.ui.module.classif;

import java.util.ArrayList;
import java.util.Collection;

import uk.ac.ebi.age.ext.authz.TagRef;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellSavedEvent;
import com.smartgwt.client.widgets.grid.events.CellSavedHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

//criteria={"fieldName":"userid","operator":"notEqual","value":"kk"}
//criteria={"_constructor":"AdvancedCriteria","operator":"or","criteria":[{"fieldName":"username","operator":"iNotStartsWith","value":"V"}]}


public class TagListPanel extends VLayout
{
 private TagSelectDialog tagSelDialog;
 private TagList list;
 
 public TagListPanel(Collection<TagRef> tags)
 {
  setWidth100();
  setHeight100();
  setMargin(5);

  
  ToolStrip grpTools = new ToolStrip();
  grpTools.setWidth100();

  list = new TagList();
  
  if( tags != null )
  {
   ListGridRecord recs[] = new ListGridRecord[ tags.size() ];
   
   int i=0;
   for( TagRef tr : tags )
   {
    ListGridRecord rec = new ListGridRecord();
    
    rec.setAttribute(TagList.classifierFieldName, tr.getClassiferName());
    rec.setAttribute(TagList.tagFieldName, tr.getTagName());
    rec.setAttribute(TagList.tagValueFieldName, tr.getTagValue());
    rec.setAttribute(TagList.tagRefFieldName, tr);
   
    recs[i++] = rec;
   }
   
   list.setData(recs);
  }
  
  ToolStripButton hdr = new ToolStripButton();
  hdr.setTitle("Tags");
  hdr.setSelected(false);
  hdr.setIcon( "icons/classif/tag_blue.png" );
  hdr.setShowDisabled(false);
  hdr.setDisabled(true);
  
  grpTools.addButton(hdr);

  ToolStripButton addBut = new ToolStripButton();
  addBut.setTitle("Add tag");
  addBut.setSelected(true);
  addBut.setIcon("icons/classif/tag_blue_add.png");
  addBut.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    if( tagSelDialog == null )
    {
     tagSelDialog = new TagSelectDialog(new TagSelectedListener(){

      @Override
      public void tagSelected(Collection<TagRef> trs)
      {
       if( trs != null )
       {
        for( TagRef tr:  trs )
        {
         ListGridRecord rec = new ListGridRecord();
         
         rec.setAttribute(TagList.classifierFieldName, tr.getClassiferName());
         rec.setAttribute(TagList.tagFieldName, tr.getTagName());
         rec.setAttribute(TagList.tagRefFieldName, tr);
         
         list.addData(rec);
        }
        
       }
       
       tagSelDialog.hide();
      }} );
    }
    
    tagSelDialog.show();
   }
  });

  grpTools.addSpacer(20);
  grpTools.addButton(addBut);

  ToolStripButton delBut = new ToolStripButton();
  delBut.setTitle("Delete tag");
  delBut.setSelected(true);
  delBut.setIcon("icons/classif/tag_blue_delete.png");
  delBut.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    list.removeSelectedData();
   }
  });

  grpTools.addSpacer(5);
  grpTools.addButton(delBut);

  addMember(grpTools);

  addMember(list);
  
  list.addCellSavedHandler( new CellSavedHandler()
  {
   
   @Override
   public void onCellSaved(CellSavedEvent event)
   {
    ((TagRef)event.getRecord().getAttributeAsObject(TagList.tagRefFieldName)).setTagValue(event.getRecord().getAttribute(TagList.tagValueFieldName));
   }
  });
 }
 
 public void destroy()
 {
  if( tagSelDialog != null )
   tagSelDialog.destroy();
 }

 public Collection<TagRef> getTags()
 {
  Collection<TagRef> tags = new ArrayList<TagRef>();
  
  for( Record r : list.getRecords() )
   tags.add( (TagRef)r.getAttributeAsObject(TagList.tagRefFieldName) );
  
  return tags;
 }
 
}
