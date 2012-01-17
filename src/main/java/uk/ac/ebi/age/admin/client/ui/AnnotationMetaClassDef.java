package uk.ac.ebi.age.admin.client.ui;

import java.util.ArrayList;
import java.util.Collection;

import uk.ac.ebi.age.admin.client.ModeledIcons;
import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeAnnotationClassImprint;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.ui.module.modeled.AnnotationCommonsPanel;
import uk.ac.ebi.age.admin.client.ui.module.modeled.XEditorPanel;
import uk.ac.ebi.age.admin.client.ui.module.modeled.XHierarchyPanel;

import com.smartgwt.client.widgets.Canvas;

public class AnnotationMetaClassDef extends MetaClassDef
{
 private static AnnotationMetaClassDef instance = new AnnotationMetaClassDef();
 
 public static AnnotationMetaClassDef getInstance()
 {
  return instance;
 }

 private AnnotationMetaClassDef()
 {}
 
 @Override
 public Collection<PanelInfo> createDetailsPanels(AgeAbstractClassImprint cls, XEditorPanel editor)
 {
  ArrayList<PanelInfo> panels = new ArrayList<PanelInfo>(5);

  PanelInfo pinf = null;

  Canvas pnl = new AnnotationCommonsPanel((AgeAnnotationClassImprint)cls, editor);
  pinf = new PanelInfo();
  panels.add(pinf);
  pinf.setPanel(pnl);
  pinf.setTitle("Common properties");
  pinf.setIcon(ModeledIcons.get.commonProperties());

  
  pnl = new XHierarchyPanel(cls, editor);
  pinf = new PanelInfo();
  panels.add(pinf);
  pinf.setPanel(pnl);
  pinf.setTitle("Hierarchy");
  pinf.setIcon(ModeledIcons.get.hierarchy());

  
  return panels;
 }

 @Override
 public ImprintTreeNode createTreeNode(AgeAbstractClassImprint root)
 {
  return new AnnotationTreeNode((AgeAnnotationClassImprint)root);
 }

 @Override
 public String getMetaClassName()
 {
  return "annotation";
 }

 @Override
 public Collection< ? extends AgeAbstractClassImprint> getXClasses(ModelImprint model)
 {
  return model.getAnnotationClasses();
 }

 @Override
 public AgeAnnotationClassImprint getRoot(ModelImprint mod)
 {
  return mod.getRootAnnotationClass();
 }

 public static String getIcon(AgeAbstractClassImprint classImprint)
 {
  if( classImprint.isAbstract() )
   return ModeledIcons.get.annotationAbstract();
  else
   return ModeledIcons.get.annotation();
 }

 @Override
 public String getClassIcon(AgeAbstractClassImprint classImprint)
 {
  return getIcon(classImprint);
 }

}
