package uk.ac.ebi.age.admin.client.ui.module.auth;

import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class UserGroupPanel extends VLayout
{

 public UserGroupPanel(AuthAdminPanel authAdminPanel)
 {
  setWidth100();
  setHeight100();
  
  HLayout row = new HLayout();
  
  row.setHeight("50%");
  row.setBorder("1px solid black");
  addMember(row);
  
  VLayout userGroups = new VLayout();
  userGroups.setWidth("50%");
  userGroups.setHeight100();
  
  UserList ul = new UserList( userGroups );
  ul.setWidth("50%");
  
  row.addMember(ul);
  row.addMember(userGroups);
  
  row = new HLayout();
  row.setBorder("1px solid black");
  row.setHeight("50%");
  addMember(row);
  
  VLayout groupUsers = new VLayout();
  groupUsers.setWidth("50%");
  
  GroupList gl = new GroupList(groupUsers);
  gl.setWidth("50%");
  
  row.addMember(gl);
  row.addMember(groupUsers);

  
 }

 
 
}
