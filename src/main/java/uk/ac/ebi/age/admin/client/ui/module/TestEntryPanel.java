package uk.ac.ebi.age.admin.client.ui.module;

import uk.ac.ebi.age.admin.client.AgeAdminService;
import uk.ac.ebi.age.admin.client.Session;
import uk.ac.ebi.age.admin.shared.Constants;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.layout.HLayout;

public class TestEntryPanel extends HLayout
{
 public TestEntryPanel()
 {

  setAutoWidth();
  setLayoutLeftMargin(15);
  setLayoutTopMargin(15);
  setMembersMargin(15);

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

  loginForm.setFields(uNameField, passField, stat, lgBt);

  addMember(loginForm);

  final ClickHandler ch = new ClickHandler()
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
      Session.setSessionId(res);
      stat.setValue("success " + res);
     }

     @Override
     public void onFailure(Throwable arg0)
     {
      arg0.printStackTrace();
      stat.setValue(arg0.getMessage());
     }
    });
   }
  };
  
  lgBt.addClickHandler(ch);

  passField.addKeyPressHandler( new KeyPressHandler()
  {
   
   @Override
   public void onKeyPress(KeyPressEvent event)
   {
    if( "Enter".equals(event.getKeyName()) )
     ch.onClick(null);
   }
  });
 }
}
