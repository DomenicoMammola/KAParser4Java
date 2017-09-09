/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.mammola.kaparser;

/**
 *
 * @author E5430
 */
public class KAParserException extends Exception {

    private static final long serialVersionUID = 7436734623842384762L;

    public KAParserException(String errorMessage) {
        super(errorMessage);

    }    
}
