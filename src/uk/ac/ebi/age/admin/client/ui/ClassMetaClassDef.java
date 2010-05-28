package uk.ac.ebi.age.admin.client.ui;

import java.util.ArrayList;
import java.util.Collection;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeClassImprint;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.ui.module.modeled.AttributesPanel;
import uk.ac.ebi.age.admin.client.ui.module.modeled.ClassCommonsPanel;
import uk.ac.ebi.age.admin.client.ui.module.modeled.RestrictionPanel;
import uk.ac.ebi.age.admin.client.ui.module.modeled.XEditorPanel;
import uk.ac.ebi.age.admin.client.ui.module.modeled.XSubclassesPanel;
import uk.ac.ebi.age.admin.client.ui.module.modeled.XSuperclassesPanel;

import com.smartgwt.client.widgets.Canvas;

public class ClassMetaClassDef implements MetaClassDef
{

 @Override
 public Collection<Canvas> createDetailsPanels(AgeAbstractClassImprint cls, XEditorPanel editor)
 {
  ArrayList<Canvas> panels = new ArrayList<Canvas>(5);

  Canvas pnl = new ClassCommonsPanel(cls, editor);
  panels.add(pnl);

  pnl = new XSuperclassesPanel(cls, editor);
  panels.add(pnl);

  pnl = new XSubclassesPanel(cls, editor);
  panels.add(pnl);

  pnl = new RestrictionPanel( (AgeClassImprint)cls, editor );
  panels.add(pnl);

  pnl = new AttributesPanel( cls, editor );
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
}
