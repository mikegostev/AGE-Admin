package uk.ac.ebi.age.admin.client.ui.module;

import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;

public class ClassEditorPanel extends HLayout
{
 public ClassEditorPanel()
 {
  TreeGrid treeGrid = new TreeGrid();
  treeGrid.setWidth("30%");
  treeGrid.setShowConnectors(true);
  treeGrid.setShowResizeBar(true);
  treeGrid.setShowRoot(true);

  Tree data = new Tree();
  data.setModelType(TreeModelType.CHILDREN);
  data.setRoot(new TreeNode("root", new TreeNode("File"), new TreeNode("Edit"), new TreeNode("Search"), new TreeNode("Project"), new TreeNode("Tools"),
    new TreeNode("Window"), new TreeNode("Favourites")));

  treeGrid.setData(data);

  TreeGridField field = new TreeGridField("Navigation");
  field.setCellFormatter(new CellFormatter()
  {

   public String format(Object value, ListGridRecord record, int rowNum, int colNum)
   {
    return record.getAttribute("name");
   }
  });
  treeGrid.setFields(field);

 }
}
