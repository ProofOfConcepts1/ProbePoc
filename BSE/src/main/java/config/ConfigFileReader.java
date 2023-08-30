package config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Properties;

public class ConfigFileReader {

	static Properties properties;

	public ConfigFileReader() throws IOException {
		properties = new Properties();
//		System.getProperty("user.dir");
		File f = new File( "./Config/data.properties");
		FileReader reader = new FileReader(f);
		properties.load(reader);
	}

	public static String getBrowser() throws IOException, SQLException {
		String browser = properties.getProperty("browser");
		if (browser != null)
			return browser;
		else
			throw new RuntimeException("browser not specified in the Configuration.properties file.");
	}

	public String getXpath() throws IOException, SQLException {
		String xpath = properties.getProperty("Xpath");
		if (xpath != null)
			return xpath;
		else
			throw new RuntimeException("Xpath not specified in the Configuration.properties file.");
	}

	public String getWebsiteUrl() throws IOException, SQLException {
		String url = properties.getProperty("WebsiteURL");
		if (url != null)
			return url;
		else
			throw new RuntimeException("URL not specified in the Configuration.properties file.");
	}

	public String getDBConnection() throws IOException, SQLException {
		String DBConn = properties.getProperty("DBConnection");
		if (DBConn != null)
			return DBConn;
		else
			throw new RuntimeException("DBConn not specified in the Configuration.properties file.");
	}

	public String getUserName() throws IOException, SQLException {

		String userName = decrypt(properties.getProperty("UserName"));
		if (userName != null)
			return userName;
		else
			throw new RuntimeException("userName not specified in the Configuration.properties file.");
	}

	public String getPassword() throws IOException, SQLException {
		String password = decrypt(properties.getProperty("Password"));
		if (password != null)
			return password;
		else
			throw new RuntimeException("password not specified in the Configuration.properties file.");
	}
	
//	public String getLocalUserName() throws IOException, SQLException {
//
//		String LocalUserName = decrypt(properties.getProperty("LocalUserName"));
//		if (LocalUserName != null)
//			return LocalUserName;
//		else
//			throw new RuntimeException("LocalUserName not specified in the Configuration.properties file.");
//	}
//
//	public String getLocalPassword() throws IOException, SQLException {
//		String LocalPassword = decrypt(properties.getProperty("LocalPassword"));
//		if (LocalPassword != null)
//			return LocalPassword;
//		else
//			throw new RuntimeException("LocalPassword not specified in the Configuration.properties file.");
//	}
	
	public String getdownloadLoc() throws IOException, SQLException {
		String downloadLoc = properties.getProperty("downloadLoc");
		if (downloadLoc != null)
			return downloadLoc;
		else
			throw new RuntimeException("downloadLoc not specified in the Configuration.properties file.");
	}
	
	public static String decrypt(String data) {
		byte[] decodedByte = Base64.getDecoder().decode(data.getBytes());
		String decodedData = new String(decodedByte);
//		System.out.println(decodedData);
		return decodedData;

	}
	
	public String getTo() throws IOException, SQLException {
		String toMail =properties.getProperty("To");
		if (toMail != null)
			return toMail;
		else
			throw new RuntimeException("toMail not specified in the Configuration.properties file.");
	}
	
	public String getCC() throws IOException, SQLException {
		String ccMail = properties.getProperty("CC");
		if (ccMail != null)
			return ccMail;
		else
			throw new RuntimeException("ccMail not specified in the Configuration.properties file.");
	}
	
	public String getSubject() throws IOException, SQLException {
		String subject = properties.getProperty("Subject");
		if (subject != null)
			return subject;
		else
			throw new RuntimeException("subject not specified in the Configuration.properties file.");
	}
	
	public String getMailbody1() throws IOException, SQLException {
		String mailbody1 = properties.getProperty("Mailbody1");
		if (mailbody1 != null)
			return mailbody1;
		else
			throw new RuntimeException("mailbody1 not specified in the Configuration.properties file.");
	}
	
	public String getMailbody2() throws IOException, SQLException {
		String mailbody2 = properties.getProperty("Mailbody2");
		if (mailbody2 != null)
			return mailbody2;
		else
			throw new RuntimeException("mailbody2 not specified in the Configuration.properties file.");
	}
	
	public String getMailbody3() throws IOException, SQLException {
		String mailbody3 = properties.getProperty("Mailbody3");
		if (mailbody3 != null)
			return mailbody3;
		else
			throw new RuntimeException("mailbody3 not specified in the Configuration.properties file.");
	}
	
	public String getMailbody4() throws IOException, SQLException {
		String mailbody4 = properties.getProperty("Mailbody4");
		if (mailbody4 != null)
			return mailbody4;
		else
			throw new RuntimeException("mailbody4 not specified in the Configuration.properties file.");
	}
	
	
	
	
	
	

//	public String getNameOfTable() throws IOException, SQLException {
//		String table = properties.getProperty("NameOfTable");
//		System.out.println(table);
//		if (table != null)
//			return table;
//		else
//			throw new RuntimeException("table not specified in the Configuration.properties file.");
//	}

//	public String getTableName() throws IOException, SQLException {
//		String tableName = properties.getProperty("TableName");
//		if (tableName != null)
//			return tableName;
//		else
//			throw new RuntimeException("tableName not specified in the Configuration.properties file.");
//	}

//	public static String encrypt(String data) {
//		byte[] encodedByte = Base64.getEncoder().encode(data.getBytes());
//		String encodedData = new String(encodedByte);
//		System.out.println(encodedData);
//		return encodedData;
//	}

	

}
