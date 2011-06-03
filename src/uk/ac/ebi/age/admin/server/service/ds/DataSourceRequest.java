package uk.ac.ebi.age.admin.server.service.ds;

import java.util.HashMap;
import java.util.Map;

import uk.ac.ebi.age.admin.shared.ds.DSField;

public class DataSourceRequest
{
 public static enum RequestType
 {
  FETCH,
  ADD,
  DELETE,
  UPDATE;
 }
 
 private int begin;
 private int end;
 private Map<DSField, String> values;
 private RequestType reqType;
 private Map<String, String> reqParams = new HashMap<String, String>();
 
 
 public void setEnd(int e)
 {
  end=e;
 }

 public int getEnd()
 {
  return end;
 }

 public int getBegin()
 {
  return begin;
 }

 public void setBegin(int begin)
 {
  this.begin = begin;
 }

 public void addFieldValue(DSField dsf, String val)
 {
  if( values == null )
   values = new HashMap<DSField, String>();
  
  values.put(dsf, val);
 }

 public RequestType getRequestType()
 {
  return reqType;
 }
 
 public void setRequestType( RequestType typ )
 {
  reqType=typ;
 }

 
 public Map<DSField, String> getValueMap()
 {
  return values;
 }

 public void addRequestParameter(String pName, String parameter)
 {
  reqParams.put(pName, parameter);
 }
 
 public Map<String, String> getRequestParametersMap()
 {
  return reqParams;
 }

}
