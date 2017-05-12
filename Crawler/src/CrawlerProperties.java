import edu.uci.ics.crawler4j.url.WebURL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;

public class CrawlerProperties
{
    ArrayList<String> urls = new ArrayList<String>();
    ArrayList<Integer> statusCodes = new ArrayList<Integer>();

    ArrayList<String> successfulURLs = new ArrayList<String>();
    ArrayList<Integer> sizes = new ArrayList<Integer>();
    ArrayList<Integer> numberOfOutlinks = new ArrayList<Integer>();
    ArrayList<String> contentTypes = new ArrayList<String>();
    ArrayList<Set<WebURL>> links = new ArrayList<Set<WebURL>>();

    ArrayList<WebURL> ourls = new ArrayList<WebURL>();
    ArrayList<String> types = new ArrayList<String>();

    ArrayList<Integer> sizesStats = new ArrayList<Integer>();
    ArrayList<String> contentTypesStats = new ArrayList<String>();

    ArrayList<String> filenames = new ArrayList<String>();

    public void addURL(String url)
    {
        urls.add(url);
    }
    
    public void addStatusCode(int statusCode)
    {
        statusCodes.add(statusCode);
    }

    public void addSuccessfulURL(String filename, String surl, int size, int outlinks, String contentType,Set<WebURL> olinks)
    {
        filenames.add(filename);
        successfulURLs.add(surl);
        sizes.add(size);
        numberOfOutlinks.add(outlinks);
        contentTypes.add(contentType);
        links.add(olinks);
    }

    public void addLinksAndCintentType(int size, String contentType)
    {
        sizesStats.add(size);
        contentTypesStats.add(contentType);
    }

    public void addOutgoingLinks(WebURL ourl, String type)
    {
        ourls.add(ourl);
        types.add(type);
    }
}
