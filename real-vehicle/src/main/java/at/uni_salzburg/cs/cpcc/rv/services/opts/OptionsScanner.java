/*
 * This code is part of the CPCC-NG project.
 *
 * Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package at.uni_salzburg.cs.cpcc.rv.services.opts;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * OptionsScanner
 */
public class OptionsScanner
{
    @SuppressWarnings("serial")
    private static final Map<String, Token> charMap = new HashMap<String, Token>()
    {
        {
            put(Symbol.EQUALS.getSymbolString(),
                new Token(Symbol.EQUALS, Symbol.EQUALS.getSymbolString(), null));
            put(Symbol.LEFT_PAREN.getSymbolString(),
                new Token(Symbol.LEFT_PAREN, Symbol.LEFT_PAREN.getSymbolString(), null));
            put(Symbol.RIGHT_PAREN.getSymbolString(),
                new Token(Symbol.RIGHT_PAREN, Symbol.RIGHT_PAREN.getSymbolString(), null));
            put(Symbol.SEMICOLON.getSymbolString(),
                new Token(Symbol.SEMICOLON, Symbol.SEMICOLON.getSymbolString(), null));
            put(Symbol.COLON.getSymbolString(),
                new Token(Symbol.COLON, Symbol.COLON.getSymbolString(), null));
            put(Symbol.COMMA.getSymbolString(),
                new Token(Symbol.COMMA, Symbol.COMMA.getSymbolString(), null));
        }
    };

    private Reader reader;
    private Character ch = null;
    private boolean firstRun = true;
    private int lineNumber = 1;
    private int columnNumber = 0;

    private static boolean isDigit(Character c)
    {
        if (c == null)
        {
            return false;
        }
        return c >= '0' && c <= '9';
    }

    private static boolean isLetter(Character c)
    {
        if (c == null)
        {
            return false;
        }
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == '_';
    }

    private static boolean isSpace(Character c)
    {
        if (c == null)
        {
            return false;
        }
        return c == ' ' || c == '\t' || c == '\r' || c == '\n';
    }

    /**
     * @param reader the reader
     */
    public OptionsScanner(Reader reader)
    {
        this.reader = reader;
    }

    /**
     * @return the line number.
     */
    public int getLineNumber()
    {
        return lineNumber;
    }

    /**
     * @return the column number.
     */
    public int getColumnNumber()
    {
        return columnNumber;
    }

    private Character getChar() throws IOException
    {
        char[] cbuf = new char[1];
        int n = reader.read(cbuf);
        if (n <= 0)
        {
            return null;
        }

        ++columnNumber;
        if (cbuf[0] == '\n')
        {
            ++lineNumber;
            columnNumber = 0;
        }

        return cbuf[0];
    }

    public Token next() throws IOException
    {
        if (firstRun)
        {
            ch = getChar();
            firstRun = false;
        }

        while (isSpace(ch))
        {
            ch = getChar();
        }

        if (ch == null)
        {
            return new Token(Symbol.END, null, null);
        }

        // number
        if (isDigit(ch))
        {
            StringBuilder b = new StringBuilder();
            boolean decimalSeparatorFound = false;

            do
            {
                b.append(ch);
                ch = getChar();
                if (ch == null)
                {
                    break;
                }
                if (!decimalSeparatorFound && ch == '.')
                {
                    b.append(ch);
                    decimalSeparatorFound = true;
                    ch = getChar();
                }
            } while (isDigit(ch));

            return new Token(Symbol.NUMBER, b.toString(), new BigDecimal(b.toString()));
        }

        // string literal
        if (ch == '"' || ch == '\'')
        {
            int delimiter = ch;

            StringBuilder b = new StringBuilder();
            while ((ch = getChar()) != null && ch != delimiter)
            {
                b.append(ch);
            }

            ch = getChar();

            return new Token(Symbol.LITERAL, b.toString(), null);
        }

        // identifier
        if (isLetter(ch))
        {
            StringBuilder b = new StringBuilder();
            b.append(ch);

            while ((ch = getChar()) != null && (isLetter(ch) || isDigit(ch)))
            {
                b.append(ch);
            }

            String identifier = b.toString();

            Symbol sym = Symbol.getSymbol(identifier);

            if (sym != null)
            {
                return new Token(sym, identifier, null);
            }

            return new Token(Symbol.IDENT, identifier, null);
        }

        if (ch == '=')
        {
            ch = getChar();
            return new Token(Symbol.EQUALS, "=", null);
        }

        String cs = String.valueOf(ch);
        ch = getChar();

        Token token = charMap.get(cs);

        if (token != null)
        {
            return token;
        }

        return new Token(Symbol.OTHER, cs, null);
    }
}
