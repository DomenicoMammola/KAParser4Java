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


public class KAParserCalculationDoubleResult {
    private Double value;
    
    public KAParserCalculationDoubleResult(){
        value = 0.0;
    }
    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
    	if (value != null) {
    		this.value = value;	
    	} else {
    		this.value = 0.0;
    	}
    		
        
    }    
}
