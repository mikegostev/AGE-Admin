package trash.uk.ac.ebi.age.admin.client.model;

public enum RestrictionType
{
 MAY ("MAY"),
 MUST ("MUST"),
 MUSTNOT ("MUST NOT");
 
 private String title;
 
 RestrictionType(String tl)
 {
  title=tl;
 }
 
 public String getTitle()
 {
  return title;
 }
}
