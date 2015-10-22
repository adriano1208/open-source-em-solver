/**
 * Written 10 September 2015 Modified and extended through to October 15
 * Adriano De Gouveia
 * Main GUI class that allows users to add antenna structures to be visualized which allows a NEC file to be generated
 * from the generalized antenna parameters that the user provides from the specified template files.
 * From this NEC file the GUI then allows visualization of the antenna structures. Simulation in the GUI
 * is carried out using the nec2c engine which generates output files that are then visualized once again
 * This front end cumulatively encompasses the Open EM solver application
 */

package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import file_operation.*;
import nec_backend.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JToolBar;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.AbstractAction;
import javax.swing.SwingWorker;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JList;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.CardLayout;
import java.awt.GridBagLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import net.miginfocom.swing.MigLayout;

import javax.swing.JInternalFrame;
import javax.swing.JRadioButton;
import javax.swing.Timer;

import java.awt.GridBagConstraints;

import javax.swing.border.LineBorder;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JTextPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JProgressBar;

public class MainMenu extends JFrame {
	private JPanel BackgroundPane;
	private JTextField StartFreqText;
	private JTextField EndFreqText;
	private JTextField NoStepsText;
	private JTextField textField1;
	private JTextField textField2;
	private JTextField textField3;
	private JTextField textField4;
	private JTextField textField5;
	private JTextField textField6;
	private JTextField textField7;
	private JTextField textField8;
	private JTextField textField9;
	private JTextField textField10;
	private JTextField textField11;
	private JTextField textField12;
	private JTextField textField13;
	private JTextField textField14;
	private JTextField textField15;
	private NecGenerator _necObject;
	private int numberClicks = 0;
	private String _structureName = null;
	private String _structurePath = null;
	
	/// method that sets the current necGenerator Object such that it can be accessed by other members of the class
	public void setNecObject(NecGenerator necObject){
		_necObject = necObject;
	}
	
	/// listmodel to store the antennanames that have been added to the array of structures before adding it to the JList
	DefaultListModel antennaListModel = new DefaultListModel();
	
	///lists that store global and specific parameters entered by the user in a list
	ArrayList<String> UserSetGlobalParameters = new ArrayList<String>();
	ArrayList<String> UserSetSpecificParameters = new ArrayList<String>();
	boolean isParametersValid = true;
	boolean isGlobalParametersValid = true;
	private JTextField StructureName;
	private JTextField RenameText;
	
	/// function which fetches template file from arrayList removes file extension and converts it to a normal array
	public static String[] FetchTemplateNames(ArrayList<String> antennas){
		ArrayList<String> tempList = new ArrayList<String>();
		for(String antenna:antennas){
			tempList.add(antenna);
		}
		String[] namesArray = new String[tempList.size()];
		namesArray = tempList.toArray(namesArray);
	
		return namesArray;
	}

	/// read in the contents of the selected template file and generate the GUI from it with the requisite labels etc
	public static void LoadGlobalGUIfromAntennaType(Object antennaType,ReadTemplateFiles templateObject, 
	JTextField StartFreqText, JRadioButton rdbtnPerfectGround, JRadioButton rdbtnNoGround){
		//firstly we need to read the values from this file
		String antTypeString = antennaType.toString();
		templateObject.getFileValues(antTypeString);
		ArrayList<String> globalValues = templateObject.getGlobalValuesList();
		int count = 0;
		//look inside the global values container and make decisions
		for(String value:globalValues){
			//replace all whitespaces with empty character delimeters
			value = value.replaceAll("\\s+", "");
			//first iteration is first element which is frequency then populate textbox with frequency value
			if(count == 0){
				StartFreqText.setText(value);
			} //if it is the second element we are looking at then we need to set the ground radio buttons
			else if(count == 1){
				if(value.contains("1")){
					rdbtnPerfectGround.setSelected(true);
					rdbtnNoGround.setSelected(false);
				}
				else if(value.contains("0")){
					rdbtnPerfectGround.setSelected(false);
					rdbtnNoGround.setSelected(true);
				}
			}
			count += 1;
		}
		//clear all of these lists once the GUI has been created otherwise it will not allow the next antenna structure to be generated
		globalValues.clear();
	}
	
	/// function that reads the contents of the template files antenna specific values and then sets the textboxes and labels equal to these values
	public static void SetSpecificGUIfromAntennaType(Object antennaType, ReadTemplateFiles templateObject, 
		ArrayList<JLabel> specificLabelsList, ArrayList<JTextField> specificTextFields, JList antennalist, JComboBox antennaTypeBox){
		//if the antennalistbox is not selected and therefore the checkbox is selected then execute getFileValues
		//otherwise just get the SpecificLabelsList and populate the textboxes etc.
		String antTypeString = antennaType.toString();
		templateObject.getFileValues(antTypeString);
		ArrayList<String> specificLabels = templateObject.getSpecificLabelsList();
		ArrayList<String> specificValues = templateObject.getSpecificValuesList();
		//Loop through each element of the arrayList containing the labels and change the text of the JLabels in the GUI to what is being read in from the file
		for (int i = 0; i<specificLabels.size();i++){
			String strippedpecificLabel = specificLabels.get(i).replaceAll("\\s+", "");
			String strippedspecificValue = specificValues.get(i).replaceAll("\\s+", "");
			specificLabelsList.get(i).setText(strippedpecificLabel);
			specificTextFields.get(i).setText(strippedspecificValue);
			specificTextFields.get(i).setVisible(true);
			specificLabelsList.get(i).setVisible(true);
		}
		
		int counter = 1;
		for(JTextField field:specificTextFields){
			if(!(counter <= specificValues.size())){
				field.setVisible(false);
			}
			counter+=1;
		}
		int counter2 = 1;
		for(JLabel label:specificLabelsList){
			if(!(counter2 <= specificLabels.size())){
				label.setVisible(false);
			}
			counter2+=1;
		}	
		specificLabels.clear();
		specificValues.clear();
		counter = 1;
		counter2 = 1;
	}
	
	/// takes existing antenna structure selected in the JList and loads its parameters into the GUI and then updates the GUI textboxes and labels accordingly.
	public void SetSpecificGUIfromSelectedAntenna(ReadTemplateFiles templateObject,ArrayList<JLabel> specificLabelsList,
	ArrayList<JTextField> specificTextFields, JList antennalist){
		ArrayList<String> specificLabels = templateObject.getSpecificLabelsList();
		ArrayList<String> specificValues = templateObject.getSpecificValuesList();
		for (int i = 0; i<specificValues.size();i++){
			String strippedspecificValue = specificValues.get(i).replaceAll("\\s+", "");
			specificTextFields.get(i).setText(strippedspecificValue);
			specificTextFields.get(i).setVisible(true);
		}
		for(int i = 0; i < specificLabels.size(); i++){
			String strippedpecificLabel = specificLabels.get(i).replaceAll("\\s+", "");
			specificLabelsList.get(i).setText(strippedpecificLabel);
			specificLabelsList.get(i).setVisible(true);
		}
		int counter = 1;
		for(JTextField field:specificTextFields){
			if(!(counter <= specificValues.size())){
				field.setVisible(false);
			}
			counter+=1;
		}
		int counter2 = 1;
		for(JLabel label:specificLabelsList){
			if(!(counter2 <= specificLabels.size())){
				label.setVisible(false);
			}
			counter2+=1;
		}	
		counter = 1;
		counter2 = 1;
	}
	
	//// check if the end frequency value is higher than the start frequency value
	public void isEndFreqHigher(double StartFreq, double EndFreq){
		if(StartFreq < EndFreq){
			return;
		}
		else if(StartFreq >= EndFreq){
			isGlobalParametersValid = false;
			JOptionPane.showMessageDialog(BackgroundPane, "The Start Frequency must be a lower double value than the end Frequency");
		}
		
	}
	/**
	 * extensive function that populates the global data in the textboxes that the user has entered when they want to complete the antenna structure
	 * depending on which radio buttons were selected the list is populated differently with separate values
	 * The function also checks to see if the textboxes contain only numbers and dots and also checks to make sure they aren't giving empty values
	 * and returns false if these conditions aren't met
	 */
	public void CaptureGlobalDataAndValidate(JRadioButton rdbtnSingle, JRadioButton rdbtnLinear, 
	JRadioButton rdbtnMult, JRadioButton rdbtnNoGround, JRadioButton rdbtnPerfectGround){
		isGlobalParametersValid = true;
		UserSetGlobalParameters.clear();
		byte[] StartFreqBytes = StartFreqText.getText().getBytes();
		byte[] EndFreqBytes = EndFreqText.getText().getBytes();
		byte[] NoStepsBytes = NoStepsText.getText().getBytes();
		isGlobalParametersValid = true;
		if(rdbtnSingle.isSelected()){
			if(StartFreqText.getText().isEmpty() || StartFreqBytes[0] == '0'){
				JOptionPane.showMessageDialog(BackgroundPane,"Cannot visualize an antenna without a frequency value or a zero frequency value");
				isGlobalParametersValid = false;
				return;
			}
			UserSetGlobalParameters.add(StartFreqText.getText());
		}
		else if(rdbtnLinear.isSelected()){
			if(StartFreqText.getText().isEmpty() || EndFreqText.getText().isEmpty() || NoStepsText.getText().isEmpty() || StartFreqBytes[0] == '0'
			   || EndFreqBytes[0] == '0' || NoStepsBytes[0] == '0'){
				JOptionPane.showMessageDialog(BackgroundPane,"Cannot visualize an antenna without all required linear frequency values or zero values");
				isGlobalParametersValid = false;
				return;
			}
			UserSetGlobalParameters.add(StartFreqText.getText());
			UserSetGlobalParameters.add(EndFreqText.getText());
			//add a zero to the list to signify that the frequency is not multiplicative
			UserSetGlobalParameters.add("0");
			UserSetGlobalParameters.add(NoStepsText.getText());
		}
		else if(rdbtnMult.isSelected()){
			if(StartFreqText.getText().isEmpty() || EndFreqText.getText().isEmpty() || NoStepsText.getText().isEmpty()  || StartFreqBytes[0] == '0'
					   || EndFreqBytes[0] == '0' || NoStepsBytes[0] == '0'){
				JOptionPane.showMessageDialog(BackgroundPane,"Cannot visualize an antenna without all required multiplicative frequency values or zero values");
				isGlobalParametersValid = false;
				return;
			}
			UserSetGlobalParameters.add(StartFreqText.getText());
			UserSetGlobalParameters.add(EndFreqText.getText());
			//add 1 to signify that it is a multiplicative frequency step
			UserSetGlobalParameters.add("1");
			UserSetGlobalParameters.add(NoStepsText.getText());
		}
		if (rdbtnNoGround.isSelected()){
			UserSetGlobalParameters.add("0");
		}
		else if(rdbtnPerfectGround.isSelected()){
			UserSetGlobalParameters.add("1");
		}
		//implement method here that can implement sommerfield ground by taking in dielectric constant and conductivity
		else if(!rdbtnNoGround.isSelected() && !rdbtnPerfectGround.isSelected()){
			JOptionPane.showMessageDialog(BackgroundPane,"Cannot visualize an antenna without selecting a ground configuration");
			isGlobalParametersValid = false;
		}
		for(String globalParameter:UserSetGlobalParameters){
			if(!globalParameter.matches("^[0-9.]+$")){
				JOptionPane.showMessageDialog(BackgroundPane, "One or more global parameters entered are not valid, please check that no parameters contain letters and try again");
				isGlobalParametersValid = false;
				return;
			}
		}
		//perform a check to ensure that the lower frequency has a lower numerical value than the higher frequency
		if(rdbtnMult.isSelected() && rdbtnLinear.isSelected()){
			double freqlow = Double.parseDouble(StartFreqText.getText());
			double freqhigh = Double.parseDouble(EndFreqText.getText());
			isEndFreqHigher(freqlow,freqhigh);
		}
	}
	
	/// function that executes action on button press that captures the data from the GUI form
	public void CaptureUserDataAndValidate(ReadTemplateFiles templateObject, ArrayList<JTextField> specificTextBoxesList){
		UserSetSpecificParameters.clear();
		isParametersValid = true;
		ArrayList<String> specificValues = templateObject.getSpecificValuesList();
		//now add the specificParameters for a particular antenna to a separate list when the add antenna button is pressed
		for(int index = 0; index < specificValues.size();index++){
			UserSetSpecificParameters.add(specificTextBoxesList.get(index).getText());
		}
		for(String specificParameter:UserSetSpecificParameters){
			if(!specificParameter.matches("^[0-9.-]+$")){
				JOptionPane.showMessageDialog(BackgroundPane, "One or more specific parameters entered are not valid, please check that no parameters contain letters and try again");
				isParametersValid = false;
				break;
			}
		}
	}
	
	/** this function executes after the user data has been captured and essentially checks if the parameters entered were deemed to be valid
	 * it then adds this antenna to a list on the GUI and instantiates a structure object of this particular antenna which passes
	 * the information to the NECgenerator class
	 */
	public void AddAntennaToGUI(ReadTemplateFiles templateObject, JList antennaList,NecGenerator _necObject,int numberClicks){
		//only if the parameters are true
		if(isParametersValid == true){
			String antennaName = templateObject.getAntennaName();
			ArrayList<String> rulesList = templateObject.getRulesList();
			ArrayList<String> specificLabels = templateObject.getSpecificLabelsList();
			ArrayList<Double> specificValuesList = new ArrayList<Double>();
			//convert the specific parameters to a double array
			for(String specificValueString: UserSetSpecificParameters){
				Double specificValue = Double.parseDouble(specificValueString);
				specificValuesList.add(specificValue);
			}
			Double[] specificValuesArray = new Double[specificValuesList.size()];
			specificValuesArray = specificValuesList.toArray(specificValuesArray);
			//convert to primitive double type instead of object
			double[] specificValuesPrimArray = Stream.of(specificValuesArray).mapToDouble(Double::doubleValue).toArray();
			//instantiating object that will store the NEC generator parameters and all creation and deletion of structure
			//the antennaName needs to be unique so append the number of button presses to the beginning of the antenna name
			String numberClicksString = Integer.toString(numberClicks);
			antennaName =  numberClicksString + "_" + antennaName;
			_necObject.addStructure(antennaName, rulesList, specificLabels, specificValuesPrimArray);
			//store the antennas in a fixed size array that is redeclared each time we add an antenna (because JList wants an array)
			antennaListModel.addElement(antennaName);
			antennaList.setModel(antennaListModel);
		}
	}
	
	/// call the functions in NecGenerator that sets the frequency and ground values depending on if multiple frequencies have been provided, single frequency etc.
	public void SetFrequencyAndGroundValues(){
		if(isGlobalParametersValid == true){
			//if there is a single frequency call the one parameter setFrequency method in the necgenerator object
			if(UserSetGlobalParameters.size() == 2){
				double singleFreqdouble = Double.parseDouble(UserSetGlobalParameters.get(0));
				_necObject.setFrequency(singleFreqdouble);
				if(UserSetGlobalParameters.get(1) == "0"){
					_necObject.setGround(false);
				}
				else if(UserSetGlobalParameters.get(1) == "1"){
					_necObject.setGround(true);
				}
			}
			//if there is multiple frequencies pass the requisite information to setFrequency() while converting to double
			else if(UserSetGlobalParameters.size() == 5){
				boolean isMult = false;
				double lowFreqdouble = Double.parseDouble(UserSetGlobalParameters.get(0));
				double highFreqdouble = Double.parseDouble(UserSetGlobalParameters.get(1));
				int stepsInt = Integer.parseInt(UserSetGlobalParameters.get(3));
				if(UserSetGlobalParameters.get(2) == "0"){
					isMult = false;
				}
				else if(UserSetGlobalParameters.get(2) == "1"){
					isMult = true;
				}
				_necObject.setFrequency(lowFreqdouble, highFreqdouble, isMult, stepsInt);
				if(UserSetGlobalParameters.get(4) == "0"){
					_necObject.setGround(false);
				}
				else if(UserSetGlobalParameters.get(4) == "1"){
					_necObject.setGround(true);
				}
			}
		}
	}

	/**
	 * Launch the application. class method that handles all elements in the GUI as well as the actionlisteners which execute code above when certain
	 * events take place
	 */
	public static void main(String[] args) {
		//read file names of the template antenna designs into the GUI application
		ReadTemplateFiles templateObject = new ReadTemplateFiles();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenu frame = new MainMenu(templateObject);
					frame.setTitle("Open Electromagnetic Solver");
					frame.setMinimumSize(new Dimension(785,830));
					frame.setMaximumSize(new Dimension(785,830));
					frame.setResizable(false);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Class Main Menu is the frame that contains all of the buttons, menu items, radio buttons, combo boxes, lists etc.
	 * This class also contains all action listener methods that execute various blocks of code as is detailed in the sequence diagrams.
	 * Due to a lack of knowledge using Swing applications this class is quite large and could be refactored into smaller classes.
	 */
	public MainMenu(ReadTemplateFiles templateObject) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 785, 830);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmNewAntennaStructure = new JMenuItem("New Structure");
		mnFile.add(mntmNewAntennaStructure);
		
		JMenuItem mntmExistingAntennaStructure = new JMenuItem("Open Existing Structure");
		mnFile.add(mntmExistingAntennaStructure);
		BackgroundPane = new JPanel();
		BackgroundPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(BackgroundPane);
		BackgroundPane.setLayout(null); 
		
		JPanel StructPane = new JPanel();
		StructPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		StructPane.setBounds(432, 630, 331, 126);
		BackgroundPane.add(StructPane);
		StructPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Please enter a name for this new structure:");
		lblNewLabel.setBounds(12, 12, 319, 16);
		StructPane.add(lblNewLabel);
		
		StructureName = new JTextField();
		StructureName.setText("untitled");
		StructureName.setBounds(12, 40, 284, 20);
		StructPane.add(StructureName);
		StructureName.setColumns(10);
		
		StructPane.setVisible(false);
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.setBounds(12, 88, 84, 26);
		StructPane.add(btnSubmit);
		
		JPanel AddAntennaPane = new JPanel();
		AddAntennaPane.setBounds(10, 33, 764, 736);
		BackgroundPane.add(AddAntennaPane);
		AddAntennaPane.setVisible(false);
		
		JPanel GlobalPane = new JPanel();
		GlobalPane.setBounds(0, 12, 311, 192);
		GlobalPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		GlobalPane.setLayout(null);
		
		JLabel lblFrequency1 = new JLabel("Frequency");
		lblFrequency1.setBounds(10, 11, 87, 14);
		GlobalPane.add(lblFrequency1);
		
		JRadioButton rdbtnMult = new JRadioButton("Multiply");
		rdbtnMult.setBounds(6, 68, 91, 23);
		GlobalPane.add(rdbtnMult);
		
		JRadioButton rdbtnSingle = new JRadioButton("Single");
		rdbtnSingle.setSelected(true);
		rdbtnSingle.setBounds(6, 28, 91, 23);
		GlobalPane.add(rdbtnSingle);
		
		JPanel MultFreqPane = new JPanel();
		MultFreqPane.setBounds(105, 52, 194, 69);
		GlobalPane.add(MultFreqPane);
		MultFreqPane.setLayout(null);
		MultFreqPane.setVisible(false);		
		JRadioButton rdbtnLinear = new JRadioButton("Linear");
		rdbtnLinear.setBounds(6, 47, 91, 23);
		GlobalPane.add(rdbtnLinear);
		
		JLabel lblStartFreq = new JLabel("Start:");
		lblStartFreq.setBounds(109, 32, 50, 14);
		GlobalPane.add(lblStartFreq);
		
		StartFreqText = new JTextField();
		StartFreqText.setBounds(151, 30, 100, 20);
		GlobalPane.add(StartFreqText);
		StartFreqText.setColumns(10);
		
		JLabel lblHertz = new JLabel("MHz");
		lblHertz.setBounds(251, 32, 50, 14);
		GlobalPane.add(lblHertz);
		
		JLabel lblEnd = new JLabel("End:");
		lblEnd.setBounds(10, 11, 33, 14);
		MultFreqPane.add(lblEnd);
		
		EndFreqText = new JTextField();
		EndFreqText.setBounds(43, 9, 103, 20);
		MultFreqPane.add(EndFreqText);
		EndFreqText.setColumns(10);
		
		JLabel lblMhz = new JLabel("MHz");
		lblMhz.setBounds(147, 11, 39, 14);
		MultFreqPane.add(lblMhz);
		
		JLabel lblNoSteps = new JLabel("No. Steps:");
		lblNoSteps.setBounds(10, 41, 74, 14);
		MultFreqPane.add(lblNoSteps);
		
		NoStepsText = new JTextField();
		NoStepsText.setColumns(10);
		NoStepsText.setBounds(86, 39, 60, 20);
		MultFreqPane.add(NoStepsText);
		
		JPanel SpecificPane = new JPanel();
		SpecificPane.setBounds(0, 206, 311, 530);
		SpecificPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		SpecificPane.setLayout(null);
		
		//add template antennas to the combo box so that they can be selected and the GUI loaded
		ArrayList<String> antennas = templateObject.ReadFileNamesInFolder();
		String[] antennaNames = FetchTemplateNames(antennas);	
		JComboBox TemplateAntennasBox = new JComboBox(antennaNames);
		
		TemplateAntennasBox.setBounds(12, 31, 184, 19);
		SpecificPane.add(TemplateAntennasBox);
		TemplateAntennasBox.setSelectedIndex(-1);
		
		JLabel lblAntennaType = new JLabel("Select Assembly Type");
		lblAntennaType.setBounds(12, 12, 167, 16);
		SpecificPane.add(lblAntennaType);
		
		JPanel SpecificPaneComp = new JPanel();
		SpecificPaneComp.setBounds(12, 65, 289, 423);
		SpecificPane.add(SpecificPaneComp);
		SpecificPaneComp.setVisible(false);
		ArrayList<JLabel> specificLabels = new ArrayList<JLabel>();
		ArrayList<JTextField> specificTextBoxesList = new ArrayList<JTextField>();
		
		//add each created label to the list of labels so that they can be passed to a function to change their text
		JLabel lblprop1 = new JLabel("");
		lblprop1.setBounds(10, 17, 175, 14);
		specificLabels.add(lblprop1);
		JLabel lblprop2 = new JLabel("");
		lblprop2.setBounds(10, 43, 175, 14);
		specificLabels.add(lblprop2);
		JLabel lblprop3 = new JLabel("");
		lblprop3.setBounds(10, 69, 175, 14);
		specificLabels.add(lblprop3);
		JLabel lblprop4 = new JLabel("");
		lblprop4.setBounds(10, 95, 175, 14);
		specificLabels.add(lblprop4);
		JLabel lblprop5 = new JLabel("");
		lblprop5.setBounds(10, 128, 175, 14);
		specificLabels.add(lblprop5);
		JLabel lblprop6 = new JLabel("");
		lblprop6.setBounds(10, 153, 175, 14);
		specificLabels.add(lblprop6);
		JLabel lblprop7 = new JLabel("");
		lblprop7.setBounds(10, 182, 175, 14);
		specificLabels.add(lblprop7);
		JLabel lblprop8 = new JLabel("");
		lblprop8.setBounds(10, 207, 175, 14);
		specificLabels.add(lblprop8);
		JLabel lblprop9 = new JLabel("");
		lblprop9.setBounds(10, 233, 175, 14);
		specificLabels.add(lblprop9);
		JLabel lblprop10 = new JLabel("");
		lblprop10.setBounds(10, 259, 175, 14);
		specificLabels.add(lblprop10);
		JLabel lblprop11 = new JLabel("");
		lblprop11.setBounds(10, 286, 175, 14);
		specificLabels.add(lblprop11);
		JLabel lblprop12 = new JLabel("");
		lblprop12.setBounds(10, 311, 175, 14);
		specificLabels.add(lblprop12);
		JLabel lblprop13 = new JLabel("");
		lblprop13.setBounds(10, 339, 175, 14);
		specificLabels.add(lblprop13);
		JLabel lblprop14 = new JLabel("");
		lblprop14.setBounds(10, 364, 175, 14);
		specificLabels.add(lblprop14);
		JLabel lblprop15 = new JLabel("");
		lblprop15.setBounds(10, 395, 175, 14);
		specificLabels.add(lblprop15);
		
		textField1 = new JTextField();
		textField1.setBounds(190, 14, 86, 20);
		textField1.setColumns(10);
		specificTextBoxesList.add(textField1);
		textField2 = new JTextField();
		textField2.setBounds(190, 40, 86, 20);
		textField2.setColumns(10);
		specificTextBoxesList.add(textField2);
		textField3 = new JTextField();
		textField3.setBounds(190, 66, 86, 20);
		textField3.setColumns(10);
		specificTextBoxesList.add(textField3);
		textField4 = new JTextField();
		textField4.setBounds(190, 92, 86, 20);
		textField4.setColumns(10);
		specificTextBoxesList.add(textField4);
		textField5 = new JTextField();
		textField5.setBounds(190, 120, 86, 20);
		textField5.setColumns(10);
		specificTextBoxesList.add(textField5);
		textField6 = new JTextField();
		textField6.setBounds(190, 150, 86, 20);
		textField6.setColumns(10);
		specificTextBoxesList.add(textField6);
		textField7 = new JTextField();
		textField7.setBounds(190, 178, 86, 20);
		textField7.setColumns(10);
		specificTextBoxesList.add(textField7);
		textField8 = new JTextField();
		textField8.setBounds(190, 204, 86, 20);
		textField8.setColumns(10);
		specificTextBoxesList.add(textField8);
		textField9 = new JTextField();
		textField9.setBounds(190, 230, 86, 20);
		textField9.setColumns(10);
		specificTextBoxesList.add(textField9);
		textField10 = new JTextField();
		textField10.setBounds(190, 256, 86, 20);
		textField10.setColumns(10);
		specificTextBoxesList.add(textField10);
		textField11 = new JTextField();
		textField11.setBounds(190, 282, 86, 20);
		textField11.setColumns(10);
		specificTextBoxesList.add(textField11);
		textField12 = new JTextField();
		textField12.setBounds(190, 308, 86, 20);
		textField12.setColumns(10);
		specificTextBoxesList.add(textField12);
		textField13 = new JTextField();
		textField13.setBounds(190, 335, 86, 20);
		textField13.setColumns(10);
		specificTextBoxesList.add(textField13);
		textField14 = new JTextField();
		textField14.setBounds(190, 361, 86, 20);
		textField14.setColumns(10);
		specificTextBoxesList.add(textField14);
		SpecificPaneComp.setLayout(null);
		SpecificPaneComp.add(lblprop1);
		SpecificPaneComp.add(textField1);
		SpecificPaneComp.add(lblprop2);
		SpecificPaneComp.add(textField2);
		SpecificPaneComp.add(lblprop3);
		SpecificPaneComp.add(textField3);
		SpecificPaneComp.add(lblprop4);
		SpecificPaneComp.add(textField4);
		SpecificPaneComp.add(lblprop5);
		SpecificPaneComp.add(lblprop6);
		SpecificPaneComp.add(textField5);
		SpecificPaneComp.add(lblprop7);
		SpecificPaneComp.add(textField6);
		SpecificPaneComp.add(lblprop11);
		SpecificPaneComp.add(textField9);
		SpecificPaneComp.add(lblprop8);
		SpecificPaneComp.add(lblprop9);
		SpecificPaneComp.add(lblprop10);
		SpecificPaneComp.add(textField7);
		SpecificPaneComp.add(textField8);
		SpecificPaneComp.add(lblprop12);
		SpecificPaneComp.add(lblprop13);
		SpecificPaneComp.add(lblprop14);
		SpecificPaneComp.add(lblprop15);
		SpecificPaneComp.add(textField10);
		SpecificPaneComp.add(textField11);
		SpecificPaneComp.add(textField12);
		SpecificPaneComp.add(textField13);
		SpecificPaneComp.add(textField14);
		
		textField15 = new JTextField();
		textField15.setColumns(10);
		textField15.setBounds(190, 392, 86, 20);
		SpecificPaneComp.add(textField15);
		specificTextBoxesList.add(textField15);
		
		//set all the textboxes to start off as not visible
		for(JTextField textfield:specificTextBoxesList){
			textfield.setVisible(false);
		}
		
		JLabel lblGround = new JLabel("Ground");
		lblGround.setBounds(10, 121, 55, 16);
		GlobalPane.add(lblGround);
		
		JRadioButton rdbtnNoGround = new JRadioButton("No Ground");
		rdbtnNoGround.setSelected(true);
		rdbtnNoGround.setBounds(6, 140, 100, 24);
		GlobalPane.add(rdbtnNoGround);
		
		JRadioButton rdbtnPerfectGround = new JRadioButton("Perfect Ground");
		rdbtnPerfectGround.setBounds(132, 140, 146, 24);
		GlobalPane.add(rdbtnPerfectGround);
		AddAntennaPane.setLayout(null);
		AddAntennaPane.add(SpecificPane);
		
		JButton btnAddAntenna = new JButton("Add Assembly");
		btnAddAntenna.setBounds(12, 494, 141, 25);
		SpecificPane.add(btnAddAntenna);
		
		JButton btnEdit = new JButton("Edit Assembly");
		btnEdit.setBounds(160, 494, 141, 25);
		SpecificPane.add(btnEdit);
		AddAntennaPane.add(GlobalPane);
		btnAddAntenna.setVisible(false);
		btnEdit.setVisible(false);
		
		JPanel NumberAntennasPanel = new JPanel();
		NumberAntennasPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		NumberAntennasPanel.setBounds(402, 12, 362, 213);
		AddAntennaPane.add(NumberAntennasPanel);
		NumberAntennasPanel.setLayout(null);
		
		JButton btnRemoveAntenna = new JButton("Remove Assembly");
		btnRemoveAntenna.setEnabled(false);
		btnRemoveAntenna.setBounds(12, 176, 161, 25);
		NumberAntennasPanel.add(btnRemoveAntenna);
		
		JMenuItem mntmAntennaSelect = new JMenuItem("Add Assemblies");
		mntmAntennaSelect.setEnabled(false);
		mntmAntennaSelect.setBounds(0, 0, 116, 21);
		BackgroundPane.add(mntmAntennaSelect);
		
		JMenuItem mntmVisualize = new JMenuItem("Visualize Structure");
		mntmVisualize.setBounds(180, 0, 161, 21);
		BackgroundPane.add(mntmVisualize);
		mntmVisualize.setEnabled(false);

		
		JMenuItem mntmSimulate = new JMenuItem("Simulate");
		mntmSimulate.setBounds(435, 0, 109, 21);
		BackgroundPane.add(mntmSimulate);
		mntmSimulate.setEnabled(false);
		
		JMenuItem mntmNewResults = new JMenuItem("Results");
		mntmNewResults.setBounds(643, 0, 109, 21);
		BackgroundPane.add(mntmNewResults);
		mntmNewResults.setEnabled(false);
		
		//set the remove antenna button to true if an antenna is selected
		JList antennalist = new JList(antennaListModel);
		antennalist.setBounds(12, 11, 338, 154);
		antennalist.setLayoutOrientation(JList.VERTICAL);
		NumberAntennasPanel.add(antennalist);
		NumberAntennasPanel.setVisible(true);
		antennalist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JButton btnCompleteStructure = new JButton("Complete Structure");
		btnCompleteStructure.setBounds(178, 175, 172, 26);
		NumberAntennasPanel.add(btnCompleteStructure);
		btnCompleteStructure.setEnabled(false);
		
		JScrollPane scrollPane = new JScrollPane(antennalist);
		scrollPane.setBounds(12, 11, 338, 154);
		NumberAntennasPanel.add(scrollPane);
		
		
		JPanel RenamePane = new JPanel();
		RenamePane.setBorder(new LineBorder(new Color(0, 0, 0)));
		RenamePane.setBounds(523, 237, 241, 86);
		AddAntennaPane.add(RenamePane);
		RenamePane.setLayout(null);
		RenamePane.setVisible(false);
		
		RenameText = new JTextField();
		RenameText.setBounds(12, 12, 217, 25);
		RenamePane.add(RenameText);
		RenameText.setColumns(10);
		
		JButton btnRenameAntenna = new JButton("Rename Assembly");
		btnRenameAntenna.setBounds(12, 49, 162, 25);
		RenamePane.add(btnRenameAntenna);
		
		JPanel SimulatePane = new JPanel();
		SimulatePane.setBounds(536, 523, 228, 70);
		AddAntennaPane.add(SimulatePane);
		SimulatePane.setLayout(null);
		
		JProgressBar SimulateBar = new JProgressBar(0,100);
		SimulateBar.setBounds(12, 12, 203, 20);
		SimulatePane.add(SimulateBar);
		SimulateBar.setValue(0);
		SimulateBar.setStringPainted(true);
		
		JLabel lblSimulateProgress = new JLabel("");
		lblSimulateProgress.setBounds(12, 44, 203, 15);
		SimulatePane.add(lblSimulateProgress);
		SimulatePane.setVisible(false);
		SimulateBar.setVisible(true);
		lblSimulateProgress.setVisible(true);

		
		/// put listeners here after declarations (functions which execute when an action occurs on a swing element buttons etc)
		rdbtnLinear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rdbtnSingle.setSelected(false);
				rdbtnMult.setSelected(false);
				if(rdbtnLinear.isSelected() == true){
					MultFreqPane.setVisible(true);
				}
				mntmVisualize.setEnabled(false);
			}
		});
		rdbtnMult.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rdbtnSingle.setSelected(false);
				rdbtnLinear.setSelected(false);
				if(rdbtnMult.isSelected() == true){
					MultFreqPane.setVisible(true);
				}
				mntmVisualize.setEnabled(false);
			}
		});
		rdbtnSingle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rdbtnMult.setSelected(false);
				rdbtnLinear.setSelected(false);
				if (rdbtnSingle.isSelected()==true){
					MultFreqPane.setVisible(false);
				}
				mntmVisualize.setEnabled(false);
			}
		});
		mntmAntennaSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AddAntennaPane.setVisible(true);
				GlobalPane.setVisible(true);
				SpecificPane.setVisible(true);
				NumberAntennasPanel.setVisible(true);
			}
		});
		rdbtnNoGround.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rdbtnPerfectGround.setSelected(false);
				mntmVisualize.setEnabled(false);
			}
		});
		rdbtnPerfectGround.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rdbtnNoGround.setSelected(false);
				mntmVisualize.setEnabled(false);
			}
		});
		/// when the user selects a particular antenna type read the info from that template file and present it to the user in the form of labels textboxes
		TemplateAntennasBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Object antennaType = TemplateAntennasBox.getSelectedItem();
				btnAddAntenna.setVisible(true);
				//execute a function that will take the option that the user selected and execute various operations that will generate the GUI from this
				if(TemplateAntennasBox.getSelectedIndex() != -1){
					SetSpecificGUIfromAntennaType(antennaType,templateObject,specificLabels,specificTextBoxesList,antennalist,TemplateAntennasBox);
					LoadGlobalGUIfromAntennaType(antennaType, templateObject, StartFreqText, rdbtnPerfectGround, rdbtnNoGround);
				}
				SpecificPaneComp.setVisible(true);
			}
		});
		
		/// When removeAntenna button is pressed remove the selected element from the ListModel and then re-render the JList
		btnRemoveAntenna.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String selectedElement = (String) antennalist.getSelectedValue();
				int selectedElementIndex = antennalist.getSelectedIndex();
				boolean isRemoved = _necObject.removeStructure(selectedElement);
				antennaListModel.remove(selectedElementIndex);
				antennalist.setModel(antennaListModel);
				if(antennaListModel.getSize() == 0){
					btnCompleteStructure.setEnabled(false);
				}
				mntmVisualize.setEnabled(false);
			}
		});
		
		mntmNewAntennaStructure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mntmVisualize.setEnabled(false);
				mntmSimulate.setEnabled(false);
				mntmNewResults.setEnabled(false);
				antennaListModel.clear();
				antennalist.setModel(antennaListModel);
				mntmAntennaSelect.setEnabled(false);
				AddAntennaPane.setVisible(false);
				StructPane.setVisible(true);
			}
		});
		/// button which stores the name of the structure for the NEC file generation
		btnSubmit.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if(StructureName.getText().contains(".")){
					JOptionPane.showMessageDialog(BackgroundPane,"Structures cannot have full stops in their names, Please try again!");
					return;
				}
				else if(StructureName.getText().isEmpty()){
					JOptionPane.showMessageDialog(BackgroundPane,"Structures cannot have empty names, Please try again!");
					return;
				}
				else{
					_structurePath = null;
					_structureName = StructureName.getText();
					String _structureFileName = _structureName + ".nec";
					String currentDir = System.getProperty("user.dir");
				    File folder = new File(currentDir);
					File[] listOfFiles = folder.listFiles();
					//create arraylist to store file names
					for (File file : listOfFiles) {
					    if (file.isFile() && file.getName().contains(".nec")) {
					    	//if the name of the structure matches an existing structure name tell the user that they will overwrite the existing structure
					        if(_structureFileName.matches(file.getName())){
					        	int n = JOptionPane.showConfirmDialog(
			                            BackgroundPane, "This structure name already exists press YES to overwrite structure or NO to choose a different structure name","",
			                            JOptionPane.YES_NO_OPTION);
					        	if (n == JOptionPane.YES_OPTION){
					        		break;
					        	}
					        	else if(n == JOptionPane.NO_OPTION){
					        		return;
					        	}
					        }
					    }
					}
					StructPane.setVisible(false);
					mntmAntennaSelect.setEnabled(true);
					//create one instance of the necGenerator object which we will always refer to for this structure
					NecGenerator newNecObject = new NecGenerator(_structureName);
					//set the necobject globally within the class
					setNecObject(newNecObject);
				}
			}
		});
		/** when action is carried out on the JList such as an element being selected
		* when an element is selected the class must fetch this element from the arrayList of structures
		* and populate the specificLabels and specificValues textboxes with the values in the template
		*/
		antennalist.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {//This line prevents double events
					if(antennalist.isSelectionEmpty()){
						btnRemoveAntenna.setEnabled(false);
						RenamePane.setVisible(false);
						btnEdit.setVisible(false);
					}
					else{
						TemplateAntennasBox.setSelectedIndex(-1);
						btnEdit.setVisible(true);
						ArrayList<String> selectedStructureLabels = new ArrayList<String>();
						double[] selectedStructureParameters = null;
						//if I make a selection I must fetch the specificLabelList of the selected structured along with their values from the necGenerator
						//object then set them in the ReadTemplatefiles object and pass them to the specificGUI loading function
						String selectedStructureName = antennalist.getSelectedValue().toString();
						selectedStructureParameters = _necObject.getStructureParameters(selectedStructureName);
						selectedStructureLabels = _necObject.getStructureLabels(selectedStructureName);
						//take these double values and convert them back to an arrayList of Strings then set them in the ReadTemplateFiles class
						ArrayList<String> selectedStringValues = new ArrayList<String>();
						for(int i = 0; i < selectedStructureParameters.length; i++){
							String element = String.valueOf(selectedStructureParameters[i]);
							selectedStringValues.add(element);
						}
						//temporary container list
						ArrayList<String> selectedStructureLabelsCopy = new ArrayList<String>();
						for(int i = 0; i < selectedStructureLabels.size(); i++){
							selectedStructureLabelsCopy.add(selectedStructureLabels.get(i));
						}
						//set these values in the readTemplateFiles class for the object that has been selected
						//System.out.println(selectedStringValues);
						templateObject.setSpecificValuesList(selectedStringValues);
						templateObject.setSpecificLabelsList(selectedStructureLabelsCopy);
						//Loop through each element of the arrayList containing the labels and change the text of the JLabels in the GUI to what is being read in from the file
						SetSpecificGUIfromSelectedAntenna(templateObject,specificLabels,specificTextBoxesList,antennalist);
						btnRemoveAntenna.setEnabled(true);
						RenamePane.setVisible(true);
					}
			    }
				
			}
		});
		/** when the complete structure button is pressed enable the visualization button
		* setFrequency and setGround
		* Then write the completed NEC file and allow the user to visualize the antenna structure
		*/
		btnCompleteStructure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CaptureGlobalDataAndValidate(rdbtnSingle,rdbtnLinear,rdbtnMult,rdbtnNoGround,rdbtnPerfectGround);
				SetFrequencyAndGroundValues();
				try{
					_necObject.generateNEC();
				}catch(Exception generateError) {
					System.out.println(generateError);
					JOptionPane.showMessageDialog(BackgroundPane,"Unable to generate required file to complete structure, check your template file!");
					return;
				}
				mntmVisualize.setEnabled(true);
			}
		});
		/** when the button is pressed the program must take all of the information that the user has entered into the text boxes and store it in an array
		* the button needs to then store the antenna name as well as the rules specified in the template file
		* Four containers are therefore needed (frequency container + ground container, specific parameters container, antenna name, rules list)
		* thereafter this function needs to instantiate a structure object and pass these parameters and if the number of structures is zero then set
		* the frequency and set the ground parameters
		*/
		btnAddAntenna.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				numberClicks += 1;
				CaptureUserDataAndValidate(templateObject,specificTextBoxesList);
				CaptureGlobalDataAndValidate(rdbtnSingle,rdbtnLinear,rdbtnMult,rdbtnNoGround,rdbtnPerfectGround);
				SetFrequencyAndGroundValues();
				AddAntennaToGUI(templateObject,antennalist,_necObject,numberClicks);
				btnCompleteStructure.setEnabled(true);
				//don't allow the user to visualize the antenna structure until they complete the structure (i.e. write the nec file)
				mntmVisualize.setEnabled(false);
				mntmSimulate.setEnabled(false);
				mntmNewResults.setEnabled(false);
			}
		});
		/** action to be taken when the renameAntennaButton is pressed is to firstly fetch the index of the selected element and then
		* retrieve the current name from that then call the renameStructure method that will rename this antenna in the backend
		* and do likewise in the JList i.e. change the name of the value in the antennaListModel and export it to the antennaList
		*/
		btnRenameAntenna.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(RenameText.getText().contains(".")){
					JOptionPane.showMessageDialog(BackgroundPane,"Antennas cannot have full stops in their names, Please try again!");
					return;
				}
				else if(RenameText.getText().isEmpty()){
					JOptionPane.showMessageDialog(BackgroundPane,"Antennas cannot have empty names, Please try again!");
					return;
				}
				else{
					String newAntennaName = RenameText.getText();
					int SelectedElementIndex = antennalist.getSelectedIndex();
					int StoreIndex = SelectedElementIndex;
					String currentAntennaName = antennalist.getSelectedValue().toString();
					//before renaming the structure perform a check to ensure an antenna does not already exist with the same name
					//System.out.println(newAntennaName);
					for(int i = 0; i < antennaListModel.size();i++){
						String antennaNameInList = antennaListModel.getElementAt(i).toString();
						if(antennaNameInList.matches(newAntennaName)){
							JOptionPane.showMessageDialog(BackgroundPane,"This antenna name already exists, Please rename and try again!");
							return;
						}
					}
					//rename the antenna structure in the backend NEC generator file
					_necObject.renameStructure(currentAntennaName,newAntennaName);
					//then rename the antenna structure in the antennaList model
					antennaListModel.remove(SelectedElementIndex);
					antennaListModel.add(StoreIndex,newAntennaName);
					antennalist.setModel(antennaListModel);
					RenameText.setText("");
				}
			}
		});
		/// when the visualize antenna button is pressed execute a terminal command that takes the generated nec file and runs xnecview on it
		mntmVisualize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String _structureFileName = _structureName + ".nec";
				String currentDir = System.getProperty("user.dir");
			    File folder = new File(currentDir);
				File[] listOfFiles = folder.listFiles();
				//create arraylist to store file names	
				for (File file : listOfFiles) {
					if (file.isFile() && file.getName().contains(".nec")) {
						//if the generated nec file has been generated correctly and exists in the current folder then run the xnecview command
						if(_structureFileName.matches(file.getName())){
							mntmSimulate.setEnabled(true);
							Process xnecview_input;
							Process permissions;
							String xnecview_command;
							xnecview_command = "./xnecview " + _structureFileName;
							try {
								permissions = Runtime.getRuntime().exec("chmod +x xnecview");
								xnecview_input = Runtime.getRuntime().exec(xnecview_command);
								return;
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else
							continue;
					}
				}
				//if the file cannot be found in the current folder then it has not been generated for some reason and cannot be visualized
				JOptionPane.showMessageDialog(BackgroundPane,"The antenna structure has not been generated and therefore cannot be visualized");
				return;
			}
		});
		/// when the simulate menu item is pressed execute nec2c engine to computationally solve the antenna and then generate a .out file then once the .out file process is finished running make the results button available to view
		mntmSimulate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Process permissions = Runtime.getRuntime().exec("chmod +x nec2c");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				SimulateBar.setValue(0);
				//if we are dealing with an existing nec file make all of the customizability components invisible
				if(_structurePath != null){
					AddAntennaPane.setVisible(true);
					GlobalPane.setVisible(false);
					SpecificPane.setVisible(false);
					RenamePane.setVisible(false);
					NumberAntennasPanel.setVisible(false);
				}
				String nec2c_command = "./nec2c -i " + _structureName + ".nec";
				//run nec2c command in a separate thread in order to not freeze up the GUI
				SwingWorker<Void, Double> progressworker = new SwingWorker<Void, Double>(){
					Double progress = new Double(0);
					@Override
					protected Void doInBackground() throws Exception {
						try {
							Process nec2c_engine = Runtime.getRuntime().exec(nec2c_command);
							while(nec2c_engine.isAlive()){
								mntmSimulate.setEnabled(false);
								mntmNewResults.setEnabled(false);
								Thread.sleep(100);
								publish(progress);
								progress+=0.1;
							}
							}	
					    catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						return null;
					}
					
					@Override
					protected void process(List<Double> chunks) {
						lblSimulateProgress.setText("Simulation In Progress");
						double value = chunks.get(chunks.size() - 1);
						double percentage = (value/100)*100;
						int percentage_round = (int) Math.round(percentage);
						SimulateBar.setValue(percentage_round);
						SimulatePane.setVisible(true);
					}
					@Override
					protected void done() {
						lblSimulateProgress.setText("Simulation Complete");
						lblSimulateProgress.setVisible(true);
						SimulateBar.setValue(100);
						mntmSimulate.setEnabled(true);
						mntmNewResults.setEnabled(true);
					}	
				};
				progressworker.execute();
			}
		});
		/// when results button is pressed show the output of the nec file in xnecview
		mntmNewResults.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SimulatePane.setVisible(false);
				Process xnecview_output;
				String xnecview_output_command = "./xnecview " + _structureName + ".out";
				try {
					xnecview_output = Runtime.getRuntime().exec(xnecview_output_command);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		/// this function opens a file chooser component when pressed
		mntmExistingAntennaStructure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mntmVisualize.setEnabled(false);
				AddAntennaPane.setVisible(false);
				mntmAntennaSelect.setEnabled(false);
				mntmSimulate.setEnabled(false);
				mntmNewResults.setEnabled(false);
				StructPane.setVisible(false);
				File currentDirectory = new File(System.getProperty("user.dir"));
				JFileChooser chooseFile = new JFileChooser(currentDirectory);
				int i = chooseFile.showOpenDialog(null);
				if(i == JFileChooser.APPROVE_OPTION){
					File f = chooseFile.getSelectedFile();
					if(f.getName().contains(".nec")){
						String splitString = f.getName().split("[.]")[0];
						_structureName = splitString;
						String structurePath = f.getAbsolutePath();
						String LinuxPath = structurePath.replaceAll("\\s","\\\\ ");
						LinuxPath = LinuxPath.replaceAll(f.getName(), "");
						_structurePath = LinuxPath;
					}
					else{
						JOptionPane.showMessageDialog(BackgroundPane,"This file is not a nec structure file");
						return;
					}
					mntmVisualize.setEnabled(true);
				}
				else if(i == chooseFile.CANCEL_OPTION){
					return;
				}
			}
		});
		/// edit antenna structure button functionality
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String selectedStructureName = antennalist.getSelectedValue().toString();
				CaptureUserDataAndValidate(templateObject, specificTextBoxesList);
				//convert UserSpecificParameters to a primitive double type
				ArrayList<Double> specificValuesList = new ArrayList<Double>();
				for(String specificValueString: UserSetSpecificParameters){
					Double specificValue = Double.parseDouble(specificValueString);
					specificValuesList.add(specificValue);
				}
				Double[] specificValuesArray = new Double[specificValuesList.size()];
				specificValuesArray = specificValuesList.toArray(specificValuesArray);
				//convert to primitive double type instead of object
				double[] specificValuesPrimArray = Stream.of(specificValuesArray).mapToDouble(Double::doubleValue).toArray();
				//finally edit the structures parameter values
				_necObject.editStructure(selectedStructureName, specificValuesPrimArray);
			}
		});
		/// document listeners to force user to re-generate NEC file if they change their frequency values
		StartFreqText.getDocument().addDocumentListener(new DocumentListener() {
		    public void changedUpdate(DocumentEvent e) {
		    	String selectedStructureName = antennalist.getSelectedValue().toString();
		    	
		    }

			@Override
			public void insertUpdate(DocumentEvent e) {
				mntmVisualize.setEnabled(false);
				mntmSimulate.setEnabled(false);
				mntmNewResults.setEnabled(false);
				
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				mntmVisualize.setEnabled(false);
				mntmSimulate.setEnabled(false);
				mntmNewResults.setEnabled(false);
				
			}
		});
		
		EndFreqText.getDocument().addDocumentListener(new DocumentListener() {
		    public void changedUpdate(DocumentEvent e) {
		    	 
		    }

			@Override
			public void insertUpdate(DocumentEvent e) {
				mntmVisualize.setEnabled(false);
				mntmSimulate.setEnabled(false);
				mntmNewResults.setEnabled(false);
				
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				mntmVisualize.setEnabled(false);
				mntmSimulate.setEnabled(false);
				mntmNewResults.setEnabled(false);
				
			}
		});
		NoStepsText.getDocument().addDocumentListener(new DocumentListener() {
		    public void changedUpdate(DocumentEvent e) {
		    	 
		    }

			@Override
			public void insertUpdate(DocumentEvent e) {
				mntmVisualize.setEnabled(false);
				mntmSimulate.setEnabled(false);
				mntmNewResults.setEnabled(false);
				
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				mntmVisualize.setEnabled(false);
				mntmSimulate.setEnabled(false);
				mntmNewResults.setEnabled(false);
			}
		});
	}
	
	public String getStructureName(){
		return _structureName;
	}
}
