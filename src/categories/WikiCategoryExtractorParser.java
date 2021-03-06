package categories;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

import parser.WikiBaseParser;

import dorado.nlp.models.PhraseModel;
import dorado.nlp.models.WordModel;
import filesystem.ParserRunner;


public class WikiCategoryExtractorParser extends WikiBaseParser{

	ArrayList<String> notValidCategories = new ArrayList<String>();
	boolean readText=false;
	boolean readTitle=false;
	//BufferedWriter output;
	
	String title="";
	String text="";

	public WikiCategoryExtractorParser(String outfilename) {
		super(outfilename);
		notValidCategories.add("Véase también");
		notValidCategories.add("Enlaces externos");
		notValidCategories.add("Referencias");
		notValidCategories.add("Bibliografía");
		notValidCategories.add("Notas");
		notValidCategories.add("Fuentes");
		notValidCategories.add("References");
		notValidCategories.add("Referencias citadas");
		notValidCategories.add("Enlaces en otros idiomas");
		
		
		
		
		/*try {
			output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfilename)));
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}*/
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		try {
			if(qName.equals("text")){
				output.write("  <text>");
				output.newLine();
				readText=true;
			}
			else if(qName.equals("title")){
				output.write("  <title>");
				readTitle=true;
			}
			else if(qName.equals("page")){
				output.write(" <page>");
				output.newLine();
			}
			else if(qName.equals("document")){
				output.write("<document>");
				output.newLine();
			}

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		try {
			
			if(qName.equals("text")){
				
				/**
				 * LOGIC HERE
				 *
				 * Modify text variable and write the result
				 */
				
				
				
				//output.write(text);
				output.write("</text>");
				output.newLine();
				//text="";
				readText=false;
				
			}
			else if(qName.equals("page")){
				output.write(" </page>");
				output.newLine();
			}
			else if(qName.equals("title")){
				
				output.write(title+"</title>");
				output.newLine();
				readTitle=false;
				
			}
			else if(qName.equals("document")){
				output.write("</document>");
				output.newLine();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void endDocument() throws SAXException {
		try {
			output.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		try {
			
			if(readTitle){
				title = new String(ch, start, length);
			}
			else if(readText){
				String text = new String(ch, start, length).trim();
				String[] lines = text.split("\n");

				for (String line : lines) {
					if(line.startsWith("==") && line.endsWith("==")){
						System.out.println("'"+line.trim()+"'");
						if(!notValidCategories.contains(line.replaceAll("=", "").trim())){
							output.write(line);
							output.newLine();
						}
					}
				}
				
				/**/
			}
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		/*if(readText){
			String text = new String(ch, start, length);
			//model.addwordsAsText(text, phraseModel);
		}*/
		
		
	}
	
	
	private ArrayList<String> extractRelations(String line) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) throws Exception{
		
		//String id = "25147";   // Perro
		//String id = "409000";  // Gato
		//String id = "25116";  // León
		
		//String id = "5131";  // Bogotá
		//String id = "13527";  // Tokio
		//String id = "2450";  // Quebec
		
		//String id = "53";  // Albert Einstein
		//String id = "18648";  // Pelé
		//String id = "15309";  // Isaac Asimov
		String id = "5692"; //Gabriel García Márquez
		
		
		System.out.println("Activity started...");
		long start = System.currentTimeMillis();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);
		SAXParser parser = factory.newSAXParser();
		WikiCategoryExtractorParser wwc = new WikiCategoryExtractorParser("/home/rdorado/Downloads/wikipedia/test/cat-"+id+".xml");
		parser.parse( "/home/rdorado/Downloads/wikipedia/output/"+id.substring(id.length()-3)+"/"+id+".xml",  wwc);
		//wwc.print();
		
		
		
		//ParserRunner.execute("/home/rdorado/Downloads/wikipedia/output", "/home/rdorado/Downloads/wikipedia/categories", WikiCategoryExtractorParser.class);
	}



}


