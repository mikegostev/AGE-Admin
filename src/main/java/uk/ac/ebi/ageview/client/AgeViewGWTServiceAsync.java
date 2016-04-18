package uk.ac.ebi.ageview.client;

import uk.ac.ebi.age.ui.shared.imprint.ObjectId;
import uk.ac.ebi.age.ui.shared.imprint.ObjectImprint;
import uk.ac.ebi.ageview.client.query.Report;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AgeViewGWTServiceAsync
{
 void selectRootObjects(String value, boolean searchAttrNm, boolean searchAttrVl, int offs, int cnt, AsyncCallback<Report> acb);

 
 void getObjectImprint( ObjectId id, AsyncCallback<ObjectImprint> cb );

}
