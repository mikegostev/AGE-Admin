package uk.ac.ebi.age.admin.client.ui.module.auth;

import uk.ac.ebi.age.admin.shared.Constants;

import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class AuthAdminPanel extends TabSet
{
 private UserGroupPanel userGroups;
 private ProfilesPanel profileGroups;

 public AuthAdminPanel()
 {
  setTabBarPosition(Side.TOP);  
  setWidth100();  
  setHeight100();  

  Tab genTab = new Tab("Users & Groups");
  genTab.setIcon("icons/auth/user.png");
  genTab.setPane( userGroups=new UserGroupPanel( this )  );
  
  addTab(genTab);

  genTab = new Tab("Profiles");
  genTab.setIcon("icons/auth/profile.png");
  genTab.setPane( profileGroups=new ProfilesPanel( this )  );
  
  addTab(genTab);

  genTab = new Tab("System permissions");
  genTab.setIcon("icons/auth/flag_pink.png");
  genTab.setPane( new ACLPanel(null, null, Constants.sysACLServiceName) );
  
  addTab(genTab);

 }
 
}
