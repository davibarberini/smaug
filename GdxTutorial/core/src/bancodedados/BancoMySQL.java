package bancodedados;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;

//import com.mysql.cj.jdbc.Driver;

import java.sql.ResultSet;

public class BancoMySQL {
	private Connection connection = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	
	public void connect() {
		String servidor = "jdbc:mysql://localhost:3306/offlife?useTimezone=true&serverTimezone=UTC";
		String usuario = "root";
		String senha = "78978541Da@";
		String driver = "com.mysql.cj.jdbc.Driver";
		try {
			Class.forName(driver);
			this.connection = DriverManager.getConnection(servidor, usuario, senha);
			this.statement = this.connection.createStatement();
			
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
	
	public void listPlayers(ArrayList<Integer> scoreList) {
		try {
			String query = "SELECT * FROM player ORDER BY nome";
			this.resultSet = this.statement.executeQuery(query);
			//this.statement = this.connection.createStatement();
			while(this.resultSet.next()) {
				System.out.println("ID: " + this.resultSet.getString("id") + " Nome: " + this.resultSet.getString("nome")
						+ " Score: " + this.resultSet.getString("score"));
				scoreList.add(Integer.parseInt(this.resultSet.getString("score")));
			}
		} catch(Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	public void insertPlayer(String nome, int score) {
		try {
			String query = "INSERT INTO player (nome, score) VALUES ('" + nome + "', '" + score + "');";
			System.out.println(query);
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
}
