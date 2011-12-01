package uk.ac.ebi.age.admin.client.ui.module.submission;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.ebi.age.admin.client.AgeAdminService;
import uk.ac.ebi.age.admin.client.ui.PlacingManager;
import uk.ac.ebi.age.admin.client.ui.module.log.LogWindow;
import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.ext.authz.TagRef;
import uk.ac.ebi.age.ext.log.SimpleLogNode;
import uk.ac.ebi.age.ext.submission.DataModuleMeta;
import uk.ac.ebi.age.ext.submission.FileAttachmentMeta;
import uk.ac.ebi.age.ext.submission.SubmissionMeta;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Record;
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
import com.smartgwt.client.widgets.viewer.DetailViewerField;

public class SubmissionDetailsPanel extends VLayout
{
 private Map<String, DetailViewer> dvMap = new HashMap<String, DetailViewer>();

 public SubmissionDetailsPanel(ListGridRecord record)
 {
  setMembersMargin(8);
  setPadding(8);
  
  final SubmissionMeta simp = (SubmissionMeta)record.getAttributeAsObject("__obj");

//  DataSource ds = SubmissionConstants.createSubmissionDataSource();
//  
//  ds.setClientOnly(true);

 
  record.setAttribute(SubmissionConstants.TAGS.name(), tagList(simp.getTags())
    +" <a class='el' href='javascript:linkClicked(&quot;"+SubmissionConstants.SUBMISSION_TAG_LINK+"&quot;,&quot;C"+simp.getId()+"&quot;)'>manage tags</a>");

//  ds.addData( record );
  
  DetailViewer submissionDetails = new DetailViewer();
  submissionDetails.setWidth("90%");
//  submissionDetails.setDataSource(ds);
  submissionDetails.setStyleName("submissionDetails");
  submissionDetails.setData( new Record[]{ record } );

  submissionDetails.setFields(
    new DetailViewerField(SubmissionConstants.SUBM_ID.name(), SubmissionConstants.SUBM_ID.title()),
    new DetailViewerField(SubmissionConstants.COMM.name(), SubmissionConstants.COMM.title()),
    new DetailViewerField(SubmissionConstants.CRTR.name(), SubmissionConstants.CRTR.title()),
    new DetailViewerField(SubmissionConstants.MDFR.name(), SubmissionConstants.MDFR.title()),
    new DetailViewerField(SubmissionConstants.CTIME.name(), SubmissionConstants.CTIME.title()),
    new DetailViewerField(SubmissionConstants.MTIME.name(), SubmissionConstants.MTIME.title()),
    new DetailViewerField(SubmissionConstants.TAGS.name(), SubmissionConstants.TAGS.title()),
    new DetailViewerField(SubmissionConstants.STS.name(), SubmissionConstants.STS.title())
    );
  
  addMember(submissionDetails);
  
  dvMap.put("C"+simp.getId(), submissionDetails);
  
  List<DataModuleMeta> mods = simp.getDataModules();
  
  if( mods != null )
  {

   for(DataModuleMeta dmImp : mods)
   {
//    DataSource dmds = SubmissionConstants.createDataModuleDataSource();
//    dmds.setClientOnly(true);

    String dvId = "M"+simp.getId().length()+":"+simp.getId()+dmImp.getId();
    
    ListGridRecord rec = new ListGridRecord();

    rec.setAttribute(SubmissionConstants.MOD_ID.name(), dmImp.getId());

    rec.setAttribute(SubmissionConstants.COMM.name(), dmImp.getDescription());
    rec.setAttribute(SubmissionConstants.CRTR.name(), dmImp.getSubmitter());
    rec.setAttribute(SubmissionConstants.MDFR.name(), dmImp.getModifier());
    rec.setAttribute(SubmissionConstants.CTIME.name(),
      DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(new Date(dmImp.getSubmissionTime())));
    rec.setAttribute(SubmissionConstants.MTIME.name(),
      DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format(new Date(dmImp.getModificationTime())));
    rec.setAttribute(SubmissionConstants.TAGS.name(), tagList(dmImp.getTags())
      +" <a class='el' href='javascript:linkClicked(&quot;"+SubmissionConstants.SUBMISSION_TAG_LINK+"&quot;,&quot;"+dvId+"&quot;)'>manage tags</a>");
    rec.setAttribute(SubmissionConstants.SRC_FILE.name(), "<a target='_blank' href='download?"
      +Constants.downloadHandlerParameter+"="+Constants.documentRequestSubject
      +"&"+Constants.clusterIdParameter+"="+simp.getId()
      +"&"+Constants.documentIdParameter+"="+dmImp.getId()
      +"&"+Constants.versionParameter+"="+dmImp.getDocVersion()
      +"'>"+dmImp.getId()+"</a>"
      );


    DetailViewer dv = new DetailViewer();
    dv.setWidth("70%");
    dv.setStyleName("moduleDetails");

    
    dv.setFields(
      new DetailViewerField(SubmissionConstants.MOD_ID.name(), SubmissionConstants.MOD_ID.title()),
      new DetailViewerField(SubmissionConstants.COMM.name(), SubmissionConstants.COMM.title()),
      new DetailViewerField(SubmissionConstants.CRTR.name(), SubmissionConstants.CRTR.title()),
      new DetailViewerField(SubmissionConstants.MDFR.name(), SubmissionConstants.MDFR.title()),
      new DetailViewerField(SubmissionConstants.CTIME.name(), SubmissionConstants.CTIME.title()),
      new DetailViewerField(SubmissionConstants.MTIME.name(), SubmissionConstants.MTIME.title()),
      new DetailViewerField(SubmissionConstants.TAGS.name(), SubmissionConstants.TAGS.title()),
      new DetailViewerField(SubmissionConstants.SRC_FILE.name(), SubmissionConstants.STS.title())
      );
    
    dv.setData( new Record[] {rec} );

    addMember(dv);

    dvMap.put(dvId, dv);
    
   }

  }
  
  if( simp.getAttachments() != null )
  {
   for(FileAttachmentMeta faImp : simp.getAttachments())
   {
    String dvId = "A"+simp.getId().length()+":"+simp.getId()+faImp.getId();

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
    rec.setAttribute(SubmissionConstants.TAGS.name(), tagList(faImp.getTags())
      +" <a class='el' href='javascript:linkClicked(&quot;"+SubmissionConstants.SUBMISSION_TAG_LINK+"&quot;,&quot;"+dvId+"&quot;)'>manage tags</a>");
    rec.setAttribute(SubmissionConstants.SRC_FILE.name(), "<a target='_blank' href='download?"
      +Constants.downloadHandlerParameter+"="+Constants.attachmentRequestSubject
      +"&"+Constants.clusterIdParameter+"="+simp.getId()
      +"&"+Constants.fileIdParameter+"="+faImp.getId()
      +"&"+Constants.versionParameter+"="+faImp.getFileVersion()
      +"'>"+faImp.getId()+"</a>"
      );


    DetailViewer dv = new DetailViewer();
    dv.setWidth("70%");
    dv.setStyleName("fileDetails");

    dv.setFields(
      new DetailViewerField(SubmissionConstants.FILE_ID.name(), SubmissionConstants.FILE_ID.title()),
      new DetailViewerField(SubmissionConstants.COMM.name(), SubmissionConstants.COMM.title()),
      new DetailViewerField(SubmissionConstants.VIS.name(), SubmissionConstants.VIS.title()),
      new DetailViewerField(SubmissionConstants.CRTR.name(), SubmissionConstants.CRTR.title()),
      new DetailViewerField(SubmissionConstants.MDFR.name(), SubmissionConstants.MDFR.title()),
      new DetailViewerField(SubmissionConstants.CTIME.name(), SubmissionConstants.CTIME.title()),
      new DetailViewerField(SubmissionConstants.MTIME.name(), SubmissionConstants.MTIME.title()),
      new DetailViewerField(SubmissionConstants.TAGS.name(), SubmissionConstants.TAGS.title()),
      new DetailViewerField(SubmissionConstants.SRC_FILE.name(), SubmissionConstants.STS.title())
      );
    
    dv.setData( new Record[] {rec} );


    addMember(dv);

    dvMap.put(dvId, dv);

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
  
  Button trank = new Button("Tranklucate");

  trank.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    SC.ask("Tranklucate submission", "Do you really want to tranklucate (wipe out) this submission", new BooleanCallback()
    {
     
     @Override
     public void execute(Boolean value)
     {
      if( value != null && value )
      {
       AgeAdminService.Util.getInstance().tranklucateSubmission(simp.getId(), new AsyncCallback<SimpleLogNode>()
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
  
  btLay.addMember(trank);
  
  addMember(btLay);
 }

 public static String tagList( Collection<TagRef> tags )
 {
  String tagStr = "";
  
  if( tags != null )
  {
   StringBuilder sb = new StringBuilder();
   
   boolean first = true;
   for( TagRef tr : tags )
   {
    if( ! first )
     sb.append("; ");
    else
     first=false;
    
    sb.append('[').append(tr.getClassiferName()).append(':').append(tr.getTagName());
    
    if( tr.getTagValue() != null )
     sb.append('=').append(tr.getTagValue());
    
    sb.append(']');
   } 

   tagStr = sb.toString();
  } 
  
  return tagStr;
 }
 
 public void setSubmissionTags(String id, Collection<TagRef> tags)
 {
  DetailViewer dv = dvMap.get(id);
  
  if( dv == null )
   return;
  
  Record record = dv.getRecordList().get(0);
  
  record.setAttribute(SubmissionConstants.TAGS.name(), tagList(tags)
    +" <a class='el' href='javascript:linkClicked(&quot;"+SubmissionConstants.SUBMISSION_TAG_LINK+"&quot;,&quot;"+id+"&quot;)'>manage tags</a>");
  
  dv.setData( new Record[]{ record } );
 }

}
