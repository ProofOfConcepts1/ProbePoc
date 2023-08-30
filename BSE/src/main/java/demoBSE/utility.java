package demoBSE;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

public class utility {

	public WebDriver driverLaunch(String downloadLoc) {
//		./driver/C:\\Users\\stagadmin\\Documents\\BSEjar\\drivers\\
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\stagadmin\\Documents\\BSEjar\\drivers\\chromedriver.exe");

		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("download.default_directory", downloadLoc);
		ChromeOptions options = new ChromeOptions();
		String[] s = System.getProperty("user.dir").split("\\\\");
		String x = s[0] + "\\" + s[1] + "\\" + s[2] + "\\";
		options.setBinary(x + "Desktop\\chrome-win64\\chrome-win64\\chrome.exe");
		options.setExperimentalOption("prefs", chromePrefs);
		options.addArguments("--remote-allow-origins=*");
		options.addArguments("--headless");
		ChromeDriver driver = new ChromeDriver(options);
		return driver;
	}

	public java.sql.Date getCurrentDate() {
		java.util.Date today = new java.util.Date();
		return new java.sql.Date(today.getTime());
	}

	public String currentTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
		LocalDateTime now = LocalDateTime.now();
		String currentTime = dtf.format(now);
		return currentTime;
	}

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

	public String scripCode(String str) {
		String[] split = str.split(Pattern.quote("("));
		str = split[split.length - 1];
		String newStr = str.replace(")", " ");
		System.out.println(newStr);

		return newStr;
	}

	public List<String[]> readAllDataAtOnce(String file) throws IOException, CsvException {
		// Create an object of file reader
		// class with CSV file as a parameter.
		FileReader filereader = new FileReader(file);

		// create csvReader object and skip first Line
		CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
		List<String[]> allData = csvReader.readAll();
//		System.out.println(allData.size());
//		System.out.println(allData);

		return allData;

	}

	public String timeStamp() {
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
//		System.out.println(timeStamp);
		return timeStamp;

	}

	

}
