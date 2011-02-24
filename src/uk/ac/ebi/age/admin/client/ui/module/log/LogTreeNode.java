package uk.ac.ebi.age.admin.client.ui.module.log;

import uk.ac.ebi.age.admin.client.log.LogNode;

import com.smartgwt.client.widgets.tree.TreeNode;

public class LogTreeNode extends TreeNode
{
 
 public LogTreeNode( LogNode ln )
 {
//  setTitle(ln.getMessage());
  setIcon("../images/icons/log/"+ln.getLevel()+".png");
  setTitle("<span class='logMsg"+ln.getLevel()+"'>"+ln.getMessage()+"</span>");
  setAttribute("Name", "<span class='logMsg"+ln.getLevel()+"'>"+ln.getMessage()+"</span>");
 }

}
