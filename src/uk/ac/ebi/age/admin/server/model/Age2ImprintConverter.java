package uk.ac.ebi.age.admin.server.model;

import java.util.HashMap;
import java.util.Map;

import uk.ac.ebi.age.admin.client.model.AgeAnnotationClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeAnnotationImprint;
import uk.ac.ebi.age.admin.client.model.AgeAttributeClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeRelationClassImprint;
import uk.ac.ebi.age.admin.client.model.Annotated;
import uk.ac.ebi.age.admin.client.model.AttributeType;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.model.RelationRuleImprint;
import uk.ac.ebi.age.model.AgeAbstractClass;
import uk.ac.ebi.age.model.AgeAnnotation;
import uk.ac.ebi.age.model.AgeAnnotationClass;
import uk.ac.ebi.age.model.AgeAttributeClass;
import uk.ac.ebi.age.model.AgeClass;
import uk.ac.ebi.age.model.AgeRelationClass;
import uk.ac.ebi.age.model.DataType;
import uk.ac.ebi.age.model.RelationRule;
import uk.ac.ebi.age.model.SemanticModel;

public class Age2ImprintConverter
{
 private static class State
 {
  Map<AgeAbstractClass, Annotated> clMap = new HashMap<AgeAbstractClass, Annotated>();
  ModelImprint mimp;
 }
 
 public static ModelImprint getModelImprint( SemanticModel sm )
 {
  final State state = new State();
  
  state.mimp = new ModelImprint();

//  Map<AgeAbstractClass, Object> clMap = new HashMap<AgeAbstractClass, Object>();

  
  AgeAnnotationClass ageAnntRoot = sm.getRootAgeAnnotationClass();

  AgeAnnotationClassImprint aImp =  state.mimp.getRootAnnotationClass();
  aImp.setName("AgeAnnotation");
  aImp.setAbstract(true);

  state.clMap.put(ageAnntRoot, aImp);

  convertAgeToImprint(ageAnntRoot, aImp, state, new Creator<AgeAnnotationClassImprint>()
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

  AgeClassImprint rImp =  state.mimp.getRootClass();
  rImp.setName("AgeClass");
  rImp.setAbstract(true);

  state.clMap.put(ageRoot, rImp);

  convertAgeToImprint(ageRoot, rImp, state, new Creator<AgeClassImprint>()
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

    transferRestrictions((AgeClass) acls, cImp, state);
    
    return cImp;
   }


  });

  AgeAttributeClass attrRoot = sm.getRootAgeAttributeClass();

  AgeAttributeClassImprint atImp = state.mimp.getRootAttributeClass();

  atImp.setName("AgeAttribute");
  atImp.setAbstract(true);

  state.clMap.put(attrRoot, atImp);

  convertAgeToImprint(attrRoot, atImp, state, new Creator<AgeAttributeClassImprint>()
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

  AgeRelationClassImprint relImp = state.mimp.getRootRelationClass();

  relImp.setName("AgeRelation");
  relImp.setAbstract(true);

  state.clMap.put(relRoot, relImp);

  convertAgeToImprint(relRoot, relImp, state, new Creator<AgeRelationClassImprint>()
  {

   @Override
   public void addSubclass(AgeRelationClassImprint bclass, AgeRelationClassImprint derClass)
   {
    derClass.addSuperClass(bclass);
   }

   @Override
   public AgeRelationClassImprint create(AgeAbstractClass acls, AgeRelationClassImprint parent)
   {
    AgeRelationClassImprint cImp = parent.createSubClass();

    cImp.setName(acls.getName());
    cImp.setId(acls.getId());
    cImp.setAbstract(acls.isAbstract());

    return cImp;
   }
  });

  for(Map.Entry<AgeAbstractClass, Annotated> me : state.clMap.entrySet() )
  {
   if( me.getKey().getAnnotations() != null )
   {
    for( AgeAnnotation aannt : me.getKey().getAnnotations() )
    {
     AgeAnnotationImprint aimp = state.mimp.createAgeAnnotationImprint((AgeAnnotationClassImprint)state.clMap.get(aannt.getAgeElClass()));
     me.getValue().addAnnotation(aimp);
    }
   }
  }
  
  if( sm.getAnnotations() != null )
  {
   for( AgeAnnotation aannt : sm.getAnnotations() )
   {
    AgeAnnotationImprint aimp = state.mimp.createAgeAnnotationImprint((AgeAnnotationClassImprint)state.clMap.get(aannt.getAgeElClass()));
    state.mimp.addAnnotation(aimp);
   }
  }
  
//  convertObjectRestrictions(sm.getRootAgeClass(), state);
//  convertAttributeRestrictions(sm.getRootAgeClass(), state);
//  convertAttributeRestrictions(sm.getRootAgeAttributeClass(), state);
//  convertAttributeRestrictions(sm.getRootAgeRelationClass(), state);

  return state.mimp;
 }

 
 private static void transferRestrictions(AgeClass acls, AgeClassImprint cImp, State state)
 {
  if( acls.getRelationRules() != null )
  {
   for( RelationRule rr : acls.getRelationRules() )
   {
    RelationRuleImprint rrimp = state.mimp.createRelationRuleImprint(rr.getRestrictionType());
    
    rrimp.setCardinality( rr.getCardinality() );
    rrimp.setCardinalityType( rr.getCardinalityType() );
    rrimp.setQualifiersUnique( rr.isQualifiersUnique() );
    rrimp.setRelationSubclassesIncluded( rr.isRelationSubclassesIncluded() );
    rrimp.setType( rr.getType() );
    rrimp.setSubclassesIncluded(rr.isSubclassesIncluded());
    rrimp.setQualifiersCondition( rr.getQualifiersCondition() );
    rrimp.setRelationClass((AgeRelationClassImprint)state.clMap.get(rr.getRelationClass()));
    rrimp.setTargetClass((AgeClassImprint)state.clMap.get(rr.getTargetClass()));
    
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
 
 private interface Creator<ImpC>
 {
  ImpC create( AgeAbstractClass mCls, ImpC parent );
  void addSubclass(ImpC bclass, ImpC derClass);
 }
 
 private static <ImpC extends Annotated> void convertAgeToImprint(AgeAbstractClass acls, ImpC parent, State state, Creator<ImpC> cr)
 {
//  ImpC cImp = cr.create(acls.getId(),acls.getName());
//  clMap.put(acls,cImp);

  if( acls.getSubClasses() != null )
  {
   for( AgeAbstractClass scls : acls.getSubClasses() )
   {
    ImpC subImp = (ImpC)state.clMap.get(scls);
    
    if( subImp == null )
    {
     subImp = cr.create(scls, parent);
     
     state.clMap.put(scls, subImp);
     convertAgeToImprint(scls, subImp, state, cr);
    }
    else
     cr.addSubclass(parent, subImp);
    
   }
  }
 }

}
