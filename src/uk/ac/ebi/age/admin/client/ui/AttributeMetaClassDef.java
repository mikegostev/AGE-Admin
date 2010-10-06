package uk.ac.ebi.age.admin.client.ui;

import java.util.ArrayList;
import java.util.Collection;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeAttributeClassImprint;
import uk.ac.ebi.age.admin.client.model.AttributeType;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.ui.module.modeled.AttributeCommonsPanel;
import uk.ac.ebi.age.admin.client.ui.module.modeled.AttributeRuleAttachPanel;
import uk.ac.ebi.age.admin.client.ui.module.modeled.XEditorPanel;
import uk.ac.ebi.age.admin.client.ui.module.modeled.XSubclassesPanel;
import uk.ac.ebi.age.admin.client.ui.module.modeled.XSuperclassesPanel;

import com.smartgwt.client.widgets.Canvas;

public class AttributeMetaClassDef extends MetaClassDef
{
 private static AttributeMetaClassDef instance = new AttributeMetaClassDef();
 
 public static AttributeMetaClassDef getInstance()
 {
  return instance;
 }

 private AttributeMetaClassDef()
 {}
 
 @Override
 public Collection<Canvas> createDetailsPanels(AgeAbstractClassImprint cls, XEditorPanel editor)
 {
  ArrayList<Canvas> panels = new ArrayList<Canvas>(5);

  Canvas pnl = new AttributeCommonsPanel((AgeAttributeClassImprint)cls, editor);
  panels.add(pnl);

  pnl = new XSuperclassesPanel(cls, editor);
  panels.add(pnl);

  pnl = new XSubclassesPanel(cls, editor);
  panels.add(pnl);

  pnl = new AttributeRuleAttachPanel( (AgeAttributeClassImprint)cls, editor );
  panels.add(pnl);

//  pnl = new AttributeAttachPanel( cls, editor );
//  panels.add(pnl);

  
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

 public static String getIcon(AgeAttributeClassImprint classImprint)
 {
  return getIcon(classImprint.getType());
 }

 public static String getIcon(AttributeType atyp)
 {
  
  switch(atyp)
  {
   case STRING:
    return "../images/icons/attribute/regular.png";
 
   case ABSTRACT:
    return "../images/icons/attribute/abstract.png";

   default:
    return "../images/icons/attribute/tag_green.png";
  }
 }



}
