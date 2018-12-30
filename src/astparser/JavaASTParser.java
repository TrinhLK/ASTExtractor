package astparser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;

import astextractor.ASTExtractorProperties;

/**
 * Handles the parsing of java files and the extraction of their Abstract Syntax Trees (ASTs).
 * 
 * @author themis
 */
public class JavaASTParser {

	//private static StringBuffer xmlString = new StringBuffer();
	private static StringBuffer methodBody = new StringBuffer();
	
	private static ArrayList<String> listMethodsName = new ArrayList<String>();
	private static ArrayList<ArrayList<String>> listTokenizedMethodsName = new ArrayList<ArrayList<String>>();
	private static ArrayList<String> listBodies = new ArrayList<String>();
	private static String[] ignoredList = {"Javadoc", "SingleMemberAnnotation", "AnonymousClassDeclaration", "TypeDeclarationStatement"}; 
	
	/**
	 * Retrieves the children of an ASTNode.
	 * 
	 * @param node the ASTNode of which the children are retrieved.
	 * @return the children of the given ASTNode.
	 */
	@SuppressWarnings("unchecked")
	private static ArrayList<ASTNode> getChildren(ASTNode node) {
		ArrayList<ASTNode> flist = new ArrayList<ASTNode>();
		List<Object> list = node.structuralPropertiesForType();
		for (int i = 0; i < list.size(); i++) {
			StructuralPropertyDescriptor curr = (StructuralPropertyDescriptor) list.get(i);
			Object child = node.getStructuralProperty(curr);
			if (child instanceof List) {
				flist.addAll((Collection<? extends ASTNode>) child);
			} else if (child instanceof ASTNode) {
				flist.add((ASTNode) child);
			} else {
			}
		}
		return flist;
	}
	
	/**
	 * 
	 * */
	public static boolean checkInIgnoredList(String elem, String[] listIgnore) {
		for (int i=0 ; i<listIgnore.length ; i++) {
			if (elem.equals(listIgnore[i]))
				return true;
		}
		
		return false;
	}
	/**
	 * Recursively visits all nodes of the AST and exports it as an XML StringBuffer.
	 * 
	 * @param result the result as a StringBuffer.
	 * @param indent the indent at the current level.
	 * @param node the current ASTNode that is examined.
	 */
	
	private static void visitNode(StringBuffer result, String indent, ASTNode node) {
		ArrayList<ASTNode> children = getChildren(node);
		String nodeType = ASTNode.nodeClassForType(node.getNodeType()).getSimpleName();
		
		//System.out.println("\n NOTE TYPE: \t" + ASTNode.nodeClassForType(node.getNodeType()).getSimpleName());
		//if (nodeType.equals("Javadoc")) {
		if (checkInIgnoredList(nodeType, ignoredList)) {
			// neu la javadoc thi bo qua
			// Do nothing
		} else if (ASTExtractorProperties.LEAF.contains(nodeType)) {
			
			//System.out.println("\n\nLEAF PROPERTIES\n");
			result.append(indent + "<" + nodeType + ">");
			result.append(node.toString().trim().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;"));
			result.append("</" + nodeType + ">\n");
		} else if (children.size() > 0) {
			
			if (nodeType.equals("MethodDeclaration")) {
				//xmlString.append("\n" + indent + "<" + nodeType + ">\n");
				methodBody.append("\n" + indent + "<" + nodeType + ">\n");
			}
			result.append(indent + "<" + nodeType + ">\n");
			for (ASTNode child : children) {
				if (nodeType.equals("MethodDeclaration")) {
					//get method name
					if (child.nodeClassForType(child.getNodeType()).getSimpleName().equals("SimpleName")) {
						//methodName.append(child.toString().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;") + "\n");
						//System.out.println("Method Name: " + child.toString());
						listMethodsName.add(child.toString().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;") + "\n");
					}
					//if (nodeType.equals("Javadoc") || nodeType.equals("Block")) continue;
					//visitNode(xmlString, indent + "   ", child);
					visitNode(methodBody, indent + "   ", child);
				}
				visitNode(result, indent + "   ", child);
			}
			if (nodeType.equals("MethodDeclaration")) {
				//xmlString.append(indent + "</" + nodeType + ">\n\n");
				methodBody.append(indent + "</" + nodeType + ">");
				listBodies.add(methodBody.toString());
				methodBody = new StringBuffer();
			}
			result.append(indent + "</" + nodeType + ">\n");
		} else {
			
			
			result.append(indent + "<" + nodeType + ">");
			result.append(node.toString().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;"));
			result.append("</" + nodeType + ">\n");
		}
	}
	
	/**
	 * Visits an AST and exports it as an XML string.
	 * 
	 * @param root the root ASTNode of the tree.
	 * @return an XML string representation of the tree.
	 */
	
	//Tach tu o day
	protected static String visitTree(ASTNode root) {
		StringBuffer result = new StringBuffer("");
		visitNode(result, "", root);
		//System.out.println("VISITING TREE.");
//		System.out.println("method \n" + xmlString + "\nMethod \n");
//		for (int i=0 ; i<listMethods.size() ; i++) {
//			System.out.println(getListWords(listMethods.get(i)));
//		}
		listTokenizedMethodsName = tokenizeMethodsName(listMethodsName);
		return result.toString();
	}
	
	/**
	 * Parses the contents of a java file and returns its AST as an XML string.
	 * 
	 * @param str the contents of a java file given as a string.
	 * @return an XML string representation of the AST of the java file contents.
	 */
	public static String parse(String str) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(str.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		return visitTree(cu);
	}

	/***/
	public static ArrayList<Integer> splitWords(String ip) {
		
		ArrayList<Integer> rs = new ArrayList<Integer>();
		for (int i = 0; i < ip.length(); i++) {
		    char c = ip.charAt(i);
		    //System.out.println(Character.isUpperCase(c));
		    if (Character.isUpperCase(c)) {
		    	rs.add(i);
		    }
		}
		
		return rs;
	}
	
	/**
	 * get Method names tokenized
	 * */
	public static ArrayList<String> getListWords(String ip){
		ArrayList<Integer> listUpper = new ArrayList<Integer>();
		ArrayList<String> listString = new ArrayList<String>();
		
		listUpper = splitWords(ip);
		//System.out.println(listUpper);
		if (listUpper.size() == 0) {
			listString.add(ip);
		}else {
			listString.add(ip.substring(0, listUpper.get(0)));
			for (int i=0 ; i<listUpper.size()-1 ; i++) {
				listString.add(ip.substring(listUpper.get(i), listUpper.get(i+1)));
			}
			listString.add(ip.substring(listUpper.get(listUpper.size()-1)));
		}
		
		return listString;
	}
	
	/***/
	public static ArrayList<ArrayList<String>> tokenizeMethodsName(ArrayList<String> ip){
		ArrayList<ArrayList<String>> rs = new ArrayList<ArrayList<String>>();
		
		for (int i=0 ; i < ip.size() ; i++) {
			//ArrayList<String> temp = getListWords(ip.get(i));
			String temp  = ip.get(i).replace("\n", "");
			listTokenizedMethodsName.add(getListWords(temp));
			rs.add(getListWords(ip.get(i)));
		}
		//System.out.println(listTokenizedMethodsName);
		return rs;
	}
	
//	public static StringBuffer getXMLFunctions(){
//		return xmlString;
//	}
	
	public static ArrayList<String> getListMethodsName(){
		return listMethodsName;
	}
	
	public static ArrayList<String> getListMethodsBody(){
		return listBodies;
	}
	
	public static ArrayList<ArrayList<String>> getListTokenizedMethodsName(){
		return listTokenizedMethodsName;
	}
}
