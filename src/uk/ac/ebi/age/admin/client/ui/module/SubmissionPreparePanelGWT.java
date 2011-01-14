package uk.ac.ebi.age.admin.client.ui.module;

import uk.ac.ebi.age.admin.client.log.LogNode;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
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
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.layout.HLayout;

public class SubmissionPreparePanelGWT extends HLayout
{
 private int n = 1;

 public SubmissionPreparePanelGWT()
 {
  setAutoWidth();
  setLayoutLeftMargin(15);
  setLayoutTopMargin(15);
  setMembersMargin(15);

  setHeight100();
  setWidth100();

  setBorder("1px solid #6a6a6a");

  DecoratorPanel decp = new DecoratorPanel();
//  decp.setWidth("500px");

  final FormPanel form = new FormPanel();
  form.setAction("upload");
  form.setEncoding(FormPanel.ENCODING_MULTIPART);
  form.setMethod(FormPanel.METHOD_POST);
//  form.setWidth("500px");

  final VerticalPanel panel = new VerticalPanel();
  panel.setSpacing(10);
  panel.setWidth("500px");
  form.setWidget(panel);

  panel.add( new Hidden("Command","Submission") );
  
  FlexTable btPan = new FlexTable();
  btPan.setCellSpacing(6);
  btPan.setWidth("100%");
  FlexCellFormatter cellFormatter = btPan.getFlexCellFormatter();

  panel.add(btPan);

  btPan.setWidget(0, 0, new Button("Submit", new com.google.gwt.event.dom.client.ClickHandler()
  {
   public void onClick(com.google.gwt.event.dom.client.ClickEvent event)
   {
    form.submit();
   }
  }));

  cellFormatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);
  Button addBt = new Button("Add Data Module", new com.google.gwt.event.dom.client.ClickHandler()
  {
   public void onClick(com.google.gwt.event.dom.client.ClickEvent event)
   {
    panel.add(new DMPanel(n++));
   }
  });
  btPan.setWidget(0, 1, addBt);

  panel.add(new Label("Submission description:"));

  final TextArea tb = new TextArea();
  tb.setName("submDescr");
  tb.setWidth("97%");
  panel.add(tb);

  panel.add(new DMPanel(n++));

  form.addSubmitHandler(new FormPanel.SubmitHandler()
  {
   public void onSubmit(SubmitEvent event)
   {
    String err = "";
    
    
    int ndm=0;
    for( Widget w : panel )
    {
     if( w instanceof DMPanel )
     {
      ndm++;
      
      DMPanel dmp = (DMPanel)w;
      
      if( dmp.getDescription().trim().length() == 0 )
       err+="Description of data module "+ndm+" is empty\n";
      
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
    
    int posB = txt.indexOf("<pre>");
    int posE = txt.lastIndexOf("</pre>");
    
    txt = txt.substring(posB+5,posE);
    
    System.out.println(txt);
    
    LogNode rLn = LogNode.convert(txt); 
    
    System.out.println(rLn.getSubnodes().get(0).getMessage());
   }
  });

  decp.setWidget(form);
  addMember(decp);
 }

 private static class DMPanel extends CaptionPanel
 {
  private TextArea dsc;
  private FileUpload upload;
  
  DMPanel(int n)
  {
   //setWidth("*");
   setWidth("auto");
   setCaptionText("Data Module");

   FlexTable layout = new FlexTable();
   layout.setWidth("100%");
   FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();

   layout.setWidget(0, 0, new Label("Description:"));

   dsc = new TextArea();
   dsc.setName("dmdesc" + n);
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
    }
   });

   layout.setWidget(0, 1, clsBt);

   cellFormatter.setColSpan(2, 0, 2);

   upload = new FileUpload();
   upload.setName("file" + n);
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
