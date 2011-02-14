package uk.ac.ebi.age.admin.server.util;

import java.io.File;

import uk.ac.ebi.age.admin.shared.StoreNode;

public class DirectoryTools
{
 public static StoreNode imprintDirectory( File d )
 {
  if( ! d.isDirectory() )
   return null;
  
  StoreNode dir = new StoreNode(d.getName());
  
  for( File f : d.listFiles() )
   if( f.isDirectory() )
    dir.addSubNode( imprintDirectory(f) );
   else
    dir.addFile( f.getName() );
  
  return dir;
 }
}
