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

public class KALexComputationResult {
private Boolean valueIsInteger;
    private Boolean valueIsDouble;
    private Boolean valueIsUndefined;

    private Integer intValue;
    private double doubleValue;
    private String stringValue;
    private KAToken token;

    public Integer getIntValue() {
        return intValue;
    }

    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public KAToken getToken() {
        return token;
    }

    public void setToken(KAToken token) {
        this.token = token;
    }

    public void clear() {
        valueIsUndefined = true;
        token = KAToken.TOKEN_UNDEFINED;
    }

    public Boolean isInteger() {
        return valueIsInteger;
    }

    public Boolean isString() {
        return (!valueIsDouble) && (!valueIsInteger) && (!valueIsUndefined);
    }

    public Boolean isDouble() {
        return valueIsDouble;
    }

    public void setInteger(final Integer aValue) {
        valueIsDouble = false;
        valueIsUndefined = false;
        valueIsInteger = true;
        token = KAToken.TOKEN_NUMBER;
        intValue = aValue;
    }

    public void setDouble(final Double aValue) {
        valueIsDouble = true;
        valueIsUndefined = false;
        valueIsInteger = false;
        token = KAToken.TOKEN_NUMBER;
        doubleValue = aValue;
    }

    public void setString(final String aValue) {
        valueIsDouble = false;
        valueIsUndefined = false;
        valueIsInteger = false;
        token = KAToken.TOKEN_STRING;
        stringValue = aValue;
    }    
}
