package uk.ac.ebi.age.admin.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RelationRuleImprint implements Serializable, IsSerializable
{

 private static final long serialVersionUID = 1L;

 private RestrictionType type = RestrictionType.MAY;
 private Cardinality cardType = Cardinality.ANY;

 private AgeClassImprint targetClass;
 private AgeRelationClassImprint relationClass;
 
 private int cardinality=1;
 private Map<RestrictionType,Collection<QualifierRuleImprint>> qualifiers;
 
 private boolean qualifiersUnique;
 private boolean subclassesIncluded=true;
 private boolean relSubclassesIncluded=true;

// private QualifiersCondition qualifiersCondition = QualifiersCondition.ANY ;
 
 private int id;
 
 private ModelImprint model;

 public RelationRuleImprint(RestrictionType typ, ModelImprint modelImprint)
 {
  type=typ;
  model=modelImprint;
 }


 public RestrictionType getType()
 {
  return type;
 }

 public void setType(RestrictionType type)
 {
  this.type = type;
 }

 public Cardinality getCardinalityType()
 {
  return cardType;
 }

 public void setCardinalityType(Cardinality cardType)
 {
  this.cardType = cardType;
 }

 public int getCardinality()
 {
  return cardinality;
 }

 public void setCardinality(int cardinality)
 {
  this.cardinality = cardinality;
 }

 public Map<RestrictionType,Collection<QualifierRuleImprint>> getQualifiersMap()
 {
  return qualifiers;
 }

// public Collection<QualifierRuleImprint> getQualifiers()
// {
//  if( qualifiers == null )
//   return null;
//  
//  return new CollectionsUnion<QualifierRuleImprint>( qualifiers.values() );
// }
 
 public void addQualifier( QualifierRuleImprint qr )
 {
  if( qualifiers == null )
  {
   qualifiers=new TreeMap<RestrictionType, Collection<QualifierRuleImprint>>();
   Collection<QualifierRuleImprint> coll = new ArrayList<QualifierRuleImprint>(5);
   coll.add(qr);
   qualifiers.put(qr.getType(), coll);
   return;
  }
  
  Collection<QualifierRuleImprint> coll = qualifiers.get(qr.getType());
  
  if( coll == null )
  {
   coll = new ArrayList<QualifierRuleImprint>(5);
   qualifiers.put(qr.getType(), coll);
  }
 
  coll.add(qr);
 
 }
 
 public void setTargetClass(AgeClassImprint tClass)
 {
  this.targetClass = tClass;
 }

 public String toString()
 {
  StringBuilder sb = new StringBuilder();
  
  sb.append("object ").append(type.getTitle()).append(" have ");
  
  
  if( type != RestrictionType.MUSTNOT )
  {
   if( cardType == Cardinality.ANY)
    sb.append(" any number of relations ");
   else
    sb.append(cardType.getTitle()).append(" ").append(cardinality).append(" relation").append(cardinality>1?"s ":" ");
   
   if( ( cardinality > 1 || cardType == Cardinality.MIN ) &&  qualifiersUnique )
   {
    sb.append("(all ");
    
    if(qualifiersUnique)
     sb.append("qualifiers");
    
    sb.append(" must be unique) ");
    
   }
   
   sb.append("with class <b>").append(targetClass.getName()).append("</b> (");
   
   if( subclassesIncluded )
    sb.append("including subclasses) ");
   else
    sb.append("excluding subclasses) ");
   
   if( qualifiers != null )
   {
    boolean hasQ=false;
  
    sb.append("and ");
    
    for( RestrictionType rt : RestrictionType.values() )
    {
     Collection<QualifierRuleImprint> qcoll = qualifiers.get(rt);
     
     if( qcoll != null && qcoll.size() > 0 )
     {
      hasQ=true;
      sb.append(rt.getTitle()).append(" have qualifiers of class");
      
      if( qcoll.size() > 1 )
       sb.append("es");
      
      sb.append(" ");
      
      for( QualifierRuleImprint qr : qcoll )
       sb.append("<b>").append(qr.getAttributeClassImprint().getName()).append("</b>, ");
      
      sb.setLength( sb.length()-2 );
      
      sb.append(" and ");
     }
    }

    if( hasQ )
     sb.setLength( sb.length()-5 );
    else
     sb.setLength( sb.length()-6 );
   }
  }
  else
  {
   if( cardType != Cardinality.ANY )
   {
    sb.append(" if they have ");
    
    switch(cardType)
    {
     case EXACT:
      sb.append(cardinality).append(" values");
      break;
 
     case MIN:
      sb.append(cardinality).append(" or more values");
      break;

     case MAX:
      sb.append(cardinality).append(" or less values");
      break;

     default:
      break;
    }
   }
   
   if( qualifiersCondition != QualifiersCondition.ANY )
   {
    if( cardType != Cardinality.ANY )
     sb.append(" and");
    
    if( qualifiersCondition == QualifiersCondition.NONE )
     sb.append(" if they have no qualifiers");
    else if( qualifiers != null && qualifiers.size() > 0 )
    {
     sb.append(" if they have the following qualifier");
     
     sb.append( qualifiers.size() > 1?"s ":" ");
     
     for( QualifierRuleImprint qr : qualifiers.get(RestrictionType.MUST) )
      sb.append("<b>").append(qr.getAttributeClassImprint().getName()).append("</b>, ");

     sb.setLength(sb.length()-2);
    }
   }
  }
  
  sb.append(" with class <b>").append(targetClass.getName()).append("</b> (");
  
  if( subclassesIncluded )
   sb.append("including subclasses)");
  else
   sb.append("excluding subclasses)");

  

  
  return sb.toString();
 }

 public void clearQualifiers()
 {
  if( qualifiers != null )
   qualifiers.clear();
 }

 public boolean isQualifiersUnique()
 {
  return qualifiersUnique;
 }

 public void setQualifiersUnique(boolean qualifiersUnique)
 {
  this.qualifiersUnique = qualifiersUnique;
 }

 public void setQualifiersCondition(QualifiersCondition qc)
 {
  qualifiersCondition = qc;
 }

 public QualifiersCondition getQualifiersCondition()
 {
  return qualifiersCondition;
 }

 public boolean isSubclassesIncluded()
 {
  return subclassesIncluded;
 }

 public void setSubclassesIncluded(boolean subclassesIncluded)
 {
  this.subclassesIncluded = subclassesIncluded;
 }

 public boolean isRelationSubclassesIncluded()
 {
  return relSubclassesIncluded;
 }

 public void setRelationSubclassesIncluded(boolean subclassesIncluded)
 {
  this.relSubclassesIncluded = subclassesIncluded;
 }

 
 public AgeClassImprint getTargetClass()
 {
  return targetClass;
 }

 public AgeRelationClassImprint getRelationClass()
 {
  return relationClass;
 }


 public void setRelationClass(AgeRelationClassImprint relationClass2)
 {
  relationClass=relationClass2;
 }


 public ModelImprint getModel()
 {
  return model;
 }


 public int getId()
 {
  return id;
 }


 public void setId(int id)
 {
  this.id = id;
 }



}
