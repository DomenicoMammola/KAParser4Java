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

import java.math.BigDecimal;

public class KAMathUtilities {
        public static final Integer DEFAULT_DECIMAL_NUMBERS = 5;
    

    public static double roundTo(double unrounded, int precision, int roundingMode) {
        BigDecimal bd = new BigDecimal(Double.toString(unrounded));
        BigDecimal rounded = bd.setScale(precision, roundingMode);
        return rounded.doubleValue();
    }

    public static double roundTo(double unrounded, int precision) {
        return roundTo(unrounded, precision, BigDecimal.ROUND_HALF_EVEN);
    }

    public static Boolean doublesAreEqual(Double aValue1, Double aValue2, Integer decimalNumbers) {
        Double compareValue = Math.pow(10.0, -1.0 * decimalNumbers) - Math.pow(10.0, -1.0 * (decimalNumbers + 1))
                - Math.pow(10.0, -1.0 * (decimalNumbers + 2.0));
        return Math.abs(aValue1 - aValue2) <= compareValue;
    }

    public static Boolean doublesAreEqual(Double aValue1, Double aValue2) {
        return doublesAreEqual(aValue1, aValue2, DEFAULT_DECIMAL_NUMBERS);
    }
    
    public static Boolean doubleIsEqualOrGreater(Double aValue1, Double aValue2, Integer aDecimalNumbers) {
        return doublesAreEqual(aValue1, aValue2, aDecimalNumbers) || (aValue1 > aValue2);
    }
    
    public static Boolean doubleIsEqualOrGreater(Double aValue1, Double aValue2) {
        return doubleIsEqualOrGreater(aValue1, aValue2, DEFAULT_DECIMAL_NUMBERS);
    }
        
    public static Boolean doubleIsEqualOrLessThan(Double aValue1, Double aValue2, Integer aDecimalNumbers) {
        return doublesAreEqual(aValue1, aValue2, aDecimalNumbers) || (aValue1 < aValue2);
    }

    public static Boolean doubleIsEqualOrLessThan(Double aValue1, Double aValue2) {
        return doubleIsEqualOrLessThan(aValue1, aValue2, DEFAULT_DECIMAL_NUMBERS);
    }
    
    public static Boolean doubleIsGreater(Double aValue1, Double aValue2, Integer aDecimalNumbers) {
        return (! doublesAreEqual(aValue1, aValue2, aDecimalNumbers)) && (aValue1 > aValue2);
    }


    public static Boolean doubleIsGreater(Double aValue1, Double aValue2) {
        return doubleIsGreater(aValue1, aValue2, DEFAULT_DECIMAL_NUMBERS);
    }
    
    public static Boolean doubleIsLessThan(Double aValue1, Double aValue2, Integer aDecimalNumbers) {
        return (! doublesAreEqual(aValue1, aValue2, aDecimalNumbers)) && (aValue1 < aValue2);
    }

    
    public static Boolean doubleIsLessThan(Double aValue1, Double aValue2) {
        return doubleIsLessThan(aValue1, aValue2, DEFAULT_DECIMAL_NUMBERS);
    }


    public static Double safeDiv(Double numer, Double denom) {
        if (denom == 0) {
            return 0.0;
        } else {
            return numer / denom;
        }
    }
    
    public static Boolean doubleIsInteger(Double value) {
        return (value != null) && (!Double.isInfinite(value)) && (KAMathUtilities.doublesAreEqual(value, Math.floor(value)));
    }
}
