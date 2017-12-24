package com.taobao.brand.bear.graphql;

import com.taobao.brand.bear.domain.Dog;
import com.taobao.brand.bear.service.DogService;
import com.taobao.brand.bear.utils.StringHelper;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.*;
import graphql.schema.idl.*;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Maps;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;

/**
 * @author jinshuan.li 2017/12/24 06:59
 */
@Slf4j
public class AppTest {

    private DogService dogService = new DogService();

    public static void main(String[] args) {

        String schema = "type Query{hello: String}";

        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);

        RuntimeWiring runtimeWiring = newRuntimeWiring()
            .type("Query", builder -> builder.dataFetcher("hello", new StaticDataFetcher("world")))
            .build();

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);

        GraphQL build = GraphQL.newGraphQL(graphQLSchema).build();
        ExecutionResult executionResult = build.execute("{hello}");

        System.out.println(executionResult.getData().toString());
        // Prints: {hello=world}
    }

    @Test
    public void testStart() {

        String schema = "type Query{name:String!}";

        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);

        RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring().type("Query",
            new UnaryOperator<TypeRuntimeWiring.Builder>() {
                @Override
                public TypeRuntimeWiring.Builder apply(TypeRuntimeWiring.Builder builder) {

                    return builder.dataFetcher("name", new StaticDataFetcher("ljinshuan"));
                }
            }).build();

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);

        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);

        GraphQL graphQL = GraphQL.newGraphQL(graphQLSchema).build();

        ExecutionResult execute = graphQL.execute("{name}");

        String s = StringHelper.toJsonString(execute);

        log.info(s);

    }

    @Test
    public void testFileSchema() {

        SchemaParser schemaParser = new SchemaParser();
        File file = new File("");
        log.info(file.getPath());
        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(
            new File("/Users/jinshuan.li/Sources/bear/src/test/resources/schema.graphql"));

        RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring().type("Query",
            builder -> builder.dataFetcher("allDogs", environment -> dogService.getAllDogs()).dataFetcher("dog",
                environment -> dogService.getDog("ljinshuan")).dataFetcher("dog2", environment -> {
                String name = environment.getArgument("name");
                return dogService.getDog(name);
            }).dataFetcher("map", environment -> {
                Map<String, Object> data = new HashMap<>();
                data.put("mapKey", "mapValue");
                return data;
            })).scalar(new MapScalarType()).build();

        SchemaGenerator schemaGenerator = new SchemaGenerator();

        schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);

        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);

        GraphQL graphQL = GraphQL.newGraphQL(graphQLSchema).build();

        //String query = "{dog2(name:\"lll\"){name,age}}";
        //String query="{map}";

        String query = "query ($name:String!){dog2(name:$name){name,age}}";
        Map<String, Object> variables = Maps.newHashMap("name", "Louise");
        ExecutionInput executionInput = ExecutionInput.newExecutionInput().context(null).query(query).variables(
            variables)
            .build();
        ExecutionResult execute = graphQL.execute(executionInput);

        String s = StringHelper.toJsonString(execute);

        log.info(s);
    }
}
