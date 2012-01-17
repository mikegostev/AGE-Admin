package uk.ac.ebi.age.admin.client.log;

import java.util.List;

import uk.ac.ebi.age.ext.log.LogNode;
import uk.ac.ebi.age.ui.client.util.ListOnJsArray;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public final class ROJSLogNode extends JavaScriptObject implements LogNode
{
 protected ROJSLogNode() { super(); };
 
 public native String getLevelAsString() /*-{ return this.level; }-*/ ;
 public native String setLevelAsString( String lvl ) /*-{ this.level=lvl; }-*/ ;
 public native String getMessage() /*-{ return this.message; }-*/ ;
 public native JsArray<ROJSLogNode> getSubnodes() /*-{ return this.subnodes; }-*/ ;
 
 public static native ROJSLogNode convert( String txt ) /*-{ return eval(txt); }-*/ ;

 @Override
 public void log(Level lvl, String msg)
 {
 }

 @Override
 public LogNode branch(String msg)
 {
  return null;
 }

 @Override
 public void setLevel(Level lvl)
 {
 }

 @Override
 public void success()
 {
 }

 @Override
 public List<? extends LogNode> getSubNodes()
 {
  JsArray<ROJSLogNode> snds = getSubnodes();
  
  if( snds == null )
   return null;
  
  return new ListOnJsArray<ROJSLogNode>( snds );
 }

 @Override
 public void append(LogNode rootNode)
 {
 }

 @Override
 public Level getLevel()
 {
  return Level.valueOf(getLevelAsString());
 }

}
