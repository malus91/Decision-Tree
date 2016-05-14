import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

/*
 *Class for reading data from files and calculating entropy values
 * 
 */

/**
 * @author Malini Kottarappatt Bhaskaran
 *
 */
public class GetEntropy {

	public static void getData(String train,String valid,String test) throws IOException {
		//Gets the Filenames and stores the data into 2 dimensional arrays by calling inputData function		
		DecTree.setTrainData(GetEntropy.inputFile(train));  //Sets training data to training data array 
		DecTree.setTestData(GetEntropy.inputFile(test));    //Sets test data to test data array
		DecTree.setValidData(GetEntropy.inputFile(valid));	//Sets validation data to validation data array	
	}

	private static int[][] inputFile(String fileName)throws IOException {
		//Gets the file name and sets the data to arrays		
		int[][] inputData;
		int height=0,width=0;
		String dataRecord = null;
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		
		String header = br.readLine();
		while(br.readLine()!=null)
		{
			height++;
		}
		br.close();
		//tokenize input data 
		//Gets attributes and putting it to attribute map
		StringTokenizer sToken = new StringTokenizer(header,",");
		int i=0;
		String str = null;		
		while(sToken.hasMoreTokens())
		{
			str = sToken.nextToken();
			DecTree.Attribute.put(i,str);
			DecTree.AttrMap.put(str, i++);
		}
		width = DecTree.Attribute.size();
		inputData = new int[height][width];
		//Fill training Data
		br = new BufferedReader(new FileReader(fileName));
				
		br.readLine();
		height =0;
		while((dataRecord=br.readLine())!=null)
		{
			width =0;
			sToken = new StringTokenizer(dataRecord,",");
			while(sToken.hasMoreTokens())
			{
				inputData[height][width++] = Integer.parseInt(sToken.nextToken());
			}
			height++;
		}		
		//Return the data array
		return inputData;
	}

	public static double calcEntropy(int[][] data) {
	//Function Calculates Entropy and returns the entropy value. (RootEntropy)		
		if(data.length==0)			
		return 0;		
		int pos =0, neg =0;
		for(int i=0;i<data.length;i++)
		{
			if(data[i][data[i].length-1]==0)
			 neg++;				
			else
		     pos ++;
		}
		double entropy =0;		
		entropy = (neg==0?0:-(((double)neg/data.length)*Math.log10((double)neg/data.length)/Math.log10(2))) + (pos==0?0:-(((double)pos/data.length)*Math.log10((double)pos/data.length)/Math.log10(2)));
		return entropy;
	}

	public static double calcEntropy(int negCount, int posCount, int totalCount) throws Exception {
    //Overriding Entropy Function (Entropy for attributes)
		double entropy =0;
		if(totalCount==0)		
			return 0;		
		  entropy = (negCount==0?0:-(((double)negCount/totalCount)*Math.log10((double)negCount/totalCount)/Math.log10(2))) + (posCount==0?0:-(((double)posCount/totalCount)*Math.log10((double)posCount/totalCount)/Math.log10(2)));
		return entropy;		
	}

	
}
