package uk.ac.ebi.age.admin.client.ui.module.auth;

import uk.ac.ebi.age.ext.authz.SystemAction;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.grid.GroupNode;
import com.smartgwt.client.widgets.grid.GroupTitleRenderer;
import com.smartgwt.client.widgets.grid.GroupValueFunction;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class PermissionList extends ListGrid
{
 public PermissionList()
 {
  final ListGrid list = this;
  list.setSelectionType(SelectionStyle.SINGLE);
  
  ListGridField icnField = new ListGridField("grpIcon", "");
  icnField.setWidth(30);
  icnField.setAlign(Alignment.CENTER);
  icnField.setType(ListGridFieldType.ICON);
  icnField.setIcon("icons/auth/permission.png");

  ListGridField idField = new ListGridField("pname", "Permission");
  idField.setWidth(200);

  idField.setGroupValueFunction(new GroupValueFunction()
  {
   public Object getGroupValue(Object value, ListGridRecord record, ListGridField field, String fieldName, ListGrid grid)
   {
    return record.getAttributeAsInt("grp");
   }
  });

  idField.setGroupTitleRenderer(new GroupTitleRenderer()
  {
   public String getGroupTitle(Object groupValue, GroupNode groupNode, ListGridField field, String fieldName, ListGrid grid)
   {
    final int groupType = (Integer) groupValue;

    return SystemAction.ActionGroup.values()[groupType].getDescription();
   }
  });

  ListGridField nameField = new ListGridField("pdesc","Description");
  nameField.setCanEdit(false);

  list.setFields(icnField, idField, nameField);
  
  list.setGroupByField("pname");
  
  list.setWidth100();
  list.setHeight100();

  list.setShowFilterEditor(true);
  list.setFilterOnKeypress(true);

  list.setShowAllRecords(true);

  for( SystemAction act : SystemAction.values() )
  {
   ListGridRecord r = new ListGridRecord();
   r.setAttribute("pname", act.name());
   r.setAttribute("pdesc", act.getDescription());
   r.setAttribute("grp", act.getGroup().ordinal());
   
   list.addData(r);
  }

 }
}
