package uk.ac.ebi.age.admin.client;

import uk.ac.ebi.age.admin.client.common.ModelPath;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.model.ModelStorage;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AgeAdminServiceAsync
{

 void login(String uname, String pass, AsyncCallback<String> callback);

 void getModelImprint(AsyncCallback<ModelImprint> callback);

 void getModelStorage(AsyncCallback<ModelStorage> asyncCallback);

 void saveModel(ModelImprint model, ModelPath storePath, AsyncCallback<Void> callback);

 void getModel(ModelPath path, AsyncCallback<ModelImprint> asyncCallback);

 void installModel(ModelPath modelPath, AsyncCallback<Void> asyncCallback);

}
