package uk.ac.ebi.age.admin.client.ui.module.classif;

import com.smartgwt.client.widgets.tab.Tab;

public class ClassifiersAdminTab extends Tab
{
 public ClassifiersAdminTab()
 {
  super("Classifiers");
  setPane( new ClassifiersPanel( )  );
 }

}
