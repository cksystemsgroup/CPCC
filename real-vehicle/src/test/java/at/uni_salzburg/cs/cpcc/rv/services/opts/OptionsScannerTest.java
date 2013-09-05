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
import java.io.StringReader;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * OptionsScannerTest
 */
public class OptionsScannerTest
{
    /**
     * @throws IOException thrown in case of errors.
     * @throws ParseException thrown in case of errors.
     */
    @Test
    public void shouldScanOneOption() throws IOException, ParseException
    {
        Reader reader = new StringReader("bugger=lala");
        OptionsScanner scanner = new OptionsScanner(reader );
        
        Token token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "bugger");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.EQUALS);
        Assert.assertEquals(token.getItemString(), "=");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "lala");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.END);
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.END);
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.END);
    }
    
    /**
     * @throws IOException thrown in case of errors.
     * @throws ParseException thrown in case of errors.
     */
    @Test
    public void shouldScanMultipleOptions() throws IOException, ParseException
    {
        Reader reader = new StringReader("bugger=lala\nbugger2=lala2");
        OptionsScanner scanner = new OptionsScanner(reader );
        
        Token token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "bugger");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.EQUALS);
        Assert.assertEquals(token.getItemString(), "=");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "lala");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "bugger2");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.EQUALS);
        Assert.assertEquals(token.getItemString(), "=");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "lala2");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.END);
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.END);
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.END);
    }
    
    /**
     * @throws IOException thrown in case of errors.
     * @throws ParseException thrown in case of errors.
     */
    @Test
    public void shouldScanOptionsLists() throws IOException, ParseException
    {
        Reader reader = new StringReader("bugger=(lala,blbla,'nix xx')\nbugger2=('lala2';'xxx')\n");
        OptionsScanner scanner = new OptionsScanner(reader );
        
        Token token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "bugger");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.EQUALS);
        Assert.assertEquals(token.getItemString(), "=");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.LEFT_PAREN);
        Assert.assertEquals(token.getItemString(), "(");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "lala");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.COMMA);
        Assert.assertEquals(token.getItemString(), ",");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "blbla");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.COMMA);
        Assert.assertEquals(token.getItemString(), ",");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.LITERAL);
        Assert.assertEquals(token.getItemString(), "nix xx");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.RIGHT_PAREN);
        Assert.assertEquals(token.getItemString(), ")");
        
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "bugger2");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.EQUALS);
        Assert.assertEquals(token.getItemString(), "=");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.LEFT_PAREN);
        Assert.assertEquals(token.getItemString(), "(");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.LITERAL);
        Assert.assertEquals(token.getItemString(), "lala2");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.SEMICOLON);
        Assert.assertEquals(token.getItemString(), ";");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.LITERAL);
        Assert.assertEquals(token.getItemString(), "xxx");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.RIGHT_PAREN);
        Assert.assertEquals(token.getItemString(), ")");
        
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.END);
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.END);
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.END);
    }
    
    /**
     * @throws IOException thrown in case of errors.
     * @throws ParseException thrown in case of errors.
     */
    @Test
    public void shouldScanNumberOptions01() throws IOException, ParseException
    {
        Reader reader = new StringReader("bugger=3.14\nbugger2=99999");
        OptionsScanner scanner = new OptionsScanner(reader );
        
        Token token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "bugger");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.EQUALS);
        Assert.assertEquals(token.getItemString(), "=");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.NUMBER);
        Assert.assertEquals(token.getItemString(), "3.14");
        Assert.assertEquals(token.getNumber().doubleValue(), 3.14, 1E-5);
    
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "bugger2");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.EQUALS);
        Assert.assertEquals(token.getItemString(), "=");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.NUMBER);
        Assert.assertEquals(token.getItemString(), "99999");
        Assert.assertEquals(token.getNumber().intValueExact(), 99999);
    }
    
    /**
     * @throws IOException thrown in case of errors.
     * @throws ParseException thrown in case of errors.
     */
    @Test
    public void shouldScanNumberOptions02() throws IOException, ParseException
    {
        Reader reader = new StringReader("bugger=lala looney=3.141592 caspar='xxx uu'");
        OptionsScanner scanner = new OptionsScanner(reader );
        
        Token token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "bugger");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.EQUALS);
        Assert.assertEquals(token.getItemString(), "=");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "lala");
        
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "looney");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.EQUALS);
        Assert.assertEquals(token.getItemString(), "=");      
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.NUMBER);
        Assert.assertEquals(token.getItemString(), "3.141592");
        Assert.assertEquals(token.getNumber().doubleValue(), 3.141592, 1E-8);
    

        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "caspar");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.EQUALS);
        Assert.assertEquals(token.getItemString(), "=");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.LITERAL);
        Assert.assertEquals(token.getItemString(), "xxx uu");
    }
}
