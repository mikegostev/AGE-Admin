package uk.ac.ebi.age.admin.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RelationRuleImprint implements Serializable, IsSerializable
{

 private static final long serialVersionUID = 1L;

 private RestrictionType type = RestrictionType.MAY;
 private Cardinality cardType = Cardinality.ANY;

 private AgeClassImprint targetClass;
 private AgeRelationClassImprint relationClass;
 
 private int cardinality=1;
 private Collection<QualifierRuleImprint> qualifiers;
 
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

 
 public void setTargetClass(AgeClassImprint tClass)
 {
  this.targetClass = tClass;
 }

 public String toString()
 {
  StringBuilder sb = new StringBuilder();
  
  boolean singleRel = cardinality == 1 && cardType != Cardinality.MIN;

  sb.append("object ").append(type.getTitle()).append(" have ");
  
  
  if( type != RestrictionType.MUSTNOT )
  {
   if( cardType == Cardinality.ANY)
    sb.append(" any number of relations ");
   else
    sb.append(cardType.getTitle()).append(" ").append(cardinality).append(" relation").append(singleRel?" ":"s ");
   
//   if( ( cardinality > 1 || cardType == Cardinality.MIN ) &&  qualifiersUnique )
//   {
//    sb.append("(all ");
//    
//    if(qualifiersUnique)
//     sb.append("qualifiers");
//    
//    sb.append(" must be unique) ");
//    
//   }
   
   sb.append("of class <b>").append(targetClass.getName()).append("</b> (");
   
   if( subclassesIncluded )
    sb.append("including subclasses) ");
   else
    sb.append("excluding subclasses) ");
   
  }
  else
  {
   if( cardType != Cardinality.ANY )
   {
    sb.append("relations if they have ");
    
    switch(cardType)
    {
     case EXACT:
      sb.append("exactly ").append(cardinality);
      break;
 
     case MIN:
      sb.append(cardinality).append(" or more");
      break;

     case MAX:
      sb.append(cardinality).append(" or less");
      break;

     default:
      break;
    }
    
    sb.append(" relation");
    
    if( ! singleRel )
     sb.append('s');
   }
   
  }
  
  sb.append(" with objects of class <b>").append(targetClass.getName()).append("</b> (");
  
  if( subclassesIncluded )
   sb.append("including subclasses)");
  else
   sb.append("excluding subclasses)");

  sb.append('.');
  

  if( qualifiers != null && qualifiers.size() > 0 )
  {
 
   sb.append(" Rule is applied only if relation");
   if( !singleRel )
    sb.append("s have");
   else
    sb.append(" has");
   
   sb.append(" qualifier(s) of class");
   
   if( qualifiers.size() > 1 )
    sb.append("es");
    
   sb.append(" ");
    
   for( QualifierRuleImprint qr : qualifiers )
   {
    sb.append("<b>").append(qr.getAttributeClassImprint().getName()).append("</b>");
    
    if(  qr.isUnique() )
     sb.append(" (unique)");

     sb.append(", ");
   }

   sb.setLength( sb.length()-2 );
  }
  
  sb.append('.');

  
  return sb.toString();
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

 public Collection<QualifierRuleImprint> getQualifiers()
 {
  return qualifiers;
 }

 public void addQualifier( QualifierRuleImprint qr )
 {
  if( qualifiers == null )
   qualifiers=new ArrayList<QualifierRuleImprint>();

  qualifiers.add(qr);
 }

 public void clearQualifiers()
 {
  if( qualifiers != null )
   qualifiers.clear();
 }

}
