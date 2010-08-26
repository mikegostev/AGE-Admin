package uk.ac.ebi.age.admin.client.ui.module.modeled;

import java.util.LinkedList;

import uk.ac.ebi.age.admin.client.common.Directory;
import uk.ac.ebi.age.admin.client.common.ModelPath;
import uk.ac.ebi.age.admin.client.model.ModelStorage;

import com.smartgwt.client.types.TreeModelType;
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
 
 private Directory pubDir;
 private Directory privDir;
 
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
  toolStrip.addMember(editButton);  
  
  ImgButton installButton = new ImgButton();  
  installButton.setSize(16);  
  installButton.setShowRollOver(false);  
  installButton.setSrc("../images/icons/class/regular.png");  
  toolStrip.addMember(installButton);  

  
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
  pubDir.setName("Common");

  privDir = modst.getUserDirectory();
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
 
 private void createTree(FileTreeNode nd, Directory dir)
 {
  if( dir == null )
   return;
  
  int subSize = 0;
  
  if( dir.getSubdirectories() != null )
   subSize+= dir.getSubdirectories().size();

  if( dir.getFiles() != null )
   subSize+= dir.getFiles().size();
  
  if( subSize == 0 )
   return;
  
  FileTreeNode[] subNodes = new FileTreeNode[ subSize ];
  int cnod = 0;
  
  if( dir.getSubdirectories() != null )
  {
   for(Directory d : dir.getSubdirectories())
   {
    subNodes[cnod] = new FileTreeNode(d); 
    createTree(subNodes[cnod], d);
    cnod++;
   }
  }
  
  if( dir.getFiles() != null )
  {
   for(String f : dir.getFiles())
   {
    subNodes[cnod] = new FileTreeNode(dir,f); 
    cnod++;
   }
  }
  
  treeGrid.getData().addList(subNodes, nd);

 }

 public ModelPath getModelPath()
 {
  FileTreeNode nd = (FileTreeNode) treeGrid.getSelectedRecord();

  if(nd == null)
   return null;

  ModelPath pth = new ModelPath();

  Directory dir = nd.getDirectory();

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
 
 private static class FileTreeNode extends TreeNode
 {
  public FileTreeNode( Directory dir )
  {
   setAttribute("Name", dir.getName() );
   setAttribute("Dir", dir );
   setTitle(dir.getName());
   setIsFolder(true);
  }

  public FileTreeNode( Directory dir, String file )
  {
   setAttribute("Name", file);
   setAttribute("Dir", dir );
   setTitle(file);
   setIsFolder(false);
  }
  
  public Directory getDirectory()
  {
   return (Directory) getAttributeAsObject("Dir");
  }
 }
}
