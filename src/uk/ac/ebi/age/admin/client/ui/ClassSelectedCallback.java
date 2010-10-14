package uk.ac.ebi.age.admin.client.ui;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;

public interface ClassSelectedCallback
{
 void classSelected( AgeAbstractClassImprint cls );
 void selectionCanceled();
}
