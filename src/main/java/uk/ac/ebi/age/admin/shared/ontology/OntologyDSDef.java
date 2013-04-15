package uk.ac.ebi.age.admin.shared.ontology;

import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.admin.shared.ds.DSDef;
import uk.ac.ebi.age.admin.shared.ds.DSField;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.types.FieldType;

public class OntologyDSDef extends DSDef
{
 public static OntologyDSDef instance;
 public static DSField ontologyNameField = new DSField("name",FieldType.TEXT,"Ontology ID");
 public static DSField ontologyDescriptionField = new DSField("desc",FieldType.TEXT,"Description");
 public static DSField ontologyURLField = new DSField("URL",FieldType.PASSWORD,"URL");
 
 static
 {
  ontologyNameField.setPrimaryKey( true );
  ontologyNameField.setWidth(150);
  
  ontologyDescriptionField.setEditable( true );
  ontologyURLField.setEditable( true );
 }
 
 public OntologyDSDef()
 {
  addField( ontologyNameField );
  addField( ontologyDescriptionField );
  addField( ontologyURLField );
 }

 public static OntologyDSDef getInstance()
 {
  if( instance == null )
   instance = new OntologyDSDef();
  
  return instance;
 }
 
 @Override
 public DataSource createDataSource()
 {
  RestDataSource ds = new RestDataSource();
  
  ds.setAutoCacheAllData(false);
  ds.setCacheAllData(false);
  
  DataSourceField idF = new DataSourceField(ontologyNameField.getFieldId(), ontologyNameField.getType(), ontologyNameField.getFieldTitle());
  idF.setPrimaryKey(true);
  
  DataSourceField descF = new DataSourceField(ontologyDescriptionField.getFieldId(), ontologyDescriptionField.getType(), ontologyDescriptionField.getFieldTitle());
  descF.setCanEdit(true);

  DataSourceField urlF = new DataSourceField(ontologyURLField.getFieldId(), ontologyURLField.getType(), ontologyURLField.getFieldTitle());
  urlF.setCanEdit(true);

  
  ds.setFields(idF,descF,urlF);
  
  ds.setID(Constants.ontologyListServiceName);
  
  return ds;
 }
}
