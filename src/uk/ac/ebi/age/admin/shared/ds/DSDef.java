package uk.ac.ebi.age.admin.shared.ds;

import java.util.ArrayList;
import java.util.List;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.RestDataSource;

public class DSDef
{
 private List<DSField> fields = new ArrayList<DSField>(10);

 
 public void addField( DSField fld )
 {
  fields.add(fld);
 }
 
 public void setFields(List<DSField> fields)
 {
  this.fields = fields;
 }

 public List<DSField> getFields()
 {
  return fields;
 }
 
 public DataSource createDataSource()
 {
  RestDataSource ds = new RestDataSource();
  
  DataSourceField fArr[] = new DataSourceField[fields.size()];
  
  int i=0;
  for( DSField f : fields )
   if( f.getWidth() >  0 )
    fArr[i++] = new DataSourceField(f.getFieldId(), f.getType(), f.getFieldTitle(), f.getWidth());
   else
    fArr[i++] = new DataSourceField(f.getFieldId(), f.getType(), f.getFieldTitle());
   
  ds.setFields(fArr);
  
  return ds;
 }
}
