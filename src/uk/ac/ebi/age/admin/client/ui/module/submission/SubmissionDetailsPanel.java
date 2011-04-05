package uk.ac.ebi.age.admin.client.ui.module.submission;

import java.util.Date;
import java.util.List;

import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.ext.submission.DataModuleMeta;
import uk.ac.ebi.age.ext.submission.FileAttachmentMeta;
import uk.ac.ebi.age.ext.submission.SubmissionMeta;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.viewer.DetailViewer;

public class SubmissionDetailsPanel extends VLayout
{

 public SubmissionDetailsPanel(ListGridRecord record)
 {
  setMembersMargin(8);
  setPadding(8);
  
  DataSource ds = SubmissionFields.createSubmissionDataSource();
  
  ds.setClientOnly(true);

  ds.addData( record );
  
  DetailViewer dv = new DetailViewer();
  dv.setWidth("90%");
  dv.setDataSource(ds);
  dv.setStyleName("groupDetails");
  
  dv.setAutoFetchData(true);

  addMember(dv);
  
  SubmissionMeta simp = (SubmissionMeta)record.getAttributeAsObject("__obj");
  
  List<DataModuleMeta> mods = simp.getDataModules();
  
  if( mods != null )
  {

   for(DataModuleMeta dmImp : mods)
   {
    DataSource dmds = SubmissionFields.createDataModuleDataSource();
    dmds.setClientOnly(true);

    ListGridRecord rec = new ListGridRecord();

    rec.setAttribute(SubmissionFields.MOD_ID.name(), dmImp.getId());

    rec.setAttribute(SubmissionFields.COMM.name(), dmImp.getDescription());
    rec.setAttribute(SubmissionFields.CRTR.name(), dmImp.getSubmitter());
    rec.setAttribute(SubmissionFields.MDFR.name(), dmImp.getModifier());
    rec.setAttribute(SubmissionFields.CTIME.name(),
      DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(new Date(dmImp.getSubmissionTime())));
    rec.setAttribute(SubmissionFields.MTIME.name(),
      DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(new Date(dmImp.getModificationTime())));
    rec.setAttribute(SubmissionFields.SRC_FILE.name(), "<a target='_blank' href='download?"
      +Constants.downloadHandlerParameter+"="+Constants.documentRequestSubject
      +"&"+Constants.clusterIdParameter+"="+simp.getId()
      +"&"+Constants.documentIdParameter+"="+dmImp.getId()
      +"&"+Constants.versionParameter+"="+dmImp.getModificationTime()
      +"'>"+dmImp.getId()+"</a>"
      );

    dmds.addData(rec);

    dv = new DetailViewer();
    dv.setWidth("70%");
    dv.setDataSource(dmds);
    dv.setStyleName("moduleDetails");

    dv.setAutoFetchData(true);

    addMember(dv);

   }

  }
  
  if( simp.getAttachments() != null )
  {
   for(FileAttachmentMeta dmImp : simp.getAttachments())
   {
    DataSource dmds = SubmissionFields.createAttachmentDataSource();
    dmds.setClientOnly(true);

    ListGridRecord rec = new ListGridRecord();

    rec.setAttribute(SubmissionFields.FILE_ID.name(), dmImp.getId());

    rec.setAttribute(SubmissionFields.COMM.name(), dmImp.getDescription());
    rec.setAttribute(SubmissionFields.CRTR.name(), dmImp.getSubmitter());
    rec.setAttribute(SubmissionFields.MDFR.name(), dmImp.getModifier());
    rec.setAttribute(SubmissionFields.CTIME.name(),
      DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(new Date(dmImp.getSubmissionTime())));
    rec.setAttribute(SubmissionFields.MTIME.name(),
      DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(new Date(dmImp.getModificationTime())));
//    rec.setAttribute(SubmissionFields.SRC_FILE.name(), dmImp.getId());
    rec.setAttribute(SubmissionFields.SRC_FILE.name(), "<a target='_blank' href='download?"
      +Constants.downloadHandlerParameter+"="+Constants.attachmentRequestSubject
      +"&"+Constants.clusterIdParameter+"="+simp.getId()
      +"&"+Constants.fileIdParameter+"="+dmImp.getId()
      +"&"+Constants.versionParameter+"="+dmImp.getModificationTime()
      +"'>"+dmImp.getId()+"</a>"
      );

    dmds.addData(rec);

    dv = new DetailViewer();
    dv.setWidth("70%");
    dv.setDataSource(dmds);
    dv.setStyleName("fileDetails");

    dv.setAutoFetchData(true);

    addMember(dv);

   }
  }
//  ListGrid 
 }

}
