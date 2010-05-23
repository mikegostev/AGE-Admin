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

}
