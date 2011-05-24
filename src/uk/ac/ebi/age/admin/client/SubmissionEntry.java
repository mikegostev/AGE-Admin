package uk.ac.ebi.age.admin.client;

import uk.ac.ebi.age.admin.shared.Constants;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.layout.HLayout;

public class SubmissionEntry implements EntryPoint
{

 @Override
 public void onModuleLoad()
 {
  HLayout rootPanel = new HLayout();
  
  rootPanel.setAutoWidth();
  rootPanel.setLayoutLeftMargin(15);
  rootPanel.setLayoutTopMargin(15);
  rootPanel.setMembersMargin(15);
  
  DynamicForm loginForm = new DynamicForm();
  loginForm.setPadding(15);
  loginForm.setGroupTitle("Login");
  loginForm.setIsGroup(true);
  loginForm.setWidth(350);
  loginForm.setHeight(200);
  
  ButtonItem lgBt = new ButtonItem("Login");
  final StaticTextItem stat = new StaticTextItem("loginstat");
  stat.setTitle("Login status");
  stat.setValue(" not logged in");
  
  final TextItem uNameField = new TextItem("Username");
  final PasswordItem passField = new PasswordItem("Password");
  
  loginForm.setFields( uNameField, passField, stat, lgBt  );
  
  rootPanel.addMember(loginForm);
  
  lgBt.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler()
  {
   
   @Override
   public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event)
   {
    AgeAdminService.Util.getInstance().login(uNameField.getValue().toString(), passField.getValue().toString(), new AsyncCallback<String>()
    {
     
     @Override
     public void onSuccess(String res)
     {
      Cookies.setCookie(Constants.sessionKey, res);
      stat.setValue( "success "+res );
     }
     
     @Override
     public void onFailure(Throwable arg0)
     {
      arg0.printStackTrace();
      stat.setValue( arg0.getMessage() );
     }
    });
   }
  } );
  
  
//  final DynamicForm formModel = new DynamicForm();
//  
//  FormItem cmd = new HiddenItem("Command");
//  cmd.setValue("SetModel");
//  
//  
//  ButtonItem sBt = new ButtonItem("Upload");
//  sBt.addClickHandler( new ClickHandler()
//  {
//   
//   @Override
//   public void onClick(ClickEvent event)
//   {
//    formModel.submit();
//   }
//  });
//
//// 
////  IButton bt1 = new IButton("Submit");
////  bt1.addClickHandler( new ClickHandler()
////  {
////   
////   @Override
////   public void onClick(ClickEvent event)
////   {
////    formModel.submitForm();
////   }
////  });
//  formModel.setFields( new UploadItem("file1", "Upload file"), cmd, sBt );
//  
//  formModel.setAction("upload");
//  formModel.setEncoding(Encoding.MULTIPART);
//
//  rootPanel.addMember( formModel );
////  rootPanel.addMember( bt1 );
  
  
  final DynamicForm submModel = new DynamicForm();
  submModel.setPadding(15);
  submModel.setGroupTitle("Submit data");
  submModel.setIsGroup(true);
  submModel.setWidth(350);
  submModel.setHeight(200);

  FormItem cmd = new HiddenItem("Command");
  cmd.setValue("Submission");
  
  
 
  ButtonItem bt2 = new ButtonItem("Submit");
  bt2.addClickHandler( new com.smartgwt.client.widgets.form.fields.events.ClickHandler()
  {
   
   @Override
   public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event)
   {
    submModel.submitForm();
   }
  });

  UploadItem upl = new UploadItem("file1", "Upload submission");
  upl.setHeight(50);
  
  submModel.setFields( upl, cmd, bt2 );
  submModel.setAction("upload");
  submModel.setEncoding(Encoding.MULTIPART);

  rootPanel.addMember( submModel );

  final DynamicForm submModel2 = new DynamicForm();
  submModel2.setPadding(15);
  submModel2.setGroupTitle("Submit model");
  submModel2.setIsGroup(true);
  submModel2.setWidth(350);
  submModel2.setHeight(200);

  cmd = new HiddenItem("Command");
  cmd.setValue("SetModel");
  
  
 
  ButtonItem bt3 = new ButtonItem("Submit");
  bt3.addClickHandler( new com.smartgwt.client.widgets.form.fields.events.ClickHandler()
  {
   
   @Override
   public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event)
   {
    submModel2.submitForm();
   }
  });

  submModel2.setFields( new UploadItem("file1", "Upload model"), cmd, bt3 );
  submModel2.setAction("upload");
  submModel2.setEncoding(Encoding.MULTIPART);

  rootPanel.addMember( submModel2 );

  
  rootPanel.draw();
 }
}
