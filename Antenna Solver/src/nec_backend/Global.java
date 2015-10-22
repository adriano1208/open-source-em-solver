package nec_backend;

//import java.util.concurrent.locks.Condition;

public class Global {
	public Global(){
		setFrequency(1e6);
		setGround(false);
	}
	public static String generateComments(){
		return "";
	}
// 2  5   10        20        30        40        50        60        70        80
	public static String generateExecution(){
		return _FR() + _GN() + _RP();
	}
	
//-----------------------------------------------------//
//----------------------Frequency----------------------//
//-----------------------------------------------------//
	//--Setters--//
	public static void setFrequency(double f_low){
		if (f_low >= 0)
		{
			_f_low = f_low;
			_f_high = f_low;
			_f_noSteps = 0;
			
			Structure.setMeshSize(c*0.1/f_low);
		}
	}
	public static void setFrequency(double f_low, double f_high, boolean f_isMultiplicative, int f_noSteps){
		if (f_high >= f_low && f_low >= 0)
		{
			_f_low = f_low;
			_f_high = f_high;
			
			if (f_isMultiplicative)
				_f_isMultiplicative = 1;
			else
				_f_isMultiplicative = 0;
			
			if (_f_low == _f_high)
				_f_noSteps = 0;
			else 
				_f_noSteps = f_noSteps;
			if (_f_noSteps == 0)
				_f_high = _f_low;
			
			if (f_isMultiplicative())
				_f_increment = Math.pow(_f_high/_f_low, 1/_f_noSteps);
			else
				_f_increment = (_f_high-_f_low)/(_f_noSteps);
			
			Structure.setMeshSize(c*0.1/f_high);
		}
	}
	//--Getters--//
	public static double f_low() {
		return _f_low;
	}
	public static double f_high() {
		return _f_high;
	}
	public static boolean f_isMultiplicative() {
		if (_f_isMultiplicative == 1)
			return true;
		else
			return false;
	}		
	public static int f_noSteps() {
		return _f_noSteps;
	}
	public static double f_increment() {
		return _f_increment;
	}
	//--Private--//
	private static double _f_low; //lowest frequency (single frequency variable)
	private static double _f_high; //highest frequency
	private static int _f_isMultiplicative; //if false then a linear spacing between 
										//frequencies is used, else multiplicative
	private static int _f_noSteps; //the number of steps between high and low frequency
	private static double _f_increment; //the increment between linear spacing 
								 //or multiplier between multiplicative spacing
	
	private static double c = 299.792458; //[m/s]
	
	//FR | frequency stepping | no of steps  | NULL | NULL | start frequency | frequency increment
	private static String _FR(){
		return "FR " + String.valueOf(_f_isMultiplicative) + " " + String.valueOf(_f_noSteps) + " 0 0 " +
				String.valueOf(_f_low) + " " + String.valueOf(_f_increment) + "\n"; 
	}
	
//-----------------------------------------------------//
//-----------------------Ground------------------------//
//-----------------------------------------------------//
	//--Setters--//
	public static void setGround(boolean isGround) {
		if (isGround)
			_g_type = 1;
		else
			_g_type = -1;
	}
	public static boolean setGround(double dielectricConstant, double conductivity) {
		if (dielectricConstant > 0 && conductivity != 0)
		{
			_g_type = 2;
			_g_dielectric = dielectricConstant;
			_g_conductivity = conductivity;
			return true;
		}
		else
			return false;
	}
	//--Getters--//
	public static boolean g_isGround() {
		if (_g_type == -1)
			return false;
		else
			return true;
	}
	public static boolean g_isPerfect() {
		if (_g_type == 1)
			return true;
		else
			return false;
	}
	public static double g_dielectric() {
		return _g_dielectric;
	}
	public static double g_conductivity() {
		return _g_conductivity;
	}
	//--Private--//
	private static int _g_type;
	private static double _g_dielectric;
	private static double _g_conductivity;
	
	//GN | type | No. radial wires | interpolate | shape | dielectric | conductivity | screen radius | wire radius
	private static String _GN(){
		if (_g_type == -1) //Free Space
			return "GN -1\n";
		if (_g_type == 1)
			return "GN 1\n";
		//if (_g_type == 2)
		else
			return "GN 2 0 1 0 " + String.valueOf(_g_dielectric) + " " + String.valueOf(_g_conductivity) + "\n";
	}
	
//-----------------------------------------------------//
//-----------------Radiation Pattern-------------------//
//-----------------------------------------------------//
	//RP | wave | no theta | no phi | control | start theta | start phi | delta theta | delta phi | distance | norm factor
	// 2  5   10        20        30        40        50        60        70        80
	//RP 0 180 360 1000 0 0 1 1 0 0
	private static String _RP(){
		return "RP 0 180 360 1000 0 0 5 5 0 0\n";
	}
	

}
