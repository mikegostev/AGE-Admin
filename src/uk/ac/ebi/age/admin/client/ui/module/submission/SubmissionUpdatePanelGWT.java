package uk.ac.ebi.age.admin.client.ui.module.submission;

import uk.ac.ebi.age.admin.client.log.LogNode;
import uk.ac.ebi.age.admin.client.ui.module.log.LogTree;
import uk.ac.ebi.age.admin.client.ui.module.submission.NewDMPanel.RemoveListener;
import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.admin.shared.SubmissionConstants;
import uk.ac.ebi.age.ext.submission.DataModuleMeta;
import uk.ac.ebi.age.ext.submission.FileAttachmentMeta;
import uk.ac.ebi.age.ext.submission.Status;
import uk.ac.ebi.age.ext.submission.SubmissionMeta;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.WidgetCanvas;
import com.smartgwt.client.widgets.layout.VLayout;

public class SubmissionUpdatePanelGWT extends VLayout
{
 
 private int n = 1;
 private long key = System.currentTimeMillis();
 private int nMods = 1;
 private VerticalPanel panel;

 private TextArea updateDescription;
 private TextArea submissionDescription;
 
 public SubmissionUpdatePanelGWT( final SubmissionMeta sbmMeta )
 {
  setAutoWidth();
  setLayoutLeftMargin(15);
  setLayoutTopMargin(15);
  setMembersMargin(15);

  setHeight100();
  setWidth100();

  setBorder("1px solid #0087BA");

  DecoratorPanel decp = new DecoratorPanel();
  decp.setHeight("100%");
//  decp.setWidth("500px");
  final WidgetCanvas wc = new WidgetCanvas(decp);
  wc.setHeight100();

  final FormPanel form = new FormPanel();
  form.setAction("upload");
  form.setEncoding(FormPanel.ENCODING_MULTIPART);
  form.setMethod(FormPanel.METHOD_POST);

//  form.setWidth("500px");

  panel = new VerticalPanel();
  panel.setSpacing(10);
  panel.setWidth("800px");
  form.setWidget(panel);

  panel.add( new Hidden(Constants.uploadHandlerParameter,SubmissionConstants.SUBMISSON_COMMAND) );
  panel.add( new Hidden(SubmissionConstants.SUBMISSON_KEY,String.valueOf(key) ) );
  panel.add( new Hidden(SubmissionConstants.SUBMISSON_ID, sbmMeta.getId() ) );
  panel.add( new Hidden(SubmissionConstants.SUBMISSON_STATUS, Status.UPDATE.name() ) );
  
  FlexTable btPan = new FlexTable();
  btPan.setCellSpacing(6);
  btPan.setWidth("100%");
  FlexCellFormatter cellFormatter = btPan.getFlexCellFormatter();

  panel.add(btPan);

  final NewDMPanel.RemoveListener rmListener = new RemoveListener()
  {
   @Override
   public void removed(Widget w)
   {
    renumberPanels();
   }
  };

  
  cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
  Button addBt = new Button("Add File", new com.google.gwt.event.dom.client.ClickHandler()
  {
   public void onClick(com.google.gwt.event.dom.client.ClickEvent event)
   {
    NewFilePanel fp = new NewFilePanel(n++, rmListener);
    
    fp.setStylePrimaryName("dmPanelNEW");
    
    panel.insert(fp, panel.getWidgetCount()-1);
   }
  });
  btPan.setWidget(0, 0, addBt);
  
  cellFormatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
  addBt = new Button("Add Data Module", new com.google.gwt.event.dom.client.ClickHandler()
  {
   public void onClick(com.google.gwt.event.dom.client.ClickEvent event)
   {
    NewFilePanel atp = new NewFilePanel(n++, rmListener);
    
    atp.setStylePrimaryName("attPanelNEW");

    panel.insert(atp, nMods+6);
    nMods++;
    wc.adjustForContent(true);
    
    renumberPanels();
   }
  });
  btPan.setWidget(0, 1, addBt);

  panel.add(new Label("The update description:"));

  updateDescription = new TextArea();
  updateDescription.setName(SubmissionConstants.THE_UPDATE_DESCR);
  updateDescription.setWidth("97%");
  panel.add(updateDescription);

  
  FlexTable sbmDesc = new FlexTable();
  sbmDesc.setWidth("97%");
  
  sbmDesc.setWidget(0, 1, new Label("Submission description:"));

  final CheckBox sDscCB = new CheckBox();
  sDscCB.setName(SubmissionConstants.SUBMISSON_DESCR_UPDATE);
 
  submissionDescription = new TextArea();
  submissionDescription.setEnabled(false);
  submissionDescription.setName(SubmissionConstants.SUBMISSON_DESCR);
  submissionDescription.setWidth("97%");
  submissionDescription.setValue(sbmMeta.getDescription());

  sDscCB.addClickHandler( new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    submissionDescription.setEnabled( sDscCB.getValue() );
    
    if( ! sDscCB.getValue() )
     submissionDescription.setValue(sbmMeta.getDescription());
   }
  });
  
  sbmDesc.setWidget(1, 0, sDscCB);

  sbmDesc.setWidget(1,1,submissionDescription);

  panel.add(sbmDesc);
  
  if( sbmMeta.getDataModules() != null )
  {
   for( DataModuleMeta dmm : sbmMeta.getDataModules() )
   {
    panel.add(new DMInfoPanel(sbmMeta, dmm, n++));
    nMods++;
   }
  }
  
  if( sbmMeta.getAttachments() != null )
  {
   for( FileAttachmentMeta fat : sbmMeta.getAttachments() )
   {
    panel.add(new AtInfoPanel(sbmMeta, fat, n++));
   }
  }

  Button sbmBt = new Button("Submit", new com.google.gwt.event.dom.client.ClickHandler()
  {
   public void onClick(com.google.gwt.event.dom.client.ClickEvent event)
   {
    form.submit();
   }
  });
  
  panel.add(sbmBt);
  panel.setCellHorizontalAlignment(sbmBt, HasHorizontalAlignment.ALIGN_RIGHT);
  
  form.addSubmitHandler(new FormPanel.SubmitHandler()
  {
   public void onSubmit(SubmitEvent event)
   {
    String err = "";
    
    if( updateDescription.getText().trim().length() == 0 )
     err += "Update description is empty\n";
    
    if( submissionDescription.getText().trim().length() == 0 )
     err += "Submission description is empty\n";

   
    
    int ndm=0;
    for( Widget w : panel )
    {
     if( w instanceof NewDMPanel )
     {
      ndm++;
      
      NewDMPanel dmp = (NewDMPanel)w;
      
      if( dmp.getDescription().trim().length() == 0 )
       err+="Description of data module "+ndm+" is empty\n";
      
      if( dmp.getFile().trim().length() == 0 )
       err+="File for data module "+ndm+" is not selected\n";
     }
     else if( w instanceof NewFilePanel )
     {
      ndm++;
      
      NewFilePanel dmp = (NewFilePanel)w;
      
      if( dmp.getID().trim().length() == 0 )
       err+="ID for file "+ndm+" is not specified\n";
      
      if( dmp.getFile().trim().length() == 0 )
       err+="File for data module "+ndm+" is not selected\n";
     }
     else if( w instanceof AtInfoPanel )
     {
      ndm++;
      
      AtInfoPanel atp = (AtInfoPanel)w;
      
      if( atp.getID().trim().length() == 0 )
       err+="ID for file "+ndm+" is not specified\n";
      
      if( atp.isFileUpdated() && atp.getFile().trim().length() == 0 )
       err+="File for data module "+ndm+" is not selected\n";

     }
     else if( w instanceof DMInfoPanel )
     {
      ndm++;
      
      DMInfoPanel dmp = (DMInfoPanel)w;

      if( dmp.getDescription().trim().length() == 0 )
       err+="Description of data module "+ndm+" is empty\n";
      
      if( dmp.isFileUpdated() && dmp.getFile().trim().length() == 0 )
       err+="File for data module "+ndm+" is not selected\n";

     }
     
    }
    
    if( err.length() > 0 )
    {
     Window.alert("ERROR:\n"+err);
     event.cancel();
    }
    
   }
  });

  form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler()
  {
   public void onSubmitComplete(SubmitCompleteEvent event)
   {
    String txt = event.getResults();
    
    if( txt.indexOf("OK-"+key) == -1 )
    {
     SC.warn("Error occured. Possibly you are not logged on or your session is expired");
     return;
    }
    
    int posB = txt.indexOf("<pre>");
    int posE = txt.lastIndexOf("</pre>");
    
    txt = txt.substring(posB+5,posE);
    
    System.out.println(txt);
    
    LogNode rLn = LogNode.convert(txt); 
    
    System.out.println(rLn.getSubnodes().get(0).getMessage());
    
    com.smartgwt.client.widgets.Window logW = new com.smartgwt.client.widgets.Window();
    logW.setWidth(1000);
    logW.setHeight(600);
    logW.centerInPage();
    
    logW.addItem( new LogTree(rLn) );
    
    logW.show();
   }
  });

  decp.setWidget(form);

  
  addMember(wc);

  
 }

 private void renumberPanels()
 {
  n=1;
  nMods=1;
  
  for( Widget w : panel )
  {
   if( w instanceof DMInfoPanel )
   {
    n++;
    nMods++;
   }
   else if( w instanceof NewDMPanel )
   {
    NewDMPanel dmp = (NewDMPanel)w;
    dmp.setOrder(n);

    n++;
    nMods++;
   }
   else if( w instanceof NewFilePanel )
   {
    NewFilePanel fp = (NewFilePanel)w;
    
    fp.setOrder(n);

    n++;
   }
   else if( w instanceof AtInfoPanel )
   {
    ((AtInfoPanel)w).setOrder(n);
    n++;
   }
  }

 }
 
 private static HTML createModuleLink(SubmissionMeta sMeta, final DataModuleMeta dmm)
 {
  return  new HTML("<a target='_blank' href='download?"
    +Constants.downloadHandlerParameter+"="+Constants.documentRequestSubject
    +"&"+Constants.clusterIdParameter+"="+sMeta.getId()
    +"&"+Constants.documentIdParameter+"="+dmm.getId()
    +"&"+Constants.versionParameter+"="+dmm.getDocVersion()
    +"'>Module file</a>");
 }
 
 private static HTML createAttachmentLink(SubmissionMeta sMeta, final FileAttachmentMeta atm)
 {
  return  new HTML("<a target='_blank' href='download?"
    +Constants.downloadHandlerParameter+"="+Constants.attachmentRequestSubject
    +"&"+Constants.clusterIdParameter+"="+sMeta.getId()
    +"&"+Constants.fileIdParameter+"="+atm.getId()
    +"&"+Constants.versionParameter+"="+atm.getFileVersion()
    +"'>"+atm.getId()+"</a>");
 }

 
 private class DMInfoPanel extends CaptionPanel
 {
  private TextArea dsc;
  private FileUpload upload;
  private String edDesc;
  private Label statusLbl = new Label();
  
  private CheckBox dscCB = new CheckBox();
  private CheckBox fileCB = new CheckBox();
  
  private Hidden statusInput = new Hidden();
  
  private Status status = Status.KEEP;
  
  private int order;
  
  DMInfoPanel( final SubmissionMeta sMeta, final DataModuleMeta dmm, int n)
  {
   order=n;
   
   //setWidth("*");
   setWidth("auto");
   setCaptionText("Data Module: "+n+" ID="+dmm.getId());

   updateStatus();
   
   int row=0;
   
   final FlexTable layout = new FlexTable();
   layout.setWidth("100%");
   FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();

   cellFormatter.setWidth(row, 0, "30");

   layout.setWidget(row, 0, new Hidden(SubmissionConstants.MODULE_ID + n, dmm.getId()) );
   
   statusLbl.setText("Status: "+status.name());
   layout.setWidget(row, 1, statusLbl);
   
   cellFormatter.setVerticalAlignment(row, 2, HasVerticalAlignment.ALIGN_TOP);
   cellFormatter.setHorizontalAlignment(row, 2, HasHorizontalAlignment.ALIGN_RIGHT);
   
   final HTML clsBt = new HTML("<img src='images/icons/delete.png'>");
   clsBt.addClickHandler(new ClickHandler()
   {
    @Override
    public void onClick(ClickEvent arg0)
    {
     if( status == Status.DELETE )
     {
      clsBt.setHTML("<img src='images/icons/delete.png'>");
      status = Status.KEEP;
     }
     else
     {
      clsBt.setHTML("<img src='images/icons/add.png'>");
      status = Status.DELETE;
     }
     
     updateStatus();
    }
   });
   
   layout.setWidget(row, 2, clsBt);

   row++;
   
   statusInput.setName( SubmissionConstants.MODULE_STATUS+n);
   statusInput.setValue(status.name());
   
   layout.setWidget( row, 0, statusInput );
   
   
   layout.setWidget(row++, 1, new Label("Description:"));

   dsc = new TextArea();
   dsc.setName(SubmissionConstants.MODULE_DESCRIPTION + n);
   dsc.setValue(dmm.getDescription());
   dsc.setEnabled(false);
   dsc.setWidth("97%");

   dscCB.setName(SubmissionConstants.MODULE_DESCRIPTION_UPDATE + n);
   dscCB.addClickHandler(new ClickHandler()
   {
    @Override
    public void onClick(ClickEvent event)
    {
     dsc.setEnabled(dscCB.getValue());
     
     if( ! dscCB.getValue() )
     {
      edDesc = dsc.getValue();
      dsc.setValue(dmm.getDescription());
     }
     else if( edDesc != null )
      dsc.setValue(edDesc);
     
     updateStatus();
    }
   });
   
   layout.setWidget( row, 0, dscCB );
   
   cellFormatter.setColSpan(row, 1, 2);
   layout.setWidget(row, 1, dsc);

   row++;

   final int upRow = row;
   
   cellFormatter.setColSpan(row, 1, 2);

   fileCB.setName(SubmissionConstants.MODULE_FILE_UPDATE + n);
   fileCB.addClickHandler(new ClickHandler()
   {
    @Override
    public void onClick(ClickEvent event)
    {
     if( fileCB.getValue() )
     {
      upload = new FileUpload();
      upload.setName(SubmissionConstants.MODULE_FILE + order);
      upload.getElement().setAttribute("size", "80");

      layout.setWidget(upRow, 1, upload);   
     }
     else
     {
      layout.setWidget(upRow, 1, createModuleLink(sMeta, dmm));
      upload = null;
     }

     updateStatus();
    }
   });
   
   layout.setWidget( row, 0, fileCB );

   layout.setWidget(row, 1, createModuleLink(sMeta, dmm));
 
//   upload = new FileUpload();
//   upload.setName(SubmissionConstants.MODULE_FILE + n);
//   upload.getElement().setAttribute("size", "255");
//   layout.setWidget(3, 0, upload);

   add(layout);
  }
  
  public boolean isFileUpdated()
  {
   return fileCB.getValue();
  }

  private void updateStatus()
  {
   if( status != Status.DELETE )
   {
    if( dscCB.getValue() || fileCB.getValue() )
     status = Status.UPDATE;
    else
     status = Status.KEEP;
   }
   
   statusInput.setValue(status.name());

   statusLbl.setText("Status: "+status.name());
   setStylePrimaryName("dmPanel"+status.name());
  }
  
  public String getDescription()
  {
   return dsc.getText();
  }

  public String getFile()
  {
   return upload.getFilename();
  }
 
 }


 public class AtInfoPanel extends CaptionPanel
 {
  private Hidden origId = new Hidden();
  
  private TextBox id;
  private CheckBox idCB = new CheckBox();

  private TextArea dsc;
  private CheckBox dscCB = new CheckBox();

  private FileUpload upload;
  private CheckBox fileCB = new CheckBox();

  private CheckBox isGlobal = new CheckBox();
  
  private Hidden statusInput = new Hidden();
  
  private int order;
  
  private Label statusLbl = new Label();
  
  private Status status = Status.KEEP;

  private FileAttachmentMeta fAtMeta;
  
  private String edDesc;
  
  AtInfoPanel( final SubmissionMeta sMeta, final FileAttachmentMeta fatMeta, int n)
  {
   order = n;
   fAtMeta = fatMeta;
   
   //setWidth("*");
   setWidth("auto");
 
   setCaptionText("Attached file: "+n+" ID="+fatMeta.getId());

   
   updateStatus();

   int row=0;
   
   final FlexTable layout = new FlexTable();
   layout.setWidth("100%");
   FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();
   

   cellFormatter.setWidth(row, 0, "30");
   origId.setName(SubmissionConstants.ATTACHMENT_ID + n);
   origId.setValue(fatMeta.getId());
   layout.setWidget(row, 0, origId );
   
   cellFormatter.setColSpan(row, 1, 3);
   layout.setWidget(row, 1, statusLbl);

   cellFormatter.setVerticalAlignment(row, 4, HasVerticalAlignment.ALIGN_TOP);
   cellFormatter.setHorizontalAlignment(row, 4, HasHorizontalAlignment.ALIGN_RIGHT);

   final HTML clsBt = new HTML("<img src='images/icons/delete.png'>");
   clsBt.addClickHandler(new ClickHandler()
   {
    @Override
    public void onClick(ClickEvent arg0)
    {
     if(status == Status.DELETE)
     {
      clsBt.setHTML("<img src='images/icons/delete.png'>");
      status = Status.KEEP;
     }
     else
     {
      clsBt.setHTML("<img src='images/icons/add.png'>");
      status = Status.DELETE;
     }

     updateStatus();
    }
   });

   layout.setWidget(row, 4, clsBt);

   row++;
   
   id = new TextBox();
   id.setValue(fatMeta.getId());
   id.setEnabled(false);
   id.setName(SubmissionConstants.ATTACHMENT_NEW_ID + n);
//   id.setWidth("97%");

   idCB.setName(SubmissionConstants.ATTACHMENT_ID_UPDATE + n);
   idCB.addClickHandler(new ClickHandler()
   {
    @Override
    public void onClick(ClickEvent event)
    {
     id.setEnabled(idCB.getValue());
     
     if( ! idCB.getValue() )
      id.setValue(fAtMeta.getId());
     
     updateStatus();
    }
   });
   
   layout.setWidget(row, 0, idCB);
   cellFormatter.setWidth(row, 1, "1");
   layout.setWidget(row, 1, new Label("ID:"));
   layout.setWidget(row, 2, id );
   cellFormatter.setWidth(row, 3, "1");
   layout.setWidget(row, 3,  new Label("Global:"));
   
   isGlobal.setValue(fatMeta.isGlobal());
   isGlobal.setName(SubmissionConstants.ATTACHMENT_GLOBAL + n);
   isGlobal.addClickHandler( new ClickHandler()
   {
    @Override
    public void onClick(ClickEvent event)
    {
     updateStatus();
    }
   });
   layout.setWidget(row, 4,  isGlobal);
   
   
   row++;
   
   statusInput.setName( SubmissionConstants.ATTACHMENT_STATUS+n);
   statusInput.setValue(status.name());
   
   layout.setWidget( row, 0, statusInput );

   
   cellFormatter.setColSpan(row, 1, 4);
   layout.setWidget(row, 1, new Label("Description:"));

   dsc = new TextArea();
   dsc.setEnabled(false);
   dsc.setValue(fatMeta.getDescription());
   dsc.setName(SubmissionConstants.ATTACHMENT_DESC + n);
   dsc.setWidth("97%");

   row++;
   
   dscCB.setName(SubmissionConstants.ATTACHMENT_DESC_UPDATE + n);
   dscCB.addClickHandler(new ClickHandler()
   {
    @Override
    public void onClick(ClickEvent event)
    {
     dsc.setEnabled(dscCB.getValue());
     
     if( ! dscCB.getValue() )
     {
      edDesc = dsc.getValue();
      dsc.setValue(fAtMeta.getDescription());
     }
     else if( edDesc != null )
      dsc.setValue(edDesc);
     
     updateStatus();
    }
   });
   layout.setWidget(row, 0, dscCB);
   
   cellFormatter.setColSpan(row, 1, 4);
   layout.setWidget(row, 1, dsc);

   row++;

   cellFormatter.setColSpan(row, 1, 4);

   final int upRow = row;
   

   fileCB.setName(SubmissionConstants.ATTACHMENT_FILE_UPDATE + n);
   fileCB.addClickHandler(new ClickHandler()
   {
    @Override
    public void onClick(ClickEvent event)
    {
     if( fileCB.getValue() )
     {
      upload = new FileUpload();
      upload.setName(SubmissionConstants.ATTACHMENT_FILE + order);
      upload.getElement().setAttribute("size", "80");
      upload.setWidth("100%");
      
      layout.setWidget(upRow, 1, upload);   
     }
     else
     {
      layout.setWidget(upRow, 1, createAttachmentLink(sMeta, fatMeta));
      upload = null;
     }

     updateStatus();
    }
   });
   
   layout.setWidget( row, 0, fileCB );

   layout.setWidget(row, 1, createAttachmentLink(sMeta, fatMeta));

   add(layout);
  }
  
  public boolean isFileUpdated()
  {
   return fileCB.getValue();
  }

  private void updateStatus()
  {
   if( status != Status.DELETE )
   {
    if( idCB.getValue() || dscCB.getValue() || fileCB.getValue() || isGlobal.getValue() != fAtMeta.isGlobal() )
     status = Status.UPDATE;
    else
     status = Status.KEEP;
   }
   
   statusInput.setValue(status.name());
   
   statusLbl.setText("Status: "+status.name());
   setStylePrimaryName("attPanel"+status.name());
  }

  
  public String getDescription()
  {
   return dsc.getText();
  }

  public String getFile()
  {
   return upload.getFilename();
  }
  
  public String getID()
  {
   return id.getText();
  }

  public void setOrder(int ndm)
  {
   order = ndm;
   
   origId.setName(SubmissionConstants.ATTACHMENT_NEW_ID + ndm);
   
   id.setName(SubmissionConstants.ATTACHMENT_ID + ndm);
   idCB.setName(SubmissionConstants.ATTACHMENT_ID_UPDATE + ndm);

   isGlobal.setName(SubmissionConstants.ATTACHMENT_GLOBAL + ndm);
   
   dsc.setName(SubmissionConstants.ATTACHMENT_DESC + ndm);
   dscCB.setName(SubmissionConstants.ATTACHMENT_DESC_UPDATE + ndm);

   fileCB.setName(SubmissionConstants.ATTACHMENT_FILE_UPDATE + ndm);
   
   if( upload != null )
    upload.setName(SubmissionConstants.ATTACHMENT_FILE + ndm);

   statusInput.setName( SubmissionConstants.ATTACHMENT_STATUS+ndm);

   setCaptionText("Attached file: "+ndm+" ID="+fAtMeta.getId());
  }

 }
}
