package com.zb.dividend.scheduler;

import com.zb.dividend.model.Company;
import com.zb.dividend.model.ScrapedResult;
import com.zb.dividend.persist.CompanyRepository;
import com.zb.dividend.persist.DividendRepository;
import com.zb.dividend.persist.entity.CompanyEntity;
import com.zb.dividend.persist.entity.DividendEntity;
import com.zb.dividend.scraper.Scraper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class ScraperScheduler {
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;
    private final Scraper yahooFinanceScraper;

    @Scheduled(cron = "${scheduler.scrap.yahoo}")
    public void yahooFinanceScheduling(){
        log.info("scraping scheduler is started");
        //저장된 회사 목록 조회
        List<CompanyEntity> companies = this.companyRepository.findAll();

        // 회사마다 배당금 정보 새로 스크래핑
        for (var company: companies){
            log.info("scraping scheduler is started -> " + company.getName());
            ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(Company.builder()
                                                                                .name(company.getName())
                                                                                .ticker(company.getTicker())
                                                                                .build());
            // 스크래핑한 대방금 정보 중 데이터베이스에 없는 값 저장
            scrapedResult.getDividends().stream()
                    // 디비든 모델을 디비든 엔티티로 매핑
                    .map(e -> new DividendEntity(company.getId(), e))
                    // 엘리먼트를 하나씩 디비든 레파지토리에 삽입
                    .forEach(e -> {
                        boolean exists = this.dividendRepository.existsByCompanyIdAndDate(e.getCompanyId(), e.getDate());
                        if(!exists){
                            this.dividendRepository.save(e);
                        }
                    });

            // 연속적으로 스크래핑 대상 사이트 서버에 요청을 날리지 않도ㅗㄱ 일시정지
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e){
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }

        }


    }
}
