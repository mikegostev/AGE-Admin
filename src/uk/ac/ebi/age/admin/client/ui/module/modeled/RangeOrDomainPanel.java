package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeRelationClassImprint;
import uk.ac.ebi.age.admin.client.ui.ClassMetaClassDef;
import uk.ac.ebi.age.admin.client.ui.ClassSelectedAdapter;
import uk.ac.ebi.age.admin.client.ui.MetaClassDef;

import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class RangeOrDomainPanel extends VLayout
{
 public RangeOrDomainPanel(final AgeRelationClassImprint cls, final boolean isRange)
 {
  String title = isRange?"Range":"Domain";
  
  setWidth100();
  
  setMargin(3);
  setTitle(title);

  ToolStrip superTS = new ToolStrip();
  superTS.setWidth100();

  Label lbl = new Label(title);
  lbl.setMargin(5);
  superTS.addMember( lbl );
  superTS.addFill();

  final MetaClassDef meta = ClassMetaClassDef.getInstance();
  
  final RelativesListPanel rangeClsList = new RelativesListPanel(meta, isRange?cls.getRange():cls.getDomain());

  ToolStripButton btadd = new ToolStripButton();
  btadd.setIcon("../images/icons/" + meta.getMetaClassName() + "/add.png");
  btadd.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    new XSelectDialog<AgeAbstractClassImprint>(meta.getRoot(cls.getModel()), meta, new ClassSelectedAdapter()
    {
     @Override
     public void classSelected(AgeAbstractClassImprint selcls)
     {
      if( isRange )
       cls.addRangeClass( (AgeClassImprint)selcls );
      else
       cls.addDomainClass( (AgeClassImprint)selcls );

      rangeClsList.addNode(selcls);
     }
    }).show();
    
   }
  });
  superTS.addButton(btadd);

  ToolStripButton btdel = new ToolStripButton();
  btdel.setIcon("../images/icons/" + meta.getMetaClassName() + "/del.png");
  btdel.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    final AgeAbstractClassImprint cimp = rangeClsList.getSelectedClass();

    if(cimp == null)
     return;

    if( isRange )
     cls.removeRangeClass( (AgeClassImprint)cimp );
    else
     cls.removeDomainClass( (AgeClassImprint)cimp );

    rangeClsList.addNode(cimp);

   }
  });
  superTS.addButton(btdel);

  addMember(superTS);

  addMember(rangeClsList);
 }

}
