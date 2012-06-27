package uk.ac.ebi.age.admin.shared.user.exception;

import com.google.gwt.user.client.rpc.IsSerializable;

public class NotAuthorizedException extends Exception implements IsSerializable
{

 private static final long serialVersionUID = 1L;

 public NotAuthorizedException()
 {
 }
 
 public NotAuthorizedException( String msg )
 {
  super( msg );
 }

}
