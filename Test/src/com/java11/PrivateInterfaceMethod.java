package com.java11;

public interface PrivateInterfaceMethod {
	private static void testPrivateMethod(String stroke) {
		System.out.println("Move into position\nHitting a " + stroke + "\nMove back into ready position");
	}
	
	static void forehand() { testPrivateMethod("forehand"); }
	static void backhand() { testPrivateMethod("backhand"); }
	static void smash() { testPrivateMethod("smash"); }
	
	public static void explanationAndExample() {
		System.out.println("\n	PrivateInterfaceMethod\n"
				+ "Introduced in Java 9, interface can now have private methods.\r\n"
				+ "These private methods can be both static and non-static.\r\n"
				+ "As they are private, they are accessible within the interface only.\r\n"
				+ "As with classes, you cannot access a non-static interface method from a static method.");
		System.out.println("	private static void testPrivateMethod(String stroke) {\r\n"
				+ "		System.out.println(\"Move into position\\nHitting a \" + stroke + \"\\nMove back into ready position\");\r\n"
				+ "	}\r\n"
				+ "	\r\n"
				+ "	static void forehand() { testPrivateMethod(\"forehand\"); }\r\n"
				+ "	static void backhand() { testPrivateMethod(\"backhand\"); }\r\n"
				+ "	static void smash() { testPrivateMethod(\"smash\"); }");
	}
}
