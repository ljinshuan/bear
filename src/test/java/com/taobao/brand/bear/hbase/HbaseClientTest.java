package com.taobao.brand.bear.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * @author jinshuan.li 2018/8/11 11:58
 */
public class HbaseClientTest {

    @Test
    public void test() throws IOException {

        Configuration conf = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(conf);

        TableName tableName = TableName.valueOf("test");
        Table table = connection.getTable(tableName);

        Scan san = new Scan();
        san.addFamily(Bytes.toBytes("cf"));
        ResultScanner scanner = table.getScanner(san);

        scanner.forEach(new Consumer<Result>() {
            @Override
            public void accept(Result result) {

                System.out.println(result);
            }
        });

    }
}


