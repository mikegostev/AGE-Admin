package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.AgeAdminService;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.model.ModelStorage;
import uk.ac.ebi.age.admin.shared.ModelPath;
import uk.ac.ebi.age.admin.shared.StoreNode;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.layout.HLayout;

public class ModelGeneralPanel extends HLayout
{
 private ModelStoreTree modelStoreTree;
 private ModelDetailsPanel modelDetailsPanel;

 public ModelGeneralPanel( ModelMngr mngr )
 {
  modelStoreTree = new ModelStoreTree( mngr );
  modelStoreTree.setHeight100();
  modelStoreTree.setWidth(300);
  
  modelDetailsPanel = new ModelDetailsPanel( mngr );
  modelDetailsPanel.setHeight100();
  modelDetailsPanel.setWidth("*");
  
  setMembers(modelStoreTree,modelDetailsPanel);
  
  addDrawHandler(new DrawHandler()
  {
   
   @Override
   public void onDraw(DrawEvent event)
   {
    System.out.println("Storage panel draw");
    AgeAdminService.Util.getInstance().getModelStorage(new AsyncCallback<ModelStorage>()
    {
     
     @Override
     public void onSuccess(ModelStorage modst)
     {
      modelStoreTree.setStorage(modst);
     }
     
     @Override
     public void onFailure(Throwable arg0)
     {
      // TODO Auto-generated method stub
      
     }
    });
    
   }
  });
 }

 public void setModel(ModelImprint mod)
 {
  modelDetailsPanel.setModel( mod );
 }
 
 public ModelPath getSelectedPath()
 {
  return modelStoreTree.getModelPath();
 }

 public StoreNode getSelectedNode()
 {
  return modelStoreTree.getSelectedNode();
 }

 public void addModel(ModelPath path)
 {
  modelStoreTree.addModel(path);
 }
}
