package file_operation;

import java.io.*;
import java.util.*;
/*
 * Written 16 September 2015 by Adriano De Gouveia
 * ReadFiles class has two member functions namely scanning a folder and reading in the file names of all the .template files then returning
 * an ArrayList containing strings of these template files
 * Secondly it has a member functions that extract data from these template files namely the label and value for each parameter and stores them in
 * accompanying array lists
 */

public class ReadTemplateFiles {
	private ArrayList<String> GlobalValuesList = new ArrayList<String>();
	private ArrayList<String> GlobalLabelsList = new ArrayList<String>();
	private ArrayList<String> SpecificValuesList = new ArrayList<String>();
	private ArrayList<String> SpecificLabelsList = new ArrayList<String>();
	private ArrayList<String> RulesList = new ArrayList<String>();
	private String antennaName = null;
	private ArrayList<String> fileNameList = new ArrayList<String>();
	
	//Function that scans the files of a folder with an extension .template and adds their names into an ArrayList
	public ArrayList<String> ReadFileNamesInFolder(){
		String currentDir = System.getProperty("user.dir");
	    File folder = new File(currentDir);
		File[] listOfFiles = folder.listFiles();
		//create arraylist to store file names
		for (File file : listOfFiles) {
		    if (file.isFile() && file.getName().contains(".template")) {
		        fileNameList.add(file.getName());
		    }
		}	
		return fileNameList;		
	}
	
	//this function must take a .template file and strips its contents to reveal the labels that must be imported into the solver GUI
	public void getFileValues(String filename){
		//firstly before executing this function ensure all of the containers are empty because we are going to read data from a new template file
		antennaName = null;
		GlobalValuesList.clear();
		GlobalLabelsList.clear();
		SpecificLabelsList.clear();
		SpecificValuesList.clear();
		RulesList.clear();
		FileInputStream fstream = null;
		try {
			fstream = new FileInputStream(filename);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		String strLine;
		//Read File Line By Line
		try {
			while ((strLine = br.readLine()) != null){
			  //this line is here to perform a check on the number of elements the line is split into for specific parameters
			  String[] checkContent = strLine.split(":");
			  //hash denotes the name of the antenna
			  if (strLine.contains("#")){
				  //remove hash and set this equal to the antenna name field
				  String name2 = strLine.split("<-")[0];
				  String name = name2.substring(1);
				  //remove whitespace from antenna name
				  name = name.replaceAll("\\s+", "");
				  antennaName = name;
			  }
			  //extract global variables from file
			  else if (strLine.contains("Frequency=") || (strLine.contains("Ground="))){
				  String label = strLine.split("=")[0];
				  GlobalLabelsList.add(label);
				  String value = strLine.split("=")[1];
				  //remove comments from lines that have comments by splitting where a comment is seen then cycling through each split string
				  //and only taking the line that isn't a comment i.e. the first element
				  if(value.contains("<-")){
					  String value2 = value.split("<-")[0];
					  GlobalValuesList.add(value2);
				  }
				  //if it has no comment great then just add the raw value to the list
				  else {
					  GlobalValuesList.add(value);
				  }
			  }
			  //up to 30 specific antenna variables (intentional design choice as to reduce antenna complexity)
			  else if (strLine.contains("V") && (strLine.contains("0") || (strLine.contains("1"))) && (strLine.contains(":")) && (checkContent.length == 3)){
				  String specificLabel = strLine.split(":")[1];
				  SpecificLabelsList.add(specificLabel);
				  String specificValue = strLine.split(":")[2];
				  if(specificValue.contains("<-")){
					  String value2 = specificValue.split("<-")[0];
					  SpecificValuesList.add(value2);
				  }
				  else {
					  SpecificValuesList.add(specificValue);
				  }
			  }
			  else if (strLine.contains("$$")){
				  String cutDollar = strLine.substring(2);
				  RulesList.add(cutDollar);
			  }
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<String> getGlobalValuesList() {
		return GlobalValuesList;
	}
	
	public String getAntennaName() {
		return antennaName;
	}

	public ArrayList<String> getGlobalLabelsList() {
		return GlobalLabelsList;
	}
	
	public ArrayList<String> getSpecificValuesList() {
		return SpecificValuesList;
	}

	public ArrayList<String> getSpecificLabelsList() {
		return SpecificLabelsList;
	}
	
	public ArrayList<String> getRulesList() {
		return RulesList;
	}
	
	public void setSpecificValuesList(ArrayList<String> _specificValuesListReceived) {
		SpecificValuesList = _specificValuesListReceived;
	}
	
	public void setSpecificLabelsList(ArrayList<String> _specificLabelsListReceived) {
		SpecificLabelsList = _specificLabelsListReceived;
	}
}
