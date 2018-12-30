package running;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import astextractor.ASTExtractor;
import astextractor.ASTExtractorProperties;
import astparser.JavaASTParser;

public class Test {

	private ArrayList<String> listJavaFiles = new ArrayList<String>();
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ASTExtractorProperties.setProperties("");
		Test ts = new Test();
		
		//String ast = ASTExtractor.parseFile("/Volumes/Setup/JavaWorkSpace/cloneGitHubPJ/clonedProject/abdera/adapters/filesystem/src/main/java/org/apache/abdera/protocol/server/adapters/filesystem/FilesystemAdapter.java");
		ts.readFileToGetAllJavaFiles("javaFilesNew.txt");
		System.out.println(ts.getListJavaFiles().size());
		int n = ts.getListJavaFiles().size();
		int count = 0;
		int begin = n/250*count;
		int end = n/250*(count+1);
		//ASTExtractor.parseFile(ts.getListJavaFiles().get(0));
		//6-7:291033
		try {
			for (int i = begin ; i < end; i++) {
				System.out.println("Parsing files: " + i + "/" + end + "\t" + ts.getListJavaFiles().get(i));
				ASTExtractor.parseFile(ts.getListJavaFiles().get(i));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(JavaASTParser.getListMethodsBody().size());
		//System.out.println(JavaASTParser.getListTokenizedMethodsName());
		//System.out.println(JavaASTParser.getListMethodsBody());
		ts.writeMethodsBodyToFile("MethodBody-" + count + "-" + (count+1) + ".xml");
		ts.writeMethodsNameToFile("MethodsName-" + count + "-" + (count+1) + ".ja");
		ts.writeExecutedFiles("TraversedFiles.txt");
	}
	
	public ArrayList<String> getListJavaFiles(){
		return listJavaFiles;
	}
	
	/**
	 * write tokenized method names File
	 * */
	public void writeExecutedFiles(String fileName) {
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {

			System.out.println("Starting to write executed into file: " + fileName + "\n");
			//String content = "This is the content to write into file\n";

			fw = new FileWriter(new File(fileName));
			bw = new BufferedWriter(fw);
			
			for (String s : listJavaFiles) {
				bw.write(s + "\n");
			}
			//bw.write(content);

			System.out.println("Done");

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}

	}
	
	/**
	 * write tokenized method names File
	 * */
	public void writeMethodsBodyToFile(String fileName) {
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {

			System.out.println("Starting to write into file: " + fileName + "\n");
			//String content = "This is the content to write into file\n";

			fw = new FileWriter(new File(fileName));
			bw = new BufferedWriter(fw);
			
			bw.write("<XMLFile>\n");
			for (int i=0 ; i<JavaASTParser.getListMethodsBody().size() ; i++) {
				bw.write(JavaASTParser.getListMethodsBody().get(i));
			}
			bw.write("</XMLFile>\n");
			//for (String s : listBodies) {
			//	bw.write(s + "\n");
			//}
			//bw.write(content);

			System.out.println("Done");

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}

	}
	
	/**
	 * write tokenized method names File
	 * */
	public void writeMethodsNameToFile(String fileName) {
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {

			System.out.println("Starting to write MethodName into file: " + fileName + "\n");
			//String content = "This is the content to write into file\n";
			
			fw = new FileWriter(new File(fileName));
			bw = new BufferedWriter(fw);
			
			for (int i=0 ; i<JavaASTParser.getListTokenizedMethodsName().size() ; i++) {
				for (int j=0 ; j<JavaASTParser.getListTokenizedMethodsName().get(i).size()-1 ; j++) {
					if (JavaASTParser.getListTokenizedMethodsName().get(i).get(j).toString().equals("")) continue;
					bw.write(JavaASTParser.getListTokenizedMethodsName().get(i).get(j) + " ");
				}
				String temp = JavaASTParser.getListTokenizedMethodsName().get(i).get(JavaASTParser.getListTokenizedMethodsName().get(i).size()-1);
				temp = temp.replace("\n", "");
				//System.out.println("The last word: " + temp + "~.");
				bw.write(temp + " .\n");
				//bw.write(".");
			}

			System.out.println("Done");

		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			try {

				if (bw != null)
					bw.close();
				
				if (fw != null)
					fw.close();
			} catch (IOException ex) {

				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * Read file
	 * */
	public void readFileToGetAllJavaFiles(String fileName) {
		BufferedReader br = null;
		FileReader fr = null;

		try {

			//br = new BufferedReader(new FileReader(FILENAME));
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				listJavaFiles.add(sCurrentLine);
				//System.out.println(sCurrentLine);
				//aSequence += insertEnterChar(sCurrentLine);
				//aSequence += rewriteXMLCode(sCurrentLine);
				//listBodies.add(rewriteXMLCode(sCurrentLine));
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
	}
	
}

