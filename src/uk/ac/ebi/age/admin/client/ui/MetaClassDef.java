package uk.ac.ebi.age.admin.client.ui;

import java.util.Collection;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.ui.module.modeled.XEditorPanel;

import com.smartgwt.client.widgets.Canvas;

public interface MetaClassDef
{

 String getMetaClassName();

 ImprintTreeNode createTreeNode(AgeAbstractClassImprint root);

 Collection<? extends AgeAbstractClassImprint> getXClasses(ModelImprint model);

 Collection<Canvas> createDetailsPanels(AgeAbstractClassImprint cls, XEditorPanel clsPanel);

 AgeAbstractClassImprint getRoot(ModelImprint mod);

}
