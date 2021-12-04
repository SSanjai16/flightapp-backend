package com.sanjai.config;

import java.util.Random;

public class Randomstringidgenerator {
	private static final String CHARACTER_SET = "23456789abcdefghijkmnpqrstuvwxyzABCDEFGHIJKLMNPQRSTUVWXYZ";
	private static Random rnd = new Random();
	 public String generateid(int length)
	 {
		 StringBuilder builder = new StringBuilder();
		 
	     for(int i = 0; i < length; i++){
	        builder.append(CHARACTER_SET.charAt(rnd.nextInt(CHARACTER_SET.length())));
	    }  
	    //System.out.println(builder.toString());
	    return builder.toString();
	 }
	 public int generatenum(int min,int max)
	 {
		 return (int) ((Math.random() * (max - min)) + min);
	 }

}
