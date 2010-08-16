package uk.ac.ebi.age.admin.client.ui;

import java.util.ArrayList;
import java.util.Collection;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeAnnotationClassImprint;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.ui.module.modeled.AnnotationCommonsPanel;
import uk.ac.ebi.age.admin.client.ui.module.modeled.XEditorPanel;
import uk.ac.ebi.age.admin.client.ui.module.modeled.XSubclassesPanel;
import uk.ac.ebi.age.admin.client.ui.module.modeled.XSuperclassesPanel;

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
 public Collection<Canvas> createDetailsPanels(AgeAbstractClassImprint cls, XEditorPanel editor)
 {
  ArrayList<Canvas> panels = new ArrayList<Canvas>(5);

  Canvas pnl = new AnnotationCommonsPanel((AgeAnnotationClassImprint)cls, editor);
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
  return model.getAnnotations();
 }

 @Override
 public AgeAnnotationClassImprint getRoot(ModelImprint mod)
 {
  return mod.getRootAnnotationClass();
 }

 public static String getIcon(AgeAnnotationClassImprint classImprint)
 {
  return "../images/icons/annotation/"+(classImprint.isAbstract()?"abstract.png":"regular.png");
 }

}
