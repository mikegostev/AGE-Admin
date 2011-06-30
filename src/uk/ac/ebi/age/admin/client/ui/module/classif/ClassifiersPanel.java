package uk.ac.ebi.age.admin.client.ui.module.classif;

import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class ClassifiersPanel extends HLayout
{
 public ClassifiersPanel()
 {
  setWidth100();
  setHeight100();
  
  
  VLayout permPanel = new VLayout();
  permPanel.setWidth("50%");
  permPanel.setHeight100();
  
  ClassifiersList ul = new ClassifiersList( permPanel );
  ul.setWidth("50%");
  
  addMember(ul);
  addMember(permPanel);
 }
}
