package nec_backend;

import java.io.BufferedWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class NecGenerator {
	public NecGenerator(String filename){
		_filename = filename;
//		_global = new Global();
		_structures = new ArrayList<Structure>();
		_comments = new ArrayList<String>();
		_geometry = new ArrayList<String>();
		_execution = new ArrayList<String>();
	}
	//*************************//
	//---Global Functions---//
	//*************************//
	//FREQUENCY//
	public void setFrequency(double f_low){
		Global.setFrequency(f_low);
	}
	public void setFrequency(double f_low, double f_high, boolean f_isMultiplicative, int f_noSteps){
		Global.setFrequency(f_low, f_high, f_isMultiplicative, f_noSteps);
	}
	public double f_low() {
		return Global.f_low();}
	public double f_high() {
		return Global.f_high();}
	public boolean f_isMultiplicative() {
		return Global.f_isMultiplicative();}
	public int f_noSteps() {
		return Global.f_noSteps();}
	public double f_increment() {
		return Global.f_increment();}
	//GROUND//
	public void setGround(boolean isGround){
		Global.setGround(isGround);
	}
	public void setGround(double dielectricConstant, double conductivity) {
		Global.setGround(dielectricConstant, conductivity);
	}
	public boolean g_isGround() {
		return Global.g_isGround();}
	public boolean g_isPerfect() {
		return Global.g_isPerfect();}
	public double g_dielectric() {
		return Global.g_dielectric();}
	public double g_conductivity() {
		return Global.g_conductivity();}
	//*************************//
	//---Structure Functions---//
	//*************************//
	public boolean addStructure(String name, ArrayList<String> rules, ArrayList<String> labels, double[] parameters){
		if (indexOfStructure(name) != -1)
			return false;
		else {
			_structures.add(new Structure(name, rules, labels, parameters));
			return true;
		}
	}
	public boolean removeStructure(String name){
		int i = indexOfStructure(name);
		if (i == -1)
			return false;
		else {
			_structures.remove(i);
			return true;
		}	
	}
	public boolean editStructure(String name, double[] parameters){
		int i = indexOfStructure(name);
		if (i == -1)
			return false;
		else if (parameters.length != _structures.get(i).parameters().length)
			return false;
		else {
			_structures.get(i).set_parameters(parameters);
			return true;
		}
	}
	public boolean renameStructure(String oldName, String newName){
		int i = indexOfStructure(newName);
		if (i != -1)
			return false;
		else
			i = indexOfStructure(oldName);
		if (i == -1)
			return false;
		else {
			_structures.get(i).rename(newName);
			return true;
		}
	}
	public boolean doesStructureExist(String name){
		if (indexOfStructure(name) == -1)
			return false;
		else
			return true;
	}
	public ArrayList<String> getStructureNames(){
		ArrayList<String> names = new ArrayList<String>();
		for (Structure structure:_structures){
			names.add(structure.name());
		}
		return names;
	}
	public ArrayList<String> getStructureRules(String name){
		try {
		for (Structure structure:_structures){
			if (structure.name() == name)
				return structure.rules();
		}
		throw new IOException();
	}
	catch (IOException e) {
		System.err.print("NoStructureWithThatName: " + e);
		return null;
	}
	}
	public ArrayList<String> getStructureLabels(String name){
		try {
		for (Structure structure:_structures){
			if (structure.name() == name)
				return structure.labels();
		}
		throw new IOException();
	}
	catch (IOException e) {
		System.err.print("NoStructureWithThatName: " + e);
		return null;
	}
	}
	public double[] getStructureParameters(String name){
	try{
		for (Structure structure:_structures){
			if (structure.name() == name)
				return structure.parameters();
		}
		throw new IOException();
	}
	catch (IOException e) {
		System.err.print("NoStructureWithThatName: " + e);
		return null;
	}
	}
	
	//**************************//
	//---Generation Functions---//
	//**************************//
	public String filename() {
		return _filename;
	}
	public void renameFile(String _filename) {
		this._filename = _filename;
	}
	public void generateNEC(){
		Structure.resetGeometry(); 
		_comments.clear();
		_execution.clear();
		_geometry.clear();
		
		String NECcontents = "CM " + String.valueOf(_filename) + "\n";
		for (Structure structure:_structures)
		{
			_comments.add(structure.generateComments());
			_execution.add(structure.generateExecution());
			_geometry.add(structure.generateGeometry());
		}
		_comments.add(Global.generateComments());
		_execution.add(Global.generateExecution());
		//comments write
		for (String comment : _comments){
			NECcontents = NECcontents.concat(comment);
		}
		NECcontents = NECcontents.concat("CE\n");
		//geometry write
		for (String geometry : _geometry){
			NECcontents = NECcontents.concat(geometry);
		}
		NECcontents = NECcontents.concat("GE\n");
		//execution write
		for (String execution : _execution){
			NECcontents = NECcontents.concat(execution);
		}
		NECcontents = NECcontents.concat("EN");
		writeToFile(NECcontents);
	}
	
	//**************************//
	//--Optimisation Functions--//
	//**************************//
//	public void setOptimisationParameters(){
//		
//	}
//	public void setOptimisationGoals(){
//		
//	}
//	public boolean optimise(){
//		_optimisation.runOptimisation();
//		
//		return false;
//	}
//	public void stopOptimisation(){
//		//not sure how to implement this
//	}
	
//...............................................................................//
//  Private variables............................................................//
//...............................................................................//
	private String _filename;		//The name of the .NEC file to be generated
	//--------------------------//
//	private Global _global;
	private ArrayList<Structure> _structures;
//	private Optimisation _optimisation;
	//--------------------------//
	private ArrayList<String> _comments;
	private ArrayList<String> _geometry;
	private ArrayList<String> _execution;
	
	//**************************//
	//-----Private Functions----//
	//**************************//	
	private int indexOfStructure(String name){
		int i=0;
		for (Structure structure:_structures)
		{
			if (structure.name() == name)
				return i;
			i++;
		}
		return -1;
	}
	private void writeToFile(String string){
		//create file
//		String currentDirectory = System.getProperty("user.dir");
		File NECfile = new File(_filename + ".nec");
		try {
			NECfile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//write to file
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(NECfile));
			bw.write(string);
			//bw.newLine();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
