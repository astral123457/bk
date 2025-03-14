# BK Kit básico de alimentação no inicio do Minecraft

Explicações:

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

Gerencia a adição e remoção de vips.

Gerencia o comando de /limpar o inventário.



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


# Testamento de (Z) or (D) Z.eus aos 4

XAΓ O Chi Rho é formado pela combinação das duas primeiras letras da palavra grega Christos, 
que significa "Cristo".  estratégia de precificação, escala, série de ideias,  uma estrela, um macho dominante


ΘΕ Consciência emocional é pequena e leva a Morte (LILITH)


ΔΙ LAtino Pequeno (Homen Barro Adão)


ΣΚ Lesbica Feminista é uma criatura aquática, semelhante a uma tartaruga, que habita rios e lagos ( EVA e Seus pecados Do mundo)

![image](https://github.com/user-attachments/assets/5a6795d7-de8a-4000-8003-b51297295103)

```mermaid
graph TD;
    A[Início do Plugin] --> B{onEnable()};
    B --> C{Criar pastas e configs};
    C --> D{Carregar status do plugin};
    D --> E{Plugin está habilitado?};
    E -- Não --> F[Desativar plugin];
    E -- Sim --> G{Registrar eventos};
    G --> H{Configurar banco de dados};
    H --> I{Carregar idioma};
    I --> J{Registrar comandos};
    J --> K[Plugin habilitado];

    L[Início do Desligamento] --> M{onDisable()};
    M --> N[Plugin desabilitado];

    O[Comando recebido] --> P{onCommand()};
    P --> Q{Comando "oi"?};
    Q -- Sim --> R{Jogador?};
    R -- Sim --> S{Já recebeu o kit?};
    S -- Não --> T{Dar kit inicial};
    T --> U{Marcar kit recebido};
    S -- Sim --> V{Dar itens adicionais (adicionarItem)};
    R -- Não --> W[Mensagem de erro (jogador apenas)];
    Q -- Não --> X{Comando "addvip"?};
    X -- Sim --> Y{Tem permissão vip.add?};
    Y -- Sim --> Z{Argumentos corretos?};
    Z -- Sim --> AA{Adicionar VIP};
    Z -- Não --> AB[Mensagem de uso incorreto];
    Y -- Não --> AC[Mensagem de sem permissão];
    X -- Não --> AD{Comando "rmvip"?};
    AD -- Sim --> AE{Tem permissão vip.add?};
    AE -- Sim --> AF{Argumentos corretos?};
    AF -- Sim --> AG{Remover VIP};
    AF -- Não --> AH[Mensagem de uso incorreto];
    AE -- Não --> AI[Mensagem de sem permissão];
    AD -- Não --> AJ{Comando "emanuel"?};
    AJ -- Sim --> AK{Jogador?};
    AK -- Sim --> AL{Tem permissão vip.use?};
    AL -- Sim --> AM{Dar kit "emanuel"};
    AL -- Não --> AN[Mensagem de sem permissão];
    AK -- Não --> AO[Mensagem de erro (jogador apenas)];
    AJ -- Não --> AP{Comando "lilith"?};
    AP -- Sim --> AQ{Jogador?};
    AQ -- Sim --> AR{Tem permissão vip.use?};
    AR -- Sim --> AS{Dar kit "lilith"};
    AR -- Não --> AT[Mensagem de sem permissão];
    AQ -- Não --> AU[Mensagem de erro (jogador apenas)];
    AP -- Não --> AV{Comando "eva"?};
    AV -- Sim --> AW{Jogador?};
    AW -- Sim --> AX{Tem permissão vip.use?};
    AX -- Sim --> AY{Dar kit "eva"};
    AX -- Não --> AZ[Mensagem de sem permissão];
    AW -- Não --> BA[Mensagem de erro (jogador apenas)];
    AV -- Não --> BB{Comando "adan"?};
    BB -- Sim --> BC{Jogador?};
    BC -- Sim --> BD{Tem permissão vip.use?};
    BD -- Sim --> BE{Dar kit "adan"};
    BD -- Não --> BF[Mensagem de sem permissão];
    BC -- Não --> BG[Mensagem de erro (jogador apenas)];
    BB -- Não --> BH{Comando "limpar"?};
    BH -- Sim --> BI{Jogador?};
    BI -- Sim --> BJ{Tem permissão vip.use?};
    BJ -- Sim --> BK{Limpar inventário};
    BJ -- Não --> BL[Mensagem de sem permissão];
    BI -- Não --> BM[Fim];
    BH -- Não --> BN{Comando "vip"?};
    BN -- Sim --> BO{Jogador?};
    BO -- Sim --> BP{Tem permissão vip.use?};
    BP -- Sim --> BQ{Dar item vip};
    BP -- Não --> BR[Mensagem de sem permissão];
    BO -- Não --> BS[Mensagem de erro (jogador apenas)];
    BN -- Não --> BT[Fim];

    V --> BU{contador < 3?};
    BU -- Sim --> BV{Adicionar itens};
    BV --> BW{Incrementar contador};
    BW --> BX[Mensagem com contador];
    BU -- Não --> BY[Mensagem de limite atingido];

    K --> L;
    T --> U;
    U --> BT;
    V --> BT;
    AA --> BT;
    AG --> BT;
    AM --> BT;
    AS --> BT;
    AY --> BT;
    BE --> BT;
    BK --> BT;
    BQ --> BT;
    W --> BT;
    AB --> BT;
    AC --> BT;
    AH --> BT;
    AI --> BT;
    AN --> BT;
    AO --> BT;
    AT --> BT;
    AU --> BT;
    AZ --> BT;
    BA --> BT;
    BF --> BT;
    BG --> BT;
    BL --> BT;
    BS --> BT;
    BX --> BT;
    BY --> BT;
```

