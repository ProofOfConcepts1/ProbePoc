package demoBSE;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

public class utility {

	public WebDriver driverLaunch(String downloadLoc) {

//		Browser Launch

		// ./driver/C:\\Users\\stagadmin\\Documents\\BSEjar\\drivers\\
		System.setProperty("webdriver.chrome.driver", "./driver/chromedriver.exe");
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("download.default_directory", downloadLoc);
		ChromeOptions options = new ChromeOptions();
		String[] s = System.getProperty("user.dir").split("\\\\");
		String x = s[0] + "\\" + s[1] + "\\" + s[2] + "\\";
		options.setBinary(x + "Desktop\\chrome-win64\\chrome-win64\\chrome.exe");
		options.setExperimentalOption("prefs", chromePrefs);
		options.addArguments("--remote-allow-origins=*");
		// options.addArguments("--headless");
		options.addArguments("--window-size=1920,1080");
		options.addArguments("--disable-gpu");
		options.addArguments("–disable-extensions");
		ChromeDriver driver = new ChromeDriver(options);
		return driver;
	}

//	To get current date

	public java.sql.Date getCurrentDate() {
		java.util.Date today = new java.util.Date();
		return new java.sql.Date(today.getTime());
	}

//	To get current time

	public String currentTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
		LocalDateTime now = LocalDateTime.now();
		String currentTime = dtf.format(now);
		return currentTime;
	}

//	To rename the downloaded file

	public String rename(String downloadLoc) {

		String renameLoc = null;
		File file = new File(downloadLoc);
		File[] listFiles = file.listFiles();
		for (File file2 : listFiles) {
			if (file2.toString().contains("Pledge")) {
				String newFileName = downloadLoc + "\\Pled_" + currentTime() + ".csv";
				System.out.println(newFileName);
				boolean isRenamed = file2.renameTo(new File(newFileName));
				System.out.println(isRenamed);
				renameLoc = newFileName;
			}
		}

		return renameLoc;
	}
	
	
	public boolean rename1(String downloadLoc) {

		String renameLoc = null;
		File file = new File(downloadLoc);
		File[] listFiles = file.listFiles();
		for (File file2 : listFiles) {
			if (file2.toString().contains("Pledge")) {
				return true;
			}
		}
		return false;

		
	}
	

//	Database connection

	public Connection dbConnection(String url, String userName, String password) {

		Connection con = null;
		try {
			con = DriverManager.getConnection(url, userName, password);
			System.out.println("DB connection established");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return con;
	}

//	To split scripcode

	public String scripCode(String str) {
		String[] split = str.split(Pattern.quote("("));
		str = split[split.length - 1];
		String newStr = str.replace(")", " ");
		System.out.println(newStr);

		return newStr;
	}

//	To read the data in CSV file

	public List<String[]> readAllDataAtOnce(String file) throws IOException, CsvException {
//	 Create an object of file reader
		// class with CSV file as a parameter.
		FileReader filereader = new FileReader(file);
//	 create csvReader object and skip first Line
		CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
		List<String[]> allData = csvReader.readAll();
		// System.out.println(allData.size());
		// System.out.println(allData);

		return allData;

	}

//	Timestamp for Edate

	public String timeStamp() {
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
		// System.out.println(timeStamp);
		return timeStamp;

	}
	
//	waithelper to click

	public void waitHelperClick(WebDriver driver, String xpath, int waitTime) {

		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(waitTime))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(NoSuchElementException.class);

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
	}

}
