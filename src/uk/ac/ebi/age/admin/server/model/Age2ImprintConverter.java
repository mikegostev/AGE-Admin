package uk.ac.ebi.age.admin.server.model;

import java.util.HashMap;
import java.util.Map;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeAttributeClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeRelationClassImprint;
import uk.ac.ebi.age.admin.client.model.AttributeType;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.model.restriction.CardinalityRestrictionImprint;
import uk.ac.ebi.age.admin.client.model.restriction.FillerRestrictionImprint;
import uk.ac.ebi.age.admin.client.model.restriction.IntanceOfRestrictionImprint;
import uk.ac.ebi.age.admin.client.model.restriction.LogicRestrictionImprint;
import uk.ac.ebi.age.admin.client.model.restriction.RestrictionImprint;
import uk.ac.ebi.age.admin.client.model.restriction.RestrictionImprint.Type;
import uk.ac.ebi.age.model.AgeAbstractClass;
import uk.ac.ebi.age.model.AgeAllValuesFromRestriction;
import uk.ac.ebi.age.model.AgeAndLogicRestriction;
import uk.ac.ebi.age.model.AgeAttributeClass;
import uk.ac.ebi.age.model.AgeClass;
import uk.ac.ebi.age.model.AgeExactCardinalityRestriction;
import uk.ac.ebi.age.model.AgeIsInstanceOfRestriction;
import uk.ac.ebi.age.model.AgeMaxCardinalityRestriction;
import uk.ac.ebi.age.model.AgeMinCardinalityRestriction;
import uk.ac.ebi.age.model.AgeNotLogicRestriction;
import uk.ac.ebi.age.model.AgeOrLogicRestriction;
import uk.ac.ebi.age.model.AgeRelationClass;
import uk.ac.ebi.age.model.AgeRestriction;
import uk.ac.ebi.age.model.AgeSomeValuesFromRestriction;
import uk.ac.ebi.age.model.DataType;
import uk.ac.ebi.age.model.SemanticModel;

public class Age2ImprintConverter
{
 public static ModelImprint getModelImprint( SemanticModel sm )
 {

  ModelImprint mimp = new ModelImprint();

  Map<AgeAbstractClass, Object> clMap = new HashMap<AgeAbstractClass, Object>();

  AgeClass ageRoot = sm.getRootAgeClass();

  AgeClassImprint rImp = mimp.getRootClass();
  rImp.setName("AgeClass");
  rImp.setAbstract(true);

  clMap.put(ageRoot, rImp);

  convertAgeToImprint(ageRoot, rImp, clMap, new Creator<AgeClassImprint>()
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

  AgeAttributeClassImprint atImp = mimp.getRootAttributeClass();

  atImp.setName("AgeAttribute");
  atImp.setAbstract(true);

  clMap.put(attrRoot, atImp);

  convertAgeToImprint(attrRoot, atImp, clMap, new Creator<AgeAttributeClassImprint>()
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

  AgeRelationClassImprint relImp = mimp.getRootRelationClass();

  relImp.setName("AgeRelation");
  relImp.setAbstract(true);

  clMap.put(relRoot, relImp);

  convertAgeToImprint(relRoot, relImp, clMap, new Creator<AgeRelationClassImprint>()
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

  convertObjectRestrictions(sm.getRootAgeClass(), clMap);
  convertAttributeRestrictions(sm.getRootAgeClass(), clMap);
  convertAttributeRestrictions(sm.getRootAgeAttributeClass(), clMap);
  convertAttributeRestrictions(sm.getRootAgeRelationClass(), clMap);

  return mimp;
 }

 
 private static void convertObjectRestrictions(AgeClass rootAgeClass, Map<AgeAbstractClass, Object> clMap)
 {
  if(rootAgeClass.getSubClasses() == null)
   return;

  for(AgeClass cls : rootAgeClass.getSubClasses())
  {
   if(cls.getObjectRestrictions() == null)
    continue;

   AgeClassImprint clImp = (AgeClassImprint) clMap.get(cls);

   for(AgeRestriction rstr : cls.getObjectRestrictions())
    clImp.addRestriction(convertRestriction(rstr, clMap));
  }
 }

 private static void  convertAttributeRestrictions(AgeAbstractClass rootAgeClass, Map<AgeAbstractClass, Object> clMap)
 {
  if(rootAgeClass.getSubClasses() == null)
   return;

  for(AgeAbstractClass cls : rootAgeClass.getSubClasses())
  {
   if(cls.getAttributeRestrictions() == null)
    continue;

   AgeAbstractClassImprint clImp = (AgeAbstractClassImprint) clMap.get(cls);

   for(AgeRestriction rstr : cls.getAttributeRestrictions())
    clImp.addAttributeRestriction(convertRestriction(rstr, clMap));
  }
 } 
 
 private static RestrictionImprint convertRestriction( AgeRestriction rstr, Map<AgeAbstractClass, Object> clMap )
 {
//  AgeObjectRestrictionImprint rstimp = null;
  
  if( rstr instanceof AgeSomeValuesFromRestriction )
  {
   FillerRestrictionImprint ri = new FillerRestrictionImprint();
   
   ri.setType(Type.SOME);
   ri.setRelation((AgeRelationClassImprint)clMap.get(((AgeSomeValuesFromRestriction) rstr).getAgeRelationClass()));
   ri.setFiller( convertRestriction(((AgeSomeValuesFromRestriction) rstr).getFiller(), clMap));
   
   return ri;
  }
  else if ( rstr instanceof AgeAllValuesFromRestriction )
  {
   FillerRestrictionImprint ri = new FillerRestrictionImprint();
 
   AgeAllValuesFromRestriction avfResr = (AgeAllValuesFromRestriction)rstr;
   
   ri.setType(Type.ONLY);
   ri.setRelation((AgeRelationClassImprint)clMap.get(avfResr.getAgeRelationClass()));
   ri.setFiller( convertRestriction(avfResr.getFiller(), clMap));
   
   return ri;
  }
  else if ( rstr instanceof AgeAndLogicRestriction )
  {
   LogicRestrictionImprint ri = new LogicRestrictionImprint();

   ri.setType(Type.AND);
   
   for( AgeRestriction ar : ((AgeAndLogicRestriction) rstr).getOperands() )
    ri.addOperand( convertRestriction(ar, clMap) );
   
   return ri;
  }
  else if ( rstr instanceof AgeOrLogicRestriction )
  {
   LogicRestrictionImprint ri = new LogicRestrictionImprint();

   ri.setType(Type.OR);
   
   for( AgeRestriction ar : ((AgeOrLogicRestriction) rstr).getOperands() )
    ri.addOperand( convertRestriction(ar, clMap) );
   
   return ri;
  }
  else if ( rstr instanceof AgeNotLogicRestriction )
  {
   LogicRestrictionImprint ri = new LogicRestrictionImprint();

   ri.setType(Type.NOT);
   
   ri.addOperand( convertRestriction( ((AgeNotLogicRestriction) rstr).getOperand(), clMap) );
   
   return ri;
  }
  else if ( rstr instanceof AgeExactCardinalityRestriction )
  {
   CardinalityRestrictionImprint ri = new CardinalityRestrictionImprint();

   ri.setType(Type.EXACTLY);
   ri.setRelation((AgeRelationClassImprint)clMap.get(((AgeExactCardinalityRestriction) rstr).getAgeRelationClass()));
   ri.setFiller( convertRestriction(((AgeExactCardinalityRestriction) rstr).getFiller(), clMap));

   ri.setCardinality(((AgeExactCardinalityRestriction) rstr).getCardinality());
   
   return ri;
  }
  else if ( rstr instanceof AgeMinCardinalityRestriction )
  {
   CardinalityRestrictionImprint ri = new CardinalityRestrictionImprint();

   ri.setType(Type.MIN);
   ri.setRelation((AgeRelationClassImprint)clMap.get(((AgeMinCardinalityRestriction) rstr).getAgeRelationClass()));
   ri.setFiller( convertRestriction(((AgeMinCardinalityRestriction) rstr).getFiller(), clMap));

   ri.setCardinality(((AgeMinCardinalityRestriction) rstr).getCardinality());
   
   return ri;
  }
  else if ( rstr instanceof AgeMaxCardinalityRestriction )
  {
   CardinalityRestrictionImprint ri = new CardinalityRestrictionImprint();

   ri.setType(Type.MAX);
   ri.setRelation((AgeRelationClassImprint)clMap.get(((AgeMaxCardinalityRestriction) rstr).getAgeRelationClass()));
   ri.setFiller( convertRestriction(((AgeMaxCardinalityRestriction) rstr).getFiller(), clMap));

   ri.setCardinality(((AgeMaxCardinalityRestriction) rstr).getCardinality());
   
   return ri;
  }
  else if ( rstr instanceof AgeIsInstanceOfRestriction )
  {
   IntanceOfRestrictionImprint ri = new IntanceOfRestrictionImprint();

   ri.setType(Type.IS);
   ri.setAgeAbstractClassImprint((AgeAbstractClassImprint)clMap.get(((AgeIsInstanceOfRestriction) rstr).getTargetClass()));
   
   return ri;
  }

  return null;
 }

 private interface Creator<ImpC>
 {
  ImpC create( AgeAbstractClass mCls, ImpC parent );
  void addSubclass(ImpC bclass, ImpC derClass);
 }
 
 private static <ImpC> void convertAgeToImprint(AgeAbstractClass acls, ImpC parent, Map<AgeAbstractClass, Object> clMap, Creator<ImpC> cr)
 {
//  ImpC cImp = cr.create(acls.getId(),acls.getName());
//  clMap.put(acls,cImp);

  if( acls.getSubClasses() != null )
  {
   for( AgeAbstractClass scls : acls.getSubClasses() )
   {
    ImpC subImp = (ImpC)clMap.get(scls);
    
    if( subImp == null )
    {
     subImp = cr.create(scls, parent);
     clMap.put(scls, subImp);

     convertAgeToImprint(scls, subImp, clMap, cr);
    }
    else
     cr.addSubclass(parent, subImp);
    
   }
  }
 }  

}
