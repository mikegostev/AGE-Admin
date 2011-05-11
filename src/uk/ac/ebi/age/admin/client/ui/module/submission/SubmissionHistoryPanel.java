package uk.ac.ebi.age.admin.client.ui.module.submission;

import java.util.Date;
import java.util.List;

import uk.ac.ebi.age.admin.client.AgeAdminService;
import uk.ac.ebi.age.ext.submission.HistoryEntry;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;

public class SubmissionHistoryPanel extends VLayout
{
 private ListGrid resultGrid = new HistoryEntryList();


 public SubmissionHistoryPanel(String id)
 {
  
  setHeight("100%");
  
  setMargin(5);
  
  setShowEdges(true);
  setEdgeSize(6);
//  setEdgeImage("gnframe.gif");
  setEdgeMarginSize(10);
  
  resultGrid.setHeight100();
  resultGrid.setShowAllRecords(true);  
  resultGrid.setWrapCells(true);
  resultGrid.setFixedRecordHeights(false);
  resultGrid.setCellHeight(20);
  
//  resultGrid.setBodyOverflow(Overflow.VISIBLE);
  resultGrid.setOverflow(Overflow.VISIBLE);
  
  resultGrid.setStyleName("historyGrid");
  
  DataSource ds = HistoryFields.createHistoryDataSource();
  
  ds.setClientOnly(true);
  
  resultGrid.setCanExpandRecords(true); 
  
  ListGridField mtimeField = new ListGridField(HistoryFields.MTIME.name(),"M. time",100);
  ListGridField mdfrField = new ListGridField(HistoryFields.MDFR.name(),"Modifier",100);
  ListGridField descField = new ListGridField(HistoryFields.COMM.name(),"Description");
  
//  propField.setHidden(true);
  
//  idField.setWidth(200);
     
  resultGrid.setFields(mtimeField,mdfrField, descField );
//  resultGrid.setExpansionMode(ExpansionMode.DETAIL_FIELD);
  
  addMember(resultGrid);
  
  
  
  AgeAdminService.Util.getInstance().getSubmissionHistory( id, new AsyncCallback<List<HistoryEntry>>()
  {
     
     @Override
     public void onSuccess(List<HistoryEntry> result)
     {
      showHistory(result);
     }

     @Override
     public void onFailure(Throwable caught)
     {
      // TODO Auto-generated method stub
      
     }
    });
 }

 
 private void showHistory(List<HistoryEntry> result)
 {
  resultGrid.selectAllRecords();
  resultGrid.removeSelectedData();
  
  for( HistoryEntry hisEnt : result )
  {
   ListGridRecord rec = new ListGridRecord();
   
   rec.setAttribute(SubmissionConstants.MDFR.name(), hisEnt.getModifier());
   rec.setAttribute(SubmissionConstants.COMM.name(), hisEnt.getDescription());
   rec.setAttribute(SubmissionConstants.MTIME.name(), DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format( new Date(hisEnt.getModificationTime())));

   rec.setAttribute("__obj", hisEnt);
   
   resultGrid.addData(rec);
  }
 }
 

 
 private class HistoryEntryList extends ListGrid
 {
  protected Canvas getExpansionComponent(final ListGridRecord record)
  {
   HistoryEntry hEnt = (HistoryEntry) record.getAttributeAsObject("__obj");
   
   return new HistoryDetailsPanel( hEnt );
  }
 }
}
