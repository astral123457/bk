package com.guia.bk;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;


import org.bukkit.block.ShulkerBox;

import org.bukkit.enchantments.Enchantment;


import org.bukkit.inventory.meta.BlockStateMeta;


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
        }

        else if (command.getName().equalsIgnoreCase("Emanuel")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("Emanuel.use")) {
                    sender.sendMessage(ChatColor.GOLD + "XAΓ, Cristo  estratégia de precificação, escala, série de ideias,  uma estrela, um macho dominante!");
                    // Adicionando itens VIP ao jogador

                    player.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 40));
                    player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 64));
                    player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 36));
                    player.getInventory().addItem(new ItemStack(Material.EMERALD, 12));
                    player.getInventory().addItem(new ItemStack(Material.TOTEM_OF_UNDYING, 1));//Sepulcral
                    player.getInventory().addItem(new ItemStack(Material.COAL, 40));//Covodos
                    player.getInventory().addItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 2));
                    player.getInventory().addItem(new ItemStack(Material.WATER_BUCKET, 42)); // 42 Baldes de Água
                    player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 5)); // + 5 GOLD LINGOT


                    // Criar o capacete de Netherite
                    ItemStack helmet = new ItemStack(Material.NETHERITE_HELMET, 1);
                    helmet.addUnsafeEnchantment(Enchantment.getByName("PROTECTION_ENVIRONMENTAL"), 4);
                    helmet.addUnsafeEnchantment(Enchantment.UNBREAKING, 3); // Inquebrável III
                    helmet.addUnsafeEnchantment(Enchantment.MENDING, 1); // Remendo

                    // Criar outras peças de armadura de Netherite
                    ItemStack chestplate = new ItemStack(Material.NETHERITE_CHESTPLATE, 1);
                    chestplate.addUnsafeEnchantment(Enchantment.getByName("PROTECTION_ENVIRONMENTAL"), 4);
                    chestplate.addUnsafeEnchantment(Enchantment.UNBREAKING, 3);
                    chestplate.addUnsafeEnchantment(Enchantment.MENDING, 1);

                    ItemStack leggings = new ItemStack(Material.NETHERITE_LEGGINGS, 1);
                    leggings.addUnsafeEnchantment(Enchantment.getByName("PROTECTION_ENVIRONMENTAL"), 4);
                    leggings.addUnsafeEnchantment(Enchantment.UNBREAKING, 3);
                    leggings.addUnsafeEnchantment(Enchantment.MENDING, 1);

                    ItemStack boots = new ItemStack(Material.NETHERITE_BOOTS, 1);
                    boots.addUnsafeEnchantment(Enchantment.getByName("PROTECTION_ENVIRONMENTAL"), 4);
                    boots.addUnsafeEnchantment(Enchantment.UNBREAKING, 3);
                    boots.addUnsafeEnchantment(Enchantment.MENDING, 1);
                    boots.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, 3); // Passos Profundos III

                    // Adicionar os itens ao inventário do jogador
                    player.getInventory().addItem(helmet, chestplate, leggings, boots);

                    player.sendMessage("Você recebeu uma armadura completa de Netherite encantada!");


                    // Criar a Shulker Box cheia de diamantes
                    ItemStack shulkerBox = new ItemStack(Material.SHULKER_BOX);
                    BlockStateMeta meta = (BlockStateMeta) shulkerBox.getItemMeta();
                    ShulkerBox shulker = (ShulkerBox) meta.getBlockState();

                    // Adicionar 900 diamantes na Shulker Box
                    for (int i = 0; i < 14; i++) { // 14 stacks de 64
                        shulker.getInventory().addItem(new ItemStack(Material.DIAMOND, 64));
                    }
                    shulker.getInventory().addItem(new ItemStack(Material.DIAMOND, 52)); // Resto (52)

                    // Salvar e aplicar as mudanças na Shulker Box
                    meta.setBlockState(shulker);
                    shulkerBox.setItemMeta(meta);

                    // Adicionar a Shulker Box ao inventário do jogador
                    player.getInventory().addItem(shulkerBox);

                    player.sendMessage(ChatColor.GOLD + "Você recebeu uma Shulker Box cheia de 900 diamantes!");



                } else {
                    sender.sendMessage(ChatColor.RED + "Você não tem permissão para usar este comando.");
                }
                return true;
            } else {
                sender.sendMessage("Este comando só pode ser executado por um jogador.");
                return true;
            }
        }

        else if (command.getName().equalsIgnoreCase("Lilith")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("Lilith.use")) {
                    sender.sendMessage(ChatColor.GOLD + "ΘΕ, Consciência emocional é pequena e leva a Morte Princesa Lilith");
                    // Adicionando itens VIP ao jogador

                    player.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 64));
                    player.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 6));// Prata
                    player.getInventory().addItem(new ItemStack(Material.EMERALD, 10));
                    player.getInventory().addItem(new ItemStack(Material.WOLF_SPAWN_EGG, 1)); // Ovo de Lobo
                    player.getInventory().addItem(new ItemStack(Material.SHEEP_SPAWN_EGG, 1)); // Ovo de Ovelha
                    player.getInventory().addItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 2));
                    player.getInventory().addItem(new ItemStack(Material.FLOWER_POT, 10)); // 10 vasos de flores

                    // Criar uma espada de Netherite
                    ItemStack sword = new ItemStack(Material.NETHERITE_SWORD, 1);

                    sword.addUnsafeEnchantment(Enchantment.getByName("DAMAGE_ALL"), 5); // Afiado V
                    sword.addUnsafeEnchantment(Enchantment.getByName("UNBREAKING"), 3); // Inquebrável III
                    sword.addUnsafeEnchantment(Enchantment.getByName("MENDING"), 1); // Remendo
                    sword.addUnsafeEnchantment(Enchantment.getByName("FIRE_ASPECT"), 2); // Aspecto Flamejante II
                    sword.addUnsafeEnchantment(Enchantment.getByName("LOOT_BONUS_MOBS"), 3); // Saque III
                    player.getInventory().addItem(sword);

                    player.sendMessage(ChatColor.GREEN + "Sera Maldita ente todas sempentes voce mata os amimais!");

                } else {
                    sender.sendMessage(ChatColor.RED + "Você não tem permissão para usar este comando.");
                }
                return true;
            } else {
                sender.sendMessage("Este comando só pode ser executado por um jogador.");
                return true;
            }
        }
        else if (command.getName().equalsIgnoreCase("Eva")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("Eva.use")) {
                    sender.sendMessage(ChatColor.GOLD + "ΣΚ, Lesbica Feminista é uma criatura aquática, semelhante a uma tartaruga, que habita rios e lagos ( EVA e Seus pecados Do mundo)");
                    // Adicionando itens VIP ao jogador
                    player.getInventory().addItem(new ItemStack(Material.DIAMOND, 64));// Rainha do Mar do Lado Oeste 27 Talentos
                    player.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 64));
                    player.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 64));

                    player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 64));
                    player.getInventory().addItem(new ItemStack(Material.NETHERITE_BLOCK, 64));
                    player.getInventory().addItem(new ItemStack(Material.EMERALD, 64));
                    player.getInventory().addItem(new ItemStack(Material.COAL, 64));
                    player.getInventory().addItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 2));


                    ItemStack hoe = new ItemStack(Material.NETHERITE_HOE, 1);
                    ItemStack axe = new ItemStack(Material.NETHERITE_AXE, 1);

                    // Criar um escudo
                    ItemStack shield = new ItemStack(Material.SHIELD, 1);

                    // Adicionar encantamentos ao escud
                    shield.addUnsafeEnchantment(Enchantment.getByName("DURABILITY"), 3); // Inquebrável III
                    shield.addUnsafeEnchantment(Enchantment.MENDING, 1); // Remendo

                    // Adicionar o escudo ao inventário do jogador
                    player.getInventory().addItem(shield,hoe,axe);

                    player.sendMessage(ChatColor.GREEN + "Rainha do Mar do Lado Oeste 27 Talentos!");

                } else {
                    sender.sendMessage(ChatColor.RED + "Você não tem permissão para usar este comando.");
                }
                return true;
            } else {
                sender.sendMessage("Este comando só pode ser executado por um jogador.");
                return true;
            }
        }
        else if (command.getName().equalsIgnoreCase("Adan")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("Adan.use")) {
                    sender.sendMessage(ChatColor.GOLD + "ΔΙ, LAtino Pequeno (Homen Barro Adao)");
                    // Adicionando itens VIP ao jogador
                    player.getInventory().addItem(new ItemStack(Material.COBBLESTONE, 64)); // 1 Esporao de 64 Rochas (Pedregulho)
                    player.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 6));// Prata
                    player.getInventory().addItem(new ItemStack(Material.WATER_BUCKET, 22)); // 22 Baldes de Água
                    player.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 40));// Prata
                    player.getInventory().addItem(new ItemStack(Material.TNT, 13)); // 13 blocos de TNT
                    player.getInventory().addItem(new ItemStack(Material.SHEEP_SPAWN_EGG, 14)); // 14 Talentos do Norte do Friu bicho peludinho ovos de ovelhas



                } else {
                    sender.sendMessage(ChatColor.RED + "Você não tem permissão para usar este comando.");
                }
                return true;
            } else {
                sender.sendMessage("Este comando só pode ser executado por um jogador.");
                return true;
            }
        }

        else if (command.getName().equalsIgnoreCase("vip")) {
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
