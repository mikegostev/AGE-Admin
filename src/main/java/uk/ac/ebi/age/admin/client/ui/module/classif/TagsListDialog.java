package uk.ac.ebi.age.admin.client.ui.module.classif;

import java.util.List;

import uk.ac.ebi.age.ext.authz.TagRef;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class TagsListDialog extends Window
{
 private TagListPanel tlPanel;
 
 private CloseClickHandler clsHnd = new CloseClickHandler()
 {
  @Override
  public void onCloseClick(CloseClientEvent event)
  {
   close();
  }
 };

 
 public TagsListDialog( List<TagRef> tags, final TagSelectedListener lsnr )
 {
  setTitle("Select Tags");
  setShowMinimizeButton(false);
  setIsModal(true);
  setShowModalMask(true);
//  setAutoSize(true);
  setAutoCenter(true);
  setHeight("90%");
  setWidth("70%");
  
  
  VLayout panel = new VLayout();
  
  panel.addMember( tlPanel = new TagListPanel( tags ) );
  
  HLayout buttonPanel = new HLayout();
//  buttonPanel.setWidth100();
  buttonPanel.setMargin(5);
  buttonPanel.setMembersMargin(15);
  buttonPanel.setAlign(Alignment.CENTER);
  
  Button okBt = new Button("OK");
  okBt.addClickHandler( new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    close();

    lsnr.tagSelected(tlPanel.getTags());
    
//    for( TagRef tr : tlPanel.getTags() )
//     System.out.println(tr.getClassiferName()+":"+tr.getTagName());
   }
  });
  
  buttonPanel.addMember( okBt );
  
  
  Button cancelBt = new Button("Cancel");
  cancelBt.addClickHandler( new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    close();
   }
  });
  
  buttonPanel.addMember( cancelBt );

  panel.addMember(buttonPanel);
    
  addItem(panel);
  
  addCloseClickHandler( clsHnd );

 }
 
 public void close()
 {
  tlPanel.destroy();
  destroy();
 }
}
