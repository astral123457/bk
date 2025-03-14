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
    A --> A;
    B --> B;
    C --> D;
    D --> E;
    E --> N;
    E --> S;
    G --> Hs;
    H --> I;
    I --> J;
    J --> K;

    L[Início do Desligamento] --> MonDisable();
    M --> N;

    O[Comando recebido] --> PonCommand();
    P --> QComando "oi"?;
    Q --> Sim --> RJogador?;
    R --> Sim --> SJá recebeu o kit?;
    S --> Não --> TDar kit inicial;
    T --> UMarcar kit recebido;
    S --> Sim --> VDar itens adicionais (adicionarItem);
    R --> Não --> W[Mensagem de erro (jogador apenas)];
    Q --> Não --> XComando "addvip"?;
    X --> Sim --> YTem permissão vip.add?;
    Y --> Sim --> ZArgumentos corretos?;
    Z ->- Sim --> AAAdicionar VIP;
    Z --> Não --> AB[Mensagem de uso incorreto];
    Y --> Não --> AC[Mensagem de sem permissão];
    X --> Não --> ADComando "rmvip"?;
    AD --> Sim --> AETem permissão vip.add?;
    AE --> Sim --> AFArgumentos corretos?;
    AF --> Sim --> AGRemover VIP;
    AF --> Não --> AH[Mensagem de uso incorreto];
    AE --> Não --> AI[Mensagem de sem permissão];
    AD --> Não --> AJComando "emanuel"?;
    AJ --> Sim --> AKJogador?;
    AK --> Sim --> ALTem permissão vip.use?;
    AL --> Sim --> AMDar kit "emanuel";
    AL --> Não --> AN[Mensagem de sem permissão];
    AK --> Não --> AO[Mensagem de erro (jogador apenas)];
    AJ --> Não --> APComando "lilith"?;
    AP --> Sim --> AQJogador?;
    AQ --> Sim --> ARTem permissão vip.use?;
    AR --> Sim --> ASDar kit "lilith";
    AR --> Não --> AT[Mensagem de sem permissão];
    AQ --> Não --> AU[Mensagem de erro (jogador apenas)];
    AP --> Não --> AVComando "eva"?;
    AV --> Sim --> AWJogador?;
    AW --> Sim --> AXTem permissão vip.use?;
    AX --> Sim --> AYDar kit "eva";
    AX --> Não --> AZ[Mensagem de sem permissão];
    AW --> Não --> BA[Mensagem de erro (jogador apenas)];
    AV --> Não --> BBComando "adan"?;
    BB --> Sim --> BCJogador?;
    BC --> Sim --> BDTem permissão vip.use?;
    BD --> Sim --> BEDar kit "adan";
    BD --> Não --> BF[Mensagem de sem permissão];
    BC --> Não --> BG[Mensagem de erro (jogador apenas)];
    BB --> Não --> BHComando "limpar"?;
    BH --> Sim --> BIJogador?;
    BI --> Sim --> BJTem permissão vip.use?;
    BJ --> Sim --> BKLimpar inventário;
    BJ --> Não --> BL[Mensagem de sem permissão];
    BI --> Não --> BM[Fim];
    BH --> Não --> BNComando "vip"?;
    BN --> Sim --> BOJogador?;
    BO --> Sim --> BPTem permissão vip.use?;
    BP --> Sim --> BQDar item vip;
    BP --> Não --> BR[Mensagem de sem permissão];
    BO --> Não --> BS[Mensagem de erro (jogador apenas)];
    BN --> Não --> BT[Fim];

    V --> BUcontador < 3?;
    BU --> Sim --> BVAdicionar itens;
    BV --> BWIncrementar contador;
    BW --> BX[Mensagem com contador];
    BU --> Não --> BY[Mensagem de limite atingido];

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

