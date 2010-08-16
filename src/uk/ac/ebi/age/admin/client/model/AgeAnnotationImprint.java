package uk.ac.ebi.age.admin.client.model;

public class AgeAnnotationImprint
{
 private AgeAnnotationClassImprint annotClass;
 private String text;
 
 public AgeAbstractClassImprint getAnnotationClass()
 {
  return annotClass;
 }

 public String getText()
 {
  return text;
 }

 public void setAnnotationClass(AgeAnnotationClassImprint cls)
 {
  annotClass=cls;
 }

 public void setText(String string)
 {
  text=string;
 }
}
