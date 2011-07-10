package uk.ac.ebi.age.admin.client.ui.module.classif;

import uk.ac.ebi.age.admin.shared.cassif.ClassifierDSDef;

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
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

public class ClassifierAddForm extends DynamicForm
{
 public ClassifierAddForm( DataSource dataSource, final CloseClickHandler clsHnd )
 {
  setMargin(20);
  
  final DynamicForm form = this;
  
  form.setWidth(300);
  
  form.setDataSource(dataSource);  
//  form.setUseAllDataSourceFields(true);  

  HeaderItem header = new HeaderItem();  
  header.setDefaultValue("New Classifier");  

  TextItem idField = new TextItem();
  idField.setName(ClassifierDSDef.idField.getFieldId());
  idField.setTitle(ClassifierDSDef.idField.getFieldTitle());  
  idField.setRequired(true);  
  
  TextItem nameField = new TextItem();
  nameField.setName(ClassifierDSDef.descField.getFieldId());
  nameField.setTitle(ClassifierDSDef.descField.getFieldTitle());  
  nameField.setRequired(true);  
  

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

  form.setFields(header, idField, nameField, sp,  addItem, cancelItem);  
 }
 
}
