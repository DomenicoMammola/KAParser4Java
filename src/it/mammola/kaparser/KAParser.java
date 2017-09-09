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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class KAParser {

    private static final String MP_INT_FUNC_TRUNC = "trunc";
    private static final String MP_INT_FUNC_SIN = "sin";
    private static final String MP_INT_FUNC_COS = "cos";
    private static final String MP_INT_FUNC_TAN = "tan";
    private static final String MP_INT_FUNC_FRAC = "frac";
    private static final String MP_INT_FUNC_INT = "int";

    private static final String MP_INTCONST_NOW = "now";
    private static final String MP_INTCONST_TODAY = "today";
    private static final String MP_INTCONST_TRUE = "true";
    private static final String MP_INTCONST_FALSE = "false";
    private static final String MP_INTCONST_PI = "pi";

    private static final String MP_SPECFUNC_IF = "if";
    private static final String MP_SPECFUNC_EMPTY = "empty";
    private static final String MP_SPECFUNC_LEN = "len";
    private static final String MP_SPECFUNC_AND = "and";
    private static final String MP_SPECFUNC_OR = "or";
    private static final String MP_SPECFUNC_SAFEDIV = "safediv";
    private static final String MP_SPECFUNC_CONCATENATE = "concatenate";
    private static final String MP_SPECFUNC_REPLICATE = "replicate";
    private static final String MP_SPECFUNC_LEFT = "left";
    private static final String MP_SPECFUNC_RIGHT = "right";
    private static final String MP_SPECFUNC_SUBSTR = "substr";
    private static final String MP_SPECFUNC_TOSTR = "tostr";
    private static final String MP_SPECFUNC_POS = "pos";
    private static final String MP_SPECFUNC_UPPERCASE = "uppercase";
    private static final String MP_SPECFUNC_LOWERCASE = "lowercase";
    private static final String MP_SPECFUNC_EQUALS = "equals";
    private static final String MP_SPECFUNC_EQUALSIGNORECASE = "equalsignorecase";
    private static final String MP_SPECFUNC_COMPARETO = "compareto";
    private static final String MP_SPECFUNC_COMPARETOIGNORECASE = "comparetoignorecase";
    private static final String MP_SPECFUNC_ROUND = "round";
    private static final String MP_SPECFUNC_CEIL = "ceil";
    private static final String MP_SPECFUNC_FLOOR = "floor";
    private static final String MP_SPECFUNC_NOT = "not";
    private static final String MP_SPECFUNC_SUM = "sum";
    private static final String MP_SPECFUNC_MAX = "max";
    private static final String MP_SPECFUNC_MIN = "min";
    private static final String MP_SPECFUNC_AVG = "avg";
    private static final String MP_SPECFUNC_COUNT = "count";
    private static final String MP_SPECFUNC_GETDAY = "getday";
    private static final String MP_SPECFUNC_GETWEEK = "getweek";
    private static final String MP_SPECFUNC_GETMONTH = "getmonth";
    private static final String MP_SPECFUNC_GETYEAR = "getyear";
    private static final String MP_SPECFUNC_GETHOUR = "gethour";
    private static final String MP_SPECFUNC_GETMINUTE = "getminute";
    private static final String MP_SPECFUNC_GETSECOND = "getsecond";
    private static final String MP_SPECFUNC_PREVIOUSMONDAY = "previousmonday";
    private static final String MP_SPECFUNC_STARTOFTHEMONTH = "startofthemonth";
    private static final String MP_SPECFUNC_ENDOFTHEMONTH = "endofthemonth";
    private static final String MP_SPECFUNC_TODATE = "todate";
    private static final String MP_SPECFUNC_TODATETIME = "todatetime";
    private static final String MP_SPECFUNC_DISTANCEFROMNOW = "distancefromnow";
    private static final String MP_SPECFUNC_DISTANCEFROMTODAY = "distancefromtoday";
    private static final String MP_SPECFUNC_STRINGTODATETIME = "stringtodatetime";
    private static final String MP_SPECFUNC_TODOUBLE = "todouble";
    private static final String MP_SPECFUNC_BETWEEN = "between";
    private static final String MP_SPECFUNC_TRIM = "trim";
    private static final String MP_SPECFUNC_LTRIM = "ltrim";
    private static final String MP_SPECFUNC_RTRIM = "rtrim";
    private static final String MP_SPECFUNC_RAISEERROR = "raiseerror";

    private static final String MP_RANGEFUNC_CHILDSNOTNULL = "childsnotnull";
    private static final String MP_RANGEFUNC_PARENTNOTNULL = "parentnotnull";
    private static final String MP_RANGEFUNC_PARENTSNOTNULL = "parentsnotnull";
    private static final String MP_RANGEFUNC_CHILDS = "childs";
    private static final String MP_RANGEFUNC_PARENTS = "parents";
    private static final String MP_RANGEFUNC_PARENT = "parent";
    private static final String MP_RANGEFUNC_THIS = "this";

    private int decimalNumbers;
    private final ArrayList<KAParserDataProvider> dataProviders;
    private final ZoneId zone;

    public KAParser(ZoneId zone){
    	this.zone = zone;
    	dataProviders = new ArrayList<>();
    	decimalNumbers = KAMathUtilities.DEFAULT_DECIMAL_NUMBERS;
    }
    
    public int getDecimalNumbers() {
        return decimalNumbers;
    }

    public void setDecimalNumbers(int decimalNumbers) {
        this.decimalNumbers = decimalNumbers;
    }

    public void addDataProvider(KAParserDataProvider dataProvider) {
        this.dataProviders.add(dataProvider);
    }

    private Boolean isDecimalSeparator(final char aValue) {
        return (aValue == '.') || (aValue == ',');
    }

    private Boolean isStringSeparator(final char aValue) {
        return (aValue == '\'') || (aValue == '\"');
    }

    private String cleanFormula(final String aFormula) {
    	if (aFormula == null) {
    		return "";
    	} else {
            String temp = aFormula.trim();
            temp = temp.replace((char) 10, '\0');
            temp = temp.replace((char) 13, '\0');
            return temp;    		
    	}
    }

    private Boolean doInternalCalculate(final KALexComputationType calculation, final String subFormula,
    		KAParserCalculationDoubleResult resValue) {
    	Boolean successful;

    	if (calculation == KALexComputationType.VALUE) {
    		if (subFormula.equalsIgnoreCase(MP_INTCONST_NOW)) {
    			resValue.setValue((double) (Instant.now().getEpochSecond() / 3600));
    			successful = true;
    		} else if (subFormula.equalsIgnoreCase(MP_INTCONST_TODAY)) {
    			resValue.setValue(KADateTimeUtilities.instantToDouble(Instant.now()));
    			successful = true;
    		} else if (subFormula.equalsIgnoreCase(MP_INTCONST_TRUE)) {
    			resValue.setValue(1.0);
    			successful = true;
    		} else if (subFormula.equalsIgnoreCase(MP_INTCONST_FALSE)) {
    			resValue.setValue(0.0);
    			successful = true;
    		} else if (subFormula.equalsIgnoreCase(MP_INTCONST_PI)) {
    			resValue.setValue(Math.PI);
    			successful = true;
    		} else if (! this.dataProviders.isEmpty()) {
    			successful = false;
    			for (KAParserDataProvider dataProvider : this.dataProviders) {
    				successful = dataProvider.getValue(this, subFormula, resValue);
    				if (successful) {
    					break;
    				}
    			}
    		} else {
    			successful = false;
    		}

    	} else { // calculation = FUNCTION 
    			if (subFormula.equalsIgnoreCase(MP_INT_FUNC_TRUNC)) {
    				resValue.setValue(Math.floor(resValue.getValue()));
    				successful = true;
    			} else if (subFormula.equalsIgnoreCase(MP_INT_FUNC_SIN)) {
    				resValue.setValue(Math.sin(resValue.getValue()));
    				successful = true;
    			} else if (subFormula.equalsIgnoreCase(MP_INT_FUNC_COS)) {
    				resValue.setValue(Math.cos(resValue.getValue()));
    				successful = true;
    			} else if (subFormula.equalsIgnoreCase(MP_INT_FUNC_TAN)) {
    				resValue.setValue(Math.tan(resValue.getValue()));
    				successful = true;
    			} else if (subFormula.equalsIgnoreCase(MP_INT_FUNC_FRAC)) {
    				resValue.setValue(resValue.getValue() - Math.floor(resValue.getValue()));
    				successful = true;
    			} else if (subFormula.equalsIgnoreCase(MP_INT_FUNC_INT)) {
    				resValue.setValue(Math.floor(resValue.getValue()));
    				successful = true;
    			} else if (! this.dataProviders.isEmpty()) {
    				successful = false;
    				for (KAParserDataProvider dataProvider : this.dataProviders) {
    					successful = dataProvider.getValue(this, subFormula, resValue);
    					if (successful) {
    						break;
    					}
    				}
    			} else {
    				successful = false;
    			}
    	}
    	return successful;
    }

    private String trimEnd(final String input, final String charsToTrim) {
        return input.replaceAll("[" + charsToTrim + "]+$", "");
    }

    private String trimStart(final String input, final String charsToTrim) {
        return input.replaceAll("^[" + charsToTrim + "]+", "");
    }

    private Boolean doInternalStringCalculate(final KALexComputationType calculation, final String subFormula,
    		KAParserCalculationStringResult resValue) {
    	Boolean successful;
    	if (calculation == KALexComputationType.FUNCTION) {
    		if (subFormula.equalsIgnoreCase(MP_SPECFUNC_TRIM)) {
    			resValue.setValue(resValue.getValue().trim());
    			successful = true;
    		} else if (subFormula.equalsIgnoreCase(MP_SPECFUNC_LTRIM)) {
    			resValue.setValue(trimStart(resValue.getValue(), " "));
    			successful = true;
    		} else if (subFormula.equalsIgnoreCase(MP_SPECFUNC_RTRIM)) {
    			resValue.setValue(trimEnd(resValue.getValue(), " "));
    			successful = true;
    		} else if (this.dataProviders.size() > 0) {
    			successful = false;
    			for (KAParserDataProvider dataProvider : this.dataProviders) {
    				successful = dataProvider.getStrValue(this, subFormula, resValue);
    				if (successful) {
    					break;
    				}
    			}
    		} else {
    			successful = false;
    		}
    	} else { // KALexComputationType.VALUE
    		if (! this.dataProviders.isEmpty()) {
    			successful = false;
    			for (KAParserDataProvider dataProvider : this.dataProviders) {
    				successful = dataProvider.getStrValue(this, subFormula, resValue);
    				if (successful) {
    					break;
    				}
    			}    			
    		} else {
    			successful = false;
    		}

    	}
    	return successful;
    }

    private Boolean doInternalRangeCalculate(final String rangeFunction, final String func,
            final KAParserValueType ValueType, final ArrayList<Object> valuesArray) {
        Boolean successful = false;

        if (! this.dataProviders.isEmpty()) {
        	successful = false;
        	for (KAParserDataProvider dataProvider : this.dataProviders) {
        		successful = dataProvider.getRangeValues(this, rangeFunction, func, ValueType, valuesArray);
        		if (successful) {
        			break;
        		}
        	}
            
        }
        return successful;
    }

    private double doUserFunction(final String funct, final ArrayList<String> parametersList, final KALexStatus lexState)
            throws KAParserException {
        Boolean successful;

        if (! this.dataProviders.isEmpty()) {
            KAParserCalculationDoubleResult tempValue = new KAParserCalculationDoubleResult();
            successful = false;
            for (KAParserDataProvider dataProvider : this.dataProviders) {
                successful = dataProvider.calcUserFunction(this, funct, parametersList, tempValue);
                if (successful) {
                	break;
                }
            }
            
            if (!successful) {
                raiseError(KAErrorType.EXTERNAL_FUNCTION_ERROR, funct, lexState);
                return 0;
            } else {
                return tempValue.getValue();
            }
        } else {
            raiseError(KAErrorType.FUNCTION_UNKNOWN, funct, lexState);
            return 0;
        }
    }

    private String doStrUserFunction(final String funct, final ArrayList<String> parametersList,
            final KALexStatus lexState) throws KAParserException {

        Boolean successful;
        if (! this.dataProviders.isEmpty()) {
            KAParserCalculationStringResult tempValue = new KAParserCalculationStringResult();
            
            successful = false;
            for (KAParserDataProvider dataProvider : this.dataProviders) {
            	successful = dataProvider.calcStrUserFunction(this, funct, parametersList, tempValue);	
            	if (successful) {
            		break;
            	}
            }
            
            if (!successful) {
                raiseError(KAErrorType.EXTERNAL_FUNCTION_ERROR, funct, lexState);
                return "";
            } else {
                return tempValue.getValue();
            }
        } else {
            raiseError(KAErrorType.FUNCTION_UNKNOWN, funct, lexState);
            return "";
        }
    }

    // Lexical Analyzer Function
    // http://www.gnu.org/software/bison/manual/html_node/Lexical.html
    private void yylex(KALexStatus lexState, KALexComputationResult lexResult) throws KAParserException {
        int currentInteger;
        double currentFloat;
        double currentdecimal;

        lexResult.clear();

        if (lexState.isEof()) {
            lexResult.setToken(KAToken.TOKEN_EOF);
            return;
        }

        while ((lexState.currentChar() == ' ') && (!lexState.isEof())) {
            lexState.advance();
        }

        if (lexState.isEof()) {
            lexResult.setToken(KAToken.TOKEN_EOF);
            return;
        }

        // ------------ maybe a string..

        if (isStringSeparator(lexState.currentChar())) {            
            StringBuilder sb = new StringBuilder("");
            char currentCharSeparator = lexState.currentChar();

            lexState.advance();

            Boolean doLoop = true;
            
            while (doLoop) {
                if (lexState.isEof()) {
                    raiseError(KAErrorType.INVALID_STRING, lexState);
                    return;
                }
                if (lexState.currentChar() == currentCharSeparator) {
                    doLoop = false;
                } else {
                    sb.append(lexState.currentChar());                    
                    lexState.advance();
                }
            }

            lexState.advance();
            lexResult.setString(sb.toString());
            return;
        }

        // ------------ maybe a number..

        if (Character.isDigit(lexState.currentChar())) {
            StringBuilder sb = new StringBuilder(Character.toString(lexState.currentChar()));
            
            lexState.advance();

            
            while ((!lexState.isEof()) && (Character.isDigit(lexState.currentChar()))) {
                sb.append(Character.toString(lexState.currentChar()));
                lexState.advance();
            }

            try {
                currentInteger = Integer.parseInt(sb.toString());
            } catch (NumberFormatException e) {
                lexResult.setToken(KAToken.TOKEN_ERROR);
                return;
            }

            if ((!lexState.isEof()) && (isDecimalSeparator(lexState.currentChar()))) {
                lexState.advance();
                currentFloat = (double) currentInteger;

                currentdecimal = (double) 1;
                while ((!lexState.isEof()) && (Character.isDigit(lexState.currentChar()))) {
                    currentdecimal = currentdecimal / 10;
                    currentFloat = currentFloat
                            + (currentdecimal * Integer.parseInt(Character.toString(lexState.currentChar())));
                    lexState.advance();
                }

                lexResult.setDouble(currentFloat);
            } else {
                lexResult.setInteger(currentInteger);
            }

            return;

        }

        // ------------ forse un identificatore..

        if (Character.isLetter(lexState.currentChar()) || (lexState.currentChar() == '_')
                || (lexState.currentChar() == '@')) {
            StringBuilder sb = new StringBuilder(Character.toString(lexState.currentChar()));
            lexState.advance();

            while ((!lexState.isEof()) && ((Character.isLetterOrDigit(lexState.currentChar()))
                    || (lexState.currentChar() == '_') || (lexState.currentChar() == '@'))) {
                sb.append(Character.toString(lexState.currentChar()));
                lexState.advance();
            }

            lexResult.setString(sb.toString());
            lexResult.setToken(KAToken.TOKEN_IDENTIFIER);
            return;
        }

        // ------------ forse un operatore..

        char opChar = lexState.currentChar();
        lexState.advance();
        switch (opChar) {
            case '=': {
                if ((!lexState.isEof()) && (lexState.currentChar() == '=')) {
                    lexState.advance();
                    lexResult.setToken(KAToken.TOKEN_EQUAL);
                } else {
                    lexResult.setToken(KAToken.TOKEN_ERROR);
                }
                break;
            }
            case '+': {
                lexResult.setToken(KAToken.TOKEN_ADD);
                break;
            }
            case '-': {
                lexResult.setToken(KAToken.TOKEN_SUBTRACT);
                break;
            }
            case '*': {
                if ((!lexState.isEof()) && (lexState.currentChar() == '*')) {
                    lexState.advance();
                    lexResult.setToken(KAToken.TOKEN_POWER);
                } else {
                    lexResult.setToken(KAToken.TOKEN_MULTIPLE);
                }
                break;
            }
            case '/': {
                lexResult.setToken(KAToken.TOKEN_DIVIDE);
                break;
            }
            case '%': {
                if ((!lexState.isEof()) && (lexState.currentChar() == '%')) {
                    lexState.advance();
                    lexResult.setToken(KAToken.TOKEN_PERCENT);
                } else {
                    lexResult.setToken(KAToken.TOKEN_MOD);
                }
                break;
            }
            case '~': {
                lexResult.setToken(KAToken.TOKEN_INVERT);
                break;
            }
            case '^': {
                lexResult.setToken(KAToken.TOKEN_XOR);
                break;
            }
            case '&': {
                lexResult.setToken(KAToken.TOKEN_AND);
                break;
            }
            case '|': {
                lexResult.setToken(KAToken.TOKEN_OR);
                break;
            }
            case '<': {
                if ((!lexState.isEof()) && (lexState.currentChar() == '=')) {
                    lexState.advance();
                    lexResult.setToken(KAToken.TOKEN_LESS_EQUAL);
                } else {
                    if ((!lexState.isEof()) && (lexState.currentChar() == '>')) {
                        lexState.advance();
                        lexResult.setToken(KAToken.TOKEN_NOT_EQUAL);
                    } else {
                        lexResult.setToken(KAToken.TOKEN_LESS);
                    }
                }
                break;
            }
            case '>': {
                if ((!lexState.isEof()) && (lexState.currentChar() == '=')) {
                    lexState.advance();
                    lexResult.setToken(KAToken.TOKEN_GREATER_EQUAL);
                } else {
                    if ((!lexState.isEof()) && (lexState.currentChar() == '<')) {
                        lexState.advance();
                        lexResult.setToken(KAToken.TOKEN_NOT_EQUAL);
                    } else {
                        lexResult.setToken(KAToken.TOKEN_GREATER);
                    }
                }
                break;
            }
            case '!': {
                if ((!lexState.isEof()) && (lexState.currentChar() == '=')) {
                    lexState.advance();
                    lexResult.setToken(KAToken.TOKEN_NOT_EQUAL);
                } else {
                    lexResult.setToken(KAToken.TOKEN_NOT);
                }
                break;
            }
            case '(': {
                lexResult.setToken(KAToken.TOKEN_LEFTBRACE);
                break;
            }
            case ')': {
                lexResult.setToken(KAToken.TOKEN_RIGHTBRACE);
                break;
            }
            case ';': {
                lexResult.setToken(KAToken.TOKEN_SEMICOLON);
                break;
            }
            default: {
                lexResult.setToken(KAToken.TOKEN_ERROR);
                lexState.goBack();
                break;
            }
        }
    }

    // double
    private void startCalculate(KAParserCalculationDoubleResult resValue, KALexStatus lexState, KALexComputationResult lexResult)
            throws KAParserException {
        calc6(resValue, lexState, lexResult);
        while (lexResult.getToken().equals(KAToken.TOKEN_SEMICOLON)) {
            yylex(lexState, lexResult);
            calc6(resValue, lexState, lexResult);
        }
        if (!(lexResult.getToken().equals(KAToken.TOKEN_EOF))) {
            raiseError(KAErrorType.SYNTAX_ERROR, lexState);
        }
    }

    private void calc6(KAParserCalculationDoubleResult resValue, KALexStatus lexState, KALexComputationResult lexResult)
            throws KAParserException {
        KAParserCalculationDoubleResult newDouble = new KAParserCalculationDoubleResult();
        KAToken lastToken;

        calc5(resValue, lexState, lexResult);
        while (lexResult.getToken().equals(KAToken.TOKEN_OR) || lexResult.getToken().equals(KAToken.TOKEN_XOR)
                || lexResult.getToken().equals(KAToken.TOKEN_AND)) {
            lastToken = lexResult.getToken();
            yylex(lexState, lexResult);
            calc5(newDouble, lexState, lexResult);
            switch (lastToken) {
                case TOKEN_OR: {
                    resValue.setValue(
                            (double) ((int) Math.floor(resValue.getValue()) | (int) Math.floor(newDouble.getValue())));
                    break;
                }
                case TOKEN_AND: {
                    resValue.setValue(
                            (double) ((int) Math.floor(resValue.getValue()) & (int) Math.floor(newDouble.getValue())));
                    break;
                }
                case TOKEN_XOR: {
                    resValue.setValue(
                            (double) ((int) Math.floor(resValue.getValue()) ^ (int) Math.floor(newDouble.getValue())));
                    break;
                }
                default: {
                    // raiseError(KaParserExceptionType.sFunctionUnknown);
                    break;
                }
            }
        }
    }

    private void calc5(KAParserCalculationDoubleResult resValue, KALexStatus lexState, KALexComputationResult lexResult)
            throws KAParserException {
        KAParserCalculationDoubleResult newDouble = new KAParserCalculationDoubleResult();
        KAToken lastToken;

        calc4(resValue, lexState, lexResult);
        while (lexResult.getToken().equals(KAToken.TOKEN_LESS) || lexResult.getToken().equals(KAToken.TOKEN_LESS_EQUAL)
                || lexResult.getToken().equals(KAToken.TOKEN_EQUAL) || lexResult.getToken().equals(KAToken.TOKEN_NOT_EQUAL)
                || lexResult.getToken().equals(KAToken.TOKEN_GREATER_EQUAL) || lexResult.getToken().equals(KAToken.TOKEN_GREATER)) {
            lastToken = lexResult.getToken();
            yylex(lexState, lexResult);
            calc4(newDouble, lexState, lexResult);
            switch (lastToken) {
                case TOKEN_LESS: {
                    resValue.setValue(booleanToFloat(KAMathUtilities.doubleIsLessThan(resValue.getValue(),
                            newDouble.getValue(), decimalNumbers)));
                    break;
                }
                case TOKEN_LESS_EQUAL: {
                    resValue.setValue(booleanToFloat(KAMathUtilities.doubleIsEqualOrLessThan(resValue.getValue(),
                            newDouble.getValue(), decimalNumbers)));
                    break;
                }
                case TOKEN_EQUAL: {
                    resValue.setValue(booleanToFloat(KAMathUtilities.doublesAreEqual(resValue.getValue(),
                            newDouble.getValue(), decimalNumbers)));
                    break;
                }
                case TOKEN_NOT_EQUAL: {
                    resValue.setValue(booleanToFloat(!KAMathUtilities.doublesAreEqual(resValue.getValue(),
                            newDouble.getValue(), decimalNumbers)));
                    break;
                }
                case TOKEN_GREATER_EQUAL: {
                    resValue.setValue(booleanToFloat(KAMathUtilities.doubleIsEqualOrGreater(resValue.getValue(),
                            newDouble.getValue(), decimalNumbers)));
                    break;
                }
                case TOKEN_GREATER: {
                    resValue.setValue(booleanToFloat(KAMathUtilities.doubleIsGreater(resValue.getValue(),
                            newDouble.getValue(), decimalNumbers)));
                    break;
                }
                default: {
                    // raiseError(KaParserExceptionType.sFunctionUnknown);
                    break;
                }
            }
        }
    }

    private void calc4(KAParserCalculationDoubleResult resValue, KALexStatus lexState, KALexComputationResult lexResult)
            throws KAParserException {
        KAParserCalculationDoubleResult newDouble = new KAParserCalculationDoubleResult();
        KAToken lastToken;

        calc3(resValue, lexState, lexResult);
        while ((lexResult.getToken().equals(KAToken.TOKEN_ADD)) || (lexResult.getToken().equals(KAToken.TOKEN_SUBTRACT))) {
            lastToken = lexResult.getToken();
            yylex(lexState, lexResult);
            calc3(newDouble, lexState, lexResult);
            switch (lastToken) {
                case TOKEN_ADD: {
                    resValue.setValue(resValue.getValue() + newDouble.getValue());
                    break;
                }
                case TOKEN_SUBTRACT: {
                    resValue.setValue(resValue.getValue() - newDouble.getValue());
                    break;
                }
                default: {
                    // raiseError(KaParserExceptionType.sFunctionUnknown);
                    break;
                }
            }
        }
    }

    private void calc3(KAParserCalculationDoubleResult resValue, KALexStatus lexState, KALexComputationResult lexResult)
            throws KAParserException {
        KAParserCalculationDoubleResult newDouble = new KAParserCalculationDoubleResult();
        KAToken lastToken;

        calc2(resValue, lexState, lexResult);
        while (lexResult.getToken().equals(KAToken.TOKEN_MULTIPLE) || lexResult.getToken().equals(KAToken.TOKEN_DIVIDE)
                || lexResult.getToken().equals(KAToken.TOKEN_MOD) || lexResult.getToken().equals(KAToken.TOKEN_PERCENT)) {
            lastToken = lexResult.getToken();
            yylex(lexState, lexResult);
            calc2(newDouble, lexState, lexResult);
            switch (lastToken) {
                case TOKEN_MULTIPLE: {
                    resValue.setValue(resValue.getValue() * newDouble.getValue());
                    break;
                }
                case TOKEN_DIVIDE: {
                    resValue.setValue(resValue.getValue() / newDouble.getValue());
                    break;
                }
                case TOKEN_MOD: // resto della divisione tra interi
                {
                    resValue.setValue(Math.floor(resValue.getValue()) % Math.floor(newDouble.getValue()));
                    break;
                }
                case TOKEN_PERCENT: {
                    resValue.setValue(resValue.getValue() * newDouble.getValue() / 100);
                    break;
                }
                default: {
                    // raiseError(KaParserExceptionType.sFunctionUnknown);
                    break;
                }

            }
        }
    }

    private void calc2(KAParserCalculationDoubleResult resValue, KALexStatus lexState, KALexComputationResult lexResult)
            throws KAParserException {
        KAToken lastToken;
        KAParserCalculationDoubleResult newDouble = new KAParserCalculationDoubleResult();

        if (lexResult.getToken().equals(KAToken.TOKEN_NOT) || lexResult.getToken().equals(KAToken.TOKEN_INVERT)
                || lexResult.getToken().equals(KAToken.TOKEN_ADD) || lexResult.getToken().equals(KAToken.TOKEN_SUBTRACT)) {
            lastToken = lexResult.getToken();
            yylex(lexState, lexResult);
            calc2(newDouble, lexState, lexResult);
            switch (lastToken) {
                case TOKEN_NOT: {
                    if (KAMathUtilities.doublesAreEqual(Math.floor(newDouble.getValue()), 0.0)) {
                        resValue.setValue(1.0);
                    } else {
                        resValue.setValue(0.0);
                    }
                    break;
                }
                case TOKEN_INVERT: {
                    resValue.setValue((double) (~(int) (Math.floor(newDouble.getValue()))));
                    break;
                }
                case TOKEN_ADD: {
                    resValue.setValue(resValue.getValue() + (newDouble.getValue()));
                    break;
                }
                case TOKEN_SUBTRACT: {
                    resValue.setValue(resValue.getValue() - (newDouble.getValue()));
                    break;
                }
                default: {
                    break;
                }
            }
        } else {
            calc1(resValue, lexState, lexResult);
        }

    }

    private void calc1(KAParserCalculationDoubleResult resValue, KALexStatus lexState, KALexComputationResult lexResult)
            throws KAParserException {
        KAParserCalculationDoubleResult newDouble = new KAParserCalculationDoubleResult();

        calcTerm(resValue, lexState, lexResult);
        if (lexResult.getToken().equals(KAToken.TOKEN_POWER)) {
            yylex(lexState, lexResult);
            calcTerm(newDouble, lexState, lexResult);
            resValue.setValue(Math.pow(resValue.getValue(), newDouble.getValue()));
        }
    }

    private void calcTerm(KAParserCalculationDoubleResult resValue, KALexStatus lexState, KALexComputationResult lexResult)
            throws KAParserException {
        String currentIdent;
        ArrayList<String> paramList = new ArrayList<>();

        switch (lexResult.getToken()) {
            case TOKEN_NUMBER: {
                if (lexResult.isInteger()) {
                    resValue.setValue((double) lexResult.getIntValue());
                } else {
                    resValue.setValue(lexResult.getDoubleValue());
                }
                yylex(lexState, lexResult);
                break;
            }
            case TOKEN_LEFTBRACE: {
                yylex(lexState, lexResult);
                calc6(resValue, lexState, lexResult);
                if (lexResult.getToken().equals(KAToken.TOKEN_RIGHTBRACE)) {
                    yylex(lexState, lexResult);
                } else {
                    raiseError(KAErrorType.SYNTAX_ERROR, lexState);
                }
                break;
            }
            case TOKEN_IDENTIFIER: {
                currentIdent = lexResult.getStringValue();
                yylex(lexState, lexResult);
                if (lexResult.getToken().equals(KAToken.TOKEN_LEFTBRACE)) {
                    if (isFunction(currentIdent)) {
                        paramList.clear();
                        parseFunctionParameters(currentIdent, paramList, lexState);
                        resValue.setValue(executeFunction(currentIdent, paramList, lexState));
                        yylex(lexState, lexResult);
                    } else if (isInternalFunction(currentIdent)) {
                        yylex(lexState, lexResult);
                        calc6(resValue, lexState, lexResult);
                        if (lexResult.getToken().equals(KAToken.TOKEN_RIGHTBRACE)) {
                            yylex(lexState, lexResult);
                        } else {
                            raiseError(KAErrorType.FUNCTION_ERROR, currentIdent, lexState);
                        }
                        if (!doInternalCalculate(KALexComputationType.FUNCTION, currentIdent, resValue)) {
                            raiseError(KAErrorType.FUNCTION_ERROR, currentIdent, lexState);
                        }
                    } else {
                        paramList.clear();
                        parseFunctionParameters(currentIdent, paramList, lexState);
                        resValue.setValue(doUserFunction(currentIdent, paramList, lexState));

                        yylex(lexState, lexResult);
                    }
                } else {
                    if (!doInternalCalculate(KALexComputationType.VALUE, currentIdent, resValue)) {
                        raiseError(KAErrorType.FUNCTION_ERROR, currentIdent, lexState);
                    }
                }
                break;
            }
            default: {
                raiseError(KAErrorType.SYNTAX_ERROR, lexState);
                break;
            }
        }

    }

    // string
    private void startCalculateStr(KAParserCalculationStringResult resValue, KALexStatus lexState, KALexComputationResult lexResult)
            throws KAParserException {
        calculateStrLevel1(resValue, lexState, lexResult);
        while (lexResult.getToken().equals(KAToken.TOKEN_SEMICOLON)) {
            yylex(lexState, lexResult);
            calculateStrLevel1(resValue, lexState, lexResult);
        }
        if (!(lexResult.getToken().equals(KAToken.TOKEN_EOF))) {
            raiseError(KAErrorType.SYNTAX_ERROR, lexState);
        }
    }

    private void calculateStrLevel1(KAParserCalculationStringResult resValue, KALexStatus lexState, KALexComputationResult lexResult)
            throws KAParserException {
        KAParserCalculationStringResult newString = new KAParserCalculationStringResult();

        calculateStrLevel2(resValue, lexState, lexResult);
        while (lexResult.getToken().equals(KAToken.TOKEN_ADD)) {
            yylex(lexState, lexResult);
            calculateStrLevel2(newString, lexState, lexResult);
            resValue.setValue(resValue.getValue() + newString.getValue());
        }
    }

    private void calculateStrLevel2(KAParserCalculationStringResult resValue, KALexStatus lexState, KALexComputationResult lexResult)
            throws KAParserException {
        switch (lexResult.getToken()) {
            case TOKEN_STRING: {
                resValue.setValue(lexResult.getStringValue().substring(0, lexResult.getStringValue().length()));
                yylex(lexState, lexResult);
                break;
            }
            case TOKEN_LEFTBRACE: {
                yylex(lexState, lexResult);
                calculateStrLevel1(resValue, lexState, lexResult);
                if (lexResult.getToken().equals(KAToken.TOKEN_RIGHTBRACE)) {
                    yylex(lexState, lexResult);
                } else {
                    raiseError(KAErrorType.SYNTAX_ERROR, lexState);
                }
                break;
            }
            case TOKEN_IDENTIFIER: {
                String currentIdent = lexResult.getStringValue();
                yylex(lexState, lexResult);
                if (lexResult.getToken().equals(KAToken.TOKEN_LEFTBRACE)) {
                    ArrayList<String> paramList = new ArrayList<>();

                    parseFunctionParameters(currentIdent, paramList, lexState);

                    if (isFunction(currentIdent)) {
                        resValue.setValue(executeStrFunction(currentIdent, paramList, lexState));
                    } else {
                        if (paramList.size() == 1) {
                            calculateString(paramList.get(0), resValue);
                            if (!doInternalStringCalculate(KALexComputationType.FUNCTION, currentIdent, resValue)) {
                                resValue.setValue(doStrUserFunction(currentIdent, paramList, lexState));
                            }
                        } else {
                            resValue.setValue(doStrUserFunction(currentIdent, paramList, lexState));
                        }
                    }
                    yylex(lexState, lexResult);
                } else {
                    if (!doInternalStringCalculate(KALexComputationType.VALUE, currentIdent, resValue)) {
                        raiseError(KAErrorType.FUNCTION_ERROR, currentIdent, lexState);
                    }
                }
                break;
            }
            default: {
                // raiseError(KaParserExceptionType.sSyntaxError);
                break;
            }
        }

    }
    // range functions

    private String insideBraces(final String value, final KALexStatus lexState) throws KAParserException {
        String tempValue = value.trim();

        int len = tempValue.length();

        if (len <= 2) {
            raiseError(KAErrorType.SYNTAX_ERROR, lexState); // stringa vuota
            // o '()'
            return "";
        }

        if (tempValue.charAt(0) != '(') {
            raiseError(KAErrorType.SYNTAX_ERROR, lexState); // non inizia
            // con '('
            return "";
        }

        if (tempValue.charAt(len - 1) != ')') {
            raiseError(KAErrorType.SYNTAX_ERROR, lexState); // non finisce
            // con ')'
            return "";
        }

        return tempValue.substring(1, len - 1).trim();
    }

    private void manageRangeFunction(final String funct, final ArrayList<String> parametersList,
            final KAParserValueType valueType, ArrayList<Object> tempValuesArray, KALexStatus lexState)
            throws KAParserException {

        tempValuesArray.clear();

        if (parametersList.size() == 1) {
            String lowercaseFunct = funct.toLowerCase();

            int k = lowercaseFunct.indexOf(MP_RANGEFUNC_CHILDSNOTNULL);
            if (k >= 0) {
                String tempString = funct.substring(k + MP_RANGEFUNC_CHILDSNOTNULL.length() - 1);
                if (!doInternalRangeCalculate(MP_RANGEFUNC_CHILDSNOTNULL, insideBraces(tempString, lexState), valueType,
                        tempValuesArray)) {
                    raiseError(KAErrorType.FUNCTION_ERROR, lexState);
                }
                return;
            }
            k = lowercaseFunct.indexOf(MP_RANGEFUNC_PARENTNOTNULL);
            if (k >= 0) {
                String tempString = funct.substring(k + MP_RANGEFUNC_PARENTNOTNULL.length() - 1);
                if (!doInternalRangeCalculate(MP_RANGEFUNC_PARENTNOTNULL, insideBraces(tempString, lexState), valueType,
                        tempValuesArray)) {
                    raiseError(KAErrorType.FUNCTION_ERROR, lexState);
                }
                return;
            }
            k = lowercaseFunct.indexOf(MP_RANGEFUNC_PARENTSNOTNULL);
            if (k >= 0) {
                String tempString = funct.substring(k + MP_RANGEFUNC_PARENTSNOTNULL.length() - 1);
                if (!doInternalRangeCalculate(MP_RANGEFUNC_PARENTSNOTNULL, insideBraces(tempString, lexState),
                        valueType, tempValuesArray)) {
                    raiseError(KAErrorType.FUNCTION_ERROR, lexState);
                }
                return;
            }
            k = lowercaseFunct.indexOf(MP_RANGEFUNC_CHILDS);
            if (k >= 0) {
                String tempString = funct.substring(k + MP_RANGEFUNC_CHILDS.length() - 1);
                if (!doInternalRangeCalculate(MP_RANGEFUNC_CHILDS, insideBraces(tempString, lexState), valueType,
                        tempValuesArray)) {
                    raiseError(KAErrorType.FUNCTION_ERROR, lexState);
                }

                return;
            }
            k = lowercaseFunct.indexOf(MP_RANGEFUNC_PARENT);
            if (k >= 0) {
                String tempString = funct.substring(k + MP_RANGEFUNC_PARENT.length() - 1);
                if (!doInternalRangeCalculate(MP_RANGEFUNC_PARENT, insideBraces(tempString, lexState), valueType,
                        tempValuesArray)) {
                    raiseError(KAErrorType.FUNCTION_ERROR, lexState);
                }
                return;
            }
            k = lowercaseFunct.indexOf(MP_RANGEFUNC_PARENTS);
            if (k >= 0) {
                String tempString = funct.substring(k + MP_RANGEFUNC_PARENTS.length() - 1);
                if (!doInternalRangeCalculate(MP_RANGEFUNC_PARENTS, insideBraces(tempString, lexState), valueType,
                        tempValuesArray)) {
                    raiseError(KAErrorType.FUNCTION_ERROR, lexState);
                }
                return;
            }
            k = lowercaseFunct.indexOf(MP_RANGEFUNC_THIS);
            if (k >= 0) {
                String tempString = funct.substring(k + MP_RANGEFUNC_THIS.length() - 1);
                if (!doInternalRangeCalculate(MP_RANGEFUNC_THIS, insideBraces(tempString, lexState), valueType,
                        tempValuesArray)) {
                    raiseError(KAErrorType.FUNCTION_ERROR, lexState);
                }
                return;
            }
        }

        for (int i = 0; i < parametersList.size(); i++) {
            if (valueType == KAParserValueType.TYPE_FLOAT) {
                KAParserCalculationDoubleResult tempDouble = new KAParserCalculationDoubleResult();
                calculate(parametersList.get(i), tempDouble);
                tempValuesArray.add(tempDouble.getValue());
            } else {
                KAParserCalculationStringResult tempString = new KAParserCalculationStringResult();
                calculateString(parametersList.get(i), tempString);
                tempValuesArray.add(tempString.getValue());
            }
        }
    }

    private Boolean isInternalFunction(final String functionName) {        

        return MP_INT_FUNC_TRUNC.equalsIgnoreCase(functionName) || MP_INT_FUNC_SIN.equalsIgnoreCase(functionName) || 
        		MP_INT_FUNC_COS.equalsIgnoreCase(functionName) || MP_INT_FUNC_TAN.equalsIgnoreCase(functionName) ||
        		MP_INT_FUNC_FRAC.equalsIgnoreCase(functionName) || MP_INT_FUNC_INT.equalsIgnoreCase(functionName);
    }

    private Boolean isFunction(final String functionName) {

        return (functionName.equalsIgnoreCase(MP_SPECFUNC_IF) || (functionName.equalsIgnoreCase(MP_SPECFUNC_EMPTY))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_LEN)) || (functionName.equalsIgnoreCase(MP_SPECFUNC_AND))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_OR))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_SAFEDIV))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_CONCATENATE))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_REPLICATE))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_LEFT))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_RIGHT))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_SUBSTR))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_TOSTR))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_POS))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_UPPERCASE))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_LOWERCASE))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_EQUALS))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_EQUALSIGNORECASE))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_COMPARETO))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_COMPARETOIGNORECASE))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_ROUND))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_CEIL))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_FLOOR))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_NOT)) || (functionName.equalsIgnoreCase(MP_SPECFUNC_SUM))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_MAX)) || (functionName.equalsIgnoreCase(MP_SPECFUNC_MIN))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_AVG))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_COUNT))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_GETDAY))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_GETWEEK))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_GETMONTH))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_GETYEAR))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_GETHOUR))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_GETMINUTE))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_GETSECOND))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_PREVIOUSMONDAY))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_STARTOFTHEMONTH))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_ENDOFTHEMONTH))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_TODATE))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_TODATETIME))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_DISTANCEFROMNOW))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_DISTANCEFROMTODAY))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_STRINGTODATETIME))
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_TODOUBLE)) 
                || (functionName.equalsIgnoreCase(MP_SPECFUNC_RAISEERROR))
        		);
    }

    private void parseFunctionParameters(final String funct, ArrayList<String> parametersList, KALexStatus lexState)
            throws KAParserException {
        int startindex = lexState.getCharIndex();
        int i;
        int q;
        int par = 1;
        Boolean insideCommas = false;

        // questa funzione deve cercare la prima parentesi ( e poi andare avanti
        // verso dx
        // aprendo e chiudendo le eventuali parentesi
        // fino a trovare la parentesi ) che corrisponde alla prima trovata
        // cosi' si risolve il problema delle parentesi annidate

        while (!lexState.isEof()) {
            if (lexState.getFormula().charAt(lexState.getCharIndex()) == '\'') {
                insideCommas = !insideCommas;
            } else if ((!insideCommas) && (lexState.getFormula().charAt(lexState.getCharIndex()) == '(')) {
                par++;
            } else if ((!insideCommas) && (lexState.getFormula().charAt(lexState.getCharIndex()) == ')')) {
                par--;
            }

            if (par == 0) {
                break;
            } else {
                lexState.advance();
            }
        }

        String parametersStr = lexState.getFormula().substring(startindex, lexState.getCharIndex()).trim();

        if (par != 0) {
            raiseError(KAErrorType.FUNCTION_ERROR, funct, lexState);
        }
        if (parametersStr.equalsIgnoreCase("")) {
            return; // no parameters
        }

        lexState.advance(); // spostiamoci al carattere successivo alla
        // parentesi

        par = 0;
        i = 0;
        int k = 0;
        q = 0;
        insideCommas = false;

        while (i < parametersStr.length()) {
            if (parametersStr.charAt(i) == '\'') {
                insideCommas = !insideCommas;
            }
            if ((!insideCommas) && (parametersStr.charAt(i) == '(')) {
                par++;
            } else if ((!insideCommas) && (parametersStr.charAt(i) == ')')) {
                par--;
            }
            if ((!insideCommas) && (parametersStr.charAt(i) == ',') && (par == 0)) {
                parametersList.add(parametersStr.substring(q, k + q).trim());
                q = i + 1;
                k = 0;
            } else {
                k++;
            }
            i++;

        }

        parametersList.add(parametersStr.substring(q).trim());

    }

    private double executeFunction(final String funct, ArrayList<String> parametersList, KALexStatus lexState)
            throws KAParserException {
        if (funct.equalsIgnoreCase(MP_SPECFUNC_IF)) {
            if (parametersList.size() != 3) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            KAParserCalculationDoubleResult tempResult = new KAParserCalculationDoubleResult();
            calculate(parametersList.get(0), tempResult);
            if (tempResult.getValue() != 0) {
                calculate(parametersList.get(1), tempResult);
            } else {
                calculate(parametersList.get(2), tempResult);
            }
            return tempResult.getValue();
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_LEN)) {
            if (parametersList.size() != 1) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            KAParserCalculationStringResult tempStrValue = new KAParserCalculationStringResult();
            calculateString(parametersList.get(0), tempStrValue);
            return new Double((double) tempStrValue.getValue().length());
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_RAISEERROR)) {
        	if (parametersList.size() != 1) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
        	raiseError(KAErrorType.USER_EXCEPTION, parametersList.get(0), lexState);
        	return 0;
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_POS)) {
            if (parametersList.size() != 2) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            KAParserCalculationStringResult tempStrValue = new KAParserCalculationStringResult();
            KAParserCalculationStringResult tempStrValue2 = new KAParserCalculationStringResult();
            calculateString(parametersList.get(0), tempStrValue);
            calculateString(parametersList.get(1), tempStrValue2);
            return (double) tempStrValue2.getValue().indexOf(tempStrValue.getValue());
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_TODOUBLE)) {
            if (parametersList.size() != 1) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            KAParserCalculationStringResult tempStrValue = new KAParserCalculationStringResult();
            calculateString(parametersList.get(0), tempStrValue);
            return strToFloatExt(tempStrValue.getValue());
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_DISTANCEFROMTODAY)) {
            if (parametersList.size() != 1) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            KAParserCalculationDoubleResult tempDouble = new KAParserCalculationDoubleResult();
            calculate(parametersList.get(0), tempDouble);
            return Math.floor(KADateTimeUtilities.instantToDouble(KADateTimeUtilities.today()) - tempDouble.getValue());
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_DISTANCEFROMNOW)) {
            if (parametersList.size() != 1) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            KAParserCalculationDoubleResult tempDouble = new KAParserCalculationDoubleResult();
            calculate(parametersList.get(0), tempDouble);
            return KADateTimeUtilities.instantToDouble(Instant.now()) - tempDouble.getValue();
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_GETDAY)) {
            if (parametersList.size() != 1) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            KAParserCalculationDoubleResult tempDouble = new KAParserCalculationDoubleResult();
            calculate(parametersList.get(0), tempDouble);
            return KADateTimeUtilities.getDayOfMonth(KADateTimeUtilities.doubleToInstant(tempDouble.getValue()), zone);
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_GETYEAR)) {
            if (parametersList.size() != 1) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            KAParserCalculationDoubleResult tempDouble = new KAParserCalculationDoubleResult();
            calculate(parametersList.get(0), tempDouble);
            return KADateTimeUtilities.getYear(KADateTimeUtilities.doubleToInstant(tempDouble.getValue()), zone);
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_GETWEEK)) {
            if (parametersList.size() != 1) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            KAParserCalculationDoubleResult tempDouble = new KAParserCalculationDoubleResult();
            calculate(parametersList.get(0), tempDouble);
            return KADateTimeUtilities.doubleToInstant(tempDouble.getValue()).get(ChronoField.ALIGNED_WEEK_OF_YEAR);
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_GETMONTH)) {
            if (parametersList.size() != 1) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            KAParserCalculationDoubleResult tempDouble = new KAParserCalculationDoubleResult();
            calculate(parametersList.get(0), tempDouble);
            return KADateTimeUtilities.getMonth(KADateTimeUtilities.doubleToInstant(tempDouble.getValue()), zone);
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_GETHOUR)) {
            if (parametersList.size() != 1) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            KAParserCalculationDoubleResult tempDouble = new KAParserCalculationDoubleResult();
            calculate(parametersList.get(0), tempDouble);
            return KADateTimeUtilities.getHour(KADateTimeUtilities.doubleToInstant(tempDouble.getValue()), zone);
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_GETMINUTE)) {
            if (parametersList.size() != 1) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            KAParserCalculationDoubleResult tempDouble = new KAParserCalculationDoubleResult();
            calculate(parametersList.get(0), tempDouble);
            return KADateTimeUtilities.getMinute(KADateTimeUtilities.doubleToInstant(tempDouble.getValue()), zone);
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_GETSECOND)) {
            if (parametersList.size() != 1) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            KAParserCalculationDoubleResult tempDouble = new KAParserCalculationDoubleResult();
            calculate(parametersList.get(0), tempDouble);
            return KADateTimeUtilities.getSecond(KADateTimeUtilities.doubleToInstant(tempDouble.getValue()), zone);
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_PREVIOUSMONDAY)) {
            if (parametersList.size() != 1) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            KAParserCalculationDoubleResult tempDouble = new KAParserCalculationDoubleResult();
            calculate(parametersList.get(0), tempDouble);
            return KADateTimeUtilities.instantToDouble(
                    KADateTimeUtilities.previousMonday(KADateTimeUtilities.doubleToInstant(tempDouble.getValue()), zone));
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_STARTOFTHEMONTH)) {
            if (parametersList.size() != 1) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            KAParserCalculationDoubleResult tempDouble = new KAParserCalculationDoubleResult();
            calculate(parametersList.get(0), tempDouble);

            return KADateTimeUtilities.instantToDouble(
                    KADateTimeUtilities.startOfTheMonth(KADateTimeUtilities.doubleToInstant(tempDouble.getValue()), zone));
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_ENDOFTHEMONTH)) {
            if (parametersList.size() != 1) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            KAParserCalculationDoubleResult tempDouble = new KAParserCalculationDoubleResult();
            calculate(parametersList.get(0), tempDouble);

            return KADateTimeUtilities.instantToDouble(
                    KADateTimeUtilities.endOfTheMonth(KADateTimeUtilities.doubleToInstant(tempDouble.getValue()), zone));
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_TODATE)) {
            if (parametersList.size() != 3) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            KAParserCalculationDoubleResult tempDouble = new KAParserCalculationDoubleResult();
            KAParserCalculationDoubleResult tempDouble2 = new KAParserCalculationDoubleResult();
            KAParserCalculationDoubleResult tempDouble3 = new KAParserCalculationDoubleResult();
            calculate(parametersList.get(0), tempDouble);
            calculate(parametersList.get(1), tempDouble2);
            calculate(parametersList.get(2), tempDouble3);
            LocalDate dt = LocalDate.of((int) Math.floor(tempDouble3.getValue()),
                    (int) Math.floor(tempDouble2.getValue()), (int) Math.floor(tempDouble.getValue()));

            return KADateTimeUtilities.instantToDouble(Instant.from(dt.atStartOfDay(this.zone)));
            // return
            // KADateTimeUtilities.instantToDouble(dt.atStartOfDay().toInstant(ZoneOffset.UTC));
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_TODATETIME)) {
            if (parametersList.size() != 6) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            KAParserCalculationDoubleResult tempDouble = new KAParserCalculationDoubleResult();
            KAParserCalculationDoubleResult tempDouble2 = new KAParserCalculationDoubleResult();
            KAParserCalculationDoubleResult tempDouble3 = new KAParserCalculationDoubleResult();
            KAParserCalculationDoubleResult tempDouble4 = new KAParserCalculationDoubleResult();
            KAParserCalculationDoubleResult tempDouble5 = new KAParserCalculationDoubleResult();
            KAParserCalculationDoubleResult tempDouble6 = new KAParserCalculationDoubleResult();
            calculate(parametersList.get(0), tempDouble);
            calculate(parametersList.get(1), tempDouble2);
            calculate(parametersList.get(2), tempDouble3);
            calculate(parametersList.get(3), tempDouble4);
            calculate(parametersList.get(4), tempDouble5);
            calculate(parametersList.get(5), tempDouble6);

            LocalDateTime dt = LocalDateTime.of((int) Math.floor(tempDouble3.getValue()),
                    (int) Math.floor(tempDouble2.getValue()), (int) Math.floor(tempDouble.getValue()),
                    (int) Math.floor(tempDouble4.getValue()), (int) Math.floor(tempDouble5.getValue()),
                    (int) Math.floor(tempDouble6.getValue()));
            return KADateTimeUtilities.instantToDouble(Instant.from(dt));
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_EMPTY)) {
            if (parametersList.size() != 1) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            KAParserCalculationStringResult tempStrValue = new KAParserCalculationStringResult();
            calculateString(parametersList.get(0), tempStrValue);
            return booleanToFloat(tempStrValue.getValue().trim().length() == 0);
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_EQUALS) || funct.equalsIgnoreCase(MP_SPECFUNC_EQUALSIGNORECASE)) {
            if (parametersList.size() != 2) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            KAParserCalculationStringResult tempStrValue = new KAParserCalculationStringResult();
            KAParserCalculationStringResult tempStrValue2 = new KAParserCalculationStringResult();
            calculateString(parametersList.get(0), tempStrValue);
            calculateString(parametersList.get(1), tempStrValue2);
            if (funct.equalsIgnoreCase(MP_SPECFUNC_EQUALS)) {
            	return booleanToFloat((tempStrValue.getValue().compareTo(tempStrValue2.getValue()) == 0));	
            } else {
            	return booleanToFloat(tempStrValue.getValue().equalsIgnoreCase(tempStrValue2.getValue()));
            }
            
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_COMPARETO) || funct.equalsIgnoreCase(MP_SPECFUNC_COMPARETOIGNORECASE)) {
            if (parametersList.size() != 2) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            KAParserCalculationStringResult tempStrValue = new KAParserCalculationStringResult();
            KAParserCalculationStringResult tempStrValue2 = new KAParserCalculationStringResult();
            calculateString(parametersList.get(0), tempStrValue);
            calculateString(parametersList.get(1), tempStrValue2);
            if (funct.equalsIgnoreCase(MP_SPECFUNC_COMPARETO)) {
            	return tempStrValue.getValue().compareTo(tempStrValue2.getValue());
            } else {
            	return tempStrValue.getValue().compareToIgnoreCase(tempStrValue2.getValue());	
            }             
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_AND)) {
            if (parametersList.size() < 2) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            KAParserCalculationDoubleResult tempDouble = new KAParserCalculationDoubleResult();
            calculate(parametersList.get(0), tempDouble);
            if (! floatToBoolean(tempDouble.getValue())) {
            	return booleanToFloat(false);
            } else {
                for (int i = 1; i <= (parametersList.size() - 1); i++) {
                    tempDouble.setValue(0.0);
                    calculate(parametersList.get(i), tempDouble);
                    //tempBoolean = tempBoolean && floatToBoolean(tempDouble.getValue());
                    if (!floatToBoolean(tempDouble.getValue())) {
                    	return booleanToFloat(false);
                    }
                }
            }
            return booleanToFloat(true);
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_OR)) {
            if (parametersList.size() < 2) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            KAParserCalculationDoubleResult tempDouble = new KAParserCalculationDoubleResult();
            calculate(parametersList.get(0), tempDouble);
            if (floatToBoolean(tempDouble.getValue())) {
            	return booleanToFloat(true);
            } else  {
                for (int i = 1; i <= (parametersList.size() - 1); i++) {
                    tempDouble.setValue(0.0);
                    calculate(parametersList.get(i), tempDouble);
                    //tempBoolean = tempBoolean || floatToBoolean(tempDouble.getValue());
                    if (floatToBoolean(tempDouble.getValue())) {
                    	return booleanToFloat(true);                        
                    }
                }
            }
            return booleanToFloat(false);
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_SAFEDIV)) {
            if (parametersList.size() != 2) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            KAParserCalculationDoubleResult tempDouble = new KAParserCalculationDoubleResult();
            KAParserCalculationDoubleResult tempDouble2 = new KAParserCalculationDoubleResult();
            calculate(parametersList.get(0), tempDouble);
            calculate(parametersList.get(1), tempDouble2);

            return KAMathUtilities.safeDiv(tempDouble.getValue(), tempDouble2.getValue());
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_BETWEEN)) {
            if (parametersList.size() != 3) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            KAParserCalculationDoubleResult tempDouble = new KAParserCalculationDoubleResult();
            KAParserCalculationDoubleResult tempDouble2 = new KAParserCalculationDoubleResult();
            KAParserCalculationDoubleResult tempDouble3 = new KAParserCalculationDoubleResult();
            calculate(parametersList.get(0), tempDouble);
            calculate(parametersList.get(1), tempDouble2);
            calculate(parametersList.get(2), tempDouble3);

            return booleanToFloat(KAMathUtilities.doubleIsEqualOrGreater(tempDouble.getValue(), tempDouble2.getValue(),
                    decimalNumbers)
                    && KAMathUtilities.doubleIsEqualOrLessThan(tempDouble.getValue(), tempDouble3.getValue(),
                            decimalNumbers));
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_ROUND)) {
            if (parametersList.size() != 2) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            KAParserCalculationDoubleResult tempDouble = new KAParserCalculationDoubleResult();
            KAParserCalculationDoubleResult tempDouble2 = new KAParserCalculationDoubleResult();
            calculate(parametersList.get(0), tempDouble);
            calculate(parametersList.get(1), tempDouble2);

            return (double) KAMathUtilities.roundTo(tempDouble.getValue(), (int) Math.round(tempDouble2.getValue()));
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_CEIL)) {
            if (parametersList.size() != 1) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            KAParserCalculationDoubleResult tempDouble = new KAParserCalculationDoubleResult();
            calculate(parametersList.get(0), tempDouble);
            return Math.ceil(tempDouble.getValue());
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_FLOOR)) {
            if (parametersList.size() != 1) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            KAParserCalculationDoubleResult tempDouble = new KAParserCalculationDoubleResult();
            calculate(parametersList.get(0), tempDouble);
            return Math.floor(tempDouble.getValue());
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_STRINGTODATETIME)) {
            if (parametersList.size() != 2) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            KAParserCalculationStringResult tempStrValue = new KAParserCalculationStringResult();
            KAParserCalculationStringResult tempStrValue2 = new KAParserCalculationStringResult();
            calculateString(parametersList.get(0), tempStrValue);
            calculateString(parametersList.get(1), tempStrValue2);
            // java è pazzo. Questo per ripristinare un minimo di semplicità e
            // far accettare formati alla Oracle tipo "DD-MM-YYYY".
            tempStrValue2.setValue(tempStrValue2.getValue().replace("DD", "dd").replace("YYYY", "yyyy")
                    .replace("YY", "yy").replace("mm", "MM"));

            SimpleDateFormat sdf = new SimpleDateFormat(tempStrValue2.getValue());
            Date tempDate = new Date();
            try {
                // sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                sdf.setTimeZone(TimeZone.getTimeZone(this.zone));
                tempDate = sdf.parse(tempStrValue.getValue());
            } catch (ParseException e) {
                raiseError(KAErrorType.PARSE_ERROR, tempStrValue.getValue(), lexState);
            }

            return KADateTimeUtilities.instantToDouble(DateToInstant(tempDate));
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_NOT)) {
            if (parametersList.size() != 1) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            KAParserCalculationDoubleResult tempDouble = new KAParserCalculationDoubleResult();
            calculate(parametersList.get(0), tempDouble);
            if (floatToBoolean(tempDouble.getValue())) {
                return 0;
            } else {
                return 1;
            }
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_SUM)) {
            if (parametersList.isEmpty()) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            ArrayList<Object> tempValuesArray = new ArrayList<>();

            manageRangeFunction(funct, parametersList, KAParserValueType.TYPE_FLOAT, tempValuesArray, lexState);

            KAParserCalculationDoubleResult tempDouble = new KAParserCalculationDoubleResult();

            for (int i = 0; i < tempValuesArray.size(); i++) {
                tempDouble.setValue(tempDouble.getValue() + (Double) tempValuesArray.get(i));
            }
            return tempDouble.getValue();
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_MAX)) {
            if (parametersList.isEmpty()) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            ArrayList<Object> tempValuesArray = new ArrayList<>();

            manageRangeFunction(funct, parametersList, KAParserValueType.TYPE_FLOAT, tempValuesArray, lexState);

            KAParserCalculationDoubleResult tempDouble = new KAParserCalculationDoubleResult();

            if (tempValuesArray.isEmpty()) {
                tempDouble.setValue(0.0);
            } else {
                tempDouble.setValue((Double) tempValuesArray.get(0));
            }
            for (int i = 1; i < tempValuesArray.size(); i++) {
                if ((Double) tempValuesArray.get(i) > tempDouble.getValue()) {
                    tempDouble.setValue((Double) tempValuesArray.get(i));
                }
            }
            return tempDouble.getValue();
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_MIN)) {
            if (parametersList.isEmpty()) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            ArrayList<Object> tempValuesArray = new ArrayList<>();

            manageRangeFunction(funct, parametersList, KAParserValueType.TYPE_FLOAT, tempValuesArray, lexState);

            KAParserCalculationDoubleResult tempDouble = new KAParserCalculationDoubleResult();

            if (tempValuesArray.isEmpty()) {
                tempDouble.setValue(0.0);
            } else {
                tempDouble.setValue((Double) tempValuesArray.get(0));
            }
            for (int i = 1; i < tempValuesArray.size(); i++) {
                if ((Double) tempValuesArray.get(i) < tempDouble.getValue()) {
                    tempDouble.setValue((Double) tempValuesArray.get(i));
                }
            }
            return tempDouble.getValue();
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_AVG)) {
            if (parametersList.isEmpty()) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            ArrayList<Object> tempValuesArray = new ArrayList<>();

            manageRangeFunction(funct, parametersList, KAParserValueType.TYPE_FLOAT, tempValuesArray, lexState);

            Double tempDouble = 0.0;

            for (int i = 0; i < tempValuesArray.size(); i++) {
                tempDouble = tempDouble + (Double) tempValuesArray.get(i);
            }
            if (!tempValuesArray.isEmpty()) {
                tempDouble = tempDouble / tempValuesArray.size();
            }
            return tempDouble;
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_COUNT)) {
            if (parametersList.isEmpty()) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return 0;
            }
            ArrayList<Object> tempValuesArray = new ArrayList<>();

            manageRangeFunction(funct, parametersList, KAParserValueType.TYPE_FLOAT, tempValuesArray, lexState);
            return tempValuesArray.size();
        } else {
            raiseError(KAErrorType.FUNCTION_UNKNOWN, lexState);
            return 0;
        }
    }

    private String executeStrFunction(final String funct, ArrayList<String> parametersList, KALexStatus lexState)
            throws KAParserException {
        if (funct.equalsIgnoreCase(MP_SPECFUNC_IF)) {
            if (parametersList.size() != 3) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return "";
            }

            KAParserCalculationDoubleResult tempValue = new KAParserCalculationDoubleResult();

            calculate(parametersList.get(0), tempValue);

            KAParserCalculationStringResult tempStrValue = new KAParserCalculationStringResult();

            if (KAMathUtilities.doubleIsEqualOrGreater(tempValue.getValue(), 1.0, decimalNumbers)) {
                calculateString(parametersList.get(1), tempStrValue);
            } else {
                calculateString(parametersList.get(2), tempStrValue);
            }

            return tempStrValue.getValue();
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_REPLICATE)) {
            if (parametersList.size() != 2) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return "";
            }
            KAParserCalculationStringResult tempStrValue = new KAParserCalculationStringResult();
            calculateString(parametersList.get(0), tempStrValue);
            KAParserCalculationDoubleResult tempValue = new KAParserCalculationDoubleResult();
            calculate(parametersList.get(1), tempValue);
            String tempStrValue2 = tempStrValue.getValue();
            for (int i = 1; i <= (int) Math.round(tempValue.getValue()); i++) {
                tempStrValue2 = tempStrValue2 + tempStrValue.getValue();
            }
            return tempStrValue2;
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_CONCATENATE)) {
            if (parametersList.size() < 2) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return "";
            }

            KAParserCalculationStringResult tempStrValue = new KAParserCalculationStringResult();
            KAParserCalculationStringResult tempStrValue2 = new KAParserCalculationStringResult();
            for (int i = 0; i < parametersList.size(); i++) {
                calculateString(parametersList.get(i), tempStrValue);
                tempStrValue2.setValue(tempStrValue2.getValue() + tempStrValue.getValue());
            }
            return tempStrValue2.getValue();
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_TOSTR)) {
            if (parametersList.size() != 1) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return "";
            }
            KAParserCalculationDoubleResult TempValue = new KAParserCalculationDoubleResult();
            calculate(parametersList.get(0), TempValue);
            return TempValue.getValue().toString();
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_UPPERCASE)) {
            if (parametersList.size() != 1) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return "";
            }
            KAParserCalculationStringResult tempStrValue = new KAParserCalculationStringResult();
            calculateString(parametersList.get(0), tempStrValue);
            return tempStrValue.getValue().toUpperCase();
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_LOWERCASE)) {
            if (parametersList.size() != 1) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return "";
            }
            KAParserCalculationStringResult tempStrValue = new KAParserCalculationStringResult();
            calculateString(parametersList.get(0), tempStrValue);
            return tempStrValue.getValue().toLowerCase();
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_LEFT)) {
            if (parametersList.size() != 2) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return "";
            }
            KAParserCalculationStringResult tempStrValue = new KAParserCalculationStringResult();
            KAParserCalculationDoubleResult tempValue = new KAParserCalculationDoubleResult();
            calculateString(parametersList.get(0), tempStrValue);
            calculate(parametersList.get(1), tempValue);

            return tempStrValue.getValue().substring(0,Math.min(tempStrValue.getValue().length(), (int) Math.round(tempValue.getValue())));
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_RIGHT)) {
            if (parametersList.size() != 2) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return "";
            }
            KAParserCalculationStringResult tempStrValue = new KAParserCalculationStringResult();
            calculateString(parametersList.get(0), tempStrValue);
            KAParserCalculationDoubleResult tempValue = new KAParserCalculationDoubleResult();
            calculate(parametersList.get(1), tempValue);
            return tempStrValue.getValue().substring(Math.max(0, tempStrValue.getValue().length() - (int) Math.round(tempValue.getValue())));
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_SUBSTR)) {
            if (parametersList.size() != 3) {
                raiseError(KAErrorType.WRONG_PARAM_COUNT, lexState);
                return "";
            }
            KAParserCalculationStringResult tempStrValue = new KAParserCalculationStringResult();
            calculateString(parametersList.get(0), tempStrValue);
            KAParserCalculationDoubleResult tempValue = new KAParserCalculationDoubleResult();
            calculate(parametersList.get(1), tempValue);
            KAParserCalculationDoubleResult tempValue2 = new KAParserCalculationDoubleResult();
            calculate(parametersList.get(2), tempValue2);
            return tempStrValue.getValue().substring((int) Math.round(tempValue.getValue()), (int) Math.round(tempValue.getValue()) + (int) Math.round(tempValue2.getValue())); // -1, alla pascal!
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_SUM)) {
            ArrayList<Object> tempValuesArray = new ArrayList<>();
            manageRangeFunction(funct, parametersList, KAParserValueType.TYPE_STRING, tempValuesArray, lexState);
            String tempStrValue = "";
            for (int i = 0; i < tempValuesArray.size(); i++) {
                tempStrValue = tempStrValue + tempValuesArray.get(i).toString();
            }
            return tempStrValue;
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_MAX)) {
            ArrayList<Object> tempValuesArray = new ArrayList<>();
            manageRangeFunction(funct, parametersList, KAParserValueType.TYPE_STRING, tempValuesArray, lexState);
            String tempStrValue = "";
            for (int i = 0; i < tempValuesArray.size(); i++) {
                if (tempStrValue.compareTo(tempValuesArray.get(i).toString()) < 0) {
                    tempStrValue = tempValuesArray.get(i).toString();
                }
            }
            return tempStrValue;
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_MIN)) {
            ArrayList<Object> tempValuesArray = new ArrayList<>();
            manageRangeFunction(funct, parametersList, KAParserValueType.TYPE_STRING, tempValuesArray, lexState);
            String tempStrValue = "";
            for (int i = 0; i < tempValuesArray.size(); i++) {
                if (tempStrValue.compareTo(tempValuesArray.get(i).toString()) > 0) {
                    tempStrValue = tempValuesArray.get(i).toString();
                }
            }
            return tempStrValue;
        } else if (funct.equalsIgnoreCase(MP_SPECFUNC_COUNT)) {
            ArrayList<Object> tempValuesArray = new ArrayList<>();
            manageRangeFunction(funct, parametersList, KAParserValueType.TYPE_STRING, tempValuesArray, lexState);
            return Integer.toString(tempValuesArray.size());
        } else {
            raiseError(KAErrorType.FUNCTION_UNKNOWN, lexState);
            return "";
        }

    }

    private String formatErrorString(final KAErrorType errorCode, final String currentIdent,
            final KALexStatus lexState) {
        String errorDescription;

        switch (errorCode) {
            case INVALID_STRING:
                errorDescription = "Invalid string";
                break;
            case SYNTAX_ERROR:
                errorDescription = "Syntax error";
                break;
            case FUNCTION_ERROR:
                errorDescription = "Function error";
                break;
            case WRONG_PARAM_COUNT:
                errorDescription = "Wrong parameters count";
                break;
            case FUNCTION_UNKNOWN:
                errorDescription = "Function unknown";
                break;
            case PARSE_ERROR:
                errorDescription = "Parse error";
                break;
            case EXTERNAL_FUNCTION_ERROR:
                errorDescription = "External function error";
                break;
            case USER_EXCEPTION:
            	errorDescription = "User exception";
            	break;
            default:
                errorDescription = "Unknown error";
        }
        if ((!currentIdent.isEmpty()) && (currentIdent.length() > 0)) {
            errorDescription = String.format(
                    "Error %s (description: %s) while validating %s in formula [%s] before position %s",
                    errorCode.toString(), errorDescription, currentIdent, lexState.getFormula(),
                    lexState.getCharIndex().toString());
        } else {
            errorDescription = String.format("Error %s (description: %s) in formula [%s] before position %s",
                    errorCode.toString(), errorDescription, lexState.getFormula(), lexState.getCharIndex().toString());
        }

        return errorDescription;
    }

    private void raiseError(final KAErrorType errorCode, final KALexStatus lexState) throws KAParserException {
        String tempErrorString = formatErrorString(errorCode, "generic error", lexState);
        throw new KAParserException(tempErrorString);
    }

    private void raiseError(final KAErrorType errorCode, final String errorMessage, final KALexStatus lexState)
            throws KAParserException {
        String tempErrorString = formatErrorString(errorCode, errorMessage, lexState);
        throw new KAParserException(tempErrorString);
    }

    private double booleanToFloat(final Boolean aValue) {
        if (aValue) {
            return 1;
        } else {
            return 0;
        }
    }

    private Boolean floatToBoolean(final double aValue) {
        return Math.round(Math.abs(aValue)) >= 1;
    }

    private double strToFloatExt(final String value) {
        String tempValue = value;

        // float.Parse(aValue, CultureInfo.InvariantCulture.NumberFormat);

        if (tempValue.indexOf(',') >= 0) {
            tempValue = tempValue.replace(',', '.');
        }

        return Double.parseDouble(tempValue);
    }

    public void calculate(final String formula, KAParserCalculationDoubleResult resValue) throws KAParserException {
        String newFormula = cleanFormula(formula);
        KALexStatus tempKaLexStatus = new KALexStatus();
        KALexComputationResult tempKaLexComputationResult = new KALexComputationResult();

        tempKaLexStatus.setFormula(newFormula);

        yylex(tempKaLexStatus, tempKaLexComputationResult);

        startCalculate(resValue, tempKaLexStatus, tempKaLexComputationResult);
    }

    public void calculateString(final String formula, KAParserCalculationStringResult resValue) throws KAParserException {
        String newFormula = cleanFormula(formula);
        KALexStatus tempKaLexStatus = new KALexStatus();
        KALexComputationResult tempKaLexComputationResult = new KALexComputationResult();

        tempKaLexStatus.setFormula(newFormula);

        yylex(tempKaLexStatus, tempKaLexComputationResult);

        startCalculateStr(resValue, tempKaLexStatus, tempKaLexComputationResult);
    }
    
    private Instant DateToInstant(Date value) {
    	if (value != null) {
    		return value.toInstant();
    	} else {
    		return null;
    	}
    }
    
}
