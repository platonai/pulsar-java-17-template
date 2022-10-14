package org.platon.template;

import ai.platon.pulsar.browser.common.BrowserSettings;
import ai.platon.pulsar.context.PulsarContexts;
import ai.platon.pulsar.persist.WebPage;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Map;

public class BasicUsage {
    public static void main(String[] args) {
        // browser settings
        BrowserSettings.privacyContext(2).maxTabs(8).withGUI();

        // create a pulsar session
        var session = PulsarContexts.createSession();
        // the main url we are playing with
        var url = "https://list.jd.com/list.html?cat=652,12345,12349";
        // load a page, fetch it from the web if it has expired or if it's being fetched for the first time
        var page = session.load(url, "-expires 1d");
        // parse the page content into a Jsoup document
        var document = session.parse(page, false);
        // do something with the document
        // ...

        // or, load and parse
        var document2 = session.loadDocument(url, "-expires 1d");
        // do something with the document
        // ...

        // load all pages with links specified by -outLink
        var pages = session.loadOutPages(url, "-expires 1d -itemExpires 7d -outLink a[href~=item]");
        // load, parse and scrape fields
        var fields = session.scrape(url, "-expires 1d", "li[data-sku]",
                Arrays.asList(".p-name em", ".p-price"));
        // load, parse and scrape named fields
        var fields2 = session.scrape(url, "-i 1d", "li[data-sku]",
                Map.of("name", ".p-name em", "price", ".p-price"));

        System.out.println("== document");
        System.out.println(document.getTitle());

        document.selectFirstOptional("title").ifPresent(System.out::println);

        System.out.println("== document2");
        System.out.println(document2.getTitle());
        document.selectFirstOptional("title").ifPresent(System.out::println);

        System.out.println("== pages");
        System.out.println(pages.stream().map(WebPage::getUrl).toList());

        var gson = new Gson();
        System.out.println("== fields");
        System.out.println(gson.toJson(fields));

        System.out.println("== fields2");
        System.out.println(gson.toJson(fields2));
    }
}
