package EasyRiskParityPortfolio;


import java.sql.Connection;
import java.sql.DriverManager;

public class dbConnector {
	
	public static Connection getConnection() {
		Connection conn = null;
		
		try {
			String url = "jdbc:mysql://localhost:3306/easyriskparitypf?serverTimezone=Asia/Seoul&useSSL=false";
			String user = "easy_risk_parity";
			String password = "easy";
			
			// Loading Driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, password);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return conn;
	} // public static Connection getConnection() - END
	
} // public class dbConnector{} - END