package uk.ac.ebi.age.admin.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AttributeRuleImprint implements IsSerializable, Serializable
{
 private static final long serialVersionUID = 1L;

 private RestrictionType type = RestrictionType.MAY;
 private Cardinality cardType = Cardinality.ANY;
 private AgeAttributeClassImprint attributeClass;
 private int cardinality=1;
 private Collection<QualifierRuleImprint> qualifiers;
 private boolean valueUnique;
 private boolean subclassesIncluded=true;
// private QualifiersCondition qualifiersCondition = QualifiersCondition.ANY ;

 private int id;
 
 private ModelImprint model;
 
 AttributeRuleImprint( RestrictionType typ, ModelImprint m )
 {
  type=typ;
  model=m;
 }
 
 public AgeAttributeClassImprint getAttributeClass()
 {
  return attributeClass;
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

 public Collection<QualifierRuleImprint> getQualifiers()
 {
  return qualifiers;
 }

// public Collection<QualifierRuleImprint> getQualifiers()
// {
//  if( qualifiers == null )
//   return null;
//  
//  return new CollectionsUnion<QualifierRuleImprint>(qualifiers.values());
// }
 
 public void addQualifier( QualifierRuleImprint qr )
 {
  if( qualifiers == null )
   qualifiers=new ArrayList<QualifierRuleImprint>();

  qualifiers.add(qr);
 }
 
 public void setAttributeClass(AgeAttributeClassImprint attributeClass)
 {
  this.attributeClass = attributeClass;
 }

 public String toString()
 {
  StringBuilder sb = new StringBuilder();
  
  boolean singleAttr = cardinality == 1 && cardType != Cardinality.MIN;
  
  sb.append("entity ").append(type.getTitle()).append(" have ");
  sb.append("attribute");
  if( !singleAttr )
   sb.append('s');
  
  sb.append(" of class <b>").append(attributeClass.getName()).append("</b> (");

  if( subclassesIncluded )
   sb.append("including subclasses)");
  else
   sb.append("excluding subclasses)");
   
  
  if( type != RestrictionType.MUSTNOT )
  {
//   sb.append(" that have ").append(cardType.name());
   
   if( cardType == Cardinality.ANY)
    sb.append(" that may have any number of values ");
   else
    sb.append(" that must have ").append(cardType.getTitle()).append(" ").append(cardinality).append(" value").append(cardinality>1?"s ":" ");
   
   if( ( ! singleAttr ) && valueUnique )
    sb.append("(all values must be unique) ");
   
  }
  else
  {
   if( !singleAttr )
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

  }
  
  sb.append('.');
  

  if( qualifiers != null && qualifiers.size() > 0 )
  {
 
   sb.append(" Rule is applied only if attribute");
   if( !singleAttr )
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

 public void clearQualifiers()
 {
  if( qualifiers != null )
   qualifiers.clear();
 }

 public boolean isValueUnique()
 {
  return valueUnique;
 }
 
 public void setValueUnique(boolean valueUnique)
 {
  this.valueUnique = valueUnique;
 }

 public boolean isSubclassesIncluded()
 {
  return subclassesIncluded;
 }

 public void setSubclassesIncluded(boolean subclassesIncluded)
 {
  this.subclassesIncluded = subclassesIncluded;
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
