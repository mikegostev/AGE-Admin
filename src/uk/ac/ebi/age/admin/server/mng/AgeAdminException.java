package uk.ac.ebi.age.admin.server.mng;

import java.io.IOException;

public class AgeAdminException extends Exception
{

 private static final long serialVersionUID = 1L;

 public AgeAdminException()
 {}

 public AgeAdminException( Exception e )
 {
  super(e);
 }

 public AgeAdminException(String string, IOException e)
 {
  super(string,e);
 }
}
