package com.taobao.brand.bear.antlr;


import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;

/**
 * @author jinshuan.li 2018/6/24 15:12
 */
public class Test {

    public static void main(String[] args) {

        ANTLRStringStream input = new ANTLRStringStream("{99,{2},3445}");
        ArrayInitLexer lexter = new ArrayInitLexer((CharStream)input);
        CommonTokenStream tokens = new CommonTokenStream(lexter);
        ArrayInitParser parser = new ArrayInitParser(tokens);

        ArrayInitParser.InitContext init = parser.init();

        System.out.println(init.toStringTree(parser));
    }
}
