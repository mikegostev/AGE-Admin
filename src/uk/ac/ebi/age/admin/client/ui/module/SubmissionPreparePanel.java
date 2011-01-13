package uk.ac.ebi.age.admin.client.ui.module;

import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class SubmissionPreparePanel extends HLayout
{
// private ArrayList<FormItem> items = new ArrayList<FormItem>();

 public SubmissionPreparePanel()
 {
  setAutoWidth();
  setLayoutLeftMargin(15);
  setLayoutTopMargin(15);
  setMembersMargin(15);
  
  setHeight(1);
  setWidth(400);
  

  setBorder("1px solid #6a6a6a"); 
  
  final DynamicForm form = new DynamicForm(); 
  
  form.setMargin(5);
  
  form.setNumCols(2);  
  form.setHeight("*");  
//  form.setColWidths(60, "*",60, "*");
  form.setTitleOrientation(TitleOrientation.TOP);
  form.setAction("upload");
  form.setTarget("_blank");
  form.setEncoding(Encoding.MULTIPART);

  
  addMember(form);
  
  
  TextAreaItem descriptionItem = new TextAreaItem();  
  descriptionItem.setName("descriptionItem");  
  descriptionItem.setTitle("Submission Description");
  descriptionItem.setWidth("100%");
  descriptionItem.setColSpan(2);
  
  ButtonItem btSbm = new ButtonItem("Submit");
  btSbm.setEndRow(false);
  
  ButtonItem addDM = new ButtonItem("AddDataModule");
  addDM.setStartRow(false);
  
  CanvasItem cnv = new CanvasItem();
  cnv.setShowTitle(false);
  
  VLayout vl = new VLayout();
  cnv.setCanvas(vl);
  
//  Canvas c = new Canvas();
//  c.setContents("<i>Hello</i> world<br><input type='file' name='aaa'>");
  
  vl.addMember( new DMPanel(1) );

  form.setFields( btSbm, addDM, cnv, descriptionItem );
  
//  items.add(descriptionItem);
//  items.add(btSbm);
//  items.add(addDM);
//  items.add(upl);
  
  btSbm.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler()
  {

   @Override
   public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event)
   {
    form.submitForm();
   }
  });

  
  addDM.addClickHandler( new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    for( FormItem it : form.getFields())
    {
     if( it instanceof CanvasItem )
     {
      CanvasItem cnv = (CanvasItem)it;
      
      Canvas c = cnv.getCanvas();
      
      if( c instanceof VLayout )
      {
       ((VLayout)c).addMember( new DMPanel(((VLayout)c).getMembers().length) );
      }
     }
    }
    
//    FormItem[] oitms = form.getFields();
//    FormItem[] nitms = new FormItem[oitms.length+1];
//    
//    TextAreaItem descriptionItem = new TextAreaItem();  
//    descriptionItem.setName("descriptionItem");  
//    descriptionItem.setTitle("Description");
//    descriptionItem.setWidth("100%");
//    
//    descriptionItem.setValue(oitms[0].getValue());
//    
//    nitms[0] = descriptionItem;
//    
//    nitms[1] = new ButtonItem("Submit");
//    nitms[2] = new ButtonItem("AddDataModule");
//
//    
//    for(int i=3; i < oitms.length; i++ )
//    {
////     nitms[i]=new UploadItem("file"+i, "Upload submission");
////     nitms[i].setValue(oitms[i].getValue());
//
//     
//    }
//    
//    UploadItem upl = new UploadItem("file"+nitms.length, "Upload submission");
//    
//    nitms[oitms.length]=upl;
//    
//    form.setFields(nitms);
//    
//    form.redraw();
   }
  });
 }

 private static class DMPanel extends Canvas
 {
  DMPanel( int n )
  {
   setContents("Description:<br><textarea name='desc"+n+"' style='width: 99%'></textarea><br><input size=60 style='width: 200' type='file' name='file"+n+"'>");
  }
 }

}
