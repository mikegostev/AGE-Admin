package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.model.ModelStorage;

import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;

public class ModelStoreTree extends VLayout
{
 private TreeGrid treeGrid;
 
 public ModelStoreTree()
 {
  TreeGrid treeGrid = new TreeGrid();
  
  treeGrid.setShowHeader(false);
  treeGrid.setShowConnectors(true);
  treeGrid.setShowRoot(false);
  treeGrid.setShowAllRecords(true);
  treeGrid.setTitleField("Name");
  treeGrid.setFields( new TreeGridField("Name") );
  
  Tree data = new Tree();
  data.setModelType(TreeModelType.CHILDREN);
  data.setNameProperty("Name");
  data.setChildrenProperty("children");
  treeGrid.setData(data);

  TreeNode rootNode = new TreeNode( "Root" );
  data.setRoot(rootNode);

  treeGrid.setWidth100();
  treeGrid.setHeight100();
  
  addMember(treeGrid);
 }

 public void setStorage(ModelStorage modst)
 {
  TreeNode nRoot = new TreeNode();
  
 }
}
