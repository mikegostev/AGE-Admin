package uk.ac.ebi.age.admin.client.ui.module.submission;

import java.util.Collection;
import java.util.Date;

import uk.ac.ebi.age.admin.client.AgeAdminService;
import uk.ac.ebi.age.admin.client.ui.module.classif.TagSelectDialog2;
import uk.ac.ebi.age.admin.client.ui.module.classif.TagSelectedListener;
import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.ext.authz.TagRef;
import uk.ac.ebi.age.ext.entity.ClusterEntity;
import uk.ac.ebi.age.ext.submission.SubmissionMeta;
import uk.ac.ebi.age.ext.submission.SubmissionQuery;
import uk.ac.ebi.age.ext.submission.SubmissionReport;
import uk.ac.ebi.age.ui.client.LinkClickListener;
import uk.ac.ebi.age.ui.client.LinkManager;
import uk.ac.ebi.age.ui.client.module.PagingRuler;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.ExpansionMode;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordCollapseEvent;
import com.smartgwt.client.widgets.grid.events.RecordCollapseHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class SubmissionsListPane extends VLayout
{
 
 private SubmissionQuery query;
 
 private SubmissionList resultGrid = new SubmissionList();
 private PagingRuler pagingRuler = new PagingRuler("submPage");
 
 public SubmissionsListPane()
 {
  setHeight("100%");
  
  setMargin(5);
  
  setShowEdges(true);
  setEdgeSize(6);
//  setEdgeImage("gnframe.gif");
  setEdgeMarginSize(10);
  
  resultGrid.setShowAllRecords(true);  
  resultGrid.setWrapCells(true);
  resultGrid.setFixedRecordHeights(false);
  resultGrid.setCellHeight(20);
  
//  resultGrid.setBodyOverflow(Overflow.VISIBLE);
  resultGrid.setOverflow(Overflow.VISIBLE);
  
  resultGrid.setStyleName("reportGrid");
  
//  DataSource ds = SubmissionConstants.createSubmissionDataSource();
//  
//  ds.setClientOnly(true);
  
  resultGrid.setCanExpandRecords(true); 
  
  ListGridField idField = new ListGridField(SubmissionConstants.SUBM_ID.name(),"ID", 200);  
  ListGridField stsField = new ListGridField(SubmissionConstants.STS.name(),"Status",50);
  ListGridField descField = new ListGridField(SubmissionConstants.COMM.name(),"Description");
  ListGridField crtrField = new ListGridField(SubmissionConstants.CRTR.name(),"Submitter",100);
  ListGridField mdfrField = new ListGridField(SubmissionConstants.MDFR.name(),"Modifier",100);
  ListGridField ctimeField = new ListGridField(SubmissionConstants.CTIME.name(),"C. time",100);
  ListGridField mtimeField = new ListGridField(SubmissionConstants.MTIME.name(),"M. time",100);
//  ListGridField propField = new ListGridField("prop","AdditionalProp");
  
//  propField.setHidden(true);
  
//  idField.setWidth(200);
     
  resultGrid.setFields(idField, stsField, descField,crtrField,mdfrField,ctimeField,mtimeField );
  resultGrid.setExpansionMode(ExpansionMode.DETAIL_FIELD);
  
  pagingRuler.setVisible(false);
  
  addMember(pagingRuler);
  addMember(resultGrid);
  
  LinkManager.getInstance().addLinkClickListener("submPage", new LinkClickListener()
  {

   @Override
   public void linkClicked(String param)
   {
    final int pgNum;
    
    try
    {
     pgNum = Integer.parseInt(param);
    }
    catch (Exception e) 
    {
     return;
    }
    
    if( pgNum <= 0 )
     return;
    
    query.setOffset( (pgNum-1)*Constants.SUBMISSIONS_PER_PAGE );
    
    AgeAdminService.Util.getInstance().getSubmissions( query, new AsyncCallback<SubmissionReport>()
      {
       
       @Override
       public void onSuccess(SubmissionReport result)
       {
        showResult(result, query, pgNum);
       }
       
       @Override
       public void onFailure(Throwable caught)
       {
        SC.warn("Action failed: "+caught.getMessage());
       }
      });
    
   }}
  );

  
  LinkManager.getInstance().addLinkClickListener("clustTags", new LinkClickListener()
  {
   @Override
   public void linkClicked(final String param)
   {
    AgeAdminService.Util.getInstance().getEntityTags( new ClusterEntity(param), new AsyncCallback<Collection<TagRef>>()
      {
       
       @Override
       public void onSuccess(final Collection<TagRef> result)
       {
        new TagSelectDialog2(result, new TagSelectedListener()
        {
         @Override
         public void tagSelected( final Collection<TagRef> tr)
         {
          
          AgeAdminService.Util.getInstance().storeEntityTags( new ClusterEntity(param), tr, new AsyncCallback<Void>(){

           @Override
           public void onFailure(Throwable caught)
           {
            String fail = caught.getMessage();
            
            if( fail == null )
             fail = caught.getClass().getName();

            SC.warn("System failure: "+fail);     
           }

           @Override
           public void onSuccess(Void result)
           {
            resultGrid.setSubmissionTags(param, tr);
           }} );
          
         }
        }).show();
       }
       
       @Override
       public void onFailure(Throwable caught)
       {
        String fail = caught.getMessage();
        
        if( fail == null )
         fail = caught.getClass().getName();
        
        SC.warn("Action failed: "+fail);
       }
      });
   }
  });
 }
 
 public void showResult( SubmissionReport result , SubmissionQuery qry, int cpage )
 {
  query = qry;
  
  resultGrid.selectAllRecords();
  resultGrid.removeSelectedData();
  
  if( result.getTotalSubmissions() > Constants.SUBMISSIONS_PER_PAGE )
  {
   pagingRuler.setContents(cpage, result.getTotalSubmissions(), Constants.SUBMISSIONS_PER_PAGE, null, null);
   pagingRuler.setVisible(true);
  }
  else
   pagingRuler.setVisible(false);
  
  int i = result.getSubmissions().size();
  
  ListGridRecord[] data = new ListGridRecord[i];
  
  i=0;
  for( SubmissionMeta sgr : result.getSubmissions() )
  {
   ListGridRecord rec = new ListGridRecord();
   
   rec.setAttribute(SubmissionConstants.SUBM_ID.name(), sgr.getId());
   rec.setAttribute(SubmissionConstants.STS.name(), sgr.isRemoved()?SubmissionConstants.RMVD.title():SubmissionConstants.ACTV.title());
   rec.setAttribute(SubmissionConstants.CRTR.name(), sgr.getSubmitter());
   rec.setAttribute(SubmissionConstants.MDFR.name(), sgr.getModifier());
   rec.setAttribute(SubmissionConstants.COMM.name(), sgr.getDescription());
   rec.setAttribute(SubmissionConstants.CTIME.name(), DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format( new Date(sgr.getSubmissionTime())));
   rec.setAttribute(SubmissionConstants.MTIME.name(), DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT).format( new Date(sgr.getModificationTime())));

   rec.setAttribute("__obj", sgr);
   
   data[i++] = rec;
  }
  
  resultGrid.setData(data);
 }


 
 
 
 private class SubmissionList extends ListGrid implements RecordCollapseHandler
 {
  static final String expansionComponentField = "__expComp";
  
  SubmissionList()
  {
   addRecordCollapseHandler(this);
  }
  
  public void setSubmissionTags( String clustId, Collection<TagRef> tags )
  {
   for( Record r : getRecords() )
   {
    if( clustId.equals(r.getAttribute(SubmissionConstants.SUBM_ID.name())) )
    {
     SubmissionMeta sm = (SubmissionMeta)r.getAttributeAsObject("__obj");
     
     sm.setTags(tags);
     
     SubmissionDetailsPanel dpnl = (SubmissionDetailsPanel)r.getAttributeAsObject(expansionComponentField);
     
     if( dpnl == null )
      return;
     
     dpnl.setSubmissionTags( tags );
    
     return;
    }
   }
  }
  
  protected Canvas getExpansionComponent(final ListGridRecord record)
  {
   SubmissionDetailsPanel dtl = new SubmissionDetailsPanel( record );
   
   record.setAttribute(expansionComponentField, dtl);
   
   return dtl;
  }
  
  protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum)
  {
   if( SubmissionConstants.RMVD.title().equals(record.getAttributeAsString(SubmissionConstants.STS.name()) ) )
    return "background-color: #FFD4D0;";
   
   return super.getCellCSSText(record, rowNum, colNum);
  }

  @Override
  public void onRecordCollapse(RecordCollapseEvent event)
  {
   SubmissionDetailsPanel dtl = (SubmissionDetailsPanel)event.getRecord().getAttributeAsObject(expansionComponentField);
   dtl.destroy();
   
   event.getRecord().setAttribute(expansionComponentField, (Object)null);
  }
 }
 
}
