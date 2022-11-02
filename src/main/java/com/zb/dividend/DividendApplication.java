package com.zb.dividend;

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
		SpringApplication.run(DividendApplication.class, args);

		try {
			Connection connection = Jsoup.connect("https://finance.yahoo.com/quote/O/history?period1=1635767002&period2=1667303002&interval=1mo&filter=history&frequency=1mo&includeAdjustedClose=true");
			Document document = connection.get();

			Elements eles = document.getElementsByAttributeValue("data-test", "historical-prices");
			Element ele = eles.get(0);
		} catch (IOException e){
			e.printStackTrace();
		}

	}

}