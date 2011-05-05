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

  if( sDiff.isDescriptionChanged() )
  {
   rec.setAttribute(HistoryFields.COMM.name(), "&#8730;" );
   
   ds.addData( rec );

   rec = new ListGridRecord();
  }
  
  
  rec.setAttribute(HistoryFields.SUBM_ID.name(), sDiff.getId() );
  rec.setAttribute(HistoryFields.COMM.name(), sDiff.getDescription() );
  rec.setAttribute(HistoryFields.CTIME.name(),
    DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(new Date(sDiff.getCreationTime())));
  rec.setAttribute(HistoryFields.MTIME.name(),
    DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(new Date(sDiff.getModificationTime())));
  rec.setAttribute(HistoryFields.CRTR.name(), sDiff.getCreator() );
  rec.setAttribute(HistoryFields.MDFR.name(), sDiff.getModifier() );
  
  
  ds.addData( rec );
  
  DetailViewer dv = new DetailViewer();
  dv.setWidth("90%");
  dv.setDataSource(ds);
  dv.setStyleName("groupDetails");
  
  dv.setAutoFetchData(true);

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
     
     ds.addData( rec );

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

 private String getStatusDesc(Status status)
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

}
