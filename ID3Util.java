import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/*
 *Class for DecTree building using ID3 Algorithm
 * 
 */

/**
 * @author Malini Kottarappatt Bhaskaran
 *
 */
public class ID3Util {
	/*
	 *Class to implement ID3 Algorithm and Related functions
	 * 
	 */
	
	public static void BuildTree(int[][] dat, Map<Integer, String> attributes, int posNode)
			throws Exception {
		//Function Build Tree for building the Decision tree using ID3
		
		//In the first call, create a new Decision tree
		if (posNode == 1) {
			DecTree.setDecTree(new HashMap<Integer, String>());
		}
		// Calculate the root Entropy (Class Label)
		double rootEntropy = GetEntropy.calcEntropy(dat);
        //If root Entropy is zero          
		if (rootEntropy == 0)
			DecTree.getDecTree().put(posNode,""+dat[0][dat[0].length-1]);
		//If only one attribute
		else if (attributes.size() == 1) {
			int lneg = 0, lpos = 0;
			//Count negative and positive values (0 and 1)
			for (int i = 0; i < dat.length; i++) {
				if (dat[i][dat[i].length-1] == 0)
				{
					lneg++;
				}
				else
				{
					lpos++;
				}
			}
			//Assign majority to the tree
			if (lneg > lpos){
				DecTree.getDecTree().put(posNode, "0");}
			else{
				DecTree.getDecTree().put(posNode, "1");}
		} 
		else 
		{
			// Initialize gain and entropy values
			double maxGain = -2;
			int maxGainAttr = -1;
			int maxGainLftNode = -1;
			int maxGainRghtNode = -1;
			int maxGainMajority = -1;
			for (int i = 0; i < dat[0].length - 1; i++) {
				//Check if i is already used
				if (attributes.containsKey(i))
				{
					int zero = 0, one = 0, zerozero = 0, zeroOne = 0, oneZero = 0, oneOne = 0;
					for(int j = 0; j < dat.length; j++) {
						if(dat[j][i] == 0) {
							zero++;
							if(dat[j][dat[j].length - 1] == 0) {
								zerozero++;
							} else {
								zeroOne++;
							}
						} else {
							one++;
							if(dat[j][dat[j].length - 1] == 0) {
								oneZero++;
							} else {
								oneOne++;
							}
						}
					}
					double negEntropy = 0, posEntropy = 0, entropy = 0,infGain = 0;
					//Get entropy for attribute i
					negEntropy = GetEntropy.calcEntropy(zerozero, zeroOne, zero);
					posEntropy = GetEntropy.calcEntropy(oneZero, oneOne, one);
					entropy = ((double)zero/(zero+one))*negEntropy + ((double)one/(zero+one))*posEntropy;
					infGain = rootEntropy - entropy;
				//Compare with maxGain to get the max Gain attribute	
				if (infGain > maxGain) 
				{
					maxGain = infGain;
					maxGainAttr = i;
					maxGainLftNode = zero;
					maxGainRghtNode = one;
					maxGainMajority = ((zerozero + oneZero) > (zeroOne + oneOne)) ? 0 : 1;
				}
			  }
			}
			//Add maxGainAttribute to the tree at PosNode
			DecTree.getDecTree().put(posNode,attributes.get(maxGainAttr));
			//Add Majority class details to the hashmap for pruning purposes.
			DecTree.nodeMajority.put(posNode,maxGainMajority);	
			//Initialize left and Right childs
			int[][] leftChild = new int[maxGainLftNode][dat[0].length],rightChild = new int[maxGainRghtNode][dat[0].length];
			//Clone attribute Map
			Map<Integer,String> leftHeadRow = (Map<Integer,String>)((HashMap<Integer,String>)attributes).clone();
			//Remove used attribute from the Map
			leftHeadRow.remove(maxGainAttr);
			//Same as Left child
		    Map<Integer,String> rightHeadRow = (Map<Integer,String>)((HashMap<Integer,String>)attributes).clone();
            rightHeadRow.remove(maxGainAttr);
            int lftArrCnt =0,rghtArrCnt =0;
            //Assign left child data and right child data
            for(int i=0;i<dat.length;i++)
            {
            	if(dat[i][maxGainAttr]==0)
            	{
            		for(int j=0;j<dat[i].length;j++)
            		{
            			leftChild[lftArrCnt][j]=dat[i][j];
            		}
            		lftArrCnt++;
            	}
            	else
            	{
            		for(int j=0;j<dat[0].length;j++)
            		{
            			rightChild[rghtArrCnt][j]=dat[i][j];            			
            		}
            		rghtArrCnt++;
            	}
            }
            //If leftNodes for Max Gain Attribute is zero, end of tree and assign as leaf Node
            if(maxGainLftNode==0){            
            	DecTree.getDecTree().put(posNode*2,""+maxGainMajority);
            }            
            else
            {     //Call Build tree recursively for left child   
            		BuildTree(leftChild, leftHeadRow,posNode*2);
            	
            }
            //If Number of right nodes for maxgain attribute is zero, then assign as leafnodes.
            if(maxGainRghtNode==0)
            {
               DecTree.getDecTree().put(posNode*2+1,""+maxGainMajority);
            }
            else
            {
            	//Call Build Tree for the right subtree
            	    	BuildTree(rightChild, rightHeadRow, posNode*2+1); 
            }    
            
		}

	}

	public static double testDT(Map<Integer, String> decTree, int[][] testData) {
		//Test the accuracy of the tree generated using Test Data		
		boolean isCorrect = false;
		int correctOut =0,wrongOut =0;
		for(int i=0;i<testData.length;i++)
		{
			//Check correctness
			isCorrect = checkOutput(decTree,testData[i]);
			if(isCorrect)
				correctOut++;			
			else
				wrongOut++;
		}
		//return Accuracy in Percentage
		return ((double)correctOut/(correctOut+wrongOut))*100;
	}

	private static boolean checkOutput(Map<Integer, String> decTree, int[] testDataRow) {
       //Verify the decision with tree result
		int posNode =1;
		//Get to leaf Nodes
       while(!decTree.get(posNode).equals("0")&&!decTree.get(posNode).equals("1"))
       {
    	  //Get the leafNode index in posnode
    	  posNode = testDataRow[DecTree.AttrMap.get(decTree.get(posNode))]==0?posNode*2: posNode*2+1;
       }
       //Check the decision tree leaf value with test data
       if(decTree.get(posNode).equals(""+testDataRow[testDataRow.length-1]))
    	   return true;  //Correct
       else
       {
    	   return false; //Wrong
       }
		
	}

	public static Map<Integer, String> pruneDecTree(int nodesPrune) {
		//Performs pruning in Decision Tree
		Map<Integer,String> dtID3 = new HashMap<Integer,String>();
		//Copy of original decision tree using ID3 algorithm
		dtID3.putAll(DecTree.getDecTree());
		//check accuracy using validation data
		//double treeAccuracy = testDT(dtID3,DecTree.getValidData());		
		//System.out.println("Accuracy for the validation data "+treeAccuracy);
		List<Integer> nonLeafNodes = new ArrayList<Integer>();
	    int nonLeaf =0;
	    int leafNodes =0;
	    Iterator<Integer> nodes =dtID3.keySet().iterator();
	    //Get the non leaf nodes other than at level 1 and 2
		while(nodes.hasNext())
		{
				Integer key = nodes.next();
				String value = dtID3.get(key);
				//To get the non leaf nodes to the ArrayList
				if(!("0".equals(value)||"1".equals(value)))
				{
					nonLeaf++;		
					//To avoid adding the root node and its immediate childs to the arraylist for pruning 
					if(key!=1&&key!=2&&key!=3)
					 nonLeafNodes.add(key);
				}
				
		}
		//Boundary for Prune number
		int pruneListSize  = nonLeafNodes.size();
		Random ran = new Random();
		int idxToPrune =0;
		int posPruning =0;
		int majorClass =0;
		//Perform for number of nodes to prune
		for(int i=0;i<nodesPrune;i++)
		{
			if(pruneListSize>0)
			{
				//Index to Prune in nonLEaf nodes List
				idxToPrune = ran.nextInt(pruneListSize);
				//Position to Prune in Tree
				posPruning = nonLeafNodes.get(idxToPrune);
				//Get the majority class value
				majorClass = DecTree.nodeMajority.get(posPruning);
				//Add node to tree with majority class
				dtID3.put(posPruning,""+majorClass);
				//Remove pruned node from nonLeafNodes List
				nonLeafNodes.remove((Integer)posPruning);
				//Prune the subtree for the pruned node
				pruneSubTree(posPruning,dtID3,nonLeafNodes);
				//Update PruneListSize
				pruneListSize  = nonLeafNodes.size();
			}
		}			
		return dtID3;
	}

	private static void pruneSubTree(int nodeChosen, Map<Integer, String> dtID3, List<Integer> nonLeafNodes) {
		// Remove left and right subtree of the pruned node		
		if(dtID3.containsKey(2*nodeChosen))
		{
			dtID3.remove(2*nodeChosen);
			nonLeafNodes.remove((Integer)(2*nodeChosen));
			pruneSubTree(2*nodeChosen, dtID3,nonLeafNodes);  //Recursive call to subsequent deletion
			
		}
		if(dtID3.containsKey(2*nodeChosen+1))
		{
			dtID3.remove(2*nodeChosen+1);
			nonLeafNodes.remove((Integer)(2*nodeChosen+1));
			pruneSubTree((2*nodeChosen+1), dtID3,nonLeafNodes);//Recursive call to subsequent deletion
			
		}

	}
	
	public static int calculateDepth(Map<Integer,String> decTree)
	{
		int leaf =0;
		
		int nodesNum =decTree.size();
		   int height =0;
		   
		   int nonLeaf =0;
	     
		   Iterator<Integer> nodes =decTree.keySet().iterator();
		    //Get the non leaf nodes other than at level 1 and 2
			while(nodes.hasNext())
			{
			   String value = decTree.get(nodes.next());
	    	 if("0".equals(value)||"1".equals(value))
				{
					leaf++;	
				}
	    	 else
	    	 {
	    		 nonLeaf++;
	    	 }
	    	 
		    }
	     int arrDepth[] = new int[leaf];
	     int j=0;
	     
	     System.out.println("Number of Nodes in Dec Tree = "+(leaf+nonLeaf));
	   
	     Iterator<Integer> nodeTree =decTree.keySet().iterator();
		    //Get the non leaf nodes other than at level 1 and 2
	    int k=0;
		while(nodeTree.hasNext())
		{
		   String value = decTree.get(nodeTree.next());
		   height =0;
	    	 if("0".equals(value)||"1".equals(value))
				{	  
	    		  if(k%2==0)
	    		  {
	    			  //Left Child
					for(int i=k;i>0;i=(i-1)/2)
					{
						height++;
					}
	    		  }
	    		  else
	    		  {
	    			  //Right Child
	    			  for(int i=k;i>0;i=i/2)
						{
							height++;
						}
	    		  }
	    		    height++; //Adding plus one to include the child level depth
					arrDepth[j] = height;
					j++;
				}
	    	 k++;
	   }
	   
	   int sum =0;
	   for(int i=0;i<arrDepth.length;i++)
		  sum=sum+arrDepth[i];	   
	   
	   int avgDepth = sum/leaf;
	     
		return avgDepth;
		
	}
}
