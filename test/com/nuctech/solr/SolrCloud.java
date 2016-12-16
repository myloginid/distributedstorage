package com.nuctech.solr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

public class SolrCloud {      
    private static CloudSolrServer cloudSolrServer;  
      
    private  static synchronized CloudSolrServer getCloudSolrServer(final String zkHost) {  
        if(cloudSolrServer == null) {  
            try {  
                cloudSolrServer = new CloudSolrServer(zkHost);  
            }catch(Exception e) {  
                e.printStackTrace();                  
            }  
        }  
          
        return cloudSolrServer;  
    }  
      
    private void addIndex(SolrServer solrServer) {        
        try {  
            SolrInputDocument doc1 = new SolrInputDocument();  
            doc1.addField("id", "421245251215121452521251");  
            doc1.addField("area_s", "北京");  
            SolrInputDocument doc2 = new SolrInputDocument();  
            doc2.addField("id", "4224558524254245848524243");  
            doc2.addField("area_s", "上海");  
              
            SolrInputDocument doc3 = new SolrInputDocument();  
            doc3.addField("id", "4543543458643541324153453");  
            doc3.addField("area_s", "重庆");  
              
            Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();  
            docs.add(doc1);  
            docs.add(doc2);  
            docs.add(doc3);  
    
            solrServer.add(docs);             
            solrServer.commit();  
              
        }catch(SolrServerException e) {  
            System.out.println("Add docs Exception !!!");  
            e.printStackTrace();          
        }catch(IOException e){  
            e.printStackTrace();  
        }catch (Exception e) {  
            System.out.println("Unknowned Exception!!!!!");  
            e.printStackTrace();  
        }         
          
    }  
      
      
    public void search(SolrServer solrServer, String String) {        
        SolrQuery query = new SolrQuery();  
        query.setQuery("*:*");  
        query.set("shards.tolerant", true);
        try {  
            QueryResponse response = solrServer.query(query);  
            SolrDocumentList docs = response.getResults();  
  
            System.out.println("文档个数：" + docs.getNumFound());  
            System.out.println("查询时间：" + response.getQTime());  
  
            for (SolrDocument doc : docs) {  
                String area = (String) doc.getFieldValue("area_s");  
                String id = (String) doc.getFieldValue("id");  
                System.out.println("id: " + id);  
                System.out.println("area: " + area);  
                System.out.println();  
            }  
        } catch (SolrServerException e) {  
            e.printStackTrace();  
        } catch(Exception e) {  
            System.out.println("Unknowned Exception!!!!");  
            e.printStackTrace();  
        }  
    }  
      
    public void deleteAllIndex(SolrServer solrServer) {  
        try {  
            solrServer.deleteByQuery("*:*");// delete everything!  
            solrServer.commit();  
        }catch(SolrServerException e){  
            e.printStackTrace();  
        }catch(IOException e) {  
            e.printStackTrace();  
        }catch(Exception e) {  
            System.out.println("Unknowned Exception !!!!");  
            e.printStackTrace();  
        }  
    }  
      
    /** 
     * @param args 
     */  
    public static void main(String[] args) {    
          final String zkHost = "192.168.25.225:2181";       
          final String  defaultCollection = "core0";  
          final int  zkClientTimeout = 20000;  
          final int zkConnectTimeout = 1000;  
            
          CloudSolrServer cloudSolrServer = getCloudSolrServer(zkHost);         
          System.out.println("The Cloud SolrServer Instance has benn created!");            
          cloudSolrServer.setDefaultCollection(defaultCollection);  
          cloudSolrServer.setZkClientTimeout(zkClientTimeout);  
          cloudSolrServer.setZkConnectTimeout(zkConnectTimeout);                   
          //cloudSolrServer.connect();  
          System.out.println("The cloud Server has been connected !!!!");            
          //测试实例！  
          SolrCloud test = new SolrCloud();         
  //        System.out.println("测试添加index！！！");       
          //添加index  
          test.addIndex(cloudSolrServer);  
            
  //        System.out.println("测试查询query！！！！");  
  //        test.search(cloudSolrServer, "id:*");  
  //          
  //        System.out.println("测试删除！！！！");  
  //        test.deleteAllIndex(cloudSolrServer);  
  //        System.out.println("删除所有文档后的查询结果：");  
         // test.search(cloudSolrServer, "zhan");      
  //        System.out.println("hashCode"+test.hashCode());
                    
           // release the resource   
          cloudSolrServer.shutdown();  
   
    }  
  
}
