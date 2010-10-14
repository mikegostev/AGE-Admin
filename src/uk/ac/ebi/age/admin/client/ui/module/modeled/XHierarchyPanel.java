package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;

public class XHierarchyPanel extends HLayout
{
 public XHierarchyPanel(final AgeAbstractClassImprint cls, final XEditorPanel editor)
 {
  setWidth100();
  setMargin(3);
  setTitle("Hierarchy");

  Canvas bPanel = new XSubclassesPanel(cls, editor);
  bPanel.setHeight100();
  
  Canvas pPanel = new XSuperclassesPanel(cls, editor);
  pPanel.setHeight100();
  
  setMembers(bPanel,pPanel);
  
 }

}
