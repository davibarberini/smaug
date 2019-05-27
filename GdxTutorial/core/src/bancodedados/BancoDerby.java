package bancodedados;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;


import java.sql.ResultSet;
import java.sql.SQLException;

public class BancoDerby {
	private Connection connection = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	private String tableName = "PLAYER";
	
	public void connect() {
		String servidor = "jdbc:derby:offlife;create=true";
		String driver = "org.apache.derby.jdbc.EmbeddedDriver";
		try {			
			Class.forName(driver);
			this.connection = DriverManager.getConnection(servidor);
			this.statement = this.connection.createStatement();
			if(connection!=null)
		    {
		        DatabaseMetaData dbmd = connection.getMetaData();
		        ResultSet rs = dbmd.getTables(null, null, tableName.toUpperCase(),null);
		        if(rs.next())
		        {
		            System.out.println("Table "+rs.getString("TABLE_NAME")+" already exists !!");
		        }
		        else
		        {
		        	PreparedStatement create = connection.prepareStatement("CREATE TABLE PLAYER(NOME VARCHAR(50), SCORE VARCHAR(50))");
					create.executeUpdate();
		        }
		    }
			
		} catch(Exception e){
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	public boolean isConnected() {
		if(this.connection != null) {
			return true;
		}
		else return false;
	}
	
	public void resetAllScores() {
		
		try {
			PreparedStatement delete = connection.prepareStatement("DROP TABLE PLAYER");
			delete.executeUpdate();
			PreparedStatement create = connection.prepareStatement("CREATE TABLE PLAYER(NOME VARCHAR(50), SCORE VARCHAR(50))");
			create.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void listPlayers(ArrayList<Integer> scoreList) {
		try {
			String query = "SELECT * FROM player ORDER BY nome";
			this.resultSet = this.statement.executeQuery(query);
			//this.statement = this.connection.createStatement();
			while(this.resultSet.next()) {
				//System.out.println(" Nome: " + this.resultSet.getString("nome")
						//+ " Score: " + this.resultSet.getString("score"));
				scoreList.add(Integer.parseInt(this.resultSet.getString("score")));
			}
		} catch(Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	public void insertPlayer(String nome, int score) {
		try {
			String query = "INSERT INTO PLAYER VALUES ('" + nome + "', '" + score + "')";
			//System.out.println(query);
			this.statement.executeUpdate(query);
		} catch(Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	
	
	public Statement getStatement() {
		return statement;
	}
	public void setStatement(Statement statement) {
		this.statement = statement;
	}
	public Connection getConnection() {
		return connection;
	}
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	public ResultSet getResultSet() {
		return resultSet;
	}
	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}
