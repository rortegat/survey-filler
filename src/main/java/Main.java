import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final String COMMENTS_FILE_URL = "src/main/resources/comentarios.xlsx";
    private static final String CHROMEDRIVER_FILE_URL = "src/main/resources/chromedriver.exe";
    private static final int SURVEYS = 10;
    private static final int USERS = 5;

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", CHROMEDRIVER_FILE_URL);

        //Getting required comments
        var comments = getCommentsArray();

        //Number of users filling surveys at the time
        var fixedThreadPool = Executors.newFixedThreadPool(USERS);
        //Running simulation
        comments.forEach(comment -> fixedThreadPool.submit(new SurveyFiller(comment)));
        //Waiting for threads task completion to free up memory
        fixedThreadPool.shutdown();
    }

    public static List<String> getCommentsArray() {
        var comments = new ArrayList<String>();
        try (var inputStream = new FileInputStream(COMMENTS_FILE_URL);
             var outputStream = new FileOutputStream(COMMENTS_FILE_URL);
             var workbook = new XSSFWorkbook(inputStream)) {
            var originSheet = workbook.getSheetAt(0);
            var targetSheet = workbook.getSheetAt(1);
            log.info("BEFORE -> Origin: {} - Target: {}", originSheet.getPhysicalNumberOfRows(), targetSheet.getPhysicalNumberOfRows());
            for (int i = 1; i <= SURVEYS; i++) {
                var lastRow = originSheet.getRow(originSheet.getLastRowNum());
                if (lastRow == null)
                    break;
                var comment = lastRow.getCell(0).getStringCellValue();
                if (!comment.isBlank()) {
                    log.info("Comment[{}] -> {}", i, comment);
                    comments.add(comment);
                    originSheet.removeRow(lastRow);
                    if (targetSheet.getRow(targetSheet.getLastRowNum()) != null)
                        targetSheet.shiftRows(0, targetSheet.getLastRowNum(), 1, true, true);
                    targetSheet.createRow(0).createCell(0).setCellValue(comment);
                }
            }
            log.info("AFTER -> Origin: {} - Target: {}", originSheet.getPhysicalNumberOfRows(), targetSheet.getPhysicalNumberOfRows());
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return comments;
    }
}
