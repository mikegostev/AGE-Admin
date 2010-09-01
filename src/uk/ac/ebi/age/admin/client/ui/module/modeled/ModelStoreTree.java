package uk.ac.ebi.age.admin.client.ui.module.modeled;

import java.util.LinkedList;

import uk.ac.ebi.age.admin.client.common.ModelPath;
import uk.ac.ebi.age.admin.client.common.StoreNode;
import uk.ac.ebi.age.admin.client.model.ModelStorage;

import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;

public class ModelStoreTree extends VLayout
{
 private TreeGrid treeGrid;
 private ModelMngr mngr;
 
 private StoreNode pubDir;
 private StoreNode privDir;
 
 public ModelStoreTree(ModelMngr m)
 {
//  setShowShadow(true);
  mngr = m;
  
  ToolStrip toolStrip = new ToolStrip();  
  toolStrip.setWidth100();  
  toolStrip.setHeight(24);  
     
  ImgButton newButton = new ImgButton();  
  newButton.setSize(16);  
  newButton.setShowRollOver(false);  
  newButton.setSrc("../images/icons/class/regular.png");
  newButton.setTooltip("Create new model");
  toolStrip.addMember(newButton);  
  newButton.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    mngr.setNewModel();
   }
  });
  
  
  ImgButton editButton = new ImgButton();  
  editButton.setSize(16);  
  editButton.setShowRollOver(false);  
  editButton.setSrc("../images/icons/class/regular.png");  
  editButton.setTooltip("Load and edit model");
  toolStrip.addMember(editButton);  
  editButton.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    StoreNode nd = getSelectedNode();
    
    if( nd == null || nd.isDirectory() )
    {
     SC.say("Please select a model in the tree");
     return;
    }
    
    mngr.loadModel( getModelPath() );
   }
  });
  
  ImgButton installButton = new ImgButton();  
  installButton.setSize(16);  
  installButton.setShowRollOver(false);  
  installButton.setSrc("../images/icons/class/regular.png");  
  installButton.setTooltip("Install selected model");
  toolStrip.addMember(installButton);  
  installButton.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    StoreNode nd = getSelectedNode();
    
    if( nd == null || nd.isDirectory() )
    {
     SC.say("Please select a model in the tree");
     return;
    }
    
    mngr.installModel( getModelPath() );
   }
  });

  
  treeGrid = new TreeGrid();
  
  treeGrid.setShowHeader(false);
  treeGrid.setShowConnectors(true);
  treeGrid.setShowRoot(false);
  treeGrid.setShowAllRecords(true);
  treeGrid.setTitleField("Name");
  treeGrid.setFields( new TreeGridField("Name") );
  
  Tree data = new Tree();
  data.setModelType(TreeModelType.CHILDREN);
  data.setNameProperty("Name");
  data.setChildrenProperty("children");
  treeGrid.setData(data);

  TreeNode rootNode = new TreeNode( "Root" );
  data.setRoot(rootNode);

  treeGrid.setWidth100();
  treeGrid.setHeight100();
  
//  HLayout butPnl = new HLayout();
//  butPnl.setMembersMargin(20);
//  butPnl.setWidth100();
//  
//  IButton newBt = new IButton("New");
//  newBt.setAutoFit(true);
//
//  IButton editBt = new IButton("Edit");
//  editBt.setAutoFit(true);
//  
//  IButton installBt = new IButton("Install");
//  installBt.setAutoFit(true);
// 
//  butPnl.setMembers(newBt, editBt, installBt);
  
  setMembers(toolStrip,treeGrid);
 }

 public void setStorage(ModelStorage modst)
 {
  TreeNode nRoot = new TreeNode();
 
  pubDir = modst.getPublicDirectory();
  
  if( pubDir == null )
   pubDir = new StoreNode();
  
  pubDir.setName("Common");

  privDir = modst.getUserDirectory();

  if( privDir == null )
   privDir = new StoreNode();
  
  privDir.setName("Private");
  

  FileTreeNode common = new FileTreeNode(pubDir);
  FileTreeNode priv = new FileTreeNode(privDir);
  
//  treeGrid.getData().addList(new TreeNode[]{ nRoot }, treeGrid.getData().getRoot());
  
  treeGrid.getData().setRoot(nRoot);
  
  treeGrid.getData().addList(new TreeNode[]{ common, priv }, nRoot);

  createTree(common, modst.getPublicDirectory());
  createTree(priv, modst.getUserDirectory());
  
  treeGrid.getData().openAll();
 }
 
 private void createTree(FileTreeNode nd, StoreNode dir)
 {
  if( dir == null )
   return;
  
  int subSize = 0;
  
  if( dir.getSubnodes() != null )
   subSize+= dir.getSubnodes().size();

  if( dir.getFiles() != null )
   subSize+= dir.getFiles().size();
  
  if( subSize == 0 )
   return;
  
  FileTreeNode[] subNodes = new FileTreeNode[ subSize ];
  int cnod = 0;
  
  if( dir.getSubnodes() != null )
  {
   for(StoreNode d : dir.getSubnodes())
   {
    subNodes[cnod] = new FileTreeNode(d); 
    createTree(subNodes[cnod], d);
    cnod++;
   }
  }
  
  if( dir.getFiles() != null )
  {
   for(StoreNode f : dir.getFiles())
   {
    subNodes[cnod] = new FileTreeNode(f); 
    cnod++;
   }
  }
  
  treeGrid.getData().addList(subNodes, nd);

 }

 public StoreNode getSelectedNode()
 {
  FileTreeNode nd = (FileTreeNode) treeGrid.getSelectedRecord();

  if(nd == null)
   return null;
  
  return nd.getStoreNode();
 }
 
 public ModelPath getModelPath()
 {
  FileTreeNode nd = (FileTreeNode) treeGrid.getSelectedRecord();

  if(nd == null)
   return null;

  ModelPath pth = new ModelPath();

  StoreNode dir = nd.getStoreNode();

  if( ! dir.isDirectory() )
  {
   pth.setModelName(dir.getName());
   dir=dir.getParent();
  }
  
  LinkedList<String> path = new LinkedList<String>();

  while(dir != pubDir && dir != privDir)
  {
   path.add(0, dir.getName());

   dir = dir.getParent();
  }

  pth.setPublic(dir == pubDir);

  if(path.size() > 0)
   pth.setPathElements(path);

  return pth;
 }

 public void addModel(ModelPath path)
 {
  TreeNode[] rnds = treeGrid.getData().getChildren(treeGrid.getData().getRoot());
  
  FileTreeNode cNode=null;
  
  for( TreeNode nd : rnds )
  {
   if( path.isPublic() && ((FileTreeNode)nd).getStoreNode() == pubDir )
   {
    cNode = (FileTreeNode)nd;
    break;
   }
   else if( !path.isPublic() && ((FileTreeNode)nd).getStoreNode() == privDir )
   {
    cNode = (FileTreeNode)nd;
    break;
   }
  }
  
  if( cNode == null )
  {
   SC.warn("Can't found Public/Private node in the tree");
   return;
  }
  
  pEls: for( String pEl : path.getPathElements() )
  {
   for( TreeNode snd : treeGrid.getData().getChildren( cNode ) )
   {
    if( ((FileTreeNode)snd).getStoreNode().getName().equals(pEl) )
    {
     cNode = (FileTreeNode)snd;
     continue pEls;
    }
   }
   
   SC.warn("Can't found '"+pEl+"' node in the tree");
   return;
  }
  
  for( TreeNode snd : treeGrid.getData().getChildren( cNode ) )
  {
   if( ((FileTreeNode)snd).getStoreNode().getName().equals(path.getModelName()) )
   return; 
  }
  
  FileTreeNode ftn = new FileTreeNode(cNode.getStoreNode().addFile(path.getModelName()));
  treeGrid.getData().addList(new TreeNode[]{ftn}, cNode);
 }
 
 
 private static class FileTreeNode extends TreeNode
 {
  public FileTreeNode( StoreNode dir )
  {
   setAttribute("Name", dir.getName() );
   setAttribute("Dir", dir );
   setTitle(dir.getName());
   setIsFolder(dir.isDirectory());
  }

  
  public StoreNode getStoreNode()
  {
   return (StoreNode) getAttributeAsObject("Dir");
  }
 }

}
