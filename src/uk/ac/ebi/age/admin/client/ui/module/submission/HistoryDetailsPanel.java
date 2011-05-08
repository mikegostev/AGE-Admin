package uk.ac.ebi.age.admin.client.ui.module.submission;

import java.util.Date;

import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.ext.submission.AttachmentDiff;
import uk.ac.ebi.age.ext.submission.DataModuleDiff;
import uk.ac.ebi.age.ext.submission.HistoryEntry;
import uk.ac.ebi.age.ext.submission.Status;
import uk.ac.ebi.age.ext.submission.SubmissionDiff;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.viewer.DetailViewer;

public class HistoryDetailsPanel extends VLayout
{

 public HistoryDetailsPanel(HistoryEntry hEnt)
 {
  setMembersMargin(8);
  setPadding(8);
  
  DataSource ds = HistoryFields.createSubmissionDiffDataSource();
  
  ds.setClientOnly(true);

  ListGridRecord rec = new ListGridRecord();

  SubmissionDiff sDiff = hEnt.getDiff();

  DetailViewer dv = new DetailViewer();
  dv.setWidth("90%");
  dv.setDataSource(ds);
  dv.setStyleName("groupDetails");
  
  dv.setAutoFetchData(true);
  dv.setRecordsPerBlock(2);
  
  ListGridRecord chgRec = null;
  
  if( sDiff.isDescriptionChanged() )
  {
   chgRec = new ListGridRecord();
   chgRec.setAttribute(HistoryFields.COMM.name(), "CHANGED" );
  }
  
  
  rec.setAttribute(HistoryFields.SUBM_ID.name(), sDiff.getId() );
  rec.setAttribute(HistoryFields.COMM.name(), sDiff.getDescription() );
  rec.setAttribute(HistoryFields.CTIME.name(),
    DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(new Date(sDiff.getCreationTime())));
  rec.setAttribute(HistoryFields.MTIME.name(),
    DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(new Date(sDiff.getModificationTime())));
  rec.setAttribute(HistoryFields.CRTR.name(), sDiff.getCreator() );
  rec.setAttribute(HistoryFields.MDFR.name(), sDiff.getModifier() );
  
  
  if( chgRec != null )
   ds.addData( chgRec );
  ds.addData( rec );
  


  addMember(dv);
  
  if( sDiff.getDataModuleDiffs() != null )
  {
   for( DataModuleDiff dmd : sDiff.getDataModuleDiffs() )
   {
    ds = HistoryFields.createModuleDiffDataSource();
    
    ds.setClientOnly(true);

    rec = new ListGridRecord();

    if( dmd.isMetaChanged() || dmd.isDataChanged() )
    {
     if( dmd.isMetaChanged()  )
      rec.setAttribute(HistoryFields.COMM.name(), "&#8730;" );

     if( dmd.isDataChanged()  )
      rec.setAttribute(HistoryFields.SRC_FILE.name(), "&#8730;" );
     
//     ds.addData( rec );

     rec = new ListGridRecord();
    }
    
    
    rec.setAttribute(HistoryFields.STS.name(), getStatusDesc( dmd.getStatus() ) );
    rec.setAttribute(HistoryFields.MOD_ID.name(), dmd.getId() );
    rec.setAttribute(HistoryFields.COMM.name(), dmd.getDescription() );
    rec.setAttribute(HistoryFields.CTIME.name(),
      DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(new Date(dmd.getCreationTime())));
    rec.setAttribute(HistoryFields.MTIME.name(),
      DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(new Date(dmd.getModificationTime())));
    rec.setAttribute(HistoryFields.CRTR.name(), dmd.getCreator() );
    rec.setAttribute(HistoryFields.CRTR.name(), dmd.getModifier() );
    rec.setAttribute(SubmissionFields.SRC_FILE.name(), "<a target='_blank' href='download?"
      +Constants.downloadHandlerParameter+"="+Constants.documentRequestSubject
      +"&"+Constants.clusterIdParameter+"="+sDiff.getId()
      +"&"+Constants.documentIdParameter+"="+dmd.getId()
      +"&"+Constants.versionParameter+"="+dmd.getNewDocumentVersion()
      +"'>"+dmd.getId()+"</a>"
      );

    
    ds.addData( rec );
    
    dv = new DetailViewer();
    dv.setWidth("70%");
    dv.setDataSource(ds);
    dv.setStyleName("moduleDetails");
    
    dv.setAutoFetchData(true);

    addMember(dv);
    
    ModuleDetails mdd = new ModuleDetails(dmd, sDiff.getId());
    mdd.setWidth("70%");
    addMember( mdd );
   }
  }
  
  if( sDiff.getAttachmentDiffs() != null )
  {
   for( AttachmentDiff atd : sDiff.getAttachmentDiffs() )
   {
    ds = HistoryFields.createAttachmentDiffDataSource();
    
    ds.setClientOnly(true);

    rec = new ListGridRecord();

    if( atd.isMetaChanged() || atd.isDataChanged() || atd.isVisibilityChanged() )
    {
     if( atd.isMetaChanged()  )
      rec.setAttribute(HistoryFields.COMM.name(), "&#8730;" );

     if( atd.isDataChanged()  )
      rec.setAttribute(HistoryFields.SRC_FILE.name(), "&#8730;" );

     if( atd.isVisibilityChanged()  )
      rec.setAttribute(HistoryFields.VIS.name(), "&#8730;" );
     
     ds.addData( rec );

     rec = new ListGridRecord();
    }
    
    
    rec.setAttribute(HistoryFields.STS.name(), getStatusDesc( atd.getStatus() ) );
    rec.setAttribute(HistoryFields.FILE_ID.name(), atd.getId() );
    rec.setAttribute(HistoryFields.COMM.name(), atd.getDescription() );
    rec.setAttribute(SubmissionFields.VIS.name(), atd.isGlobal()?"Global":"Cluster");
    rec.setAttribute(HistoryFields.CTIME.name(),
      DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(new Date(atd.getCreationTime())));
    rec.setAttribute(HistoryFields.MTIME.name(),
      DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(new Date(atd.getModificationTime())));
    rec.setAttribute(HistoryFields.CRTR.name(), atd.getCreator() );
    rec.setAttribute(HistoryFields.CRTR.name(), atd.getModifier() );
    rec.setAttribute(SubmissionFields.SRC_FILE.name(), "<a target='_blank' href='download?"
      +Constants.downloadHandlerParameter+"="+Constants.attachmentRequestSubject
      +"&"+Constants.clusterIdParameter+"="+sDiff.getId()
      +"&"+Constants.fileIdParameter+"="+atd.getId()
      +"&"+Constants.versionParameter+"="+atd.getNewFileVersion()
      +"'>"+atd.getId()+"</a>"
      );

    
    ds.addData( rec );
    
    dv = new DetailViewer();
    dv.setWidth("70%");
    dv.setDataSource(ds);
    dv.setStyleName("moduleDetails");
    
    dv.setAutoFetchData(true);

    addMember(dv);
   }
  }
 }

 private static String getStatusDesc(Status status)
 {
  switch(status)
  {
   case DELETE:
    return "Deleted";
   case NEW:
    return "New";
   case UPDATE:
    return "Changed";
   case KEEP:
    return "Not changed";
  }
  
  return "Unknown";
 }

 private static class ModuleDetails extends ListGrid
 {
  private DataModuleDiff modDiff;
  
  ModuleDetails( DataModuleDiff dmd, String sbmId )
  {
   setShowAllRecords(true);  
   setWrapCells(true);
   setFixedRecordHeights(false);
   setCellHeight(20);
   
   setAutoFetchData(true);
   setShowCustomScrollbars(false);
   
   setBodyOverflow(Overflow.VISIBLE);
   setOverflow(Overflow.VISIBLE);
   
   setLeaveScrollbarGap(false);

   setShowRollOver(false);
   setShowSelectedStyle(false);
   
   setStyleName("propViewer");
   
   modDiff = dmd;
   
   DataSource ds = new DataSource();
   
   ds.setClientOnly(true);
   
   ds.addField(new DataSourceTextField("name", "name"));
   ds.addField(new DataSourceTextField("value", "value"));
   ds.addField(new DataSourceTextField("changed", "changed"));

   setDataSource(ds);
   setShowHeader(false);

   ListGridField nmField = new ListGridField("name",100);
   nmField.setAutoFitWidth(true);
   nmField.setBaseStyle("dataModulePropName");
   nmField.setAlign(Alignment.RIGHT);
  
   ListGridField valField = new ListGridField("value");
   valField.setBaseStyle("propValue");
  
   ListGridField chgField = new ListGridField("changed",25);
   chgField.setBaseStyle("propChanged");

   setFields(nmField,valField,chgField);
   
   ListGridRecord rec = new ListGridRecord();
   
   rec.setAttribute( "name", HistoryFields.STS.title()+":" );
   rec.setAttribute( "value", getStatusDesc( dmd.getStatus() ) );
   rec.setAttribute( "changed", "" );

   addData(rec);

   rec = new ListGridRecord();
   
   rec.setAttribute( "name", HistoryFields.MOD_ID.title()+":" );
   rec.setAttribute( "value", dmd.getId() );
   rec.setAttribute( "changed", "" );

   addData(rec);

   rec = new ListGridRecord();
   
   rec.setAttribute( "name", HistoryFields.COMM.title()+":" );
   rec.setAttribute( "value", dmd.getDescription() );
   rec.setAttribute( "changed", dmd.isMetaChanged()?"&#8730;":"" );

   addData(rec);

   rec = new ListGridRecord();
   
   rec.setAttribute( "name", HistoryFields.CTIME.title()+":" );
   rec.setAttribute( "value", DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(new Date(dmd.getCreationTime())) );
   rec.setAttribute( "changed", "" );

   addData(rec);

   rec = new ListGridRecord();
   
   rec.setAttribute( "name", HistoryFields.MTIME.title()+":" );
   rec.setAttribute( "value", DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(new Date(dmd.getModificationTime())) );
   rec.setAttribute( "changed", "" );

   addData(rec);

   rec = new ListGridRecord();
   
   rec.setAttribute( "name", HistoryFields.CRTR.title()+":" );
   rec.setAttribute( "value", dmd.getCreator() );
   rec.setAttribute( "changed", "" );

   addData(rec);

   rec = new ListGridRecord();
   
   rec.setAttribute( "name", HistoryFields.MDFR.title()+":" );
   rec.setAttribute( "value", dmd.getCreator() );
   rec.setAttribute( "changed", "" );

   addData(rec);
   
   rec = new ListGridRecord();
   
   String fileRef = "<a target='_blank' href='download?"
    +Constants.downloadHandlerParameter+"="+Constants.documentRequestSubject
    +"&"+Constants.clusterIdParameter+"="+sbmId
    +"&"+Constants.documentIdParameter+"="+dmd.getId()
    +"&"+Constants.versionParameter+"="+dmd.getNewDocumentVersion()
    +"'>"+dmd.getId()+"</a>" ;
   
   if( dmd.isDataChanged() )
   {
    fileRef += " (<a target='_blank' href='download?"
     +Constants.downloadHandlerParameter+"="+Constants.documentRequestSubject
     +"&"+Constants.clusterIdParameter+"="+sbmId
     +"&"+Constants.documentIdParameter+"="+dmd.getId()
     +"&"+Constants.versionParameter+"="+dmd.getOldDocumentVersion()
     +"'>was</a>)" ;
   }
   
   rec.setAttribute( "name", HistoryFields.SRC_FILE.title()+":" );
   rec.setAttribute( "value", fileRef);
   rec.setAttribute( "changed", dmd.isDataChanged()?"&#8730;":"" );

   addData(rec);

  }
  
  protected String getCellStyle(ListGridRecord record,
    int rowNum,
    int colNum)
  {
   if( colNum != 0 && ( ( rowNum == 2 && modDiff.isMetaChanged() ) || ( rowNum == 7 && modDiff.isDataChanged() )  ) )
    return "changedProperty&#32;"+super.getCellStyle(record,rowNum,colNum);
   else
    return super.getCellStyle(record,rowNum,colNum);
  }
 }
}
