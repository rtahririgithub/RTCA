package com.telus.credit.batch;

//import com.telus.credit.batch.extract.*;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AutomatedBuildTestSuite {
	
    public static Test suite() {

        TestSuite suite = new TestSuite();
	
        // Add test case classes here either by passing the entire .class object or by calling the "suite()" method.
        //suite.addTestSuite( com.telus.credit.batch.extract.ExtractCustomerGroupBatchTest.class );
        //suite.addTestSuite( com.telus.credit.batch.extract.ExtractCustomerGroupBatchTest.suite() );

        suite.addTestSuite( com.telus.credit.batch.extract.ExtractCustomerGroupBatchTest.class );

        return suite;
    }

    /**
     * Runs the test suite using the textual runner.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
