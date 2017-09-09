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

public enum KAToken {
    TOKEN_UNDEFINED, 
    TOKEN_EOF, 
    TOKEN_ERROR, 
    TOKEN_LEFTBRACE, 
    TOKEN_RIGHTBRACE, 
    TOKEN_NUMBER, 
    TOKEN_IDENTIFIER, 
    TOKEN_SEMICOLON, 
    TOKEN_POWER,
    TOKEN_INVERT, 
    TOKEN_NOT,
    TOKEN_MULTIPLE, 
    TOKEN_DIVIDE, 
    TOKEN_MOD, 
    TOKEN_PERCENT,
    TOKEN_ADD, 
    TOKEN_SUBTRACT,
    TOKEN_LESS, 
    TOKEN_LESS_EQUAL, 
    TOKEN_EQUAL, 
    TOKEN_NOT_EQUAL, 
    TOKEN_GREATER_EQUAL, 
    TOKEN_GREATER,
    TOKEN_OR, 
    TOKEN_XOR, 
    TOKEN_AND, 
    TOKEN_STRING  
}
