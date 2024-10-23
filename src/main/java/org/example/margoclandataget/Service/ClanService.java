package org.example.margoclandataget.Service;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ClanService {

    public String getPlayerId(String url) {
        int commaIndex = url.indexOf(',');
        String playerid = url.substring(commaIndex + 1);
        System.out.println(playerid);
        return playerid;
    }

    public String createMargonemPlayerUrl(String playerid, String charid, String server){
        return "https://margonem.pl/profile/view," + playerid + "#char_" + charid + "," + server;
    }

    public Document getDocumentFromMargonemUrl(int maxRetry, String url) throws Exception {
        int attempt = 0;

        while (attempt < maxRetry) {
            try {
                Document doc = Jsoup.connect(url).get();
                return doc;
            } catch (HttpStatusException e) {
                if (e.getStatusCode() == 500) {
                    System.out.println("/clanlist Received 500 error, retrying... Attempt " + (attempt + 1));
                } else {
                    String errorMsg = "/clanlist HTTP error fetching URL. Status: " + e.getStatusCode();
                    System.out.println(errorMsg);
                    throw new Exception(errorMsg); // Throw exception for non-retryable HTTP status
                }
            } catch (IOException e) {
                String errorMsg = "/clanlist IOException during connection attempt " + (attempt + 1) + ": " + e.getMessage();
                System.out.println(errorMsg);
                throw new IOException(errorMsg); // Throw IOException
            } catch (Exception e) {
                String errorMsg = "/clanlist Exception during connection attempt " + (attempt + 1) + ": " + e.getMessage();
                System.out.println(errorMsg);
                e.printStackTrace();
                throw new Exception(errorMsg); // Throw general Exception
            }

            attempt++;
            if (attempt < maxRetry) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new InterruptedException("Interrupted during retries"); // Throw InterruptedException
                }
            }
        }
        throw new Exception("Failed to fetch document after " + maxRetry + " attempts"); // Throw exception if all attempts fail
    }


}
