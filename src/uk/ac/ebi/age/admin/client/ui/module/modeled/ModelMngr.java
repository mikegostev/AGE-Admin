package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.shared.ModelPath;

public interface ModelMngr
{

 void setNewModel();

 void saveModel(ModelImprint model);

 void loadModel(ModelPath selectedNode);

 void installModel(ModelPath modelPath);

}
