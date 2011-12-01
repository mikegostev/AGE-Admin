package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;
import uk.ac.ebi.age.admin.client.ui.PanelInfo;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;

public class XDetailsPanel extends SectionStack
{
 public XDetailsPanel( AgeAbstractClassImprint cls, XEditorPanel clsPanel )
 {
  setWidth100();
  setHeight100();
  setOverflow(Overflow.AUTO);
  
  setVisibilityMode(VisibilityMode.MULTIPLE);
  
  int i=0;
  for( PanelInfo pnlinf : clsPanel.getMetaClassDef().createDetailsPanels(cls, clsPanel) )
  {
   SectionStackSection sec = new SectionStackSection(pnlinf.getTitle());
   sec.addItem(pnlinf.getPanel());
   
   if( pnlinf.getIcon() != null)
   {
    Label icn = new Label();
    icn.setIcon(pnlinf.getIcon());
    
    sec.setControls( icn );
   }
   
   
   
   addSection(sec);
   
   expandSection(i++);
  }
   
 }
 
}
