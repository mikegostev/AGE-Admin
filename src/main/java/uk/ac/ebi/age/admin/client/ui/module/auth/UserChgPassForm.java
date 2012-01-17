package uk.ac.ebi.age.admin.client.ui.module.auth;

import uk.ac.ebi.age.admin.shared.auth.UserDSDef;
import uk.ac.ebi.age.admin.shared.ds.DSField;

import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.HeaderItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.validator.MatchesFieldValidator;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class UserChgPassForm extends DynamicForm
{
 public UserChgPassForm( ListGridRecord rec, DataSource dataSource, final CloseClickHandler clsHnd  )
 {
  setMargin(20); 
  
  final DynamicForm form = this;
  
  form.setWidth(300);
  
  form.setDataSource(dataSource);  
//  form.setUseAllDataSourceFields(true);  

  int n=0;
  FormItem[] items = new FormItem[ UserDSDef.getInstance().getFields().size()+6-1];
  
  String userId = rec.getAttribute(UserDSDef.userIdField.getFieldId());
  
  HeaderItem header = new HeaderItem();  
  header.setDefaultValue("Change password for user: "+userId);  
  items[n++] = header;
  
  for( DSField f : UserDSDef.getInstance().getFields() )
  {
   if( f == UserDSDef.userPassField )
    continue;
   
   HiddenItem ff = new HiddenItem();
   ff.setName(f.getFieldId());
   ff.setTitle(f.getFieldTitle()); 
   ff.setValue(rec.getAttribute(f.getFieldId()) );
   
   items[n++]=ff;
  }
  
  PasswordItem passwordItem = new PasswordItem();  
  passwordItem.setName(UserDSDef.userPassField.getFieldId());  
  passwordItem.setTitle(UserDSDef.userPassField.getFieldTitle());  
  passwordItem.setRequired(true);
  items[n++]=passwordItem;

  PasswordItem passwordItem2 = new PasswordItem();  
  passwordItem2.setName("password2");  
  passwordItem2.setTitle("Password Again");  
  passwordItem2.setRequired(true);  
  passwordItem2.setLength(30);  
  items[n++]=passwordItem2;

  MatchesFieldValidator matchesValidator = new MatchesFieldValidator();  
  matchesValidator.setOtherField(UserDSDef.userPassField.getFieldId());  
  matchesValidator.setErrorMessage("Passwords do not match");          
  passwordItem2.setValidators(matchesValidator);  


  FormItem sp = new SpacerItem();
  sp.setHeight(15);
  items[n++]=sp;

  
  ButtonItem addItem = new ButtonItem();
  addItem.setAlign(Alignment.CENTER);
  addItem.setTitle("Change");  
  addItem.addClickHandler(new ClickHandler()
  {  
      public void onClick(ClickEvent event)
      {  
       if( ! form.validate(false) )
        return;
          
       saveData( new DSCallback()
       {
        @Override
        public void execute(DSResponse response, Object rawData, DSRequest request)
        {
         if( response.getStatus() == RPCResponse.STATUS_SUCCESS )
          clsHnd.onCloseClick(null); 
        }
       });
      }  
  });  
  addItem.setEndRow(false);
  items[n++]=addItem;

  ButtonItem cancelItem = new ButtonItem();
  cancelItem.setAlign(Alignment.CENTER);
  cancelItem.setTitle("Cancel");  
  cancelItem.addClickHandler(new ClickHandler()
  {  
   public void onClick(ClickEvent event) { 
       clsHnd.onCloseClick(null); 
   }  
  });  
  cancelItem.setStartRow(false);
  items[n++]=cancelItem;

  form.setFields(items);  
 }
 
}
