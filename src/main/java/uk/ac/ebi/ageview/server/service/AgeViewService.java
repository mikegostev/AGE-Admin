package uk.ac.ebi.ageview.server.service;

import uk.ac.ebi.age.ext.user.exception.NotAuthorizedException;
import uk.ac.ebi.age.model.AgeObject;
import uk.ac.ebi.age.ui.shared.imprint.ObjectId;
import uk.ac.ebi.age.ui.shared.imprint.ObjectImprint;
import uk.ac.ebi.ageview.client.query.Report;
import uk.ac.ebi.ageview.client.shared.MaintenanceModeException;
import uk.ac.ebi.ageview.server.stat.AgeViewStat;

public abstract class AgeViewService
{
 private static AgeViewService service;
 
 public static AgeViewService getInstance()
 {
  return service;
 }
 
 public static void setDefaultInstance( AgeViewService srv )
 {
  service=srv;
 }

 public abstract void shutdown();

 
 public abstract Report selectRootObjects(String value,  boolean searchAttrNm, boolean searchAttrVl, int offset, int count)
   throws MaintenanceModeException;

 public abstract AgeViewStat getStatistics();

 public abstract AgeObject getRootObject(String groupId) throws MaintenanceModeException;

 public abstract ObjectImprint getObjectImprint(ObjectId id) throws MaintenanceModeException, NotAuthorizedException;

}
