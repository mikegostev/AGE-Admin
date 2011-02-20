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
import java.util.List;

import uk.ac.ebi.age.admin.server.model.SubmissionMeta;
import uk.ac.ebi.age.admin.server.submission.SubmissionDB;
import uk.ac.ebi.age.admin.shared.submission.SubmissionImprint;
import uk.ac.ebi.age.admin.shared.submission.SubmissionQuery;
import uk.ac.ebi.age.model.DataModuleMeta;
import uk.ac.ebi.mg.filedepot.FileDepot;

import com.pri.util.StringUtils;

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

  try
  {
   stmt.executeUpdate("CALL FTL_CREATE_INDEX('"+submissionDB+"', '"+submissionTable+"', 'desc')");
  }
  catch (Exception e)
  {
  }
  
  try
  {
   stmt.executeUpdate("CALL FTL_CREATE_INDEX('"+submissionDB+"', '"+moduleTable+"', 'desc')");
  }
  catch (Exception e)
  {
  }

  
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

 @Override
 public List<SubmissionImprint> getSubmissions(SubmissionQuery q)
 {
  String query = q.getQuery();
  
  if( query != null )
  {
   query = query.trim();
   if( query.length() == 0 )
    query = null;
  }
  
  boolean needJoin = query != null || q.getModuleID() != null || q.getModifiedFrom() != -1 || q.getModifiedTo() != -1 || q.getModifier() != null;
  
  
  StringBuilder sql = new StringBuilder(800);
  
//  sql.append("SELECT S.id FROM ").append(submissionDB).append('.').append(submissionTable).append(" S, ")
//  .append(submissionDB).append('.').append(moduleTable).append(" M");
  
  sql.append("SELECT distinct S.* FROM "+submissionDB+'.'+submissionTable+" S");
  
  if( needJoin )
   sql.append(" INNER JOIN "+submissionDB+'.'+submissionTable+" M ON S.id=M.submid");
  
  if( q.getQuery() != null && q.getQuery().length() > 0 )
  {
   sql.append(", FTL_SEARCH_DATA('");
   StringUtils.appendEscaped(sql, q.getQuery(), '\'', '\'');
   sql.append("', 0, 0) FT");
  }
  

  //select * from FTL_SEARCH_DATA('Hello seva world', 0, 0) FT join tbl S ON  S.ID=FT.KEYS[0] left join stbl M on S.id=m.pid  where  FT.TABLE='TBL' and m.txt like '%va%';
  //select distinct S.id from  tbl S left join stbl M on S.id=M.pid join FTL_SEARCH_DATA('Seva world', 0, 0) FT ON ( FT.TABLE='TBL' AND S.ID=FT.KEYS[0] ) OR (  FT.TABLE='STBL' AND M.ID=FT.KEYS[0]) limit 1,1
  //SELECT T.*,S.* FROM FTL_SEARCH_DATA('Hello AND external', 0, 0) FT, AAA.TBL T, AAA.STBL S WHERE ( FT.TABLE='TBL' AND T.ID=FT.KEYS[0] ) OR (  FT.TABLE='STBL' AND S.ID=FT.KEYS[0] AND S.PID=T.ID)

  
  return null;
 }
}
