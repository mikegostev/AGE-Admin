package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.ui.ClassSelectedCallback;
import uk.ac.ebi.age.admin.client.ui.ImprintTreeNode;
import uk.ac.ebi.age.admin.client.ui.MetaClassDef;
import uk.ac.ebi.age.admin.client.ui.module.modeled.XTreePanel.Direction;

import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.util.ValueCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class XEditorPanel extends HLayout
{
 private VLayout  detailPanel;
 private ModelImprint model;
 private XTreePanel treePanel;
 private XTreePanel childrenTreePanel;
 private XTreePanel parentsTreePanel;
 
 private MetaClassDef metaDef;
 
 public XEditorPanel(  MetaClassDef md )
 {
  metaDef = md;
  
  treePanel = new XTreePanel( null, metaDef ); 
  treePanel.setHeight("*");
  
  
  detailPanel = new VLayout();
  detailPanel.setHeight100();
  detailPanel.setWidth("70%");

  ToolStrip clsTools = new ToolStrip();
  clsTools.setWidth100();
  
  ToolStripButton chldBut = new ToolStripButton();
  chldBut.setTitle("Add child");
  chldBut.setIcon("../images/icons/"+metaDef.getMetaClassName()+"/addchild.png");
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
  sibBut.setIcon("../images/icons/"+metaDef.getMetaClassName()+"/addsibling.png");
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
  delBut.setTitle("Delete");
  delBut.setIcon("../images/icons/"+metaDef.getMetaClassName()+"/del.png");
  delBut.addClickHandler( new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    deleteClass();
   }
  });
  
  clsTools.addButton(delBut);

  
  VLayout toolTree = new VLayout();
  toolTree.setShowResizeBar(true);
  toolTree.setWidth("30%");

  toolTree.addMember(clsTools);
  toolTree.addMember(treePanel);
  
  HLayout auxTrees = new HLayout();
  auxTrees.setHeight("25%");
  
  childrenTreePanel = new XTreePanel( null, metaDef );
  childrenTreePanel.setWidth("50%");
  childrenTreePanel.setHeight100();

  parentsTreePanel = new XTreePanel(null, Direction.CHILD2PARENT, metaDef);
  parentsTreePanel.setWidth("50%");
  parentsTreePanel.setHeight100();
  
  auxTrees.setMembers(childrenTreePanel, parentsTreePanel );
  
  toolTree.addMember(auxTrees);
  
  setMembers(toolTree, detailPanel);

 
  treePanel.addSelectionChangedHandler( new SelectionChangedHandler()
  {
   
   @Override
   public void onSelectionChanged(SelectionEvent event)
   {
    if( ! event.getState() )
     return;
    
    showClassDetails( ((ImprintTreeNode)event.getRecord()).getClassImprint() );
    childrenTreePanel.setRoot(((ImprintTreeNode)event.getRecord()).getClassImprint());
    parentsTreePanel.setRoot(((ImprintTreeNode)event.getRecord()).getClassImprint());
   }
  });

 }

 private void addSiblingClass( )
 {
  final ImprintTreeNode selNode = (ImprintTreeNode)treePanel.getSelectedRecord();
  
  if( selNode == null )
   return;
  
  ImprintTreeNode pNode = ((ImprintTreeNode)treePanel.getData().getParent(selNode));
  
  if( pNode == null )
   return;
  
  addChildClass( pNode );
 }

 private void deleteClass()
 {
  final ImprintTreeNode selNode = (ImprintTreeNode)treePanel.getSelectedRecord();
  
  if( selNode == null )
   return;

  final AgeAbstractClassImprint cimp = selNode.getClassImprint();

  SC.confirm("Are you sure to delete class '"+cimp.getName()+"'", new BooleanCallback()
  {
   
   @Override
   public void execute(Boolean value)
   {
    if( ! value )
     return;

    cimp.delete();
    
    treePanel.removeClass(cimp);
    updateSubbranchView(null);
    
    showClassDetails(null);
   }
  });
  
  
 }
 
 private void addChildClass( ImprintTreeNode node )
 {
  
  final ImprintTreeNode selNode = node==null?(ImprintTreeNode) treePanel.getSelectedRecord():node;
  
  if( selNode == null )
   return;
  
  SC.askforValue("Please enter new class name", new ValueCallback()
  {
   @Override
   public void execute(String value)
   {
    value = value.trim();
    
    if( value.length() == 0 )
     return;
    
    AgeAbstractClassImprint cImp = selNode.getClassImprint();
    
    for( AgeAbstractClassImprint othCls : metaDef.getXClasses(cImp.getModel()) )
    {
     if( othCls.getName().equals(value) )
     {
      SC.warn("Name '"+value+"' is used by another class");
      return;
     }
    }

    AgeAbstractClassImprint subCls = cImp.createSubClass();
    subCls.setName(value);

    treePanel.addBranch(cImp, subCls);
    
    updateSubbranchView(cImp);
    
   }
  });
  
 }
 
 private void showClassDetails(AgeAbstractClassImprint cls)
 {
  if( cls == null )
   detailPanel.setMembers( new Canvas[0] );
  else  
   detailPanel.setMembers( new XDetailsPanel(cls, this) );
 }
 
 public void setModel(ModelImprint mod)
 {
  model=mod;

  treePanel.setRoot(metaDef.getRoot(mod));
  
 }

 void updateClassName(AgeAbstractClassImprint classImprint, String newName )
 {
  treePanel.updateClassName( classImprint, newName  );
  childrenTreePanel.updateClassName( classImprint, newName );
  parentsTreePanel.updateClassName( classImprint, newName );
 }

 void updateClassType(AgeAbstractClassImprint classImprint )
 {
  treePanel.updateClassType( classImprint  );
  childrenTreePanel.updateClassType( classImprint  );
  parentsTreePanel.updateClassType( classImprint );
 }

 private void updateSubbranchView( AgeAbstractClassImprint cls )
 {
  if( cls == null )
  {
   ImprintTreeNode node = (ImprintTreeNode)treePanel.getSelectedRecord();
   
   if( node != null )
    cls = node.getClassImprint();
  }
  
  childrenTreePanel.setRoot(cls);
  parentsTreePanel.setRoot(cls);
 }

 private boolean checkCycle( AgeAbstractClassImprint superClass, AgeAbstractClassImprint subClass )
 {
  if( superClass == subClass )
   return false;
  
  if( subClass.getChildren() == null )
   return true;
  
  for( AgeAbstractClassImprint scls : subClass.getChildren() )
  {
   if( ! checkCycle(superClass,scls) )
    return false;
  }
  
  return true;
 }
 
 public void addSubclass( final AgeAbstractClassImprint superClass, final ClassSelectedCallback extCB )
 {
  new XSelectDialog<AgeAbstractClassImprint>(model.getRootClass(), metaDef, new ClassSelectedCallback()
  {
   @Override
   public void classSelected(AgeAbstractClassImprint cls)
   {
    if( ! checkCycle(superClass, cls) )
    {
     SC.warn("A hierarchy must not have cycles");
     return;
    }
    
    cls.addSuperClass(superClass);
    treePanel.addBranch(superClass, cls);
    
    updateSubbranchView(null);

    if( extCB != null )
     extCB.classSelected(cls);
   }
  }).show();
 }

 public void addSuperclass( final AgeAbstractClassImprint subClass, final ClassSelectedCallback extCB  )
 {
  new XSelectDialog<AgeAbstractClassImprint>(model.getRootClass(), metaDef, new ClassSelectedCallback()
  {
   @Override
   public void classSelected(AgeAbstractClassImprint cls)
   {
    if( ! checkCycle( cls, subClass) )
    {
     SC.warn("A hierarchy must not have cycles");
     return;
    }

    subClass.addSuperClass(cls);
    treePanel.addBranch( cls, subClass);
    
    updateSubbranchView(null);
    
    if( extCB != null )
     extCB.classSelected(cls);
   }
  }).show();
 }

 public void unlink(final AgeAbstractClassImprint superClassImprint, final AgeAbstractClassImprint classImprint)
 {
  classImprint.removeParent(superClassImprint);
  superClassImprint.removeChild(classImprint);
  
  treePanel.unlink(superClassImprint, classImprint);
  
  updateSubbranchView(null);
 }

 public MetaClassDef getMetaClassDef()
 {
  return metaDef;
 }

 
}
