package EasyRiskParityPortfolio;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

// 1. 종목 정보 입력 : 종목명, TICKER, 연도별 수익률
// 2. 종목에 대한 후속처리 : 연평균수익률, 조정 수익률, 표준편차 등 계산해서 각 테이블에 추가
public class EasyRiskParityPortfolio {

	public static void main(String[] args) {
		Connection         conn  = null;
		PreparedStatement  pstmt = null;
		ResultSet          rs    = null;
		ResultSetMetaData  rsmd  = null;
		String             SQL   = "";
		
		try {
			SQL = "SELECT * FROM yield";
			conn = dbConnector.getConnection();
			pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			rsmd = rs.getMetaData();
			
			// 필드(컬럼)명 출력
			int field_number = rsmd.getColumnCount();
			for(int i=1; i<=field_number; i++) {
				System.out.print(rsmd.getColumnName(i)+"\t");
			}
			System.out.println();
			System.out.println("===============================================================================================");
			
			// 데이터 출력
			while(rs.next()) {
				for(int i=1; i<=field_number; i++) {
					System.out.print(rs.getString(i)+"\t");
				}
				System.out.println();
			}
			System.out.println("===============================================================================================");
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally {
			try {
				if(rs!=null)    { rs.close(); }
				if(pstmt!=null) { pstmt.close(); }
				if(conn!=null)  { conn.close(); }
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	} // public static void main(String[] args)

} // public class EasyRiskParityPortfolio{} - END