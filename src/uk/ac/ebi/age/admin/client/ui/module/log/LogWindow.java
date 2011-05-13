package uk.ac.ebi.age.admin.client.ui.module.log;


import uk.ac.ebi.age.ext.log.LogNode;

import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;

public class LogWindow extends Window
{
 public LogWindow( String title, LogNode rLn )
 {
  super();
  
  setWidth(1000);
  setHeight(600);
  centerInPage();
  setCanDragReposition(true);
  setCanDragResize(true);
  setKeepInParentRect(true);
  setTitle(title);
  
  final LogTree lgTree = new LogTree(rLn);
  
  addCloseClickHandler( new CloseClickHandler()
  {
   @Override
   public void onCloseClick(CloseClientEvent event)
   {
    destroy();
   }
  });
 
  addItem( lgTree );

 }
}
