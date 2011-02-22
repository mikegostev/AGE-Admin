package uk.ac.ebi.age.admin.client.ui.module.log;

import uk.ac.ebi.age.admin.client.log.LogNode;

import com.google.gwt.core.client.JsArray;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;

public class LogTree extends TreeGrid
{
 public LogTree( LogNode root )
 {
  setWidth100();
  setHeight100();
  
  Tree data = new Tree();
  setData(data);
  
  
  if( root == null )
  {
   data.setRoot(new TreeNode());
   
   return;
  }
  
  
  TreeNode rootNode = new TreeNode();
  

  LogTreeNode  clsRoot = new LogTreeNode(root);
  
  createTreeStructure(root, clsRoot);

  data.setRoot(rootNode);
  
  data.addList(new TreeNode[] { clsRoot } , rootNode);
  
//  data.openAll();

 }
 
 private void createTreeStructure(LogNode cls, LogTreeNode node)
 {
  
  if( cls.getSubnodes() == null)
   return;

  JsArray<LogNode> subNodes = cls.getSubnodes();
  
  TreeNode[] children = new TreeNode[ subNodes.length() ];


  for( int i=0; i < subNodes.length(); i++ )
  {
   LogNode subLn = subNodes.get(i);
   
   LogTreeNode snd = new LogTreeNode( subLn );
   children[i++] = snd;

   createTreeStructure(subLn, snd);
  }
  
  getData().addList(children, node);
 }
}
