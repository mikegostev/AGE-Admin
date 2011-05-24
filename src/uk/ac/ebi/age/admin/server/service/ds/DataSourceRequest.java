package uk.ac.ebi.age.admin.server.service.ds;

import java.util.HashMap;
import java.util.Map;

import uk.ac.ebi.age.admin.shared.ds.DSField;

public class DataSourceRequest
{
 private int begin;
 private int end;
 private Map<DSField, String> values;

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

}
