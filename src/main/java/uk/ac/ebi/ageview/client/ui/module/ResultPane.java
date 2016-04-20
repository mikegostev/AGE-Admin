package uk.ac.ebi.ageview.client.ui.module;

import uk.ac.ebi.age.ui.client.LinkClickListenerJSO;
import uk.ac.ebi.age.ui.client.LinkManager;
import uk.ac.ebi.age.ui.client.ObjectProviderService;
import uk.ac.ebi.age.ui.client.module.ObjectImprintViewPanelHTML;
import uk.ac.ebi.age.ui.client.module.ObjectImprintViewerWindow;
import uk.ac.ebi.age.ui.client.module.PagingRuler;
import uk.ac.ebi.age.ui.shared.imprint.AttributeImprint;
import uk.ac.ebi.age.ui.shared.imprint.ObjectId;
import uk.ac.ebi.age.ui.shared.imprint.ObjectImprint;
import uk.ac.ebi.ageview.client.AgeViewGWTService;
import uk.ac.ebi.ageview.client.query.Report;
import uk.ac.ebi.ageview.client.ui.ResultRenderer;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.ExpansionMode;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordCollapseEvent;
import com.smartgwt.client.widgets.grid.events.RecordCollapseHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class ResultPane extends VLayout implements ResultRenderer
{
 public final static int MAX_GROUPS_PER_PAGE=25;
 public final static int MAX_SAMPLES_PER_PAGE=20;
 
 private String query;
// private boolean searchSamples;
// private boolean searchGroups;
 private boolean searchAtNames;
 private boolean searchAtValues;
 
 private ListGrid resultGrid = new GroupsList();
 private PagingRuler pagingRuler = new PagingRuler("groupPage");
 private HTMLFlow statusBar = new HTMLFlow();
 
 public ResultPane()
 {
  setHeight("100%");
  
  setMargin(5);
  
  setShowEdges(true);
  setEdgeSize(6);
  setEdgeImage("gnframe.gif");
  setEdgeMarginSize(10);
  
  resultGrid.setShowAllRecords(true);  
  resultGrid.setWrapCells(true);
  resultGrid.setFixedRecordHeights(false);
  resultGrid.setCellHeight(20);
  resultGrid.setCanDragSelectText(true);
  resultGrid.setCanDrag(false);
 
//  resultGrid.setBodyOverflow(Overflow.VISIBLE);
  resultGrid.setOverflow(Overflow.VISIBLE);
  
  resultGrid.setStyleName("reportGrid");
  
//  setBaseStyle("reportGrid");
  
  DataSource ds = new DataSource();
  
  ds.setClientOnly(true);
  
  ds.addField(new DataSourceTextField("id", "ID"));
  ds.addField(new DataSourceTextField("desc", "Description", 2000));
  ds.addField(new DataSourceIntegerField("prop", "Property", 111));
  
//  setDataSource(ds);
  
  resultGrid.setCanExpandRecords(true); 
  
  ListGridField idField = new ListGridField("id","ID", 200);  
  ListGridField descField = new ListGridField("desc","Description");
//  ListGridField propField = new ListGridField("prop","AdditionalProp");
  
//  propField.setHidden(true);

     
  resultGrid.setFields(idField, descField );
  resultGrid.setExpansionMode(ExpansionMode.DETAIL_FIELD);
  
  pagingRuler.setVisible(true);
  
  statusBar.setWidth100();
  statusBar.setHeight(22);
  
  addMember(pagingRuler);
  addMember(resultGrid);
  addMember(statusBar);
  
  resultGrid.addRecordCollapseHandler( new RecordCollapseHandler()
  {
   
   @Override
   public void onRecordCollapse(RecordCollapseEvent event)
   {
    Canvas pnl = (Canvas)event.getRecord().getAttributeAsObject("_panel");

    if( pnl !=null )
    {
     pnl.destroy();
     event.getRecord().setAttribute("_panel", (Object)null);
    }
    
    LinkManager.getInstance().removeLinkClickListener(event.getRecord().getAttribute("id"));
   }
  });
  
  LinkManager.getInstance().addLinkClickListener("ShowObject", new LinkClickListenerJSO()
  {

   @Override
   public void linkClicked(JsArrayString param)
   {
   
    ObjectId id = new ObjectId(param.get(0), param.get(1), param.get(2));
    
//    new ObjectImprintViewerWindow(id, 2, null );
    
   }

   @Override
   public void linkClicked(JavaScriptObject param)
   {
    JsArrayString jsa = param.cast();
    
    ObjectId id = new ObjectId(jsa.get(0), jsa.get(1), jsa.get(2));

    AgeViewGWTService.Util.getInstance().getObjectImprint(id, new AsyncCallback<ObjectImprint>()
    {
     
     @Override
     public void onSuccess(ObjectImprint arg0)
     {
      new ObjectImprintViewerWindow(arg0, 2, new ObjectProviderService()
      {
       
       @Override
       public void getObject(ObjectId id, final AsyncCallback<ObjectImprint> cb)
       {
        AgeViewGWTService.Util.getInstance().getObjectImprint(id, new AsyncCallback<ObjectImprint>(){

         @Override
         public void onFailure(Throwable arg0)
         {
          cb.onFailure(arg0);
         }

         @Override
         public void onSuccess(ObjectImprint arg0)
         {
          cb.onSuccess(arg0);
         }} );
        
       }
      }).show();
      
     }
     
     @Override
     public void onFailure(Throwable arg0)
     {
      // TODO Auto-generated method stub
      
     }
    });
    
//    System.out.println("Object "+jsa.length()+" "+id);
   }}
  );
//  ListGridRecord rec = new ListGridRecord();
//  
//  rec.setAttribute("id", "SBM00000X");
//  rec.setAttribute("desc", "This is my first submission description\nline 2 This is my first submission description\\nline 2This is my first submission description\\nline 2This is my first submission description\\nline 2This is my first submission description\\nline 2");
//  rec.setAttribute("prop", 100);
//  
//  addData(rec);
  
//  rec = new ListGridRecord();
//  
//  rec.setAttribute("id", "SBM00000X");
//  rec.setAttribute("desc", "This is my first submission description");
//  rec.setAttribute("prop", 100);
//
//  ds.addData(rec);
  
//  rec.setDetailDS(ds);
  
//  setAlign(Alignment.CENTER);
//  
//  Label lb = new Label("Query result is empty");
//  
//  lb.setAlign(Alignment.CENTER);
//  
//  addMember(lb);
 }
 
 @Override
 public void showResult( Report res, String qry, boolean sAtrNm, boolean sAtrVl, int cpage )
 {
  query = qry;
//  searchSamples=sSmp;
//  searchGroups=sGrp;
  searchAtNames=sAtrNm;
  searchAtValues=sAtrVl;
  
  resultGrid.selectAllRecords();
  resultGrid.removeSelectedData();
  
  int firstGrp = (cpage-1)*MAX_GROUPS_PER_PAGE+1;
  int lastGrp = firstGrp+MAX_GROUPS_PER_PAGE-1;
  
  if( lastGrp > res.getTotalObjects() )
   lastGrp = res.getTotalObjects();
  
  pagingRuler.setContents( cpage, res.getTotalObjects(), MAX_GROUPS_PER_PAGE,
    "Objects: "+res.getTotalObjects()+"&nbsp;&nbsp;Displaying objects "+firstGrp+" to "+lastGrp+".", null);
  
//  "Groups: "+res.getTotalGroups()+" Samples: "+res.getTotalSamples()
//  if( res.getTotalGroups() > MAX_GROUPS_PER_PAGE )
//  {
//   pagingRuler.setPagination(cpage, res.getTotalGroups(), MAX_GROUPS_PER_PAGE);
//   pagingRuler.setVisible(true);
//  }
//  else
//   pagingRuler.setVisible(false);
  
  for( ObjectImprint sgr : res.getObjects() )
  {
   ImprintGridRecord rec = new ImprintGridRecord();
   
   rec.setAttribute("id", sgr.getId().getObjectId());

   String title = null;
   
   for( AttributeImprint ati : sgr.getAttributes() )
   {
    if( ati.getClassImprint().getName().equalsIgnoreCase("Title") )
    {
     title = ati.getValue().getStringValue();
     break;
    }
    else if( ati.getClassImprint().getName().equalsIgnoreCase("Description")  )
     title = ati.getValue().getStringValue();
    else if( title == null && ati.getClassImprint().getName().equalsIgnoreCase("Name")  )
     title = ati.getValue().getStringValue();

   }
   
   if( title == null )
    title = sgr.getClassImprint().getName()+""+sgr.getId();
   
   rec.setAttribute("desc", title);

   rec.setImprint(sgr);
   
   resultGrid.addData(rec);
  }
  
  statusBar.setContents( "Objects: "+res.getTotalObjects() );
 }


 
// private void renderResultList(final VLayout lay, final Report smpls)
// {
//  if( smpls.getObjects().size() < 50 )
//  {
//   renderSampleList(lay,smpls);
//   return;
//  }
//  
//  final Window waitW = new Window();
//  
//  waitW.setHeight(100);
//  waitW.setWidth(250);
//  waitW.setShowMinimizeButton(false);  
//  waitW.setIsModal(true);  
//  waitW.setShowModalMask(true);  
//  waitW.centerInPage();
//
//  Label msg = new Label("<b>Please wait for result rendering</b>");
//  msg.setAlign(Alignment.CENTER);
//  
//  waitW.addItem(msg);
//  
//  waitW.addDrawHandler(new DrawHandler()
//  {
//   @Override
//   public void onDraw(DrawEvent event)
//   {
//    DeferredCommand.addCommand(new Command()
//    {
//     @Override
//     public void execute()
//     {
//      renderSampleList(lay,smpls);
//      waitW.destroy();
//     }
//    });
//   }
//  });
//  
//  waitW.show();
//  
// }

 
 
 private class GroupsList extends ListGrid
 {
  @Override
  protected Canvas getExpansionComponent(final ListGridRecord record)
  {
//   Canvas details = new RootObjectDetailsPanel(record.getAttributeAsRecordArray("details")[0], query, searchAtNames, searchAtValues );
//   
//   record.setAttribute("_panel", details);

   Canvas details = new ObjectImprintViewPanelHTML(((ImprintGridRecord)record).getImprint(), 2, "popup", "ShowObject");
   
   return details;
  }
 }
 
}
