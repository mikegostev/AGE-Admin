package uk.ac.ebi.age.admin.client.ui.module.classif;

import uk.ac.ebi.age.admin.client.ui.ObjectSelectedListened;
import uk.ac.ebi.age.ext.authz.TagRef;

import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class ClassifiersPanel extends HLayout
{
 private ClassifiersList classifierList;
 
 public ClassifiersPanel()
 {
  this( false, null );
 }
  
 public ClassifiersPanel( boolean readOnly, ObjectSelectedListened<TagRef> lsnr )
 { 
  setWidth100();
  setHeight100();
  
  
  VLayout tagsPanel = new VLayout();
  tagsPanel.setWidth("50%");
  tagsPanel.setHeight100();
  
  classifierList = new ClassifiersList( tagsPanel, readOnly, lsnr );
  classifierList.setWidth("50%");
  
  addMember(classifierList);
  addMember(tagsPanel);
 }
 
 public TagRef getSelectedTag()
 {
  return classifierList.getSelectedTag();
 }
}
