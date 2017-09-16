/*
 *  This is part of the KaParser4Java Library
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * This software is distributed without any warranty.
 *
 * @author Domenico Mammola (mimmo71@gmail.com - www.mammola.net)
 *
 */
package it.mammola.kaparser;

import java.time.ZoneId;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import org.junit.Test;


public class KAParserTestSuite {
    
    private KaParserTestDataProvider testProvider;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        testProvider = new KaParserTestDataProvider();
    }

    @After
    public void tearDown() throws Exception {
        testProvider = null;
    }
    
    @Test
    public void test1 () throws KAParserException{
        KAParser parser = new KAParser(ZoneId.systemDefault());
        parser.addDataProvider(testProvider);
        KAParserCalculationDoubleResult tempResult = new KAParserCalculationDoubleResult();
        parser.calculate("2 + 2", tempResult);
        Double expectedValue;        
        expectedValue = 4.0;
        assertEquals(expectedValue, tempResult.getValue());
        
        parser.calculate("((21 * 2) - 1) - 20", tempResult);  
        expectedValue = 21.0;
        assertEquals(expectedValue, tempResult.getValue());
        
        parser.calculate("IF(1, 0, 1)", tempResult);       
        expectedValue = 0.0;
        assertEquals(expectedValue, tempResult.getValue());
        
        
    }
    
}
