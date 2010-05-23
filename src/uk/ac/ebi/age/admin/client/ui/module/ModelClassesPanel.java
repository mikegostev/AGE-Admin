package uk.ac.ebi.age.admin.client.ui.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import uk.ac.ebi.age.admin.client.model.AgeClassImprint;
import uk.ac.ebi.age.admin.client.model.ModelImprint;

import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;

public class ModelClassesPanel extends HLayout
{
 private TreeGrid treePanel;
 private VLayout  detailPanel;

 public ModelClassesPanel()
 {
  treePanel = new TreeGrid();

  treePanel.setWidth100();
  treePanel.setHeight100();

  treePanel.setNodeIcon("../images/icons/bullet.png");
  treePanel.setFolderIcon("../images/icons/bullet.png");

  treePanel.setShowConnectors(true);
  treePanel.setShowRoot(false);


  treePanel.setFields(new TreeGridField("Class"));

  Tree data = new Tree();
  data.setModelType(TreeModelType.CHILDREN);

  ClassTreeNode classRoot =  new ClassTreeNode("AgeClass");
  classRoot.setIcon("../images/icons/eye.png");
  
  TreeNode rootNode = new TreeNode( "Root" );
  rootNode.setChildren( new TreeNode[] {classRoot } );
  
  data.setRoot(rootNode);

  treePanel.setData(data);

  detailPanel = new VLayout();
  detailPanel.setHeight100();
  detailPanel.setWidth("70%");

  ToolStrip clsTools = new ToolStrip();
  clsTools.setWidth100();
  
  VLayout toolTree = new VLayout();
  toolTree.setShowResizeBar(true);
  toolTree.setWidth("30%");

  toolTree.addMember(clsTools);
  toolTree.addMember(treePanel);
  
  setMembers(toolTree, detailPanel);

  addDrawHandler(new DrawHandler()
  {

   @Override
   public void onDraw(DrawEvent event)
   {
    System.out.println("Classes panel draw");
   }
  });
 }

 public void setModel(ModelImprint mod)
 {
//  Tree data = new Tree();
//  data.setModelType(TreeModelType.CHILDREN);

  Tree data = treePanel.getData();
  
  TreeNode rootNode =data.getRoot();
  
//  ClassTreeNode rootNode = new ClassTreeNode(mod.getRootClass());

  Map<AgeClassImprint, Collection<ClassTreeNode>> nodeMap = new HashMap<AgeClassImprint, Collection<ClassTreeNode>>();

  ClassTreeNode  clsRoot = new ClassTreeNode(mod.getRootClass());
  
  createTreeStructure(mod.getRootClass(), clsRoot, nodeMap);

  rootNode.setChildren( new TreeNode[] { clsRoot } );
  
//  data.reloadChildren(rootNode);
  
  data.setRoot(rootNode);
  
//  treePanel.setData(data);
//  TreeGridField field = new TreeGridField("Class");
//
//  field.setCellFormatter(new CellFormatter()
//  {
//
//   public String format(Object value, ListGridRecord record, int rowNum, int colNum)
//   {
//    return record.getAttribute("name");
//   }
//  });
//
//  treePanel.setFields(field);

 }

 
 private void createTreeStructure(AgeClassImprint cls, TreeNode node, Map<AgeClassImprint, Collection<ClassTreeNode>> nodeMap)
 {
  if(cls.getChildren() == null)
   return;

  TreeNode[] children = new TreeNode[cls.getChildren().size()];


  int i = 0;
  for(AgeClassImprint subcls : cls.getChildren())
  {
   ClassTreeNode ctn = new ClassTreeNode(subcls);
   ctn.setName(subcls.getName());

   Collection<ClassTreeNode> coll = nodeMap.get(subcls);

   if(coll == null)
   {
    coll = new ArrayList<ClassTreeNode>(5);
    nodeMap.put(subcls, coll);
   }

   coll.add(ctn);

   ctn.setLinkedNodes(coll);

   children[i++] = ctn;

   createTreeStructure(subcls, ctn, nodeMap);
  }

  node.setChildren(children);

 }

 
 private void createTreeStructure2(AgeClassImprint cls, TreeNode node,
   Map<AgeClassImprint, Collection<ClassTreeNode>> nodeMap)
 {
  if(cls.getChildren() == null)
   return;

  TreeNode[] children = new TreeNode[cls.getChildren().size()];

  int i = 0;
  for(AgeClassImprint subcls : cls.getChildren())
  {
   ClassTreeNode ctn = new ClassTreeNode(subcls);
   ctn.setName(subcls.getName());

   Collection<ClassTreeNode> coll = nodeMap.get(subcls);

   if(coll == null)
   {
    coll = new ArrayList<ClassTreeNode>(5);
    nodeMap.put(subcls, coll);
   }

   coll.add(ctn);

   ctn.setLinkedNodes(coll);

   children[i++] = ctn;

   createTreeStructure(subcls, ctn, nodeMap);
  }

  node.setChildren(children);

 }

 private static class ClassTreeNode extends TreeNode
 {
  private AgeClassImprint           cls;
  private Collection<ClassTreeNode> linkedNodes;

  public ClassTreeNode( AgeClassImprint cl )
  {
   cls=cl;
   
   setAttribute("Class", cl.getName());
  }
  
  public ClassTreeNode( String nm )
  {
   setAttribute("Class", nm);
  }
  
  public void setLinkedNodes(Collection<ClassTreeNode> linkedNodes)
  {
   this.linkedNodes = linkedNodes;
  }

  public AgeClassImprint getCls()
  {
   return cls;
  }

  public void setCls(AgeClassImprint cls)
  {
   this.cls = cls;
  }

  public Collection<ClassTreeNode> getLinkedNodes()
  {
   return linkedNodes;
  }

  public void addLinkedNode(ClassTreeNode nd)
  {
   if(linkedNodes == null)
    linkedNodes = new ArrayList<ClassTreeNode>(5);

   linkedNodes.add(nd);
  }

 }
}
