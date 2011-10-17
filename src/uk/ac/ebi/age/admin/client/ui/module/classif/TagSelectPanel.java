package uk.ac.ebi.age.admin.client.ui.module.classif;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.ac.ebi.age.admin.client.ui.ObjectSelectedListened;
import uk.ac.ebi.age.ext.authz.TagRef;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellSavedEvent;
import com.smartgwt.client.widgets.grid.events.CellSavedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class TagSelectPanel extends HLayout
{
 private TagList tagList = new TagList();
 
 
 public TagSelectPanel( Collection<TagRef> tags )
 {
  final ClassifiersPanel clsfPnl = new ClassifiersPanel( true, new ObjectSelectedListened<TagRef>()
  {
   @Override
   public void objectSelected(TagRef obj)
   {
    tagList.addTagRef(obj);
   }
  } );

  clsfPnl.setWidth("66%");
  
  addMember(clsfPnl);
  
  VLayout tPnl = new VLayout();
  
  tPnl.setMargin(5);
  
  tPnl.setWidth("33%");
  
  addMember(tPnl);

  
  setWidth100();
  setHeight100();
  setMargin(5);

  
  ToolStrip grpTools = new ToolStrip();
  grpTools.setWidth100();

  
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
   
   tagList.setData(recs);
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
    TagRef tr = clsfPnl.getSelectedTag();
    
    if( tr == null )
     return;
    
    for( Record r : tagList.getRecords() )
    {
     TagRef etr = (TagRef)r.getAttributeAsObject(TagList.tagRefFieldName);
     
     if( etr.getClassiferName().equals(tr.getClassiferName()) && etr.getTagName().equals( tr.getTagName() ) )
      return;
    }
    
    ListGridRecord rec = new ListGridRecord();
    
    rec.setAttribute(TagList.classifierFieldName, tr.getClassiferName());
    rec.setAttribute(TagList.tagFieldName, tr.getTagName());
    rec.setAttribute(TagList.tagRefFieldName, tr);
    
    tagList.addData(rec);
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
    tagList.removeSelectedData();
   }
  });

  grpTools.addSpacer(5);
  grpTools.addButton(delBut);

  tPnl.addMember(grpTools);

  tPnl.addMember(tagList);
  
  tagList.addCellSavedHandler( new CellSavedHandler()
  {
   
   @Override
   public void onCellSaved(CellSavedEvent event)
   {
    ((TagRef)event.getRecord().getAttributeAsObject(TagList.tagRefFieldName)).setTagValue(event.getRecord().getAttribute(TagList.tagValueFieldName));
   }
  });
 }

 public Collection<TagRef> getTags()
 {
  List<TagRef> tags = new ArrayList<TagRef>();
  
  for( Record r : tagList.getRecords() )
  {
   tags.add( (TagRef) r.getAttributeAsObject(TagList.tagRefFieldName) );
  }
  
  return tags;
 }
}
