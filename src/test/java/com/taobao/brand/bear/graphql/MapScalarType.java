package com.taobao.brand.bear.graphql;

import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;

/**
 * @author jinshuan.li 2017/12/24 22:40
 */
public class MapScalarType extends GraphQLScalarType {

    public MapScalarType() {
        super("Map", "xxx", new Coercing() {

            @Override
            public Object serialize(Object dataFetcherResult) {
                return dataFetcherResult;
            }

            @Override
            public Object parseValue(Object input) {
                return input;
            }

            @Override
            public Object parseLiteral(Object input) {
                return input;
            }
        });
    }

    public MapScalarType(String name, String description, Coercing coercing) {
        super(name, description, coercing);
    }

}
