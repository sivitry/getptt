import org.apache.http.HttpStatus;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;

public class Controller2 {
	public static void main(String[] args) throws Exception {
		String crawlStorageFolder = "C:\\data\\crawl\\root";
		int numberOfCrawlers = 1;

		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);		
		config.setIncludeHttpsPages(true);
		
		
		config.setPolitenessDelay(1000);
		config.setMaxDepthOfCrawling(5);
//		config.setMaxPagesToFetch(100);
//		config.setIncludeBinaryContentInCrawling(false);
//		config.setResumableCrawling(false);

		
		/*
		 * Instantiate the controller for this crawl.
		 */
//		PageFetcher pageFetcher = new PageFetcher(config);
		PageFetcher pageFetcher = new MyFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig,
				pageFetcher);

		CrawlController controller = new CrawlController(config, pageFetcher,
				robotstxtServer);

		/*
		 * For each crawl, you need to add some seed urls. These are the first
		 * URLs that are fetched and then the crawler starts following links
		 * which are found in these pages
		 */
		controller.addSeed("https://www.ptt.cc/bbs/MacShop/index.html");
//		controller.addSeed("https://www.ptt.cc/bbs/MacShop/index3091.html");
//		controller.addSeed("https://www.ptt.cc/bbs/MacShop/index3092.html");
//		controller.addSeed("https://tw.news.yahoo.com/index.html");
//		controller.addSeed("http://www.ipeen.com.tw/");

		/*
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * will reach the line after this only when crawling is finished.
		 */
		controller.start(MyCrawler2.class, numberOfCrawlers);
	}
	/*
	 private Page download(String url){
   	  WebURL curURL=new WebURL();
   	  curURL.setURL(url);
   	  PageFetchResult fetchResult=null;
   	  try {
   	    fetchResult=pageFetcher.fetchHeader(curURL);
   	    if (fetchResult.getStatusCode() == HttpStatus.SC_OK) {
   	      try {
   	        Page page=new Page(curURL);
   	        fetchResult.fetchContent(page);
   	        if (parser.parse(page,curURL.getURL())) {
   	          return page;
   	        }
   	      }
   	 catch (      Exception e) {
   	        e.printStackTrace();
   	      }
   	    }
   	  }
   	  finally {
   	    fetchResult.discardContentIfNotConsumed();
   	  }
   	  return null;
   	}
	*/
}
