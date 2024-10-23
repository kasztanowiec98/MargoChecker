package org.example.margoclandataget;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HTMLGetter {
    public static void getClan(String url, String xpath){
        try {
            // Fetch the HTML content from the URL
            Document doc = Jsoup.connect(url).get();

            // Use CSS selector to select elements
            Elements elements = doc.select(".char-data-column char-data-clan");

            System.out.println("x");
            System.out.println(doc.outerHtml());
            for(Element e: elements){
                String clan = e.select("a").text();
                System.out.println(clan);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
