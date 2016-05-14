import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/*
 *Class for implementing DecTree creation using RAndom Attribute method
 * 
 */

/**
 * @author Malini Kottarappatt Bhaskaran
 *
 */
public class RandomAttribute {
	
	//This class implements the selection of random attribute as the first node. 

	static Integer classID = (Integer) DecTree.Attribute.keySet().toArray()[DecTree.Attribute.size()-1];
	
	public static void BuildDecTree(int[][] data, Map<Integer, String> attribute,int posNode) {
		// TODO Auto-generated method stub
		
		if (posNode == 1) {
			DecTree.setDecTree(new HashMap<Integer, String>());
		}
		int atListSize =0;
	
		   atListSize = attribute.size()-1;
		//System.out.println("ArrayList Size "+atListSize);
		Random ran = new Random();
		double rootEntropy = GetEntropy.calcEntropy(data);
		
		if (rootEntropy == 0){
			DecTree.getDecTree().put(posNode,""+data[0][data[0].length-1]);}
		else if (attribute.size() == 1) 
		{
			int lneg = 0, lpos = 0;
			for (int i = 0; i < data.length; i++) {
				if (data[i][data[i].length-1] == 0)
				{
					lneg++;
				}
				else
				{
					lpos++;
				}
			}
			if (lneg > lpos){
				DecTree.getDecTree().put(posNode, "0");
				}
			else{
				DecTree.getDecTree().put(posNode, "1");
				}		
	    }
		else
		{
			//Select a  random attribute
			int leftNodes =0,rightNodes =0, majority=0;			
			int randomAttr = ran.nextInt(atListSize);
			if(!attribute.containsKey(randomAttr))
            {
				for(Integer val:attribute.keySet())
				{
					if(!val.equals(20)) //To avoid random Attr as 20 -Class label
					{
						randomAttr = (int)val;
					}
				}	            		
	        }
			
			DecTree.getDecTree().put(posNode,attribute.get(randomAttr));
			DecTree.nodeMajority.put(posNode,majority);	
							
					int zero = 0, one = 0, zerozero = 0, zeroOne = 0, oneZero = 0, oneOne = 0;
					for(int j = 0; j < data.length; j++) 
					{
						if(data[j][randomAttr] == 0) {
							zero++;
							//Check if the output is zero
							if(data[j][(data[j].length - 1)] == 0) {
								zerozero++;
							} else {
								zeroOne++;
							}
						} else {
							one++;
							if(data[j][(data[j].length - 1)] == 0) {
								oneZero++;
							} else {
								oneOne++;
							}
						}
					}
					
					 leftNodes = zero;
					 rightNodes = one;
					 majority = ((zerozero + oneZero) > (zeroOne + oneOne)) ? 0 : 1;		  
						
		//	System.out.println("Right nodes "+rightNodes+" leftNode "+leftNodes);
			
            int[][] leftChild  = new int[leftNodes][data[0].length];
            int[][] rightChild = new int[rightNodes][data[0].length];
		
			Map<Integer,String> leftHeadRow = (Map<Integer,String>)((HashMap<Integer,String>)attribute).clone();
			leftHeadRow.remove(randomAttr);
			
			Map<Integer,String> rightHeadRow = (Map<Integer,String>)((HashMap<Integer,String>)attribute).clone();
            rightHeadRow.remove(randomAttr);
            
            int lftArrCnt =0,rghtArrCnt =0;
            for(int i=0;i<data.length;i++)
            {   
            	if(data[i][randomAttr]==0)
            	{
            		for(int j=0;j<(data[i].length);j++)
            		{
            		   leftChild[lftArrCnt][j]=data[i][j];
               		}
            		lftArrCnt++;
            	}
            	else
            	{
            		for(int j=0;j<(data[0].length);j++)
            		{  
            			
            			rightChild[rghtArrCnt][j]=data[i][j];            			
            		}
            		rghtArrCnt++;
            	}
            
            }
            if(leftNodes==0){            
            	DecTree.getDecTree().put(posNode*2,""+majority);}
            
            else{        
            		BuildDecTree(leftChild,leftHeadRow,posNode*2);
            	}
            
            if(rightNodes==0){            	
               DecTree.getDecTree().put(posNode*2+1,""+majority);
               }
            else{
            	BuildDecTree(rightChild, rightHeadRow, posNode*2+1);
			
            }
		}

	}
	
}
