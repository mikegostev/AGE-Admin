package uk.ac.ebi.age.admin.client.ui.module.auth;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;

public class ProfileAddDialog extends Window
{
 
 private final CloseClickHandler clsHnd = new CloseClickHandler()
 {
  @Override
  public void onCloseClick(CloseClickEvent event)
  {
   close();
  }
 };

 public ProfileAddDialog(DataSource ds)
 {
  setTitle("Add Profile");
  setShowMinimizeButton(false);
  setIsModal(true);
  setShowModalMask(true);
  setAutoSize(true);
  setAutoCenter(true);

  addCloseClickHandler( clsHnd );
  
  addItem( new ProfileAddForm(ds,clsHnd) );
 }

 @Override
 public void close()
 {
  destroy();
 }

}
