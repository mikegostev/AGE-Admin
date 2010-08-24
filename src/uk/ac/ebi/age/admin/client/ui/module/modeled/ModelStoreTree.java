package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.common.Directory;
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
 
  FileTreeNode common = new FileTreeNode("Common",true);
  FileTreeNode priv = new FileTreeNode("Private",true);
  
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
    subNodes[cnod] = new FileTreeNode(d.getName(),true); 
    createTree(subNodes[cnod], d);
    cnod++;
   }
  }
  
  if( dir.getFiles() != null )
  {
   for(String f : dir.getFiles())
   {
    subNodes[cnod] = new FileTreeNode(f,false); 
    cnod++;
   }
  }
  
  treeGrid.getData().addList(subNodes, nd);

 }

 private static class FileTreeNode extends TreeNode
 {

  public FileTreeNode(String string, boolean dir)
  {
   setAttribute("Name", string);
   setTitle(string);
   setIsFolder(dir);
  }
 }
}
