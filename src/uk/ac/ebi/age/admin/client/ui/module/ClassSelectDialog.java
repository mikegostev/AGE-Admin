package uk.ac.ebi.age.admin.client.ui.module;

import uk.ac.ebi.age.admin.client.model.ModelImprint;

import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.layout.VLayout;

public class ClassSelectDialog extends Window
{
 public ClassSelectDialog( ModelImprint mod)
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
   
   ClassTreePanel cTree = new ClassTreePanel(mod);
   cTree.setWidth100();
   cTree.setHeight("*"); 
   
   winInter.addMember(new ClassTreePanel(mod));
   
   addItem( winInter );
 }
}
