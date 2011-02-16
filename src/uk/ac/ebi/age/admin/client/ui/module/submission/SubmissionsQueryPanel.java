package uk.ac.ebi.age.admin.client.ui.module.submission;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class SubmissionsQueryPanel extends TabSet
{

 public SubmissionsQueryPanel(SubmissionsPane resultPane)
 {
  setHeight("5%");
  setWidth100();
  
  setOverflow(Overflow.VISIBLE);
  
  Tab simpQ = new Tab("Simple query");
  Tab advQ = new Tab("Advanced query");
  
  DynamicForm simpQForm = new DynamicForm();
  simpQForm.setHeight("100%");
  
  PickerIcon searchPicker = new PickerIcon(PickerIcon.SEARCH, new FormItemClickHandler()
  {
   
   @Override
   public void onFormItemClick(FormItemIconClickEvent event)
   {
    // TODO Auto-generated method stub
    
   }
  });
  
  TextItem queryField = new TextItem("refreshPicker","Query");
//  queryField.setColSpan(5);
  queryField.setWidth(410);
  queryField.setTitleStyle("queryFieldTitle");
  queryField.setShowTitle(false);
  queryField.setIcons(searchPicker);
//  queryField.addKeyPressHandler(act);

  simpQForm.setFields(queryField);
  
  simpQ.setPane(simpQForm);
  
  
  DynamicForm advQForm = new DynamicForm();
  advQForm.setHeight("100%");
  
  queryField = new TextItem("refreshPicker", "Query");
  queryField.setWidth(410);
  queryField.setTitleStyle("queryFieldTitle");
  queryField.setShowTitle(true);
  queryField.setIcons(searchPicker);

  TextItem queryField2 = new TextItem("refreshPicker2", "Query");
  queryField2.setWidth(410);
  queryField2.setTitleStyle("queryFieldTitle");
  queryField2.setShowTitle(true);
  queryField2.setIcons(searchPicker);

  
  advQForm.setFields(queryField, queryField2);
  
  advQ.setPane(advQForm);

  
  addTab(simpQ);
  addTab(advQ);
 }

 public void executeQuery()
 {
  // TODO Auto-generated method stub
  
 }

}
