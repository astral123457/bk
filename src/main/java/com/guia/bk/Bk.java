package com.guia.bk;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

public final class Bk extends JavaPlugin implements Listener {

    private static final String DB_URL = "jdbc:sqlite:minecraft_spigot.db";

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Bk plugin habilitado!");
        getServer().getPluginManager().registerEvents(this, this);
        setupDatabase();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Bk plugin desabilitado!");
    }



    int contador = 0;

    public void adicionarItem(Player player) {
        if (contador < 3) {
            player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 1));
            contador++;
            player.sendMessage(ChatColor.GREEN + "Você recebeu um hambúrguer durante o dia de hoje! (" + contador + "/3)");
        } else {
            player.sendMessage(ChatColor.RED + "Você já recebeu o máximo de hambúrgueres permitido durante o dia de hoje.");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("oi")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                String playerName = player.getName();

                if (!hasReceivedKit(playerName)) {
                    sender.sendMessage(ChatColor.LIGHT_PURPLE + "Bem vindo, " + playerName + "!");
                    sender.sendMessage(ChatColor.GREEN + "Você acaba de receber um kit de sobrevivência básico!");

                    // Dando o kit ao jogador
                    player.getInventory().addItem(new ItemStack(Material.TORCH, 1));
                    player.getInventory().addItem(new ItemStack(Material.IRON_PICKAXE, 1));
                    player.getInventory().addItem(new ItemStack(Material.IRON_SWORD, 1));
                    player.getInventory().addItem(new ItemStack(Material.BOOK, 1));
                    player.getInventory().addItem(new ItemStack(Material.DIAMOND, 2));
                    player.getInventory().addItem(new ItemStack(Material.OBSIDIAN, 3));
                    player.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 5));
                    player.getInventory().addItem(new ItemStack(Material.EMERALD, 26));


                    markKitReceived(playerName);
                } else {
                    sender.sendMessage(ChatColor.LIGHT_PURPLE + "Bem vindo de volta, " + playerName + "!");
                    sender.sendMessage(ChatColor.GREEN + "Você já recebeu seu kit. Aqui está um Livro como presente de retorno!");
                    player.getInventory().addItem(new ItemStack(Material.BOOK, 1));
                    adicionarItem(player);
                }

                return true;
            } else {
                sender.sendMessage("Este comando só pode ser executado por um jogador.");
                return true;
            }
        } else if (command.getName().equalsIgnoreCase("vip")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("vip.use")) {
                    sender.sendMessage(ChatColor.GOLD + "Bem-vindo, VIP!");
                    // Adicionando itens VIP ao jogador
                    player.getInventory().addItem(new ItemStack(Material.DIAMOND, 64));
                    player.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 64));
                    player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 64));
                    player.getInventory().addItem(new ItemStack(Material.NETHERITE_BLOCK, 64));
                    player.getInventory().addItem(new ItemStack(Material.EMERALD, 64));
                    player.getInventory().addItem(new ItemStack(Material.COAL, 64));
                    player.getInventory().addItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 2));
                } else {
                    sender.sendMessage(ChatColor.RED + "Você não tem permissão para usar este comando.");
                }
                return true;
            } else {
                sender.sendMessage("Este comando só pode ser executado por um jogador.");
                return true;
            }
        }
        return false;
    }

    private void setupDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS kit_log (" +
                    "player_name VARCHAR(255) NOT NULL," +
                    "PRIMARY KEY (player_name))";
            stmt.executeUpdate(sql);

            String sql2 = "CREATE TABLE IF NOT EXISTS death_locations (" +
                    "player_id VARCHAR(36) NOT NULL," +
                    "world VARCHAR(255) NOT NULL," +
                    "x DOUBLE NOT NULL," +
                    "y DOUBLE NOT NULL," +
                    "z DOUBLE NOT NULL," +
                    "yaw FLOAT NOT NULL," +
                    "pitch FLOAT NOT NULL," +
                    "PRIMARY KEY (player_id))";
            stmt.executeUpdate(sql2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    private void removeDeathLocation(UUID playerId) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM death_locations WHERE player_id = ?")) {
            pstmt.setString(1, playerId.toString());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean hasReceivedKit(String playerName) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("SELECT player_name FROM kit_log WHERE player_name = ?")) {
            pstmt.setString(1, playerName);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void markKitReceived(String playerName) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO kit_log (player_name) VALUES (?)")) {
            pstmt.setString(1, playerName);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
