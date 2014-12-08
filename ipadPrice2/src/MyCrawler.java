import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {
	private final static Pattern FILTERS = Pattern
			.compile(".*(\\.(css|js|bmp|gif|jpe?g"
					+ "|png|tiff?|mid|mp2|mp3|mp4"
					+ "|wav|avi|mov|mpeg|ram|m4v|pdf"
					+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	/**
	 * You should implement this function to specify whether the given url
	 * should be crawled or not (based on your crawling logic).
	 */
	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();
//		return !FILTERS.matcher(href).matches() && href.startsWith("http://www.timliao.com/");
		//return !FILTERS.matcher(href).matches() && href.startsWith("http://www.ipeen.com.tw/");
		return !FILTERS.matcher(href).matches() && href.startsWith("https://www.ptt.cc/bbs/MacShop/");
		
	}

	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program.
	 */
	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		System.out.println("URL: " + url);

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
			List<WebURL> links = htmlParseData.getOutgoingUrls();

			System.out.println("Text length: " + text.length());
			System.out.println("Html length: " + html.length());
			System.out.println("Number of outgoing links: " + links.size());
			download(url, html);
		}
	}

	
	private static void download(String url, String html){
		String filename = urlencoding(url);
		File file = new File("./data/"+filename);
		file.getParentFile().mkdirs();
		try{
			if (!file.exists()) {
				file.createNewFile();
			}
			//use FileWriter to write file
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			String line;
	//		while ((line = br.readLine()) != null) {	
	//			bw.write(line);
	//		}
			bw.write(html);
			bw.close();
	//		br.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static String urlencoding(String str){
		String tmp = new String();
		try {
			tmp = java.net.URLEncoder.encode(str, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tmp;
	}
}
