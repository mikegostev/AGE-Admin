package uk.ac.ebi.age.admin.server.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import uk.ac.ebi.age.admin.client.model.AgeAnnotationClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeAnnotationImprint;
import uk.ac.ebi.age.admin.client.model.AgeAttributeClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeRelationClassImprint;
import uk.ac.ebi.age.admin.client.model.Annotated;
import uk.ac.ebi.age.admin.client.model.AttributeRuleImprint;
import uk.ac.ebi.age.admin.client.model.AttributeType;
import uk.ac.ebi.age.admin.client.model.AttributedImprintClass;
import uk.ac.ebi.age.admin.client.model.Cardinality;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.model.QualifierRuleImprint;
import uk.ac.ebi.age.admin.client.model.RelationRuleImprint;
import uk.ac.ebi.age.admin.client.model.RestrictionType;
import uk.ac.ebi.age.model.AgeAbstractClass;
import uk.ac.ebi.age.model.AgeAnnotation;
import uk.ac.ebi.age.model.AgeAnnotationClass;
import uk.ac.ebi.age.model.AgeAttributeClass;
import uk.ac.ebi.age.model.AgeClass;
import uk.ac.ebi.age.model.AgeRelationClass;
import uk.ac.ebi.age.model.AttributeAttachmentRule;
import uk.ac.ebi.age.model.AttributedClass;
import uk.ac.ebi.age.model.DataType;
import uk.ac.ebi.age.model.QualifierRule;
import uk.ac.ebi.age.model.RelationRule;
import uk.ac.ebi.age.model.SemanticModel;
import uk.ac.ebi.age.model.writable.AgeAbstractClassWritable;
import uk.ac.ebi.age.model.writable.AgeAnnotationClassWritable;
import uk.ac.ebi.age.model.writable.AgeAnnotationWritable;
import uk.ac.ebi.age.model.writable.AgeAttributeClassWritable;
import uk.ac.ebi.age.model.writable.AgeClassWritable;
import uk.ac.ebi.age.model.writable.AgeRelationClassWritable;
import uk.ac.ebi.age.model.writable.AttributeAttachmentRuleWritable;
import uk.ac.ebi.age.model.writable.AttributedClassWritable;
import uk.ac.ebi.age.model.writable.QualifierRuleWritable;
import uk.ac.ebi.age.model.writable.RelationRuleWritable;
import uk.ac.ebi.age.service.id.IdGenerator;

public class Age2ImprintConverter
{
 private static class StateA2I
 {
  Map<AgeAbstractClass, Annotated> classMap = new HashMap<AgeAbstractClass, Annotated>();
  ModelImprint modelImprint;
 }
 
 private static class StateI2A
 {
  Map<Annotated, AgeAbstractClassWritable> classMap = new HashMap< Annotated, AgeAbstractClassWritable >();
  SemanticModel model;
 }

 public static SemanticModel convertImprintToModel( ModelImprint mimp, SemanticModel semanticModel )
 {
  
  final StateI2A state = new StateI2A();
  
  state.model = semanticModel;
  
  state.model.setIdGen(mimp.getIdGen());
  
  AgeAnnotationClassImprint anCImp = mimp.getRootAnnotationClass();
  
  AgeAnnotationClassWritable aACls =  (AgeAnnotationClassWritable)state.model.getRootAgeAnnotationClass();
  
  state.classMap.put(anCImp, aACls);

  convertImprintToAge(anCImp, aACls, state, new AgeCreator<AgeAnnotationClassImprint,AgeAnnotationClassWritable>() {

   @Override
   public AgeAnnotationClassWritable create(AgeAnnotationClassImprint parent, AgeAnnotationClassWritable mCls)
   {
    AgeAnnotationClassWritable aac = mCls.getSemanticModel().createAgeAnnotationClass( parent.getName(), parent.getAliases(), parent.getId(), mCls );
    
    aac.setAbstract( parent.isAbstract() );
    
    return aac;
   }

   @Override
   public Collection<AgeAnnotationClassImprint> getSubclasses(AgeAnnotationClassImprint parent)
   {
    return parent.getChildren();
   }

   @Override
   public void addSubClass(AgeAnnotationClassWritable prnt, AgeAnnotationClassWritable chld)
   {
    prnt.addSubClass(chld);
    
   }} );
  
  
  {
   AgeClassImprint rootClsImp = mimp.getRootClass();

   AgeClassWritable aCls = (AgeClassWritable)state.model.getRootAgeClass();
   aCls.setAbstract(true);
 
   state.classMap.put(rootClsImp, aCls);

   convertImprintToAge(rootClsImp, aCls, state, new AgeCreator<AgeClassImprint, AgeClassWritable>()
   {

    @Override
    public AgeClassWritable create(AgeClassImprint parent, AgeClassWritable mCls)
    {
     AgeClassWritable aac = mCls.getSemanticModel().createAgeClass(parent.getName(), parent.getAliases(), parent.getId(), parent.getPrefix(),mCls);

     aac.setAbstract(parent.isAbstract());

     return aac;
    }

    @Override
    public Collection<AgeClassImprint> getSubclasses(AgeClassImprint parent)
    {
     return parent.getChildren();
    }

    @Override
    public void addSubClass(AgeClassWritable prnt, AgeClassWritable chld)
    {
     prnt.addSubClass(chld);

    }
   });

  }
  
  AgeAttributeClassImprint rootAttrImp = mimp.getRootAttributeClass();

  AgeAttributeClassWritable atrCls =  (AgeAttributeClassWritable)state.model.getRootAgeAttributeClass();
  atrCls.setAbstract(true);
  
  state.classMap.put(rootAttrImp, atrCls);

  convertImprintToAge(rootAttrImp, atrCls, state, new AgeCreator<AgeAttributeClassImprint,AgeAttributeClassWritable>() {

   @Override
   public AgeAttributeClassWritable create(AgeAttributeClassImprint parent, AgeAttributeClassWritable mCls)
   {
    DataType typ = null;
    
    switch( parent.getType() )
    {
     case BOOLEAN:
      typ = DataType.BOOLEAN;
      break;
     
     case INTEGER:
      typ = DataType.INTEGER;
      break;
     
     case REAL:
      typ = DataType.REAL;
      break;
     
     case STRING:
      typ = DataType.STRING;
      break;
     
     case TEXT:
      typ = DataType.TEXT;
      break;
     
     case URI:
      typ = DataType.URI;
      break;
      
     case OBJECT:
      typ = DataType.OBJECT;
      break;

   }
    
    AgeAttributeClassWritable aac = mCls.getSemanticModel().createAgeAttributeClass( parent.getName(), parent.getAliases(), parent.getId(), typ, mCls );
    
    aac.setAbstract( parent.isAbstract() );
    
    if( parent.getType() == AttributeType.OBJECT && parent.getTargetClass() != null )
    {
     AgeAbstractClass tc = state.classMap.get(parent.getTargetClass());
     
     if( tc != null && tc instanceof AgeClass)
      aac.setTargetClass( (AgeClass) tc );
    }
    
    return aac;
   }

   @Override
   public Collection<AgeAttributeClassImprint> getSubclasses(AgeAttributeClassImprint parent)
   {
    return parent.getChildren();
   }

   @Override
   public void addSubClass(AgeAttributeClassWritable prnt, AgeAttributeClassWritable chld)
   {
    prnt.addSubClass(chld);
    
   }} );

  {
   AgeRelationClassImprint rootRlClsImp = mimp.getRootRelationClass();

   AgeRelationClassWritable aCls = (AgeRelationClassWritable)state.model.getRootAgeRelationClass();
   aCls.setAbstract(true);
 
   state.classMap.put(rootRlClsImp, aCls);

   convertImprintToAge(rootRlClsImp, aCls, state, new AgeCreator<AgeRelationClassImprint, AgeRelationClassWritable>()
   {

    @Override
    public AgeRelationClassWritable create(AgeRelationClassImprint parent, AgeRelationClassWritable mCls)
    {
     AgeRelationClassWritable aac = mCls.getSemanticModel().createAgeRelationClass(parent.getName(), parent.getAliases(), parent.getId(), mCls);

     aac.setAbstract(parent.isAbstract());


     mCls.setFunctional(parent.isFunctional());
     mCls.setInverseFunctional(parent.isInverseFunctional());
     mCls.setSymmetric(parent.isSymmetric());
     mCls.setTransitive(parent.isTransitive());
     
     if( parent.getDomain() != null )
     {
      for( AgeClassImprint dc : parent.getDomain() )
      {
       AgeAbstractClass tc = state.classMap.get(dc);
       
       if( tc != null && tc instanceof AgeClass)
        aac.addDomainClass( (AgeClass) tc );
      }
     }
     
     if( parent.getRange() != null )
     {
      for( AgeClassImprint rc : parent.getRange() )
      {
       AgeAbstractClass tc = state.classMap.get(rc);
       
       if( tc != null && tc instanceof AgeClass)
        aac.addRangeClass( (AgeClass) tc );
      }
     }
    
     return aac;
    }

    @Override
    public Collection<AgeRelationClassImprint> getSubclasses(AgeRelationClassImprint parent)
    {
     return parent.getChildren();
    }

    @Override
    public void addSubClass(AgeRelationClassWritable prnt, AgeRelationClassWritable chld)
    {
     prnt.addSubClass(chld);

    }
   });

  }
  
  for(Map.Entry<Annotated, AgeAbstractClassWritable> me : state.classMap.entrySet() )
  {
   if( me.getKey().getAnnotations() != null )
   {
    for( AgeAnnotationImprint aaImp : me.getKey().getAnnotations() )
    {
     AgeAnnotationWritable ant = state.model.createAgeAnnotation((AgeAnnotationClass)state.classMap.get(aaImp.getAnnotationClass()));
     
     ant.setText(aaImp.getText());
     me.getValue().addAnnotation(ant);
    }
   }
   
   if( me.getKey() instanceof AttributedImprintClass )
    transferAttributeRulesI2M((AttributedImprintClass) me.getKey(), (AttributedClassWritable)me.getValue(), state);
   
   if( me.getKey() instanceof AgeClassImprint )
    transferRelationRulesI2M((AgeClassImprint) me.getKey(), (AgeClassWritable)me.getValue(), state);
   else if( me.getKey() instanceof AgeRelationClassImprint )
   {
    AgeRelationClassImprint arc  = (AgeRelationClassImprint)me.getKey();
    
    if( arc.getInverseRelation() != null )
     ((AgeRelationClassWritable)me.getValue()).setInverseRelationClass((AgeRelationClass)state.classMap.get(arc.getInverseRelation()));
    else
    {
     AgeRelationClassWritable dirRelCls = (AgeRelationClassWritable)me.getValue();
     AgeRelationClassWritable invRelCls = state.model.createAgeRelationClass( "!"+arc.getName(),
       "InvImpRelClass-"+IdGenerator.getInstance().getStringId("classId"), null );
     
     invRelCls.setImplicit(true);
     
     dirRelCls.setInverseRelationClass( invRelCls );
     invRelCls.setInverseRelationClass(dirRelCls);
    }
   }
   

  }
  
  if( mimp.getAnnotations() != null )
  {
   for( AgeAnnotationImprint aaImp : mimp.getAnnotations() )
   {
    AgeAnnotationWritable ant = state.model.createAgeAnnotation((AgeAnnotationClass)state.classMap.get(aaImp.getAnnotationClass()));
    
    ant.setText(aaImp.getText());
    
    state.model.addAnnotation(ant);
   }
  }
  
  return state.model;
 }
 
 private static void transferAttributeRulesI2M(AttributedImprintClass impr, AttributedClassWritable cls, StateI2A state)
 {
  if(impr.getAttributeRules() != null)
  {
   for(AttributeRuleImprint atrl : impr.getAttributeRules())
   {
    AttributeAttachmentRuleWritable atatRule = state.model.createAttributeAttachmentRule( convertRestrictionTypeI2M( atrl.getType() ) );

    atatRule.setRuleId( atrl.getId() );
    atatRule.setAttributeClass((AgeAttributeClass) state.classMap.get(atrl.getAttributeClass()));
    atatRule.setCardinality(atrl.getCardinality());
    atatRule.setCardinalityType( convertCardinalityI2M( atrl.getCardinalityType() ) );
    atatRule.setSubclassesIncluded(atrl.isSubclassesIncluded());
    atatRule.setSubclassesCountedSeparately(atrl.isSubclassesCountedSeparately() );
    atatRule.setType( convertRestrictionTypeI2M( atrl.getType() ) );
    atatRule.setValueUnique(atrl.isValueUnique());

    if(atrl.getQualifiers() != null)
    {
     for(QualifierRuleImprint crimp : atrl.getQualifiers())
     {
      QualifierRuleWritable qrul = state.model.createQualifierRule();

      qrul.setRuleId(crimp.getId());
      qrul.setUnique(crimp.isUnique());
      qrul.setAttributeClass((AgeAttributeClass) state.classMap.get(crimp.getAttributeClassImprint()));

      atatRule.addQualifier(qrul);
     }
    }
   
    cls.addAttributeAttachmentRule(atatRule);
   }
  }
 }

 
 private static void transferRelationRulesI2M(AgeClassImprint impr, AgeClassWritable cls, StateI2A state)
 {
  if(impr.getRelationRules() != null)
  {
   for(RelationRuleImprint rri : impr.getRelationRules())
   {
    RelationRuleWritable mrr = state.model.createRelationRule( convertRestrictionTypeI2M(rri.getType()) );
    
    mrr.setRuleId(rri.getId());
    mrr.setCardinality(rri.getCardinality());
    mrr.setCardinalityType(convertCardinalityI2M(rri.getCardinalityType()));
    mrr.setRelationSubclassesIncluded(rri.isRelationSubclassesIncluded());
    mrr.setType( convertRestrictionTypeI2M( rri.getType() ) );
    mrr.setSubclassesIncluded(rri.isSubclassesIncluded());
    mrr.setRelationClass((AgeRelationClass) state.classMap.get(rri.getRelationClass()));
    mrr.setTargetClass((AgeClass) state.classMap.get(rri.getTargetClass()));

    if(rri.getQualifiers() != null)
    {
     for(QualifierRuleImprint crimp : rri.getQualifiers())
     {
      QualifierRuleWritable qrul = state.model.createQualifierRule();

      qrul.setRuleId(crimp.getId());
      qrul.setUnique(crimp.isUnique());
      qrul.setAttributeClass((AgeAttributeClass) state.classMap.get(crimp.getAttributeClassImprint()));

      mrr.addQualifier(qrul);
     }
    }

    cls.addRelationRule(mrr);
   }
  }


 }

 public static ModelImprint convertModelToImprint( SemanticModel sm )
 {
  final StateA2I state = new StateA2I();
  
  state.modelImprint = new ModelImprint();

  state.modelImprint.setIdGen( sm.getIdGen() );
  
//  Map<AgeAbstractClass, Object> clMap = new HashMap<AgeAbstractClass, Object>();

  
  AgeAnnotationClass ageAnntRoot = sm.getRootAgeAnnotationClass();

  AgeAnnotationClassImprint aImp =  state.modelImprint.getRootAnnotationClass();
  aImp.setName(ageAnntRoot.getName());
  aImp.setId(ageAnntRoot.getId());
  aImp.setAbstract(true);

  state.classMap.put(ageAnntRoot, aImp);

  convertAgeToImprint(ageAnntRoot, aImp, state, new ImprintCreator<AgeAnnotationClassImprint>()
  {

   @Override
   public void addSubclass(AgeAnnotationClassImprint bclass, AgeAnnotationClassImprint derClass)
   {
    derClass.addSuperClass(bclass);
   }

   @Override
   public AgeAnnotationClassImprint create(AgeAbstractClass acls, AgeAnnotationClassImprint parent)
   {
    AgeAnnotationClassImprint cImp = parent.createSubClass();

    cImp.setName(acls.getName());
    cImp.setId(acls.getId());
    cImp.setAbstract(acls.isAbstract());
    
    if( acls.getAliases() != null )
    {
     for( String al : acls.getAliases() )
      cImp.addAlias(al);
    }

    return cImp;
   }
  });

  
  AgeClass ageRoot = sm.getRootAgeClass();

  AgeClassImprint cImp =  state.modelImprint.getRootClass();
  cImp.setName(ModelImprint.ROOT_CLASS_NAME);
  cImp.setId(ageRoot.getId());
  cImp.setAbstract(true);

  state.classMap.put(ageRoot, cImp);

  convertAgeToImprint(ageRoot, cImp, state, new ImprintCreator<AgeClassImprint>()
  {

   @Override
   public void addSubclass(AgeClassImprint bclass, AgeClassImprint derClass)
   {
    derClass.addSuperClass(bclass);
   }

   @Override
   public AgeClassImprint create(AgeAbstractClass acls, AgeClassImprint parent)
   {
    AgeClassImprint cImp = parent.createSubClass();

    cImp.setName(acls.getName());
    cImp.setId(acls.getId());
    cImp.setAbstract(acls.isAbstract());

    if( acls.getAliases() != null )
    {
     for( String al : acls.getAliases() )
      cImp.addAlias(al);
    }

    
    return cImp;
   }


  });

  AgeAttributeClass attrRoot = sm.getRootAgeAttributeClass();

  AgeAttributeClassImprint atImp = state.modelImprint.getRootAttributeClass();

  atImp.setName(ModelImprint.ROOT_ATTR_NAME);
  atImp.setId(attrRoot.getId());
  atImp.setAbstract(true);

  state.classMap.put(attrRoot, atImp);

  convertAgeToImprint(attrRoot, atImp, state, new ImprintCreator<AgeAttributeClassImprint>()
  {

   @Override
   public void addSubclass(AgeAttributeClassImprint bclass, AgeAttributeClassImprint derClass)
   {
    derClass.addSuperClass(bclass);
   }

   @Override
   public AgeAttributeClassImprint create(AgeAbstractClass acls, AgeAttributeClassImprint parent)
   {
    AgeAttributeClassImprint cImp = parent.createSubClass();

    cImp.setName(acls.getName());
    cImp.setId(acls.getId());

    DataType typ = ((AgeAttributeClass) acls).getDataType();

    if(typ != null)
    {
     switch(typ)
     {
      case BOOLEAN:
       cImp.setType(AttributeType.BOOLEAN);
       break;

      case INTEGER:
       cImp.setType(AttributeType.INTEGER);
       break;

      case REAL:
       cImp.setType(AttributeType.REAL);
       break;

      case STRING:
       cImp.setType(AttributeType.STRING);
       break;

      case URI:
       cImp.setType(AttributeType.URI);
       break;

      case TEXT:
       cImp.setType(AttributeType.TEXT);
       break;
      case OBJECT:
       cImp.setType(AttributeType.OBJECT);
       cImp.setTargetClass((AgeClassImprint) state.classMap.get(((AgeAttributeClass)acls).getTargetClass()));
       break;
      case GUESS: // Must be class of type GUESS
       break;
     }

    }
    else
     cImp.setAbstract(true);

    if( acls.getAliases() != null )
    {
     for( String al : acls.getAliases() )
      cImp.addAlias(al);
    }


    return cImp;
   }
  });

  AgeRelationClass relRoot = sm.getRootAgeRelationClass();

  AgeRelationClassImprint relImp = state.modelImprint.getRootRelationClass();

  relImp.setName(ModelImprint.ROOT_REL_NAME);
  relImp.setAbstract(true);
  
  
  state.classMap.put(relRoot, relImp);

  convertAgeToImprint(relRoot, relImp, state, new ImprintCreator<AgeRelationClassImprint>()
  {

   @Override
   public void addSubclass(AgeRelationClassImprint bclass, AgeRelationClassImprint derClass)
   {
    derClass.addSuperClass(bclass);
   }

   @Override
   public AgeRelationClassImprint create(AgeAbstractClass acls, AgeRelationClassImprint parent)
   {
    AgeRelationClass rlCls = (AgeRelationClass)acls;
    AgeRelationClassImprint cImp = parent.createSubClass();

    cImp.setName(acls.getName());
    cImp.setId(acls.getId());
    cImp.setAbstract(acls.isAbstract());
    cImp.setFunctional(rlCls.isFunctional());
    cImp.setInverseFunctional(rlCls.isInverseFunctional());
    cImp.setSymmetric(rlCls.isSymmetric());
    cImp.setTransitive(rlCls.isTransitive());

    if( acls.getAliases() != null )
    {
     for( String al : acls.getAliases() )
      cImp.addAlias(al);
    }

    return cImp;
   }
  });

  for(Map.Entry<AgeAbstractClass, Annotated> me : state.classMap.entrySet() )
  {
   if( me.getKey().getAnnotations() != null )
   {
    for( AgeAnnotation aannt : me.getKey().getAnnotations() )
    {
     AgeAnnotationImprint aimp = state.modelImprint.createAgeAnnotationImprint((AgeAnnotationClassImprint)state.classMap.get(aannt.getAgeElClass()));

     aimp.setText(aannt.getText());
     
     me.getValue().addAnnotation(aimp);
    }
   }
   
   if( me.getKey() instanceof AttributedClass )
    transferAttributeRulesM2I((AttributedClass) me.getKey(), (AttributedImprintClass)me.getValue(), state);

   
   if( me.getKey() instanceof AgeClass )
    transferRelationRulesM2I((AgeClass) me.getKey(), (AgeClassImprint)me.getValue(), state);
   else if( me.getKey() instanceof AgeRelationClass )
   {
    AgeRelationClass arc  = (AgeRelationClass)me.getKey();
    
    if( arc.getInverseRelationClass() != null )
     ((AgeRelationClassImprint)me.getValue()).setInverseRelation((AgeRelationClassImprint)state.classMap.get(arc.getInverseRelationClass()));
   }

  }
  
  if( sm.getAnnotations() != null )
  {
   for( AgeAnnotation aannt : sm.getAnnotations() )
   {
    AgeAnnotationImprint aimp = state.modelImprint.createAgeAnnotationImprint((AgeAnnotationClassImprint)state.classMap.get(aannt.getAgeElClass()));
    
    aimp.setText(aannt.getText());
    
    state.modelImprint.addAnnotation(aimp);
   }
  }
  
//  convertObjectRestrictions(sm.getRootAgeClass(), state);
//  convertAttributeRestrictions(sm.getRootAgeClass(), state);
//  convertAttributeRestrictions(sm.getRootAgeAttributeClass(), state);
//  convertAttributeRestrictions(sm.getRootAgeRelationClass(), state);

  return state.modelImprint;
 }

 private static void transferAttributeRulesM2I(AttributedClass acls, AttributedImprintClass cImp, StateA2I state)
 {
  if(acls.getAttributeAttachmentRules() != null)
  {
   for(AttributeAttachmentRule atatrl : acls.getAttributeAttachmentRules())
   {
    AttributeRuleImprint arimp = state.modelImprint.createAttributeRuleImprint(convertRestrictionTypeM2I(atatrl.getRestrictionType()));

    arimp.setId(atatrl.getRuleId());
    arimp.setAttributeClass((AgeAttributeClassImprint) state.classMap.get(atatrl.getAttributeClass()));
    arimp.setCardinality(atatrl.getCardinality());
    arimp.setCardinalityType(convertCardinalityM2I(atatrl.getCardinalityType()));
    arimp.setSubclassesIncluded(atatrl.isSubclassesIncluded());
    arimp.setSubclassesCountedSeparately(atatrl.isSubclassesCountedSeparately());
    arimp.setType(convertRestrictionTypeM2I(atatrl.getType()));
    arimp.setValueUnique(atatrl.isValueUnique());

    
    if(atatrl.getQualifiers() != null)
    {
     for(QualifierRule cr : atatrl.getQualifiers())
     {
      QualifierRuleImprint qrimp = state.modelImprint.createQualifierRuleImprint();

      qrimp.setId(cr.getRuleId());
      qrimp.setUnique(cr.isUnique());
      qrimp.setAttributeClassImprint((AgeAttributeClassImprint) state.classMap.get(cr.getAttributeClass()));
      
      arimp.addQualifier(qrimp);
     }
    }

    cImp.addAttributeRule(arimp);
   }
  }
 }
 
 private static void transferRelationRulesM2I(AgeClass acls, AgeClassImprint cImp, StateA2I state)
 {
  if( acls.getRelationRules() != null )
  {
   for( RelationRule rr : acls.getRelationRules() )
   {
    RelationRuleImprint rrimp = state.modelImprint.createRelationRuleImprint( convertRestrictionTypeM2I( rr.getRestrictionType() ) );
    
    rrimp.setId(rr.getRuleId());
    rrimp.setCardinality( rr.getCardinality() );
    rrimp.setCardinalityType( convertCardinalityM2I( rr.getCardinalityType() ) );
    rrimp.setRelationSubclassesIncluded( rr.isRelationSubclassesIncluded() );
    rrimp.setType( convertRestrictionTypeM2I( rr.getType() ) );
    rrimp.setSubclassesIncluded(rr.isSubclassesIncluded());
    rrimp.setRelationClass((AgeRelationClassImprint)state.classMap.get(rr.getRelationClass()));
    rrimp.setTargetClass((AgeClassImprint)state.classMap.get(rr.getTargetClass()));
    
    if(rr.getQualifiers() != null)
    {
     for(QualifierRule cr : rr.getQualifiers())
     {
      QualifierRuleImprint qrimp = state.modelImprint.createQualifierRuleImprint();

      qrimp.setId(cr.getRuleId());
      qrimp.setUnique(cr.isUnique());
      qrimp.setAttributeClassImprint((AgeAttributeClassImprint) state.classMap.get(cr.getAttributeClass()));
      
      rrimp.addQualifier(qrimp);
     }
    }
    
    cImp.addRelationRule(rrimp);
   }
  }

  
 }

/*
 private static void convertObjectRestrictions(AgeClass rootAgeClass, State state)
 {
  if(rootAgeClass.getSubClasses() == null)
   return;

  for(AgeClass cls : rootAgeClass.getSubClasses())
  {
   if(cls.getObjectRestrictions() == null)
    continue;

   AgeClassImprint clImp = (AgeClassImprint) state.clMap.get(cls);

//   for(AgeRestriction rstr : cls.getObjectRestrictions())
//    clImp.addRestriction(convertRestriction(rstr, clMap));
  }
 }

 private static void  convertAttributeRestrictions(AgeAbstractClass rootAgeClass, State state)
 {
  if(rootAgeClass.getSubClasses() == null)
   return;

  for(AgeAbstractClass cls : rootAgeClass.getSubClasses())
  {
   if(cls.getAttributeRestrictions() == null)
    continue;

   AgeAbstractClassImprint clImp = (AgeAbstractClassImprint) state.clMap.get(cls);

//   for(AgeRestriction rstr : cls.getAttributeRestrictions())
//    clImp.addAttributeRestriction(convertRestriction(rstr, clMap));
  }
 } 
 
 private static RestrictionImprint convertRestriction( AgeRestriction rstr, State state )
 {
//  AgeObjectRestrictionImprint rstimp = null;
  
  if( rstr instanceof AgeSomeValuesFromRestriction )
  {
   FillerRestrictionImprint ri = new FillerRestrictionImprint();
   
   ri.setType(Type.SOME);
   ri.setRelation((AgeRelationClassImprint)state.clMap.get(((AgeSomeValuesFromRestriction) rstr).getAgeRelationClass()));
   ri.setFiller( convertRestriction(((AgeSomeValuesFromRestriction) rstr).getFiller(), state));
   
   return ri;
  }
  else if ( rstr instanceof AgeAllValuesFromRestriction )
  {
   FillerRestrictionImprint ri = new FillerRestrictionImprint();
 
   AgeAllValuesFromRestriction avfResr = (AgeAllValuesFromRestriction)rstr;
   
   ri.setType(Type.ONLY);
   ri.setRelation((AgeRelationClassImprint)state.clMap.get(avfResr.getAgeRelationClass()));
   ri.setFiller( convertRestriction(avfResr.getFiller(), state));
   
   return ri;
  }
  else if ( rstr instanceof AgeAndLogicRestriction )
  {
   LogicRestrictionImprint ri = new LogicRestrictionImprint();

   ri.setType(Type.AND);
   
   for( AgeRestriction ar : ((AgeAndLogicRestriction) rstr).getOperands() )
    ri.addOperand( convertRestriction(ar, state) );
   
   return ri;
  }
  else if ( rstr instanceof AgeOrLogicRestriction )
  {
   LogicRestrictionImprint ri = new LogicRestrictionImprint();

   ri.setType(Type.OR);
   
   for( AgeRestriction ar : ((AgeOrLogicRestriction) rstr).getOperands() )
    ri.addOperand( convertRestriction(ar, state) );
   
   return ri;
  }
  else if ( rstr instanceof AgeNotLogicRestriction )
  {
   LogicRestrictionImprint ri = new LogicRestrictionImprint();

   ri.setType(Type.NOT);
   
   ri.addOperand( convertRestriction( ((AgeNotLogicRestriction) rstr).getOperand(), state) );
   
   return ri;
  }
  else if ( rstr instanceof AgeExactCardinalityRestriction )
  {
   CardinalityRestrictionImprint ri = new CardinalityRestrictionImprint();

   ri.setType(Type.EXACTLY);
   ri.setRelation((AgeRelationClassImprint)state.clMap.get(((AgeExactCardinalityRestriction) rstr).getAgeRelationClass()));
   ri.setFiller( convertRestriction(((AgeExactCardinalityRestriction) rstr).getFiller(), state));

   ri.setCardinality(((AgeExactCardinalityRestriction) rstr).getCardinality());
   
   return ri;
  }
  else if ( rstr instanceof AgeMinCardinalityRestriction )
  {
   CardinalityRestrictionImprint ri = new CardinalityRestrictionImprint();

   ri.setType(Type.MIN);
   ri.setRelation((AgeRelationClassImprint)state.clMap.get(((AgeMinCardinalityRestriction) rstr).getAgeRelationClass()));
   ri.setFiller( convertRestriction(((AgeMinCardinalityRestriction) rstr).getFiller(), state));

   ri.setCardinality(((AgeMinCardinalityRestriction) rstr).getCardinality());
   
   return ri;
  }
  else if ( rstr instanceof AgeMaxCardinalityRestriction )
  {
   CardinalityRestrictionImprint ri = new CardinalityRestrictionImprint();

   ri.setType(Type.MAX);
   ri.setRelation((AgeRelationClassImprint)state.clMap.get(((AgeMaxCardinalityRestriction) rstr).getAgeRelationClass()));
   ri.setFiller( convertRestriction(((AgeMaxCardinalityRestriction) rstr).getFiller(), state));

   ri.setCardinality(((AgeMaxCardinalityRestriction) rstr).getCardinality());
   
   return ri;
  }
  else if ( rstr instanceof AgeIsInstanceOfRestriction )
  {
   IntanceOfRestrictionImprint ri = new IntanceOfRestrictionImprint();

   ri.setType(Type.IS);
   ri.setAgeAbstractClassImprint((AgeAbstractClassImprint)state.clMap.get(((AgeIsInstanceOfRestriction) rstr).getTargetClass()));
   
   return ri;
  }

  return null;
 }
*/
 
 private interface ImprintCreator<ImpC>
 {
  ImpC create( AgeAbstractClass mCls, ImpC parent );
  void addSubclass(ImpC bclass, ImpC derClass);
 }
 
 private static <ImpC extends Annotated> void convertAgeToImprint(AgeAbstractClass acls, ImpC parent, StateA2I state, ImprintCreator<ImpC> cr)
 {
//  ImpC cImp = cr.create(acls.getId(),acls.getName());
//  clMap.put(acls,cImp);

  if( acls.getSubClasses() != null )
  {
   for( AgeAbstractClass scls : acls.getSubClasses() )
   {
    ImpC subImp = (ImpC)state.classMap.get(scls);
    
    if( subImp == null )
    {
     subImp = cr.create(scls, parent);
     
     state.classMap.put(scls, subImp);
     convertAgeToImprint(scls, subImp, state, cr);
    }
    else
     cr.addSubclass(parent, subImp);
    
   }
  }
 }

 private interface AgeCreator<ImpC, AgeC extends AgeAbstractClassWritable>
 {
  AgeC create( ImpC parent, AgeC mCls );
  Collection<ImpC> getSubclasses(ImpC parent);
  void addSubClass(AgeC prnt, AgeC chld);
 }

 
 private static <ImpC extends Annotated, AgeC extends AgeAbstractClassWritable> void convertImprintToAge(ImpC imp, AgeC parent,  StateI2A state, AgeCreator<ImpC,AgeC> cr)
 {
  if( cr.getSubclasses(imp) != null )
  {
   for( ImpC scls : cr.getSubclasses(imp) )
   {
    AgeC subCls = (AgeC)state.classMap.get(scls);
    
    if( subCls == null )
    {
     subCls = cr.create( scls, parent );
     
     state.classMap.put(scls, subCls);
     convertImprintToAge(scls, subCls, state, cr);
    }
    else
     cr.addSubClass(parent, subCls);
    
   }
  }
 }
 
 private static Cardinality convertCardinalityM2I( uk.ac.ebi.age.model.Cardinality card )
 {
  return Cardinality.valueOf(card.name());
 }
 
 private static uk.ac.ebi.age.model.Cardinality convertCardinalityI2M( Cardinality card )
 {
  return uk.ac.ebi.age.model.Cardinality.valueOf(card.name());
 }
 
 private static RestrictionType convertRestrictionTypeM2I( uk.ac.ebi.age.model.RestrictionType card )
 {
  return RestrictionType.valueOf(card.name());
 }

 private static uk.ac.ebi.age.model.RestrictionType convertRestrictionTypeI2M( RestrictionType card )
 {
  return uk.ac.ebi.age.model.RestrictionType.valueOf(card.name());
 }

// private static QualifiersCondition convertQualifiersConditionM2I( uk.ac.ebi.age.model.QualifiersCondition card )
// {
//  return QualifiersCondition.valueOf(card.name());
// }
//
// private static uk.ac.ebi.age.model.QualifiersCondition convertQualifiersConditionI2M( QualifiersCondition card )
// {
//  return uk.ac.ebi.age.model.QualifiersCondition.valueOf(card.name());
// }

}
