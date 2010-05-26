package uk.ac.ebi.age.admin.client.ui.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import uk.ac.ebi.age.admin.client.model.AgeClassImprint;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.ui.ClassTreeNode;

import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;

public class ClassTreePanel extends TreeGrid
{
 private Map<AgeClassImprint, Collection<ClassTreeNode>> nodeMap = new HashMap<AgeClassImprint, Collection<ClassTreeNode>>();
 
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
  
  addSelectionChangedHandler( new SelectionChangedHandler()
  {
   @Override
   public void onSelectionChanged(SelectionEvent event)
   {
    ClassTreeNode ctn = (ClassTreeNode)event.getRecord();
    
    Collection<ClassTreeNode> coll = nodeMap.get(ctn.getCls());
    
    if( coll.size() == 1 )
     return;
    
    for(ClassTreeNode othnd : coll )
    {
     if( othnd != ctn )
     {
      othnd.set_baseStyle(event.getState()?"sameClassHighlite treeCell":"treeCell");
     }
    }
    
    redraw();
    
   }
  });
  
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
  addNodeToMap(node);
  
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
 
 private void addNodeToMap( ClassTreeNode node )
 {
  Collection<ClassTreeNode> coll = nodeMap.get(node.getCls());
  
  if( coll == null )
  {
   coll = new ArrayList<ClassTreeNode>(5);
   nodeMap.put(node.getCls(), coll);
  }
  
  coll.add(node);
 }


 public void addBranch(AgeClassImprint superClass, AgeClassImprint subClass)
 {
  for( ClassTreeNode supNodes  : nodeMap.get(superClass) )
  {
   ClassTreeNode nd = new ClassTreeNode(subClass);
   createTreeStructure(subClass, nd);
   getData().add(nd, supNodes);
   
   getData().openAll(supNodes);
   
//   System.out.println("Adding "+subClass.getName()+" to "+supNodes.getTitle());
  }
 }
 
 void updateClassName(AgeClassImprint classImprint, String newName )
 {
  classImprint.setName(newName);
  
  for(ClassTreeNode tn : nodeMap.get(classImprint) )
  {
   tn.setTitle(newName);
  }
  
  getData().setRoot(getData().getRoot());
 }

 void updateClassType(AgeClassImprint classImprint, boolean abstr )
 {
  classImprint.setAbstract(abstr);

  
  for(ClassTreeNode tn : nodeMap.get(classImprint) )
  {
   tn.setAbstract(abstr);
  }
  
  
  getData().setRoot(getData().getRoot());
 }
 
}
