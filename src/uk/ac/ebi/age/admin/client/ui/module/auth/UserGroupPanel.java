package uk.ac.ebi.age.admin.client.ui.module.auth;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class UserGroupPanel extends HLayout
{

 public UserGroupPanel(AuthAdminPanel authAdminPanel)
 {
  setWidth100();
  setHeight100();
  
  VLayout vl = new VLayout();
  
  vl.setWidth("50%");
  addMember(vl);
  
  UserList ul = new UserList();
  ul.setHeight("50%");
  
  vl.addMember(ul);
  
  addMember(new Canvas());
 }

 
 
}
