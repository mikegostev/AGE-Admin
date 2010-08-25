package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.AgeAdminService;
import uk.ac.ebi.age.admin.client.model.AgeClassImprint;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.ui.AnnotationMetaClassDef;
import uk.ac.ebi.age.admin.client.ui.AttributeMetaClassDef;
import uk.ac.ebi.age.admin.client.ui.ClassAuxData;
import uk.ac.ebi.age.admin.client.ui.ClassMetaClassDef;
import uk.ac.ebi.age.admin.client.ui.RelationMetaClassDef;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;


public class ModelPanel extends TabSet implements ModelMngr
{
 private ModelGeneralPanel generalPanel;
 private XEditorPanel classesPanel;
 private XEditorPanel attribPanel;
 private XEditorPanel relationsPanel;
 private XEditorPanel annotationsPanel;
 
 public ModelPanel()
 {
  setTabBarPosition(Side.TOP);  
  setWidth100();  
  setHeight100();  

  Tab genTab = new Tab("General");
  genTab.setPane( generalPanel=new ModelGeneralPanel( this )  );
  
  addTab(genTab);

  
  Tab classTab = new Tab("Classes");
  classTab.setPane( classesPanel=new XEditorPanel( ClassMetaClassDef.getInstance() )  );
  
  addTab(classTab);

  
  Tab attrTab = new Tab("Attributes");
  attrTab.setPane( attribPanel=new XEditorPanel( AttributeMetaClassDef.getInstance() ) );
  
  addTab(attrTab);

  
  Tab relTab = new Tab("Relations");
  relTab.setPane( relationsPanel = new XEditorPanel(RelationMetaClassDef.getInstance()));
  
  addTab(relTab);

  Tab annotTab = new Tab("Annotations");
  annotTab.setPane( annotationsPanel = new XEditorPanel(AnnotationMetaClassDef.getInstance()));
  
  addTab(annotTab);

  
  addDrawHandler(new DrawHandler()
  {
   
   @Override
   public void onDraw(DrawEvent event)
   {
    System.out.println("Master panel draw");
    AgeAdminService.Util.getInstance().getModelImprint(new AsyncCallback<ModelImprint>()
    {
     
     @Override
     public void onSuccess(ModelImprint mod)
     {
      for(AgeClassImprint climp : mod.getClasses() )
       climp.setAuxData(new ClassAuxData());
      
      classesPanel.setModel(mod);
      attribPanel.setModel(mod);
      relationsPanel.setModel(mod);
      annotationsPanel.setModel(mod);
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

 private void setModel( ModelImprint mod )
 {
  generalPanel.setModel(mod);
  classesPanel.setModel(mod);
  attribPanel.setModel(mod);
  relationsPanel.setModel(mod);
  annotationsPanel.setModel(mod);

 }
 
 @Override
 public void setNewModel()
 {
  ModelImprint mod = new ModelImprint();
  setModel(mod);
 }

 @Override
 public void saveModel(ModelImprint model)
 {
  if( model.getStorePath() != null )
   AgeAdminService.Util.getInstance().saveModel(model,model.getStorePath(),new AsyncCallback<Void>(){

    @Override
    public void onFailure(Throwable arg0)
    {
     // TODO Auto-generated method stub
     
    }

    @Override
    public void onSuccess(Void arg0)
    {
     // TODO Auto-generated method stub
     
    }});

 }
}
