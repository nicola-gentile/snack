package org.snack.compiler.lexer;

import lombok.val;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LexerTest {

    @Test
    public void scanIntegerNumbersTest() throws Exception {
        val integersCode = Map.of(
                "12)", "12",
                "01234567890", "1234567890",
                "0b01101", "13",
                "0B110010", "50",
                "0o01234567", "342391",
                "0O1230", "664",
                "0x01234abcdef", "78193085935",
                "0X56789ABCDEF", "5942249508335"
        );
        for(val key : integersCode.keySet()) {
            try(val scanner = new LexerScanner(key)) {
                val lexer = new Lexer(scanner);
                val actual = lexer.scanIntegerNumber();
                val expected = Optional.of(new IntegerNumber(new BigInteger(integersCode.get(key))));
                Assert.assertEquals(expected, actual);
            }
        }
//        val wrongIntegers = List.of(
//                "123abc", "12a34",
//                "0xsnack",
//                "0b", "0B", "0b2", "0b3", "0b4", "0b5", "0b6", "0b7", "0b8", "0b9", "0b120",
//                "0o", "0O", "0o8", "0o9", "0O1C4",
//                "0x", "0X", "0xP", "0Xy"
//        );
//        for(val s : wrongIntegers) {
//            val actual = new Lexer(s).scanIntegerNumber();
//            val expected = Optional.empty();
//            Assert.assertEquals(expected, actual);
//        }
    }

    @Test
    public void scanRealNumbersTest() throws Exception {
        val floatsCode = List.of(
                "1.0", "-2.", "03.00",
                "-.04", "0.05", "67.89e2",
                ".4E-4", "56.e-345", "89E7"
        );
        for(val s : floatsCode) {
            try(val scanner = new LexerScanner(s)) {
                val lexer = new Lexer(scanner);
                val actual = lexer.scanRealNumber();
                val expected = Optional.of(new RealNumber(new BigDecimal(s)));
                Assert.assertEquals(expected, actual);
            }
        }
    }

    @Test
    public void scanIdentifierTest() throws Exception {
        val identifiers = List.of(
                "var", "Compare'", "Maybe", "lines!", "$MACRO_ID",
                "derivate'", "f123?'", "conatins?", "magic_var'?!"
        );
        for(val s : identifiers) {
            try(val scanner = new LexerScanner(s)) {
                val lexer = new Lexer(scanner);
                val actual = lexer.scanIdentifier();
                val expected = Optional.of(new Identifier(s));
                Assert.assertEquals(expected, actual);
            }
        }
    }

    @Test
    public void scanInvalidTokenTest() throws Exception {
        val invalidTokens = List.of(
                "123abc456", "-45.4E7j'?",
                "-12.3e-3hgf",
                "123abc", "12a34",
                "0xsnack",
                "0b", "0B", "0b2", "0b3", "0b4", "0b5", "0b6", "0b7", "0b8", "0b9", "0b120",
                "0o", "0O", "0o8", "0o9", "0O1C4",
                "0x", "0X", "0xP", "0Xy",
                "0b", "0B", "0o", "0O", "0x", "0X",
                "0b2", "0B3", "0b4", "0B5", "0b6", "0B7", "0b8", "0B9",
                "0o8", "0O9",
                "0xfg2w"
        );
        for(val s : invalidTokens) {
            try(val scanner = new LexerScanner(s)) {
                val lexer = new Lexer(scanner);
                val actual = lexer.scanInvalidToken();
                val expected = Optional.of(new InvalidToken(s));
                Assert.assertEquals(expected, actual);
            }
        }
    }

    @Test
    public void scanCommentTest() throws Exception {
        val comments = Map.of(
            "#blablabla\nblablabla", "blablabla",
            "#hello#world\nprintln(\"hello world\")", "hello#world",
            "#first\n#second", "first",
            "#{firstline\nsecondline}# otherstaff", "firstline\nsecondline",
            "#{firstline}\nsecondline}#", "firstline}\nsecondline"
        );
        for(val s : comments.keySet()) {
            try(val scanner = new LexerScanner(s)) {
                val lexer = new Lexer(scanner);
                val expected = Optional.of(new Comment(comments.get(s)));
                val actual = lexer.scanComment();
                Assert.assertEquals(expected, actual);
            }
        }
    }

    @Test
    public void scanNewLineTest() throws Exception {
        try(val lexerScanner = new LexerScanner("\n")) {
            Assert.assertEquals(Optional.of(new NewLine()), new Lexer(lexerScanner).scanNewLine());
        }
    }

    @Test
    public void scanCharacterTest() throws Exception {
        val chars = Map.of(
                "'a' - 7", 'a',
                "'\\n' end", '\n',
                "'\\t' do", '\t',
                "'\\r' \n", '\r',
                "'\\b'", '\b',
                "'\\\\'", '\\',
                "'\\f'", '\f',
                "'\\''", '\'',
                "'\\x41'", 'A',
                "'\\u42'", 'B'
        );
        for(val p : chars.entrySet()) {
            try(val scanner = new LexerScanner(p.getKey())) {
                val lexer = new Lexer(scanner);
                val expected = Optional.of(new Char(p.getValue()));
                val actual = lexer.scanChar();
                Assert.assertEquals(expected, actual);
            }
        }
    }

    @Test
    public void scanStringTest() throws Exception {
        val strings = Map.of(
                "\"hello world\"", "hello world",
                "\"hey mate\\nhere a new line\"", "hey mate\\nhere a new line",
                "\"this is an escape character \\\" which is a double quote\"", "this is an escape character \\\" which is a double quote"
        );
        for(val p : strings.entrySet()) {
            try(val scanner = new LexerScanner(p.getKey())) {
                val lexer = new Lexer(scanner);
                val expected = Optional.of(new Str(p.getValue()));
                val actual = lexer.scanStr();
                Assert.assertEquals(expected, actual);
            }
        }
    }

}
