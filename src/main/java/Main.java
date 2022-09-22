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
    private static final String FILE_URL = "src/main/resources/comentarios.xlsx";
    private static final int SURVEYS = 5;

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");

        var comments = getCommentsArray(FILE_URL);

        var fixedThreadPool = Executors.newFixedThreadPool(3);
        comments.forEach(comment -> fixedThreadPool.submit(new FormFiller(comment)));
        fixedThreadPool.shutdown();
    }

    public static List<String> getCommentsArray(String fileUrl) {
        var comments = new ArrayList<String>();
        try (var inputStream = new FileInputStream(fileUrl);
             var workbook = new XSSFWorkbook(inputStream);
             var outputStream = new FileOutputStream(FILE_URL)) {
            var originSheet = workbook.getSheetAt(0);
            var targetSheet = workbook.getSheetAt(1);
            log.info("BEFORE -> Origin: {} - Target: {}", originSheet.getPhysicalNumberOfRows(), targetSheet.getPhysicalNumberOfRows());
            for (int i = 1; i <= SURVEYS; i++) {
                var lastRow = originSheet.getRow(originSheet.getLastRowNum());
                if (lastRow == null)
                    break;
                var comment = lastRow.getCell(0).getStringCellValue();
                if(!comment.isBlank()) {
                    log.info("Comment[{}] -> {}", i, comment);
                    comments.add(comment);
                    originSheet.removeRow(lastRow);
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
