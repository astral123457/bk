# BK – Basic Food Kit Plugin for Minecraft
Enhance the early-game experience in Minecraft with BK Kit, a plugin designed to provide players with essential food resources upon starting their journey.
Language Support:
Supported languages: BR, EN, ES, FR, DE.
Functionality Overview:
onEnable():
- Initializes the plugin, loads configurations, registers events and commands.
- Checks if the plugin is enabled—if not, it automatically deactivates.
onDisable():
- Disables the plugin when necessary.
- Adds food items to the player's inventory (limited to 3 times).
- Performs permission and argument checks.
- Executes different actions based on the command entered.
- Identifies whether the command was executed by a player or the console.
- Manages player permissions and VIP kits.
- Handles VIP additions and removals.
- Allows players to use /limpar to clear their inventory.
Dependencies:
This plugin requires LuckPerms-Bukkit-5.4.156 for proper permission handling.

Dependencia LuckPerms-Bukkit-5.4.156 https://luckperms.net/

         Command: /oi
                  /vip
                  /addvip <name>
                  /rmvip <name>
                  /lilith (kit Bruxa)
                  /emanuel (kit stive Apocaliptico)
                  /eva (kit rainha do mar)
                  /adan (kit escravo da terra)
                  /limpar (limpar o inventário.)

![image](https://github.com/user-attachments/assets/53654929-c8c6-496d-8387-777ece188616)


# Testament of (Z) or (D) Z.eus to the Four

XAΓ – The Chi Rho symbol is formed by combining the first two letters of the Greek word Christos, meaning "Christ." It represents a pricing strategy, scaling, a series of ideas, a star, and a dominant male figure.

ΘΕ – Emotional consciousness is small and leads to death (LILITH).

ΔΙ – Latino "Small" (Adam, the clay man).

ΣΚ – Lesbian feminist is an aquatic creature, similar to a turtle, inhabiting rivers and lakes (EVA and her sins of the world).

![image](https://github.com/user-attachments/assets/5a6795d7-de8a-4000-8003-b51297295103)

# Fluxo Grama 

           a ideia e ser 4 presentes para um vip ou um moderador poder dar a alguem se quise...

```mermaid
graph TD;
    Inicializa-->onEnable;
    Inicializa-->onDisable;
    onEnable-->adicionarItem;
    onDisable-->Fim;
    adicionarItem-->vips;
    adicionarItem-->IF_verifica_ja_recebeu;
    vips-->Add;
    vips-->Rm;
    IF_verifica_ja_recebeu-->kit_inicial;
    IF_verifica_ja_recebeu-->Kit_basico_com_limite_de_3_ao_dia;
    kit_inicial-->Fim;
    Kit_basico_com_limite_de_3_ao_dia-->Fim;
    Add-->adicionarItemVIP;
    adicionarItemVIP-->/lilith;
    /lilith-->Fim;
    adicionarItemVIP-->/emanuel;
    /emanuel-->Fim;
    adicionarItemVIP-->/eva;
    /eva-->Fim;
    adicionarItemVIP-->/adan;
    /adan-->Fim;
    adicionarItemVIP-->/limpar;
    /limpar-->Fim;
    Rm-->Fim;
```
[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/H2H411P12P)



