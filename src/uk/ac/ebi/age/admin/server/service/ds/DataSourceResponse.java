package uk.ac.ebi.age.admin.server.service.ds;

import uk.ac.ebi.age.admin.shared.ds.DSField;
import uk.ac.ebi.age.transaction.ReadLock;
import uk.ac.ebi.age.transaction.TransactionalDB;

import com.pri.util.collection.MapIterator;

public class DataSourceResponse
{
 private TransactionalDB db;
 private ReadLock lck;
 private int size;
 private int total;
 private MapIterator<DSField, String> iterator;
 private String errorMessage;

 public DataSourceResponse()
 {}
 
 public DataSourceResponse(TransactionalDB db, ReadLock lck)
 {
  super();
  this.db = db;
  this.lck = lck;
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

 public final void release()
 {
  if( db!=null )
   db.releaseLock(lck);
 }
 
}
