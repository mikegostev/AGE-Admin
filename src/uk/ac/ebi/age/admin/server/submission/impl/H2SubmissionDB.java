package uk.ac.ebi.age.admin.server.submission.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import uk.ac.ebi.age.admin.server.model.SubmissionMeta;
import uk.ac.ebi.age.admin.server.submission.SubmissionDB;
import uk.ac.ebi.age.model.DataModuleMeta;
import uk.ac.ebi.mg.filedepot.FileDepot;

public class H2SubmissionDB extends SubmissionDB
{
 private static final String submissionDB = "submissiondb";
 private static final String submissionTable = "submission";
 private static final String moduleTable = "module";

 private static final String insertSubmissionSQL = "INSERT INTO "+submissionDB+"."+submissionTable
 +" (id,desc,ctime,mtime,creator,modifier) VALUES (?,?,?,?,?,?)";
 private static final String insertModuleSQL = "INSERT INTO "+submissionDB+"."+moduleTable
 +" (id,subm,desc,mtime,modifier,version) VALUES (?,?,?,?,?,?)";
 
 private static final String h2DbPath = "h2db";
 private static final String docDepotPath = "docs";
 
 private static final Charset docCharset = Charset.forName("UTF-8");
 
 private Connection conn;
 private FileDepot depot;

 public H2SubmissionDB( File sbmDbRoot )
 {
  try
  {
   Class.forName("org.h2.Driver");
   conn = DriverManager.getConnection("jdbc:h2:"+new File(sbmDbRoot,h2DbPath).getAbsolutePath(), "sa", "");
   conn.setAutoCommit(false);
   
   initSubmissionDb();
   
   depot = new FileDepot( new File(sbmDbRoot,docDepotPath) );
  }
  catch(Exception e)
  {
   e.printStackTrace();
   
   throw new RuntimeException("Database initialization error: "+e.getMessage(),e);
  }

 }

 @Override
 public void storeSubmission(SubmissionMeta sMeta)
 {
  try
  {
   PreparedStatement pstsmt  = conn.prepareStatement(insertSubmissionSQL);
   pstsmt.setString(1, sMeta.getId());
   pstsmt.setString(2, sMeta.getDescription());
   pstsmt.setLong(3, sMeta.getSubmissionTime());
   pstsmt.setLong(4, sMeta.getModificationTime());
   pstsmt.setString(5, sMeta.getSubmitter());
   pstsmt.setString(6, sMeta.getModifier());
   
   pstsmt.executeUpdate();
   pstsmt.close();
   
   pstsmt  = conn.prepareStatement(insertModuleSQL);
   for( DataModuleMeta dmm : sMeta.getDataModules() )
   {
    pstsmt.setString(1, dmm.getId());
    pstsmt.setString(2, sMeta.getId());
    pstsmt.setString(3, dmm.getDescription());
    pstsmt.setLong(4, dmm.getModificationTime());
    pstsmt.setString(5, dmm.getModifier());
    pstsmt.setLong(6, dmm.getVersion());
    
    pstsmt.executeUpdate();
    
    File outPFile = depot.getFilePath(dmm.getId(), dmm.getVersion());
    
    OutputStreamWriter wrtr = new OutputStreamWriter( new FileOutputStream(outPFile), docCharset);
    
    wrtr.write(dmm.getText());
    
    wrtr.close();
   }
   
   pstsmt.close();
   
   conn.commit();
  }
  catch(Exception e)
  {
   try
   {
    conn.rollback();
   }
   catch(SQLException e1)
   {
    e1.printStackTrace();
   }

   e.printStackTrace();
  }


 }

 private void initSubmissionDb() throws SQLException
 {
  Statement stmt = conn.createStatement();
  
  stmt.executeUpdate("CREATE SCHEMA IF NOT EXISTS "+submissionDB);

  stmt.executeUpdate("CREATE TABLE IF NOT EXISTS "+submissionDB+'.'+submissionTable+" ("+
    "id VARCHAR PRIMARY KEY, desc VARCHAR, ctime BIGINT, mtime BIGINT, creator VARCHAR, modifier VARCHAR, object BINARY)");

  stmt.executeUpdate("CREATE INDEX IF NOT EXISTS ctimeIdx ON "+submissionDB+'.'+submissionTable+"(ctime)");
  stmt.executeUpdate("CREATE INDEX IF NOT EXISTS mtimeIdx ON "+submissionDB+'.'+submissionTable+"(mtime)");
  stmt.executeUpdate("CREATE INDEX IF NOT EXISTS creatorIdx ON "+submissionDB+'.'+submissionTable+"(creator)");
  stmt.executeUpdate("CREATE INDEX IF NOT EXISTS modifierIdx ON "+submissionDB+'.'+submissionTable+"(modifier)");

  stmt.executeUpdate("CREATE TABLE IF NOT EXISTS "+submissionDB+'.'+moduleTable+" ("+
    "id VARCHAR PRIMARY KEY, submid VARCHAR, desc VARCHAR, mtime BIGINT, modifier VARCHAR, version BIGINT, object BINARY, FOREIGN KEY(submid) REFERENCES "
    +submissionDB+'.'+submissionTable+"(id) )");

  stmt.executeUpdate("CREATE ALIAS IF NOT EXISTS FTL_INIT FOR \"org.h2.fulltext.FullTextLucene.init\"");
  stmt.executeUpdate("CALL FTL_INIT()");

  stmt.close();
 }

 @Override
 public void shutdown()
 {
  if( conn != null )
   try
   {
    conn.close();
   }
   catch(SQLException e)
   {
    e.printStackTrace();
   }
  
  if( depot != null )
   depot.shutdown();  
 }

 @Override
 public void init()
 {
 }
}
