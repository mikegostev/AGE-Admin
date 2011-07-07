package uk.ac.ebi.age.admin.server.service.ds;

import uk.ac.ebi.age.admin.shared.ds.DSField;
import uk.ac.ebi.age.transaction.Lock;

import com.pri.util.collection.MapIterator;

public class DataSourceResponse
{
 private Lock lck;
 private int size;
 private int total;
 private MapIterator<DSField, String> iterator;
 private String errorMessage;

 public DataSourceResponse()
 {}
 
 public DataSourceResponse( Lock l )
 {
  lck=l;
 }
 
 public void setTotal(int ttl)
 {
  total=ttl;
 }

 public int getTotal()
 {
  return total;
 }

 public void setIterator(MapIterator<DSField, String> mpi)
 {
  iterator=mpi;
 }

 public MapIterator<DSField, String> getIterator()
 {
  return iterator;
 }

 public int getSize()
 {
  return size;
 }

 public void setSize(int size)
 {
  this.size = size;
 }

 public void setErrorMessage(String errorMessage)
 {
  this.errorMessage = errorMessage;
 }

 public String getErrorMessage()
 {
  return errorMessage;
 }

 public void release()
 {
  if( lck != null )
   lck.release();
 }
 
}
