package uk.ac.ebi.age.admin.client.ui;

import java.util.ArrayList;
import java.util.Collection;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeRelationClassImprint;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.ui.module.modeled.RelationCommonsPanel;
import uk.ac.ebi.age.admin.client.ui.module.modeled.XEditorPanel;
import uk.ac.ebi.age.admin.client.ui.module.modeled.XSubclassesPanel;
import uk.ac.ebi.age.admin.client.ui.module.modeled.XSuperclassesPanel;

import com.smartgwt.client.widgets.Canvas;

public class RelationMetaClassDef implements MetaClassDef
{
 private static RelationMetaClassDef instance = new RelationMetaClassDef();
 
 public static RelationMetaClassDef getInstance()
 {
  return instance;
 }

 private RelationMetaClassDef()
 {}
 
 @Override
 public Collection<Canvas> createDetailsPanels(AgeAbstractClassImprint cls, XEditorPanel editor)
 {
  ArrayList<Canvas> panels = new ArrayList<Canvas>(5);

  Canvas pnl = new RelationCommonsPanel((AgeRelationClassImprint)cls, editor);
  panels.add(pnl);

  pnl = new XSuperclassesPanel(cls, editor);
  panels.add(pnl);

  pnl = new XSubclassesPanel(cls, editor);
  panels.add(pnl);

//  pnl = new AttributeAttachPanel( cls, editor );
//  panels.add(pnl);

  
  return panels;
 }

 @Override
 public ImprintTreeNode createTreeNode(AgeAbstractClassImprint root)
 {
  return new RelationTreeNode((AgeRelationClassImprint)root);
 }

 @Override
 public String getMetaClassName()
 {
  return "relation";
 }

 @Override
 public Collection< ? extends AgeAbstractClassImprint> getXClasses(ModelImprint model)
 {
  return model.getRelations();
 }

 @Override
 public AgeAbstractClassImprint getRoot(ModelImprint mod)
 {
  return mod.getRootRelationClass();
 }

 public static String getIcon(AgeRelationClassImprint classImprint)
 {
  return "../images/icons/relation/"+(classImprint.isAbstract()?"abstract.png":"regular.png");
 }

}
