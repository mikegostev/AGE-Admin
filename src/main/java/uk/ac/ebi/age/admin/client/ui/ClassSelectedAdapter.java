package uk.ac.ebi.age.admin.client.ui;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;

public abstract class ClassSelectedAdapter implements ClassSelectedCallback
{

 @Override
 public abstract void classSelected(AgeAbstractClassImprint cls);


 @Override
 public void selectionCanceled()
 {
 }

}
