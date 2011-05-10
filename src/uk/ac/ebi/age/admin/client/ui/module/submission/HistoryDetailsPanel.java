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

public class HistoryDetailsPanel extends VLayout
{

 public HistoryDetailsPanel(HistoryEntry hEnt)
 {
  setMembersMargin(8);
  setPadding(8);
  
  SubmissionDiff sDiff = hEnt.getDiff();

  SubmissionDetails sbd = new SubmissionDetails(sDiff);
  sbd.setWidth("90%");
  addMember( sbd );
  
  if( sDiff.getDataModuleDiffs() != null )
  {
   for( DataModuleDiff dmd : sDiff.getDataModuleDiffs() )
   {
    ModuleDetails mdd = new ModuleDetails(dmd, sDiff.getId());
    mdd.setWidth("70%");
    addMember( mdd );
   }
  }
  
  if( sDiff.getAttachmentDiffs() != null )
  {
   for( AttachmentDiff atd : sDiff.getAttachmentDiffs() )
   {
    AttachmentDetails mdd = new AttachmentDetails(atd, sDiff.getId());
    mdd.setWidth("70%");
    addMember( mdd );
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

 private static class SubmissionDetails extends ListGrid
 {
  private SubmissionDiff sbmDiff;
  
  SubmissionDetails( SubmissionDiff sbd )
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
   
   setAlternateRecordStyles(false);
   
   setBodyStyleName("propViewer");
   setStyleName("submissionPropViewer");
   
   sbmDiff = sbd;
   
   DataSource ds = new DataSource();
   
   ds.setClientOnly(true);
   
   DataSourceTextField nameField = new DataSourceTextField("name", "name");
   nameField.setPrimaryKey(true);
   ds.addField(nameField);
   ds.addField(new DataSourceTextField("value", "value"));
   ds.addField(new DataSourceTextField("changed", "changed"));

   setDataSource(ds);
   setShowHeader(false);

   ListGridField nmField = new ListGridField("name",100);
   nmField.setAutoFitWidth(true);
   nmField.setBaseStyle("submissionPropName");
   nmField.setAlign(Alignment.RIGHT);
  
   ListGridField valField = new ListGridField("value");
   valField.setBaseStyle("propValue");
  
   ListGridField chgField = new ListGridField("changed",25);
   chgField.setBaseStyle("propChanged");

   setFields(nmField,valField,chgField);
   
   ListGridRecord rec = new ListGridRecord();
   
   rec.setAttribute( "name", HistoryFields.SUBM_ID.title()+":" );
   rec.setAttribute( "value", sbd.getId() );
   rec.setAttribute( "changed", "" );

   addData(rec);

   rec = new ListGridRecord();
   
   rec.setAttribute( "name", HistoryFields.COMM.title()+":" );
   rec.setAttribute( "value", sbd.getDescription() );
   rec.setAttribute( "changed", sbd.isDescriptionChanged()?"&#8730;":"" );

   addData(rec);

   rec = new ListGridRecord();
   
   rec.setAttribute( "name", HistoryFields.CTIME.title()+":" );
   rec.setAttribute( "value", DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(new Date(sbd.getCreationTime())) );
   rec.setAttribute( "changed", "" );

   addData(rec);

   rec = new ListGridRecord();
   
   rec.setAttribute( "name", HistoryFields.MTIME.title()+":" );
   rec.setAttribute( "value", DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(new Date(sbd.getModificationTime())) );
   rec.setAttribute( "changed", "" );

   addData(rec);

   rec = new ListGridRecord();
   
   rec.setAttribute( "name", HistoryFields.CRTR.title()+":" );
   rec.setAttribute( "value", sbd.getCreator() );
   rec.setAttribute( "changed", "" );

   addData(rec);

   rec = new ListGridRecord();
   
   rec.setAttribute( "name", HistoryFields.MDFR.title()+":" );
   rec.setAttribute( "value", sbd.getCreator() );
   rec.setAttribute( "changed", "" );

   addData(rec);
   
  }
  
  protected String getCellStyle(ListGridRecord record, int rowNum, int colNum)
  {
   if( colNum != 0  && rowNum == 1 && sbmDiff.isDescriptionChanged() )
     return "changedProperty";

    return super.getCellStyle(record,rowNum,colNum);
  }
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
   
   setBodyStyleName("propViewer");
   setStyleName("modulePropViewer");
   
   modDiff = dmd;
   
   DataSource ds = new DataSource();
   
   ds.setClientOnly(true);
   
   DataSourceTextField nameField = new DataSourceTextField("name", "name");
   nameField.setPrimaryKey(true);
   ds.addField(nameField);
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
  
  protected String getCellStyle(ListGridRecord record, int rowNum, int colNum)
  {
   if( colNum != 0 )
   {
    if( ( ( rowNum == 2 && modDiff.isMetaChanged() ) || ( rowNum == 7 && modDiff.isDataChanged() )  ) )
     return "changedProperty";
    else if( rowNum == 0 )
     return "status"+modDiff.getStatus().name();
   }

    return super.getCellStyle(record,rowNum,colNum);
  }
 }
 
 private static class AttachmentDetails extends ListGrid
 {
  private AttachmentDiff attDiff;
  
  AttachmentDetails( AttachmentDiff atd, String sbmId )
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
   
   setBodyStyleName("propViewer");
   setStyleName("attachmentPropViewer");

   
   attDiff = atd;
   
   DataSource ds = new DataSource();
   
   ds.setClientOnly(true);
   
   DataSourceTextField nameField = new DataSourceTextField("name", "name");
   nameField.setPrimaryKey(true);
   ds.addField(nameField);
   ds.addField(new DataSourceTextField("value", "value"));
   ds.addField(new DataSourceTextField("changed", "changed"));

   setDataSource(ds);
   setShowHeader(false);

   ListGridField nmField = new ListGridField("name",100);
   nmField.setAutoFitWidth(true);
   nmField.setBaseStyle("attachmentPropName");
   nmField.setAlign(Alignment.RIGHT);
  
   ListGridField valField = new ListGridField("value");
   valField.setBaseStyle("propValue");
  
   ListGridField chgField = new ListGridField("changed",25);
   chgField.setBaseStyle("propChanged");

   setFields(nmField,valField,chgField);
   
   ListGridRecord rec = new ListGridRecord();
   
   rec.setAttribute( "name", HistoryFields.STS.title()+":" );
   rec.setAttribute( "value", getStatusDesc( atd.getStatus() ) );
   rec.setAttribute( "changed", "" );

   addData(rec);

   rec = new ListGridRecord();
   
   rec.setAttribute( "name", HistoryFields.FILE_ID.title()+":" );
   rec.setAttribute( "value", atd.getId() );
   rec.setAttribute( "changed", "" );

   addData(rec);

   rec = new ListGridRecord();
   
   rec.setAttribute( "name", HistoryFields.COMM.title()+":" );
   rec.setAttribute( "value", atd.getDescription() );
   rec.setAttribute( "changed", atd.isMetaChanged()?"&#8730;":"" );

   addData(rec);

   rec = new ListGridRecord();
   
   rec.setAttribute( "name", HistoryFields.VIS.title()+":" );
   rec.setAttribute( "value", atd.isGlobal()?"Global":"Cluster" );
   rec.setAttribute( "changed", atd.isVisibilityChanged()?"&#8730;":"" );

   addData(rec);

   rec = new ListGridRecord();
   
   rec.setAttribute( "name", HistoryFields.CTIME.title()+":" );
   rec.setAttribute( "value", DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(new Date(atd.getCreationTime())) );
   rec.setAttribute( "changed", "" );

   addData(rec);

   rec = new ListGridRecord();
   
   rec.setAttribute( "name", HistoryFields.MTIME.title()+":" );
   rec.setAttribute( "value", DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(new Date(atd.getModificationTime())) );
   rec.setAttribute( "changed", "" );

   addData(rec);

   rec = new ListGridRecord();
   
   rec.setAttribute( "name", HistoryFields.CRTR.title()+":" );
   rec.setAttribute( "value", atd.getCreator() );
   rec.setAttribute( "changed", "" );

   addData(rec);

   rec = new ListGridRecord();
   
   rec.setAttribute( "name", HistoryFields.MDFR.title()+":" );
   rec.setAttribute( "value", atd.getCreator() );
   rec.setAttribute( "changed", "" );

   addData(rec);
   
   rec = new ListGridRecord();
   
   String fileRef = "<a target='_blank' href='download?"
    +Constants.downloadHandlerParameter+"="+Constants.attachmentRequestSubject
    +"&"+Constants.clusterIdParameter+"="+sbmId
    +"&"+Constants.fileIdParameter+"="+atd.getId()
    +"&"+Constants.versionParameter+"="+atd.getNewFileVersion()
    +"'>"+atd.getId()+"</a>" ;
   
   if( atd.isDataChanged() )
   {
    fileRef += " (<a target='_blank' href='download?"
     +Constants.downloadHandlerParameter+"="+Constants.attachmentRequestSubject
     +"&"+Constants.clusterIdParameter+"="+sbmId
     +"&"+Constants.fileIdParameter+"="+atd.getId()
     +"&"+Constants.versionParameter+"="+atd.getOldFileVersion()
     +"'>was</a>)" ;
   }
   
   rec.setAttribute( "name", HistoryFields.SRC_FILE.title()+":" );
   rec.setAttribute( "value", fileRef);
   rec.setAttribute( "changed", atd.isDataChanged()?"&#8730;":"" );

   addData(rec);

  }
  
  protected String getCellStyle(ListGridRecord record, int rowNum, int colNum)
  {
   if( colNum != 0 )
   {
    if( ( rowNum == 2 && attDiff.isMetaChanged() ) 
      || ( rowNum == 3 && attDiff.isVisibilityChanged() ) 
      || ( rowNum == 8 && attDiff.isDataChanged() ) )
     return "changedProperty";
    else if( rowNum == 0 )
     return "status"+attDiff.getStatus().name();
   }

    return super.getCellStyle(record,rowNum,colNum);
  }
 }

}
