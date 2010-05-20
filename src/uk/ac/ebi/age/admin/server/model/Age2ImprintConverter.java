package uk.ac.ebi.age.admin.server.model;

import java.util.HashMap;
import java.util.Map;

import uk.ac.ebi.age.admin.client.model.AgeAttributeClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeRelationClassImprint;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.model.restriction.CardinalityRestrictionImprint;
import uk.ac.ebi.age.admin.client.model.restriction.FillerRestrictionImprint;
import uk.ac.ebi.age.admin.client.model.restriction.IntanceOfRestrictionImprint;
import uk.ac.ebi.age.admin.client.model.restriction.LogicRestrictionImprint;
import uk.ac.ebi.age.admin.client.model.restriction.ObjectRestrictionImprint;
import uk.ac.ebi.age.admin.client.model.restriction.ObjectRestrictionImprint.Type;
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
import uk.ac.ebi.age.model.SemanticModel;

public class Age2ImprintConverter
{
 public static ModelImprint getModelImprint( SemanticModel sm )
 {
  
  ModelImprint mimp = new ModelImprint();
  
  Map<AgeAbstractClass,Object> clMap = new HashMap<AgeAbstractClass, Object>();
  
  AgeClass ageRoot = sm.getRootAgeClass();
  
  AgeClassImprint rImp = new AgeClassImprint();
  
  for( AgeClass acls : ageRoot.getSubClasses() )
  {
   AgeClassImprint simp = convertAgeToImprint(acls,clMap, new Creator<AgeClassImprint>()
   {

    @Override
    public void addSubclass(AgeClassImprint bclass, AgeClassImprint derClass)
    {
     bclass.addSubClass(derClass);
     derClass.addSuperClass(bclass);
    }

    @Override
    public AgeClassImprint create(String id, String name)
    {
     AgeClassImprint cImp = new AgeClassImprint();
     
     cImp.setName(name);
     cImp.setId(id);
     
     return cImp;
    }
   });
   
   rImp.addSubClass(simp);
   simp.addSuperClass(rImp);
  }
  
  mimp.setRootClass(rImp);
  
  
  
  AgeAttributeClass attrRoot = sm.getRootAgeAttributeClass();
  
  AgeAttributeClassImprint atImp = new AgeAttributeClassImprint();
  
  for( AgeAttributeClass acls : attrRoot.getSubClasses() )
  {
   AgeAttributeClassImprint simp = convertAgeToImprint(acls,clMap, new Creator<AgeAttributeClassImprint>()
   {

    @Override
    public void addSubclass(AgeAttributeClassImprint bclass, AgeAttributeClassImprint derClass)
    {
     bclass.addSubClass(derClass);
     derClass.addSuperClass(bclass);
    }

    @Override
    public AgeAttributeClassImprint create(String id, String name)
    {
     AgeAttributeClassImprint cImp = new AgeAttributeClassImprint();
     
     cImp.setName(name);
     cImp.setId(id);
     
     return cImp;
    }
   });
   
   atImp.addSubClass(simp);
   simp.addSuperClass(atImp);
  }
  
  mimp.setRootAttributeClass(atImp);
  
  
  
  AgeRelationClass relRoot = sm.getRootAgeRelationClass();
  
  AgeRelationClassImprint relImp = new AgeRelationClassImprint();
  
  for( AgeRelationClass rcls : relRoot.getSubClasses() )
  {
   AgeRelationClassImprint simp = convertAgeToImprint(rcls,clMap, new Creator<AgeRelationClassImprint>()
   {

    @Override
    public void addSubclass(AgeRelationClassImprint bclass, AgeRelationClassImprint derClass)
    {
     bclass.addSubClass(derClass);
     derClass.addSuperClass(bclass);
    }

    @Override
    public AgeRelationClassImprint create(String id, String name)
    {
     AgeRelationClassImprint cImp = new AgeRelationClassImprint();
     
     cImp.setName(name);
     cImp.setId(id);
     
     return cImp;
    }
   });
   
   relImp.addSubClass(simp);
   simp.addSuperClass(relImp);
  }
  
  mimp.setRootRelationClass(relImp);

  convertObjectRestrictions(sm.getRootAgeClass(), clMap );
  
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

 private <T extends AgeAbstractClass> void  convertAttributeRestrictions(T rootAgeClass, Map<AgeAbstractClass, Object> clMap)
 {
  if(rootAgeClass.getSubClasses() == null)
   return;

  for(AgeAbstractClass cls : rootAgeClass.getSubClasses())
  {
   if(cls.getAttributeRestrictions() == null)
    continue;

   AgeClassImprint clImp = (AgeClassImprint) clMap.get(cls);

   for(AgeRestriction rstr : cls.getAttributeRestrictions())
    clImp.addRestriction(convertRestriction(rstr, clMap));
  }

 } 
 
 private static ObjectRestrictionImprint convertRestriction( AgeRestriction rstr, Map<AgeAbstractClass, Object> clMap )
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
 
   ri.setType(Type.ONLY);
   ri.setRelation((AgeRelationClassImprint)clMap.get(((AgeSomeValuesFromRestriction) rstr).getAgeRelationClass()));
   ri.setFiller( convertRestriction(((AgeSomeValuesFromRestriction) rstr).getFiller(), clMap));
   
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
   ri.setAgeClassImprint((AgeClassImprint)clMap.get(((AgeIsInstanceOfRestriction) rstr).getTargetClass()));
   
   return ri;
  }

  return null;
 }

 private interface Creator<ImpC>
 {
  ImpC create( String id, String name );
  void addSubclass(ImpC bclass, ImpC derClass);
 }
 
 private static <ImpC> ImpC convertAgeToImprint(AgeAbstractClass acls, Map<AgeAbstractClass, Object> clMap, Creator<ImpC> cr)
 {
  ImpC cImp = cr.create(acls.getId(),acls.getName());
  

  if( acls.getSubClasses() != null )
  {
   for( AgeAbstractClass scls : acls.getSubClasses() )
   {
    ImpC subImp = (ImpC)clMap.get(scls);
    
    if( subImp == null )
    {
     subImp = convertAgeToImprint(scls, clMap, cr);
     clMap.put(scls, subImp);
    }
    
    cr.addSubclass(cImp,subImp);
   }
  }
  
  return cImp;
 }

}
