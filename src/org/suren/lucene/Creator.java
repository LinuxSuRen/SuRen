/**
 * Create Date: 2011-8-18<br>
 * File Name: Creator.java
 */
package org.suren.lucene;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.suren.SuRenProperties;
import org.suren.main.StartUp;
import org.suren.util.io.FileUtil;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * @author SuRen<br>
 *         Create Time: 11:08:56<br>
 */
public class Creator
{
	public static String	absPath	= "path";
	public static String	content	= "contents";

	/**
	 * @param dataFiles
	 * @return 如果返回-1，dataFiles为null，正常应该返回创建索引所花的时间
	 * @throws CreatorException
	 *             等配置文件对象为null时抛出异常
	 * @throws IOException
	 */
	public long create(List<File> dataFiles) throws CreatorException, IOException
	{
		long start = System.currentTimeMillis();

		SuRenProperties properties = StartUp.properties;

		if (properties == null) throw new CreatorException();

		String index = properties.getLucene().getIndex();

		File indexDir = new File(index);
		Directory dir = new SimpleFSDirectory(indexDir);
		IndexWriter writer = new IndexWriter(dir, new IKAnalyzer(), false, MaxFieldLength.LIMITED);

		for (File file : dataFiles)
		{
			Document doc = new Document();

			doc.add(new Field(absPath, file.getAbsolutePath(), Field.Store.YES,
					Field.Index.ANALYZED));
			doc.add(new Field(content, FileUtil.allContent(file), Field.Store.YES,
					Field.Index.ANALYZED));

			writer.addDocument(doc);
		}

		writer.optimize();
		writer.close();

		return System.currentTimeMillis() - start;
	}

	public long createForAll() throws CreatorException, IOException
	{
		long start = System.currentTimeMillis();
		File[] root = File.listRoots();

		if (root != null)
		{
			for (File dir : root)
			{
				List<File> files = FileUtil.allFile(dir, FileUtil.TEXT);

				create(files);
			}
		}

		return System.currentTimeMillis() - start;
	}
	
	public void createFor(File dir) throws CreatorException, IOException
	{
		create(FileUtil.allFile(dir, FileUtil.TEXT));
	}

}
