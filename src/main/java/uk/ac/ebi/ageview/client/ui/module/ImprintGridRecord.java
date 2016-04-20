package uk.ac.ebi.ageview.client.ui.module;

import uk.ac.ebi.age.ui.shared.imprint.ObjectImprint;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ImprintGridRecord extends ListGridRecord
{
 private ObjectImprint imprint;

 public ObjectImprint getImprint()
 {
  return imprint;
 }

 public void setImprint(ObjectImprint imprint)
 {
  this.imprint = imprint;
 }
 
 
}
