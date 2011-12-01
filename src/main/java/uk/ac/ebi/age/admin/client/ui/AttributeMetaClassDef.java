package uk.ac.ebi.age.admin.client.ui;

import java.util.ArrayList;
import java.util.Collection;

import uk.ac.ebi.age.admin.client.ModeledIcons;
import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeAttributeClassImprint;
import uk.ac.ebi.age.admin.client.model.AttributeType;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.ui.module.modeled.AttributeCommonsPanel;
import uk.ac.ebi.age.admin.client.ui.module.modeled.AttributeRuleAttachPanel;
import uk.ac.ebi.age.admin.client.ui.module.modeled.XEditorPanel;
import uk.ac.ebi.age.admin.client.ui.module.modeled.XHierarchyPanel;

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
 public Collection<PanelInfo> createDetailsPanels(AgeAbstractClassImprint cls, XEditorPanel editor)
 {
  ArrayList<PanelInfo> panels = new ArrayList<PanelInfo>(5);

  PanelInfo pinf = new PanelInfo();
  panels.add(pinf);
  
  Canvas pnl = new AttributeCommonsPanel((AgeAttributeClassImprint)cls, editor);
  pinf.setPanel(pnl);
  pinf.setTitle("Common properties");
  pinf.setIcon(ModeledIcons.get.commonProperties());

  
  pinf = new PanelInfo();
  panels.add(pinf);

  pnl = new XHierarchyPanel(cls, editor);
  pinf.setPanel(pnl);
  pinf.setTitle("Hierarchy");
  pinf.setIcon(ModeledIcons.get.hierarchy());

  
  pinf = new PanelInfo();
  panels.add(pinf);
 
  pnl = new AttributeRuleAttachPanel( (AgeAttributeClassImprint)cls, editor );
  pinf.setPanel(pnl);
  pinf.setTitle("Attribute rules");
  pinf.setIcon(ModeledIcons.get.attributeRules());

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
  return ModeledIcons.get.getString("attribute"+atyp.name());
 }

 @Override
 public String getClassIcon(AgeAbstractClassImprint classImprint)
 {
  return getIcon((AgeAttributeClassImprint)classImprint);
 }



}
