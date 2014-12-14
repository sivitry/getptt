import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
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
	final int maxcount = 6553600;
	
	
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
    		System.out.println("reach maxcount and system exit, count="+count);
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
//[���~����]
//[���~�W��] 
//[�O�T���]
//[��l�o��]�G�o��§�� �S���o��
//[�H���t��]�G��t���˰t��
//[�Ӥ�s��]�G�L �ݭn���ܥi�p�T����
//[���s��]�G
//[�s���覡]�G�����H
//[����a�I]�G��_ ���
//[����覡]�G����
//[������]�G6800
//[��L�Ƶ�]�G���s�o���~�A�|����ʡA��ۤv�w�g���F�A�ҥH����X��M�D���t�H�o

        	
        	//-- replace ',' to '_'
        	author = author.replace(',','_');
        	board = board.replace(',','_');
        	title = title.replace(',','_');
        	thedate = thedate.replace(',','_');
        	context = context.replace(',','_');
        	
//        	byte[] big5 = null;
        	//-- big5 to utf8
//        	try {
//        		context = context.getBytes("Big5");        		
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
        	
        	//-- igore several first words
//        	context = context.substring(context.indexOf("[物品型號]")+6);
        	
        	//-- cut string before "QQXX", cut string after "-- ※"
        	int start = context.indexOf("[物品型號]");
        	int end = context.indexOf("-- ※");
        	if(start!=-1 && end!=-1){
        		context = context.substring(start,end);
        	}else if(start!=-1){
        		context = context.substring(start, context.length());
        	}else{
        		context = context.substring(0, end);
        	}
        	
        	//-- find index
//        	String[] stray = new String[20];
//        	for(String s : stray){
//        		s = " ";
//        	}
        	
        	//-- remove ':' and '：'
        	context = context.replace(":", " ");
        	context = context.replace("：", " ");
        	
        	String[] text = new String[20];
        	int[] index;
        	index = new int[20];
        	for(int i=0; i<index.length; i++){
        		index[i] = -1;
        		text[i]="@";
        	}
        	index[0] = context.indexOf("[物品型號]");
        	index[1] = context.indexOf("[物品規格]");
        	index[2] = context.indexOf("[保固日期]");
        	index[3] = context.indexOf("[原始發票]");
        	index[4] = context.indexOf("[隨機配件]");
        	index[5] = context.indexOf("[照片連結]");
        	index[6] = context.indexOf("[拍賣連結]");
        	index[7] = context.indexOf("[連絡方式]");
        	index[8] = context.indexOf("[交易地點]");
        	index[9] = context.indexOf("[交易方式]");
        	index[10] = context.indexOf("[交易價格]");
        	index[11] = context.indexOf("[其他備註]");

        	int offset = 6;
        	for(int i=0; i<12; i++){
        		if(i==11){
        			text[i] = context.substring(index[i]+offset, context.length());
        			continue;
        		}else if(index[i]!=-1 && index[i+1]!=-1){
        			text[i] = context.substring(index[i]+offset, index[i+1]);
        		}else if(index[i]==-1){		//-- self prefix not found
        			text[i]="X";
        			continue;
        		}else if(index[i+1]==-1){	//-- find next non -1 index
        			for(int j=i+1; j<12; j++){
        				if(index[j]!=-1){
        					text[i] = context.substring(index[i]+offset, index[j]);
        					break;
        				}
        				if(j==11){
        					text[i] = context.substring(index[i]+offset, context.length());
            			}
        			}
        		}
        	}
        	
        	for(int i=0; i<12; i++){
        		System.out.println(i+"="+index[i]+" "+text[i]);
        	}        	
        	
        	//-- price field string remove '_' char
        	text[10].replace("_","");
        	
        	
/*        	String str = author+","
        			+board+","
        			+title+","
        			+thedate+","
        			+context+"\n";*/

        	String str = author+",";
        	str = str.concat(board+",");
        	str = str.concat(title+",");
        	str = str.concat(thedate+",");
        	str = str.concat(url+",");
        	for(int i=0; i<12; i++){
        		if(i==11){
        			str = str.concat(text[i]+"\n");
        			break;
        		}
        		str = str.concat(text[i]+",");	
        	}
        	
        	//-- default word
//        	str = str.replace("[物品型號]：", " ").replace("[物品規格]：", ",").replace("[保固日期]：", ",").replace("[原始發票]：", ",");
//        	str = str.replace("[隨機配件]：", ",").replace("[照片連結]：", ",").replace("[拍賣連結]：", ",").replace("[連絡方式]：", ",");
//        	str = str.replace("[交易地點]：", ",").replace("[交易方式]：", ",").replace("[交易價格]：", ",").replace("[其他備註]：", ",");
        	
        	//-- small :
//        	str = str.replace("[物品型號]:", " ").replace("[物品規格]:", ",").replace("[保固日期]:", ",").replace("[原始發票]:", ",");
//        	str = str.replace("[隨機配件]:", ",").replace("[照片連結]:", ",").replace("[拍賣連結]:", ",").replace("[連絡方式]:", ",");
//        	str = str.replace("[交易地點]:", ",").replace("[交易方式]:", ",").replace("[交易價格]:", ",").replace("[其他備註]:", ",");
        	
        	//-- custom word
//        	str = str.replace("[希望價格]：", ",").replace("[備註事項]：", ",").replace("[保固狀態]：", ",").replace("[我想要買]：", ",");
        	
//        	System.out.println("str=" + str);
        	
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
				
				//-- write header
				String header = "作者,哪版, 標題, 日期, [物品型號],[物品規格],[保固日期],[原始發票],[隨機配件],[照片連結],[拍賣連結],[連絡方式],[交易地點],[交易方式],[交易價格],[其他備註]\n";
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "big5"));
				writer.append(header);
				writer.close();
			}
			//use FileWriter to write file
//			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
//			BufferedWriter bw = new BufferedWriter(fw);
//			bw.write(str);
//			bw.close();

			// 2014.12.10 Samuel, 設定為BIG5格式
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "big5"));
			writer.append(str);
			writer.close();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
    
    
   
}
