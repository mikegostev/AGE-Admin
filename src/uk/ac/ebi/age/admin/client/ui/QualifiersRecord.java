package uk.ac.ebi.age.admin.client.ui;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class QualifiersRecord extends ListGridRecord
{
 private AgeAbstractClassImprint cls;
 private int id;
 
 public QualifiersRecord( int id, boolean uq, AgeAbstractClassImprint ci )
 {
  super();
  
  this.id=id;
  cls=ci;
  
  setAttribute("id", id );
  setAttribute("name", ci.getName() );
  setAttribute("uniq", uq );
 }
 
 public AgeAbstractClassImprint getAgeAbstractClassImprint()
 {
  return cls;
 }
 
 public int getId()
 {
  return id;
 }
 
 public boolean isUniq()
 {
  return getAttributeAsBoolean("uniq");
 }
 
// public void toggleType()
// {
//  RestrictionType[] vals = RestrictionType.values();
//  
//  for(int i=0; i < vals.length; i++ )
//  {
//   if( vals[i] == rtype )
//   {
//    if( i == (vals.length-1) )
//     rtype=vals[0];
//    else
//     rtype = vals[i+1];
//    
//    setAttribute("type", rtype.name() );
//    return;
//   }
//  }
// }
}
