package uk.ac.ebi.age.admin.client.ui;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;

import com.smartgwt.client.widgets.tree.TreeNode;

public abstract class ImprintTreeNode extends TreeNode
{
 private AgeAbstractClassImprint           cls;

 public ImprintTreeNode( AgeAbstractClassImprint cl )
 {
  cls=cl;
  
  setTitle(cl.getName());
  setIcon("../images/icons/"+getMetaClassname()+"/"+(cl.isAbstract()?"abstract.png":"regular.png"));
 }
 
// public ClassTreeNode( String nm )
// {
//  setTitle(nm);
// }
 
 public abstract String getMetaClassname(); 

 public AgeAbstractClassImprint getClassImprint()
 {
  return cls;
 }

 public void setClassImprint(AgeAbstractClassImprint cls)
 {
  this.cls = cls;
 }

 public void setAbstract(boolean abstr)
 {
  setIcon("../images/icons/"+getMetaClassname()+"/"+(abstr?"abstract.png":"regular.png"));
 }

}
