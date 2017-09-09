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

public class KALexStatus {
    private String formula;
    private Integer charIndex;
    private Integer lenFormula;

    public Boolean isEof() {
        return charIndex >= lenFormula;
    }

    public char currentChar() {
        return formula.charAt(charIndex);
    }

    public void advance() {
        charIndex++;
    }

    public void goBack() {
        charIndex--;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String value) {
        formula = value;
        lenFormula = formula.length();
        charIndex = 0;
    }

    public Integer getCharIndex() {
        return charIndex;
    }    
}
