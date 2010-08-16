package uk.ac.ebi.age.admin.client.ui;

import java.util.Collection;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeAnnotationClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeAttributeClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeRelationClassImprint;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.ui.module.modeled.XEditorPanel;

import com.smartgwt.client.widgets.Canvas;

public abstract class MetaClassDef
{

 public abstract String getMetaClassName();

 public abstract ImprintTreeNode createTreeNode(AgeAbstractClassImprint root);

 public abstract Collection<? extends AgeAbstractClassImprint> getXClasses(ModelImprint model);

 public abstract Collection<Canvas> createDetailsPanels(AgeAbstractClassImprint cls, XEditorPanel clsPanel);

 public abstract AgeAbstractClassImprint getRoot(ModelImprint mod);
 
 public static MetaClassDef getMetaClass( AgeAbstractClassImprint cls )
 {
  if( cls instanceof AgeClassImprint )
   return ClassMetaClassDef.getInstance();
  else if( cls instanceof AgeAttributeClassImprint )
   return AttributeMetaClassDef.getInstance();
  else if( cls instanceof AgeRelationClassImprint )
   return RelationMetaClassDef.getInstance();
  else if( cls instanceof AgeAnnotationClassImprint )
   return AnnotationMetaClassDef.getInstance();

  return null;
 }

 
}
