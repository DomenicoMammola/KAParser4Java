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

import java.util.List;

public class KaParserTestDataProvider implements KAParserDataProvider{

    @Override
    public Boolean getValue(Object sender, String valueName, KAParserCalculationDoubleResult value) {
        if ("DOUBLE1".equalsIgnoreCase(valueName)) {
            value.setValue(3.2);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean getStrValue(Object sender, String valueName, KAParserCalculationStringResult value) {
        if ("STRING1".equalsIgnoreCase(valueName)) {
            value.setValue("DONALD");
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean getRangeValues(Object sender, String rangeFunction, String internalFunc, KAParserValueType valueType, List<Object> valuesArray) {
        return false;
    }

    @Override
    public Boolean calcUserFunction(Object sender, String func, List<String> parameters, KAParserCalculationDoubleResult value) throws KAParserException {
        return false;
    }

    @Override
    public Boolean calcStrUserFunction(Object sender, String func, List<String> parameters, KAParserCalculationStringResult value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
