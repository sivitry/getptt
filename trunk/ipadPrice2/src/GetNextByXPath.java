import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class GetNextByXPath {
	
	public String getNextURL(String myhtml){

		 
		//read an xml node using xpath
//		Node node = (Node) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODE);
		
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			Document document = Jsoup.parse(myhtml);
//			String result = new String();
			
			//XPath = //*[@id="action-bar-container"]/div/div[2]/a[2]
			String result = document.select("#action-bar-container > div > div:eq(1) > a:eq(1)").attr("href");
			System.out.println("result=" + result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();  
		}
		return null;
	}
	
}
