package uk.ac.ebi.age.admin.client.ui.module.auth;

import uk.ac.ebi.age.admin.shared.auth.UserDSDef;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.HeaderItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.validator.MatchesFieldValidator;

public class UserAddForm extends DynamicForm
{
 public UserAddForm( DataSource dataSource )
 {
  setMargin(20);
  
  final DynamicForm form = this;
  
  form.setWidth(300);
  
  form.setDataSource(dataSource);  
//  form.setUseAllDataSourceFields(true);  

  HeaderItem header = new HeaderItem();  
  header.setDefaultValue("Registration Form");  

  TextItem idField = new TextItem();
  idField.setName(UserDSDef.userIdField.getFieldId());
  idField.setTitle(UserDSDef.userIdField.getFieldTitle());  
  idField.setRequired(true);  
  
  TextItem nameField = new TextItem();
  nameField.setName(UserDSDef.userNameField.getFieldId());
  nameField.setTitle(UserDSDef.userNameField.getFieldTitle());  
  nameField.setRequired(true);  
  
  PasswordItem passwordItem = new PasswordItem();  
  passwordItem.setName(UserDSDef.userPassField.getFieldId());  
  passwordItem.setTitle(UserDSDef.userPassField.getFieldTitle());  
  passwordItem.setRequired(true);  

  PasswordItem passwordItem2 = new PasswordItem();  
  passwordItem2.setName("password2");  
  passwordItem2.setTitle("Password Again");  
  passwordItem2.setRequired(true);  
  passwordItem2.setLength(30);  

  MatchesFieldValidator matchesValidator = new MatchesFieldValidator();  
  matchesValidator.setOtherField(UserDSDef.userPassField.getFieldId());  
  matchesValidator.setErrorMessage("Passwords do not match");          
  passwordItem2.setValidators(matchesValidator);  


  FormItem sp = new SpacerItem();
  sp.setHeight(15);
  
  ButtonItem addItem = new ButtonItem();
  addItem.setAlign(Alignment.CENTER);
  addItem.setTitle("Add");  
  addItem.addClickHandler(new ClickHandler()
  {  
      public void onClick(ClickEvent event) {  
          if( ! form.validate(false) )
           return;
          
          saveData();
      }  
  });  
  addItem.setEndRow(false);
  
  ButtonItem cancelItem = new ButtonItem();
  cancelItem.setAlign(Alignment.CENTER);
  cancelItem.setTitle("Cancel");  
  cancelItem.addClickHandler(new ClickHandler()
  {  
      public void onClick(ClickEvent event) {  
          form.validate(false);  
      }  
  });  
  cancelItem.setStartRow(false);

  form.setFields(header, idField, nameField, passwordItem, passwordItem2, sp,  addItem, cancelItem);  
 }
 
}
