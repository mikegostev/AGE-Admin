package uk.ac.ebi.age.admin.shared.ds;

import com.smartgwt.client.types.FieldType;

public class DSField
{
 private String fieldId;
 private String fieldTitle;
 private FieldType type;
 private int    width = -1;

 public static final String fieldIdPrefix = "##";
 
 public DSField(String string, FieldType t, String ttl, int w)
 {
  
  if( string.startsWith(fieldIdPrefix) )
   fieldId=string;
  else
   fieldId = fieldIdPrefix+string;
  
  type = t;
  fieldTitle = ttl;
  width = w;
 }

 public DSField(String string, FieldType text, String string2)
 {
  this( string, text, string2, -1);
 }

 public String getFieldId()
 {
  return fieldId;
 }

 public void setFieldId(String fId)
 {
  if( fId.startsWith(fieldIdPrefix) )
   fieldId=fId;
  else
   fieldId = fieldIdPrefix+fId;
 }

 public String getFieldTitle()
 {
  return fieldTitle;
 }

 public void setFieldTitle(String fieldTitle)
 {
  this.fieldTitle = fieldTitle;
 }

 public int getWidth()
 {
  return width;
 }

 public void setWidth(int width)
 {
  this.width = width;
 }

 public FieldType getType()
 {
  return type;
 }

 public void setType(FieldType type)
 {
  this.type = type;
 }
 
 public boolean equals( Object o )
 {
  if( o instanceof DSField && ((DSField)o).getFieldId().equals(getFieldId()) )
   return true;
  
  return false;
 }
 
 public int hashCode()
 {
  return fieldId.hashCode();
 }
}
