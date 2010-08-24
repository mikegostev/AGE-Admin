package uk.ac.ebi.age.admin.client.model;

import java.util.Collection;

public interface Annotated
{

 Collection<AgeAnnotationImprint> getAnnotations();

 ModelImprint getModel();

 void removeAnnotation(AgeAnnotationImprint annotation);

 void addAnnotation(AgeAnnotationImprint annt);

}
