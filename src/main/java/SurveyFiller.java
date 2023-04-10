import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class SurveyFiller implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(SurveyFiller.class);
    private final String comment;

    public SurveyFiller(String comment) {
        this.comment = comment;
    }

    @Override
    public void run() {
        fillAndSend();
    }

    public void fillAndSend() {
        var failures = false;
        var options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*","ignore-certificate-errors");
        var driver = new ChromeDriver(options);
        driver.get("https://chilis.miexperiencia.com.mx/");

        var start = Instant.now();
        log.info("LOADING...");
        sleep(Duration.ofSeconds(4).toMillis());
        log.info("STARTED -> {}s", Duration.between(start, Instant.now()).toMillis());

        var iframe = driver.findElement(By.tagName("iframe"));
        driver.switchTo().frame(iframe);

        try {
            section1(driver);
        } catch (Exception e) {
            failures = true;
            log.error("Execution failed in the 1st section", e);
        }

        try {
            section2(driver);
        } catch (Exception e) {
            failures = true;
            log.error("Execution failed in the 2nd section", e);
        }

        try {
            section3(driver);
        } catch (Exception e) {
            failures = true;
            log.error("Execution failed in the 3rd section", e);
        }

        try {
            section4(driver);
        } catch (Exception e) {
            failures = true;
            log.error("Execution failed in the 4th section", e);
        }

        try {
            section5(driver);
        } catch (Exception e) {
            failures = true;
            log.error("Execution failed in the 5th section", e);
        }

        try {
            section6(driver);
        } catch (Exception e) {
            failures = true;
            log.error("Execution failed in the 6th section", e);
        }

        try {
            section7(driver);
        } catch (Exception e) {
            failures = true;
            log.error("Execution failed in the 7th section", e);
        }

        if (!failures) {
            log.info("SURVEY COMPLETED SUCCESSFULLY");
            driver.close();
        }

        log.info("ENDING FORM AUTOMATION -> {}s", Duration.between(start, Instant.now()).toSeconds());
    }

    private void section1(WebDriver driver) {
        var start = Instant.now();
        sleep(2000);

        //how much would you recommend us?
        driver.findElement(By.id("onf_q_alsea_ltr_scale11_10")).click();
        //Why are you giving us this note?
        driver.findElement(By.id("spl_q_alsea_ltr_razones_comment")).sendKeys(comment);
        //Submit
        driver.findElement(By.id("buttonBegin")).submit();

        log.info("SECTION 1 -> {}s", Duration.between(start, Instant.now()).toSeconds());
    }

    private void section2(WebDriver driver) {
        var start = Instant.now();
        sleep(2000);

        //Do you have ticket?
        driver.findElement(By.id("onf_q_alsea_ticket_yn_2")).click();
        //Submit
        driver.findElement(By.id("buttonNext")).submit();

        log.info("SECTION 2 -> {}s", Duration.between(start, Instant.now()).toSeconds());
    }

    private void section3(WebDriver driver) {
        var start = Instant.now();
        sleep(2000);

        //Select State
        for (var state : openAndGetNextDropDownList(driver))
            if (state.getText().equals("Ciudad de México"))
                state.click();
        //Select City
        for (var city : openAndGetNextDropDownList(driver))
            if (city.getText().equals("Benito Juárez"))
                city.click();
        //Select Store
        for (var store : openAndGetNextDropDownList(driver))
            if (store.getText().equals("Chili's Insurgentes"))
                store.click();
        //Where was my service
        driver.findElement(By.id("onf_q_alsea_tipo_de_servicio_enum_0")).click();
        //Submit
        driver.findElement(By.id("buttonNext")).submit();

        log.info("SECTION 3 -> {}s", Duration.between(start, Instant.now()).toSeconds());
    }

    private void section4(WebDriver driver) {
        var start = Instant.now();
        sleep(2000);

        //5 stars
        driver.findElement(By.id("onf_q_alsea_osat_gral_enum_4")).click();
        //It gives me more for my money
        driver.findElement(By.id("onf_q_alsea_precio_calidad_enum3_1")).click();
        //Incidents in my visit
        driver.findElement(By.id("onf_q_alsea_incidente_yn_2")).click();
        //Submit
        driver.findElement(By.id("buttonNext")).submit();


        log.info("SECTION 4 -> {}s", Duration.between(start, Instant.now()).toSeconds());
    }

    private void section5(WebDriver driver) {
        var start = Instant.now();
        sleep(2000);

        //More details
        driver.findElement(By.id("onf_q_alsea_flex_yn_1")).click();
        //Submit
        driver.findElement(By.id("buttonNext")).submit();

        log.info("SECTION 5 -> {}s", Duration.between(start, Instant.now()).toSeconds());
    }

    private void section6(WebDriver driver) {
        var start = Instant.now();
        sleep(2000);

        //Service time
        driver.findElement(By.id("onf_q_alsea_atributos_tiempo_servicio_scale10_10")).click();
        //Order time
        driver.findElement(By.id("onf_q_alsea_atributos_tiempo_recibir_scale_10")).click();
        //Food QA
        driver.findElement(By.id("onf_q_alsea_atributos_calidad_scale_10")).click();
        //Food flavor
        driver.findElement(By.id("onf_q_alsea_atributos_sabor_scale_10")).click();
        //Food temperature
        driver.findElement(By.id("onf_q_alsea_atributos_temperatura_scale_10")).click();
        //Dish portion
        driver.findElement(By.id("onf_q_alsea_atributos_porcion_scale_10")).click();
        //Variety
        driver.findElement(By.id("onf_q_alsea_atributos_variedad_scale_10")).click();
        //Submit
        driver.findElement(By.id("buttonNext")).submit();

        log.info("SECTION 6 -> {}s", Duration.between(start, Instant.now()).toSeconds());
    }

    private void section7(WebDriver driver) {
        var start = Instant.now();
        sleep(3000);

        //Restroom clean
        driver.findElement(By.id("onf_q_alsea_inst_banos_10")).click();
        //Restaurant clean
        driver.findElement(By.id("onf_q_alsea_inst_restaurante_10")).click();
        //Welcome
        driver.findElement(By.id("onf_q_alsea_instal_bienvenida_1")).click();
        //Good attention
        driver.findElement(By.id("onf_q_alsea_instal_atencion_oportuna_1")).click();
        //Manager visit
        driver.findElement(By.id("onf_q_alsea_visita_gerente_yn_1")).click();
        //Submit
        driver.findElement(By.id("buttonFinish")).submit();

        log.info("SECTION 7 -> {}s", Duration.between(start, Instant.now()).toSeconds());
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.error("Error while Sleeping ");
            Thread.currentThread().interrupt();
        }
    }

    public static List<WebElement> openAndGetNextDropDownList(WebDriver driver) {
        var selects = driver.findElements(By.className("dropdown_dropdownTitle"));
        WebElement selectable = null;
        for (var select : selects)
            if (select.getText().equals("- Seleccionar -"))
                selectable = select;

        if (selectable != null)
            selectable.click();

        sleep(Duration.ofSeconds(1).toMillis());
        return driver.findElements(By.className("dropdown_dropdownListItem"));
    }
}
