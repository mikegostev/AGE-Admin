package uk.ac.ebi.ageview.server.service;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.NullFragmenter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;

import uk.ac.ebi.age.admin.server.mng.Configuration;
import uk.ac.ebi.age.annotation.AnnotationManager;
import uk.ac.ebi.age.annotation.Topic;
import uk.ac.ebi.age.authz.ACR.Permit;
import uk.ac.ebi.age.authz.BuiltInUsers;
import uk.ac.ebi.age.authz.PermissionManager;
import uk.ac.ebi.age.authz.SecurityChangedListener;
import uk.ac.ebi.age.ext.annotation.AnnotationDBException;
import uk.ac.ebi.age.ext.authz.SystemAction;
import uk.ac.ebi.age.ext.authz.TagRef;
import uk.ac.ebi.age.ext.entity.Entity;
import uk.ac.ebi.age.ext.user.exception.NotAuthorizedException;
import uk.ac.ebi.age.model.AgeAttribute;
import uk.ac.ebi.age.model.AgeAttributeClass;
import uk.ac.ebi.age.model.AgeClass;
import uk.ac.ebi.age.model.AgeObject;
import uk.ac.ebi.age.model.AgeObjectAttribute;
import uk.ac.ebi.age.model.AgeRelation;
import uk.ac.ebi.age.model.AgeRelationClass;
import uk.ac.ebi.age.model.Attributed;
import uk.ac.ebi.age.model.DataType;
import uk.ac.ebi.age.query.AgeQuery;
import uk.ac.ebi.age.query.ClassOrSubclassNameExpression;
import uk.ac.ebi.age.storage.AgeStorage;
import uk.ac.ebi.age.storage.DataChangeListener;
import uk.ac.ebi.age.storage.MaintenanceModeListener;
import uk.ac.ebi.age.storage.ModelChangeListener;
import uk.ac.ebi.age.storage.exeption.IndexIOException;
import uk.ac.ebi.age.storage.index.Selection;
import uk.ac.ebi.age.storage.index.TextFieldExtractor;
import uk.ac.ebi.age.storage.index.TextIndex;
import uk.ac.ebi.age.storage.index.TextValueExtractor;
import uk.ac.ebi.age.ui.server.imprint.ImprintBuilder;
import uk.ac.ebi.age.ui.server.imprint.ImprintingHint;
import uk.ac.ebi.age.ui.server.imprint.StringProcessor;
import uk.ac.ebi.age.ui.shared.imprint.AttributeImprint;
import uk.ac.ebi.age.ui.shared.imprint.ObjectId;
import uk.ac.ebi.age.ui.shared.imprint.ObjectImprint;
import uk.ac.ebi.age.ui.shared.imprint.ObjectValue;
import uk.ac.ebi.age.ui.shared.imprint.Value;
import uk.ac.ebi.ageview.client.query.AttributedImprint;
import uk.ac.ebi.ageview.client.query.Report;
import uk.ac.ebi.ageview.client.shared.MaintenanceModeException;
import uk.ac.ebi.ageview.server.stat.AgeViewStat;
import uk.ac.ebi.mg.assertlog.Log;
import uk.ac.ebi.mg.assertlog.LogFactory;

import com.pri.util.StringUtils;

public class AgeViewServiceImpl extends AgeViewService implements SecurityChangedListener
{
 private static Log log = LogFactory.getLog(AgeViewServiceImpl.class);
 
 private AgeStorage storage;
 
 private static final String ROOTOBJ_INDEX_NAME="ROOTOBJINDEX";
 
 private TextIndex rootObjIndex;
 
 private AgeQuery rootSelectQuery;
// private List<AgeObject> groupList;
 
 private AgeClass rootClass;
 
 private AgeRelationClass objToPublicationRelClass;
 private AgeRelationClass objToContactRelClass;
 
 private AgeAttributeClass titleAttributeClass;
 private AgeAttributeClass commentAttributeClass;

 
 private AgeViewStat statistics;
 
 private volatile boolean  maintenanceMode = false;
 
 private final WeakHashMap<String, UserCacheObject> userCache  = new WeakHashMap<String, UserCacheObject>();
 
 private final Analyzer analizer = new StandardAnalyzer();
 private final QueryParser queryParser = new QueryParser( AgeViewConfigManager.ATTR_VALUES_FIELD_NAME, analizer );
 private final SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter("<span class='sHL'>","</span>");

 private ImprintingHint objConvHint;
 


 private final StringProcessor htmlEscProc = new StringProcessor()
 {
  @Override
  public String process(String str)
  {
   return StringUtils.htmlEscaped(str);
  }
 };
 
 public AgeViewServiceImpl( AgeStorage stor ) throws AgeViewInitException
 {
  long startTime=0;
  
  storage=stor;
  
  
 
  objConvHint = new ImprintingHint();
  objConvHint.setConvertRelations(true);
  objConvHint.setConvertAttributes(true);
  objConvHint.setQualifiersDepth(2);
  objConvHint.setResolveObjectAttributesTarget(true);
  objConvHint.setResolveRelationsTarget(false);
  objConvHint.setConvertImplicitRelations(false);
  
  ClassOrSubclassNameExpression clsExp = new ClassOrSubclassNameExpression();
  clsExp.setClassName( AgeViewConfigManager.ROOT_CLASS_NAME );

//  orExp.addExpression(clsExp);

  
  rootSelectQuery = AgeQuery.create(clsExp);
  
  ArrayList<TextFieldExtractor> extr = new ArrayList<TextFieldExtractor>(4);
  
  TagsExtractor tagExtr = new TagsExtractor();
  OwnerExtractor ownExtr = new OwnerExtractor();

  extr.add( new TextFieldExtractor(AgeViewConfigManager.SECTAGS_FIELD_NAME, tagExtr));
  extr.add( new TextFieldExtractor(AgeViewConfigManager.OWNER_FIELD_NAME, ownExtr) );
  
  extr.add( new TextFieldExtractor(AgeViewConfigManager.ATTR_NAMES_FIELD_NAME, new AttrNamesExtractor()) );
  extr.add( new TextFieldExtractor(AgeViewConfigManager.ATTR_VALUES_FIELD_NAME, new AttrValuesExtractor()) );

  
  assert ( startTime = System.currentTimeMillis() ) != 0;

  long idxTime=0;

  try
  {
   assert ( idxTime = System.currentTimeMillis() ) != 0;

   rootObjIndex = storage.createTextIndex(ROOTOBJ_INDEX_NAME, rootSelectQuery, extr);

   assert log.info("Group index building time: "+StringUtils.millisToString(System.currentTimeMillis()-idxTime));

  }
  catch(IndexIOException e)
  {
   throw new AgeViewInitException("Init failed. Can't create group index",e);
  }

  
  assert log.info("Indices building time: "+StringUtils.millisToString(System.currentTimeMillis()-startTime));

  
  
  
  storage.addDataChangeListener( new DataChangeListener() 
  {
   @Override
   public void dataChanged()
   {
    //collectStats();
   }
  } );
  

  storage.addMaintenanceModeListener(new MaintenanceModeListener()
  {
   @Override
   public void exitMaintenanceMode()
   {
    maintenanceMode = false;
   }
   
   @Override
   public void enterMaintenanceMode()
   {
    maintenanceMode = true;
   }
  });
  
  
  storage.addModelChangeListener( new ModelChangeListener()
  {
   
   @Override
   public void modelChanged()
   {
    updateClassLinks();
   }
  });

  assert ( startTime = System.currentTimeMillis() ) != 0;
  
//  collectStats();

  assert log.info("Stats collecting time: "+(System.currentTimeMillis()-startTime)+"ms");

 }


 private void updateClassLinks()
 {
  
  rootClass = storage.getSemanticModel().getDefinedAgeClass( AgeViewConfigManager.ROOT_CLASS_NAME );
  titleAttributeClass = storage.getSemanticModel().getDefinedAgeAttributeClass( AgeViewConfigManager.TITLE_ATTR_CLASS_NAME );
  
  objToPublicationRelClass = storage.getSemanticModel().getDefinedAgeRelationClass(AgeViewConfigManager.HAS_PUBLICATION_REL_CLASS_NAME);
  
  objToContactRelClass = storage.getSemanticModel().getDefinedAgeRelationClass(AgeViewConfigManager.CONTACT_OF_REL_CLASS_NAME);
  
  if(objToContactRelClass != null )
   objToContactRelClass = objToContactRelClass.getInverseRelationClass();
 
 }
 
 
 
 @Override
 public Report selectRootObjects(String query, boolean searchAttrNm, boolean searchAttrVl, int offset, int count) throws MaintenanceModeException
 {
  
  Highlighter highlighter = null;

  try
  {
   if( query != null )
   {
    highlighter = new Highlighter(htmlFormatter, new QueryScorer( queryParser.parse(query) ) );
    highlighter.setTextFragmenter(new NullFragmenter());
   }
  }
  catch(ParseException e)
  {
   Report rep = new Report();
   rep.setObjects(new ArrayList<AttributedImprint>());
   rep.setTotalObjects(0);
  
   return rep;
  }
  
  
  
  if( maintenanceMode )
   throw new MaintenanceModeException();
 
  String user = Configuration.getDefaultConfiguration().getSessionManager().getEffectiveUser();

  
  if( query == null )
   query = "";
  else
   query=query.trim();
  
  
  StringBuilder sb = new StringBuilder();
  
  if( query.length() > 0  )
  {
   sb.append("( ");

   if(searchAttrNm)
    sb.append(AgeViewConfigManager.ATTR_NAMES_FIELD_NAME).append(":(").append(query).append(") OR ");

   if(searchAttrVl)
    sb.append(AgeViewConfigManager.ATTR_VALUES_FIELD_NAME).append(":(").append(query).append(") OR ");

   sb.setLength(sb.length() - 4);

   sb.append(" ) AND ");
  }
  

  if( ! BuiltInUsers.SUPERVISOR.getName().equals(user) )
  {
   UserCacheObject uco = getUserCacheobject(user);

   sb.append("(").append(AgeViewConfigManager.SECTAGS_FIELD_NAME).append(":(").append(uco.getAllowTags()).append(") OR ")
   .append(AgeViewConfigManager.OWNER_FIELD_NAME).append(":(").append(user).append("))").append(" AND ");

   if(uco.getDenyTags().length() > 0)
    sb.append("NOT ").append(AgeViewConfigManager.SECTAGS_FIELD_NAME).append(":(").append(uco.getDenyTags()).append(") AND ");
  }
  
  int qLen = sb.length()-5;
 
  if( qLen > 0 )
   sb.setLength(qLen);
  
  String lucQuery = sb.toString(); 
  
  assert log.debug("Query: "+lucQuery);
  
  Selection sel = null;
  Report rep = new Report();
  
  try
  {
   storage.lockRead();

   sel = rootObjIndex.select(lucQuery, offset, count, null );

   List<AttributedImprint> res = new ArrayList<AttributedImprint>();

   List<AgeObject> groups = sel.getObjects();
   
   if(count > groups.size())
    count = groups.size();

   for(int i = 0; i < count; i++)
   {
    AttributedImprint gr = createRootObject(groups.get(i),  highlighter, searchAttrNm, searchAttrVl);

    res.add(gr);
   }

   rep.setObjects(res);
   rep.setTotalObjects(sel.getTotalCount());
  }
  finally
  {
   storage.unlockRead();
  }
  
  return rep;
 }
 
 private UserCacheObject getUserCacheobject(String user)
 {
  synchronized(userCache)
  {
   UserCacheObject uco = userCache.get(user);
   
   if( uco != null )
    return uco;
   
   uco = new UserCacheObject();
   uco.setUserName(user);

   StringBuilder sb = new StringBuilder(100);
   
   Collection<TagRef> tags = Configuration.getDefaultConfiguration().getPermissionManager().getAllowTags(SystemAction.READ, user);
   
   if( tags != null )
   {
    for( TagRef tr : tags )
     sb.append(tr.getClassiferName().length()).append(tr.getClassiferName()).append(tr.getTagName()).append(" ");
   }
   
   if( sb.length() > 0 )
    sb.setLength(sb.length()-1);
   else
    sb.append("XXX");
   
   uco.setAllowTags(sb.toString());
   
   sb.setLength(0);
   
   tags = Configuration.getDefaultConfiguration().getPermissionManager().getDenyTags(SystemAction.READ, user);

   if( tags != null )
   {
    for( TagRef tr : tags )
     sb.append(tr.getClassiferName().length()).append(tr.getClassiferName()).append(tr.getTagName()).append(" ");
   }
   
   if( sb.length() > 0 )
    sb.setLength(sb.length()-1);
   
   uco.setDenyTags(sb.toString());

   userCache.put(user, uco);
   
   return uco;
  }
 }

 private String highlight( Highlighter hlighter, Object str )
 {
  if( str == null )
   return null;
  
  String s = str.toString();

  if( hlighter == null )
   return s;
  
  if( s.startsWith("http://") )
   return s;
  
  try
  {
   String out = hlighter.getBestFragment(analizer, "", s);
   
   if( out == null )
    return s;
   
   return out;
  }
  catch(Exception e)
  {
  }
 
  return s;
 }
 
 private AttributedImprint createRootObject( AgeObject obj, Highlighter hlighter, boolean hlName, boolean hlValue )
 {
  AttributedImprint sgRep = new AttributedImprint();

  String strN = null;
  String strV = null;
  
  strV=obj.getId();
  
  sgRep.setId(strV );

//  if( hlValue )  
//   strV = highlight(hlighter, obj.getId());

//  sgRep.addAttribute("Submission ID", obj.getSubmission().getId(), true, 0);
  sgRep.addAttribute("__ID", strV, true, 0);
  
  
  if( titleAttributeClass == null )
   titleAttributeClass = storage.getSemanticModel().getDefinedAgeAttributeClass( AgeViewConfigManager.TITLE_ATTR_CLASS_NAME );
   
  if( titleAttributeClass != null )
  {

   Object descVal = obj.getAttributeValue(titleAttributeClass);

   if(descVal != null)
   {
    strV = StringUtils.htmlEscaped(descVal.toString());

    if(hlValue)
     strV = highlight(hlighter, strV);

    sgRep.setTitle(strV);
   }
  }
 
  for( AgeAttribute atr : obj.getAttributes() )
  {
   AgeAttributeClass atCls = atr.getAgeElClass();
   
   if( commentAttributeClass == null )
    commentAttributeClass = storage.getSemanticModel().getDefinedAgeAttributeClass( AgeViewConfigManager.COMMENT_ATTR_CLASS_NAME );

   
   if( commentAttributeClass != null && atCls.isClassOrSubclass( commentAttributeClass ) )
   {
    strN = StringUtils.htmlEscaped( atCls.getName() );
    
    if( hlName )  
     strN = highlight(hlighter, strN);

    strV = atr.getValue()!=null? StringUtils.htmlEscaped(atr.getValue().toString()):null;
    
    if( hlValue )  
     strV = highlight(hlighter, strV );
    
    sgRep.addOtherInfo( strN, strV );
   }
   else if( atCls.getDataType() == DataType.OBJECT )
   {
    strN = StringUtils.htmlEscaped( atCls.getName() );
    
    if( hlName )  
     strN = highlight(hlighter, strN);

    
    sgRep.attachObjects( strN, createAttributedObject( ((AgeObjectAttribute)atr).getValue(), hlighter, hlName, hlValue)  );
   } 
   else
   {
    strN = StringUtils.htmlEscaped( atCls.getName() );
    
    if( hlName )  
     strN = highlight(hlighter, strN);

    strV = atr.getValue()!=null? StringUtils.htmlEscaped(atr.getValue().toString()):null;
    
    if( hlValue )  
     strV = highlight(hlighter, strV );

     sgRep.addAttribute(strN, strV, atr.getAgeElClass().isCustom(),atr.getOrder());
   }
  }
  
  if( objToPublicationRelClass == null )
   objToPublicationRelClass = storage.getSemanticModel().getDefinedAgeRelationClass(AgeViewConfigManager.HAS_PUBLICATION_REL_CLASS_NAME);

  if( objToPublicationRelClass != null )
  {
   
   Collection<? extends AgeRelation> pubRels =  obj.getRelationsByClass(objToPublicationRelClass, false);
   
   if( pubRels != null )
   {
    if( hlName )  
     strN = highlight(hlighter, "Publications");
    else
     strN = "Publications";
    
    for( AgeRelation pRel : pubRels )
     sgRep.attachObjects( strN, createAttributedObject(pRel.getTargetObject(), hlighter, hlName, hlValue ) );
   }

  }
  

  if( objToContactRelClass == null )
   objToContactRelClass = storage.getSemanticModel().getDefinedAgeRelationClass(AgeViewConfigManager.CONTACT_OF_REL_CLASS_NAME);

  if( objToContactRelClass!= null )
  {
   Collection< ? extends AgeRelation> persRels = obj.getRelationsByClass(objToContactRelClass, false);

   if(persRels != null)
   {
    if(hlName)
     strN = highlight(hlighter, "Contacts");
    else
     strN = "Contacts";

    for(AgeRelation pRel : persRels)
     sgRep.attachObjects("Contacts", createAttributedObject(pRel.getTargetObject(), hlighter, hlName, hlValue));
   }
  }
  
  return sgRep;
 }

 private AttributedImprint createAttributedObject(AgeObject ageObj, Highlighter hlighter, boolean hlName, boolean hlValue )
 {
  String strN;
  String strV;
  
  AttributedImprint obj = new AttributedImprint();
  
  if( ageObj.getAttributes() != null )
  {
   for( AgeAttribute attr : ageObj.getAttributes() )
   {
    strN = StringUtils.htmlEscaped( attr.getAgeElClass().getName() );
    
    if( hlName )  
     strN = highlight(hlighter, strN);

    strV = attr.getValue()!=null? StringUtils.htmlEscaped(attr.getValue().toString()):null;
    
    if( hlValue )  
     strV = highlight( hlighter, strV );

    obj.addAttribute(strN, strV, attr.getAgeElClass().isCustom(), attr.getOrder());
   }
  }
  
  return obj;
 }
 


 @Override
 public void shutdown()
 {
  storage.shutdown();
 }
 
 class TagsExtractor implements TextValueExtractor
 {
  private final PermissionManager permMngr = Configuration.getDefaultConfiguration().getPermissionManager();

  @Override
  public String getValue(AgeObject ao)
  {
   
   Collection<TagRef> tags = permMngr.getEffectiveTags( ao );
   
   if( tags == null )
    return "";
   
   StringBuilder sb = new StringBuilder();
   
   for( TagRef tr : tags )
    sb.append(tr.getClassiferName().length()).append(tr.getClassiferName()).append(tr.getTagName()).append(" ");
    
   String val = sb.toString(); 
   
   return val;
  }
  
 }
 
 class OwnerExtractor implements TextValueExtractor
 {
  private final AnnotationManager annorMngr = Configuration.getDefaultConfiguration().getAnnotationManager();

  @Override
  public String getValue(AgeObject ao)
  {
   Entity entId = ao;
   
   String own = null;
   
   while( entId != null )
   {
    try
    {
     own = (String)annorMngr.getAnnotation(Topic.OWNER, entId, true);
    }
    catch(AnnotationDBException e)
    {
     e.printStackTrace();
     return "";
    }
    
    if( own != null )
     break;
    
    entId = entId.getParentEntity();
   }
   
   if( own == null )
   {
    own = Configuration.getDefaultConfiguration().getSessionManager().getEffectiveUser();
    
    if( own == null )
     own = "";
   }

   return own;
  }
  
 }
 
 
 
 
 abstract static class AttrExtractor implements TextValueExtractor
 {
  public abstract void set(AgeAttribute attr, Set<String> tokSet);
  
  private void collectTokens(AgeObject gobj, Set<AgeObject> objSet, Set<String> tokSet )
  {
   objSet.add(gobj);
   
   if( gobj.getAttributes() != null  )
   {
    for( AgeAttribute attr : gobj.getAttributes() )
    {
     if( attr.getAgeElClass().getDataType() == DataType.OBJECT )
     {
      AgeObject obj = (AgeObject)attr.getValue();
      
      if( obj != null && ! objSet.contains(obj) && obj.getDataModule() == gobj.getDataModule() )
       collectTokens((AgeObject)attr.getValue(), objSet, tokSet);
     }

     set(attr, tokSet);
     
    }
   }
   
   if( gobj.getRelations() != null )
   {
    for( AgeRelation rl : gobj.getRelations() )
    {
     AgeObject robj = rl.getTargetObject();
     
     if( robj != null && ! objSet.contains(robj) && robj.getDataModule() == gobj.getDataModule() )
      collectTokens(robj, objSet, tokSet);
    }
    
   }
   
  }
  
  
  @Override
  public String getValue(AgeObject gobj)
  {
   StringBuilder sb = new StringBuilder();
   Set<String> tokSet = new HashSet<String>();
   Set<AgeObject> objSet = new HashSet<AgeObject>();

   collectTokens(gobj,objSet,tokSet);
   
   for( String tk : tokSet )
    sb.append( tk ).append(' ');

   String res = sb.toString();
  
   return res;
  }
 }
 
 static class AttrNamesExtractor extends AttrExtractor
 {
  @Override
  public void set(AgeAttribute attr, Set<String> tokSet)
  {
   tokSet.add( attr.getAgeElClass().getName() );
  }
 }
 
 static class AttrValuesExtractor extends AttrExtractor
 {
  @Override
  public void set(AgeAttribute attr, Set<String> tokSet)
  {
   Object val = attr.getValue();
   
   if( attr.getAgeElClass().getDataType().isTextual() )
   {
    String txt = val.toString();

    if( txt == null )
     return;
    
    for( String str : StringUtils.splitString(txt, ' ') )
    {
     str = str.trim();
     
     if( str.length() > 0 )
      tokSet.add(str);
    }
   }
  }
 }

 

 
 @Override
 public ObjectImprint getObjectImprint( ObjectId id ) throws MaintenanceModeException, NotAuthorizedException
 {
  if( maintenanceMode )
   throw new MaintenanceModeException();
  
  
  String user = Configuration.getDefaultConfiguration().getSessionManager().getEffectiveUser();
  
  try
  {
   storage.lockRead();

   AgeObject obj = storage.getObject( id.getClusterId(), id.getModuleId(), id.getObjectId() );
   
   if( obj == null )
    return null;
   
   if( ! BuiltInUsers.SUPERVISOR.getName().equals(user) )
   {
    if( Configuration.getDefaultConfiguration().getPermissionManager().checkPermission(SystemAction.READ, user, obj) != Permit.ALLOW  )
     throw new NotAuthorizedException();
   }
   
   ImprintBuilder ibld = new ImprintBuilder( htmlEscProc, htmlEscProc, null, null);
   
   return ibld.convert(obj, objConvHint);
  }
  finally
  {
   storage.unlockRead();
  }
 }
 

 
 private void highlightAttributedImprint( Highlighter hlite, uk.ac.ebi.age.ui.shared.imprint.AttributedImprint obj )
 {
  if( obj.getAttributes() == null )
   return;
  
  for( AttributeImprint atImp : obj.getAttributes() )
  {
   if( atImp.getClassImprint().getType() == uk.ac.ebi.age.ui.shared.imprint.ClassType.ATTR_STRING)
   {
    for( Value v : atImp.getValues() )
    {
     ((uk.ac.ebi.age.ui.shared.imprint.StringValue)v).setValue( highlight(hlite, v.getStringValue() ) );
     
     highlightAttributedImprint( hlite, v );
    }
   }
   else if( atImp.getClassImprint().getType() == uk.ac.ebi.age.ui.shared.imprint.ClassType.ATTR_OBJECT )
   {
    for( Value v : atImp.getValues() )
    {
     if( ((ObjectValue)v).getObjectImprint() != null )
      highlightAttributedImprint( hlite, ((ObjectValue)v).getObjectImprint() );
     
     highlightAttributedImprint( hlite, v );
    }
   }
  }
 }

 
 private void exportAttributed( Attributed ao,PrintWriter out, Set<AgeAttributeClass> atset )
 {
  for( AgeAttributeClass aac : ao.getAttributeClasses() )
  {
   if( atset != null )
    atset.add(aac);
   
   out.print("<attribute class=\"");
   out.print(StringUtils.xmlEscaped(aac.getName()));
   out.println("\" classDefined=\""+(aac.isCustom()?"false":"true")+"\" dataType=\""+aac.getDataType().name()+"\">");

   for( AgeAttribute attr : ao.getAttributesByClass(aac, false) )
   {
    if( aac.getDataType() == DataType.OBJECT )
     out.print("<objectValue>");
    else
     out.print("<simpleValue>");
    
    exportAttributed( attr, out, null );

    if( aac.getDataType() != DataType.OBJECT )
    {
     out.print("<value>");
     out.print(StringUtils.xmlEscaped(attr.getValue().toString()));
     out.print("</value>");
    }
    else
    {
     AgeObject tgtObj = (AgeObject)attr.getValue();
     
     out.print("<object id=\""+StringUtils.xmlEscaped(tgtObj.getId())+"\" class=\"");
     out.print(StringUtils.xmlEscaped(tgtObj.getAgeElClass().getName()));
     out.println("\" classDefined=\""+(tgtObj.getAgeElClass().isCustom()?"false":"true")+"\">");
 
     exportAttributed( tgtObj, out, null );
    
     out.println("</object>");
    }

    if( aac.getDataType() == DataType.OBJECT )
     out.println("</objectValue>");
    else
     out.println("</simpleValue>");
   }

   out.println("</attribute>");
  }
 }
 

 @Override
 public AgeViewStat getStatistics()
 {
  return statistics;
 }

 @Override
 public void securityChanged()
 {
  synchronized(userCache)
  {
   userCache.clear();
  }
 }


 @Override
 public AgeObject getRootObject(String groupId) throws MaintenanceModeException
 {
  if( maintenanceMode )
   throw new MaintenanceModeException();

  if( rootClass == null )
   return null;
  
  try
  {
   storage.lockRead();
   
   AgeObject smpObj = storage.getGlobalObject(groupId);
   
   if( smpObj == null || ! smpObj.getAgeElClass().isClassOrSubclass(rootClass) )
    return null;
   
   return smpObj;
  }
  finally
  {
   storage.unlockRead();
  }
 }

}
