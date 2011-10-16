package uk.ac.ebi.age.admin.client.ui.module.auth;

import java.util.Collection;

import uk.ac.ebi.age.admin.client.AgeAdminService;
import uk.ac.ebi.age.admin.client.ui.module.classif.TagSelectDialog2;
import uk.ac.ebi.age.admin.client.ui.module.classif.TagSelectedListener;
import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.ext.authz.TagRef;
import uk.ac.ebi.age.ext.entity.GraphEntity;
import uk.ac.ebi.age.ui.client.LinkClickListener;
import uk.ac.ebi.age.ui.client.LinkManager;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.pri.util.collection.Collections;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;

public class SystemPermissionsPanel extends VLayout implements LinkClickListener
{
 private Label graphTags;
 
 public SystemPermissionsPanel()
 {
  addMember( graphTags = new Label() );
  addMember(new ACLPanel(null, null, Constants.sysACLServiceName));
  
  
  LinkManager.getInstance().addLinkClickListener("graphTags", this);

  AgeAdminService.Util.getInstance().getEntityTags( GraphEntity.getInstance(), new AsyncCallback<Collection<TagRef>>()
    {
     
     @Override
     public void onSuccess(Collection<TagRef> result)
     {
      if( result == null )
       result = Collections.emptyList();
      
      setGraphTags( result );
     }
     
     @Override
     public void onFailure(Throwable caught)
     {
      SC.warn("Action failed: "+caught.getMessage());
     }
    });
  
 }

 private void setGraphTags(Collection<TagRef> tgList)
 {
  String contents = "Graph tags: ";
  
  boolean first=true;
  
  for( TagRef tr : tgList )
  {
   if( ! first )
    contents +="; ";
   else
    first=false;
   
   contents += "<b>"+tr.getClassiferName()+":"+tr.getTagName()+"</b>";
   
   if( tr.getTagValue() != null )
    contents+= "="+tr.getTagValue();
  }
  
  contents += "&nbsp;&nbsp;,<a href='javascript:linkClicked(&quot;graphTags&quot;,null)'>manage tags</a>";
  
  graphTags.setContents(contents);
 }

 @Override
 public void linkClicked(String param)
 {
  AgeAdminService.Util.getInstance().getEntityTags(GraphEntity.getInstance(), new AsyncCallback<Collection<TagRef>>()
  {

   @Override
   public void onSuccess(final Collection<TagRef> result)
   {
    new TagSelectDialog2(result, new TagSelectedListener()
    {
     @Override
     public void tagSelected(final Collection<TagRef> tr)
     {

      AgeAdminService.Util.getInstance().storeEntityTags(GraphEntity.getInstance(), tr, new AsyncCallback<Void>()
      {

       @Override
       public void onFailure(Throwable caught)
       {
        SC.warn("System failure: " + caught.getMessage());
       }

       @Override
       public void onSuccess(Void result)
       {
        setGraphTags(tr);

       }
      });

     }
    }).show();
   }

   @Override
   public void onFailure(Throwable caught)
   {
    SC.warn("Action failed: " + caught.getMessage());
   }
  });
 }


}
