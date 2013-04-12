package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.AgeAdminService;
import uk.ac.ebi.age.admin.client.model.AgeClassImprint;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.ui.AnnotationMetaClassDef;
import uk.ac.ebi.age.admin.client.ui.AttributeMetaClassDef;
import uk.ac.ebi.age.admin.client.ui.ClassAuxData;
import uk.ac.ebi.age.admin.client.ui.ClassMetaClassDef;
import uk.ac.ebi.age.admin.client.ui.RelationMetaClassDef;
import uk.ac.ebi.age.admin.client.ui.module.log.LogWindow;
import uk.ac.ebi.age.admin.shared.ModelPath;
import uk.ac.ebi.age.admin.shared.StoreNode;
import uk.ac.ebi.age.ext.log.LogNode;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
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
 private OntologyPanel ontologyPanel;
 
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

  Tab ontolTab = new Tab("Ontologies");
  ontolTab.setPane( ontologyPanel = new OntologyPanel());
  
  addTab(ontolTab);

  
  addDrawHandler(new DrawHandler()
  {
   
   @Override
   public void onDraw(DrawEvent event)
   {
    AgeAdminService.Util.getInstance().getModelImprint(new AsyncCallback<ModelImprint>()
    {
     
     @Override
     public void onSuccess(ModelImprint mod)
     {
      for(AgeClassImprint climp : mod.getClasses() )
       climp.setAuxData(new ClassAuxData());
      
      setModel(mod);
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
  ontologyPanel.setModel(mod);
 }
 
 @Override
 public void setNewModel()
 {
  ModelImprint mod = new ModelImprint();
  setModel(mod);
 }

 private void doSaveModel(final ModelImprint model, final ModelPath path)
 {
  path.setModelName(model.getName());
  
  AgeAdminService.Util.getInstance().saveModel(model,path,new AsyncCallback<Void>(){

   @Override
   public void onFailure(Throwable arg0)
   {
    SC.warn("Error occured while saving model:<br> "+arg0.getMessage());
   }

   @Override
   public void onSuccess(Void arg0)
   {
    model.setStorePath(path);
    generalPanel.addModel(path);
   }});
 }
 
 @Override
 public void saveModel(final ModelImprint model)
 {
  if( model.getStorePath() != null )
  {
   doSaveModel(model,model.getStorePath());
  }
  else
  {
   StoreNode nd = generalPanel.getSelectedNode();
   
   if( nd == null )
   {
    SC.say("Please select destination directory");
    return;
   }
   
   if( ! nd.isDirectory() )
    nd = nd.getParent();
   
   String modName = model.getName();
   
   if( modName == null || modName.length() == 0 )
   {
    SC.say("Enter model name", "Please enter model name to save");
    return;
   }
   
   if( nd.getFiles() != null )
   {
    for( StoreNode f : nd.getFiles() )
    {
     if( f.getName().equals(modName) )
     {
      SC.ask("Confirm overwrite", "Model with name '"+modName+"' already exists. Do you want to overwrite it?", new BooleanCallback()
      {
       @Override
       public void execute(Boolean value)
       {
        if( !value )
         return;
        
        doSaveModel(model, generalPanel.getSelectedPath());
        return;
       }
      });
     }
    }
   }

   doSaveModel(model, generalPanel.getSelectedPath());
  }
 }

 @Override
 public void loadModel(final ModelPath path)
 {
  AgeAdminService.Util.getInstance().getModel(path,new AsyncCallback<ModelImprint>(){

   @Override
   public void onFailure(Throwable arg0)
   {
    SC.warn("Error occured while saving the model:<br> "+arg0.getMessage());
   }

   @Override
   public void onSuccess(ModelImprint model)
   {
    model.setStorePath(path);
    setModel(model);
   }
   });
 }

 @Override
 public void installModel(ModelPath modelPath)
 {
  AgeAdminService.Util.getInstance().installModel( modelPath, new AsyncCallback<LogNode>(){

   @Override
   public void onFailure(Throwable arg0)
   {
    SC.warn("Error occured while saving the model:<br> "+arg0.getMessage());
   }

   @Override
   public void onSuccess( LogNode arg)
   {
    new LogWindow("Model installation log", arg).show();
    //SC.say("Model installed");
   }
   });
 }
  
}
