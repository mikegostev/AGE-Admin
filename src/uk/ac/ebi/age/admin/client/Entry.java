package uk.ac.ebi.age.admin.client;

import uk.ac.ebi.age.admin.client.ui.module.RootTabPanel;

import com.google.gwt.core.client.EntryPoint;

public class Entry implements EntryPoint
{

 @Override
 public void onModuleLoad()
 {
  new RootTabPanel().draw();
 }
}
