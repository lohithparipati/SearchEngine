package edgeGenerator;
import java.io.*;
import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class edgeGenerator {
	
	public static void main(String[] args) throws ParseException, IOException {


        BufferedReader br = new BufferedReader(new FileReader("/Users/Lohith/Documents/NBCNewsData/mapNBCNewsDataFile.csv"));
        String line = null;
        HashMap<String, String> fileUrlMap = new HashMap<String, String>();
        HashMap<String, String> urlFileMap = new HashMap<String, String>();
        while ((line = br.readLine()) != null) {
            String str[] = line.split(",");
            fileUrlMap.put(str[0], str[1]);
            urlFileMap.put(str[1], str[0]);
        }

        File dir = new File("/Users/Lohith/Documents/NBCNewsData/NBCNewsDownloadData/");
        System.out.println("no.of files in dir:" + dir.listFiles().length);
        Set<String> edges = new HashSet<String>();
        for (File file : dir.listFiles()) 
        {	
        	Document doc = Jsoup.parse(file, "UTF-8", fileUrlMap.get(file.getName()));
            Elements links = doc.select("a[href]");
            System.out.println("file:"+file.getName() + " has" + links.size() + " links");
            for (Element link : links) {
                String url = link.attr("abs:href").trim();
                if (urlFileMap.containsKey(url)) {
                    edges.add("/Users/Lohith/Documents/NBCNewsData/NBCNewsDownloadData/"+file.getName() + " " +"/Users/Lohith/Documents/NBCNewsData/NBCNewsDownloadData/"+urlFileMap.get(url));
                }
            }
        }

        File edgeList = new File("/Users/Lohith/Documents/NBCNewsData/edgeList.txt");
        PrintWriter edgeWriter = new PrintWriter(edgeList);
        int count = 0;
        for (String s : edges) {
            edgeWriter.println(s);
            count++;
        }
        System.out.println(count);
        edgeWriter.flush();
        edgeWriter.close();
    }
}
