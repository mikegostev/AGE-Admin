package uk.ac.ebi.age.admin.client.ui.module.submission;

import java.util.Date;
import java.util.List;

import uk.ac.ebi.age.admin.client.AgeAdminService;
import uk.ac.ebi.age.admin.client.ui.PlacingManager;
import uk.ac.ebi.age.admin.client.ui.module.log.LogWindow;
import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.ext.log.SimpleLogNode;
import uk.ac.ebi.age.ext.submission.DataModuleMeta;
import uk.ac.ebi.age.ext.submission.FileAttachmentMeta;
import uk.ac.ebi.age.ext.submission.SubmissionMeta;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.viewer.DetailViewer;

public class SubmissionDetailsPanel extends VLayout
{

 public SubmissionDetailsPanel(ListGridRecord record)
 {
  setMembersMargin(8);
  setPadding(8);
  
  DataSource ds = SubmissionConstants.createSubmissionDataSource();
  
  ds.setClientOnly(true);

  ds.addData( record );
  
  DetailViewer dv = new DetailViewer();
  dv.setWidth("90%");
  dv.setDataSource(ds);
  dv.setStyleName("submissionDetails");
  
  dv.setAutoFetchData(true);

  addMember(dv);
  
  final SubmissionMeta simp = (SubmissionMeta)record.getAttributeAsObject("__obj");
  
  List<DataModuleMeta> mods = simp.getDataModules();
  
  if( mods != null )
  {

   for(DataModuleMeta dmImp : mods)
   {
    DataSource dmds = SubmissionConstants.createDataModuleDataSource();
    dmds.setClientOnly(true);

    ListGridRecord rec = new ListGridRecord();

    rec.setAttribute(SubmissionConstants.MOD_ID.name(), dmImp.getId());

    rec.setAttribute(SubmissionConstants.COMM.name(), dmImp.getDescription());
    rec.setAttribute(SubmissionConstants.CRTR.name(), dmImp.getSubmitter());
    rec.setAttribute(SubmissionConstants.MDFR.name(), dmImp.getModifier());
    rec.setAttribute(SubmissionConstants.CTIME.name(),
      DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(new Date(dmImp.getSubmissionTime())));
    rec.setAttribute(SubmissionConstants.MTIME.name(),
      DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(new Date(dmImp.getModificationTime())));
    rec.setAttribute(SubmissionConstants.SRC_FILE.name(), "<a target='_blank' href='download?"
      +Constants.downloadHandlerParameter+"="+Constants.documentRequestSubject
      +"&"+Constants.clusterIdParameter+"="+simp.getId()
      +"&"+Constants.documentIdParameter+"="+dmImp.getId()
      +"&"+Constants.versionParameter+"="+dmImp.getDocVersion()
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
   for(FileAttachmentMeta faImp : simp.getAttachments())
   {
    DataSource dmds = SubmissionConstants.createAttachmentDataSource();
    dmds.setClientOnly(true);

    ListGridRecord rec = new ListGridRecord();

    rec.setAttribute(SubmissionConstants.FILE_ID.name(), faImp.getId());

    rec.setAttribute(SubmissionConstants.COMM.name(), faImp.getDescription());
    rec.setAttribute(SubmissionConstants.VIS.name(), faImp.isGlobal()?"Global":"Cluster");
    rec.setAttribute(SubmissionConstants.CRTR.name(), faImp.getSubmitter());
    rec.setAttribute(SubmissionConstants.MDFR.name(), faImp.getModifier());
    rec.setAttribute(SubmissionConstants.CTIME.name(),
      DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(new Date(faImp.getSubmissionTime())));
    rec.setAttribute(SubmissionConstants.MTIME.name(),
      DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(new Date(faImp.getModificationTime())));
//    rec.setAttribute(SubmissionFields.SRC_FILE.name(), dmImp.getId());
    rec.setAttribute(SubmissionConstants.SRC_FILE.name(), "<a target='_blank' href='download?"
      +Constants.downloadHandlerParameter+"="+Constants.attachmentRequestSubject
      +"&"+Constants.clusterIdParameter+"="+simp.getId()
      +"&"+Constants.fileIdParameter+"="+faImp.getId()
      +"&"+Constants.versionParameter+"="+faImp.getFileVersion()
      +"'>"+faImp.getId()+"</a>"
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
  
  HLayout btLay = new HLayout();
  btLay.setMembersMargin(10);
  btLay.setAlign(Alignment.RIGHT);
  btLay.setWidth100();
  
  Button b = new Button("Update");
  
  b.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    PlacingManager.placeWidget(new SubmissionUpdatePanelGWT( simp ), "Update");
   }
  });
  
  btLay.addMember(b);
  
  if( simp.getSubmissionTime() != simp.getModificationTime() )
  {
   Button hb = new Button("History");
   
   hb.addClickHandler(new ClickHandler()
   {
    @Override
    public void onClick(ClickEvent event)
    {
     PlacingManager.placeWidget(new SubmissionHistoryPanel( simp.getId() ), "Submission "+simp.getId()+" history");
    }
   });
   
   btLay.addMember(hb);
  }
   
  if( simp.isRemoved() )
  {
   Button rmb = new Button("Restore");

   rmb.addClickHandler(new ClickHandler()
   {
    @Override
    public void onClick(ClickEvent event)
    {
     SC.ask("Restore submission", "Do you really want to restorethis submission", new BooleanCallback()
     {
      
      @Override
      public void execute(Boolean value)
      {
       if( value != null && value )
       {
        AgeAdminService.Util.getInstance().restoreSubmission(simp.getId(), new AsyncCallback<SimpleLogNode>()
        {

         @Override
         public void onSuccess( SimpleLogNode res )
         {
          SimpleLogNode.setLevels( res );
          new LogWindow("Submission restoration log",res).show();
         }

         @Override
         public void onFailure(Throwable caught)
         {
          SC.say("Operation error: "+caught.getMessage());
         }
        });
       }     
       }       
      });
     }
   });
   
   btLay.addMember(rmb);  }
  else
  {
   Button rmb = new Button("Delete");

   rmb.addClickHandler(new ClickHandler()
   {
    @Override
    public void onClick(ClickEvent event)
    {
     SC.ask("Delete submission", "Do you really want to delete this submission", new BooleanCallback()
     {
      
      @Override
      public void execute(Boolean value)
      {
       if( value != null && value )
       {
        AgeAdminService.Util.getInstance().deleteSubmission(simp.getId(), new AsyncCallback<SimpleLogNode>()
        {

         @Override
         public void onSuccess( SimpleLogNode res )
         {
          SimpleLogNode.setLevels( res );
          new LogWindow("Submission deletion log", res).show();
         }

         @Override
         public void onFailure(Throwable caught)
         {
          SC.say("Operation error: "+caught.getMessage());
         }
        });
       }     
       }       
      });
     }
   });
   
   btLay.addMember(rmb);
  }
  
  
  
  addMember(btLay);
 }

}
