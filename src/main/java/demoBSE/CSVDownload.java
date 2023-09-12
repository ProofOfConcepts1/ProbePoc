package demoBSE;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.opencsv.exceptions.CsvException;

import config.ConfigFileReader;
import config.GmailEmailSender;

public class CSVDownload {
	private static final boolean True = false;

	static ConfigFileReader configFileReader;

	public static void main(String[] args) throws IOException, CsvException, SQLException {

		utility util = new utility();
		int differenceInCount;
		try {
			configFileReader = new ConfigFileReader();

// Setting up the variable names         
			
			WebDriver driver;
			String downloadLoc = configFileReader.getdownloadLoc();
			

			String url = configFileReader.getDBConnection();
			String userName = configFileReader.getUserName();
			String password = configFileReader.getPassword();
			
			int countExcel;
			String countSqlquery;
			int countSQL;
			int internalCounter;

			String name_of_the_target_company = null;
			String isin_number = null;
			String name_of_the_promoters = null;
			String ph_in_company_no_of_shares = null;
			String ph_in_company_per_of_equityshare = null;
			String ph_in_encumbered_no_of_shares = null;
			String ph_in_encumbered_per_of_equityshare = null;
			String depe_types_of_event = null;
			String depe_date_of_creation = null;
			String depe_no_of_shares = null;
			String depe_per_of_equityshare = null;
			String depe_reason_encumbrance = null;
			String depe_name_of_entity = null;
			String depe_name_of_ultimatelender = null;
			String pecs_no_of_shares = null;
			String pecs_per_of_equityShare = null;
			String exchange_broadcast_dt = null;
			String scraped_on = null;

//    	Launching the chrome driver
			driver = util.driverLaunch(downloadLoc);
			driver.manage().window().maximize();
			
//		Launching the url of the website
			driver.get(configFileReader.getWebsiteUrl()); // URL of the website

//      Locate and click the download link or button
			util.waitHelperClick(driver, configFileReader.getXpath(), 30);
			WebElement downloadLink = driver.findElement(By.xpath(configFileReader.getXpath()));

			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", downloadLink);

//      Wait for the file to download (you may need to adjust the wait time)

			while (!util.rename1(downloadLoc)) {

				try {
					Thread.sleep(7000); // Wait for 5 seconds
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

//		Quitting the driver after the file is downloaded
			
			driver.quit();
			
//		Renaming the downloaded file in the respective folder

			String renamedloc = util.rename(downloadLoc);

//		Reading the data in the downloaded csv file
			
			List<String[]> allData = util.readAllDataAtOnce(renamedloc);
			
//		Setting up the database connection and running the query to get the datas in the database

			Connection con = util.dbConnection(url, userName, password);

			countExcel = allData.size();

			countSqlquery = "select COUNT(*) from bse_pledge";

			Statement createStatement = con.createStatement();
			ResultSet executeQuery = createStatement.executeQuery(countSqlquery);
			countSQL = 0;

			executeQuery.next();

			countSQL = executeQuery.getInt(1);

			System.out.println("Excel Count: " + countExcel + "\n" + "SQL COUNT: " + countSQL);

			differenceInCount = countExcel - countSQL;
			
//		Sending exception mail when the no of data in the source is less than the data in the database			
			
			if (countExcel < countSQL) {
				GmailEmailSender.emailSent(configFileReader.getSubject() + '\r' + util.getCurrentDate() + "",
						configFileReader.getMailbody1() + '\n' + '\n' + configFileReader.getMailbody2() + '\n' + '\n'
								+ configFileReader.getMailbody3() + '\n' + '\n'
								+ "There is count mismatch issue SQL count is " + countSQL + " Excel count is "
								+ countExcel + " Hence deviation is " + differenceInCount + '\n' + '\n' + '\n' + '\n'
								+ configFileReader.getMailbody4());

			}
			
			System.out.println(differenceInCount);
			internalCounter = 0;
			
//      Reading the data from the source file

			if (differenceInCount != 0) {
				System.out.println("loop started");
				for (int i = differenceInCount - 1; i >= 0; i--) {

					String[] row = allData.get(i);

					name_of_the_target_company = row[0];
					name_of_the_target_company = name_of_the_target_company.replace(".", "");
					isin_number = row[1];
					name_of_the_promoters = row[2];
					ph_in_company_no_of_shares = row[3];
					ph_in_company_per_of_equityshare = row[4];
					ph_in_encumbered_no_of_shares = row[5];
					ph_in_encumbered_per_of_equityshare = row[6];
					depe_types_of_event = row[7];
					depe_date_of_creation = row[8];
					depe_no_of_shares = row[9];
					depe_per_of_equityshare = row[10];
					depe_reason_encumbrance = row[11];
					depe_reason_encumbrance = depe_reason_encumbrance.replaceAll("\\s{2,}", " ");
					depe_name_of_entity = row[12];
					depe_name_of_ultimatelender = row[13];
					pecs_no_of_shares = row[14];
					pecs_per_of_equityShare = row[15];
					try {
						exchange_broadcast_dt = row[16];
						String str = "9/1/2023 9:38:55 AM"; // length = 19
						if (exchange_broadcast_dt.length() == str.length()) {
						} else {
							exchange_broadcast_dt = null;
						}
					} catch (Exception e) {

					}
					
//		 Writing the data into the database
					
					scraped_on = util.timeStamp();

					String sql = "INSERT INTO bse_pledge "
							+ "(name_of_the_target_company,isin_number,name_of_the_promoters,ph_in_company_no_of_shares,ph_in_company_per_of_equityshare,ph_in_encumbered_no_of_shares,ph_in_encumbered_per_of_equityshare,depe_types_of_event,depe_date_of_creation,depe_no_of_shares,depe_per_of_equityshare,depe_reason_encumbrance,depe_name_of_entity,depe_name_of_ultimatelender,pecs_no_of_shares,pecs_per_of_equityShare,exchange_broadcast_dt,scraped_on) "
							+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

					PreparedStatement ps = con.prepareStatement(sql);

					ps.setString(1, name_of_the_target_company);
					ps.setString(2, isin_number);
					ps.setString(3, name_of_the_promoters);
					ps.setString(4, ph_in_company_no_of_shares);
					ps.setString(5, ph_in_company_per_of_equityshare);
					ps.setString(6, ph_in_encumbered_no_of_shares);
					ps.setString(7, ph_in_encumbered_per_of_equityshare);
					ps.setString(8, depe_types_of_event);
					ps.setString(9, depe_date_of_creation);
					ps.setString(10, depe_no_of_shares);
					ps.setString(11, depe_per_of_equityshare);
					ps.setString(12, depe_reason_encumbrance);
					ps.setString(13, depe_name_of_entity);
					ps.setString(14, depe_name_of_ultimatelender);
					ps.setString(15, pecs_no_of_shares);
					ps.setString(16, pecs_per_of_equityShare);
					ps.setString(17, exchange_broadcast_dt);
					ps.setString(18, scraped_on);
//    					ps.setTime(17, default);
//    					ps.setTimestamp(17,new Timestamp());

					int rowsAffected = ps.executeUpdate();

					System.out.println(rowsAffected);

					internalCounter++;

					System.out.println(differenceInCount);
					System.out.println(internalCounter);

					if (internalCounter >= differenceInCount) {
//    						con.commit();
						break;
					}
				}

			}
		} catch (Exception e) {
			
//			Sending exception mail for all kind of exceptions

			GmailEmailSender.emailSent(configFileReader.getSubject() + '\r' + util.getCurrentDate() + "",
					configFileReader.getMailbody1() + '\n' + '\n' + configFileReader.getMailbody2() + '\n' + '\n'
							+ configFileReader.getMailbody3() + '\n' + '\n' + e.getMessage() + '\n' + '\n' + '\n' + '\n'
							+ configFileReader.getMailbody4());
			e.printStackTrace();

		}
	}
}
