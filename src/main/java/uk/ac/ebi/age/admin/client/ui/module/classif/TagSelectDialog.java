package uk.ac.ebi.age.admin.client.ui.module.classif;

import java.util.Collections;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class TagSelectDialog extends Window
{

 public TagSelectDialog( final TagSelectedListener lsnr )
 {
  setTitle("Select tag");
  setShowMinimizeButton(false);
  setIsModal(true);
  setShowModalMask(true);
  setWidth("90%");
  setHeight("90%");
  setAutoCenter(true);

  // addCloseClickHandler( clsHnd );

  VLayout panel = new VLayout();
  panel.setWidth100();
  panel.setHeight100();

  final ClassifiersPanel clsfPnl = new ClassifiersPanel( true, null );

  panel.addMember(clsfPnl);

  HLayout buttonPanel = new HLayout();
  // buttonPanel.setWidth100();
  buttonPanel.setMargin(5);
  buttonPanel.setMembersMargin(15);
  buttonPanel.setAlign(Alignment.CENTER);

  Button okBt = new Button("OK");
  okBt.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    lsnr.tagSelected( Collections.singletonList(clsfPnl.getSelectedTag()) );
   }
  });

  buttonPanel.addMember(okBt);

  Button cancelBt = new Button("Cancel");
  cancelBt.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    lsnr.tagSelected(null);
   }
  });

  buttonPanel.addMember(cancelBt);

  panel.addMember(buttonPanel);

  addItem(panel);
 }

 public void close()
 {
  destroy();
 }

}
