package uk.ac.ebi.age.admin.client.ui.module.log;

import uk.ac.ebi.age.ext.log.LogNode;

import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.fields.DataSourceImageField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class LogDataSource extends LocalDataSource
{
 private LogNode rootNode;

 public LogDataSource(LogNode root)
 {
  rootNode = root;
  
  setIconField("status");
  setTitleField("name");
  setDataField("name");
  
  
  DataSourceTextField idField = new DataSourceTextField("id");
  DataSourceTextField nameField = new DataSourceTextField("name", "Message");
  DataSourceImageField icnFiels = new DataSourceImageField("status");
  
  
  idField.setRequired(true);  
  idField.setPrimaryKey(true);

  DataSourceTextField parentField = new DataSourceTextField("childOf");  
  parentField.setRequired(true);  
  parentField.setForeignKey(getID() + ".id");  
  parentField.setRootValue("__root");  

  
  
  setFields(idField,icnFiels,nameField,parentField);
 }

 @Override
 protected void executeFetch(String requestId, DSRequest request, DSResponse response)
 {
  String[] prop = JSOHelper.getProperties(request.getData());
  System.out.println(rootNode.hashCode()); 
  
  for( String s: prop)
   System.out.println(s+"="+JSOHelper.getAttribute(request.getData(), s));
  
  Record[] returnRecords = null ; 
    
  String parent =   JSOHelper.getAttribute(request.getData(), "childOf");
    
  if( "__root".equals(parent) )
  {
   Record res = new ListGridRecord();
   
   returnRecords = new Record[1];
   returnRecords[0]=res;
   
   res.setAttribute("id", rootNode.hashCode());
   res.setAttribute("name", "Log");
   res.setAttribute("status", "icons/log/"+rootNode.getLevel().name()+".png");
  }
  else
  {
   for( LogNode ln : rootNode.getSubNodes())
    System.out.println("hash="+ln.hashCode());
     
   String[] path = parent.split(":");
  }
    
  response.setData(returnRecords);
  processResponse(requestId, response);

//  // assume we have 1000 items.
//  response.setTotalRows(total);
//  int end = request.getEndRow();
//  if(end > total)
//  {
//   end = total;
//  }
//  Record returnRecords[] = new Record[end - request.getStartRow()];
//  for(int i = request.getStartRow(); i < end; i++)
//  {
//   ListGridRecord r = new ListGridRecord();
//   r = (ListGridRecord) records[i];
//   returnRecords[i - request.getStartRow()] = r;
//  }
//  GWT.log(" called from " + request.getStartRow() + " to " + request.getEndRow() + " result " + returnRecords.length, null);
//  response.setData(returnRecords);
//  processResponse(requestId, response);
 }

 @Override
 protected void executeAdd(String requestId, DSRequest request, DSResponse response)
 {
  // TODO Auto-generated method stub

 }

 @Override
 protected void executeUpdate(String requestId, DSRequest request, DSResponse response)
 {
  // TODO Auto-generated method stub

 }

 @Override
 protected void executeRemove(String requestId, DSRequest request, DSResponse response)
 {
  // TODO Auto-generated method stub

 }
}
