package uk.ac.ebi.age.admin.client.ui;

import java.util.ArrayList;
import java.util.Collection;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeAttributeClassImprint;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.ui.module.modeled.AttributesPanel;
import uk.ac.ebi.age.admin.client.ui.module.modeled.ClassCommonsPanel;
import uk.ac.ebi.age.admin.client.ui.module.modeled.XEditorPanel;
import uk.ac.ebi.age.admin.client.ui.module.modeled.XSubclassesPanel;
import uk.ac.ebi.age.admin.client.ui.module.modeled.XSuperclassesPanel;

import com.smartgwt.client.widgets.Canvas;

public class AttributeMetaClassDef implements MetaClassDef
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

  pnl = new AttributesPanel( cls, editor );
  panels.add(pnl);

  
  return panels;
 }

 @Override
 public ImprintTreeNode createTreeNode(AgeAbstractClassImprint root)
 {
  return new AttributeTreeNode((AgeAttributeClassImprint)root);
 }

 @Override
 public String getMetaClassName()
 {
  return "attribute";
 }

 @Override
 public Collection< ? extends AgeAbstractClassImprint> getXClasses(ModelImprint model)
 {
  return model.getAttributes();
 }

 @Override
 public AgeAbstractClassImprint getRoot(ModelImprint mod)
 {
  return mod.getRootAttributeClass();
 }

}
