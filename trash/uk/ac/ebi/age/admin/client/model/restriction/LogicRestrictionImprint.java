package uk.ac.ebi.age.admin.client.model.restriction;

import java.util.ArrayList;
import java.util.Collection;

public class LogicRestrictionImprint extends RestrictionImprint
{

 private Collection<RestrictionImprint> operands = new ArrayList<RestrictionImprint>(3);

 public Collection<RestrictionImprint> getOperands()
 {
  return operands;
 }

 public void setOperands(Collection<RestrictionImprint> operands)
 {
  this.operands = operands;
 }
 
 public void addOperand(RestrictionImprint operand)
 {
  operands.add(operand);
 }
 
 public String toString()
 {
  StringBuilder sb = new StringBuilder();
  
  if( getType() == Type.NOT )
  {
   sb.append("NOT ( ").append( operands.iterator().next() ).append(" )");
   return sb.toString();
  }
  
  sb.append("( ");
  
  String op = getType() == Type.AND?" AND ":" OR ";
  
  for( RestrictionImprint rimp : operands )
  {
   sb.append(rimp.toString());
   sb.append(op);
  }
  
  sb.setLength( sb.length()-op.length() );
  
  sb.append(" )");
  
  return sb.toString();
  
 }

}
