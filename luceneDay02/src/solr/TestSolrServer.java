package solr;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrServer {
	@Test
	public void TestSolr() throws Exception{
		SolrServer solrServer=new HttpSolrServer("http://localhost:8080/solr/collection1");
		SolrInputDocument document=new SolrInputDocument();
		document.addField("id", "5");
		document.addField("title", "使用solrj添加的文档");
		document.addField("content", "文档的内容");
		solrServer.add(document);
		solrServer.commit();
	}
	@Test
	public void delete()throws Exception{
		SolrServer solrServer=new HttpSolrServer("http://localhost:8080/solr/collection1");
		solrServer.deleteById("5");
		solrServer.commit();
	}
	@Test
	public void deleteByQuery()throws Exception{
		SolrServer solrServer=new HttpSolrServer("http://localhost:8080/solr/collection1");
		solrServer.deleteByQuery("*:*");
		solrServer.commit();
	}
	@Test
	public void searchByQuery() throws Exception{
		SolrServer solrServer=new HttpSolrServer("http://localhost:8080/solr/collection1");
		SolrQuery query=new SolrQuery();
		//query.setQuery("花儿");
		query.set("q", "*:*");
		QueryResponse queryResponse = solrServer.query(query);
		SolrDocumentList results = queryResponse.getResults();
		System.out.println(results.getNumFound());
		for (SolrDocument solrDocument : results) {
			
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("product_catalog_name"));
			System.out.println(solrDocument.get("product_price"));
			System.out.println(solrDocument.get("product_name"));
			System.out.println(solrDocument.get("product_picture"));
		}
	}
	@Test
	public void fuza()throws Exception{
		SolrServer solrServer=new HttpSolrServer("http://localhost:8080/solr/collection1");
		SolrQuery query=new SolrQuery();
		query.setQuery("花儿");
		query.addFilterQuery("product_price:[0 TO 50]");
		query.setSort("product_price", ORDER.asc);
		query.setStart(0);
		query.setRows(10);
		query.setFields("id","product_catalog_name","product_price","product_name","product_picture");
		query.set("df","product_keywords");
		query.setHighlight(true);
		query.addHighlightField("product_name");
		query.setHighlightSimplePre("<em>");
		query.setHighlightSimplePost("</em>");
		QueryResponse queryResponse = solrServer.query(query);
		SolrDocumentList results = queryResponse.getResults();
		System.out.println(results.getNumFound());
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		for (SolrDocument solrDocument : results) {
			List<String> list = highlighting.get(solrDocument.get("id")).get("product_name");
			String name="";
			if(list!=null&&list.size()>0){
				name=list.get(0);
			}else{
				name=solrDocument.get("product_name").toString();
			}
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("product_catalog_name"));
			System.out.println(solrDocument.get("product_price"));
			System.out.println(name);
			System.out.println(solrDocument.get("product_picture"));
		}
		
	}
}
