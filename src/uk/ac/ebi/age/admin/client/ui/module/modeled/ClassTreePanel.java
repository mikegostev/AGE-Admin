package uk.ac.ebi.age.admin.client.ui.module.modeled;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;
import uk.ac.ebi.age.admin.client.ui.ImprintTreeNode;
import uk.ac.ebi.age.admin.client.ui.NodeCreator;

import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;

public class ClassTreePanel extends TreeGrid
{
 public enum Direction
 {
  PARENT2CHILD,
  CHILD2PARENT
 }

 private Direction direction;

 private Map<AgeAbstractClassImprint, Collection<ImprintTreeNode>> nodeMap = new HashMap<AgeAbstractClassImprint, Collection<ImprintTreeNode>>();
 private NodeCreator nodeCreator;
 

 ClassTreePanel( String title ,AgeAbstractClassImprint root, NodeCreator nc )
 {
  this(title, root,Direction.PARENT2CHILD,nc);
 }

 ClassTreePanel( AgeAbstractClassImprint root, NodeCreator nc )
 {
  this(null, root,Direction.PARENT2CHILD,nc);
 }

 ClassTreePanel(String title , AgeAbstractClassImprint root, Direction dir, NodeCreator nc )
 {
  direction=dir;
  nodeCreator = nc;
  
//  if( title!=null )
//  {
//   setGroupTitle(title);
////   setShowHeader(title!=null);
////   setFields( new TreeGridField(title,title) );
//  }
  
  setShowHeader(false);
  setShowConnectors(true);
  setShowRoot(false);
  setTitleField("Name");
  
  Tree data = new Tree();
  data.setModelType(TreeModelType.CHILDREN);
  setData(data);

  TreeNode rootNode = new TreeNode( "Root" );
  data.setRoot(rootNode);

  if( root != null )
   setRoot(root);
 
  addSelectionChangedHandler( new SelectionChangedHandler()
  {
   @Override
   public void onSelectionChanged(SelectionEvent event)
   {
    ImprintTreeNode ctn = (ImprintTreeNode)event.getRecord();
    
    Collection<ImprintTreeNode> coll = nodeMap.get(ctn.getClassImprint());
    
    if( coll.size() == 1 )
     return;
    
    for(ImprintTreeNode othnd : coll )
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

 
 public void setRoot(AgeAbstractClassImprint root)
 {
  Tree data = getData();

  if( root == null )
  {
   data.getRoot().setChildren( new TreeNode[0] );
   data.setRoot(data.getRoot());
   return;
  }
  
  
  TreeNode rootNode =data.getRoot();
  

  ImprintTreeNode  clsRoot = nodeCreator.create(root);
  
  createTreeStructure(root, clsRoot);

  rootNode.setChildren( new TreeNode[] { clsRoot } );
  
  data.setRoot(rootNode);
  
  data.openAll();
 }
 
 private void createTreeStructure(AgeAbstractClassImprint cls, ImprintTreeNode node)
 {
  addNodeToMap(node);
  
  if( (direction == Direction.PARENT2CHILD && cls.getChildren() == null) || (direction == Direction.CHILD2PARENT && cls.getParents() == null))
   return;

  TreeNode[] children = new TreeNode[ direction==Direction.PARENT2CHILD?cls.getChildren().size():cls.getParents().size()];


  int i = 0;
  for(AgeAbstractClassImprint subcls : direction==Direction.PARENT2CHILD?cls.getChildren():cls.getParents())
  {
   ImprintTreeNode ctn = nodeCreator.create(subcls);
   children[i++] = ctn;

   createTreeStructure(subcls, ctn);
  }

  node.setChildren(children);

 }
 
 private void addNodeToMap( ImprintTreeNode node )
 {
  Collection<ImprintTreeNode> coll = nodeMap.get(node.getClassImprint());
  
  if( coll == null )
  {
   coll = new ArrayList<ImprintTreeNode>(5);
   nodeMap.put(node.getClassImprint(), coll);
  }
  
  coll.add(node);
 }


 public void addBranch(AgeAbstractClassImprint superClass, AgeAbstractClassImprint subClass)
 {
  for( ImprintTreeNode supNodes  : nodeMap.get(superClass) )
  {
   ImprintTreeNode nd = nodeCreator.create(subClass);
   createTreeStructure(subClass, nd);
   getData().add(nd, supNodes);
   
   getData().openAll(supNodes);
   
//   System.out.println("Adding "+subClass.getName()+" to "+supNodes.getTitle());
  }
 }
 
 void updateClassName(AgeAbstractClassImprint classImprint, String newName )
 {
  classImprint.setName(newName);
  
  for(ImprintTreeNode tn : nodeMap.get(classImprint) )
  {
   tn.setTitle(newName);
  }
  
  getData().setRoot(getData().getRoot());
 }

 void updateClassType(AgeAbstractClassImprint classImprint, boolean abstr )
 {
  classImprint.setAbstract(abstr);

  
  for(ImprintTreeNode tn : nodeMap.get(classImprint) )
  {
   tn.updateType();
  }
  
  
  getData().setRoot(getData().getRoot());
 }

 public void removeClass(AgeAbstractClassImprint cimp)
 {

  for(ImprintTreeNode tn : nodeMap.get(cimp) )
  {
   getData().remove(tn);
  }
  
  nodeMap.remove(cimp);
 }
 
 public void unlink( AgeAbstractClassImprint parent, AgeAbstractClassImprint child )
 {
  Tree data = getData();
  
  Collection<ImprintTreeNode> ndColl = nodeMap.get(child);
  
  Iterator<ImprintTreeNode> iter = ndColl.iterator();
  
  ListGridRecord selNode = getSelectedRecord();
  
  ListGridRecord toSelect = null;
  boolean needReselect=false;
  
  while( iter.hasNext() )
  {
   ImprintTreeNode node = iter.next();
   
   if( ((ImprintTreeNode)data.getParent(node)).getClassImprint() == parent )
   {
    if( node == selNode )
    {
     needReselect=true;
     selectRecord(selNode, false);
    }
    
    data.remove(node);
    iter.remove();
   }
   else if( toSelect == null ) 
    toSelect = node;
  }
  
  if( needReselect && toSelect != null )
   selectRecord(toSelect);
 }
}
