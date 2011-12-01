package uk.ac.ebi.age.admin.client.ui.module.submission;

import uk.ac.ebi.age.admin.client.ui.module.submission.NewDMPanel.RemoveListener;
import uk.ac.ebi.age.admin.shared.SubmissionConstants;
import uk.ac.ebi.age.ext.submission.Status;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

public class NewFilePanel extends CaptionPanel
{
 private TextBox id;
 private TextArea dsc;
 private FileUpload upload;
 private CheckBox isGlobal;
 private Hidden statusInput = new Hidden();
 
 private RemoveListener remListener;
 private int order;
 
 NewFilePanel(int n, RemoveListener rml )
 {
  remListener = rml;
  order = n;
 
  //setWidth("*");
  setWidth("auto");
  setCaptionText("Attached file: "+n);

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

  HTML clsBt = new HTML("<img src='admin_images/icons/delete.png'>");
  clsBt.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent arg0)
   {
    removeFromParent();
    
    if( remListener != null )
     remListener.removed(NewFilePanel.this);
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

  statusInput.setName(SubmissionConstants.ATTACHMENT_STATUS + n);
  statusInput.setValue(Status.NEW.name());

  layout.setWidget(4, 0, statusInput);
  
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

 public void setOrder(int ndm)
 {
  order = ndm;
  setCaptionText("Attached file: "+order);
  
  id.setName(SubmissionConstants.ATTACHMENT_ID + ndm);
  isGlobal.setName(SubmissionConstants.ATTACHMENT_GLOBAL + ndm);
  dsc.setName(SubmissionConstants.ATTACHMENT_DESC + ndm);
  upload.setName(SubmissionConstants.ATTACHMENT_FILE + ndm);
  statusInput.setName(SubmissionConstants.ATTACHMENT_STATUS + ndm);

 }

}

