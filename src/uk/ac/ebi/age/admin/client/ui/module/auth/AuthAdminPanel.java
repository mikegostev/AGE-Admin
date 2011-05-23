package uk.ac.ebi.age.admin.client.ui.module.auth;

import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class AuthAdminPanel extends TabSet
{
 private UserGroupPanel userGroups;

 public AuthAdminPanel()
 {
  setTabBarPosition(Side.TOP);  
  setWidth100();  
  setHeight100();  

  Tab genTab = new Tab("Users & Groups");
  genTab.setPane( userGroups=new UserGroupPanel( this )  );
  
  addTab(genTab);

 }
 
}
