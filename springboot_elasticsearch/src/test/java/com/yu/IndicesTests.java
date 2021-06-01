package com.yu;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

/**
 * es索引操作
 */
@SpringBootTest
public class IndicesTests {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    //创建索引
    @Test
    void createIndices() throws IOException {
        //创建索引Create请求
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("java_indices");
        //客户端执行请求
        CreateIndexResponse response = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        System.out.println(response);
    }

    //获取索引，判断索引是否存在
    @Test
    void indicesIsExists() throws IOException {
        //创建索引Get请求
        GetIndexRequest request = new GetIndexRequest("java_indices");
        boolean exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    //删除索引
    @Test
    void deleteIndices() throws IOException {
        //创建索引Delete请求
        DeleteIndexRequest request = new DeleteIndexRequest("java_indices");
        AcknowledgedResponse response = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        System.out.println(response.isAcknowledged());
    }
}
