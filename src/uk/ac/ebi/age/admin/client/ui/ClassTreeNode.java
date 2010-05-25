package uk.ac.ebi.age.admin.client.ui;

import uk.ac.ebi.age.admin.client.model.AgeClassImprint;

import com.smartgwt.client.widgets.tree.TreeNode;

public class ClassTreeNode extends TreeNode
{
 private AgeClassImprint           cls;

 public ClassTreeNode( AgeClassImprint cl )
 {
  cls=cl;
  
  setTitle(cl.getName());
  setIcon("../images/icons/class/"+(cl.isAbstract()?"abstract.png":"regular.png"));
 }
 
 public ClassTreeNode( String nm )
 {
  setTitle(nm);
 }
 

 public AgeClassImprint getCls()
 {
  return cls;
 }

 public void setCls(AgeClassImprint cls)
 {
  this.cls = cls;
 }

 public void setAbstract(boolean abstr)
 {
  setIcon("../images/icons/class/"+(abstr?"abstract.png":"regular.png"));
 }

}
