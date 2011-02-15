package uk.ac.ebi.age.admin.client.ui.module.submission;

import uk.ac.ebi.age.admin.shared.submission.SubmissionImprint;
import uk.ac.ebi.age.admin.shared.submission.SubmissionReport;
import uk.ac.ebi.age.ui.client.LinkManager;
import uk.ac.ebi.age.ui.client.module.PagingRuler;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.ExpansionMode;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordCollapseEvent;
import com.smartgwt.client.widgets.grid.events.RecordCollapseHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class SubmissionsPane extends VLayout
{
 public final static int MAX_SUBMISSIONS_PER_PAGE=20;
 
 private String query;
 
 private ListGrid resultGrid = new SubmissionList();
 private PagingRuler pagingRuler = new PagingRuler("submPage");
 
 public SubmissionsPane()
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
  
//  resultGrid.setBodyOverflow(Overflow.VISIBLE);
  resultGrid.setOverflow(Overflow.VISIBLE);
  
  resultGrid.setStyleName("reportGrid");
  
//  setBaseStyle("reportGrid");
  
  DataSource ds = new DataSource();
  
  ds.setClientOnly(true);
  
  ds.addField(new DataSourceTextField("id", "ID"));
  ds.addField(new DataSourceTextField("desc", "Description", 2000));
  ds.addField(new DataSourceDateField("ctime", "Submitted"));
  ds.addField(new DataSourceDateField("mtime", "Modified"));
  ds.addField(new DataSourceTextField("submitter", "ID"));
  
//  setDataSource(ds);
  
  resultGrid.setCanExpandRecords(true); 
  
  ListGridField idField = new ListGridField("id","ID", 200);  
  ListGridField descField = new ListGridField("desc","Description");
//  ListGridField propField = new ListGridField("prop","AdditionalProp");
  
//  propField.setHidden(true);
  
//  idField.setWidth(200);
     
  resultGrid.setFields(idField, descField );
  resultGrid.setExpansionMode(ExpansionMode.DETAIL_FIELD);
  
  pagingRuler.setVisible(false);
  
  addMember(pagingRuler);
  addMember(resultGrid);
  
  resultGrid.addRecordCollapseHandler( new RecordCollapseHandler()
  {
   
   @Override
   public void onRecordCollapse(RecordCollapseEvent event)
   {
    LinkManager.getInstance().removeLinkClickListener(event.getRecord().getAttribute("id"));
   }
  });
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
 
 public void showResult( SubmissionReport res, String qry, int cpage )
 {
  query = qry;
  
  resultGrid.selectAllRecords();
  resultGrid.removeSelectedData();
  
  if( res.getTotalSubmissions() > MAX_SUBMISSIONS_PER_PAGE )
  {
   pagingRuler.setContents(cpage, res.getTotalSubmissions(), MAX_SUBMISSIONS_PER_PAGE, null, null);
   pagingRuler.setVisible(true);
  }
  else
   pagingRuler.setVisible(false);
  
  for( SubmissionImprint sgr : res.getObjects() )
  {
   ListGridRecord rec = new ListGridRecord();
   
   rec.setAttribute("id", sgr.getId());
   rec.setAttribute("submitter", sgr.getSubmitter());
   rec.setAttribute("modifier", sgr.getModifier());
   rec.setAttribute("desc", sgr.getDescription());
   rec.setAttribute("ctime", sgr.getCtime());
   rec.setAttribute("mtime", sgr.getMtime());

   resultGrid.addData(rec);
  }
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

 
 
 private class SubmissionList extends ListGrid
 {
  protected Canvas getExpansionComponent(final ListGridRecord record)
  {
   return new SubmissionDetailsPanel( record );
  }
 }
 
}
