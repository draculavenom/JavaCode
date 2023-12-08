package com.strings;

import java.util.Arrays;

import com.utilities.PrintingUtility;

public class TestingStrings implements PrintingUtility{
	public void test1() {
		println("Some of the most used methods for Strings:");
		String text = "This is a text that will be used for testing purposes";
		p("String text = \"This is a text that will be used for testing purposes\";");
		p("println(\"\" + text.charAt(0) + text.charAt(5) + text.charAt(10));");
		println("" + text.charAt(0) + text.charAt(5) + text.charAt(10));
		p("println(text.compareTo(\"some other text\"));");
		println(text.compareTo("some other text"));
		p("println(\"Hello \".concat(\"World\"));");
		println("Hello ".concat("World"));
		p("println(text.contains(\"u\"));");
		println(text.contains("u"));
		p("println(\"dracula\".equals(\"dracula\"));");
		println("dracula".equals("dracula"));
		p("println(\"dracula\".equals(\"Dracula\"));");
		println("dracula".equals("Dracula"));
		p("println(\"dracula\".equalsIgnoreCase(\"Dracula\"));");
		println("dracula".equalsIgnoreCase("Dracula"));
		p("println(text.indexOf(\"se\"));");
		println(text.indexOf("se"));
		p("println(text.lastIndexOf(\"se\"));");
		println(text.lastIndexOf("se"));
		p("println(text.length());");
		println(text.length());
		p("println(text.replace(\"i\",\"I\"));");
		println(text.replace("i","I"));
		p("println(Arrays.toString(text.split(\" \")));");
		println(Arrays.toString(text.split(" ")));
		p("println(text.substring(10));");
		println(text.substring(10));
		p("println(text.substring(10, 20));");
		println(text.substring(10, 20));
		p("println(text.toLowerCase());");
		println(text.toLowerCase());
		p("println(text.toUpperCase());");
		println(text.toUpperCase());
		p("println(\" Hello World \".trim());");
		println(" Hello World ".trim());
	}
}
