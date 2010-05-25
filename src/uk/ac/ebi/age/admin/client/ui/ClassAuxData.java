package uk.ac.ebi.age.admin.client.ui;

import java.util.Collection;
import java.util.LinkedList;

public class ClassAuxData
{
 private Collection<ClassTreeNode> nodes = new LinkedList<ClassTreeNode>();

 public Collection<ClassTreeNode> getNodes()
 {
  return nodes;
 }
 
 public void addNode( ClassTreeNode nd )
 {
  nodes.add(nd);
 }
}
