package uk.gov.hmcts.reform.wataskconfigurationapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@SuppressWarnings("HideUtilityClassConstructor") // Spring needs a constructor, its not a utility class
@EnableFeignClients(basePackages = {
    "uk.gov.hmcts.reform.wataskconfigurationapi",
    "uk.gov.hmcts.reform.authorisation",
    "uk.gov.hmcts.reform.ccd.client"
})
@EnableCaching
public class Application {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
