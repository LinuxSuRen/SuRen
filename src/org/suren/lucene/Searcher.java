/**
 * Create Date: 2011-8-18<br>
 * File Name: Searcher.java
 */
package org.suren.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.suren.SuRenProperties;
import org.suren.core.model.Document;
import org.suren.main.StartUp;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * @author SuRen<br>
 *         Create Time: 11:08:25<br>
 */
public class Searcher
{
	public List<Document> search(String key) throws CreatorException, CorruptIndexException, IOException,
			ParseException
	{
		List<Document> docs = new ArrayList<Document>();
		SuRenProperties properties = StartUp.properties;

		if (properties == null) throw new CreatorException();

		IndexSearcher searcher = new IndexSearcher(FSDirectory.open(new File(properties.getLucene()
				.getIndex())));

		QueryParser parser = new QueryParser(Version.LUCENE_30, "contents", new IKAnalyzer());

		Query query = parser.parse(key);

		TopDocs topDocs = searcher.search(query, searcher.maxDoc());

		int total = topDocs != null ? topDocs.totalHits : 0;

		for (int i = 0; i < total; i++)
		{
			org.apache.lucene.document.Document tempDoc = searcher.doc(topDocs.scoreDocs[i].doc);

			Document doc = new Document();
			
			doc.setPath(tempDoc.getField("path").stringValue());
			
			docs.add(doc);
		}
		
		return docs;
	}

}
