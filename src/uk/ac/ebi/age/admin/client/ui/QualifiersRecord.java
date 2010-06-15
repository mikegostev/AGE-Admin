package uk.ac.ebi.age.admin.client.ui;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;
import uk.ac.ebi.age.admin.client.model.restriction.RestrictionType;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class QualifiersRecord extends ListGridRecord
{
 private AgeAbstractClassImprint cls;
 private RestrictionType rtype;
 
 public QualifiersRecord( RestrictionType rt, AgeAbstractClassImprint ci )
 {
  super();
  
  rtype=rt;
  cls=ci;
  
  setAttribute("type", rt.name() );
  setAttribute("name", ci.getName() );
 }
 
 public AgeAbstractClassImprint getAgeAbstractClassImprint()
 {
  return cls;
 }
 
 public RestrictionType getType()
 {
  return rtype;
 }
 
 public void toggleType()
 {
  RestrictionType[] vals = RestrictionType.values();
  
  for(int i=0; i < vals.length; i++ )
  {
   if( vals[i] == rtype )
   {
    if( i == (vals.length-1) )
     rtype=vals[0];
    else
     rtype = vals[i+1];
    
    setAttribute("type", rtype.name() );
    return;
   }
  }
 }
}
