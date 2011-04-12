package uk.ac.ebi.age.admin.client.ui;

import com.smartgwt.client.widgets.Canvas;

public class PlacingManager
{
 private static PlacingManager instance;
 
 public static void setManager( PlacingManager m )
 {
  instance=m;
 }
 
 public static void placeWidget( Canvas pnl, String title )
 {
  instance._placeWidget( pnl, title );
 }

 protected void _placeWidget(Canvas pnl, String title)
 {
 }
}
