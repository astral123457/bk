# BK Kit básico de alimentação no inicio do Minecraft

(OBS A VERSAO !.8 Esta com ERRO baixe a 1.7 eu Pretendo Atualizar...

# Languem Support

BR, EN, ES, FR, DE
RU, ZH, ZH-TW, JA
KO, IT, NL, PL, SV,
CS, HU, TR, AR, FI, DA

# Explicações:

onEnable():

Inicializa o plugin, carrega configurações, registra eventos e comandos.

Verifica se o plugin está habilitado na configuração e, se não, o desativa.

onDisable():

Desabilita o plugin.

Adiciona itens ao inventário do jogador, limitando a 3 vezes.

Realiza verificações de permissão e argumentos.

Executa ações diferentes dependendo do comando.

Verifica se o jogador que executou o comando é um jogador ou o console.

Verifica as permissões do jogador.

Gerencia os kits dos vips.

O modulo vip e caso voce queria dar um presente escolha uma Cor ?

Rosa , Verde , Amarelo e Maron ai voce da /addvip <name>

       Digita o comando: 
                        /eva (para rosa)
      
                        /lilth (para verde)
                        
                        /emanuel (para amarela)
                        
                       /adan (para maron)
                       
     o admim pergunta pegou seu kit ? se sim voce da o comando /rmvip <name> para nao fica pegando sempre!


Gerencia a adição e remoção de vips.

Gerencia o comando de /limpar o inventário.



Dependencia LuckPerms-Bukkit-5.4.156 https://luckperms.net/

         Command: /oi , /bk ou /kit
                  /vip
                  /addvip <name>
                  /rmvip <name>
                  /lilith (kit Bruxa)
                  /emanuel (kit stive Apocaliptico)
                  /eva (kit rainha do mar)
                  /adan (kit escravo da terra)
                  /limpar (limpar o inventário.)
                  /addpermission (permisao ao jogador para explorar o mundo)

![image](https://github.com/user-attachments/assets/53654929-c8c6-496d-8387-777ece188616)


# Testamento de (Z) or (D) Z.eus aos 4

XAΓ O Chi Rho é formado pela combinação das duas primeiras letras da palavra grega Christos, 
que significa "Cristo".  estratégia de precificação, escala, série de ideias,  uma estrela, um macho dominante


ΘΕ Consciência emocional é pequena e leva a Morte (LILITH)


ΔΙ LAtino Pequeno (Homen Barro Adão)


ΣΚ Lesbica Feminista é uma criatura aquática, semelhante a uma tartaruga, que habita rios e lagos ( EVA e Seus pecados Do mundo)

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

