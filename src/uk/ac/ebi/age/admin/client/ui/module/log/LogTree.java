package uk.ac.ebi.age.admin.client.ui.module.log;

import java.util.List;

import uk.ac.ebi.age.ext.log.LogNode;

import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;

public class LogTree extends TreeGrid
{
 Tree data = new Tree();
 
 public LogTree( LogNode root )
 {
  setWidth100();
  setHeight100();
  
  setShowConnectors(true);
  setShowHeader(false);
  
//  Tree data = new Tree();
  
  if( root == null )
  {
   data.setRoot(new TreeNode());
   
   return;
  }
  
  
  TreeNode rootNode = new TreeNode();
  

  LogTreeNode  clsRoot = new LogTreeNode(root);
  clsRoot.setTitle("Log");
  
  data.setRoot(rootNode);
  data.addList(new TreeNode[] { clsRoot } , rootNode);

  createTreeStructure(root, clsRoot);
  
  setData(data);
  
//  data.openFolder(clsRoot);
  
//  expandRecord(clsRoot);

//  data.openAll();

 }
 
 protected void onDraw()
 {
  super.onDraw();
  
  TreeNode rn = data.getRoot();
  TreeNode[] rCh = data.getChildren(rn);
  
  data.openFolder( rCh[0] );
 }
 
 private void createTreeStructure(LogNode cls, LogTreeNode node)
 {
  
  if( cls.getSubNodes() == null)
   return;

  List<? extends LogNode> subNodes = cls.getSubNodes();
  
  TreeNode[] children = new TreeNode[ subNodes.size() ];


  for( int i=0; i < subNodes.size(); i++ )
  {
   LogNode subLn = subNodes.get(i);
   
   LogTreeNode snd = new LogTreeNode( subLn );
   children[i] = snd;

   createTreeStructure(subLn, snd);
  }
  
  data.addList(children, node);
 }
}
