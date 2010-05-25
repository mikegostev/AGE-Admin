package uk.ac.ebi.age.admin.client.ui.module;

import uk.ac.ebi.age.admin.client.model.AgeClassImprint;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.ui.ClassAuxData;
import uk.ac.ebi.age.admin.client.ui.ClassTreeNode;

import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;

public class ClassTreePanel extends TreeGrid
{
 ClassTreePanel( ModelImprint mod )
 {
  setShowHeader(false);
  
  setShowConnectors(true);
  setShowRoot(false);
  setTitleField("Name");
  
  Tree data = new Tree();
  data.setModelType(TreeModelType.CHILDREN);
  setData(data);

  TreeNode rootNode = new TreeNode( "Root" );
  data.setRoot(rootNode);

  if( mod == null )
  {
   ClassTreeNode classRoot =  new ClassTreeNode("AgeClass");
   classRoot.setIcon("../images/icons/class/abstract.png");
   
   rootNode.setChildren( new TreeNode[] { classRoot } );
  }
  else
  {
   setModel(mod);
  }
  
 }

 
 public void setModel(ModelImprint mod)
 {
  Tree data = getData();
  
  TreeNode rootNode =data.getRoot();
  

  ClassTreeNode  clsRoot = new ClassTreeNode(mod.getRootClass());
  
  createTreeStructure(mod.getRootClass(), clsRoot);

  rootNode.setChildren( new TreeNode[] { clsRoot } );
  
  data.setRoot(rootNode);
  
  data.openAll();
 }
 
 private void createTreeStructure(AgeClassImprint cls, ClassTreeNode node)
 {
  ((ClassAuxData)cls.getAuxData()).addNode(node);
  
  if(cls.getChildren() == null)
   return;

  TreeNode[] children = new TreeNode[cls.getChildren().size()];


  int i = 0;
  for(AgeClassImprint subcls : cls.getChildren())
  {
   ClassTreeNode ctn = new ClassTreeNode(subcls);
   children[i++] = ctn;

   createTreeStructure(subcls, ctn);
  }

  node.setChildren(children);

 }
}
