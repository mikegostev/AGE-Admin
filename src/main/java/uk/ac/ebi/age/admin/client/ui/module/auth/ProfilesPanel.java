package uk.ac.ebi.age.admin.client.ui.module.auth;

import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class ProfilesPanel extends HLayout
{

 public ProfilesPanel(AuthAdminPanel authAdminPanel)
 {
  setWidth100();
  setHeight100();
  
  
  VLayout permPanel = new VLayout();
  permPanel.setWidth("50%");
  permPanel.setHeight100();
  
  ProfileListPanel ul = new ProfileListPanel( permPanel );
  ul.setWidth("50%");
  
  addMember(ul);
  addMember(permPanel);
  
 }

 
 
}
