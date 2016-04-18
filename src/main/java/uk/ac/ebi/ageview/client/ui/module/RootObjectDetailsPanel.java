package uk.ac.ebi.ageview.client.ui.module;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.ebi.age.ui.client.LinkClickListener;
import uk.ac.ebi.age.ui.client.LinkManager;
import uk.ac.ebi.age.ui.client.ObjectProviderService;
import uk.ac.ebi.age.ui.client.module.PagingRuler;
import uk.ac.ebi.age.ui.shared.imprint.ObjectId;
import uk.ac.ebi.age.ui.shared.imprint.ObjectImprint;
import uk.ac.ebi.ageview.client.AgeViewGWTService;
import uk.ac.ebi.ageview.client.AgeViewGWTServiceAsync;
import uk.ac.ebi.ageview.client.query.AttributedImprint;
import uk.ac.ebi.ageview.client.shared.AttributeReport;
import uk.ac.ebi.ageview.client.shared.Pair;
import uk.ac.ebi.ageview.client.ui.SourceIconBundle;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.viewer.DetailViewer;

public class RootObjectDetailsPanel extends VLayout
{
 private static final int BRIEF_LEN=200;
 
 private String groupID;
 
 private String query;
 private boolean searchAtNames;
 private boolean searchAtValues;
 
 private final ObjectProviderService objectSvc = new BioSDObjectProvider();
 
 private List< Pair<String, String> > otherInfoList;
 private final Map<String, List<AttributedImprint> > attMap = new HashMap<String, List<AttributedImprint>>();
// private List< AttributedImprint > publList;
// private List< AttributedImprint > contList;
 
 private PagingRuler pager;
 
 @SuppressWarnings("unchecked")
 public RootObjectDetailsPanel(final Record r, final String query, final boolean searchAtNames, final boolean searchAtValues)
 {
  this.query=query;
  this.searchAtNames=searchAtNames;
  this.searchAtValues=searchAtValues;
  
  DataSource ds = new DataSource();
  
  ds.setClientOnly(true);
  
  ListGridRecord rec = new ListGridRecord();

  groupID = r.getAttributeAsString("__ID");

  String[] attrs = r.getAttributes();

  for( String s : attrs )
  {
   if( s.startsWith("__")  )
    continue;
   
   ds.addField(new DataSourceTextField(s, s));
   
   String val = r.getAttributeAsString(s);
   
   if("Link".equals(s) )
   {
    String dsAttr = r.getAttributeAsString("Data Source");
    
    if( dsAttr != null )
    {
     String icon = SourceIconBundle.getIcon(dsAttr);
     
     if( icon != null )
      rec.setAttribute(s, "<a target='_blank' border=0 href='"+val+"'><img border=0 src='"+icon+"'></a>");
     else
      rec.setAttribute(s, "<a target='_blank' border=0 href='"+val+"'>"+val+"</a>");
//     
//     if("Array Express".equals(dsAttr))
//      rec.setAttribute(s, "<a target='_blank' border=0 href='"+val+"'><img border=0 src='images/ae.png'></a>");
//     else if("Pride".equals(dsAttr))
//      rec.setAttribute(s, "<a target='_blank' border=0 href='"+val+"'><img border=0 src='images/pride.jpg'></a>");
//     else
//      rec.setAttribute(s, "<a target='_blank' border=0 href='"+val+"'>"+val+"</a>");
    }
    else
     rec.setAttribute(s, "<a target='_blank' border=0 href='"+val+"'>"+val+"</a>");
    
   }
   else if("Submission PubMed ID".equals(s))
    rec.setAttribute(s, "<a target='_blank' border=0 href='http://www.ncbi.nlm.nih.gov/pubmed/"+val+"'>"+val+"</a>");
   else if("Submission DOI".equals(s))
    rec.setAttribute(s, "<a target='_blank' border=0 href='http://dx.doi.org/"+val+"'>"+val+"</a>");
   else if( val.length() > 8 && val.substring(0, 7).equalsIgnoreCase("http://") )
    rec.setAttribute(s, "<a target=\"_blank\" href=\"" + val + "\">" + val + "</a>");
   else
    rec.setAttribute(s, val);

   //   System.out.println("At: "+s+" "+r.getAttributeAsString(s));
  }

  for( String attr : attrs )
  {
   if( attr.startsWith("__$att$") )
   {
    List< AttributedImprint > list = (List< AttributedImprint >) r.getAttributeAsObject(attr);
    String title = attr.substring(7);
    
    String repstr = makeRepresentationString2(list, title);

    ds.addField(new DataSourceTextField(title, title) );
    
    rec.setAttribute(title, repstr);
    
    attMap.put(title, list);
   }
  }
  
  
  otherInfoList = (List< Pair<String, String> >) r.getAttributeAsObject("__other");
  
  if( otherInfoList != null && otherInfoList.size() > 0 )
  {
   String repstr = "<div style='float: left' class='briefObjectRepString'>";
   
   int cCount = 0;
   
   for( Pair<String, String> fstEl : otherInfoList )
   {
    if( cCount > BRIEF_LEN )
     break;
    
    repstr += "<b>"+fstEl.getFirst()+"</b>"; 
    
    repstr += ": "+fstEl.getSecond()+"; ";

    cCount+=fstEl.getFirst().length()+fstEl.getSecond().length();
   }

   repstr += "</div><div><a class='el' href='javascript:linkClicked(&quot;"+groupID+"&quot;,&quot;other&quot;)'>more</a></div>";

   
   ds.addField(new DataSourceTextField("Other", "Other") );
   
   rec.setAttribute("Other", repstr);
  }
   
  
  ds.addData(rec);
  
  
  DetailViewer dv = new DetailViewer();
  dv.setWidth("90%");
  dv.setDataSource(ds);
  dv.setStyleName("groupDetails");
  dv.setCanSelectText(true);
  
  dv.setAutoFetchData(true);
  
  Canvas spc = new Canvas();
  spc.setHeight(15);

  addMember(spc);
  
  addMember(dv);
  
  spc = new Canvas();
  spc.setHeight(5);
  addMember(spc);
  
 
  spc = new Canvas();
  spc.setHeight(5);
  addMember(spc);

  pager = new PagingRuler(groupID);
  pager.setWidth("99%");
  pager.setVisible(false);
  addMember(pager);
  
  LinkManager.getInstance().addLinkClickListener(groupID, new LinkClickListener()
  {
   @Override
   public void linkClicked(String param)
   {
    if( "other".equals(param) )
    {
     new NameValuePanel( otherInfoList ).show();
     return;
    }
    else if( attMap.containsKey(param) )
    {
     new AttributedListPanel( param, attMap.get(param) ).show();
     return;
    }
   
    int pNum = 1;
    
    try
    {
     pNum=Integer.parseInt(param);
    }
    catch(Exception e)
    {
    }
    
   }
  });
 }
 
 private String makeRepresentationString( List< AttributedImprint > list, String theme )
 {
  AttributedImprint fstEl = list.get(0);
  String repstr = "";
  
  int lastBold = -1;
  
  for( AttributeReport attr : fstEl.getAttributes() )
  {
   if( repstr.length() > BRIEF_LEN )
    break;
   
   repstr += "<b>"+attr.getName()+"</b>"; 
   
   lastBold=repstr.length();
   
   repstr += ": "+attr.getValue()+"; ";
  }

  if( repstr.length() > BRIEF_LEN )
   repstr=repstr.substring(0,BRIEF_LEN);
  
  if( BRIEF_LEN < lastBold )
   repstr+="</b>";
 
  repstr += "... <a class='el' href='javascript:linkClicked(&quot;"+groupID+"&quot;,&quot;"+theme+"&quot;)'>more</a>";
  
  return repstr;
 }
 
 private String makeRepresentationString2( List< AttributedImprint > list, String theme )
 {
  AttributedImprint fstEl = list.get(0);
  String repstr = "<div style='float: left' class='briefObjectRepString'>";
  
  int cCount = 0;
  
  for( AttributeReport attr : fstEl.getAttributes() )
  {
   if( cCount > BRIEF_LEN )
    break;
   
   repstr += "<b>"+attr.getName()+"</b>"; 
   
   repstr += ": "+attr.getValue()+"; ";

   cCount+=attr.getName().length()+attr.getValue().length();
  }

  repstr += "</div><div><a class='el' href='javascript:linkClicked(&quot;"+groupID+"&quot;,&quot;"+ SafeHtmlUtils.htmlEscape(theme)+"&quot;)'>more</a></div>";
  
  return repstr;
 }
 

 private static class BioSDObjectProvider implements ObjectProviderService
 {
  private final AgeViewGWTServiceAsync biosd =  AgeViewGWTService.Util.getInstance();
  
  @Override
  public void getObject(ObjectId id, AsyncCallback<ObjectImprint> cb)
  {
   biosd.getObjectImprint( id, cb );
  }
  
 }
 
}
