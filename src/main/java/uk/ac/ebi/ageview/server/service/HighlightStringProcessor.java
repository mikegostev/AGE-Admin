package uk.ac.ebi.ageview.server.service;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.highlight.Highlighter;

import uk.ac.ebi.age.ui.server.imprint.StringProcessor;

public class HighlightStringProcessor implements StringProcessor
{
 private final Analyzer analizer = new StandardAnalyzer();

 private Highlighter  hlighter;
 
 public HighlightStringProcessor( Highlighter hl )
 {
  hlighter=hl;
 }
 
 @Override
 public String process(String str)
 {
  return highlight(hlighter, str);
 }

 private String highlight( Highlighter hlighter, Object str )
 {
  if( str == null )
   return null;
  
  String s = str.toString();

  if( hlighter == null )
   return s;
  
  if( s.startsWith("http://") )
   return s;
  
  try
  {
   String out = hlighter.getBestFragment(analizer, "", s);
   
   if( out == null )
    return s;
   
   return out;
  }
  catch(Exception e)
  {
  }
 
  return s;
 }
}
