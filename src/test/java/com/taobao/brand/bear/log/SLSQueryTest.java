package com.taobao.brand.bear.log;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.log.Client;
import com.aliyun.openservices.log.common.Histogram;
import com.aliyun.openservices.log.common.QueriedLog;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.request.GetHistogramsRequest;
import com.aliyun.openservices.log.request.GetLogsRequest;
import com.aliyun.openservices.log.request.ListLogStoresRequest;
import com.aliyun.openservices.log.response.GetHistogramsResponse;
import com.aliyun.openservices.log.response.GetLogsResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * @author jinshuan.li 2018/7/16 14:39
 */
@Slf4j
public class SLSQueryTest {

    private static final String endpoint = "http://cn-hangzhou-corp.sls.aliyuncs.com";

    static String accessKeyId = "LTAIbiK1jNAguyqk"; // 使用您的阿里云访问密钥 AccessKeyId
    static String accessKeySecret = "cJq0IEbqXn5LpufXxRwlNtBFvihWj2"; // 使用您的阿里云访问密钥

    static final String project = "tmall-wireless-sls"; // 上面步骤创建的项目名称
    static final String logstore = "tac-log-store"; // 上面步骤创建的日志库名称

    private Client client;

    @Before
    public void before() {

        // Endpoint

        // AccessKeySecret
        // 构建一个客户端实例
        this.client = new Client(endpoint, accessKeyId, accessKeySecret);

    }

    @Test
    public void listStoreNames() throws LogException {

        // 列出当前 project 下的所有日志库名称
        int offset = 0;
        int size = 100;
        String logStoreSubName = "";
        ListLogStoresRequest req1 = new ListLogStoresRequest(project, offset, size, logStoreSubName);
        ArrayList<String> logStores = client.ListLogStores(req1).GetLogStores();
        System.out.println("ListLogs:" + logStores.toString() + "\n");
    }

    @Test
    public void queryTest() throws LogException, InterruptedException {

        String query
            = "* and __tag__:__path__: /home/admin/tac/logs/tac-user.log.2017092900 and "
            + "0ba9e91b15317237716222604e7fc1 ";

        String topic = "";
        // 查询日志分布情况
        int from = (int)(new Date().getTime() / 1000 - 3000);
        int to = (int)(new Date().getTime() / 1000);
        GetHistogramsResponse res3 = null;
        while (true) {
            GetHistogramsRequest req3 = new GetHistogramsRequest(project, logstore, topic, query, from, to);
            res3 = client.GetHistograms(req3);
            if (res3 != null && res3.IsCompleted()) // IsCompleted() 返回
            // true，表示查询结果是准确的，如果返回
            // false，则重复查询
            {
                break;
            }
            Thread.sleep(200);
        }
        System.out.println("Total count of logs is " + res3.GetTotalCount());
        for (Histogram ht : res3.GetHistograms()) {
            System.out.printf("from %d, to %d, count %d.\n", ht.GetFrom(), ht.GetTo(), ht.GetCount());
        }
        // 查询日志数据
        long total_log_lines = res3.GetTotalCount();
        int log_offset = 0;
        int log_line
            = 10;//log_line 最大值为100，每次获取100行数据。若需要读取更多数据，请使用offset翻页。offset和lines只对关键字查询有效，若使用SQL查询，则无效。在SQL
        // 查询中返回更多数据，请使用limit语法。
        while (log_offset <= total_log_lines) {
            GetLogsResponse res4 = null;
            // 对于每个 log offset,一次读取 10 行 log，如果读取失败，最多重复读取 3 次。
            for (int retry_time = 0; retry_time < 3; retry_time++) {
                GetLogsRequest req4 = new GetLogsRequest(project, logstore, from, to, topic, query, log_offset,
                    log_line, false);
                res4 = client.GetLogs(req4);
                if (res4 != null && res4.IsCompleted()) {
                    break;
                }
                Thread.sleep(200);
            }
            System.out.println("Read log count:" + String.valueOf(res4.GetCount()));
            log_offset += log_line;
        }

    }

    @Test
    public void dumpHistograms() throws LogException, IOException {

        String query = "* and HSFBizProcessor-DEFAULT-8-thread-289";

        int from = (int)(System.currentTimeMillis() / 1000) - 15 * 60;
        int to = (int)(System.currentTimeMillis() / 1000);

        GetHistogramsResponse histogramsResponse = queryHistograms(query, from, to);

        JSONObject object = new JSONObject();
        object.put("headers", histogramsResponse.GetAllHeaders());
        object.put("histograms", histogramsResponse.GetHistograms());
        object.put("requestId", histogramsResponse.GetRequestId());
        object.put("totalCount", histogramsResponse.GetTotalCount());

        String s = object.toJSONString();
        FileUtils.write(new File("histograms.json"), s, Charsets.UTF_8);
        log.info(s);

    }

    @Test
    public void dumpLogs() throws LogException, IOException {
        String query = "* and HSFBizProcessor-DEFAULT-8-thread-289";

        int from = (int)(System.currentTimeMillis() / 1000) - 15 * 60;
        int to = (int)(System.currentTimeMillis() / 1000);

        GetLogsResponse response = queryLogs(query, from, to, 100, 100);

        String aggQuery = response.getAggQuery();
        Map<String, String> stringStringMap = response.GetAllHeaders();
        int i = response.GetCount();

        long elapsedMilliSecond = response.getElapsedMilliSecond();

        ArrayList<QueriedLog> queriedLogs = response.GetLogs();

        long processedRow = response.getProcessedRow();

        String s1 = response.GetRequestId();

        String whereQuery = response.getWhereQuery();

        JSONObject object = new JSONObject();
        object.put("aggQuery", response.getAggQuery());
        object.put("headers", response.GetAllHeaders());
        object.put("count", response.GetCount());
        object.put("elapsedMilliSecond", response.getElapsedMilliSecond());
        object.put("queriedLogs", response.GetLogs());
        object.put("processedRow", response.getProcessedRow());
        object.put("requestId", response.GetRequestId());
        object.put("whereQuery", response.getWhereQuery());

        String s = object.toJSONString();
        FileUtils.write(new File("logs.json"), s, Charsets.UTF_8);
        log.info(s);
    }

    /**
     * @param query
     * @param from
     * @param to
     * @param offset
     * @param lineCount
     */
    public GetLogsResponse queryLogs(String query, int from, int to, int offset, int lineCount) throws LogException {

        String topic = "";
        GetLogsResponse response = null;

        GetLogsRequest request = new GetLogsRequest(project, logstore, from, to, topic, query, offset,
            lineCount, false);
        response = client.GetLogs(request);

        return response;

    }

    public GetHistogramsResponse queryHistograms(String query, int from, int to) throws LogException {

        String topic = "";
        // 查询日志分布情况
        GetHistogramsResponse histogramsResponse = null;

        GetHistogramsRequest histogramsRequest = new GetHistogramsRequest(project, logstore, topic, query, from,
            to);
        histogramsResponse = client.GetHistograms(histogramsRequest);
        if (histogramsResponse != null && histogramsResponse.IsCompleted()) {
            return histogramsResponse;
        }

        if (histogramsRequest == null) {
            return null;
        }

        if (!histogramsResponse.IsCompleted()) {
            log.info("response in not completed {}", histogramsResponse);
        }

        log.info("Total count of logs is {} ", histogramsResponse.GetHistograms().size());

        return histogramsResponse;

    }

}
