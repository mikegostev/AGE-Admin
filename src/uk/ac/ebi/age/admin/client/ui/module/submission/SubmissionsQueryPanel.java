package uk.ac.ebi.age.admin.client.ui.module.submission;

import uk.ac.ebi.age.admin.shared.submission.SubmissionQuery;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.MiniDateRangeItem;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.layout.HLayout;

public class SubmissionsQueryPanel extends HLayout
{
 private TextItem queryField;
 private TextItem submissionIDField;
 private TextItem moduleIDField;
 private MiniDateRangeItem createdRangeField;
 private MiniDateRangeItem modifiedRangeField;
 private TextItem submitterField;
 private TextItem modifierField;
 
 public SubmissionsQueryPanel(SubmissionsListPane resultPane)
 {
//  setHeight("100");
//  setWidth("800");
  setMembersMargin(20);
  setAlign(Alignment.CENTER);
  
  setBorder("1px dotted green");
  
//  setOverflow(Overflow.VISIBLE);
  
  DynamicForm simpQForm = new DynamicForm();
  simpQForm.setHeight("120");
  simpQForm.setIsGroup(true);
  simpQForm.setGroupTitle("Search by description");
  simpQForm.setPadding(5);
  
  
  PickerIcon searchPicker = new PickerIcon(PickerIcon.SEARCH, new FormItemClickHandler()
  {
   
   @Override
   public void onFormItemClick(FormItemIconClickEvent event)
   {
    executeQuery();
   }
  });
  
  queryField = new TextItem("refreshPicker","Query");
  queryField.setWidth(350);
  queryField.setShowTitle(false);
  queryField.setIcons(searchPicker);

  ButtonItem searchBt=new ButtonItem();
  searchBt.setTitle("Search");
  searchBt.setAlign(Alignment.RIGHT);
  searchBt.setEndRow(true);
  
  SpacerItem spIt = new SpacerItem();
  spIt.setHeight(10);
  spIt.setEndRow(true);
  
  simpQForm.setFields(spIt,queryField,searchBt);
  
  
  DynamicForm advQForm = new DynamicForm();
  advQForm.setCellPadding(5);
  advQForm.setNumCols(3);
  advQForm.setHeight("120");
  advQForm.setGroupTitle("Advanced");
  advQForm.setIsGroup(true);
  advQForm.setTitleOrientation(TitleOrientation.TOP);
  
  submissionIDField = new TextItem("subID", "Submission ID");
  submissionIDField.setWidth(150);
  submissionIDField.setShowTitle(true);

  moduleIDField = new TextItem("modID", "Module ID");
  moduleIDField.setWidth(150);
  moduleIDField.setShowTitle(true);

  createdRangeField = new MiniDateRangeItem("ctime", "Created within");
  createdRangeField.setWidth(200);
  
  modifiedRangeField = new MiniDateRangeItem("mtime", "Modified within");
  modifiedRangeField.setWidth(200);

  
  submitterField = new TextItem("submitter", "Submitted by");
  submitterField.setWidth(150);
  submitterField.setShowTitle(true);

  modifierField = new TextItem("modifier", "Modified by");
  modifierField.setWidth(150);
  modifierField.setShowTitle(true);

  
  advQForm.setFields(submissionIDField, submitterField, createdRangeField, moduleIDField,  modifierField, modifiedRangeField);
  
  Canvas sp = new Canvas();
  sp.setWidth100();
  
  addMember(sp);

  addMember(simpQForm);
  addMember(advQForm);
  
  sp = new Canvas();
  sp.setWidth100();
  
  addMember(sp);
 }

 public void executeQuery()
 {
  SubmissionQuery q = new SubmissionQuery();
 
  q.setQuery( queryField.getValueAsString() );
  
 }

}
