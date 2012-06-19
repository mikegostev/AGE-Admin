package uk.ac.ebi.age.admin.shared.ds;

import java.util.ArrayList;
import java.util.List;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.RestDataSource;

public class DSDef
{
 private DSField keyField;
 private List<DSField> fields = new ArrayList<DSField>(10);


 public void addField( DSField fld )
 {
  fields.add(fld);

  if( fld.isPrimaryKey() )
   keyField = fld;
 }
 
 public void setFields(List<DSField> fields)
 {
  this.fields = fields;
  
  for( DSField dsf : fields )
  {
   if( dsf.isPrimaryKey() )
   {
    keyField = dsf;
    break;
   }
  }
 }

 public DSField getKeyField()
 {
  return keyField;
 }
 
 public List<DSField> getFields()
 {
  return fields;
 }
 
 public DataSource createDataSource()
 {
  RestDataSource ds = new RestDataSource();
  
  ds.setAutoCacheAllData(false);
  ds.setCacheAllData(false);
  
  DataSourceField fArr[] = new DataSourceField[fields.size()];
  
  int i=0;
  for( DSField f : fields )
  {
   DataSourceField dsf=null;
   
   if( f.getWidth() >  0 )
    dsf = new DataSourceField(f.getFieldId(), f.getType(), f.getFieldTitle(), f.getWidth());
   else
    dsf = new DataSourceField(f.getFieldId(), f.getType(), f.getFieldTitle());
   
   dsf.setPrimaryKey( f.isPrimaryKey() );
   dsf.setCanEdit(f.isEditable());
   dsf.setHidden( f.isHidden() );
   
   fArr[i++]=dsf;
  }
  
  ds.setFields(fArr);
  
  return ds;
 }
}
