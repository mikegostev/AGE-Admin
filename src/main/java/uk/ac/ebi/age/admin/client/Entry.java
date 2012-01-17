package uk.ac.ebi.age.admin.client;

import uk.ac.ebi.age.admin.client.ui.module.RootTabPanel;

import com.google.gwt.core.client.EntryPoint;
import com.smartgwt.client.util.Page;

public class Entry implements EntryPoint
{

 @Override
 public void onModuleLoad()
 {
  Page.setAppImgDir("[APP]admin_images/");
  new RootTabPanel().draw();
 }
}
