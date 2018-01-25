package IndexManager;

import java.io.File;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class searchIndex {
	private IndexSearcher indexSearcher;
	private IndexReader indexReader;
	@Before
	public void Before()throws Exception{
		Directory directory=FSDirectory.open(new File("D:/developments/temp/index"));
		indexReader=DirectoryReader.open(directory);
		indexSearcher=new IndexSearcher(indexReader);
	}
	public void exeQuery(Query query)throws Exception{
		TopDocs topDocs = indexSearcher.search(query, 10);
		System.out.println("查询结果的总记录数："+topDocs.totalHits);
		for(ScoreDoc scoreDoc : topDocs.scoreDocs){
			Document document=indexSearcher.doc(scoreDoc.doc);
			System.out.println(document.get("name"));
			System.out.println(document.get("path"));
			System.out.println(document.get("size"));
			System.out.println(document.get("content"));
		}
	}
	@Test
	public void testMatchAllDocsQuery() throws Exception{
		
		Query query=new MatchAllDocsQuery();
		exeQuery(query);
		
	}
	@Test
	public void NumericRangeQuerya()throws Exception{
		Query query=NumericRangeQuery.newLongRange("size", 1l, 9999l, true, false);
		exeQuery(query);
	
	}
	@After
	public void after()throws Exception{
		indexReader.close();
		
	}
	
	@Test
	public void booleanQueryk() throws Exception{
		BooleanQuery query=new BooleanQuery();
		Query query1=new TermQuery(new Term("name","spring"));
		Query query2=new TermQuery(new Term("content","spring"));
		query.add(query1,Occur.SHOULD);
		query.add(query2,Occur.SHOULD);
		System.out.println(query);
		exeQuery(query);
	}
	@Test
	public void testQueryParser()throws Exception{
		QueryParser parser=new QueryParser("content",new IKAnalyzer());
		//Query query=parser.parse("lucene is a apache project");
		Query query=parser.parse("size:{0 TO 1000}");
		System.out.println(query);
		exeQuery(query);
	}
	@Test
	public void testMultiFieldQueryParser()throws Exception{
		String[] fields={"name","content"};
		MultiFieldQueryParser quertParser=new MultiFieldQueryParser(fields,new IKAnalyzer());
		Query query=quertParser.parse("spring");
		System.out.println(query);
		//name:spring content:spring
		exeQuery(query);
	}
	@Test
	public void  testt() throws Exception{
		QueryParser queryParser=new QueryParser("name",new IKAnalyzer());
		Query query=queryParser.parse("name:spring");
		exeQuery(query);
	}
	
}
