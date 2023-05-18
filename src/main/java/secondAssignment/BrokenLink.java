package secondAssignment;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import resources.Base;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.net.HttpURLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;


public class BrokenLink {
    WebDriver driver;
    WebDriverWait wait;

    File src;
    File source;
    Base base;

    public BrokenLink(WebDriver driver, Base base) {
        this.driver = driver;
        this.base = base;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

    }

    public void verifyLink() throws IOException {
        Logger log = Logger.getLogger(getClass().getName());
        String homePage = "https://www.ebay.com/";
        String url = "";
        HttpURLConnection httpURLConnection = null;
        int respCode = 200;

        FileOutputStream fos = new FileOutputStream("output.xlsx");

        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet sheet = workbook.createSheet("LinkOutput");

        //Creating Headers on Xel sheet
        Row headerRow = sheet.createRow(0);
        Cell headerCell1 = headerRow.createCell(0);
        headerCell1.setCellValue("Links from footer of the page");
        Cell headerCell2 = headerRow.createCell(1);
        headerCell2.setCellValue("StatusCode of the link");
        Cell headerCell3 = headerRow.createCell(2);
        headerCell3.setCellValue("Result based on status-code");
        workbook.write(fos);


        //Count of the Link in the Entire Page
        WebElement footer = driver.findElement(By.xpath(".//*[@id='glbfooter']"));
        List<WebElement> links = footer.findElements(By.tagName("a"));
        log.info("Links on the Page :" + links.size());
        Iterator<WebElement> it = links.iterator();

        int RowCount = 1;

        while (it.hasNext()) {

            url = it.next().getAttribute("href");

            if (url == null || url.isEmpty()) {
                log.info("URL is either not configured for anchor tag or it is empty");
                continue;
            }
            try {
                // Send HTTP request
                httpURLConnection = (HttpURLConnection) (new URL(url).openConnection());

                httpURLConnection.setRequestMethod("HEAD");

                httpURLConnection.connect();

                //Validate Links
                respCode = httpURLConnection.getResponseCode();

                //Write result in Xel sheet
                Row DataRow = sheet.createRow(RowCount++);
                Cell DataCell1 = DataRow.createCell(0);
                DataCell1.setCellValue(url);
                Cell DataCell2 = DataRow.createCell(1);
                DataCell2.setCellValue(respCode);
                log.info("URL:" + url + "StatusCode : " + respCode);
                Cell DataCell3 = DataRow.createCell(2);
                if (respCode >= 400) {

                    DataCell3.setCellValue(" It is a broken link");
                } else {
                    DataCell3.setCellValue(" It is a valid link");
                }
                workbook.write(fos);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                FileUtils.copyFile(source, new File("screenshot(URLException).png"));

            } catch (IOException e) {
                e.printStackTrace();
                FileUtils.copyFile(source, new File("screenshot(IOException).png"));

            } catch (NoSuchElementException e) {
                e.printStackTrace();
                FileUtils.copyFile(source, new File("screenshot(NoSuchElementException).png"));

            } catch (StaleElementReferenceException e) {
                e.printStackTrace();
                FileUtils.copyFile(source, new File("screenshot(StaleElementException).png"));

            } catch (TimeoutException e) {
                e.printStackTrace();
                FileUtils.copyFile(source, new File("screenshot(TimeOutException).png"));

            } catch (WebDriverException e) {
                e.printStackTrace();
                FileUtils.copyFile(source, new File("screenshot(WebDriverException).png"));

            }
        }
    }
}
