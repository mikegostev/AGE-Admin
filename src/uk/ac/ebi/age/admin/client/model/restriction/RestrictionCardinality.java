package uk.ac.ebi.age.admin.client.model.restriction;

public enum RestrictionCardinality
{
 SOME ("Some"),
 EXACT ("Exactly"),
 LE ("Less or equal"),
 ME ("More or equal"),
 RP ("Repeating");
 
 private String title;
 
 RestrictionCardinality(String tl)
 {
  title=tl;
 }
 
 public String getTitle()
 {
  return title;
 }
}
