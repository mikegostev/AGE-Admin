package uk.ac.ebi.age.admin.client.ui.module.auth;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;

public class GroupAddDialog extends Window
{
 
 private final CloseClickHandler clsHnd = new CloseClickHandler()
 {

  @Override
  public void onCloseClick(CloseClickEvent event)
  {
   close();
  }
 };

 public GroupAddDialog(DataSource ds)
 {
  setTitle("Add Group");
  setShowMinimizeButton(false);
  setIsModal(true);
  setShowModalMask(true);
  setAutoSize(true);
  setAutoCenter(true);

  addCloseClickHandler( clsHnd );
  
  addItem( new GroupAddForm(ds,clsHnd) );
 }

 public void close()
 {
  destroy();
 }

}
