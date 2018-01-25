package IndexManager;

import java.io.File;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class IndexManager {
	public IndexWriter indexWriter;
	@Before
	public void beforee()throws Exception{
	//	1、创建一个Directory对象，指定索引库的位置
		Directory directory=FSDirectory.open(new File("D:/developments/temp/index"));
//		2、创建一个IndexWriter对象。以写的方式打开索引库。
		Analyzer analyzer=new IKAnalyzer();	
		IndexWriterConfig conf=new IndexWriterConfig(Version.LATEST, analyzer);
		indexWriter=new IndexWriter(directory,conf);
	}
	@Test
	public void addDocument()throws Exception{
	
//		3、创建一个Document对象。
		Document document=new Document();
//		4、向Document对象中添加域。
		Field field=new TextField("name","测试文档spring册ksldfjajdskafksagdskjf",Store.YES);
		field.setBoost(100);
		document.add(field);
		document.add(new TextField("content","测试文档的内容",Store.YES));
		document.add(new StoredField("path","d:/temp"));
		document.add(new StringField("haha","测试文档",Store.YES));
		document.add(new TextField("hahahaha","测试文档",Store.YES));
		document.add(new LongField("size",100,Store.YES));
//		5、把文档对象写入索引库
		indexWriter.addDocument(document);
	}
	@After
	public void afteri() throws Exception{
		
//		6、提交
		indexWriter.commit();
//		7、关闭IndexWriter对象。
		indexWriter.close();
	}
	@Test
	public void delete()throws Exception{
		indexWriter.deleteAll();
	}
	@Test
	public void deletebyQuery()throws Exception{
		Query query=new TermQuery(new Term("name","apache"));
		indexWriter.deleteDocuments(query);
	}
	@Test
	public void updateDocument()throws Exception{
		Document document=new Document();
		document.add(new TextField("haha","更新文档",Store.YES));
		indexWriter.updateDocument(new Term("content","spring"), document);
	}
}
