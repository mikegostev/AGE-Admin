package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;
import uk.ac.ebi.age.admin.client.ui.ClassSelectedCallback;
import uk.ac.ebi.age.admin.client.ui.ImprintTreeNode;
import uk.ac.ebi.age.admin.client.ui.MetaClassDef;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class XSelectDialog<T extends AgeAbstractClassImprint> extends Window
{

 public XSelectDialog(T rootNode, MetaClassDef creator, final ClassSelectedCallback cb)
 {
  setWidth(600);
  setHeight(600);
  setTitle("Select class");
  setShowMinimizeButton(false);
  setIsModal(true);
  setShowModalMask(true);
  centerInPage();

  VLayout winInter = new VLayout();
  winInter.setWidth100();
  winInter.setHeight100();

  final XTreePanel cTree = new XTreePanel(rootNode, creator);
  cTree.setWidth100();
  cTree.setHeight("*");

  winInter.addMember(cTree);

  HLayout btnPanel = new HLayout();
  btnPanel.setLayoutAlign(Alignment.CENTER);
  btnPanel.setWidth("1%");
  btnPanel.setHeight("40");
  btnPanel.setMembersMargin(30);

  IButton button;

  button = new IButton("OK");
  button.setLayoutAlign(VerticalAlignment.CENTER);
  button.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    ImprintTreeNode nd = (ImprintTreeNode) cTree.getSelectedRecord();

    if(nd == null)
     return;

    destroy();
    cb.classSelected(nd.getClassImprint());
   }
  });
  btnPanel.addMember(button);

  button = new IButton("Cancel");
  button.setLayoutAlign(VerticalAlignment.CENTER);
  button.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    destroy();
    cb.selectionCanceled();
   }
  });
  btnPanel.addMember(button);

  winInter.addMember(btnPanel);

  addItem(winInter);
 
  
  cTree.addRecordDoubleClickHandler( new RecordDoubleClickHandler()
  {
   
   @Override
   public void onRecordDoubleClick(RecordDoubleClickEvent event)
   {
    ImprintTreeNode nd = (ImprintTreeNode) cTree.getSelectedRecord();

    if(nd == null)
     return;

    destroy();
    cb.classSelected(nd.getClassImprint());   }
  });
 
 }
}
