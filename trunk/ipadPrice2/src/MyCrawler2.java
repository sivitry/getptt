import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;


public class MyCrawler2 extends WebCrawler {

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" 
                                                      + "|png|tiff?|mid|mp2|mp3|mp4"
                                                      + "|wav|avi|mov|mpeg|ram|m4v|pdf" 
                                                      + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    /**
     * You should implement this function to specify whether
     * the given url should be crawled or not (based on your
     * crawling logic).
     */
    @Override
    public boolean shouldVisit(WebURL url) {
            String href = url.getURL().toLowerCase();
            System.out.println(href);
//            return !FILTERS.matcher(href).matches() && href.startsWith("http://www.ipeen.com.tw/");
//            return !FILTERS.matcher(href).matches() && href.startsWith("https://www.ptt.cc/bbs/macShop");
            return !FILTERS.matcher(href).matches() && href.startsWith("https://www.ptt.cc/bbs/macshop/");
            
            
//            return !FILTERS.matcher(href).matches() && href.startsWith("https://tw.news.yahoo.com/");
            //return !FILTERS.matcher(href).matches() && href.startsWith("/bbs/MacShop/");
    }

    /**
     * This function is called when a page is fetched and ready 
     * to be processed by your program.
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
                    
                    getDetail(url, html);
                    
            }
    }
    
    

    public void getDetail(String url, String html){
        if(!url.contains("index")){
        	Document document = Jsoup.parse(html);
//        	Elements result = document.select("#main-content");
        	
        	// Get tag value, like title/time
        	Elements tagvalue = document.select(".article-meta-value");
        	System.out.println("tagvalue="+tagvalue);
        	
        	// Get Detail Context
        	String context = document.select("#main-content").text();
        	System.out.println("context="+context);
        	
            // 2014.12.07.Samuel 
        	// Need Write to file with csv type
        }
    }
   
}
