package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.model.AgeRelationClassImprint;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;

public class RangeDomainPanel extends HLayout
{
 public RangeDomainPanel(final AgeRelationClassImprint cls, final XEditorPanel editor)
 {
  setWidth100();
  setMargin(3);
  setTitle("Range / Domain");

  Canvas rPanel = new RangeOrDomainPanel(cls, true);
  rPanel.setHeight100();
  
  Canvas dPanel = new RangeOrDomainPanel(cls, false);
  dPanel.setHeight100();
  
  setMembers(rPanel,dPanel);
  
 }

}
