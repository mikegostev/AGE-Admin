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
import uk.ac.ebi.age.admin.client.model.AttributeRule;
import uk.ac.ebi.age.admin.client.model.AttributeType;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.model.QualifierRuleImprint;
import uk.ac.ebi.age.admin.client.model.RelationRuleImprint;
import uk.ac.ebi.age.mng.SemanticManager;
import uk.ac.ebi.age.model.AgeAbstractClass;
import uk.ac.ebi.age.model.AgeAnnotation;
import uk.ac.ebi.age.model.AgeAnnotationClass;
import uk.ac.ebi.age.model.AgeAttributeClass;
import uk.ac.ebi.age.model.AgeClass;
import uk.ac.ebi.age.model.AgeRelationClass;
import uk.ac.ebi.age.model.AttributeAttachmentRule;
import uk.ac.ebi.age.model.DataType;
import uk.ac.ebi.age.model.QualifierRule;
import uk.ac.ebi.age.model.RelationRule;
import uk.ac.ebi.age.model.SemanticModel;
import uk.ac.ebi.age.model.writable.AgeAnnotationClassWritable;

public class Age2ImprintConverter
{
 private static class StateA2I
 {
  Map<AgeAbstractClass, Annotated> classMap = new HashMap<AgeAbstractClass, Annotated>();
  ModelImprint modelImprint;
 }
 
 private static class StateI2A
 {
  Map<Annotated, AgeAbstractClass> classMap = new HashMap< Annotated, AgeAbstractClass >();
  SemanticModel model;
 }

 public static SemanticModel convertToModel( ModelImprint mimp )
 {
  
  final StateI2A state = new StateI2A();
  
  state.model = SemanticManager.createModelInstance();
  state.classMap = new HashMap<Annotated, AgeAbstractClass>();
  
  AgeAnnotationClassImprint anCImp = mimp.getRootAnnotationClass();
  
  AgeAnnotationClassWritable aCls =  state.model.createAgeAnnotationClass("AgeAnnotation", anCImp.getId());
  aCls.setAbstract(true);
  
  state.classMap.put(anCImp, aCls);

  convertImprintToAge(anCImp, aCls, state, new AgeCreator<AgeAnnotationClassImprint,AgeAnnotationClassWritable>() {

   @Override
   public AgeAnnotationClassWritable create(AgeAnnotationClassImprint parent, AgeAnnotationClassWritable mCls)
   {
    AgeAnnotationClassWritable aac = mCls.getSemanticModel().createAgeAnnotationClass( parent.getName(), parent.getId() );

    if( parent.getAliases() != null )
    {
     for( String ali : parent.getAliases() )
      aac.addAlias( ali );
    }
    
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
  
  return state.model;
 }
 
 public static ModelImprint convertToImprint( SemanticModel sm )
 {
  final StateA2I state = new StateA2I();
  
  state.modelImprint = new ModelImprint();

//  Map<AgeAbstractClass, Object> clMap = new HashMap<AgeAbstractClass, Object>();

  
  AgeAnnotationClass ageAnntRoot = sm.getRootAgeAnnotationClass();

  AgeAnnotationClassImprint aImp =  state.modelImprint.getRootAnnotationClass();
  aImp.setName("AgeAnnotation");
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

    return cImp;
   }
  });

  
  AgeClass ageRoot = sm.getRootAgeClass();

  AgeClassImprint rImp =  state.modelImprint.getRootClass();
  rImp.setName("AgeClass");
  rImp.setAbstract(true);

  state.classMap.put(ageRoot, rImp);

  convertAgeToImprint(ageRoot, rImp, state, new ImprintCreator<AgeClassImprint>()
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

    return cImp;
   }


  });

  AgeAttributeClass attrRoot = sm.getRootAgeAttributeClass();

  AgeAttributeClassImprint atImp = state.modelImprint.getRootAttributeClass();

  atImp.setName("AgeAttribute");
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
     }

    }
    else
     cImp.setAbstract(true);


    return cImp;
   }
  });

  AgeRelationClass relRoot = sm.getRootAgeRelationClass();

  AgeRelationClassImprint relImp = state.modelImprint.getRootRelationClass();

  relImp.setName("AgeRelation");
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
     me.getValue().addAnnotation(aimp);
    }
   }
   
   if( me.getKey() instanceof AgeClass )
    transferRestrictions((AgeClass) me.getKey(), (AgeClassImprint)me.getValue(), state);
   else if( me.getKey() instanceof AgeRelationClass )
   {
    AgeRelationClass arc  = (AgeRelationClass)me.getKey();
    
    if( arc.getInverseClass() != null )
     ((AgeRelationClassImprint)me.getValue()).setInverseRelation((AgeRelationClassImprint)state.classMap.get(arc.getInverseClass()));
   }
   

  }
  
  if( sm.getAnnotations() != null )
  {
   for( AgeAnnotation aannt : sm.getAnnotations() )
   {
    AgeAnnotationImprint aimp = state.modelImprint.createAgeAnnotationImprint((AgeAnnotationClassImprint)state.classMap.get(aannt.getAgeElClass()));
    state.modelImprint.addAnnotation(aimp);
   }
  }
  
//  convertObjectRestrictions(sm.getRootAgeClass(), state);
//  convertAttributeRestrictions(sm.getRootAgeClass(), state);
//  convertAttributeRestrictions(sm.getRootAgeAttributeClass(), state);
//  convertAttributeRestrictions(sm.getRootAgeRelationClass(), state);

  return state.modelImprint;
 }

 
 private static void transferRestrictions(AgeClass acls, AgeClassImprint cImp, StateA2I state)
 {
  if( acls.getRelationRules() != null )
  {
   for( RelationRule rr : acls.getRelationRules() )
   {
    RelationRuleImprint rrimp = state.modelImprint.createRelationRuleImprint(rr.getRestrictionType());
    
    rrimp.setCardinality( rr.getCardinality() );
    rrimp.setCardinalityType( rr.getCardinalityType() );
    rrimp.setQualifiersUnique( rr.isQualifiersUnique() );
    rrimp.setRelationSubclassesIncluded( rr.isRelationSubclassesIncluded() );
    rrimp.setType( rr.getType() );
    rrimp.setSubclassesIncluded(rr.isSubclassesIncluded());
    rrimp.setQualifiersCondition( rr.getQualifiersCondition() );
    rrimp.setRelationClass((AgeRelationClassImprint)state.classMap.get(rr.getRelationClass()));
    rrimp.setTargetClass((AgeClassImprint)state.classMap.get(rr.getTargetClass()));
    
    if( rr.getQualifiers() != null )
    {
     for( QualifierRule clrl : rr.getQualifiers() )
     {
      QualifierRuleImprint qrimp = state.modelImprint.createQualifierRuleImprint();
      
      qrimp.setType(clrl.getType());
      qrimp.setAttributeClassImprint((AgeAttributeClassImprint)state.classMap.get(clrl.getAttrbuteClass()));
      
      rrimp.addQualifier(qrimp);
     }
    }
    
    cImp.addRelationRule(rrimp);
   }
  }
  
  if( acls.getAttributeAttachmentRules() != null )
  {
   for( AttributeAttachmentRule atatrl : acls.getAttributeAttachmentRules() )
   {
    AttributeRule arimp = state.modelImprint.createAttributeRuleImprint(atatrl.getRestrictionType());
    
    arimp.setAttributeClass((AgeAttributeClassImprint)state.classMap.get(atatrl.getAttrbuteClass()));
    arimp.setCardinality( atatrl.getCardinality() );
    arimp.setCardinalityType( atatrl.getCardinalityType() );
    arimp.setQualifiersCondition( atatrl.getQualifiersCondition() );
    arimp.setQualifiersUnique( atatrl.isQualifiersUnique() );
    arimp.setSubclassesIncluded( atatrl.isSubclassesIncluded() );
    arimp.setType( atatrl.getType() );
    arimp.setValueUnique( atatrl.isValueUnique() );

    if( atatrl.getQualifiers() != null )
    {
     for( QualifierRule clrl : atatrl.getQualifiers() )
     {
      QualifierRuleImprint qrimp = state.modelImprint.createQualifierRuleImprint();
      
      qrimp.setType(clrl.getType());
      qrimp.setAttributeClassImprint((AgeAttributeClassImprint)state.classMap.get(clrl.getAttrbuteClass()));
      
      arimp.addQualifier(qrimp);
     }
    }
    cImp.addAttributeRule(arimp);
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

 private interface AgeCreator<ImpC, AgeC extends AgeAbstractClass>
 {
  AgeC create( ImpC parent, AgeC mCls );
  Collection<ImpC> getSubclasses(ImpC parent);
  void addSubClass(AgeC prnt, AgeC chld);
 }

 
 private static <ImpC extends Annotated, AgeC extends AgeAbstractClass> void convertImprintToAge(ImpC imp, AgeC parent,  StateI2A state, AgeCreator<ImpC,AgeC> cr)
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
}
