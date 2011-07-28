package uk.ac.ebi.age.admin.client.ui.module.log;

import java.util.List;

import uk.ac.ebi.age.ext.log.LogNode;

import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;

public class LogTree2 extends TreeGrid
{
 Tree data = new Tree();
 
 public LogTree2( LogNode root )
 {
  setWidth100();
  setHeight100();
  
  setShowConnectors(true);
  setShowHeader(false);
  
  setDataSource( new LogDataSource(root) );
  
  setAutoFetchData(true);
  
  setCustomIconProperty("status");
  
  setFields( new TreeGridField("name") );
//  setTitleField("message");
  
//  data.openFolder(clsRoot);
  
//  expandRecord(clsRoot);

//  data.openAll();

 }
 
// protected void onDraw()
// {
//  super.onDraw();
//  
//  TreeNode rn = data.getRoot();
//  TreeNode[] rCh = data.getChildren(rn);
//  
//  data.openFolder( rCh[0] );
// }
 
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
