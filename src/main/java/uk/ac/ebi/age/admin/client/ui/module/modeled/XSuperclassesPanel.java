package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.ModeledIcons;
import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;
import uk.ac.ebi.age.admin.client.ui.ClassSelectedAdapter;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class XSuperclassesPanel extends VLayout
{
 public XSuperclassesPanel(final AgeAbstractClassImprint cls, final XEditorPanel editor)
 {
  setWidth100();
  setMargin(3);
  setTitle("Superclasses");

  ToolStrip superTS = new ToolStrip();
  superTS.setWidth100();

  Label lbl = new Label("Superclasses");
  lbl.setMargin(5);
  superTS.addMember( lbl );
  superTS.addFill();

  
  final RelativesListPanel superClsList = new RelativesListPanel(editor.getMetaClassDef(), cls.getParents());

  ToolStripButton btadd = new ToolStripButton();
  btadd.setIcon(ModeledIcons.get.getString(editor.getMetaClassDef().getMetaClassName()+"AddSuperclass"));
  btadd.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    editor.addSuperclass(cls, new ClassSelectedAdapter()
    {
     @Override
     public void classSelected(AgeAbstractClassImprint cls)
     {
      superClsList.addNode(cls);
     }
    });
   }
  });
  superTS.addButton(btadd);

  ToolStripButton btdel = new ToolStripButton();
  btdel.setIcon(ModeledIcons.get.getString(editor.getMetaClassDef().getMetaClassName()+"DeleteSuperclass"));
  btdel.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    final AgeAbstractClassImprint cimp = superClsList.getSelectedClass();

    if(cimp == null)
     return;

    if(cls.getParents().size() == 1)
     SC.warn("You can't delete the last superclass");
    else
    {
     editor.unlink(cimp, cls);
     superClsList.deleteNode(cimp);
    }
   }
  });
  superTS.addButton(btdel);

  addMember(superTS);

  addMember(superClsList);
 }

}
