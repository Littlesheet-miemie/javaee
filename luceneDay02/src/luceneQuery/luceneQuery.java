package luceneQuery;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class luceneQuery {
	@Test
	public void lucene() throws Exception{
	//3）创建一个Directory对象指定索引库保存的路径。
	//	Directory directory=new RAMDirectory();
	//可以是内存也可以是磁盘。
		Directory directory=FSDirectory.open(new File("D:/developments/temp/index"));
	//4）需要以写的方式打开索引库创建一个IndexWriter对象。
		Analyzer analyzer=new StandardAnalyzer();
		IndexWriterConfig conf=new IndexWriterConfig(Version.LATEST, analyzer);
		IndexWriter indexWriter=new IndexWriter(directory, conf);
	//两个参数：
	//IndexWriterConfig
	//两个参数：
	//Version对象
	//Analyzer对象，使用标准分析器对象
	//Directory对象
		File sourcePath=new File("D:/黑马70期/mybatis--苏丙伦老师/mybatis02/lucene&solr/00.参考资料/searchsource");
		File[] listFiles = sourcePath.listFiles();
		for (File file : listFiles) {
			//5）读取文件内容
//			文件名
			String fileName = file.getName();
//			文件大小
			long fileSize = FileUtils.sizeOf(file);
//			文件內容
			String fileContent = FileUtils.readFileToString(file);
//			文件路径
			String filePath = file.getPath();
			//6）为每个文件创建一个Document对象
			Document document=new Document();
			//7）向文档对象中添加域。
			Field fieldName=new TextField("name",fileName,Store.YES);
			Field fieldSize=new TextField("size",fileSize+"",Store.YES);
			Field fieldContent=new TextField("content",fileContent,Store.YES);
			Field fieldPath=new TextField("path",filePath,Store.YES);
			document.add(fieldPath);
			document.add(fieldContent);
			document.add(fieldSize);
			document.add(fieldName);
			//8）把Document对象写入索引库
			indexWriter.addDocument(document);
		}
		//9）关闭IndexWriter。
		indexWriter.commit();
		indexWriter.close();
	}
	@Test
	public void luceneQuery() throws Exception{
//		1）创建一个Directory对象。
		Directory directory=FSDirectory.open(new File("D:/developments/temp/index"));
//		2）创建一个IndexReader对象，以读的方式打开索引库
		IndexReader indexReader=DirectoryReader.open(directory);
//		3）创建一个IndexSearcher对象，需要IndexReader对象。
		IndexSearcher searcher=new IndexSearcher(indexReader);
//		4）创建一个Query对象。需要指定要搜索的域及要搜索的关键词。
		Query query=new TermQuery(new Term("name","全"));
//		5）执行查询。可以得到一个TopDocs对象。
		TopDocs topDocs = searcher.search(query, 10);
//		6）取查询结果的总记录数。
		System.out.println("查詢結果总记录数："+topDocs.totalHits);
//		7）遍历查询结果。包含文档的id列表。
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		for (ScoreDoc scoreDoc : scoreDocs) {
			int docId = scoreDoc.doc;
			Document document = searcher.doc(docId);
			System.out.println(document.get("name"));
			System.out.println(document.get("path"));
			System.out.println(document.get("size"));
			System.out.println(document.get("content"));
			
		}
//		8）根据id取文档对象
//		9）从document对象中取field的值，并打印。
//		10）关闭IndexReader对象。
		indexReader.close();
	}
	@Test
	public void luceneAnalyst()throws Exception{
//		1）创建一个分析器对象
		//Analyzer analyzer=new StandardAnalyzer();
//		Analyzer analyzer=new CJKAnalyzer();
		//Analyzer analyzer=new SmartChineseAnalyzer();
		Analyzer analyzer=new IKAnalyzer();
//		2）使用Analyzer对象的TokenStream方法获得一个TokenStream对象，其中参数指定要分析的文本内容。
		TokenStream tokenStream = analyzer.tokenStream("name", "有一个梦想就是哈哈哈哈apple banana pear支付宝");
//		3）遍历之前先调用reset方法。
		tokenStream.reset();
//		4）设置一个引用，相当于指针。指向列表中的当前单词。
		CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
//		5）遍历tokenStream对象中的内容。使用while循环变量。
		while(tokenStream.incrementToken()){
//		6）引用对应的内容，把当前单词打印出来。
			System.out.println(charTermAttribute);
		}
//		7）关闭流
		tokenStream.close();
	}
}
