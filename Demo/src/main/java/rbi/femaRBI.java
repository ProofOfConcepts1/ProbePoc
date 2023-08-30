package rbi;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import config.ConfigFileReader;
import config.GmailEmailSender;

public class femaRBI extends ConfigFileReader {

	public femaRBI() throws IOException {
		super();
	}

	static DateTimeFormatter dtFormatter;
	static LocalDate date;
	static String currDate;

	static ConfigFileReader configFileReader;

	public static void main(String[] args) throws SQLException, IOException {

		ChromeDriver driver = null;
		

		try {
			System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");

			configFileReader = new ConfigFileReader();
			dtFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			date = LocalDate.now();
			currDate = date.format(dtFormatter);
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--remote-allow-origins=*");
			options.addArguments("--headless");
			
			String[] s = System.getProperty("user.dir").split("\\\\");
			String x = s[0]+"\\"+s[1]+"\\"+s[2]+"\\";
			
			options.setBinary(x+"Desktop\\chrome-win64\\chrome-win64\\chrome.exe");
			driver = new ChromeDriver(options);
			

//		 Navigate to the webpage

			driver.get(configFileReader.getWebsiteUrl());
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			driver.manage().window().maximize();
			WebElement tbl;
			try {
				tbl = driver.findElement(By.xpath(configFileReader.getXpath()));
			} catch (Exception e) {

//				 need to write the code send an email

				throw e;
			}

//		check all row, identification with 'tr' tag

			List<WebElement> rows = tbl.findElements(By.tagName(configFileReader.getRow()));
			System.out.println(rows.size() - 1);
			System.out.println(rowCount());
			if (rowCount() < rows.size() - 1) {
				int a = rows.size() - 1 - rowCount();
				for (int i = a; i > 0; i--) {

//		check column each in row, identification with 'td' tag

					List<WebElement> cols = rows.get(i).findElements(By.tagName(configFileReader.getColumn()));

//		column iteration

					for (int j = 1; j < cols.size(); j++) {
//						System.out.println(cols.get(j).getText());
					}
//					System.out.println(i + "---" + cols.get(1).getText() + "-" + cols.get(2).getText());
					String col1Data = cols.get(1).getText();
					String col2Data = cols.get(2).getText();
					String date = cols.get(3).getText();
					String[] split = date.split("[-]");
					String col3Data = LocalDate
							.of(Integer.parseInt(split[2]), Integer.parseInt(split[1]), Integer.parseInt(split[0]))
							.toString();
					String col4Data = cols.get(4).getText();
					// String col5Data = dateOfScraping;
					String col5Data = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
					insertDataIntoDatabase(col1Data, col2Data, col3Data, col4Data, col5Data);
				}
				System.out.println("data uploaded successfully");
				driver.close();
			} else if (rowCount() == 0) {

//			row iteration

				for (int i = rows.size() - 1; i > 0; i--) {

//				check column each in row, identification with 'td' tag

					List<WebElement> cols = rows.get(i).findElements(By.tagName(configFileReader.getColumn()));

//				 column iteration

					for (int j = 1; j < cols.size(); j++) {
//						System.out.println(cols.get(j).getText());
					}
//				System.out.println(i + "---" + cols.get(1).getText() + "-" + cols.get(2).getText());

					String col1Data = cols.get(1).getText();
					String col2Data = cols.get(2).getText();
					String date = cols.get(3).getText();
					String[] split = date.split("[-]");
					String col3Data = LocalDate
							.of(Integer.parseInt(split[2]), Integer.parseInt(split[1]), Integer.parseInt(split[0]))
							.toString();
					String col4Data = cols.get(4).getText();
					String col5Data = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());

					// String col5Data = dateOfScraping;

					insertDataIntoDatabase(col1Data, col2Data, col3Data, col4Data, col5Data);
				}
				System.out.println("data uploaded successfully");
				driver.close();
			} else {
				driver.close();
			}

			ErrorLog log = new ErrorLog("PASS", "");
		} catch (Exception e) {
			System.out.println(e);

			GmailEmailSender.emailSent(configFileReader.getSubject()+'\r'+ currDate + "",
					configFileReader.getMailbody1() + '\n' + '\n' + configFileReader.getMailbody2() + '\n' + '\n'
							+ configFileReader.getMailbody3() + '\n' + '\n' + e.getMessage() + '\n' + '\n'+'\n'+'\n'
							+ configFileReader.getMailbody4());
			ErrorLog log = new ErrorLog("Fail", e.getMessage());
			try {
				driver.close();
			} catch (Exception e2) {
				System.out.println("Browser is already closed");
				System.out.println(e2);
			}
		}
	}

	private static void insertDataIntoDatabase(String col1Data, String col2Data, String col3Data, String col4Data,
			String col5Data) throws IOException {
		try {

//			 Establish database connection

			Connection connection = DriverManager.getConnection(configFileReader.getDBConnection(),
					configFileReader.getUserName(), configFileReader.getPassword());
			String sql = "INSERT INTO app_fema (name_of_applicant,details_of_contraventions,date_of_order,amount_imposed,scraped_at) VALUES (?,?,?,?,?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, col1Data);
			statement.setString(2, col2Data);
			statement.setString(3, col3Data);
			statement.setString(4, col4Data);
			statement.setString(5, col5Data);

//			 Execute the statement

			statement.executeUpdate();

//			 Close resources

			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static int rowCount() throws SQLException, IOException {
		Connection connection = DriverManager.getConnection(configFileReader.getDBConnection(),
				configFileReader.getUserName(), configFileReader.getPassword());

//		 Prepare the SQL statement

		String query = "SELECT * FROM app_fema";

//		 PreparedStatement statement = connection.prepareStatement(query);

		Statement st = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = st.executeQuery(query);
		int rows = 0;
		rs.last();
		rows = rs.getRow();
		rs.beforeFirst();
		return rows;

//		 System.out.println("Your query have " + rows + " rows.");
	}

}