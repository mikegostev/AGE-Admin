package uk.ac.ebi.age.admin.client.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AgeAnnotationImprint implements IsSerializable, Serializable
{
 private static final long serialVersionUID = 1L;

 private AgeAnnotationClassImprint annotClass;
 private String text;
 
 public AgeAnnotationImprint( AgeAnnotationClassImprint cls )
 {
  annotClass=cls;
 }
 
 public AgeAbstractClassImprint getAnnotationClass()
 {
  return annotClass;
 }

 public String getText()
 {
  return text;
 }

// public void setAnnotationClass(AgeAnnotationClassImprint cls)
// {
//  annotClass=cls;
// }

 public void setText(String string)
 {
  text=string;
 }

}
