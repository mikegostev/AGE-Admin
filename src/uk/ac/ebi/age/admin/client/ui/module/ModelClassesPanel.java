package uk.ac.ebi.age.admin.client.ui.module;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import uk.ac.ebi.age.admin.client.model.AgeClassImprint;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.ui.ClassAuxData;
import uk.ac.ebi.age.admin.client.ui.ClassTreeNode;

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
import com.smartgwt.client.widgets.tree.TreeNode;

public class ModelClassesPanel extends HLayout
{
 private TreeGrid treePanel;
 private VLayout  detailPanel;
 private ModelImprint model;
 
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
  treePanel.setTitleField("Name");

//  treePanel.setFields(new TreeGridField("Name"));

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
    subCls.setAuxData(new ClassAuxData());
    
    ClassTreeNode nNd = new ClassTreeNode(subCls);
    ((ClassAuxData)subCls.getAuxData()).addNode(nNd);
    
    treePanel.getData().add(nNd, selNode);
    
    treePanel.getData().openFolder(selNode);
   }
  });
  
 }
 
 private void showClassDetails(AgeClassImprint cls)
 {
  detailPanel.setMembers( new ClassDetailsPanel(cls, this) );
 }
 
 public void setModel(ModelImprint mod)
 {
  model=mod;

  Tree data = treePanel.getData();
  
  TreeNode rootNode =data.getRoot();
  
//  ClassTreeNode rootNode = new ClassTreeNode(mod.getRootClass());

  Map<AgeClassImprint, Collection<ClassTreeNode>> nodeMap = new HashMap<AgeClassImprint, Collection<ClassTreeNode>>();

  ClassTreeNode  clsRoot = new ClassTreeNode(mod.getRootClass());
  
  createTreeStructure(mod.getRootClass(), clsRoot, nodeMap);

  rootNode.setChildren( new TreeNode[] { clsRoot } );
  
//  data.reloadChildren(rootNode);
  
  data.setRoot(rootNode);
  
  data.openAll();
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

 
 private void createTreeStructure(AgeClassImprint cls, ClassTreeNode node, Map<AgeClassImprint, Collection<ClassTreeNode>> nodeMap)
 {
  ((ClassAuxData)cls.getAuxData()).addNode(node);
  
  if(cls.getChildren() == null)
   return;

  TreeNode[] children = new TreeNode[cls.getChildren().size()];


  int i = 0;
  for(AgeClassImprint subcls : cls.getChildren())
  {
   ClassTreeNode ctn = new ClassTreeNode(subcls);
   children[i++] = ctn;

   createTreeStructure(subcls, ctn, nodeMap);
  }

  node.setChildren(children);

 }

 
// private void createTreeStructure2(AgeClassImprint cls, TreeNode node,
//   Map<AgeClassImprint, Collection<ClassTreeNode>> nodeMap)
// {
//  if(cls.getChildren() == null)
//   return;
//
//  TreeNode[] children = new TreeNode[cls.getChildren().size()];
//
//  int i = 0;
//  for(AgeClassImprint subcls : cls.getChildren())
//  {
//   ClassTreeNode ctn = new ClassTreeNode(subcls);
//   ctn.setName(subcls.getName());
//
//   Collection<ClassTreeNode> coll = nodeMap.get(subcls);
//
//   if(coll == null)
//   {
//    coll = new ArrayList<ClassTreeNode>(5);
//    nodeMap.put(subcls, coll);
//   }
//
//   coll.add(ctn);
//
//   ctn.setLinkedNodes(coll);
//
//   children[i++] = ctn;
//
//   createTreeStructure(subcls, ctn, nodeMap);
//  }
//
//  node.setChildren(children);
//
// }

 void updateClassName(AgeClassImprint classImprint, String newName )
 {
  classImprint.setName(newName);
  
  ClassAuxData aux = (ClassAuxData)classImprint.getAuxData();
  
  for(ClassTreeNode tn : aux.getNodes() )
  {
   tn.setTitle(newName);
  }
  
  treePanel.getData().setRoot(treePanel.getData().getRoot());
 }

 void updateClassType(AgeClassImprint classImprint, boolean abstr )
 {
  classImprint.setAbstract(abstr);

  
  ClassAuxData aux = (ClassAuxData)classImprint.getAuxData();
  
  for(ClassTreeNode tn : aux.getNodes() )
  {
   tn.setAbstract(abstr);
  }
  
  treePanel.getData().setRoot(treePanel.getData().getRoot());
 }

 public void addSubclass()
 {
  new ClassSelectDialog(model).show();
 }

}
