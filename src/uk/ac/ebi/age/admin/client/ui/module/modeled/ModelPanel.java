package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.AgeAdminService;
import uk.ac.ebi.age.admin.client.model.AgeClassImprint;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.ui.AttributeMetaClassDef;
import uk.ac.ebi.age.admin.client.ui.ClassAuxData;
import uk.ac.ebi.age.admin.client.ui.ClassMetaClassDef;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;


public class ModelPanel extends TabSet
{
 private ModelClassesPanel classesPanel;
 private XEditorPanel attribPanel;
 private XEditorPanel relationsPanel;
 
 public ModelPanel()
 {
  setTabBarPosition(Side.TOP);  
  setWidth100();  
  setHeight100();  

  Tab classTab = new Tab("Classes");
  classTab.setPane( classesPanel=new ModelClassesPanel() );
  
  addTab(classTab);

  
  Tab attrTab = new Tab("Attributes");
  attrTab.setPane( attribPanel=new XEditorPanel( new ClassMetaClassDef() ) );
  
  addTab(attrTab);

  
  Tab relTab = new Tab("Relations");
  relTab.setPane( relationsPanel=new XEditorPanel( new AttributeMetaClassDef() ));
  
  addTab(relTab);

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
}
