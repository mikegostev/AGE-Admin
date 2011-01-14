package uk.ac.ebi.age.admin.client.log;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public final class LogNode extends JavaScriptObject
{
 protected LogNode() { super(); };
 
 public native String getLevel() /*-{ return this.level; }-*/ ;
 public native String getMessage() /*-{ return this.message; }-*/ ;
 public native JsArray<LogNode> getSubnodes() /*-{ return this.subnodes; }-*/ ;
 
 public static native LogNode convert( String txt ) /*-{ return eval(txt); }-*/ ;
}
