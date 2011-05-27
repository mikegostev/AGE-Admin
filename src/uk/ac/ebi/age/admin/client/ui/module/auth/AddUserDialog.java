package uk.ac.ebi.age.admin.client.ui.module.auth;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;

public class AddUserDialog extends Window
{

 public AddUserDialog(DataSource ds)
 {
//  setWidth(300);
//  setHeight(200);
  setTitle("Add user");
  setShowMinimizeButton(false);
  setIsModal(true);
  setShowModalMask(true);
//  centerInPage();
  setAutoSize(true);
  setAutoCenter(true);

  addCloseClickHandler( new CloseClickHandler()
  {
   @Override
   public void onCloseClick(CloseClientEvent event)
   {
    close();
   }
  });
  
  addItem( new UserAddForm(ds) );
 }

 public void close()
 {
  destroy();
 }

}
