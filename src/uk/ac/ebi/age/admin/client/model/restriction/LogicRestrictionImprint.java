package uk.ac.ebi.age.admin.client.model.restriction;

import java.util.Collection;

public class LogicRestrictionImprint extends ObjectRestrictionImprint
{

 private Collection<ObjectRestrictionImprint> operands;

 public Collection<ObjectRestrictionImprint> getOperands()
 {
  return operands;
 }

 public void setOperands(Collection<ObjectRestrictionImprint> operands)
 {
  this.operands = operands;
 }
 
 public void addOperand(ObjectRestrictionImprint operand)
 {
  operands.add(operand);
 }

}
