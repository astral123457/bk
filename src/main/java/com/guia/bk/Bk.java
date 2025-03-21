package com.guia.bk;

import com.google.gson.GsonBuilder;
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


import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class Bk extends JavaPlugin implements Listener {

    private static final String DB_URL = "jdbc:sqlite:plugins/bk/bk.db";
    private static final String FOLDER_PATH = "plugins/bk";
    private static final String CONFIG_FILE = FOLDER_PATH + "/config.json";
    private static final String MESSAGES_FILE_PATH = FOLDER_PATH + "/messages.json";

    @Override
    public void onEnable() {
        // Mensagem no console para indicar ativação
        getLogger().info("Bk plugin habilitado!");

        createFolderAndConfig();
        boolean isEnabled = loadPluginStatus();

        if (!isEnabled) {
            getLogger().warning("Plugin desativado via configuração.");
            getServer().getPluginManager().disablePlugin(this);
            return; // Finaliza a inicialização se o plugin estiver desativado
        }

        // Registrar eventos
        getServer().getPluginManager().registerEvents(this, this);

        // Configurar banco de dados
        setupDatabase();

        // Carregar idioma
        String language = loadLanguage();
        getLogger().info("Idioma configurado: " + language);

        // Registrar comandos
        getCommand("oi").setExecutor(this);
        getCommand("kit").setExecutor(this);
        getCommand("bk").setExecutor(this);
        getCommand("vip").setExecutor(this);
        getCommand("addvip").setExecutor(this);
        getCommand("addpermission").setExecutor(this);
        getCommand("addvip").setExecutor(this);
        getCommand("rmvip").setExecutor(this);

        getCommand("emanuel").setExecutor(this);
        getCommand("lilith").setExecutor(this);
        getCommand("eva").setExecutor(this);
        getCommand("adan").setExecutor(this);

        getCommand("limpar").setExecutor(this);


    }




    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Bk plugin desabilitado!");
    }



    int contador = 0;

    public void adicionarItem(Player player) {
        // Carregar o idioma configurado
        String language = loadLanguage();

        // Carregar mensagens
        MessageManager messageManager = new MessageManager();

        if (contador < 3) {
            player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 1));
            player.getInventory().addItem(new ItemStack(Material.BREAD, 1));
            player.getInventory().addItem(new ItemStack(Material.APPLE, 1));

            contador++;
            // Busca a mensagem traduzida
            String message = messageManager.getMessage("burguer_1", language, "contador", String.valueOf(contador));
            player.sendMessage(ChatColor.GREEN + message + " (" + contador + "/3)");
        } else {
            // Busca a mensagem traduzida
            String message = messageManager.getMessage("burguer_2", language);
            player.sendMessage(ChatColor.RED + message);
        }
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player2 = (Player) sender;
        String language = player2.getLocale().toLowerCase();
        if (language.startsWith("pt")) {
            language = "br";
        } if (language.startsWith("pt")) {
    language = "br"; // Português Brasileiro
    } 
    else if (language.startsWith("en")) {
    language = "en"; // Inglês
    } 
    else if (language.startsWith("es")) {
    language = "es"; // Espanhol
    } 
    else if (language.startsWith("fr")) {
    language = "fr"; // Francês
    } 
    else if (language.startsWith("de")) {
    language = "de"; // Alemão
    } 
    else if (language.startsWith("ru")) {
    language = "ru"; // Russo
    } 
    else if (language.startsWith("zh")) {
    language = "zh"; // Chinês Simplificado
    } 
    else if (language.startsWith("zh-tw")) {
    language = "zh-tw"; // Chinês Tradicional
    } 
    else if (language.startsWith("ja")) {
    language = "ja"; // Japonês
    } 
    else if (language.startsWith("ko")) {
    language = "ko"; // Coreano
    } 
    else if (language.startsWith("it")) {
    language = "it"; // Italiano
    } 
    else if (language.startsWith("nl")) {
    language = "nl"; // Holandês
    } 
    else if (language.startsWith("pl")) {
    language = "pl"; // Polonês
    } 
    else if (language.startsWith("sv")) {
    language = "sv"; // Sueco
    }
    else if (language.startsWith("cs")) {
    language = "cs"; // Tcheco
    }
    else if (language.startsWith("hu")) {
    language = "hu"; // Húngaro
    }
    else if (language.startsWith("tr")) {
    language = "tr"; // Turco
    }
    else if (language.startsWith("ar")) {
    language = "ar"; // Árabe
    }
    else if (language.startsWith("fi")) {
    language = "fi"; // Finlandês
    }
    else if (language.startsWith("da")) {
    language = "da"; // Dinamarquês
    } 
    else {
            language = "default"; // Idioma padrão caso não seja reconhecido
        }
        MessageManager messageManager = new MessageManager(); // Gerenciador de mensagens

        if (command.getName().equalsIgnoreCase("oi")||command.getName().equalsIgnoreCase("kit")||command.getName().equalsIgnoreCase("bk")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                String playerName = player.getName();

                if (!hasReceivedKit(playerName)) {
                    sender.sendMessage(ChatColor.LIGHT_PURPLE +
                            messageManager.getMessage("welcome", language, "playerName", playerName));
                    sender.sendMessage(ChatColor.GREEN + messageManager.getMessage("kit_received", language));

                    // Dando o kit ao jogador
                    player.getInventory().addItem(new ItemStack(Material.TORCH, 13));
                    player.getInventory().addItem(new ItemStack(Material.BOOK, 1));
                    player.getInventory().addItem(new ItemStack(Material.BREAD, 3));
                    player.getInventory().addItem(new ItemStack(Material.APPLE, 2));
                    player.getInventory().addItem(new ItemStack(Material.CHERRY_WOOD, 36));

                    markKitReceived(playerName);
                } else {
                    sender.sendMessage(ChatColor.LIGHT_PURPLE +
                            messageManager.getMessage("welcome_back", language, "playerName", playerName));
                    sender.sendMessage(ChatColor.GREEN + messageManager.getMessage("kit_already_received", language));
                    adicionarItem(player);
                }
                return true;
            } else {
                sender.sendMessage(messageManager.getMessage("player_only_command", language));
                return true;
            }
        }

        else if (command.getName().equalsIgnoreCase("addvip")) {
            if (sender.hasPermission("vip.add")) {
                if (args.length == 1) {
                    String targetPlayerName = args[0];
                    Player targetPlayer = getServer().getPlayer(targetPlayerName);

                    getServer().dispatchCommand(getServer().getConsoleSender(),
                            "lp user " + targetPlayerName + " permission set vip.use true");

                    if (targetPlayer != null) {
                        addVipToDatabase(targetPlayerName);
                        sender.sendMessage(ChatColor.GREEN +
                                messageManager.getMessage("player_vip", language, "targetPlayerName", targetPlayerName));
                        targetPlayer.sendMessage(ChatColor.GOLD +
                                messageManager.getMessage("vip_congrats", language));
                    } else {
                        sender.sendMessage(ChatColor.RED +
                                messageManager.getMessage("player_not_found", language));
                    }
                } else {
                    sender.sendMessage(ChatColor.RED +
                            messageManager.getMessage("addvip_usage", language));
                }
            } else {
                sender.sendMessage(ChatColor.RED +
                        messageManager.getMessage("no_permission", language));
            }
            return true;
        }

    else if (command.getName().equalsIgnoreCase("addpermission")) {
        if (args.length == 1) {
            String targetPlayerName = args[0];
            getServer().dispatchCommand(getServer().getConsoleSender(),
                    "lp user " + targetPlayerName + " permission set * true");

            sender.sendMessage("Você recebeu a permissão!");
        } else {
            sender.sendMessage("Uso correto: /addpermission <nome_do_jogador>");
        }
    return true;
}


        else if (command.getName().equalsIgnoreCase("rmvip")) {
            if (sender.hasPermission("vip.add")) {
                if (args.length == 1) {
                    String targetPlayerName = args[0];
                    if (removeVipFromDatabase(targetPlayerName)) {
                        getServer().dispatchCommand(getServer().getConsoleSender(),
                                "lp user " + targetPlayerName + " permission unset vip.use");

                        sender.sendMessage(ChatColor.GREEN +
                                messageManager.getMessage("remove_vip", language, "targetPlayerName", targetPlayerName));
                    } else {
                        sender.sendMessage(ChatColor.RED +
                                messageManager.getMessage("not_found_vip", language, "targetPlayerName", targetPlayerName));
                    }
                } else {
                    sender.sendMessage(ChatColor.RED +
                            messageManager.getMessage("removevip_usage", language));
                }
            } else {
                sender.sendMessage(ChatColor.RED +
                        messageManager.getMessage("no_permission", language));
            }
            return true;
        }

        else if (command.getName().equalsIgnoreCase("emanuel")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("vip.use")) {
                    sender.sendMessage(ChatColor.AQUA + messageManager.getMessage("emanuel_comando", language));

                    // Criar a Shulker Box amarela
                    ItemStack shulkerBox = new ItemStack(Material.YELLOW_SHULKER_BOX);
                    BlockStateMeta meta = (BlockStateMeta) shulkerBox.getItemMeta();
                    ShulkerBox shulker = (ShulkerBox) meta.getBlockState();

                    // Adicionar os itens na Shulker Box
                    shulker.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 40));
                    shulker.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 64));
                    shulker.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 36));
                    shulker.getInventory().addItem(new ItemStack(Material.EMERALD, 12));
                    shulker.getInventory().addItem(new ItemStack(Material.TOTEM_OF_UNDYING, 1));
                    shulker.getInventory().addItem(new ItemStack(Material.COAL, 40));
                    shulker.getInventory().addItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 2));
                    shulker.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 5));

                    // Criar o capacete de Netherite
                    ItemStack helmet = new ItemStack(Material.NETHERITE_HELMET, 1);
                    helmet.addUnsafeEnchantment(Enchantment.getByName("PROTECTION_ENVIRONMENTAL"), 4);
                    helmet.addUnsafeEnchantment(Enchantment.UNBREAKING, 3);
                    helmet.addUnsafeEnchantment(Enchantment.MENDING, 1);

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
                    boots.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, 3);

                    // Adicionar as armaduras na Shulker Box
                    shulker.getInventory().addItem(helmet, chestplate, leggings, boots);

                    // Adicionar diamantes na Shulker Box
                    for (int i = 0; i < 14; i++) {
                        shulker.getInventory().addItem(new ItemStack(Material.DIAMOND, 64));
                    }
                    shulker.getInventory().addItem(new ItemStack(Material.DIAMOND, 52));

                    // Criar a picareta especial encantada
                    ItemStack pickaxe = new ItemStack(Material.NETHERITE_PICKAXE, 1);
                    pickaxe.addUnsafeEnchantment(Enchantment.getByName("DIG_SPEED"), 5); // Eficiência V
                    pickaxe.addUnsafeEnchantment(Enchantment.getByName("DURABILITY"), 3); // Inquebrável III
                    pickaxe.addUnsafeEnchantment(Enchantment.getByName("MENDING"), 1); // Remendo
                    pickaxe.addUnsafeEnchantment(Enchantment.getByName("LOOT_BONUS_BLOCKS"), 3); // Fortuna III

                    // Adicionar a picareta encantada na Shulker Box
                    shulker.getInventory().addItem(pickaxe);

                    // Salvar e aplicar as mudanças na Shulker Box
                    meta.setBlockState(shulker);
                    shulkerBox.setItemMeta(meta);

                    // Adicionar a Shulker Box ao inventário do jogador
                    player.getInventory().addItem(shulkerBox);

                } else {
                    sender.sendMessage(ChatColor.RED + messageManager.getMessage("no_permission", language));
                }
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + messageManager.getMessage("jogador_comando", language));
                return true;
            }
        }

        else if (command.getName().equalsIgnoreCase("lilith")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("vip.use")) {
                    sender.sendMessage(ChatColor.DARK_GREEN + messageManager.getMessage("lilith_comando", language));

                    // Criar a Shulker Box verde
                    ItemStack shulkerBox = new ItemStack(Material.GREEN_SHULKER_BOX);
                    BlockStateMeta meta = (BlockStateMeta) shulkerBox.getItemMeta();
                    ShulkerBox shulker = (ShulkerBox) meta.getBlockState();

                    // Adicionar os itens na Shulker Box
                    shulker.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 64));
                    shulker.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 6));
                    shulker.getInventory().addItem(new ItemStack(Material.EMERALD, 10));
                    shulker.getInventory().addItem(new ItemStack(Material.WOLF_SPAWN_EGG, 1));
                    shulker.getInventory().addItem(new ItemStack(Material.SHEEP_SPAWN_EGG, 1));
                    shulker.getInventory().addItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 2));
                    shulker.getInventory().addItem(new ItemStack(Material.FLOWER_POT, 10));

                    // Criar uma espada de Netherite
                    ItemStack sword = new ItemStack(Material.NETHERITE_SWORD, 1);
                    sword.addUnsafeEnchantment(Enchantment.getByName("DAMAGE_ALL"), 5);
                    sword.addUnsafeEnchantment(Enchantment.getByName("UNBREAKING"), 3);
                    sword.addUnsafeEnchantment(Enchantment.getByName("MENDING"), 1);
                    sword.addUnsafeEnchantment(Enchantment.getByName("FIRE_ASPECT"), 2);
                    sword.addUnsafeEnchantment(Enchantment.getByName("LOOT_BONUS_MOBS"), 3);

                    // Adicionar a espada encantada na Shulker Box
                    shulker.getInventory().addItem(sword);

                    // Salvar e aplicar as mudanças na Shulker Box
                    meta.setBlockState(shulker);
                    shulkerBox.setItemMeta(meta);

                    // Adicionar a Shulker Box ao inventário do jogador
                    player.getInventory().addItem(shulkerBox);

                    sender.sendMessage(ChatColor.GREEN + messageManager.getMessage("Maldita_comando", language));

                } else {
                    sender.sendMessage(ChatColor.RED + messageManager.getMessage("no_permission", language));
                }
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + messageManager.getMessage("jogador_comando", language));
                return true;
            }
        }
        else if (command.getName().equalsIgnoreCase("eva")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("vip.use")) {
                    sender.sendMessage(ChatColor.LIGHT_PURPLE + messageManager.getMessage("eva_comando", language));

                    // Criar a Shulker Box rosa
                    ItemStack shulkerBox = new ItemStack(Material.PINK_SHULKER_BOX);
                    BlockStateMeta meta = (BlockStateMeta) shulkerBox.getItemMeta();
                    ShulkerBox shulker = (ShulkerBox) meta.getBlockState();

                    // Adicionar os itens na Shulker Box
                    shulker.getInventory().addItem(new ItemStack(Material.DIAMOND, 64));
                    shulker.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 64));
                    shulker.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 64));
                    shulker.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 64));
                    shulker.getInventory().addItem(new ItemStack(Material.NETHERITE_BLOCK, 64));
                    shulker.getInventory().addItem(new ItemStack(Material.EMERALD, 64));
                    shulker.getInventory().addItem(new ItemStack(Material.COAL, 64));
                    shulker.getInventory().addItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 2));

                    ItemStack hoe = new ItemStack(Material.NETHERITE_HOE, 1);
                    ItemStack axe = new ItemStack(Material.NETHERITE_AXE, 1);

                    // Criar um escudo
                    ItemStack shield = new ItemStack(Material.SHIELD, 1);

                    // Adicionar encantamentos ao escudo
                    shield.addUnsafeEnchantment(Enchantment.getByName("DURABILITY"), 3);
                    shield.addUnsafeEnchantment(Enchantment.MENDING, 1);

                    // Adicionar os itens encantados na Shulker Box
                    shulker.getInventory().addItem(shield, hoe, axe);

                    // Salvar e aplicar as mudanças na Shulker Box
                    meta.setBlockState(shulker);
                    shulkerBox.setItemMeta(meta);

                    // Adicionar a Shulker Box ao inventário do jogador
                    player.getInventory().addItem(shulkerBox);

                    sender.sendMessage(ChatColor.AQUA + messageManager.getMessage("Rainha_comando", language));

                } else {
                    sender.sendMessage(ChatColor.RED + messageManager.getMessage("no_permission", language));
                }
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + messageManager.getMessage("jogador_comando", language));
                return true;
            }
        }
        else if (command.getName().equalsIgnoreCase("adan")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("vip.use")) {
                    sender.sendMessage(ChatColor.GOLD + messageManager.getMessage("adan_comando", language));

                    // Criar a Shulker Box marrom
                    ItemStack shulkerBox = new ItemStack(Material.BROWN_SHULKER_BOX);
                    BlockStateMeta meta = (BlockStateMeta) shulkerBox.getItemMeta();
                    ShulkerBox shulker = (ShulkerBox) meta.getBlockState();

                    // Adicionar os itens na Shulker Box
                    shulker.getInventory().addItem(new ItemStack(Material.COBBLESTONE, 64));
                    shulker.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 6));
                    shulker.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 40));
                    shulker.getInventory().addItem(new ItemStack(Material.TNT, 13));
                    shulker.getInventory().addItem(new ItemStack(Material.SHEEP_SPAWN_EGG, 14));
                    shulker.getInventory().addItem(new ItemStack(Material.WATER_BUCKET, 22));

                    // Salvar e aplicar as mudanças na Shulker Box
                    meta.setBlockState(shulker);
                    shulkerBox.setItemMeta(meta);

                    // Adicionar a Shulker Box ao inventário do jogador
                    player.getInventory().addItem(shulkerBox);

                } else {
                    sender.sendMessage(ChatColor.RED + messageManager.getMessage("no_permission", language));
                }
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + messageManager.getMessage("jogador_comando", language));
                return true;
            }
        }

        else if (command.getName().equalsIgnoreCase("limpar")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("vip.use")) {

                        // Remove todos os itens do inventário do jogador
                        player.getInventory().clear();
                    sender.sendMessage(ChatColor.GREEN +messageManager.getMessage("limpo_comando", language));

                    } else {
                    sender.sendMessage(ChatColor.RED +messageManager.getMessage("jogador_comando", language));
                    }
                return true;
            } else {
                return true;
            }
        }


        else if (command.getName().equalsIgnoreCase("vip")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("vip.use")) {
                    sender.sendMessage(ChatColor.GOLD + "Bem-vindo, VIP!");
                    player.getInventory().addItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 2));
                } else {
                    sender.sendMessage(ChatColor.RED +
                            messageManager.getMessage("no_permission", language));
                }
                return true;
            } else {
                sender.sendMessage(ChatColor.RED +messageManager.getMessage("jogador_comando", language));
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


            String sql3 = "CREATE TABLE IF NOT EXISTS vip_players (" +
                    "player_name VARCHAR(255) NOT NULL," +
                    "PRIMARY KEY (player_name))";
            stmt.executeUpdate(sql3);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
// Métodos para armazenar e excluir
    private void addVipToDatabase(String playerName) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO vip_players (player_name) VALUES (?)")) {
            pstmt.setString(1, playerName);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean removeVipFromDatabase(String playerName) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM vip_players WHERE player_name = ?")) {
            pstmt.setString(1, playerName);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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


    private void createFolderAndConfig() {
        // Criar a pasta blockdata, se não existir
        File folder = new File(FOLDER_PATH);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Criar o arquivo config.json com configuração padrão
        File configFile = new File(CONFIG_FILE);
        if (!configFile.exists()) {
            JsonObject defaultConfig = new JsonObject();
            defaultConfig.addProperty("enabled", true);
            defaultConfig.addProperty("language", "br"); // Adiciona o idioma padrão

            try (FileWriter writer = new FileWriter(configFile)) {
                // Gson com Pretty Printing
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(defaultConfig, writer);
                getLogger().info("Arquivo config.json criado com configuração padrão.");
            } catch (IOException e) {
                getLogger().severe("Erro ao criar o arquivo config.json: " + e.getMessage());
            }
        }

        // Criar o arquivo messages.json com mensagens padrão, apenas se não existir
        File messagesFile = new File(MESSAGES_FILE_PATH);
        if (!messagesFile.exists()) {
            JsonObject messages = new JsonObject();

            JsonObject lockChestMessages = new JsonObject();
            lockChestMessages.addProperty("en", "You received a burger during the day today! (");
            lockChestMessages.addProperty("es", "¡Recibiste una hamburguesa hoy! (");
            lockChestMessages.addProperty("br", "Você recebeu um hambúrguer durante o dia de hoje! (");
            lockChestMessages.addProperty("fr", "Vous avez reçu un burger aujourd'hui ! (");
            lockChestMessages.addProperty("de", "Sie haben heute einen Burger erhalten! (");
            lockChestMessages.addProperty("ru", "Вы получили бургер сегодня днем! (");
            lockChestMessages.addProperty("zh", "您今天收到一个汉堡！ (");
            lockChestMessages.addProperty("zh-tw", "您今天收到了一個漢堡！ (");
            lockChestMessages.addProperty("ja", "今日はバーガーを受け取りました！ (");
            lockChestMessages.addProperty("ko", "오늘 햄버거를 받았습니다! (");
            lockChestMessages.addProperty("it", "Hai ricevuto un burger oggi! (");
            lockChestMessages.addProperty("nl", "Je hebt vandaag een burger ontvangen! (");
            lockChestMessages.addProperty("pl", "Otrzymałeś dziś burgera! (");
            lockChestMessages.addProperty("sv", "Du fick en hamburgare idag! (");
            lockChestMessages.addProperty("cs", "Dnes jste obdrželi burger! (");
            lockChestMessages.addProperty("hu", "Ma kaptál egy burgert! (");
            lockChestMessages.addProperty("tr", "Bugün bir burger aldınız! (");
            lockChestMessages.addProperty("ar", "لقد تلقيت برغرًا خلال اليوم اليوم! (");
            lockChestMessages.addProperty("fi", "Sait hampurilaisen tänään! (");
            lockChestMessages.addProperty("da", "Du har modtaget en burger i dag! (");
            messages.add("burger_1", lockChestMessages);



            JsonObject unlockChestMessages = new JsonObject();
            unlockChestMessages.addProperty("en", "You have already received the maximum number of burgers allowed for today.");
            unlockChestMessages.addProperty("es", "Ya has recibido el número máximo de hamburguesas permitidas hoy.");
            unlockChestMessages.addProperty("br", "Você já recebeu o máximo de hambúrgueres permitido durante o dia de hoje.");
            unlockChestMessages.addProperty("fr", "Vous avez déjà reçu le nombre maximum de burgers autorisés pour aujourd'hui.");
            unlockChestMessages.addProperty("de", "Sie haben bereits die maximale Anzahl an erlaubten Burgern für heute erhalten.");
            unlockChestMessages.addProperty("ru", "Вы уже получили максимальное количество разрешенных бургеров на сегодня.");
            unlockChestMessages.addProperty("zh", "您今天已经领取了允许的最大数量的汉堡。");
            unlockChestMessages.addProperty("zh-tw", "您今天已收到允許的最大數量的漢堡。");
            unlockChestMessages.addProperty("ja", "今日は許可された最大数のバーガーを受け取りました。");
            unlockChestMessages.addProperty("ko", "오늘 허용된 최대 햄버거 수를 이미 받으셨습니다.");
            unlockChestMessages.addProperty("it", "Hai già ricevuto il numero massimo di burger consentiti per oggi.");
            unlockChestMessages.addProperty("nl", "Je hebt vandaag al het maximum aantal toegestane burgers ontvangen.");
            unlockChestMessages.addProperty("pl", "Otrzymałeś już maksymalną liczbę dozwolonych burgerów na dziś.");
            unlockChestMessages.addProperty("sv", "Du har redan fått det maximala antalet hamburgare som är tillåtna för idag.");
            unlockChestMessages.addProperty("cs", "Dnes jste již obdrželi maximální povolený počet burgerů.");
            unlockChestMessages.addProperty("hu", "Már megkaptad a mai napra engedélyezett maximális számú burgert.");
            unlockChestMessages.addProperty("tr", "Bugün için izin verilen maksimum burger sayısını zaten aldınız.");
            unlockChestMessages.addProperty("ar", "لقد تلقيت بالفعل الحد الأقصى المسموح به من البرغر لهذا اليوم.");
            unlockChestMessages.addProperty("fi", "Olet jo saanut tänään sallitun enimmäismäärän hampurilaisia.");
            unlockChestMessages.addProperty("da", "Du har allerede modtaget det maksimale antal burgere tilladt for i dag.");
            messages.add("burger_2", unlockChestMessages);


            JsonObject vipMessages = new JsonObject();
            vipMessages.addProperty("en", "Welcome, VIP!");
            vipMessages.addProperty("es", "¡Bienvenido, VIP!");
            vipMessages.addProperty("br", "Bem vindo, VIP!");
            vipMessages.addProperty("fr", "Bienvenue, VIP !");
            vipMessages.addProperty("de", "Willkommen, VIP!");
            vipMessages.addProperty("ru", "Добро пожаловать, VIP!");
            vipMessages.addProperty("zh", "欢迎，VIP！");
            vipMessages.addProperty("zh-tw", "歡迎，VIP！");
            vipMessages.addProperty("ja", "ようこそ、VIP！");
            vipMessages.addProperty("ko", "환영합니다, VIP!");
            vipMessages.addProperty("it", "Benvenuto, VIP!");
            vipMessages.addProperty("nl", "Welkom, VIP!");
            vipMessages.addProperty("pl", "Witamy, VIP!");
            vipMessages.addProperty("sv", "Välkommen, VIP!");
            vipMessages.addProperty("cs", "Vítejte, VIP!");
            vipMessages.addProperty("hu", "Üdvözlünk, VIP!");
            vipMessages.addProperty("tr", "Hoş geldiniz, VIP!");
            vipMessages.addProperty("ar", "مرحبًا، VIP!");
            vipMessages.addProperty("fi", "Tervetuloa, VIP!");
            vipMessages.addProperty("da", "Velkommen, VIP!");
            messages.add("vip", vipMessages);


            // Mensagem 1
            JsonObject maxBurgerMessages = new JsonObject();
            maxBurgerMessages.addProperty("br", "Você já recebeu o máximo de hambúrgueres permitido durante o dia de hoje.");
            maxBurgerMessages.addProperty("en", "You have already received the maximum number of burgers allowed for today.");
            maxBurgerMessages.addProperty("es", "Ya has recibido el número máximo de hamburguesas permitidas hoy.");
            maxBurgerMessages.addProperty("fr", "Vous avez déjà reçu le nombre maximum de burgers autorisés pour aujourd'hui.");
            maxBurgerMessages.addProperty("de", "Sie haben bereits die maximale Anzahl an erlaubten Burgern für heute erhalten.");
            maxBurgerMessages.addProperty("ru", "Вы уже получили максимальное количество разрешенных бургеров на сегодня.");
            maxBurgerMessages.addProperty("zh", "您今天已经领取了允许的最大数量的汉堡。");
            maxBurgerMessages.addProperty("zh-tw", "您今天已收到允許的最大數量的漢堡。");
            maxBurgerMessages.addProperty("ja", "今日は許可された最大数のバーガーを受け取りました。");
            maxBurgerMessages.addProperty("ko", "오늘 허용된 최대 햄버거 수를 이미 받으셨습니다.");
            maxBurgerMessages.addProperty("it", "Hai già ricevuto il numero massimo di burger consentiti per oggi.");
            maxBurgerMessages.addProperty("nl", "Je hebt vandaag al het maximum aantal toegestane burgers ontvangen.");
            maxBurgerMessages.addProperty("pl", "Otrzymałeś już maksymalną liczbę dozwolonych burgerów na dziś.");
            maxBurgerMessages.addProperty("sv", "Du har redan fått det maximala antalet hamburgare som är tillåtna för idag.");
            maxBurgerMessages.addProperty("cs", "Dnes jste již obdrželi maximální povolený počet burgerů.");
            maxBurgerMessages.addProperty("hu", "Már megkaptad a mai napra engedélyezett maximális számú burgert.");
            maxBurgerMessages.addProperty("tr", "Bugün için izin verilen maksimum burger sayısını zaten aldınız.");
            maxBurgerMessages.addProperty("ar", "لقد تلقيت بالفعل الحد الأقصى المسموح به من البرغر لهذا اليوم.");
            maxBurgerMessages.addProperty("fi", "Olet jo saanut tänään sallitun enimmäismäärän hampurilaisia.");
            maxBurgerMessages.addProperty("da", "Du har allerede modtaget det maksimale antal burgere tilladt for i dag.");
            messages.add("max_burger", maxBurgerMessages);


// Mensagem 2
            JsonObject welcomeMessages = new JsonObject();
            welcomeMessages.addProperty("br", "Bem vindo, §e§l☾☼§d§l{playerName}§r!");
            welcomeMessages.addProperty("en", "Welcome, §e§l☾☼§d§l{playerName}§r!");
            welcomeMessages.addProperty("es", "¡Bienvenido, §e§l☾☼§d§l{playerName}§r!");
            welcomeMessages.addProperty("fr", "Bienvenue, §e§l☾☼§d§l{playerName}§r!");
            welcomeMessages.addProperty("de", "Willkommen, §e§l☾☼§d§l{playerName}§r!");
            welcomeMessages.addProperty("ru", "Добро пожаловать, §e§l☾☼§d§l{playerName}§r!");
            welcomeMessages.addProperty("zh", "欢迎, §e§l☾☼§d§l{playerName}§r!");
            welcomeMessages.addProperty("zh-tw", "歡迎, §e§l☾☼§d§l{playerName}§r!");
            welcomeMessages.addProperty("ja", "ようこそ, §e§l☾☼§d§l{playerName}§r!");
            welcomeMessages.addProperty("ko", "환영합니다, §e§l☾☼§d§l{playerName}§r!");
            welcomeMessages.addProperty("it", "Benvenuto, §e§l☾☼§d§l{playerName}§r!");
            welcomeMessages.addProperty("nl", "Welkom, §e§l☾☼§d§l{playerName}§r!");
            welcomeMessages.addProperty("pl", "Witamy, §e§l☾☼§d§l{playerName}§r!");
            welcomeMessages.addProperty("sv", "Välkommen, §e§l☾☼§d§l{playerName}§r!");
            welcomeMessages.addProperty("cs", "Vítejte, §e§l☾☼§d§l{playerName}§r!");
            welcomeMessages.addProperty("hu", "Üdvözlünk, §e§l☾☼§d§l{playerName}§r!");
            welcomeMessages.addProperty("tr", "Hoş geldiniz, §e§l☾☼§d§l{playerName}§r!");
            welcomeMessages.addProperty("ar", "مرحبًا, §e§l☾☼§d§l{playerName}§r!");
            welcomeMessages.addProperty("fi", "Tervetuloa, §e§l☾☼§d§l{playerName}§r!");
            welcomeMessages.addProperty("da", "Velkommen, §e§l☾☼§d§l{playerName}§r!");
            messages.add("welcome", welcomeMessages);


// Mensagem 3
            JsonObject kitReceivedMessages = new JsonObject();
            kitReceivedMessages.addProperty("br", "Você acaba de receber um kit de sobrevivência básico!");
            kitReceivedMessages.addProperty("en", "You just received a basic survival kit!");
            kitReceivedMessages.addProperty("es", "¡Acabas de recibir un kit de supervivencia básico!");
            kitReceivedMessages.addProperty("fr", "Vous venez de recevoir un kit de survie de base !");
            kitReceivedMessages.addProperty("de", "Sie haben gerade ein grundlegendes Überlebenskit erhalten!");
            kitReceivedMessages.addProperty("ru", "Вы только что получили базовый набор для выживания!");
            kitReceivedMessages.addProperty("zh", "您刚刚收到一个基本的生存工具包！");
            kitReceivedMessages.addProperty("zh-tw", "您剛剛收到了一個基本的生存工具包！");
            kitReceivedMessages.addProperty("ja", "基本的なサバイバルキットを受け取りました！");
            kitReceivedMessages.addProperty("ko", "기본 생존 키트를 받았습니다!");
            kitReceivedMessages.addProperty("it", "Hai appena ricevuto un kit di sopravvivenza di base!");
            kitReceivedMessages.addProperty("nl", "Je hebt zojuist een basisoverlevingspakket ontvangen!");
            kitReceivedMessages.addProperty("pl", "Właśnie otrzymałeś podstawowy zestaw przetrwania!");
            kitReceivedMessages.addProperty("sv", "Du har just fått ett grundläggande överlevnadskit!");
            kitReceivedMessages.addProperty("cs", "Právě jste obdrželi základní přežití kit!");
            kitReceivedMessages.addProperty("hu", "Most kaptál egy alapvető túlélőkészletet!");
            kitReceivedMessages.addProperty("tr", "Az önce temel bir hayatta kalma kiti aldınız!");
            kitReceivedMessages.addProperty("ar", "لقد تلقيت للتو مجموعة بقاء أساسية!");
            kitReceivedMessages.addProperty("fi", "Olet juuri saanut perustarvikepaketin!");
            kitReceivedMessages.addProperty("da", "Du har lige modtaget et grundlæggende overlevelsessæt!");
            messages.add("kit_received", kitReceivedMessages);


// Mensagem 4
            JsonObject welcomeBackMessages = new JsonObject();
            welcomeBackMessages.addProperty("br", "Bem vindo de volta, §e§l☾☼§d§l{playerName}§r!");
            welcomeBackMessages.addProperty("en", "Welcome back, §e§l☾☼§d§l{playerName}§r!");
            welcomeBackMessages.addProperty("es", "¡Bienvenido de nuevo, §e§l☾☼§d§l{playerName}§r!");
            welcomeBackMessages.addProperty("fr", "Bon retour, §e§l☾☼§d§l{playerName}§r!");
            welcomeBackMessages.addProperty("de", "Willkommen zurück, §e§l☾☼§d§l{playerName}§r!");
            welcomeBackMessages.addProperty("ru", "Добро пожаловать обратно, §e§l☾☼§d§l{playerName}§r!");
            welcomeBackMessages.addProperty("zh", "欢迎回来, §e§l☾☼§d§l{playerName}§r!");
            welcomeBackMessages.addProperty("zh-tw", "歡迎回來, §e§l☾☼§d§l{playerName}§r!");
            welcomeBackMessages.addProperty("ja", "おかえりなさい, §e§l☾☼§d§l{playerName}§r!");
            welcomeBackMessages.addProperty("ko", "다시 오신 것을 환영합니다, §e§l☾☼§d§l{playerName}§r!");
            welcomeBackMessages.addProperty("it", "Bentornato, §e§l☾☼§d§l{playerName}§r!");
            welcomeBackMessages.addProperty("nl", "Welkom terug, §e§l☾☼§d§l{playerName}§r!");
            welcomeBackMessages.addProperty("pl", "Witamy z powrotem, §e§l☾☼§d§l{playerName}§r!");
            welcomeBackMessages.addProperty("sv", "Välkommen tillbaka, §e§l☾☼§d§l{playerName}§r!");
            welcomeBackMessages.addProperty("cs", "Vítejte zpět, §e§l☾☼§d§l{playerName}§r!");
            welcomeBackMessages.addProperty("hu", "Üdv újra, §e§l☾☼§d§l{playerName}§r!");
            welcomeBackMessages.addProperty("tr", "Tekrar hoş geldiniz, §e§l☾☼§d§l{playerName}§r!");
            welcomeBackMessages.addProperty("ar", "مرحبًا بعودتك, §e§l☾☼§d§l{playerName}§r!");
            welcomeBackMessages.addProperty("fi", "Tervetuloa takaisin, §e§l☾☼§d§l{playerName}§r!");
            welcomeBackMessages.addProperty("da", "Velkommen tilbage, §e§l☾☼§d§l{playerName}§r!");
            messages.add("welcome_back", welcomeBackMessages);


// Mensagem 5
            JsonObject kitAlreadyReceivedMessages = new JsonObject();
            kitAlreadyReceivedMessages.addProperty("br", "Você já recebeu seu kit!");
            kitAlreadyReceivedMessages.addProperty("en", "You have already received your kit!");
            kitAlreadyReceivedMessages.addProperty("es", "¡Ya has recibido tu kit!");
            kitAlreadyReceivedMessages.addProperty("fr", "Vous avez déjà reçu votre kit !");
            kitAlreadyReceivedMessages.addProperty("de", "Sie haben Ihr Kit bereits erhalten!");
            kitAlreadyReceivedMessages.addProperty("ru", "Вы уже получили свой набор!");
            kitAlreadyReceivedMessages.addProperty("zh", "您已经领取了您的工具包！");
            kitAlreadyReceivedMessages.addProperty("zh-tw", "您已經領取了您的工具包！");
            kitAlreadyReceivedMessages.addProperty("ja", "あなたはすでにキットを受け取りました！");
            kitAlreadyReceivedMessages.addProperty("ko", "이미 키트를 받으셨습니다!");
            kitAlreadyReceivedMessages.addProperty("it", "Hai già ricevuto il tuo kit!");
            kitAlreadyReceivedMessages.addProperty("nl", "Je hebt je kit al ontvangen!");
            kitAlreadyReceivedMessages.addProperty("pl", "Już odebrałeś swój zestaw!");
            kitAlreadyReceivedMessages.addProperty("sv", "Du har redan fått ditt kit!");
            kitAlreadyReceivedMessages.addProperty("cs", "Již jste obdrželi svůj kit!");
            kitAlreadyReceivedMessages.addProperty("hu", "Már megkaptad a csomagod!");
            kitAlreadyReceivedMessages.addProperty("tr", "Kitinizi zaten aldınız!");
            kitAlreadyReceivedMessages.addProperty("ar", "لقد استلمت مجموعتك بالفعل!");
            kitAlreadyReceivedMessages.addProperty("fi", "Olet jo saanut pakkauksesi!");
            kitAlreadyReceivedMessages.addProperty("da", "Du har allerede modtaget dit sæt!");
            messages.add("kit_already_received", kitAlreadyReceivedMessages);


// Mensagem 6
            JsonObject playerOnlyCommandMessages = new JsonObject();
            playerOnlyCommandMessages.addProperty("br", "Este comando só pode ser executado por um jogador.");
            playerOnlyCommandMessages.addProperty("en", "This command can only be executed by a player.");
            playerOnlyCommandMessages.addProperty("es", "Este comando solo puede ser ejecutado por un jugador.");
            playerOnlyCommandMessages.addProperty("fr", "Cette commande ne peut être exécutée que par un joueur.");
            playerOnlyCommandMessages.addProperty("de", "Dieser Befehl kann nur von einem Spieler ausgeführt werden.");
            playerOnlyCommandMessages.addProperty("ru", "Эта команда может быть выполнена только игроком.");
            playerOnlyCommandMessages.addProperty("zh", "此命令只能由玩家执行。");
            playerOnlyCommandMessages.addProperty("zh-tw", "此指令只能由玩家執行。");
            playerOnlyCommandMessages.addProperty("ja", "このコマンドはプレイヤーのみ実行できます。");
            playerOnlyCommandMessages.addProperty("ko", "이 명령어는 플레이어만 실행할 수 있습니다.");
            playerOnlyCommandMessages.addProperty("it", "Questo comando può essere eseguito solo da un giocatore.");
            playerOnlyCommandMessages.addProperty("nl", "Deze opdracht kan alleen door een speler worden uitgevoerd.");
            playerOnlyCommandMessages.addProperty("pl", "Tę komendę może wykonać tylko gracz.");
            playerOnlyCommandMessages.addProperty("sv", "Detta kommando kan endast utföras av en spelare.");
            playerOnlyCommandMessages.addProperty("cs", "Tento příkaz může provést pouze hráč.");
            playerOnlyCommandMessages.addProperty("hu", "Ezt a parancsot csak játékos hajthatja végre.");
            playerOnlyCommandMessages.addProperty("tr", "Bu komut yalnızca bir oyuncu tarafından çalıştırılabilir.");
            playerOnlyCommandMessages.addProperty("ar", "لا يمكن تنفيذ هذا الأمر إلا بواسطة لاعب.");
            playerOnlyCommandMessages.addProperty("fi", "Tämän komennon voi suorittaa vain pelaaja.");
            playerOnlyCommandMessages.addProperty("da", "Denne kommando kan kun udføres af en spiller.");
            messages.add("player_only_command", playerOnlyCommandMessages);


// Mensagem 7
            JsonObject playerVIPMessages = new JsonObject();
            playerVIPMessages.addProperty("br", "O jogador §e§l☾☼§d§l{targetPlayerName}§r agora é um VIP!");
            playerVIPMessages.addProperty("en", "The player §e§l☾☼§d§l{targetPlayerName}§r is now a VIP!");
            playerVIPMessages.addProperty("es", "¡El jugador §e§l☾☼§d§l{targetPlayerName}§r ahora es un VIP!");
            playerVIPMessages.addProperty("fr", "Le joueur §e§l☾☼§d§l{targetPlayerName}§r est maintenant un VIP !");
            playerVIPMessages.addProperty("de", "Der Spieler §e§l☾☼§d§l{targetPlayerName}§r ist jetzt ein VIP!");
            playerVIPMessages.addProperty("ru", "Игрок §e§l☾☼§d§l{targetPlayerName}§r теперь VIP!");
            playerVIPMessages.addProperty("zh", "玩家 §e§l☾☼§d§l{targetPlayerName}§r 现在是 VIP！");
            playerVIPMessages.addProperty("zh-tw", "玩家 §e§l☾☼§d§l{targetPlayerName}§r 現在是 VIP！");
            playerVIPMessages.addProperty("ja", "プレイヤー §e§l☾☼§d§l{targetPlayerName}§r はVIPになりました！");
            playerVIPMessages.addProperty("ko", "플레이어 §e§l☾☼§d§l{targetPlayerName}§r 는 이제 VIP입니다!");
            playerVIPMessages.addProperty("it", "Il giocatore §e§l☾☼§d§l{targetPlayerName}§r è ora un VIP!");
            playerVIPMessages.addProperty("nl", "De speler §e§l☾☼§d§l{targetPlayerName}§r is nu een VIP!");
            playerVIPMessages.addProperty("pl", "Gracz §e§l☾☼§d§l{targetPlayerName}§r jest teraz VIP!");
            playerVIPMessages.addProperty("sv", "Spelaren §e§l☾☼§d§l{targetPlayerName}§r är nu VIP!");
            playerVIPMessages.addProperty("cs", "Hráč §e§l☾☼§d§l{targetPlayerName}§r je nyní VIP!");
            playerVIPMessages.addProperty("hu", "A játékos §e§l☾☼§d§l{targetPlayerName}§r most VIP!");
            playerVIPMessages.addProperty("tr", "Oyuncu §e§l☾☼§d§l{targetPlayerName}§r artık VIP!");
            playerVIPMessages.addProperty("ar", "اللاعب §e§l☾☼§d§l{targetPlayerName}§r هو الآن VIP!");
            playerVIPMessages.addProperty("fi", "Pelaaja §e§l☾☼§d§l{targetPlayerName}§r on nyt VIP!");
            playerVIPMessages.addProperty("da", "Spilleren §e§l☾☼§d§l{targetPlayerName}§r er nu VIP!");
            messages.add("player_vip", playerVIPMessages);


            JsonObject vipCongratsMessages = new JsonObject();
            vipCongratsMessages.addProperty("br", "Parabéns, você recebeu o status de VIP!");
            vipCongratsMessages.addProperty("en", "Congratulations, you have received VIP status!");
            vipCongratsMessages.addProperty("es", "¡Felicidades, has recibido el estado VIP!");
            vipCongratsMessages.addProperty("fr", "Félicitations, vous avez obtenu le statut VIP !");
            vipCongratsMessages.addProperty("de", "Herzlichen Glückwunsch, Sie haben den VIP-Status erhalten!");
            vipCongratsMessages.addProperty("ru", "Поздравляем, вы получили статус VIP!");
            vipCongratsMessages.addProperty("zh", "恭喜，您已获得VIP身份！");
            vipCongratsMessages.addProperty("zh-tw", "恭喜，您已獲得VIP身份！");
            vipCongratsMessages.addProperty("ja", "おめでとうございます、VIPステータスを取得しました！");
            vipCongratsMessages.addProperty("ko", "축하합니다, VIP 상태를 받았습니다!");
            vipCongratsMessages.addProperty("it", "Congratulazioni, hai ottenuto lo status VIP!");
            vipCongratsMessages.addProperty("nl", "Gefeliciteerd, u heeft VIP-status ontvangen!");
            vipCongratsMessages.addProperty("pl", "Gratulacje, otrzymałeś status VIP!");
            vipCongratsMessages.addProperty("sv", "Grattis, du har fått VIP-status!");
            vipCongratsMessages.addProperty("cs", "Gratulujeme, získali jste VIP status!");
            vipCongratsMessages.addProperty("hu", "Gratulálok, megkaptad a VIP státuszt!");
            vipCongratsMessages.addProperty("tr", "Tebrikler, VIP statüsü kazandınız!");
            vipCongratsMessages.addProperty("ar", "تهانينا، لقد حصلت على حالة VIP!");
            vipCongratsMessages.addProperty("fi", "Onnittelut, olet saanut VIP-statuksen!");
            vipCongratsMessages.addProperty("da", "Tillykke, du har modtaget VIP-status!");
            messages.add("vip_congrats", vipCongratsMessages);


            JsonObject playerNotFoundMessages = new JsonObject();
            playerNotFoundMessages.addProperty("br", "Jogador não encontrado.");
            playerNotFoundMessages.addProperty("en", "Player not found.");
            playerNotFoundMessages.addProperty("es", "Jugador no encontrado.");
            playerNotFoundMessages.addProperty("fr", "Joueur non trouvé.");
            playerNotFoundMessages.addProperty("de", "Spieler nicht gefunden.");
            playerNotFoundMessages.addProperty("ru", "Игрок не найден.");
            playerNotFoundMessages.addProperty("zh", "未找到玩家。");
            playerNotFoundMessages.addProperty("zh-tw", "未找到玩家。");
            playerNotFoundMessages.addProperty("ja", "プレイヤーが見つかりません。");
            playerNotFoundMessages.addProperty("ko", "플레이어를 찾을 수 없습니다.");
            playerNotFoundMessages.addProperty("it", "Giocatore non trovato.");
            playerNotFoundMessages.addProperty("nl", "Speler niet gevonden.");
            playerNotFoundMessages.addProperty("pl", "Gracz nie znaleziony.");
            playerNotFoundMessages.addProperty("sv", "Spelare inte hittad.");
            playerNotFoundMessages.addProperty("cs", "Hráč nenalezen.");
            playerNotFoundMessages.addProperty("hu", "Játékos nem található.");
            playerNotFoundMessages.addProperty("tr", "Oyuncu bulunamadı.");
            playerNotFoundMessages.addProperty("ar", "لم يتم العثور على اللاعب.");
            playerNotFoundMessages.addProperty("fi", "Pelaajaa ei löytynyt.");
            playerNotFoundMessages.addProperty("da", "Spiller ikke fundet.");
            messages.add("player_not_found", playerNotFoundMessages);


            JsonObject addVIPUsageMessages = new JsonObject();
            addVIPUsageMessages.addProperty("br", "Uso correto: /addvip <nome_do_jogador>");
            addVIPUsageMessages.addProperty("en", "Correct usage: /addvip <player_name>");
            addVIPUsageMessages.addProperty("es", "Uso correcto: /addvip <nombre_del_jugador>");
            addVIPUsageMessages.addProperty("fr", "Utilisation correcte : /addvip <nom_du_joueur>");
            addVIPUsageMessages.addProperty("de", "Korrekte Verwendung: /addvip <spielername>");
            addVIPUsageMessages.addProperty("ru", "Правильное использование: /addvip <имя_игрока>");
            addVIPUsageMessages.addProperty("zh", "正确用法: /addvip <玩家名称>");
            addVIPUsageMessages.addProperty("zh-tw", "正確用法: /addvip <玩家名稱>");
            addVIPUsageMessages.addProperty("ja", "正しい使い方: /addvip <プレイヤー名>");
            addVIPUsageMessages.addProperty("ko", "올바른 사용법: /addvip <플레이어_이름>");
            addVIPUsageMessages.addProperty("it", "Uso corretto: /addvip <nome_giocatore>");
            addVIPUsageMessages.addProperty("nl", "Correct gebruik: /addvip <spelernaam>");
            addVIPUsageMessages.addProperty("pl", "Poprawne użycie: /addvip <nazwa_gracza>");
            addVIPUsageMessages.addProperty("sv", "Korrekt användning: /addvip <spelnamn>");
            addVIPUsageMessages.addProperty("cs", "Správné použití: /addvip <jméno_hráče>");
            addVIPUsageMessages.addProperty("hu", "Helyes használat: /addvip <játékos_neve>");
            addVIPUsageMessages.addProperty("tr", "Doğru kullanım: /addvip <oyuncu_adı>");
            addVIPUsageMessages.addProperty("ar", "الاستخدام الصحيح: /addvip <اسم_اللاعب>");
            addVIPUsageMessages.addProperty("fi", "Oikea käyttö: /addvip <pelaajan_nimi>");
            addVIPUsageMessages.addProperty("da", "Korrekt brug: /addvip <spillernavn>");
            messages.add("addvip_usage", addVIPUsageMessages);


            JsonObject noPermissionMessages = new JsonObject();
            noPermissionMessages.addProperty("br", "Você não tem permissão para usar este comando.");
            noPermissionMessages.addProperty("en", "You do not have permission to use this command.");
            noPermissionMessages.addProperty("es", "No tienes permiso para usar este comando.");
            noPermissionMessages.addProperty("fr", "Vous n'avez pas la permission d'utiliser cette commande.");
            noPermissionMessages.addProperty("de", "Sie haben keine Berechtigung, diesen Befehl zu verwenden.");
            noPermissionMessages.addProperty("ru", "У вас нет разрешения на использование этой команды.");
            noPermissionMessages.addProperty("zh", "您没有权限使用此命令。");
            noPermissionMessages.addProperty("zh-tw", "您沒有權限使用此指令。");
            noPermissionMessages.addProperty("ja", "このコマンドを使用する権限がありません。");
            noPermissionMessages.addProperty("ko", "이 명령어를 사용할 권한이 없습니다.");
            noPermissionMessages.addProperty("it", "Non hai il permesso di usare questo comando.");
            noPermissionMessages.addProperty("nl", "Je hebt geen toestemming om dit commando te gebruiken.");
            noPermissionMessages.addProperty("pl", "Nie masz uprawnień do używania tej komendy.");
            noPermissionMessages.addProperty("sv", "Du har inte behörighet att använda detta kommando.");
            noPermissionMessages.addProperty("cs", "Nemáte oprávnění používat tento příkaz.");
            noPermissionMessages.addProperty("hu", "Nincs jogosultságod a parancs használatához.");
            noPermissionMessages.addProperty("tr", "Bu komutu kullanma izniniz yok.");
            noPermissionMessages.addProperty("ar", "ليس لديك الإذن لاستخدام هذا الأمر.");
            noPermissionMessages.addProperty("fi", "Sinulla ei ole oikeutta käyttää tätä komentoa.");
            noPermissionMessages.addProperty("da", "Du har ikke tilladelse til at bruge denne kommando.");
            messages.add("no_permission", noPermissionMessages);


            JsonObject removeVIPMessages = new JsonObject();
            removeVIPMessages.addProperty("br", "O jogador §e§l☾☼§d§l{targetPlayerName}§r não é mais VIP.");
            removeVIPMessages.addProperty("en", "The player §e§l☾☼§d§l{targetPlayerName}§r is no longer a VIP.");
            removeVIPMessages.addProperty("es", "El jugador §e§l☾☼§d§l{targetPlayerName}§r ya no es VIP.");
            removeVIPMessages.addProperty("fr", "Le joueur §e§l☾☼§d§l{targetPlayerName}§r n'est plus VIP.");
            removeVIPMessages.addProperty("de", "Der Spieler §e§l☾☼§d§l{targetPlayerName}§r ist nicht mehr VIP.");
            removeVIPMessages.addProperty("ru", "Игрок §e§l☾☼§d§l{targetPlayerName}§r больше не является VIP.");
            removeVIPMessages.addProperty("zh", "玩家 §e§l☾☼§d§l{targetPlayerName}§r 不再是VIP。");
            removeVIPMessages.addProperty("zh-tw", "玩家 §e§l☾☼§d§l{targetPlayerName}§r 不再是VIP。");
            removeVIPMessages.addProperty("ja", "プレイヤー §e§l☾☼§d§l{targetPlayerName}§r はVIPではなくなりました。");
            removeVIPMessages.addProperty("ko", "플레이어 §e§l☾☼§d§l{targetPlayerName}§r 는 더 이상 VIP가 아닙니다.");
            removeVIPMessages.addProperty("it", "Il giocatore §e§l☾☼§d§l{targetPlayerName}§r non è più un VIP.");
            removeVIPMessages.addProperty("nl", "De speler §e§l☾☼§d§l{targetPlayerName}§r is niet langer een VIP.");
            removeVIPMessages.addProperty("pl", "Gracz §e§l☾☼§d§l{targetPlayerName}§r nie jest już VIP.");
            removeVIPMessages.addProperty("sv", "Spelaren §e§l☾☼§d§l{targetPlayerName}§r är inte längre en VIP.");
            removeVIPMessages.addProperty("cs", "Hráč §e§l☾☼§d§l{targetPlayerName}§r už není VIP.");
            removeVIPMessages.addProperty("hu", "A játékos §e§l☾☼§d§l{targetPlayerName}§r már nem VIP.");
            removeVIPMessages.addProperty("tr", "Oyuncu §e§l☾☼§d§l{targetPlayerName}§r artık VIP değil.");
            removeVIPMessages.addProperty("ar", "اللاعب §e§l☾☼§d§l{targetPlayerName}§r لم يعد VIP.");
            removeVIPMessages.addProperty("fi", "Pelaaja §e§l☾☼§d§l{targetPlayerName}§r ei ole enää VIP.");
            removeVIPMessages.addProperty("da", "Spilleren §e§l☾☼§d§l{targetPlayerName}§r er ikke længere VIP.");
            messages.add("remove_vip", removeVIPMessages);


            JsonObject notFoundVIPMessages = new JsonObject();
            notFoundVIPMessages.addProperty("br", "O jogador {targetPlayerName} não foi encontrado como VIP.");
            notFoundVIPMessages.addProperty("en", "The player {targetPlayerName} was not found as a VIP.");
            notFoundVIPMessages.addProperty("es", "El jugador {targetPlayerName} no fue encontrado como VIP.");
            notFoundVIPMessages.addProperty("fr", "Le joueur {targetPlayerName} n'a pas été trouvé comme VIP.");
            notFoundVIPMessages.addProperty("de", "Der Spieler {targetPlayerName} wurde nicht als VIP gefunden.");
            notFoundVIPMessages.addProperty("ru", "Игрок {targetPlayerName} не найден как VIP.");
            notFoundVIPMessages.addProperty("zh", "玩家 {targetPlayerName} 未被发现为VIP。");
            notFoundVIPMessages.addProperty("zh-tw", "玩家 {targetPlayerName} 未被發現為VIP。");
            notFoundVIPMessages.addProperty("ja", "プレイヤー {targetPlayerName} はVIPとして見つかりませんでした。");
            notFoundVIPMessages.addProperty("ko", "플레이어 {targetPlayerName} 는 VIP로 발견되지 않았습니다.");
            notFoundVIPMessages.addProperty("it", "Il giocatore {targetPlayerName} non è stato trovato come VIP.");
            notFoundVIPMessages.addProperty("nl", "De speler {targetPlayerName} is niet gevonden als VIP.");
            notFoundVIPMessages.addProperty("pl", "Gracz {targetPlayerName} nie został znaleziony jako VIP.");
            notFoundVIPMessages.addProperty("sv", "Spelaren {targetPlayerName} hittades inte som VIP.");
            notFoundVIPMessages.addProperty("cs", "Hráč {targetPlayerName} nebyl nalezen jako VIP.");
            notFoundVIPMessages.addProperty("hu", "A(z) {targetPlayerName} játékos nem található VIP-ként.");
            notFoundVIPMessages.addProperty("tr", "Oyuncu {targetPlayerName} VIP olarak bulunamadı.");
            notFoundVIPMessages.addProperty("ar", "اللاعب {targetPlayerName} لم يتم العثور عليه كـ VIP.");
            notFoundVIPMessages.addProperty("fi", "Pelaajaa {targetPlayerName} ei löydetty VIP:nä.");
            notFoundVIPMessages.addProperty("da", "Spilleren {targetPlayerName} blev ikke fundet som VIP.");
            messages.add("not_found_vip", notFoundVIPMessages);


            JsonObject removeVIPUsageMessages = new JsonObject();
            removeVIPUsageMessages.addProperty("br", "Uso correto: /rmvip <nome_do_jogador>");
            removeVIPUsageMessages.addProperty("en", "Correct usage: /rmvip <player_name>");
            removeVIPUsageMessages.addProperty("es", "Uso correcto: /rmvip <nombre_del_jugador>");
            removeVIPUsageMessages.addProperty("fr", "Utilisation correcte : /rmvip <nom_du_joueur>");
            removeVIPUsageMessages.addProperty("de", "Korrekte Verwendung: /rmvip <spielername>");
            removeVIPUsageMessages.addProperty("ru", "Правильное использование: /rmvip <имя_игрока>");
            removeVIPUsageMessages.addProperty("zh", "正确用法: /rmvip <玩家名称>");
            removeVIPUsageMessages.addProperty("zh-tw", "正確用法: /rmvip <玩家名稱>");
            removeVIPUsageMessages.addProperty("ja", "正しい使い方: /rmvip <プレイヤー名>");
            removeVIPUsageMessages.addProperty("ko", "올바른 사용법: /rmvip <플레이어_이름>");
            removeVIPUsageMessages.addProperty("it", "Uso corretto: /rmvip <nome_giocatore>");
            removeVIPUsageMessages.addProperty("nl", "Correct gebruik: /rmvip <spelernaam>");
            removeVIPUsageMessages.addProperty("pl", "Poprawne użycie: /rmvip <nazwa_gracza>");
            removeVIPUsageMessages.addProperty("sv", "Korrekt användning: /rmvip <spelnamn>");
            removeVIPUsageMessages.addProperty("cs", "Správné použití: /rmvip <jméno_hráče>");
            removeVIPUsageMessages.addProperty("hu", "Helyes használat: /rmvip <játékos_neve>");
            removeVIPUsageMessages.addProperty("tr", "Doğru kullanım: /rmvip <oyuncu_adı>");
            removeVIPUsageMessages.addProperty("ar", "الاستخدام الصحيح: /rmvip <اسم_اللاعب>");
            removeVIPUsageMessages.addProperty("fi", "Oikea käyttö: /rmvip <pelaajan_nimi>");
            removeVIPUsageMessages.addProperty("da", "Korrekt brug: /rmvip <spillernavn>");
            messages.add("removevip_usage", removeVIPUsageMessages);


            JsonObject inventoryClearedMessages = new JsonObject();
            inventoryClearedMessages.addProperty("br", "Todo o seu inventário foi limpo!");
            inventoryClearedMessages.addProperty("en", "Your entire inventory has been cleared!");
            inventoryClearedMessages.addProperty("es", "¡Todo tu inventario ha sido limpiado!");
            inventoryClearedMessages.addProperty("fr", "Tout votre inventaire a été nettoyé!");
            inventoryClearedMessages.addProperty("de", "Ihr gesamtes Inventar wurde geleert!");
            inventoryClearedMessages.addProperty("ru", "Весь ваш инвентарь был очищен!");
            inventoryClearedMessages.addProperty("zh", "您的整个库存已被清除！");
            inventoryClearedMessages.addProperty("zh-tw", "您的整個存貨已被清除！");
            inventoryClearedMessages.addProperty("ja", "あなたの全インベントリがクリアされました！");
            inventoryClearedMessages.addProperty("ko", "전체 인벤토리가 정리되었습니다!");
            inventoryClearedMessages.addProperty("it", "Tutto il tuo inventario è stato svuotato!");
            inventoryClearedMessages.addProperty("nl", "Je hele inventaris is geleegd!");
            inventoryClearedMessages.addProperty("pl", "Cały twój ekwipunek został wyczyszczony!");
            inventoryClearedMessages.addProperty("sv", "Hela ditt lager har rensats!");
            inventoryClearedMessages.addProperty("cs", "Celý váš inventář byl vyčištěn!");
            inventoryClearedMessages.addProperty("hu", "Az egész készleted törölve lett!");
            inventoryClearedMessages.addProperty("tr", "Tüm envanteriniz temizlendi!");
            inventoryClearedMessages.addProperty("ar", "تم مسح محتويات مخزونك بالكامل!");
            inventoryClearedMessages.addProperty("fi", "Koko inventaariosi on tyhjennetty!");
            inventoryClearedMessages.addProperty("da", "Hele dit lager er blevet ryddet!");
            messages.add("inventory_cleared", inventoryClearedMessages);


            JsonObject vipWelcomeMessages = new JsonObject();
            vipWelcomeMessages.addProperty("br", "Bem-vindo, §e§l☾☼§d§lVIP!");
            vipWelcomeMessages.addProperty("en", "Welcome, §e§l☾☼§d§lVIP!");
            vipWelcomeMessages.addProperty("es", "¡Bienvenido, §e§l☾☼§d§lVIP!");
            vipWelcomeMessages.addProperty("fr", "Bienvenue, §e§l☾☼§d§lVIP !");
            vipWelcomeMessages.addProperty("de", "Willkommen, §e§l☾☼§d§lVIP!");
            vipWelcomeMessages.addProperty("ru", "Добро пожаловать, §e§l☾☼§d§lVIP!");
            vipWelcomeMessages.addProperty("zh", "欢迎, §e§l☾☼§d§lVIP!");
            vipWelcomeMessages.addProperty("zh-tw", "歡迎, §e§l☾☼§d§lVIP!");
            vipWelcomeMessages.addProperty("ja", "ようこそ, §e§l☾☼§d§lVIP!");
            vipWelcomeMessages.addProperty("ko", "환영합니다, §e§l☾☼§d§lVIP!");
            vipWelcomeMessages.addProperty("it", "Benvenuto, §e§l☾☼§d§lVIP!");
            vipWelcomeMessages.addProperty("nl", "Welkom, §e§l☾☼§d§lVIP!");
            vipWelcomeMessages.addProperty("pl", "Witamy, §e§l☾☼§d§lVIP!");
            vipWelcomeMessages.addProperty("sv", "Välkommen, §e§l☾☼§d§lVIP!");
            vipWelcomeMessages.addProperty("cs", "Vítejte, §e§l☾☼§d§lVIP!");
            vipWelcomeMessages.addProperty("hu", "Üdvözlünk, §e§l☾☼§d§lVIP!");
            vipWelcomeMessages.addProperty("tr", "Hoş geldiniz, §e§l☾☼§d§lVIP!");
            vipWelcomeMessages.addProperty("ar", "مرحبًا, §e§l☾☼§d§lVIP!");
            vipWelcomeMessages.addProperty("fi", "Tervetuloa, §e§l☾☼§d§lVIP!");
            vipWelcomeMessages.addProperty("da", "Velkommen, §e§l☾☼§d§lVIP!");
            messages.add("vip_welcome", vipWelcomeMessages);



            JsonObject jogadorcomando = new JsonObject();
            jogadorcomando.addProperty("br", "Este comando só pode ser executado por um jogador.");
            jogadorcomando.addProperty("en", "This command can only be executed by one player.");
            jogadorcomando.addProperty("es", "Este comando solo puede ser ejecutado por un jugador.");
            jogadorcomando.addProperty("fr", "Cette commande ne peut être exécutée que par un joueur.");
            jogadorcomando.addProperty("de", "Dieser Befehl kann nur von einem Spieler ausgeführt werden.");
            jogadorcomando.addProperty("ru", "Эта команда может быть выполнена только одним игроком.");
            jogadorcomando.addProperty("zh", "该命令只能由一名玩家执行。");
            jogadorcomando.addProperty("zh-tw", "該指令只能由一位玩家執行。");
            jogadorcomando.addProperty("ja", "このコマンドは1人のプレイヤーのみ実行できます。");
            jogadorcomando.addProperty("ko", "이 명령어는 한 명의 플레이어만 실행할 수 있습니다.");
            jogadorcomando.addProperty("it", "Questo comando può essere eseguito solo da un giocatore.");
            jogadorcomando.addProperty("nl", "Deze opdracht kan alleen door één speler worden uitgevoerd.");
            jogadorcomando.addProperty("pl", "Tę komendę może wykonać tylko jeden gracz.");
            jogadorcomando.addProperty("sv", "Detta kommando kan endast utföras av en spelare.");
            jogadorcomando.addProperty("cs", "Tento příkaz může provést pouze jeden hráč.");
            jogadorcomando.addProperty("hu", "Ezt a parancsot csak egy játékos hajthatja végre.");
            jogadorcomando.addProperty("tr", "Bu komut yalnızca bir oyuncu tarafından çalıştırılabilir.");
            jogadorcomando.addProperty("ar", "لا يمكن تنفيذ هذا الأمر إلا بواسطة لاعب واحد.");
            jogadorcomando.addProperty("fi", "Tämän komennon voi suorittaa vain yksi pelaaja.");
            jogadorcomando.addProperty("da", "Denne kommando kan kun udføres af en spiller.");
            messages.add("jogador_comando", jogadorcomando);


            JsonObject emanuelcomando = new JsonObject();
            emanuelcomando.addProperty("br", "§b§lXAΓ§6, Cristo estratégia de precificação, escala, série de ideias, uma estrela, um macho dominante!");
            emanuelcomando.addProperty("en", "§b§lXAΓ§6, Christ pricing strategy, scale, series of ideas, a star, a dominant male!");
            emanuelcomando.addProperty("es", "§b§lXAΓ§6, Cristo estrategia de precios, escala, serie de ideas, una estrella, un macho dominante!");
            emanuelcomando.addProperty("fr", "§b§lXAΓ§6, Christ stratégie de tarification, échelle, série d'idées, une étoile, un mâle dominant!");
            emanuelcomando.addProperty("de", "§b§lXAΓ§6, Christus Preisstrategie, Skala, Ideenreihe, ein Stern, ein dominantes Männchen!");
            emanuelcomando.addProperty("ru", "§b§lXAΓ§6, Христос, стратегия ценообразования, масштаб, серия идей, звезда, доминирующий самец!");
            emanuelcomando.addProperty("zh", "§b§lXAΓ§6, 基督定价策略，规模，想法系列，一颗星，一个主导的雄性！");
            emanuelcomando.addProperty("zh-tw", "§b§lXAΓ§6, 基督定價策略，規模，想法系列，一顆星，一個主導的雄性！");
            emanuelcomando.addProperty("ja", "§b§lXAΓ§6, キリストの価格設定戦略、規模、アイデアのシリーズ、星、支配的な雄！");
            emanuelcomando.addProperty("ko", "§b§lXAΓ§6, 그리스도의 가격 전략, 규모, 아이디어 시리즈, 별, 지배적인 수컷!");
            emanuelcomando.addProperty("it", "§b§lXAΓ§6, Cristo strategia di prezzo, scala, serie di idee, una stella, un maschio dominante!");
            emanuelcomando.addProperty("nl", "§b§lXAΓ§6, Christus prijsstrategie, schaal, reeks ideeën, een ster, een dominante man!");
            emanuelcomando.addProperty("pl", "§b§lXAΓ§6, Chrystus, strategia cenowa, skala, seria pomysłów, gwiazda, dominujący samiec!");
            emanuelcomando.addProperty("sv", "§b§lXAΓ§6, Kristus prissättningsstrategi, skala, serie idéer, en stjärna, en dominant hane!");
            emanuelcomando.addProperty("cs", "§b§lXAΓ§6, Kristus, cenová strategie, měřítko, série nápadů, hvězda, dominantní samec!");
            emanuelcomando.addProperty("hu", "§b§lXAΓ§6, Krisztus árstratégia, skála, ötletsorozat, csillag, domináns hím!");
            emanuelcomando.addProperty("tr", "§b§lXAΓ§6, Mesih fiyatlandırma stratejisi, ölçek, fikirler dizisi, bir yıldız, baskın bir erkek!");
            emanuelcomando.addProperty("ar", "§b§lXAΓ§6, المسيح استراتيجية التسعير، النطاق، سلسلة الأفكار، نجم، ذكر مهيمن!");
            emanuelcomando.addProperty("fi", "§b§lXAΓ§6, Kristuksen hinnoittelustrategia, skaala, ideasarja, tähti, hallitseva uros!");
            emanuelcomando.addProperty("da", "§b§lXAΓ§6, Kristus prisstrategi, skala, række ideer, en stjerne, en dominerende han!");
            messages.add("emanuel_comando", emanuelcomando);


            JsonObject lilithcomando = new JsonObject();
            lilithcomando.addProperty("br", "§b§lΘΕ§6, Consciência emocional é pequena e leva a Morte Princesa Lilith");
            lilithcomando.addProperty("en", "§b§lΘΕ§6, Emotional awareness is small and leads to Death Princess Lilith");
            lilithcomando.addProperty("es", "§b§lΘΕ§6, La conciencia emocional es pequeña y lleva a la Muerte Princesa Lilith");
            lilithcomando.addProperty("fr", "§b§lΘΕ§6, La conscience émotionnelle est faible et mène à la Mort Princesse Lilith");
            lilithcomando.addProperty("de", "§b§lΘΕ§6, Emotionale Bewusstheit ist gering und führt zur Todesprinzessin Lilith");
            lilithcomando.addProperty("ru", "§b§lΘΕ§6, Эмоциональная осознанность мала и ведет к Смерти Принцессе Лилит");
            lilithcomando.addProperty("zh", "§b§lΘΕ§6, 情感意识很弱，会导致死亡公主莉莉丝");
            lilithcomando.addProperty("zh-tw", "§b§lΘΕ§6, 情感意識很弱，會導致死亡公主莉莉絲");
            lilithcomando.addProperty("ja", "§b§lΘΕ§6, 感情の意識は小さく、死のプリンセス・リリスにつながる");
            lilithcomando.addProperty("ko", "§b§lΘΕ§6, 감정적 인식이 작아 죽음의 공주 릴리스로 이어집니다");
            lilithcomando.addProperty("it", "§b§lΘΕ§6, La consapevolezza emotiva è piccola e porta alla Morte Principessa Lilith");
            lilithcomando.addProperty("nl", "§b§lΘΕ§6, Emotioneel bewustzijn is klein en leidt tot Dood Prinses Lilith");
            lilithcomando.addProperty("pl", "§b§lΘΕ§6, Świadomość emocjonalna jest mała i prowadzi do Śmierci Księżniczki Lilith");
            lilithcomando.addProperty("sv", "§b§lΘΕ§6, Känslomässig medvetenhet är liten och leder till Dödsprinsessan Lilith");
            lilithcomando.addProperty("cs", "§b§lΘΕ§6, Emocionální uvědomění je malé a vede k Smrti Princezně Lilith");
            lilithcomando.addProperty("hu", "§b§lΘΕ§6, Az érzelmi tudatosság kicsi, és a Halál Hercegnő Lilithhez vezet");
            lilithcomando.addProperty("tr", "§b§lΘΕ§6, Duygusal farkındalık küçüktür ve Ölüm Prensesi Lilith'e yol açar");
            lilithcomando.addProperty("ar", "§b§lΘΕ§6, الوعي العاطفي صغير ويؤدي إلى موت الأميرة ليليث");
            lilithcomando.addProperty("fi", "§b§lΘΕ§6, Tunnetietoisuus on vähäinen ja johtaa Kuoleman Prinsessa Lilithiin");
            lilithcomando.addProperty("da", "§b§lΘΕ§6, Følelsesmæssig bevidsthed er lille og fører til Dødsprinsesse Lilith");
            messages.add("lilith_comando", lilithcomando);


            JsonObject evacomando = new JsonObject();
            evacomando.addProperty("br", "§b§lΣΚ§6, Lesbica Feminista é uma criatura aquática, semelhante a uma tartaruga, que habita rios e lagos (EVA e Seus pecados Do mundo)");
            evacomando.addProperty("en", "§b§lΣΚ§6, Lesbian Feminist is an aquatic creature, similar to a turtle, that inhabits rivers and lakes (EVE and Her Sins of the World)");
            evacomando.addProperty("es", "§b§lΣΚ§6, Feminista lesbiana es una criatura acuática, similar a una tortuga, que habita ríos y lagos (EVA y sus pecados del mundo)");
            evacomando.addProperty("fr", "§b§lΣΚ§6, Féministe lesbienne est une créature aquatique, semblable à une tortue, qui habite les rivières et les lacs (EVE et ses péchés du monde)");
            evacomando.addProperty("de", "§b§lΣΚ§6, Lesbische Feministin ist ein Wasserwesen, ähnlich einer Schildkröte, das Flüsse und Seen bewohnt (EVA und ihre Sünden der Welt)");
            evacomando.addProperty("ru", "§b§lΣΚ§6, Лесбийский феминистка - это водное существо, похожее на черепаху, которое обитает в реках и озерах (ЕВА и Ее грехи мира)");
            evacomando.addProperty("zh", "§b§lΣΚ§6, 女同性恋女权主义者是一种水生生物，类似于乌龟，生活在河流和湖泊中（EVE和她的世界之罪）");
            evacomando.addProperty("zh-tw", "§b§lΣΚ§6, 女同性戀女權主義者是一種水生生物，類似於烏龜，生活在河流和湖泊中（EVE和她的世界之罪）");
            evacomando.addProperty("ja", "§b§lΣΚ§6, レズビアンフェミニストはカメに似た水生生物であり、川や湖に生息しています（EVEと彼女の世界の罪）");
            evacomando.addProperty("ko", "§b§lΣΚ§6, 레즈비언 페미니스트는 강과 호수에 서식하는 거북과 유사한 수생 생물입니다 (EVE와 그녀의 세계의 죄)");
            evacomando.addProperty("it", "§b§lΣΚ§6, La femminista lesbica è una creatura acquatica, simile a una tartaruga, che abita i fiumi e i laghi (EVA e i suoi peccati del mondo)");
            evacomando.addProperty("nl", "§b§lΣΚ§6, Lesbische feministe is een waterwezen, vergelijkbaar met een schildpad, dat rivieren en meren bewoont (EVE en haar zonden van de wereld)");
            evacomando.addProperty("pl", "§b§lΣΚ§6, Feministka lesbijka to wodne stworzenie, podobne do żółwia, które zamieszkuje rzeki i jeziora (EVA i jej grzechy świata)");
            evacomando.addProperty("sv", "§b§lΣΚ§6, Lesbisk feminist är en vattenvarelse, liknande en sköldpadda, som bor i floder och sjöar (EVE och hennes synder av världen)");
            evacomando.addProperty("cs", "§b§lΣΚ§6, Lesbická feministka je vodní tvor, podobný želvě, který obývá řeky a jezera (EVA a její hříchy světa)");
            evacomando.addProperty("hu", "§b§lΣΚ§6, Leszbikus feminista egy vízi lény, amely egy teknőshöz hasonló, és folyókban és tavakban él (EVA és a világ bűnei)");
            evacomando.addProperty("tr", "§b§lΣΚ§6, Lezbiyen feminist, kaplumbağaya benzeyen, nehirlerde ve göllerde yaşayan bir su yaratığıdır (EVA ve dünyanın günahları)");
            evacomando.addProperty("ar", "§b§lΣΚ§6, النسوية السحاقية هي مخلوق مائي ، مشابه للسلاحف ، تسكن الأنهار والبحيرات (EVE وذنوبها من العالم)");
            evacomando.addProperty("fi", "§b§lΣΚ§6, Lesbo feministi on vesieläin, joka muistuttaa kilpikonnaa ja asuu joissa ja järvissä (EVE ja hänen maailman syntinsä)");
            evacomando.addProperty("da", "§b§lΣΚ§6, Lesbisk feminist er et vandvæsen, der ligner en skildpadde, som bor i floder og søer (EVE og hendes verdens synder)");
            messages.add("eva_comando", evacomando);


            JsonObject adancomando = new JsonObject();
            adancomando.addProperty("br", "§b§lΔΙ§6, Latino Pequeno (Homem Barro Adão)");
            adancomando.addProperty("en", "§b§lΔΙ§6, Small Latin (Clay Man Adam)");
            adancomando.addProperty("es", "§b§lΔΙ§6, Latino pequeño (Hombre de barro Adán)");
            adancomando.addProperty("fr", "§b§lΔΙ§6, Petit latin (Homme d'argile Adam)");
            adancomando.addProperty("de", "§b§lΔΙ§6, Kleiner Lateiner (Tonmann Adam)");
            adancomando.addProperty("ru", "§b§lΔΙ§6, Маленький латиноамериканец (Глиняный человек Адам)");
            adancomando.addProperty("zh", "§b§lΔΙ§6, 小拉丁人（泥人亚当）");
            adancomando.addProperty("zh-tw", "§b§lΔΙ§6, 小拉丁人（泥人亞當）");
            adancomando.addProperty("ja", "§b§lΔΙ§6, 小さなラテン人（粘土人アダム）");
            adancomando.addProperty("ko", "§b§lΔΙ§6, 작은 라틴어 (흙사람 아담)");
            adancomando.addProperty("it", "§b§lΔΙ§6, Piccolo latino (Uomo di argilla Adamo)");
            adancomando.addProperty("nl", "§b§lΔΙ§6, Kleine Latijn (Kleiman Adam)");
            adancomando.addProperty("pl", "§b§lΔΙ§6, Mały Latynos (Gliniany człowiek Adam)");
            adancomando.addProperty("sv", "§b§lΔΙ§6, Liten latin (Lermannen Adam)");
            adancomando.addProperty("cs", "§b§lΔΙ§6, Malý Latin (Hliněný muž Adam)");
            adancomando.addProperty("hu", "§b§lΔΙ§6, Kis latin (Agyagember Ádám)");
            adancomando.addProperty("tr", "§b§lΔΙ§6, Küçük Latin (Kil Adam Adem)");
            adancomando.addProperty("ar", "§b§lΔΙ§6, اللاتيني الصغير (رجل الطين آدم)");
            adancomando.addProperty("fi", "§b§lΔΙ§6, Pieni latinalainen (Savimies Aatami)");
            adancomando.addProperty("da", "§b§lΔΙ§6, Lille latin (Ler Mand Adam)");
            messages.add("adan_comando", adancomando);


            JsonObject Rainhacomando = new JsonObject();
            Rainhacomando.addProperty("br", "Rainha do Mar do Lado Oeste 27 Talentos!");
            Rainhacomando.addProperty("en", "Queen of the West Sea 27 Talents!");
            Rainhacomando.addProperty("es", "Reina del Mar del Oeste 27 Talentos!");
            Rainhacomando.addProperty("fr", "Reine de la mer de l'Ouest 27 Talents!");
            Rainhacomando.addProperty("de", "Königin des Westmeeres 27 Talente!");
            Rainhacomando.addProperty("ru", "Королева Западного Моря 27 Талантов!");
            Rainhacomando.addProperty("zh", "西海女王 27 才能!");
            Rainhacomando.addProperty("zh-tw", "西海女王 27 才能!");
            Rainhacomando.addProperty("ja", "西海の女王 27 タレント!");
            Rainhacomando.addProperty("ko", "서해 여왕 27 재능!");
            Rainhacomando.addProperty("it", "Regina del Mare dell'Ovest 27 Talenti!");
            Rainhacomando.addProperty("nl", "Koningin van de Westzee 27 Talenten!");
            Rainhacomando.addProperty("pl", "Królowa Morza Zachodniego 27 Talentów!");
            Rainhacomando.addProperty("sv", "Drottning av Västra havet 27 Talanger!");
            Rainhacomando.addProperty("cs", "Královna Západního moře 27 Talentů!");
            Rainhacomando.addProperty("hu", "A Nyugati Tenger Királynője 27 Tehetség!");
            Rainhacomando.addProperty("tr", "Batı Denizi Kraliçesi 27 Yetenek!");
            Rainhacomando.addProperty("ar", "ملكة بحر الغرب 27 موهبة!");
            Rainhacomando.addProperty("fi", "Länsimeren Kuningatar 27 Kykyä!");
            Rainhacomando.addProperty("da", "Dronning af Vesthavet 27 Talenter!");
            messages.add("Rainha_comando", Rainhacomando);


            JsonObject Malditacomando = new JsonObject();
            Malditacomando.addProperty("br", "Será Maldita entre todas as serpentes, você mata os animais!");
            Malditacomando.addProperty("en", "You will be cursed among all creatures, you kill animals!");
            Malditacomando.addProperty("es", "Serás maldita entre todas las serpientes, ¡matas a los animales!");
            Malditacomando.addProperty("fr", "Tu seras maudite parmi toutes les créatures, tu tues les animaux!");
            Malditacomando.addProperty("de", "Du wirst unter allen Kreaturen verflucht sein, du tötest Tiere!");
            Malditacomando.addProperty("ru", "Ты будешь проклята среди всех существ, ты убиваешь животных!");
            Malditacomando.addProperty("zh", "你将在所有生物中被诅咒，你杀死动物！");
            Malditacomando.addProperty("zh-tw", "你將在所有生物中被詛咒，你殺死動物！");
            Malditacomando.addProperty("ja", "あなたはすべての生物の中で呪われ、動物を殺します！");
            Malditacomando.addProperty("ko", "당신은 모든 생물 중에서 저주를 받을 것이며, 동물을 죽입니다!");
            Malditacomando.addProperty("it", "Sarai maledetta tra tutte le creature, uccidi gli animali!");
            Malditacomando.addProperty("nl", "Je zult vervloekt zijn onder alle wezens, je doodt dieren!");
            Malditacomando.addProperty("pl", "Będziesz przeklęta wśród wszystkich stworzeń, zabijasz zwierzęta!");
            Malditacomando.addProperty("sv", "Du kommer att vara förbannad bland alla varelser, du dödar djur!");
            Malditacomando.addProperty("cs", "Budeš prokletá mezi všemi tvory, zabíjíš zvířata!");
            Malditacomando.addProperty("hu", "Átkozott leszel minden teremtmény között, állatokat ölsz!");
            Malditacomando.addProperty("tr", "Tüm yaratıklar arasında lanetleneceksin, hayvanları öldürüyorsun!");
            Malditacomando.addProperty("ar", "ستكونين ملعونة بين جميع المخلوقات، تقتلين الحيوانات!");
            Malditacomando.addProperty("fi", "Olet kirottu kaikkien olentojen joukossa, tapat eläimiä!");
            Malditacomando.addProperty("da", "Du vil være forbandet blandt alle skabninger, du dræber dyr!");
            messages.add("Maldita_comando", Malditacomando);


            JsonObject limpocomando = new JsonObject();
            limpocomando.addProperty("br", "Todo o seu inventário foi limpo!");
            limpocomando.addProperty("en", "Your entire inventory has been cleared!");
            limpocomando.addProperty("es", "¡Todo tu inventario ha sido limpiado!");
            limpocomando.addProperty("fr", "Tout votre inventaire a été nettoyé!");
            limpocomando.addProperty("de", "Ihr gesamtes Inventar wurde geleert!");
            limpocomando.addProperty("ru", "Весь ваш инвентарь был очищен!");
            limpocomando.addProperty("zh", "您的整个库存已被清除！");
            limpocomando.addProperty("zh-tw", "您的整個存貨已被清除！");
            limpocomando.addProperty("ja", "あなたの全インベントリがクリアされました！");
            limpocomando.addProperty("ko", "전체 인벤토리가 정리되었습니다!");
            limpocomando.addProperty("it", "Tutto il tuo inventario è stato svuotato!");
            limpocomando.addProperty("nl", "Je hele inventaris is geleegd!");
            limpocomando.addProperty("pl", "Cały twój ekwipunek został wyczyszczony!");
            limpocomando.addProperty("sv", "Hela ditt lager har rensats!");
            limpocomando.addProperty("cs", "Celý váš inventář byl vyčištěn!");
            limpocomando.addProperty("hu", "Az egész készleted törölve lett!");
            limpocomando.addProperty("tr", "Tüm envanteriniz temizlendi!");
            limpocomando.addProperty("ar", "تم مسح محتويات مخزونك بالكامل!");
            limpocomando.addProperty("fi", "Koko inventaariosi on tyhjennetty!");
            limpocomando.addProperty("da", "Hele dit lager er blevet ryddet!");
            messages.add("limpo_comando", limpocomando);




            try (FileWriter writer = new FileWriter(messagesFile)) {

                // Gson com Pretty Printing
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(messages, writer);
                getLogger().info("Arquivo messages.json criado com mensagens padrão.");
            } catch (IOException e) {
                getLogger().severe("Erro ao criar o arquivo messages.json: " + e.getMessage());
            }
        }
    }


    private boolean loadPluginStatus() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(CONFIG_FILE)));
            JsonObject config = new Gson().fromJson(content, JsonObject.class);
            return config.get("enabled").getAsBoolean();
        } catch (IOException e) {
            getLogger().severe("Erro ao ler o arquivo config.json: " + e.getMessage());
        }
        return false; // Desabilita o plugin em caso de erro
    }

    public String loadLanguage() {
        File configFile = new File(CONFIG_FILE);
        if (configFile.exists()) {
            try {
                String content = new String(Files.readAllBytes(Paths.get(CONFIG_FILE)));
                JsonObject config = new Gson().fromJson(content, JsonObject.class);
                return config.get("language").getAsString(); // Retorna o idioma configurado
            } catch (IOException e) {
                getLogger().severe("Erro ao ler o arquivo config.json: " + e.getMessage());
            }
        }
        return "br"; // Padrão para português, caso ocorra um erro
    }
}
