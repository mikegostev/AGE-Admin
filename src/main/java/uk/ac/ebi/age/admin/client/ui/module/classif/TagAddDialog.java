package uk.ac.ebi.age.admin.client.ui.module.classif;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;

public class TagAddDialog extends Window
{
 
 private CloseClickHandler clsHnd = new CloseClickHandler()
 {
  @Override
  public void onCloseClick(CloseClientEvent event)
  {
   close();
  }
 };

 public TagAddDialog(DataSource ds, String parent)
 {
  setTitle("Add Tag");
  setShowMinimizeButton(false);
  setIsModal(true);
  setShowModalMask(true);
  setAutoSize(true);
  setAutoCenter(true);

  addCloseClickHandler( clsHnd );
  
  addItem( new TagAddForm(ds, parent, clsHnd) );
 }

 public void close()
 {
  destroy();
 }

}
