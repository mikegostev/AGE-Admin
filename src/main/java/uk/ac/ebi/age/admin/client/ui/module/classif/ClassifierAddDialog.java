package uk.ac.ebi.age.admin.client.ui.module.classif;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;

public class ClassifierAddDialog extends Window
{
 
 private final CloseClickHandler clsHnd = new CloseClickHandler()
 {
  @Override
  public void onCloseClick(CloseClickEvent event)
  {
   close();
  }
 };

 public ClassifierAddDialog(DataSource ds)
 {
  setTitle("Add Classifier");
  setShowMinimizeButton(false);
  setIsModal(true);
  setShowModalMask(true);
  setAutoSize(true);
  setAutoCenter(true);

  addCloseClickHandler( clsHnd );
  
  addItem( new ClassifierAddForm(ds,clsHnd) );
 }

 public void close()
 {
  destroy();
 }

}
