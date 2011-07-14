package uk.ac.ebi.age.admin.client.ui.module.classif;

import java.util.Collection;

import uk.ac.ebi.age.ext.authz.TagRef;

public interface TagSelectedListener
{
 void tagSelected( Collection<TagRef> tr );
}
