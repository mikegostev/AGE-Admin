package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;

import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;

public class XDetailsPanel extends SectionStack
{
 public XDetailsPanel( AgeAbstractClassImprint cls, XEditorPanel clsPanel )
 {
  setWidth100();
  setHeight100();
  
  setVisibilityMode(VisibilityMode.MULTIPLE);
  
  int i=0;
  for( Canvas pnl : clsPanel.getMetaClassDef().createDetailsPanels(cls, clsPanel) )
  {
   SectionStackSection sec = new SectionStackSection(pnl.getTitle());
   sec.addItem(pnl);

   addSection(sec);
   
   expandSection(i++);
  }
   
 }
 

 


 
}
