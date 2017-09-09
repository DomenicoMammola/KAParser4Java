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

public class KAParserFunctionHelp {

    private String function;
    private String sintax;
    private String description;
    private String sample;
    private String category;

    public KAParserFunctionHelp(String function, String sintax, String description, String sample, String category) {
        this.function = function;
        this.sintax = sintax;
        this.description = description;
        this.sample = sample;
        this.category = category;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getSintax() {
        return sintax;
    }

    public void setSintax(String sintax) {
        this.sintax = sintax;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
