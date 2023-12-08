package com.java11;

public class NewFeatures {
	LocalVariableTypeInference lvti;
	
	public NewFeatures() {
		 lvti= new LocalVariableTypeInference();
	}
	
	public void testLVTI() {// test LocalVariableTypeInference
		lvti.explanation();
		lvti.example();
	}
	
	public void testPIM() {//PrivateInterfaceMethod
		PrivateInterfaceMethod.explanationAndExample();		
	}
	
	public void testAE() {//AdvancedEncapsulation
		AdvancedEncapsulation ae = new AdvancedEncapsulation();
		ae.explanation();
	}
}
