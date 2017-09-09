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

public interface KAParserDataProvider {

    Boolean getValue (final Object sender, final String valueName, KAParserCalculationDoubleResult value);
    Boolean getStrValue(final Object sender, final String valueName, KAParserCalculationStringResult value);
    Boolean getRangeValues(final Object sender, final String rangeFunction, final String internalFunc, final KAParserValueType valueType, List<Object> valuesArray);
    public abstract Boolean calcUserFunction(final Object sender, final String func, final List<String> parameters, KAParserCalculationDoubleResult value) throws KAParserException;
    public abstract Boolean calcStrUserFunction(final Object sender, final String func, final List<String> parameters, KAParserCalculationStringResult value);    
}
