package uk.ac.ebi.age.admin.client.ui.module.submission;

import java.util.Date;

import uk.ac.ebi.age.admin.client.AgeAdminService;
import uk.ac.ebi.age.ext.submission.SubmissionQuery;
import uk.ac.ebi.age.ext.submission.SubmissionReport;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.MiniDateRangeItem;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
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
 
 private SubmissionsListPane resultPane;
 
 public SubmissionsQueryPanel(SubmissionsListPane rp)
 {
  resultPane = rp;
  
//  setHeight("100");
//  setWidth("800");
  setMembersMargin(20);
  setPadding(5);
  setAlign(Alignment.CENTER);
  
  setBorder("1px dotted green");
  
  setOverflow(Overflow.VISIBLE);
  
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
  searchBt.addClickHandler( new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    executeQuery();
   }
  });
  
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
  final SubmissionQuery q = new SubmissionQuery();
 
  q.setQuery( queryField.getValueAsString() );
  q.setSubmissionID( submissionIDField.getValueAsString() );
  q.setModuleID( moduleIDField.getValueAsString() );

  q.setSubmitter( submitterField.getValueAsString() );
  q.setModifier( modifierField.getValueAsString() );
  
  Date dt = createdRangeField.getFromDate();
  
  if( dt != null )
   q.setCreatedFrom(dt.getTime());
  else
   q.setCreatedFrom(-1);

  dt = createdRangeField.getToDate();
  
  if( dt != null )
   q.setCreatedTo(dt.getTime()+24L*3600L*1000L);
  else
   q.setCreatedTo(-1);

  dt = modifiedRangeField.getFromDate();
  
  if( dt != null )
   q.setModifiedFrom(dt.getTime());
  else
   q.setModifiedFrom(-1);

  dt = modifiedRangeField.getToDate();
  
  if( dt != null )
   q.setModifiedTo(dt.getTime()+24L*3600L*1000L);
  else
   q.setModifiedTo(-1);
  
  AgeAdminService.Util.getInstance().getSubmissions( q, new AsyncCallback<SubmissionReport>()
  {
   
   @Override
   public void onSuccess(SubmissionReport result)
   {
    resultPane.showResult(result, q, 1);
   }
   
   @Override
   public void onFailure(Throwable caught)
   {
    // TODO Auto-generated method stub
    
   }
  });
 }

}
