package uk.ac.ebi.age.admin.client.ui;

import java.util.ArrayList;
import java.util.Collection;

import uk.ac.ebi.age.admin.client.ModeledIcons;
import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeClassImprint;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.ui.module.modeled.AttributeRuleAttachPanel;
import uk.ac.ebi.age.admin.client.ui.module.modeled.RelationRuleAttachPanel;
import uk.ac.ebi.age.admin.client.ui.module.modeled.XCommonsPanel;
import uk.ac.ebi.age.admin.client.ui.module.modeled.XEditorPanel;
import uk.ac.ebi.age.admin.client.ui.module.modeled.XHierarchyPanel;

import com.smartgwt.client.widgets.Canvas;

public class ClassMetaClassDef extends MetaClassDef
{
 private static ClassMetaClassDef instance = new ClassMetaClassDef();
 
 private ClassMetaClassDef()
 {}
 
 @Override
 public Collection<PanelInfo> createDetailsPanels(AgeAbstractClassImprint cls, XEditorPanel editor)
 {
  ArrayList<PanelInfo> panels = new ArrayList<PanelInfo>(5);

  PanelInfo pinf = new PanelInfo();
  panels.add(pinf);

  Canvas pnl = new XCommonsPanel(cls, editor);
  pinf.setPanel(pnl);
  pinf.setTitle("Common properties");
  pinf.setIcon(ModeledIcons.get.commonProperties());


  pnl = new XHierarchyPanel(cls, editor);
  pinf = new PanelInfo();
  panels.add(pinf);
  pinf.setPanel(pnl);
  pinf.setTitle("Hierarchy");
  pinf.setIcon(ModeledIcons.get.hierarchy());
  
  pnl = new RelationRuleAttachPanel( (AgeClassImprint)cls, editor );
  pinf = new PanelInfo();
  panels.add(pinf);
  pinf.setPanel(pnl);
  pinf.setTitle("Relation rules");
  pinf.setIcon(ModeledIcons.get.relationRules());

  pnl = new AttributeRuleAttachPanel( (AgeClassImprint)cls, editor );
  pinf = new PanelInfo();
  panels.add(pinf);
  pinf.setPanel(pnl);
  pinf.setTitle("Attribute rules");
  pinf.setIcon(ModeledIcons.get.attributeRules());


  
  return panels;
 }

 @Override
 public ImprintTreeNode createTreeNode(AgeAbstractClassImprint root)
 {
  return new ClassTreeNode((AgeClassImprint)root);
 }

 @Override
 public String getMetaClassName()
 {
  return "class";
 }

 @Override
 public Collection< ? extends AgeAbstractClassImprint> getXClasses(ModelImprint model)
 {
  return model.getClasses();
 }

 @Override
 public AgeAbstractClassImprint getRoot(ModelImprint mod)
 {
  return mod.getRootClass();
 }

 @Override
 public String getClassIcon(AgeAbstractClassImprint classImprint)
 {
  return getIcon(classImprint);
 }

 
 public static String getIcon(AgeAbstractClassImprint classImprint)
 {
  if( classImprint.isAbstract() )
   return ModeledIcons.get.ageAbstractClass();
  else
   return ModeledIcons.get.ageClass();
  
//  return "../images/icons/class/"+(classImprint.isAbstract()?"abstract.png":"regular.png");
 }

 public static MetaClassDef getInstance()
 {
  return instance;
 }
}
