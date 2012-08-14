package uk.ac.ebi.age.admin.client.ui.module.log;


import uk.ac.ebi.age.ext.log.LogNode;

import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;

public class LogWindow3 extends Window
{
 public LogWindow3( String title, LogNode rLn )
 {
  super();
  
  setWidth(1000);
  setHeight(600);
  centerInPage();
  setCanDragReposition(true);
  setCanDragResize(true);
  setKeepInParentRect(true);
  setTitle(title);
  
  final LogTree3 lgTree = new LogTree3(rLn);
  
  addCloseClickHandler( new CloseClickHandler()
  {
   @Override
   public void onCloseClick(CloseClickEvent event)
   {
    destroy();
   }
  });
 
  addItem( lgTree );

 }
}
