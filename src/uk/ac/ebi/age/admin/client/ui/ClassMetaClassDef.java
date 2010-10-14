package uk.ac.ebi.age.admin.client.ui;

import java.util.ArrayList;
import java.util.Collection;

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
 public Collection<Canvas> createDetailsPanels(AgeAbstractClassImprint cls, XEditorPanel editor)
 {
  ArrayList<Canvas> panels = new ArrayList<Canvas>(5);

  Canvas pnl = new XCommonsPanel(cls, editor);
  panels.add(pnl);

//  pnl = new XSuperclassesPanel(cls, editor);
//  panels.add(pnl);
//
//  pnl = new XSubclassesPanel(cls, editor);
//  panels.add(pnl);

  pnl = new XHierarchyPanel(cls, editor);
  panels.add(pnl);
  
  pnl = new RelationRuleAttachPanel( (AgeClassImprint)cls, editor );
  panels.add(pnl);

  pnl = new AttributeRuleAttachPanel( (AgeClassImprint)cls, editor );
  panels.add(pnl);


  
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

 public static String getIcon(AgeClassImprint classImprint)
 {
  return "../images/icons/class/"+(classImprint.isAbstract()?"abstract.png":"regular.png");
 }

 public static MetaClassDef getInstance()
 {
  return instance;
 }
}
