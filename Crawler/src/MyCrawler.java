import com.google.common.io.Files;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;


public class MyCrawler extends WebCrawler 
{
    int size = 0;
    String contentType = "";
    CrawlerProperties props = new CrawlerProperties();
    ArrayList<String> urls = new ArrayList<String>();

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|mp3|zip|gz))$");

    public boolean shouldVisit(Page referringPage, WebURL url) 
    {
        String href = url.getURL().toLowerCase();
        //return !FILTERS.matcher(href).matches() && href.startsWith("http://www.nbcnews.com/");
        return !FILTERS.matcher(href).matches() && href.startsWith("http://abcnews.go.com/");
    }

    public void visit(Page page) 
    {
        String url = page.getWebURL().getURL();
        System.out.println("URL: " + url);
        ParseData parseData = page.getParseData();
        Set<WebURL> links = parseData.getOutgoingUrls();
        int statusCode = page.getStatusCode();
        props.addLinksAndCintentType(page.getContentData().length,page.getContentType());
        
        //Downloading pages
        String extension = "";
        try 
        {
            contentType = page.getContentType();
            TikaConfig config = TikaConfig.getDefaultConfig();
            MimeTypes allTypes = config.getMimeRepository();
            MimeType mimeType = allTypes.forName(contentType.split(";")[0]);
            extension = mimeType.getExtension();
        } 
        catch (MimeTypeException e) 
        {
            extension = "";
        }

        if (extension.equals(".pdf") || extension.equals(".doc") || extension.equals(".docx") 
         || extension.equals(".html") || extension.equals(".htm") || extension.equals(".gif")
         || extension.equals(".png") || extension.equals(".jpg") ||extension.equals(".jpeg")
         ||extension.equals(".tiff"))
        {
            String hashedName = UUID.randomUUID() + extension;
            String filename = "/Users/Lohith/IR-Assignment-2/" + hashedName;
            props.addSuccessfulURL(filename, url, page.getContentData().length, links.size(), page.getContentType(), links);
            try
            {
                Files.write(page.getContentData(), new File(filename));
                logger.info("Stored: {}", url);
            }
            catch (IOException iox)
            {
                logger.error("Failed to write file: " + filename, iox);
            }
        }

        for (WebURL l : links) 
        {
            String sub = l.getSubDomain();
            String dom = l.getDomain();

            //if ( dom.toLowerCase().contains("nbcnews.com"))
            if ( dom.toLowerCase().contains("abcnews") || dom.toLowerCase().contains("go.com"))
            {
            	//System.out.println("DOM Contains true: " +dom.toLowerCase());            	
                props.addOutgoingLinks(l, "OK");
            } 
            else 
            {
            	//System.out.println("DOM Contains false: " +dom.toLowerCase());
            	props.addOutgoingLinks(l, "N_OK");
            }
        }
    }

    protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) 
    {
        props.addURL(webUrl.getURL());
        props.addStatusCode(statusCode);
    }

    @Override
    protected void onPageBiggerThanMaxSize(String urlStr, long pageSize) {
        System.out.println("Big Page");
    }

    @Override
    protected void onContentFetchError(WebURL webUrl) 
    {
        System.out.println("Content Error");
    }

    protected void onUnhandledException(WebURL webUrl, Throwable e) 
    {
        System.out.println("Unhandeled exception "+e);
    }

    public Object getMyLocalData() 
    {
        return props;
    }
}

