package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;
import uk.ac.ebi.age.admin.client.ui.ClassSelectedAdapter;

import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class XSubclassesPanel extends VLayout
{
 public XSubclassesPanel(final AgeAbstractClassImprint cls, final XEditorPanel editor)
 {
  setWidth100();
  
  setMargin(3);
  setTitle("Subclasses");

  ToolStrip superTS = new ToolStrip();
  superTS.setWidth100();

  Label lbl = new Label("Subclasses");
  lbl.setMargin(5);
  superTS.addMember( lbl );
  superTS.addFill();

  
  final RelativesListPanel subClsList = new RelativesListPanel(editor.getMetaClassDef().getMetaClassName(), cls.getChildren());

  ToolStripButton btadd = new ToolStripButton();
  btadd.setIcon("../images/icons/" + editor.getMetaClassDef().getMetaClassName() + "/add.png");
  btadd.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    editor.addSubclass(cls, new ClassSelectedAdapter()
    {
     @Override
     public void classSelected(AgeAbstractClassImprint cls)
     {
      subClsList.addNode(cls);
     }
    });
   }
  });
  superTS.addButton(btadd);

  ToolStripButton btdel = new ToolStripButton();
  btdel.setIcon("../images/icons/" + editor.getMetaClassDef().getMetaClassName() + "/del.png");
  btdel.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    final AgeAbstractClassImprint cimp = subClsList.getSelectedClass();

    if(cimp == null)
     return;

    if(cimp.getParents().size() == 1)
    {
     SC.confirm("Class '" + cimp.getName() + "' will be deleted", new BooleanCallback()
     {

      @Override
      public void execute(Boolean value)
      {
       if(!value)
        return;

       editor.unlink(cls, cimp);
       subClsList.deleteNode(cimp);
      }
     });
    }
    else
    {
     editor.unlink(cls, cimp);
     subClsList.deleteNode(cimp);
    }

   }
  });
  superTS.addButton(btdel);

  addMember(superTS);

  addMember(subClsList);
 }

}
