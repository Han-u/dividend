package com.zb.dividend;

import com.zb.dividend.model.Company;
import com.zb.dividend.scraper.YahooFinanceScraper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class DividendApplication {

	public static void main(String[] args) {
//		SpringApplication.run(DividendApplication.class, args);
		YahooFinanceScraper scraper = new YahooFinanceScraper();
		var result = scraper.scrap(Company.builder().ticker("o").build());
		System.out.println(result);
	}

}
