package fr.tim.smpbank.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

public class SQLGetter {

	//private smpBank plugin;
	
	
	
	//public SQLGetter(smpBank plugin) {
		//this.plugin = plugin;
	//}
	
	
	
	public static void createTable() {
		PreparedStatement ps;
		try {
			ps = MySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS solde" +
					"(Pseudo VARCHAR(100),UUID VARCHAR(100),Solde DECIMAL(15,3),PRIMARY KEY (Pseudo))");
			
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	public static void createPlayer(Player player) {
		try {
			UUID uuid = player.getUniqueId();
			if (!exists(uuid)) {
				PreparedStatement ps2 = MySQL.getConnection().prepareStatement("INSERT IGNORE INTO solde" +
			"(Pseudo,UUID) VALUES (?,?)");
				ps2.setString(1,player.getName());
				ps2.setString(2,uuid.toString());
				ps2.executeUpdate();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	public static boolean exists(UUID uuid) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM solde WHERE UUID=?");
			ps.setString(1, uuid.toString());
			
			ResultSet results = ps.executeQuery();
			if (results.next() ) {
				//Joueur existe
				return true;
			}
			
			} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	
	public static void addPoints(UUID uuid, float points) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE solde SET Solde=? WHERE UUID=?");
			ps.setFloat(1,(getPoints(uuid)+points));
			ps.setString(2, uuid.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	public static float getPoints(UUID uuid) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Solde FROM solde WHERE UUID=?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			float points = 0;
			if (rs.next() ) {
				points = rs.getFloat("Solde");
				return points;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	
	public static List<UUID> getPlayers() {
		List<UUID> players =  new ArrayList<UUID>();
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT UUID FROM solde");
			ResultSet rs = ps.executeQuery();
			while (rs.next() ) {
				players.add(UUID.fromString(rs.getString("UUID")));
			}
			return players;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
		
		
	}
}
