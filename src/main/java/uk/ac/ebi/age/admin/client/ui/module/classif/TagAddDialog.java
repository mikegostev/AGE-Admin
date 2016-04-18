package uk.ac.ebi.age.admin.client.ui.module.classif;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;

public class TagAddDialog extends Window
{
 
 private final CloseClickHandler clsHnd = new CloseClickHandler()
 {
  @Override
  public void onCloseClick(CloseClickEvent event)
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

 @Override
 public void close()
 {
  destroy();
 }

}
