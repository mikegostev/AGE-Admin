package uk.ac.ebi.age.admin.client.ui;

import java.util.ArrayList;
import java.util.Collection;

import uk.ac.ebi.age.admin.client.ModeledIcons;
import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeRelationClassImprint;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.ui.module.modeled.AttributeRuleAttachPanel;
import uk.ac.ebi.age.admin.client.ui.module.modeled.RangeDomainPanel;
import uk.ac.ebi.age.admin.client.ui.module.modeled.RelationCommonsPanel;
import uk.ac.ebi.age.admin.client.ui.module.modeled.XEditorPanel;
import uk.ac.ebi.age.admin.client.ui.module.modeled.XHierarchyPanel;

import com.smartgwt.client.widgets.Canvas;

public class RelationMetaClassDef extends MetaClassDef
{
 private static RelationMetaClassDef instance = new RelationMetaClassDef();
 
 public static RelationMetaClassDef getInstance()
 {
  return instance;
 }

 private RelationMetaClassDef()
 {}
 
 @Override
 public Collection<PanelInfo> createDetailsPanels(AgeAbstractClassImprint cls, XEditorPanel editor)
 {
  ArrayList<PanelInfo> panels = new ArrayList<PanelInfo>(5);

  PanelInfo pinf = null;
  
  Canvas pnl = new RelationCommonsPanel((AgeRelationClassImprint)cls, editor);
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
 
  pnl = new RangeDomainPanel((AgeRelationClassImprint)cls, editor);
  pinf = new PanelInfo();
  panels.add(pinf);
  pinf.setPanel(pnl);
  pinf.setTitle("Range / Domain");
  pinf.setIcon(ModeledIcons.get.rangeDomain());

  pnl = new AttributeRuleAttachPanel( (AgeRelationClassImprint)cls, editor );
  pinf = new PanelInfo();
  panels.add(pinf);
  pinf.setPanel(pnl);
  pinf.setTitle("Attribute rules");
  pinf.setIcon( ModeledIcons.get.attributeRules());

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

 @Override
 public String getClassIcon(AgeAbstractClassImprint classImprint)
 {
  return getIcon(classImprint);
 }
 
 public static String getIcon(AgeAbstractClassImprint classImprint)
 {
  if( classImprint.isAbstract() )
   return ModeledIcons.get.relationAbstract();
  else
   return ModeledIcons.get.relation();
 }

}
