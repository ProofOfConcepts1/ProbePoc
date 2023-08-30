package demoBSE;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.openqa.selenium.By;
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

			WebDriver driver;
			String downloadLoc = configFileReader.getdownloadLoc();
//    	String dummyDownloadLoc = "C:\\Users\\Kiruthiga.5785\\Documents\\Blinkit\\BSE\\BseDownload\\Pledge.csv";

			String url = configFileReader.getDBConnection();
			String userName = configFileReader.getUserName();
			String password = configFileReader.getPassword();

			

			String NameoftheTargetCompany = null;
			String ISIN = null;
			String NamesOfthePromoters = null;
			String PHinComapny_noOfShares = null;
			String PHinCompany_perOfequityShare = null;
			String PHinEncumbered_noOfShares = null;
			String PHinEncumbered_perOfequityShare = null;
			String DEPE_typesOfEvent = null;
			String DEPE_dateOfCreation = null;
			String DEPE_noOfShares = null;
			String DEPE_perOfequityShare = null;
			String DEPE_ReasonEncumbrance = null;
			String DEPE_NameodEntity = null;
			String DEPE_NameofUltimateLender = null;
			String PECS_noOfShares = null;
			String PECS_perOfequityShare = null;
			String ExchangeBroadcastDT = null;
			String Edate = null;

//    	utility util = new utility();
			driver = util.driverLaunch(downloadLoc);

			driver.manage().window().maximize();
			driver.get(configFileReader.getWebsiteUrl()); // URL of the website

			// Locate and click the download link or button
			WebElement downloadLink = driver.findElement(By.xpath(configFileReader.getXpath()));
			downloadLink.click();

			// Wait for the file to download (you may need to adjust the wait time)
			try {
				Thread.sleep(50000); // Wait for 5 seconds
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			driver.quit();

			String renamedloc = util.rename(downloadLoc);

			List<String[]> allData = util.readAllDataAtOnce(renamedloc);

			Connection con = util.dbConnection(url, userName, password);
//    	con.setAutoCommit(True);

			int countExcel = allData.size();

			String countSqlquery = "select COUNT(*) from app_pledge_api";

			Statement createStatement = con.createStatement();
			ResultSet executeQuery = createStatement.executeQuery(countSqlquery);
			int countSQL = 0;

			executeQuery.next();

			countSQL = executeQuery.getInt(1);

			System.out.println("Excel Count: " + countExcel + "\n" + "SQL COUNT: " + countSQL);

			 differenceInCount = countExcel - countSQL;
			 if(differenceInCount<countSQL)
			 {
			 GmailEmailSender.emailSent(configFileReader.getSubject()+'\r'+ util.getCurrentDate() + "",
						configFileReader.getMailbody1() + '\n' + '\n' + configFileReader.getMailbody2() + '\n' + '\n'
								+ configFileReader.getMailbody3() + '\n' + '\n' + "There is count mismatch issue SQL count is "+countSQL+" Excel count is "+countExcel+" Hence deviation is "+differenceInCount+ '\n' + '\n'+'\n'+'\n'
								+ configFileReader.getMailbody4());
				
			 }
			System.out.println(differenceInCount);
			int internalCounter = 0;

			if (differenceInCount != 0) {
				System.out.println("loop started");
				for (int i = differenceInCount - 1; i >= 0; i--) {

					String[] row = allData.get(i);

					NameoftheTargetCompany = row[0];
					ISIN = row[1];
					NamesOfthePromoters = row[2];
					PHinComapny_noOfShares = row[3];
					PHinCompany_perOfequityShare = row[4];
					PHinEncumbered_noOfShares = row[5];
					PHinEncumbered_perOfequityShare = row[6];
					DEPE_typesOfEvent = row[7];
					DEPE_dateOfCreation = row[8];
					DEPE_noOfShares = row[9];
					DEPE_perOfequityShare = row[10];
					DEPE_ReasonEncumbrance = row[11];
					DEPE_ReasonEncumbrance = DEPE_ReasonEncumbrance.replaceAll("\\s{2,}"," ");
					DEPE_NameodEntity = row[12];
					DEPE_NameofUltimateLender = row[13];
					PECS_noOfShares = row[14];
					PECS_perOfequityShare = row[15];
					try {
						ExchangeBroadcastDT = row[16];
					} catch (Exception e) {
					}
					Edate = util.timeStamp();

					String sql = "INSERT INTO app_pledge_api "
							+ "(NameoftheTargetCompany,ISIN,NamesOfthePromoters,PHinComapny_noOfShares,PHinCompany_perOfequityShare,PHinEncumbered_noOfShares,PHinEncumbered_perOfequityShare,DEPE_typesOfEvent,DEPE_dateOfCreation,DEPE_noOfShares,DEPE_perOfequityShare,DEPE_ReasonEncumbrance,DEPE_NameodEntity,DEPE_NameofUltimateLender,PECS_noOfShares,PECS_perOfequityShare,ExchangeBroadcastDT,Edate) "
							+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

					PreparedStatement ps = con.prepareStatement(sql);

					ps.setString(1, NameoftheTargetCompany);
					ps.setString(2, ISIN);
					ps.setString(3, NamesOfthePromoters);
					ps.setString(4, PHinComapny_noOfShares);
					ps.setString(5, PHinCompany_perOfequityShare);
					ps.setString(6, PHinEncumbered_noOfShares);
					ps.setString(7, PHinEncumbered_perOfequityShare);
					ps.setString(8, DEPE_typesOfEvent);
					ps.setString(9, DEPE_dateOfCreation);
					ps.setString(10, DEPE_noOfShares);
					ps.setString(11, DEPE_perOfequityShare);
					ps.setString(12, DEPE_ReasonEncumbrance);
					ps.setString(13, DEPE_NameodEntity);
					ps.setString(14, DEPE_NameofUltimateLender);
					ps.setString(15, PECS_noOfShares);
					ps.setString(16, PECS_perOfequityShare);
					ps.setString(17, ExchangeBroadcastDT);
					ps.setString(18, Edate);
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
			
			GmailEmailSender.emailSent(configFileReader.getSubject()+'\r'+ util.getCurrentDate() + "",
					configFileReader.getMailbody1() + '\n' + '\n' + configFileReader.getMailbody2() + '\n' + '\n'
							+ configFileReader.getMailbody3() + '\n' + '\n' + e.getMessage() + '\n' + '\n'+'\n'+'\n'
							+ configFileReader.getMailbody4());
			e.printStackTrace();

		}
	}
}
