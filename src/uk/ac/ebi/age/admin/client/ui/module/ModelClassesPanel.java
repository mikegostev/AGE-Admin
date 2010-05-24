package uk.ac.ebi.age.admin.client.ui.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import uk.ac.ebi.age.admin.client.model.AgeClassImprint;
import uk.ac.ebi.age.admin.client.model.ModelImprint;

import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.util.ValueCallback;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
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
  treePanel.setShowHeader(false);

//  treePanel.setNodeIcon("../images/icons/bullet.png");
//  treePanel.setFolderIcon("../images/icons/bullet.png");

  treePanel.setShowConnectors(true);
  treePanel.setShowRoot(false);

  treePanel.setFields(new TreeGridField("Class"));

  Tree data = new Tree();
  data.setModelType(TreeModelType.CHILDREN);

  ClassTreeNode classRoot =  new ClassTreeNode("AgeClass");
  classRoot.setIcon("../images/icons/class/abstract.png");
  
  TreeNode rootNode = new TreeNode( "Root" );
  rootNode.setChildren( new TreeNode[] {classRoot } );
  
  data.setRoot(rootNode);

  treePanel.setData(data);

  detailPanel = new VLayout();
  detailPanel.setHeight100();
  detailPanel.setWidth("70%");

  ToolStrip clsTools = new ToolStrip();
  clsTools.setWidth100();
  
  ToolStripButton chldBut = new ToolStripButton();
  chldBut.setTitle("Add child");
  chldBut.setIcon("../images/icons/class/addchild.png");
  chldBut.addClickHandler( new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    addChildClass(null);
   }
  });
  
  clsTools.addSpacer(20);
  clsTools.addButton(chldBut);
  
  ToolStripButton sibBut = new ToolStripButton();
  sibBut.setTitle("Add sibling");
  sibBut.setIcon("../images/icons/class/addsibling.png");
  sibBut.addClickHandler( new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    addSiblingClass();
   }
  });
  
  clsTools.addButton(sibBut);
 
  ToolStripButton delBut = new ToolStripButton();
  delBut.setTitle("Delete class");
  delBut.setIcon("../images/icons/class/del.png");
  
  clsTools.addButton(delBut);

  
  VLayout toolTree = new VLayout();
  toolTree.setShowResizeBar(true);
  toolTree.setWidth("30%");

  toolTree.addMember(clsTools);
  toolTree.addMember(treePanel);
  
  setMembers(toolTree, detailPanel);

 
  treePanel.addCellClickHandler( new CellClickHandler()
  {
   
   @Override
   public void onCellClick(CellClickEvent event)
   {
    showClassDetails( ((ClassTreeNode)event.getRecord()).getCls() );
   }


  });
 }

 private void addSiblingClass( )
 {
  final ClassTreeNode selNode = (ClassTreeNode)treePanel.getSelectedRecord();
  
  if( selNode == null )
   return;
  
  ClassTreeNode pNode = ((ClassTreeNode)treePanel.getData().getParent(selNode));
  
  if( pNode == null )
   return;
  
  addChildClass( pNode );
 }

 
 private void addChildClass( ClassTreeNode node )
 {
  
  final ClassTreeNode selNode = node==null?(ClassTreeNode) treePanel.getSelectedRecord():node;
  
  if( selNode == null )
   return;
  
  SC.askforValue("Please inter new class name", new ValueCallback()
  {
   @Override
   public void execute(String value)
   {
    value = value.trim();
    
    if( value.length() == 0 )
     return;
    
    AgeClassImprint cImp = selNode.getCls();
    
    for( AgeClassImprint othCls : cImp.getModel().getClasses() )
    {
     if( othCls.getName().equals(value) )
     {
      SC.warn("Name '"+value+"' is used by another class");
      return;
     }
    }
    
    AgeClassImprint subCls = cImp.createSubClass();
    subCls.setName(value);
    
    treePanel.getData().add(new ClassTreeNode(subCls), selNode);
    
    treePanel.getData().openFolder(selNode);
//    treePanel.expandRecord(selNode);
//    treePanel.getData().setRoot(treePanel.getData().getRoot());
//    treePanel.setData(treePanel.getData());
//    
//    treePanel.redraw();
//    selNode.setChildren( new TreeNode[] { new ClassTreeNode(value) } );
    
   }
  });
  
//  System.out.println("Node selected: " + (selNode!=null?selNode.getName():"None"));
//  System.out.println("Parent node: " + ((ClassTreeNode)treePanel.getData().getParent(selNode)).getCls().getName());
 }
 
 private void showClassDetails(AgeClassImprint cls)
 {
  detailPanel.setMembers( new ClassDetailsPanel(cls) );
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
   setIcon("../images/icons/class/"+(cl.isAbstract()?"abstract.png":"regular.png"));
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
