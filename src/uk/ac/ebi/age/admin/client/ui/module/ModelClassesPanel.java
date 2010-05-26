package uk.ac.ebi.age.admin.client.ui.module;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeClassImprint;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.ui.ClassSelectedCallback;
import uk.ac.ebi.age.admin.client.ui.ClassTreeNode;
import uk.ac.ebi.age.admin.client.ui.ImprintTreeNode;
import uk.ac.ebi.age.admin.client.ui.NodeCreator;
import uk.ac.ebi.age.admin.client.ui.module.ClassTreePanel.Direction;

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

public class ModelClassesPanel extends HLayout
{
 private static NodeCreator classNodeCreator = new NodeCreator() 
 {
  @Override
  public ImprintTreeNode create(AgeAbstractClassImprint cls)
  {
   return new ClassTreeNode((AgeClassImprint)cls);
  }
 };
 
// private TreeGrid treePanel;
 private VLayout  detailPanel;
 private ModelImprint model;
 private ClassTreePanel treePanel;
 private ClassTreePanel childrenTreePanel;
 private ClassTreePanel parentsTreePanel;
 
 public ModelClassesPanel()
 {
//  treePanel = new TreeGrid();
//
//  treePanel.setWidth100();
//  treePanel.setHeight100();
//  treePanel.setShowHeader(false);
//
////  treePanel.setNodeIcon("../images/icons/bullet.png");
////  treePanel.setFolderIcon("../images/icons/bullet.png");
//
//  treePanel.setShowConnectors(true);
//  treePanel.setShowRoot(false);
//  treePanel.setTitleField("Name");
//
////  treePanel.setFields(new TreeGridField("Name"));
//
//  Tree data = new Tree();
//  data.setModelType(TreeModelType.CHILDREN);
//
//  ClassTreeNode classRoot =  new ClassTreeNode("AgeClass");
//  classRoot.setIcon("../images/icons/class/abstract.png");
//  
//  TreeNode rootNode = new TreeNode( "Root" );
//  rootNode.setChildren( new TreeNode[] {classRoot } );
//  
//  data.setRoot(rootNode);
//
//  treePanel.setData(data);
//
//  treePanel.addSelectionChangedHandler(new SelectionChangedHandler()
//  {
//   
//   @Override
//   public void onSelectionChanged(SelectionEvent event)
//   {
//    ClassTreeNode ctn = (ClassTreeNode)event.getRecord();
//    
//    Collection<ClassTreeNode> sameNodes = ((ClassAuxData)ctn.getCls().getAuxData()).getNodes();
//    
//    if(sameNodes.size()==1)
//     return;
//    
//    TreeNode[] auxNodes = new TreeNode[sameNodes.size()-1]; 
//    
//    int i=0;
//    for( ClassTreeNode smNd  : sameNodes )
//     if( smNd != ctn )
//      auxNodes[i++]=smNd;
//    
//    treePanel.selectRecords(auxNodes);
//    
//    System.out.println("Node selected : "+ctn.getCls().getName());
//   }
//  });
  
  
  treePanel = new ClassTreePanel(null, classNodeCreator ); 
  treePanel.setHeight("*");
  
  
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
  
  HLayout auxTrees = new HLayout();
  auxTrees.setHeight("25%");
  
  childrenTreePanel = new ClassTreePanel(null, classNodeCreator);
  childrenTreePanel.setWidth("50%");
  childrenTreePanel.setHeight100();

  parentsTreePanel = new ClassTreePanel(null, Direction.CHILD2PARENT, classNodeCreator);
  parentsTreePanel.setWidth("50%");
  parentsTreePanel.setHeight100();
  
  auxTrees.setMembers(childrenTreePanel, parentsTreePanel );
  
  toolTree.addMember(auxTrees);
  
  setMembers(toolTree, detailPanel);

 
  treePanel.addCellClickHandler( new CellClickHandler()
  {
   
   @Override
   public void onCellClick(CellClickEvent event)
   {
    showClassDetails( ((ClassTreeNode)event.getRecord()).getClassImprint() );
    childrenTreePanel.setRoot(((ClassTreeNode)event.getRecord()).getClassImprint());
    parentsTreePanel.setRoot(((ClassTreeNode)event.getRecord()).getClassImprint());
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
    
    AgeClassImprint cImp = selNode.getClassImprint();
    
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

    treePanel.addBranch(cImp, subCls);
    
//    AgeClassImprint subCls = cImp.createSubClass();
//    subCls.setName(value);
//    subCls.setAuxData(new ClassAuxData());
//    
//    ClassTreeNode nNd = new ClassTreeNode(subCls);
//    ((ClassAuxData)subCls.getAuxData()).addNode(nNd);
//    
//    treePanel.getData().add(nNd, selNode);
//    
//    treePanel.getData().openFolder(selNode);
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

  treePanel.setRoot(mod.getRootClass());
  
//  Tree data = treePanel.getData();
//  
//  TreeNode rootNode =data.getRoot();
//  
////  ClassTreeNode rootNode = new ClassTreeNode(mod.getRootClass());
//
//
//  ClassTreeNode  clsRoot = new ClassTreeNode(mod.getRootClass());
//  
//  createTreeStructure( mod.getRootClass(), clsRoot );
//
//  rootNode.setChildren( new TreeNode[] { clsRoot } );
//  
////  data.reloadChildren(rootNode);
//  
//  data.setRoot(rootNode);
//  
//  data.openAll();
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

 
// private void createTreeStructure(AgeClassImprint cls, ClassTreeNode node )
// {
//  ((ClassAuxData)cls.getAuxData()).addNode(node);
//  
//  if(cls.getChildren() == null)
//   return;
//
//  TreeNode[] children = new TreeNode[cls.getChildren().size()];
//
//
//  int i = 0;
//  for(AgeClassImprint subcls : cls.getChildren())
//  {
//   ClassTreeNode ctn = new ClassTreeNode(subcls);
//   children[i++] = ctn;
//
//   createTreeStructure(subcls, ctn);
//  }
//
//  node.setChildren(children);
//
// }



 void updateClassName(AgeClassImprint classImprint, String newName )
 {
  treePanel.updateClassName( classImprint, newName  );
 }

 void updateClassType(AgeClassImprint classImprint, boolean abstr )
 {
  treePanel.updateClassType( classImprint, abstr  );
 }

 public void addSubclass( final AgeClassImprint superClass )
 {
  new ClassSelectDialog<AgeClassImprint>(model.getRootClass(), classNodeCreator, new ClassSelectedCallback()
  {
   @Override
   public void classSelected(AgeClassImprint cls)
   {
    cls.addSuperClass(superClass);
    treePanel.addBranch(superClass, cls);
   }
  }).show();
 }

 public void addSuperclass( final AgeClassImprint subClass )
 {
  new ClassSelectDialog<AgeClassImprint>(model.getRootClass(), classNodeCreator, new ClassSelectedCallback()
  {
   @Override
   public void classSelected(AgeClassImprint cls)
   {
    subClass.addSuperClass(cls);
    treePanel.addBranch( cls, subClass);
   }
  }).show();
 }
 
}
