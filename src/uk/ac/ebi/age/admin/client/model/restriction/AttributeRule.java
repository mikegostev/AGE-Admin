package uk.ac.ebi.age.admin.client.model.restriction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import uk.ac.ebi.age.admin.client.model.AgeAttributeClassImprint;

public class AttributeRule
{
 private RestrictionType type = RestrictionType.MAY;
 private RestrictionCardinality cardType = RestrictionCardinality.SOME;
 private AgeAttributeClassImprint attributeClass;
 private int cardinality;
 private Map<RestrictionType,Collection<QualifierRule>> qualifiers;

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

 public RestrictionCardinality getCardinalityType()
 {
  return cardType;
 }

 public void setCardinalityType(RestrictionCardinality cardType)
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

 public Map<RestrictionType,Collection<QualifierRule>> getQualifiersMap()
 {
  return qualifiers;
 }

 public void addQualifier( QualifierRule qr )
 {
  if( qualifiers == null )
  {
   qualifiers=new TreeMap<RestrictionType, Collection<QualifierRule>>();
   Collection<QualifierRule> coll = new ArrayList<QualifierRule>(5);
   coll.add(qr);
   qualifiers.put(qr.getType(), coll);
   return;
  }
  
  Collection<QualifierRule> coll = qualifiers.get(qr.getType());
  
  if( coll == null )
  {
   coll = new ArrayList<QualifierRule>(5);
   qualifiers.put(qr.getType(), coll);
  }
 
  coll.add(qr);
 
 }
 
 public void setAttributeClass(AgeAttributeClassImprint attributeClass)
 {
  this.attributeClass = attributeClass;
 }

 public String toString()
 {
  StringBuilder sb = new StringBuilder();
  
  sb.append("object ").append(type.getTitle()).append(" have ");
  
  sb.append(cardType.getTitle());
  
  if( cardType == RestrictionCardinality.RP )
   sb.append(" ").append(cardinality).append(" times");
  else if( cardType != RestrictionCardinality.SOME )
   sb.append(" ").append(cardinality);
  
  
  sb.append(" attributes of class <b>").append(attributeClass.getName()).append("</b>");
  
  if( qualifiers != null )
  {
   boolean hasQ=false;
 
   sb.append(" that ");
   
   for( RestrictionType rt : RestrictionType.values() )
   {
    Collection<QualifierRule> qcoll = qualifiers.get(rt);
    
    if( qcoll != null && qcoll.size() > 0 )
    {
     hasQ=true;
     sb.append(rt.getTitle()).append(" have qualifiers of class");
     
     if( qcoll.size() > 1 )
      sb.append("es");
     
     sb.append(" ");
     
     for( QualifierRule qr : qcoll )
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
  
  return sb.toString();
 }

 public void clearQualifiers()
 {
  if( qualifiers != null )
   qualifiers.clear();
 }
}
