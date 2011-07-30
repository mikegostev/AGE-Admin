package uk.ac.ebi.age.admin.client.ui.module.log;


import uk.ac.ebi.age.ext.log.LogNode;

import com.smartgwt.client.widgets.tree.TreeNode;

public class LogTreeNode extends TreeNode
{
 
 public LogTreeNode( LogNode ln )
 {
  setIcon("../admin_images/icons/log/"+ln.getLevel()+".png");
  setTitle("<span class='logMsg"+ln.getLevel()+"'>"+ln.getMessage()+"</span>");
  setAttribute("__obj", ln);
  
  boolean folder = ln.getSubNodes() != null && ln.getSubNodes().size() > 0 ;
  
  
  setIsFolder(folder);
 }
 
 public LogNode getNode()
 {
  return (LogNode) getAttributeAsObject("__obj");
 }

}
