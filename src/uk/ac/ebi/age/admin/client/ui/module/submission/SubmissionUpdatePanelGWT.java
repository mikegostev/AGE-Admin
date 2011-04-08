package uk.ac.ebi.age.admin.client.ui.module.submission;

import uk.ac.ebi.age.admin.client.log.LogNode;
import uk.ac.ebi.age.admin.client.ui.module.log.LogTree;
import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.admin.shared.SubmissionConstants;
import uk.ac.ebi.age.ext.submission.DataModuleMeta;
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

 public SubmissionUpdatePanelGWT( SubmissionMeta sbmMeta )
 {
  setAutoWidth();
  setLayoutLeftMargin(15);
  setLayoutTopMargin(15);
  setMembersMargin(15);

  setHeight100();
  setWidth100();

  setBorder("1px solid #6a5a6a");

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
  panel.setWidth("500px");
  form.setWidget(panel);

  panel.add( new Hidden(Constants.uploadHandlerParameter,SubmissionConstants.SUBMISSON_COMMAND) );
  panel.add( new Hidden(SubmissionConstants.SUBMISSON_KEY,String.valueOf(key) ) );
  
  FlexTable btPan = new FlexTable();
  btPan.setCellSpacing(6);
  btPan.setWidth("100%");
  FlexCellFormatter cellFormatter = btPan.getFlexCellFormatter();

  panel.add(btPan);

  cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
  Button addBt = new Button("Add File", new com.google.gwt.event.dom.client.ClickHandler()
  {
   public void onClick(com.google.gwt.event.dom.client.ClickEvent event)
   {
    panel.insert(new FilePanel(n++), panel.getWidgetCount()-1);
   }
  });
  btPan.setWidget(0, 0, addBt);
  
  cellFormatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
  addBt = new Button("Add Data Module", new com.google.gwt.event.dom.client.ClickHandler()
  {
   public void onClick(com.google.gwt.event.dom.client.ClickEvent event)
   {
    panel.insert(new DMNewPanel(n++), nMods+5);
    nMods++;
    wc.adjustForContent(true);
   }
  });
  btPan.setWidget(0, 1, addBt);

  panel.add(new Label("The update description:"));

  final TextArea updtDesc = new TextArea();
  updtDesc.setName(SubmissionConstants.UPDATE_DESCR);
  updtDesc.setWidth("97%");
  panel.add(updtDesc);

  
  panel.add(new Label("Submission description:"));

  final TextArea tb = new TextArea();
  tb.setName(SubmissionConstants.SUBMISSON_DESCR);
  tb.setWidth("97%");
  tb.setValue(sbmMeta.getDescription());
  panel.add(tb);

  if( sbmMeta.getDataModules() != null )
  {
   for( DataModuleMeta dmm : sbmMeta.getDataModules() )
    
    panel.add(new DMInfoPanel(dmm, n++));
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
     else if( w instanceof FilePanel )
     {
      ndm++;
      
      FilePanel dmp = (FilePanel)w;
      
      if( dmp.getID().trim().length() == 0 )
       err+="ID for file "+ndm+" is not specified\n";
      
      if( dmp.getFile().trim().length() == 0 )
       err+="File for data module "+ndm+" is not selected\n";
     }
     else if( w instanceof TextArea )
     {
      if( ((TextArea)w).getText().trim().length() == 0 )
       err += "Submission description is empty\n";
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

 private static class FilePanel extends CaptionPanel
 {
  private TextBox id;
  private TextArea dsc;
  private FileUpload upload;
  private CheckBox isGlobal;
  
  FilePanel(int n)
  {
   //setWidth("*");
   setWidth("auto");
   setCaptionText("Attached file");

   FlexTable layout = new FlexTable();
   layout.setWidth("100%");
   FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();

   id = new TextBox();
   id.setName(SubmissionConstants.ATTACHMENT_ID + n);
   id.setWidth("97%");

   layout.setWidget(0, 0, new Label("ID:"));
   layout.setWidget(0, 1, id );
   layout.setWidget(0, 2,  new Label("Global:"));
   
   isGlobal = new CheckBox();
   isGlobal.setName(SubmissionConstants.ATTACHMENT_GLOBAL + n);
   layout.setWidget(0, 3,  isGlobal);
   
   cellFormatter.setVerticalAlignment(0, 4, HasVerticalAlignment.ALIGN_TOP);
   cellFormatter.setHorizontalAlignment(0, 4, HasHorizontalAlignment.ALIGN_RIGHT);

   HTML clsBt = new HTML("<img src='images/icons/delete.png'>");
   clsBt.addClickHandler(new ClickHandler()
   {
    @Override
    public void onClick(ClickEvent arg0)
    {
     removeFromParent();
    }
   });

   layout.setWidget(0, 4, clsBt);
   
   cellFormatter.setColSpan(1, 0, 5);
   layout.setWidget(1, 0, new Label("Description:"));

   dsc = new TextArea();
   dsc.setName(SubmissionConstants.ATTACHMENT_DESC + n);
   dsc.setWidth("97%");

   cellFormatter.setColSpan(2, 0, 5);
   layout.setWidget(2, 0, dsc);



   cellFormatter.setColSpan(3, 0, 5);

   upload = new FileUpload();
   upload.setName(SubmissionConstants.ATTACHMENT_FILE + n);
   upload.getElement().setAttribute("size", "80");
   layout.setWidget(3, 0, upload);

   add(layout);
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

 }
 
 private class DMInfoPanel extends CaptionPanel
 {
  private TextArea dsc;
  private FileUpload upload;
  
  DMInfoPanel( DataModuleMeta dmm, int n)
  {
   //setWidth("*");
   setWidth("auto");
   setCaptionText("Data Module. ID = "+n);

   FlexTable layout = new FlexTable();
   layout.setWidth("100%");
   FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();

   layout.setWidget(0, 0, new Label("ID: "+dmm.getId()));
   layout.setWidget(1, 0, new Label("Description:"));

   dsc = new TextArea();
   dsc.setName(SubmissionConstants.MODULE_NAME + n);
   dsc.setValue(dmm.getDescription());
   dsc.setEnabled(false);
   dsc.setWidth("97%");

   cellFormatter.setColSpan(2, 0, 2);
   layout.setWidget(2, 0, dsc);

   cellFormatter.setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
   cellFormatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);

   HTML clsBt = new HTML("<img src='images/icons/delete.png'>");
   clsBt.addClickHandler(new ClickHandler()
   {
    @Override
    public void onClick(ClickEvent arg0)
    {
     removeFromParent();
     nMods--;
    }
   });

   layout.setWidget(0, 1, clsBt);

   cellFormatter.setColSpan(3, 0, 2);

   upload = new FileUpload();
   upload.setName(SubmissionConstants.MODULE_FILE + n);
   upload.getElement().setAttribute("size", "255");
   layout.setWidget(3, 0, upload);

   add(layout);
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

 private class DMNewPanel extends CaptionPanel
 {
  private TextArea dsc;
  private FileUpload upload;
  
  DMNewPanel(int n)
  {
   //setWidth("*");
   setWidth("auto");
   setCaptionText("Data Module");

   FlexTable layout = new FlexTable();
   layout.setWidth("100%");
   FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();

   layout.setWidget(0, 0, new Label("Description:"));

   dsc = new TextArea();
   dsc.setName(SubmissionConstants.MODULE_NAME + n);
   dsc.setWidth("97%");

   cellFormatter.setColSpan(1, 0, 2);
   layout.setWidget(1, 0, dsc);

   cellFormatter.setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
   cellFormatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);

   HTML clsBt = new HTML("<img src='images/icons/delete.png'>");
   clsBt.addClickHandler(new ClickHandler()
   {
    @Override
    public void onClick(ClickEvent arg0)
    {
     removeFromParent();
     nMods--;
    }
   });

   layout.setWidget(0, 1, clsBt);

   cellFormatter.setColSpan(2, 0, 2);

   upload = new FileUpload();
   upload.setName(SubmissionConstants.MODULE_FILE + n);
   upload.getElement().setAttribute("size", "80");
   layout.setWidget(2, 0, upload);

   add(layout);
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
}
