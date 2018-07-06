package com.taobao.brand.bear.antlr;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * @author jinshuan.li 2018/6/24 15:12
 */
public class Test {

    public static void main(String[] args) {

        CodePointCharStream codePointCharStream = CharStreams.fromString("{99,33,3445}");
        ArrayInitLexer lexter = new ArrayInitLexer(codePointCharStream);
        CommonTokenStream tokens = new CommonTokenStream(lexter);
        ArrayInitParser parser = new ArrayInitParser(tokens);

        ArrayInitParser.InitContext init = parser.init();

        System.out.println(init.toStringTree(parser));

        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new ShortToUnicodeString(), init);

        System.out.println();
    }
}
