package uk.ac.ebi.age.admin.client.ui.module.classif;

import java.util.HashMap;

import uk.ac.ebi.age.admin.client.Session;
import uk.ac.ebi.age.admin.client.ui.PlacingManager;
import uk.ac.ebi.age.admin.client.ui.module.auth.ACLPanel;
import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.admin.shared.cassif.TagsDSDef;
import uk.ac.ebi.age.ext.authz.TagRef;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.DSProtocol;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditFailedEvent;
import com.smartgwt.client.widgets.grid.events.EditFailedHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.events.DataArrivedEvent;
import com.smartgwt.client.widgets.tree.events.DataArrivedHandler;

public class TagsTree extends VLayout
{
 private DataSource ds;
 private String classifierId;
 private TreeGrid tagsTreeGrid;
 
 @SuppressWarnings("serial")
 TagsTree( String classifId, boolean readOnly )
 {
  setWidth100();
  setHeight100();
  setMargin(5);
  
  classifierId = classifId;
  
  ds = DataSource.getDataSource(Constants.tagTreeServiceName);
  
  if( ds != null )
  {
   ds.destroy();
   ds = null;
  }
  
  if( ds == null )
  {
   ds = TagsDSDef.getInstance().createDataSource();

   ds.setDataFormat(DSDataFormat.JSON);
   ds.setDataURL(Constants.dsServiceUrl);
   ds.setDataProtocol(DSProtocol.POSTPARAMS);
  }
  
  ds.setDefaultParams(new HashMap<String, String>()
    {{
     put(Constants.sessionKey,Session.getSessionId());
     put(Constants.classifIdParam, classifierId);
    }});

  tagsTreeGrid = new TreeGrid();
  
  tagsTreeGrid.setWidth100();
  tagsTreeGrid.setHeight100();
  tagsTreeGrid.setAutoFetchData(true);
  tagsTreeGrid.setDataSource(ds);
  tagsTreeGrid.setShowAllRecords(true);
  tagsTreeGrid.setLoadDataOnDemand(false);
  
  tagsTreeGrid.setNodeIcon("icons/classif/tag_blue.png");  
  tagsTreeGrid.setFolderIcon("icons/classif/tag_blue.png");  
  tagsTreeGrid.setShowOpenIcons(false);  
  tagsTreeGrid.setShowDropIcons(false);  
  tagsTreeGrid.setClosedIconSuffix(""); 
  tagsTreeGrid.setShowConnectors(true);
  
  tagsTreeGrid.addDataArrivedHandler(new DataArrivedHandler()
  {
   public void onDataArrived(DataArrivedEvent event)
   {
    tagsTreeGrid.getData().openAll();
   }
  });
  
  TreeGridField descField = new TreeGridField(TagsDSDef.descField.getFieldId(),TagsDSDef.descField.getFieldTitle());
  descField.setCanEdit( ! readOnly );
  
  tagsTreeGrid.setFields(new TreeGridField(TagsDSDef.idField.getFieldId(),TagsDSDef.idField.getFieldTitle()),descField );  
  
//  treeGrid.getData().openAll();
  

  if( ! readOnly )
  {
   
   ToolStrip tagTools = new ToolStrip();
   tagTools.setWidth100();
   
   
   ToolStripButton hdr = new ToolStripButton();
   hdr.setTitle("Tags");
   hdr.setSelected(false);
   hdr.setIcon( "icons/classif/tag_blue.png" );
   hdr.setShowDisabled(false);
   hdr.setDisabled(true);
   
   tagTools.addButton(hdr);
   
   ToolStripButton addBut = new ToolStripButton();
   addBut.setTitle("Add root tag");
   addBut.setSelected(true);
   addBut.setIcon("icons/classif/tag_blue_add.png");
   addBut.addClickHandler(new ClickHandler()
   {
    @Override
    public void onClick(ClickEvent event)
    {
     new TagAddDialog(ds, Constants.rootTagId).show();
    }
   });
   
   tagTools.addSpacer(20);
   tagTools.addButton(addBut);
   
   ToolStripButton addchBut = new ToolStripButton();
   addchBut.setTitle("Add child tag");
   addchBut.setSelected(true);
   addchBut.setIcon("icons/classif/tag_blue_add.png");
   addchBut.addClickHandler(new ClickHandler()
   {
    @Override
    public void onClick(ClickEvent event)
    {
     ListGridRecord rec = tagsTreeGrid.getSelectedRecord();
     
     if( rec == null )
      return;
     
     new TagAddDialog(ds, rec.getAttribute(TagsDSDef.idField.getFieldId())).show();
    }
   });
   
   tagTools.addSpacer(5);
   tagTools.addButton(addchBut);
   
   ToolStripButton delBut = new ToolStripButton();
   delBut.setTitle("Delete tag");
   delBut.setSelected(true);
   delBut.setIcon("icons/classif/tag_blue_delete.png");
   delBut.addClickHandler(new ClickHandler()
   {
    @Override
    public void onClick(ClickEvent event)
    {
     tagsTreeGrid.removeSelectedData();
    }
   });
   
   tagTools.addSpacer(5);
   tagTools.addButton(delBut);
   
   ToolStripButton permBut = new ToolStripButton();
   permBut.setTitle("Edit permissions");
   permBut.setSelected(true);
   permBut.setIcon("icons/auth/permission.png");
   permBut.addClickHandler(new ClickHandler()
   {
    @Override
    public void onClick(ClickEvent event)
    {
     ListGridRecord rec = tagsTreeGrid.getSelectedRecord();
     
     if( rec == null )
      return;
     
     String tagId = rec.getAttribute(TagsDSDef.idField.getFieldId());
     
     Canvas permPan = new ACLPanel( classifierId , tagId, Constants.tagACLServiceName );
     
     PlacingManager.placeWidget(permPan, "Tag '"+tagId+"' permissions");
    }
   });
   
   tagTools.addSpacer(10);
   tagTools.addButton(permBut);
   
   
   addMember(tagTools);
  
  }
  


  tagsTreeGrid.addEditFailedHandler(new EditFailedHandler()
  {
   @Override
   public void onEditFailed(EditFailedEvent event)
   {
    SC.warn(event.getDsResponse().getAttributeAsString("data"));

    tagsTreeGrid.discardAllEdits();
   }
  });


   
//  treeGrid.addSelectionChangedHandler(new SelectionChangedHandler()
//  {
//   
//   @Override
//   public void onSelectionChanged(SelectionEvent event)
//   {
//    clearDetailsPanel();
//    
//    if( event.getSelection() == null || event.getSelection().length != 1 )
//     return;
//    
//    TagsTree gpl = new TagsTree( event.getSelectedRecord().getAttribute(ClassifierDSDef.idField.getFieldId()), null );
//    
//    detailsPanel.addMember(gpl);
//   }
//  });

  addMember(tagsTreeGrid);

 }
 
 public TagRef getSelectedTag()
 {
  ListGridRecord rec = tagsTreeGrid.getSelectedRecord();
  
  if(rec == null)
   return null;
 
  return new TagRef( classifierId, rec.getAttribute(TagsDSDef.idField.getFieldId()) );
 }

}
