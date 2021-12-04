package com.sanjai.airplane;


import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
   public void runtests() {
      Result result = JUnitCore.runClasses(FlightResourceTest.class);
		
      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
		
      System.out.println("All tests are success " + result.wasSuccessful());
   }

}
