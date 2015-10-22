
import static org.junit.Assert.*;
//import file_operation.*;
import nec_backend.*;
import java.util.ArrayList;

import org.junit.Test;

public class NecGeneratorTest {

	@Test
	public void addStructureTrueTest() {
		ArrayList<String> rulesList = new ArrayList<String>();
		ArrayList<String> labelsList = new ArrayList<String>();
		//populate these lists with test values
		for(int i = 0; i < 5; i++){
			rulesList.add("rules");
			labelsList.add("labels");
		}
		double[] parameters = {1,2,3,4,5};
		
		NecGenerator gen = new NecGenerator("testfilename");
		boolean expectedOutput = true;
		boolean actualOutput = gen.addStructure("testname", rulesList, labelsList, parameters);
		assertEquals(expectedOutput,actualOutput);
		actualOutput = gen.addStructure("anothername", rulesList, labelsList, parameters);
		assertEquals(expectedOutput,actualOutput);
	}
	@Test
	public void addStructureFalseTest() {
		ArrayList<String> rulesList = new ArrayList<String>();
		ArrayList<String> labelsList = new ArrayList<String>();
		//populate these lists with test values
		for(int i = 0; i < 5; i++){
			rulesList.add("rules");
			labelsList.add("labels");
		}
		double[] parameters = {1,2,3,4,5};
		
		NecGenerator gen = new NecGenerator("testfilename");
		boolean expectedOutput = false;
		gen.addStructure("testname", rulesList, labelsList, parameters);
		boolean actualOutput = gen.addStructure("testname", rulesList, labelsList, parameters);
		assertEquals(expectedOutput,actualOutput);
	}
	
	@Test
	public void removeStructureTrueTest() {
		ArrayList<String> rulesList = new ArrayList<String>();
		ArrayList<String> labelsList = new ArrayList<String>();
		//populate these lists with test values
		for(int i = 0; i < 5; i++){
			rulesList.add("rules");
			labelsList.add("labels");
		}
		double[] parameters = {1,2,3,4,5};
		
		NecGenerator gen = new NecGenerator("testfilename");
		boolean expectedOutput = true;
		gen.addStructure("testname", rulesList, labelsList, parameters);
		boolean actualOutput = gen.removeStructure("testname");
		assertEquals(expectedOutput,actualOutput);
	}
	@Test
	public void removeStructureFalseTest() {
		ArrayList<String> rulesList = new ArrayList<String>();
		ArrayList<String> labelsList = new ArrayList<String>();
		//populate these lists with test values
		for(int i = 0; i < 5; i++){
			rulesList.add("rules");
			labelsList.add("labels");
		}
		double[] parameters = {1,2,3,4,5};
		
		NecGenerator gen = new NecGenerator("testfilename");
		boolean expectedOutput = false;
		boolean actualOutput = gen.removeStructure("testname2");
		assertEquals(expectedOutput,actualOutput);
		
		gen.addStructure("testname", rulesList, labelsList, parameters);
		actualOutput = gen.removeStructure("testname2");
		assertEquals(expectedOutput,actualOutput);	
	}
	
	@Test
	public void editStructureTrueTest() {
		ArrayList<String> rulesList = new ArrayList<String>();
		ArrayList<String> labelsList = new ArrayList<String>();
		//populate these lists with test values
		for(int i = 0; i < 5; i++){
			rulesList.add("rules");
			labelsList.add("labels");
		}
		double[] parameters = {1,2,3,4,5};
		
		NecGenerator gen = new NecGenerator("testfilename");
		boolean expectedOutput = true;
		gen.addStructure("testname", rulesList, labelsList, parameters);
		boolean actualOutput = gen.editStructure("testname", parameters);
		assertEquals(expectedOutput,actualOutput);
	}
	@Test
	public void editStructureFalseTest() {
		ArrayList<String> rulesList = new ArrayList<String>();
		ArrayList<String> labelsList = new ArrayList<String>();
		//populate these lists with test values
		for(int i = 0; i < 5; i++){
			rulesList.add("rules");
			labelsList.add("labels");
		}
		double[] parameters = {1,2,3,4,5};
		double[] parameters2 = {1,2,3,4};
		
		NecGenerator gen = new NecGenerator("testfilename");
		boolean expectedOutput = false;
		gen.addStructure("testname", rulesList, labelsList, parameters);
		boolean actualOutput = gen.editStructure("anothername", parameters);
		assertEquals(expectedOutput,actualOutput);
		
		actualOutput = gen.editStructure("testname", parameters2);
	}
	
	@Test
	public void renameStructureTest() {
		ArrayList<String> rulesList = new ArrayList<String>();
		ArrayList<String> labelsList = new ArrayList<String>();
		//populate these lists with test values
		for(int i = 0; i < 5; i++){
			rulesList.add("rules");
			labelsList.add("labels");
		}
		double[] parameters = {1,2,3,4,5};
		
		NecGenerator gen = new NecGenerator("testfilename");
		boolean expectedOutput = true;
		gen.addStructure("testname", rulesList, labelsList, parameters);
		gen.addStructure("anothertestname", rulesList, labelsList, parameters);
		boolean actualOutput = gen.renameStructure("testname","newname");
		assertEquals(expectedOutput,actualOutput);
		
		expectedOutput = false;
		actualOutput = gen.renameStructure("testname","mynewnewname");
		assertEquals(expectedOutput,actualOutput);
		actualOutput = gen.renameStructure("newname","anothertestname");
		assertEquals(expectedOutput,actualOutput);
	}
	
	@Test
	public void doesStructureExistTest() {
		ArrayList<String> rulesList = new ArrayList<String>();
		ArrayList<String> labelsList = new ArrayList<String>();
		//populate these lists with test values
		for(int i = 0; i < 5; i++){
			rulesList.add("rules");
			labelsList.add("labels");
		}
		double[] parameters = {1,2,3,4,5};
		
		NecGenerator gen = new NecGenerator("testfilename");
		boolean expectedOutput = true;
		gen.addStructure("testname", rulesList, labelsList, parameters);
		boolean actualOutput = gen.doesStructureExist("testname");
		assertEquals(expectedOutput,actualOutput);
		
		expectedOutput = false;
		actualOutput = gen.doesStructureExist("anothername");
		assertEquals(expectedOutput,actualOutput);
	}

	@Test
	public void getStructureNamesTest() {
		ArrayList<String> rulesList = new ArrayList<String>();
		ArrayList<String> labelsList = new ArrayList<String>();
		//populate these lists with test values
		for(int i = 0; i < 5; i++){
			rulesList.add("rules");
			labelsList.add("labels");
		}
		double[] parameters = {1,2,3,4,5};
		
		NecGenerator gen = new NecGenerator("testfilename");
		ArrayList<String> expectedOutput = new ArrayList<String>();
			expectedOutput.add("testname");
			expectedOutput.add("Hooray");
			expectedOutput.add("WhereU@");
			expectedOutput.add("feeling22");
		ArrayList<String> actualOutput = new ArrayList<String>();
		gen.addStructure("testname", rulesList, labelsList, parameters);
		gen.addStructure("Hooray", rulesList, labelsList, parameters);
		gen.addStructure("WhereU@", rulesList, labelsList, parameters);
		gen.addStructure("feeling22", rulesList, labelsList, parameters);
		actualOutput = gen.getStructureNames() ;
		assertEquals(expectedOutput,actualOutput);
	}
	
	@Test
	public void getStructureRulesTest() {
		ArrayList<String> rulesList = new ArrayList<String>();
		ArrayList<String> rulesList2 = new ArrayList<String>();
		ArrayList<String> labelsList = new ArrayList<String>();
		//populate these lists with test values
		for(int i = 0; i < 5; i++){
			rulesList.add("rules");
			labelsList.add("labels");
		}
		rulesList2.add("my test 2");
		double[] parameters = {1,2,3,4,5};
		
		NecGenerator gen = new NecGenerator("testfilename");
		ArrayList<String> expectedOutput = rulesList;
		ArrayList<String> actualOutput = new ArrayList<String>();
		gen.addStructure("testname", rulesList, labelsList, parameters);
		gen.addStructure("Hooray", rulesList2, labelsList, parameters);
		actualOutput = gen.getStructureRules("testname");
		assertEquals(expectedOutput,actualOutput);
		expectedOutput = rulesList2;
		actualOutput = gen.getStructureRules("Hooray");
		assertEquals(expectedOutput,actualOutput);
	}
//	@Test(expected=IOException.class)
//	public void getStructureRulesThrowTest() {
//		ArrayList<String> rulesList = new ArrayList<String>();
//		ArrayList<String> labelsList = new ArrayList<String>();
//		//populate these lists with test values
//		for(int i = 0; i < 5; i++){
//			rulesList.add("rules");
//			labelsList.add("labels");
//		}
//		double[] parameters = {1,2,3,4,5};
//		
//		NecGenerator gen = new NecGenerator("testfilename");
//		gen.addStructure("testname", rulesList, labelsList, parameters);
//		gen.getStructureRules("hello");
//	}
	
	@Test
	public void getStructureParametersTest() {
		ArrayList<String> rulesList = new ArrayList<String>();
		ArrayList<String> labelsList = new ArrayList<String>();
		//populate these lists with test values
		for(int i = 0; i < 5; i++){
			rulesList.add("rules");
			labelsList.add("labels");
		}
		double[] parameters = {1,2,3,4,5};
		
		NecGenerator gen = new NecGenerator("testfilename");
		double[] expectedOutput = parameters;
		double[] actualOutput;
		gen.addStructure("testname", rulesList, labelsList, parameters);
		actualOutput = gen.getStructureParameters("testname");
		assertEquals(expectedOutput,actualOutput);
	}
//	@Test(expected=IOException.class)
//	public void getStructureParametersThrowTest() {
//		ArrayList<String> rulesList = new ArrayList<String>();
//		ArrayList<String> labelsList = new ArrayList<String>();
//		//populate these lists with test values
//		for(int i = 0; i < 5; i++){
//			rulesList.add("rules");
//			labelsList.add("labels");
//		}
//		double[] parameters = {1,2,3,4,5};
//		
//		NecGenerator gen = new NecGenerator("testfilename");
//		double[] actualOutput;
//		gen.addStructure("testname", rulesList, labelsList, parameters);
//		actualOutput = gen.getStructureParameters("hello");
//	}
	
	@Test
	public void frequencyTest(){
		NecGenerator gen = new NecGenerator("testfilename");
		double F_low = 10E6;
		double F_high = 80E6;
		boolean Mult = false;
		int No_steps = 14;
		double Increment = 5.0E6;
		gen.setFrequency(F_low);
		assertEquals(F_low, gen.f_low(), 0.01);
		assertEquals(F_low, gen.f_high(), 0.01);
		assertEquals(Mult, gen.f_isMultiplicative());
		assertEquals(0, gen.f_noSteps());
		assertEquals(0, gen.f_increment(), 0.01);
		
		gen.setFrequency(F_low, F_high, Mult, No_steps);
		assertEquals(F_low, gen.f_low(), 0.01);
		assertEquals(F_high, gen.f_high(), 0.01);
		assertEquals(Mult, gen.f_isMultiplicative());
		assertEquals(No_steps, gen.f_noSteps());	
		assertEquals(Increment, gen.f_increment(), 1);	
	}
	
	@Test
	public void groundTest() {
		NecGenerator gen = new NecGenerator("testfilename");
		double dielectric = 0.0003;
		double conductivity = 4.2;
		gen.setGround(false);
		assertEquals(false, gen.g_isGround());
		gen.setGround(true);
		assertEquals(true, gen.g_isGround());
		assertEquals(true, gen.g_isPerfect());
		gen.setGround(dielectric, conductivity);
		assertEquals(true, gen.g_isGround());
		assertEquals(false, gen.g_isPerfect());
		System.out.println(gen.g_dielectric() + " " + gen.g_conductivity());
		assertEquals(dielectric, gen.g_dielectric(), 0.00001);
		assertEquals(conductivity, gen.g_conductivity(), 0.00001);
		
	}
	
	@Test
	public void generateNECTest() {
		ArrayList<String> rules = new ArrayList<String>();
		rules.add("EX:0:001:50:0:1:0:0");
		rules.add("GW:001:0:0:0:0:0:V02:V01/(V0+V8)");
		ArrayList<String> labels= new ArrayList<String>();
		for(int i = 0; i < 9; i++){
			labels.add("label" + i);
		}
		double[] parameters = {1,2,3,4,5,6,7,8,9};
		
		NecGenerator gen = new NecGenerator("testfilename");
		gen.setFrequency(100);
		gen.setGround(false);
		gen.addStructure("testname", rules, labels, parameters);
		gen.generateNEC();
		
	}
}
