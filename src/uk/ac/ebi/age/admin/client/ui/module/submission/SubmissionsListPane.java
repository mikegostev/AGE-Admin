package uk.ac.ebi.age.admin.client.ui.module.submission;

import java.util.Date;

import uk.ac.ebi.age.ext.submission.SubmissionMeta;
import uk.ac.ebi.age.ext.submission.SubmissionQuery;
import uk.ac.ebi.age.ext.submission.SubmissionReport;
import uk.ac.ebi.age.ui.client.module.PagingRuler;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.ExpansionMode;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;

public class SubmissionsListPane extends VLayout
{
 public final static int MAX_SUBMISSIONS_PER_PAGE=20;
 
 private SubmissionQuery query;
 
 private ListGrid resultGrid = new SubmissionList();
 private PagingRuler pagingRuler = new PagingRuler("submPage");
 
 public SubmissionsListPane()
 {
  setHeight("100%");
  
  setMargin(5);
  
  setShowEdges(true);
  setEdgeSize(6);
//  setEdgeImage("gnframe.gif");
  setEdgeMarginSize(10);
  
  resultGrid.setShowAllRecords(true);  
  resultGrid.setWrapCells(true);
  resultGrid.setFixedRecordHeights(false);
  resultGrid.setCellHeight(20);
  
//  resultGrid.setBodyOverflow(Overflow.VISIBLE);
  resultGrid.setOverflow(Overflow.VISIBLE);
  
  resultGrid.setStyleName("reportGrid");
  
  DataSource ds = SubmissionConstants.createSubmissionDataSource();
  
  ds.setClientOnly(true);
  
  resultGrid.setCanExpandRecords(true); 
  
  ListGridField idField = new ListGridField(SubmissionConstants.SUBM_ID.name(),"ID", 200);  
  ListGridField stsField = new ListGridField(SubmissionConstants.STS.name(),"Status",50);
  ListGridField descField = new ListGridField(SubmissionConstants.COMM.name(),"Description");
  ListGridField crtrField = new ListGridField(SubmissionConstants.CRTR.name(),"Submitter",100);
  ListGridField mdfrField = new ListGridField(SubmissionConstants.MDFR.name(),"Modifier",100);
  ListGridField ctimeField = new ListGridField(SubmissionConstants.CTIME.name(),"C. time",100);
  ListGridField mtimeField = new ListGridField(SubmissionConstants.MTIME.name(),"M. time",100);
//  ListGridField propField = new ListGridField("prop","AdditionalProp");
  
//  propField.setHidden(true);
  
//  idField.setWidth(200);
     
  resultGrid.setFields(idField, stsField, descField,crtrField,mdfrField,ctimeField,mtimeField );
  resultGrid.setExpansionMode(ExpansionMode.DETAIL_FIELD);
  
  pagingRuler.setVisible(false);
  
  addMember(pagingRuler);
  addMember(resultGrid);
  
//  resultGrid.addRecordCollapseHandler( new RecordCollapseHandler()
//  {
//   
//   @Override
//   public void onRecordCollapse(RecordCollapseEvent event)
//   {
//    LinkManager.getInstance().removeLinkClickListener(event.getRecord().getAttribute("id"));
//   }
//  });
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
 
 public void showResult( SubmissionReport result , SubmissionQuery qry, int cpage )
 {
  query = qry;
  
  resultGrid.selectAllRecords();
  resultGrid.removeSelectedData();
  
  if( result.getTotalSubmissions() > MAX_SUBMISSIONS_PER_PAGE )
  {
   pagingRuler.setContents(cpage, result.getTotalSubmissions(), MAX_SUBMISSIONS_PER_PAGE, null, null);
   pagingRuler.setVisible(true);
  }
  else
   pagingRuler.setVisible(false);
  
  for( SubmissionMeta sgr : result.getSubmissions() )
  {
   ListGridRecord rec = new ListGridRecord();
   
   rec.setAttribute(SubmissionConstants.SUBM_ID.name(), sgr.getId());
   rec.setAttribute(SubmissionConstants.STS.name(), sgr.isRemoved()?SubmissionConstants.RMVD.title():SubmissionConstants.ACTV.title());
   rec.setAttribute(SubmissionConstants.CRTR.name(), sgr.getSubmitter());
   rec.setAttribute(SubmissionConstants.MDFR.name(), sgr.getModifier());
   rec.setAttribute(SubmissionConstants.COMM.name(), sgr.getDescription());
   rec.setAttribute(SubmissionConstants.CTIME.name(), DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format( new Date(sgr.getSubmissionTime())));
   rec.setAttribute(SubmissionConstants.MTIME.name(), DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format( new Date(sgr.getModificationTime())));

   rec.setAttribute("__obj", sgr);
   
   resultGrid.addData(rec);
  }
 }


 
 
 
 private class SubmissionList extends ListGrid
 {
  protected Canvas getExpansionComponent(final ListGridRecord record)
  {
   return new SubmissionDetailsPanel( record );
  }
  
  protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum)
  {
   if( SubmissionConstants.RMVD.title().equals(record.getAttributeAsString(SubmissionConstants.STS.name()) ) )
    return "background-color: #FFD4D0;";
   
   return super.getCellCSSText(record, rowNum, colNum);
  }
 }
 
}
