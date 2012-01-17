package uk.ac.ebi.age.admin.client.ui;

import com.smartgwt.client.widgets.Canvas;

public class PanelInfo
{
 private String title;
 private String icon;
 private Canvas panel;
 
 public String getIcon()
 {
  return icon;
 }
 
 public void setIcon(String icon)
 {
  this.icon = icon;
 }
 
 public Canvas getPanel()
 {
  return panel;
 }
 
 public void setPanel(Canvas panel)
 {
  this.panel = panel;
 }

 public String getTitle()
 {
  return title;
 }

 public void setTitle(String title)
 {
  this.title = title;
 }
}
