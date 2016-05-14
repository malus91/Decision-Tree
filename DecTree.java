import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
 *Class for Decision Tree.
 *Getter setter Methods and main attributes 
 * 
 */

/**
 * @author Malini Kottarappatt Bhaskaran
 *
 */

public class DecTree {
	/**
	 * Class for DecTree Attributes and print function
	 */
	public static int[][] trainData, testData, validData;
	public static Map<Integer, String> decTree   = new HashMap<Integer, String>();
	public static Map<Integer, String> Attribute = new HashMap<Integer, String>();
	public static Map<String, Integer> AttrMap   = new HashMap<String, Integer>();
	//HashMap to store the majority class details for each node
	public static Map<Integer,Integer> nodeMajority = new HashMap<Integer,Integer>();
	
   
	//Setters and Getters for the attributes in the class
	public static Map<Integer, String> getDecTree() {
		return decTree;
	}

	public static void setDecTree(Map<Integer, String> decTree) {
		DecTree.decTree = decTree;
	}

	public static int[][] getTrainData() {
		return trainData;
	}

	public static void setTrainData(int[][] trainData) {
		DecTree.trainData = trainData;
	}

	public static int[][] getTestData() {
		return testData;
	}

	public static void setTestData(int[][] testData) {
		DecTree.testData = testData;
	}

	public static int[][] getValidData() {
		return validData;
	}

	public static void setValidData(int[][] validData) {
		DecTree.validData = validData;
	}

	public static Map<Integer, String> getAttribute() {
		return Attribute;
	}

	public static void setAttribute(Map<Integer, String> Attribute) {
		DecTree.Attribute = Attribute;
	}

	public static Map<String, Integer> getAttrMap() {
		return AttrMap;
	}

	public static void setAttrMap(Map<String, Integer> AttrMap) {
		DecTree.AttrMap = AttrMap;
	}

    //Print util function to display a string
	public void print(String str) throws IOException {
		System.out.println(str);		
	}
    //For printing the Tree
	public void printDecTree(int posNode, String position, String cond) throws IOException {
	    //Print as childs
		if (getDecTree().containsKey(posNode)) {
			if (!getDecTree().containsKey(2 * posNode) && !getDecTree().containsKey(2 * posNode + 1)) {
				String str = position.substring(1) + cond + getDecTree().get(posNode);
				System.out.println(str);			
			} else {
				System.out.println((position.length() > 0 ? position.substring(1) : position) + cond);			
				printDecTree(posNode * 2, position + "| ", getDecTree().get(posNode) + "= 0 :");
				printDecTree(posNode * 2 + 1, position + "| ", getDecTree().get(posNode) + "= 1 :");
			}
		}

	}

}
