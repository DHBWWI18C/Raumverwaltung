package com.Mosbach.Raumverwaltung.domain;

public class IntBoolHelper {
	public static boolean intToBool(int intValue){
		if (intValue == 1) return true;
		return false;
	}
	
	public static int boolToInt(boolean boolValue){
		if (boolValue) return 1;
		return 0;
	}
}
