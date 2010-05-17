package uk.ac.ebi.age.admin.client.ui.module;

import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;


public class ModelPanel extends TabSet
{
 public ModelPanel()
 {
  setTabBarPosition(Side.TOP);  
  setWidth100();  
  setHeight100();  

  Tab classTab = new Tab("Classes");
  classTab.setPane( new ClassEditorPanel() );
  
  addTab(classTab);

  
  Tab attrTab = new Tab("Attributes");
  attrTab.setPane( new AttributeEditorPanel() );
  
  addTab(attrTab);

  
  Tab relTab = new Tab("Relations");
  relTab.setPane( new RelationEditorPanel() );
  
  addTab(relTab);

  
 }
}
