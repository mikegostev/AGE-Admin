package uk.ac.ebi.age.admin.client.ui.module;

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
 // private ArrayList<FormItem> items = new ArrayList<FormItem>();
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

  final FormPanel form = new FormPanel("_blank");
  form.setAction("upload");
  form.setEncoding(FormPanel.ENCODING_MULTIPART);
  form.setMethod(FormPanel.METHOD_POST);

  // Create a panel to hold all of the form widgets.
  final VerticalPanel panel = new VerticalPanel();
  panel.setSpacing(5);
  form.setWidget(panel);

  panel.add( new Hidden("Command","Submission") );
  
  FlexTable btPan = new FlexTable();
  btPan.setCellSpacing(6);
  btPan.setWidth("100%");
  FlexCellFormatter cellFormatter = btPan.getFlexCellFormatter();

  panel.add(btPan);

  // Add a 'submit' button.
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

  panel.add(new Label("Submission description"));

  final TextArea tb = new TextArea();
  tb.setName("submDescr");
  tb.setWidth("97%");
  panel.add(tb);

  panel.add(new DMPanel(n++));

  // Add an event handler to the form.
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
    // When the form submission is successfully completed, this event is
    // fired. Assuming the service returned a response of type text/html,
    // we can get the result text here (see the FormPanel documentation for
    // further explanation).
    Window.alert(event.getResults());
   }
  });

  decp.setWidget(form);
  addMember(decp);
  // final DynamicForm form = new DynamicForm();
  //
  // form.setMargin(5);
  //
  // form.setNumCols(2);
  // form.setHeight("*");
  // // form.setColWidths(60, "*",60, "*");
  // form.setTitleOrientation(TitleOrientation.TOP);
  // form.setAction("upload");
  // form.setTarget("_blank");
  // form.setEncoding(Encoding.MULTIPART);
  //
  //
  // addMember(form);
  //
  //
  // TextAreaItem descriptionItem = new TextAreaItem();
  // descriptionItem.setName("descriptionItem");
  // descriptionItem.setTitle("Submission Description");
  // descriptionItem.setWidth("100%");
  // descriptionItem.setColSpan(2);
  //
  // ButtonItem btSbm = new ButtonItem("Submit");
  // btSbm.setEndRow(false);
  //
  // ButtonItem addDM = new ButtonItem("AddDataModule");
  // addDM.setStartRow(false);
  //
  // CanvasItem cnv = new CanvasItem();
  // cnv.setShowTitle(false);
  //
  // VLayout vl = new VLayout();
  // cnv.setCanvas(vl);
  //
  // // Canvas c = new Canvas();
  // // c.setContents("<i>Hello</i> world<br><input type='file' name='aaa'>");
  //
  // vl.addMember( new DMPanel(1) );
  //
  // form.setFields( btSbm, addDM, cnv, descriptionItem );
  //
  // // items.add(descriptionItem);
  // // items.add(btSbm);
  // // items.add(addDM);
  // // items.add(upl);
  //
  // btSbm.addClickHandler(new
  // com.smartgwt.client.widgets.form.fields.events.ClickHandler()
  // {
  //
  // @Override
  // public void
  // onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event)
  // {
  // form.submitForm();
  // }
  // });
  //
  //
  // addDM.addClickHandler( new ClickHandler()
  // {
  // @Override
  // public void onClick(ClickEvent event)
  // {
  // for( FormItem it : form.getFields())
  // {
  // if( it instanceof CanvasItem )
  // {
  // CanvasItem cnv = (CanvasItem)it;
  //
  // Canvas c = cnv.getCanvas();
  //
  // if( c instanceof VLayout )
  // {
  // ((VLayout)c).addMember( new DMPanel(((VLayout)c).getMembers().length) );
  // }
  // }
  // }
  //
  // // FormItem[] oitms = form.getFields();
  // // FormItem[] nitms = new FormItem[oitms.length+1];
  // //
  // // TextAreaItem descriptionItem = new TextAreaItem();
  // // descriptionItem.setName("descriptionItem");
  // // descriptionItem.setTitle("Description");
  // // descriptionItem.setWidth("100%");
  // //
  // // descriptionItem.setValue(oitms[0].getValue());
  // //
  // // nitms[0] = descriptionItem;
  // //
  // // nitms[1] = new ButtonItem("Submit");
  // // nitms[2] = new ButtonItem("AddDataModule");
  // //
  // //
  // // for(int i=3; i < oitms.length; i++ )
  // // {
  // //// nitms[i]=new UploadItem("file"+i, "Upload submission");
  // //// nitms[i].setValue(oitms[i].getValue());
  // //
  // //
  // // }
  // //
  // // UploadItem upl = new UploadItem("file"+nitms.length,
  // "Upload submission");
  // //
  // // nitms[oitms.length]=upl;
  // //
  // // form.setFields(nitms);
  // //
  // // form.redraw();
  // }
  // });
 }

 private static class DMPanel extends CaptionPanel
 {
  private TextArea dsc;
  private FileUpload upload;
  
  DMPanel(int n)
  {
   setCaptionText("Data Module");

   FlexTable layout = new FlexTable();
   FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();

   layout.setWidget(0, 0, new Label("Description"));

   dsc = new TextArea();
   dsc.setName("dmdesc" + n);
   dsc.setWidth("97%");

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
