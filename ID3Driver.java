import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/*
 *Driver program for Decision tree creation using ID3 Algorithm and Random Attribute method
 * 
 */

/**
 * @author Malini Kottarappatt Bhaskaran
 *
 */
public class ID3Driver{
	//Driver program to get the file names and perform ID3
public static void main(String[] args) throws Exception {
// Triggers ID3 Algorithm Implementation
		Scanner in;
		int nodesPrune = 0;
		String trainFile = new String();
		String validFile = new String();
		String testFile = new String();
		int out = 1; // Boolean variable to indicate whether to print the model

		// Input of file
		if (args.length > 0) {
			nodesPrune = Integer.parseInt(args[0]);
			trainFile = args[1];
			validFile = args[2];
			testFile = args[3];
			out = Integer.parseInt(args[4]);
		} else {
			in = new Scanner(System.in);
			System.out.println("Enter the number of nodes to prune");
			nodesPrune = in.nextInt();
			//nodesPrune= 10;
			System.out.println("Training File Path");
			trainFile = in.next();
			//trainFile = "D:/UTD_courses/MachineLearning/Assignment2/ID3Implementation/src/training_set.csv";
			System.out.println("Validating File path");
			validFile = in.next();
			//validFile = "D:/UTD_courses/MachineLearning/Assignment2/ID3Implementation/src/validation_Set.csv";
			System.out.println("Test File path");
			testFile = in.next();
			//testFile = "D:/UTD_courses/MachineLearning/Assignment2/ID3Implementation/src/test_Set.csv";
			System.out.println("Print the model or not 0-No print 1-Print");
			out = in.nextInt();
			
		}

		DecTree dt = new DecTree();
        try {
		     //Reads input data from the files and fills it to the arrays
			GetEntropy.getData(trainFile,validFile,testFile);
			// Build Decision Tree Using ID3
			ID3Util.BuildTree(DecTree.getTrainData(), DecTree.getAttribute(), 1);
			/*if (out == 1) {
				dt.print("");
				dt.print("Decision Tree using ID3 Algorithm:Before Pruning");
				dt.printDecTree(1, "", "");

			}*/
			Map<Integer,String> ID3Tree = new HashMap<Integer,String>();
			ID3Tree.putAll(DecTree.getDecTree());
			//Check accuracy of tree using ID3 before pruning
			double accuracyBeforePrune = ID3Util.testDT(DecTree.getDecTree(), DecTree.getValidData());
			// Prune Decision Tree
			DecTree.setDecTree(ID3Util.pruneDecTree(nodesPrune));
			/*if (out == 1) {
				dt.print(" ");
				dt.print("Decision Tree using ID3: After Pruning");
				dt.printDecTree(1, "", "");
			}*/
			Map<Integer,String> ID3PruneTree = new HashMap<Integer,String>();
			ID3PruneTree.putAll(DecTree.getDecTree());
			//Test accuracy after prune 
			double accuracyAfterPrune = ID3Util.testDT(DecTree.getDecTree(), DecTree.getValidData());
			System.out.println("********************************************************************************************");
			System.out.println("Accuracy on Validation Data for ID3");
			System.out.println("********************************************************************************************");
			System.out.println(
					"Decision Tree using ID3 Before Pruning: Accuracy =  " + accuracyBeforePrune);
			System.out.println(
					"Decision Tree using ID3 Ater Pruning:Accuracy = " + accuracyAfterPrune);
			
			double accTestData = 0;
			if(accuracyAfterPrune>accuracyBeforePrune)
			{//After pruning Accuracy is good. So report accuracy using Pruned tree
				accTestData = ID3Util.testDT(ID3PruneTree, DecTree.getTestData());				
				DecTree.setDecTree(ID3PruneTree);
				if (out == 1) {
					dt.print(" ");
					dt.print("Decision Tree using ID3");
					dt.printDecTree(1, "", "");
				}
				System.out.println("Depth of tree created using ID3:"+ID3Util.calculateDepth(ID3Tree));
					
			}
			else
			{
				accTestData = ID3Util.testDT(ID3Tree, DecTree.getTestData());
				DecTree.setDecTree(ID3Tree);
				if (out == 1) {
					dt.print(" ");
					dt.print("Decision Tree using ID3");
					dt.printDecTree(1, "", "");					
				}
				System.out.println("Depth of tree created using ID3:"+ID3Util.calculateDepth(ID3Tree));
				
				
			}
			
			System.out.println("Accuracy of Decision Tree Using ID3 :"+accTestData);
			
			System.out.println("          ");
			System.out.println("**********Decision Tree using Random Attribute Selection***************");
			//To Build Decision Tree without using ID3
			RandomAttribute.BuildDecTree(DecTree.getTrainData(),DecTree.getAttribute(),1);
			double accRandom  = ID3Util.testDT(DecTree.getDecTree(), DecTree.getValidData());
			System.out.println("********************************************************************************************");
			System.out.println("Accuracy on Validation Data for Decision tree using Random Attribte");
			System.out.println("********************************************************************************************");
			System.out.println("Accuracy in random case Before Pruning: "+accRandom);
			/*if (out == 1) {
				dt.print("");
				dt.print("Decision Tree using Random attribute");
				dt.printDecTree(1, "", "");

			}*/
			
			Map<Integer,String>ID3RandomTree = new HashMap<Integer,String>();
			ID3RandomTree.putAll(DecTree.getDecTree());
			
			DecTree.setDecTree(ID3Util.pruneDecTree(nodesPrune));
			Map<Integer,String> ID3PruneRandomTree = new HashMap<Integer,String>();
			ID3PruneRandomTree.putAll(DecTree.getDecTree());
			
			//Test accuracy after prune 
			double accAfterRandPrune = ID3Util.testDT(ID3PruneRandomTree, DecTree.getValidData());
			System.out.println("Accuracy of random selection tree after pruning :"+accAfterRandPrune);
			double accTestRandom = 0;
			if(accRandom>accAfterRandPrune)
			{//After pruning Accuracy is good. So report accuracy using Pruned tree
				accTestRandom = ID3Util.testDT(ID3RandomTree, DecTree.getTestData());
				DecTree.setDecTree(ID3RandomTree);
				if (out == 1) {
					dt.print("");
					dt.print("Decision Tree using Random attribute");
					dt.printDecTree(1, "", "");

				}
				System.out.println("Depth of tree created using random attribute method :"+ID3Util.calculateDepth(ID3RandomTree));
							
			}
			else
			{
				accTestRandom = ID3Util.testDT(ID3PruneRandomTree, DecTree.getTestData());	
				DecTree.setDecTree(ID3PruneRandomTree);
				if (out == 1) {
					dt.print("");
					dt.print("Decision Tree using Random attribute");
					dt.printDecTree(1, "", "");
				}
				 System.out.println("Depth of tree created using random attribute method :"+ID3Util.calculateDepth(ID3PruneRandomTree));
					
			}
			
			System.out.println("Accuracy of Tree using Random Attribute Selection: "+accTestRandom);
					
			
		} catch (Exception e) {
			System.out.println("Invalid Arguments/File name incorrect/Please check the exception");
			throw e;
		} 
	}
}
