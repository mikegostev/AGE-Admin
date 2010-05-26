package uk.ac.ebi.age.admin.client.ui;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;

public interface NodeCreator //<T extends AgeAbstractClassImprint>
{
 ImprintTreeNode create(AgeAbstractClassImprint cls);
}
