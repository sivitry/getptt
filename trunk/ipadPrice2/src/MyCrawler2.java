import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;


public class MyCrawler2 extends WebCrawler {

	public static int count=0; 
	final int maxcount = 200;
	
	
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
            
            if(href.equals("https://www.ptt.cc/bbs/macshop/index1.html")){
            	return false;
            }
            else{            	
            	return !FILTERS.matcher(href).matches() && href.startsWith("https://www.ptt.cc/bbs/macshop");
            }
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
    
    
    private void checkMaxCount(){
    	//--control max read number
    	count++;
    	if(count>maxcount){
    		System.exit(0);
    	}
    }
    

    public void getDetail(String url, String html){
        if(!url.contains("index")){
        	
        	
        	Document document = Jsoup.parse(html);
//        	Elements result = document.select("#main-content");
        	
        	// Get tag value, like title/time
        	Elements tagvalue = document.select(".article-meta-value");

        	// Get Detail Context
        	String context = document.select("#main-content").text();
        	
        	String author	= tagvalue.get(0).text();
        	String board	= tagvalue.get(1).text();
        	String title	= tagvalue.get(2).text();
        	String thedate	= tagvalue.get(3).text();
        	
            // 2014.12.09.Samuel
        	// Need add to parse the following string from context: 
//[物品型號]
//[物品規格] 
//[保固日期]
//[原始發票]：得獎禮物 沒有發票
//[隨機配件]：原廠盒裝配件
//[照片連結]：無 需要的話可私訊提供
//[拍賣連結]：
//[連絡方式]：站內信
//[交易地點]：雙北 桃園
//[交易方式]：面交
//[交易價格]：6800
//[其他備註]：全新得獎的獎品，尚未拆封，但自己已經有了，所以降價出售尋求有緣人囉

        	
//        	int index = 0;
        	author = author.replace(',','_');
        	board = board.replace(',','_');
        	title = title.replace(',','_');
        	thedate = thedate.replace(',','_');
        	context = context.replace(',','_');
        	
        	
        	
        	String str = author+","
        			+board+","
        			+title+","
        			+thedate+","
        			+context+"\n";
        	
/*
        	System.out.println("author, board, title, date, context="
        			+tagvalue.get(0).text()+","
        			+tagvalue.get(1).text()+","
        			+tagvalue.get(2).text()+","
        			+tagvalue.get(3).text()+","
        			+context);
*/        			
        	System.out.println("str=" + str);
        	
        	//	new file name
        	File file = new File(Controller2.filename);
        	file.getParentFile().mkdirs();

        	title = title.toLowerCase();
        	if(title.contains("ipad")){
        		// inc count and decide exit y/n
        		checkMaxCount();
        		
        		savestr(file, str);
        		
        	}else if(title.contains("iphone")){
                // 2014.12.09.Samuel 
            	// write to ptt_iphone.csv
        	}else if(title.contains("mac")){
                // 2014.12.09.Samuel 
            	// write to ptt_mac.csv
        	}else{
                // 2014.12.09.Samuel 
            	// write to ptt_other.csv
        	}
        }
    }
    
    
	private static void savestr(File file, String str){		
		try{
			if (!file.exists()) {
				file.createNewFile();
			}
			//use FileWriter to write file
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write(str);
			bw.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
    
    
   
}
