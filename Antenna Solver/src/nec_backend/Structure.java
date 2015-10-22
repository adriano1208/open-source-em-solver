package nec_backend;

//import java.io.IOException;
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.regex.Pattern;
//import java.util.Map.Entry;

public class Structure {
	//------------------------//
	//------Constructors------//
	//------------------------//
	public Structure(String name, ArrayList<String> rules, ArrayList<String> labels, double[] parameters){
		_name = name;
		_rules = rules;
		_labels = labels;
		set_parameters(parameters);
		_wireIndex = new ArrayList<Integer>();
		_wirePositions = new ArrayList<Double>();
	}
	//-------------------------//
	//-----Public functions----//
	//-------------------------//
	public static void resetGeometry(){
		_tagCounter = 0;
	}
	public String generateComments(){
		String str = "CM ********************************\nCM " + String.valueOf(_name) + "\n";
		for (int i=0; i < _parameters.length; i++ ){
			str = str.concat("CM " + String.valueOf(_labels.get(i)) + ": " + String.valueOf(_parameters[i]) + "\n");
		}
		return str;
	}
	public String generateExecution(){
		_wirePositions.clear();
		_wirePositions.add(0.0);
		_wireIndex.clear();
		_wireIndex.add(_tagCounter);
		int Tag;
		String str = "";
		String[] inStr;
		inStr = new String[8]; //Max variables for a basic EX comment card, segmentation is handled automatically 
		//0 :1   :2  :3       :4   :5   :6    :7
		//EX:Type:Tag:Position:Table:Real:Imag:Norm   (Type, Table and Norm set to 0 since not implemented)
		//EX | type | tag | segment | table | real voltage | imaginary voltage | norm value
		//
		//LD:Type:Tag:???
		//LD | type | tag | first segment | last segment | resistance | inductance | capacitance
		for (String rule:_rules){
			if (rule.contains("EX")){
				inStr = rule.split(":");
				inStr = Helper.solveLine(inStr,_parameters);
				Tag = _wireIndex.get(0) + _wireIndex.size();
				_wireIndex.add((int)Math.round(Double.parseDouble(inStr[2]))); //2 - Structure-specific Tag
				_wirePositions.add(Double.parseDouble(inStr[3])); //3 - position along wire [%]
				_tagCounter++; //TODO is this necessary?
				str = str.concat("EX ");
				if ((int)Math.round(Double.parseDouble(inStr[1])) == 5) //accomodates Current Source
					str = str.concat("5 ");
				else		//otherwise defaults to Voltage Source
				str = str.concat("0 ");
				str = str.concat(Tag + " 1 0");
				str = str.concat(Helper.getString(inStr,5));
				str = str.concat("\n");
//				str = str.concat(String.format("%1$-8s %2$-2s %3$-3s %4$-13s %5$-13s %6$-13s % ",
//						Tag, "1", "0",
//						inStr[5],
//						inStr[6],
//						inStr[7] + "\n"));
			} //else if (rule.contains("LD")){
				//_tagCounter++;
			//}
		}
//		str = str.concat(moveAllCardHack());
		return str;
	}	
	public String generateGeometry(){
		String str = "";
		String[] inStr;
		inStr = new String[9]; //Max variables for a GW comment card, segmentation is handled automatically 
		ArrayList<Integer> newTags = new ArrayList<Integer>();
		ArrayList<Double> positions = new ArrayList<Double>();
		int[] newTagsArray;
		double[] positionsArray;
		int counter1 = 0;
		int counter2 = 0;
		
		int tag = 0;
		double[] p_start = {0,0,0};
		double[] p1 = {0,0,0};
		double[] p2 = {0,0,0};
		double[] p_end = {0,0,0};
		double[] m_unit = {0,0,0};
		double[] m = {0,0,0};
		double radius = 0;
		int noSeg;
		double wireLength;
		double segLength;
		
		//0 :1  :2 :3 :4 :5 :6 :7 :8 
		//GW:Tag:X1:Y1:Z1:X2:Y2:Z3:radius
		for (String rule:_rules){
			if (rule.contains("GW")){
				inStr = rule.split(":");
				inStr = Helper.solveLine(inStr, _parameters);
				// save information from the line
				tag = (int)Double.parseDouble(inStr[1]);
				p_start[0] = Double.parseDouble(inStr[2]);
				p_start[1] = Double.parseDouble(inStr[3]);
				p_start[2] = Double.parseDouble(inStr[4]);
				p_end[0] = Double.parseDouble(inStr[5]);
				p_end[1] = Double.parseDouble(inStr[6]);
				p_end[2] = Double.parseDouble(inStr[7]);
				radius = Double.parseDouble(inStr[8]);
				//rotate and translate the input wire according to V00-V05
				p_start = Helper.moveCoord(p_start, _parameters);
				p_end = Helper.moveCoord(p_end, _parameters);
				// get the unit vector from the start to end point
				wireLength = Helper.getLength(p_start,p_end); // total wire length
				m_unit = Helper.getUnitVector(p_start, p_end); // unit vector
				// get which wires are important enough to be labelled
				newTags.clear();
				for (int i=0; i < _wireIndex.size(); i++){//check if matches important tag
					if (_wireIndex.get(i) == tag){
						newTags.add(_wireIndex.get(0) + i);
						positions.add(_wirePositions.get(i));
					}	
				}
				// if there is an excitation on the very ends, no dummy ends are needed
				counter1=0; counter2=0;
				for (double position : positions) {
					if (position/100 <= _meshSize/wireLength) {
						counter1++;
						position = 0.0;
					}
					if (position/100 >= 1 - _meshSize/wireLength) {
						counter2++;
						position = 100.0;
					}
//					for (double position2 : positions) {
//						if (position < position2 + _meshSize/wireLength &&
//								position2 > position - _meshSize/wireLength)
//							throw
//					}
				}
				if (counter1 == 0) {
					positions.add(0.0);	
					newTags.add(0);
				} //else throw
				if (counter2 == 0) {
					positions.add(100.0);
					newTags.add(0);
				} //else throw
				
				// now the number of positions won't change and we need array form to sort
				newTagsArray = new int[newTags.size()];
				positionsArray = new double[positions.size()];
				for (int i=0; i < positionsArray.length; i++){
					newTagsArray[i] = newTags.get(i).intValue();
					positionsArray[i] = positions.get(i).doubleValue();
				}
				newTagsArray = Helper.BubbleSort(positionsArray, newTagsArray);
				positionsArray = Helper.BubbleSort(positionsArray);
				// now for each subwire (between each start/end and excitation/load)
				noSeg = 0;
				p1 = p_start;
				for (int i=0; i < positionsArray.length - 1; i++) {
					m = Helper.coordMult(m_unit, (wireLength * 
							( (positionsArray[i+1]/100) - (Helper.getLength(p_start, p1)/wireLength) ) )); //subwire vector
					p2 = Helper.coordAdd(p1,m); //end point of subwire
					noSeg = (int)Math.ceil(Helper.getLength(p1,p2)/_meshSize); //min segments needed
					segLength = Helper.getLength(p1,p2)/noSeg; //segment length in current subwire
					if (i < positionsArray.length - 2) { //case for end subwire if false
						segLength = segLength*noSeg/(noSeg+0.5); //shift centre of excited wire to centre
					}
					m = Helper.coordMult(m_unit, segLength); // segment vector
					
					// the subwire is segmented according to the meshsize
					for (int j=0; j < noSeg; j++) {
						p2 = Helper.coordAdd(p1, m);
						//GW | tag | segments | x1 | y1 | z1 | x2 | y2 | z2 | radius
						if (j == 0)
							str = str.concat("GW" + String.format("%1$3s",Integer.toString(newTagsArray[i])) + " 1  ");
						else
							str = str.concat("GW" + "  0" + " 1  ");
						str = str.concat(String.format("%1$-13s %2$-13s %3$-13s %4$-13s %5$-13s %6$-13s",
										Helper.padRight(p1[0]),
										Helper.padRight(p1[1]),
										Helper.padRight(p1[2]),
										Helper.padRight(p2[0]),
										Helper.padRight(p2[1]),
										Helper.padRight(p2[2])));
						str = str.concat(Double.toString(radius) + "\n");
						p1 = Helper.coordAdd(p2, 0);
					}
					//set the end of the subwire to the start of the next
					p1 = Helper.coordAdd(p2, 0);
				}
				
				
			}
		}
		//TODO implement translation and rotation code
		return str;
	}
	
	//-------------------------//
	//---------Setters---------//
	//-------------------------//	
	public void rename(String name){
		_name = name;
	}
	public void set_parameters(double[] parameters){
		_tagCounter = 0;
		_parameters = parameters;
	}
	
	//-------------------------//
	//---------Getters---------//
	//-------------------------//	
	public String name(){
		return _name;
	}
	public ArrayList<String> rules(){
		return _rules;
	}
	public ArrayList<String> labels(){
		return _labels;
	}
	public double[] parameters(){
		return _parameters;
	}
	public static int noImportantWires(){
		return _tagCounter;
	}
	public static void setMeshSize(double meshSize){
		_meshSize = meshSize;
	}
	
//----------------------------------------------------------------------------------------//
	//-------------------------//
	//----Private variables----//
	//-------------------------//
	private String _name; //the name of the antenna define by the template
	private final ArrayList<String> _rules; //the rules for generating the antenna-specific 
									  //NEC file -- passed in from the template file
	private final ArrayList<String> _labels;
	private double[] _parameters; //the user-defined parameters needed for the
								  //rules to generate a NEC file
	private ArrayList<Integer> _wireIndex; //index of wires with excitations and loads
											//on them will have to be kept track of specifically
	private ArrayList<Double> _wirePositions; //works with _wireIndex to keep track of where
											  //on the wire [%] the LDs and EXs go (for segmentation-algorithm)
	
	private static int _tagCounter; //counter for the number of important wires across all structures
	private static double _meshSize; //the MoM maximum segment radius (tenth of a wavelength)
	
	//-------------------------//
	//----Private functions----//
	//-------------------------//	

	
}
