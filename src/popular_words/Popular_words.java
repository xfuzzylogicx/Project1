package popular_words;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Popular_words {

    public static void main(String[] args)
    {

        String[] infoSites= new String[]{"http://www.onet.pl/","http://www.polsatnews.pl/","https://niezalezna.pl/"};
        ArrayList<String> titles=new ArrayList<String>();
        StringBuilder sb =new StringBuilder();
        PrintWriter printWriter=null;
        try {
            for (int i = 0; i < infoSites.length; i++) {
                Connection connect = Jsoup.connect(infoSites[i]);
                Document document = connect.get();
                Elements links=null;
                switch (infoSites[i])
                {
                    case "http://www.polsatnews.pl/" : links= document.select("h2.news__title");
                    for (Element elem : links) {
                        titles.add(elem.text());
                    }
                    break;
                   case "http://www.onet.pl/" :
                       links= document.select("span.title");
                       for (Element elem : links) {
                           titles.add(elem.text());
                       }
                        break;
                    case "https://niezalezna.pl/": links= document.select("div.uitemTitle");
                    for (Element elem : links) {
                        titles.add(elem.text());
                    }
                    break;
                }


            }

            Scanner scanner = new Scanner(titles.toString());

            printWriter = new PrintWriter("popular_words.txt");
            while (scanner.hasNext()) {
                sb.append(scanner.next().replaceAll("\\P{L}", " ").trim());
                if (sb.toString().length() > 3) {
                    printWriter.print(sb.toString());
                    printWriter.println();
                }
                sb.delete(0, sb.length());
            }
            printWriter.close();
            Path popularPath = Paths.get("./popular_words.txt");
            String[] exclude = new String[]{"ponieważ", "oraz"};
            printWriter=new PrintWriter("filtered_popular_words.txt");
            sb.append(Files.readAllLines(popularPath));
            for (String word : Files.readAllLines(popularPath))
            {
                for(int i=0;i<exclude.length;i++)
                {
                    if (word.toLowerCase().equals(exclude[i].toLowerCase()))
                    {
                        word="";
                    }
                }

                if (word.length()>1)
                {
                    printWriter.println(word);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
