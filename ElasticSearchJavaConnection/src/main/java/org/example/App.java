package org.example;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.elasticsearch.xcontent.XContentType;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws UnknownHostException {

        // APPLYING CRUD (create, read, update, and delete)
        // OPERATIONS TO ELASTIC SEARCH
        Settings settings = Settings.builder()
                .put("cluster.name", "elasticsearch").build();

        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));

// on shutdown

       List<DiscoveryNode> discoveryNodes = client.listedNodes();
       for(DiscoveryNode node: discoveryNodes) {
           System.out.println(node);
       }

       Map<String,Object> json = new HashMap<>();

       json.put("name","Asus Vivobook X542UR");
       json.put("detail","Intel Core İ5, 8GB RAM, 1TB SSD");
       json.put("price","3500");
       json.put("provider","Asus Türkiye");

       IndexResponse response = client.prepareIndex("product","_doc","3").setSource(json, XContentType.JSON).get();
       System.out.println(response.getId()); //printing id of the index response.
        Map<String,Object> json2 = new HashMap<>();
        json2.put("name","Apple Macbook Pro");
        json2.put("detail","Intel Core İ9, 32GB RAM, 2TB SSD");
        json2.put("price","15000");
        json2.put("provider","Apple Türkiye");

        IndexResponse res = client.prepareIndex("product","_doc","4").setSource(json2,XContentType.JSON).get();
        System.out.println(res.getId());

        GetResponse getResponse = client.prepareGet("product","_doc","3").get();

        Map<String,Object>map = getResponse.getSource();

        String name = (String) map.get("name");
        String price = (String) map.get("price");
        String detail = (String) map.get("detail");
        String provider = (String) map.get("provider");


        System.out.println("Name: "+name);
        System.out.println("Price: "+price);
        System.out.println("Detail: "+detail);
        System.out.println("Provider: "+provider);

        SearchResponse r = client.prepareSearch("product").setTypes("_doc").setQuery(QueryBuilders.matchQuery("name","Apple")).get();

        SearchHit[] hits = r.getHits().getHits();

        for(SearchHit hit: hits) {
            Map<String,Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println(sourceAsMap);
        }
        System.out.println("-------");


        DeleteResponse dr = client.prepareDelete("product","_doc","4").get();
        System.out.println(dr.getId());
        System.out.println(dr.getIndex());

        DeleteResponse resp = client.prepareDelete("product", "_doc", "1").get();
        System.out.println(resp.getIndex());
        System.out.println(resp.getId());





    }
}
