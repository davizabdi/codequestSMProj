package com.codequest.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Configuração central de acesso ao banco de dados PostgreSQL.
 * Lê as credenciais de variáveis de ambiente (com valores padrão para dev
 * local)
 * e executa as migrations (CREATE TABLE IF NOT EXISTS) na inicialização,
 * o que evita depender de ferramenta externa de migration nesse projeto
 * simples.
 */
public class Database {

    private static HikariDataSource dataSource;

    public static void init() {
        String host = env("DB_HOST", "localhost");
        String port = env("DB_PORT", "5432");
        String name = env("DB_NAME", "codequest");
        String user = env("DB_USER", "postgres");
        String pass = env("DB_PASSWORD", "postgres");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://" + host + ":" + port + "/" + name);
        config.setUsername(user);
        config.setPassword(pass);
        config.setMaximumPoolSize(10);
        config.setDriverClassName("org.postgresql.Driver");

        dataSource = new HikariDataSource(config);

        migrate();
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private static void migrate() {
        String createModulos = """
                CREATE TABLE IF NOT EXISTS modulos (
                    id SERIAL PRIMARY KEY,
                    numero INT NOT NULL,
                    titulo VARCHAR(255) NOT NULL,
                    descricao TEXT,
                    topicos INT NOT NULL DEFAULT 0,
                    disponivel BOOLEAN NOT NULL DEFAULT FALSE,
                    ordem INT NOT NULL DEFAULT 0
                )
                """;

        String createQuestoes = """
                CREATE TABLE IF NOT EXISTS questoes (
                    id SERIAL PRIMARY KEY,
                    modulo_id INT NOT NULL REFERENCES modulos(id) ON DELETE CASCADE,
                    categoria VARCHAR(100) DEFAULT 'Geral',
                    nivel VARCHAR(50) DEFAULT 'Fácil',
                    enunciado TEXT NOT NULL,
                    alternativa_a VARCHAR(255) NOT NULL,
                    alternativa_b VARCHAR(255) NOT NULL,
                    alternativa_c VARCHAR(255) NOT NULL,
                    alternativa_d VARCHAR(255) NOT NULL,
                    correta CHAR(1) NOT NULL,
                    explicacao TEXT,
                    ordem INT NOT NULL DEFAULT 0
                )
                """;

        String createEtapas = """
                CREATE TABLE IF NOT EXISTS etapas (
                    id SERIAL PRIMARY KEY,
                    modulo_id INT NOT NULL REFERENCES modulos(id) ON DELETE CASCADE,
                    numero INT NOT NULL,
                    titulo VARCHAR(255) NOT NULL,
                    objetivo TEXT,
                    explicacao TEXT,
                    exemplo TEXT,
                    ordem INT NOT NULL DEFAULT 0
                )
                """;

        String createProgresso = """
                CREATE TABLE IF NOT EXISTS progresso_usuario (
                    id SERIAL PRIMARY KEY,
                    usuario_id VARCHAR(100) NOT NULL DEFAULT 'aluno_demo',
                    modulo_id INT NOT NULL REFERENCES modulos(id) ON DELETE CASCADE,
                    xp_total INT NOT NULL DEFAULT 0,
                    estrelas INT NOT NULL DEFAULT 0,
                    concluido BOOLEAN NOT NULL DEFAULT FALSE,
                    nota_maxima BOOLEAN NOT NULL DEFAULT FALSE,
                    sem_perder_vidas BOOLEAN NOT NULL DEFAULT FALSE,
                    tentativas INT NOT NULL DEFAULT 0,
                    CONSTRAINT unq_user_modulo UNIQUE(usuario_id, modulo_id)
                )
                """;

        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute(createModulos);
            stmt.execute(createQuestoes);
            stmt.execute(createEtapas);
            stmt.execute(createProgresso);

            // Garantir colunas em bancos existentes
            stmt.execute("ALTER TABLE questoes ADD COLUMN IF NOT EXISTS categoria VARCHAR(100) DEFAULT 'Geral';");
            stmt.execute("ALTER TABLE questoes ADD COLUMN IF NOT EXISTS nivel VARCHAR(50) DEFAULT 'Fácil';");
            stmt.execute("ALTER TABLE questoes ADD COLUMN IF NOT EXISTS explicacao TEXT;");

            stmt.execute("TRUNCATE TABLE modulos CASCADE;");
            seedModulo1(conn);
            seedModulo2(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao rodar as migrations do banco", e);
        }
    }

    private static void seedModulo1(Connection conn) throws SQLException {
        String checkSql = "SELECT id FROM modulos WHERE numero = 1";
        try (Statement stmt = conn.createStatement();
                var rs = stmt.executeQuery(checkSql)) {
            if (rs.next()) {
                return; // Módulo 1 já existe
            }
        }

        String insertModuloSql = """
                INSERT INTO modulos (numero, titulo, descricao, topicos, disponivel, ordem)
                VALUES (1, 'Introdução à Lógica de Programação', 'Aprenda a organizar uma solução em pequenas etapas e entenda o pensamento computacional.', 5, TRUE, 1)
                RETURNING id
                """;

        int moduloId;
        try (Statement stmt = conn.createStatement();
                var rs = stmt.executeQuery(insertModuloSql)) {
            if (rs.next()) {
                moduloId = rs.getInt(1);
            } else {
                return;
            }
        }

        String insertEtapaSql = """
                INSERT INTO etapas (modulo_id, numero, titulo, objetivo, explicacao, exemplo, ordem)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (var stmt = conn.prepareStatement(insertEtapaSql)) {
            // Etapa 1
            stmt.setInt(1, moduloId);
            stmt.setInt(2, 1);
            stmt.setString(3, "O que é Lógica de Programação?");
            stmt.setString(4,
                    "Mostrar que programar não é decorar comandos, mas resolver problemas de forma organizada.");
            stmt.setString(5,
                    "Antes de escrever qualquer código, um programador precisa saber exatamente quais passos devem ser executados.\n\nA lógica de programação é justamente essa capacidade de organizar uma solução em pequenas etapas.\n\nTodos os programas, dos mais simples aos mais complexos, seguem uma sequência de instruções.");
            stmt.setString(6,
                    "Fazer um sanduíche:\nVocê não simplesmente diz \"faça um sanduíche\".\nVocê segue uma sequência:\n1. Pegar duas fatias de pão\n2. Colocar queijo\n3. Colocar presunto\n4. Fechar o sanduíche\n\nIsso já é um algoritmo!");
            stmt.setInt(7, 1);
            stmt.addBatch();

            // Etapa 2
            stmt.setInt(1, moduloId);
            stmt.setInt(2, 2);
            stmt.setString(3, "Algoritmos");
            stmt.setString(4,
                    "Explicar o conceito de algoritmo e como ele se aplica no dia a dia e na solução de problemas.");
            stmt.setString(5,
                    """
                            O que é um algoritmo?

                            Um algoritmo é uma sequência finita e organizada de passos que resolve um problema ou executa uma tarefa.

                            Os algoritmos não existem apenas na programação. Eles fazem parte do nosso dia a dia.

                            Sempre que seguimos uma receita culinária, um manual de instruções ou as etapas para montar um móvel, estamos seguindo um algoritmo.
                            """);
            stmt.setString(6,
                    """
                            Problema:
                            Calcular a média de duas notas.

                            Algoritmo:
                            1. Receber a primeira nota.
                            2. Receber a segunda nota.
                            3. Somar as duas notas.
                            4. Dividir o resultado por dois.
                            5. Mostrar a média.

                            Observe que existe uma ordem lógica. Se tentarmos dividir antes de somar, o algoritmo deixa de funcionar corretamente.
                            """);
            stmt.setInt(7, 2);
            stmt.addBatch();

            // Etapa 3
            stmt.setInt(1, moduloId);
            stmt.setInt(2, 3);
            stmt.setString(3, "Fluxogramas");
            stmt.setString(4,
                    "Mostrar que algoritmos também podem ser representados visualmente através de símbolos padronizados.");
            stmt.setString(5,
                    """
                            O que é um fluxograma?

                            Um fluxograma é um desenho que representa visualmente a sequência de execução de um algoritmo.

                            Cada símbolo possui um significado específico. Isso facilita o entendimento da lógica antes mesmo de escrever o código.

                            Principais símbolos:
                            🟢 Início/Fim: Representa onde o algoritmo começa e termina.
                            ▭ Processo: Representa uma ação ou cálculo.
                            ▱ Entrada/Saída: Representa informações que entram ou saem do programa.
                            ◇ Decisão: Representa um ponto onde o algoritmo precisa escolher um caminho.
                            """);
            stmt.setString(6,
                    """
                            <svg width="250" height="420" viewBox="0 0 250 420" xmlns="http://www.w3.org/2000/svg" style="fill:none;stroke:#00ff88;stroke-width:2;font-family:monospace;font-size:14px;margin:auto;display:block;">
                              <!-- Início -->
                              <rect x="75" y="20" width="100" height="40" rx="20" fill="#161b22" />
                              <text x="125" y="45" text-anchor="middle" fill="#e6edf3" stroke="none">Início</text>
                              <line x1="125" y1="60" x2="125" y2="90" />
                              <polygon points="120,85 130,85 125,90" fill="#00ff88"/>

                              <!-- Processo -->
                              <rect x="65" y="90" width="120" height="40" fill="#161b22" />
                              <text x="125" y="115" text-anchor="middle" fill="#e6edf3" stroke="none">Ler idade</text>
                              <line x1="125" y1="130" x2="125" y2="160" />
                              <polygon points="120,155 130,155 125,160" fill="#00ff88"/>

                              <!-- Decisão -->
                              <polygon points="125,160 185,200 125,240 65,200" fill="#161b22" />
                              <text x="125" y="205" text-anchor="middle" fill="#e6edf3" stroke="none">Idade >= 18?</text>

                              <!-- Sim (Right) -->
                              <line x1="185" y1="200" x2="215" y2="200" />
                              <text x="200" y="190" fill="#e6edf3" stroke="none" font-size="12px">Sim</text>
                              <line x1="215" y1="200" x2="215" y2="260" />
                              <polygon points="210,255 220,255 215,260" fill="#00ff88"/>
                              <rect x="165" y="260" width="100" height="40" fill="#161b22" />
                              <text x="215" y="285" text-anchor="middle" fill="#e6edf3" stroke="none">Pode entrar</text>
                              <line x1="215" y1="300" x2="215" y2="340" />
                              <polygon points="210,335 220,335 215,340" fill="#00ff88"/>
                              <rect x="175" y="340" width="80" height="40" rx="20" fill="#161b22" />
                              <text x="215" y="365" text-anchor="middle" fill="#e6edf3" stroke="none">Fim</text>

                              <!-- Não (Left) -->
                              <line x1="65" y1="200" x2="35" y2="200" />
                              <text x="50" y="190" fill="#e6edf3" stroke="none" font-size="12px">Não</text>
                              <line x1="35" y1="200" x2="35" y2="260" />
                              <polygon points="30,255 40,255 35,260" fill="#00ff88"/>
                              <rect x="-15" y="260" width="100" height="40" fill="#161b22" />
                              <text x="35" y="285" text-anchor="middle" fill="#e6edf3" stroke="none">Proibida</text>
                              <line x1="35" y1="300" x2="35" y2="340" />
                              <polygon points="30,335 40,335 35,340" fill="#00ff88"/>
                              <rect x="-5" y="340" width="80" height="40" rx="20" fill="#161b22" />
                              <text x="35" y="365" text-anchor="middle" fill="#e6edf3" stroke="none">Fim</text>
                            </svg>
                            """);
            stmt.setInt(7, 3);
            stmt.addBatch();

            // Etapa 4
            stmt.setInt(1, moduloId);
            stmt.setInt(2, 4);
            stmt.setString(3, "Pseudocódigo");
            stmt.setString(4,
                    "Aprender o conceito de pseudocódigo e como representar algoritmos de forma estruturada sem depender de uma linguagem de programação.");
            stmt.setString(5,
                    """
                            Depois de planejar um algoritmo, precisamos escrevê-lo de uma forma organizada. Entretanto, ainda não queremos utilizar uma linguagem de programação. Para isso utilizamos o pseudocódigo.

                            O que é pseudocódigo?
                            Pseudocódigo é uma forma de escrever algoritmos utilizando uma linguagem simples, parecida com português, mas organizada como um programa. Ele serve para representar a lógica sem depender de uma linguagem específica.

                            O simulador:
                            Durante este curso você encontrará um simulador que executará cada algoritmo passo a passo. Ele mostrará:
                            • qual linha está sendo executada;
                            • quais variáveis mudaram;
                            • qual foi a saída produzida.

                            Assim você poderá visualizar exatamente como o computador interpreta cada instrução.
                            """);
            stmt.setString(6, """
                    algoritmo "BoasVindas"

                    inicio

                        escreva("Olá!")

                    fim
                    """);
            stmt.setInt(7, 4);
            stmt.addBatch();

            // Etapa 5
            stmt.setInt(1, moduloId);
            stmt.setInt(2, 5);
            stmt.setString(3, "Entrada, Processamento e Saída");
            stmt.setString(4,
                    "Compreender o ciclo fundamental (Entrada -> Processamento -> Saída) presente em praticamente todos os programas de computador.");
            stmt.setString(5,
                    """
                            Quase todos os programas seguem o mesmo ciclo de funcionamento. Independentemente de ser um jogo, um aplicativo bancário ou uma rede social, todos recebem informações, fazem algum processamento e apresentam um resultado. Esse modelo é conhecido como Entrada → Processamento → Saída.

                            📥 Entrada
                            São todas as informações fornecidas ao programa.
                            Exemplos: Nome do usuário, Idade, Dois números digitados, Um clique do mouse, Um arquivo enviado. Sem dados de entrada, muitos programas não conseguem realizar nenhuma tarefa.

                            ⚙️ Processamento
                            É o trabalho realizado pelo computador. Durante essa etapa o programa faz cálculos, toma decisões, organiza informações, realiza comparações e executa algoritmos. É nessa fase que acontece a "mágica" da programação.

                            📤 Saída
                            Depois de processar as informações, o programa apresenta um resultado.
                            Exemplos: Exibir uma mensagem, Mostrar um valor, Abrir uma página, Exibir uma imagem, Informar que um login foi realizado com sucesso.
                            """);
            stmt.setString(6, """
                    Exemplo completo: Calculadora

                    Entrada:
                    O usuário digita: 8 e 4

                    Processamento:
                    O programa realiza: 8 + 4

                    Saída:
                    O resultado exibido será: 12

                    Esse mesmo ciclo acontece em praticamente todos os programas que utilizamos diariamente.
                    """);
            stmt.setInt(7, 5);
            stmt.addBatch();

            stmt.executeBatch();
        }

        seedQuestoesModulo1(conn, moduloId);
    }

    private static void seedQuestoesModulo1(Connection conn, int moduloId) throws SQLException {
        String insertSql = """
                INSERT INTO questoes (modulo_id, categoria, nivel, enunciado, alternativa_a, alternativa_b, alternativa_c, alternativa_d, correta, explicacao, ordem)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        Object[][] questoesData = new Object[][] {
                // Q1
                { moduloId, "Lógica de Programação", "Fácil", "O que é lógica de programação?",
                        "Uma linguagem de programação.",
                        "A capacidade de organizar uma sequência de passos para resolver um problema.",
                        "Um programa utilizado para escrever códigos.", "Um tipo de computador.",
                        "B", "Lógica de programação é a organização do raciocínio em etapas para solucionar problemas.",
                        1 },
                // Q2
                { moduloId, "Lógica de Programação", "Fácil", "Qual destes exemplos representa melhor um algoritmo?",
                        "Escolher qualquer ação aleatoriamente.",
                        "Seguir uma sequência organizada de passos para preparar um café.",
                        "Escrever palavras sem ordem.", "Digitar qualquer comando.",
                        "B",
                        "Um algoritmo é uma sequência lógica e ordenada de passos para atingir um objetivo específico.",
                        2 },
                // Q3
                { moduloId, "Algoritmos", "Fácil", "Um algoritmo deve possuir:",
                        "Apenas início.", "Apenas fim.", "Uma sequência organizada de instruções.", "Somente cálculos.",
                        "C", "Algoritmos precisam de uma estrutura organizada de instruções com início, meio e fim.",
                        3 },
                // Q4
                { moduloId, "Algoritmos", "Médio", "Qual destas sequências é a mais adequada para escovar os dentes?",
                        "Escovar -> Colocar pasta -> Pegar escova",
                        "Pegar escova -> Colocar pasta -> Escovar -> Enxaguar", "Enxaguar -> Escovar -> Guardar escova",
                        "Guardar escova -> Escovar",
                        "B", "A sequência correta deve respeitar a ordem lógica das ações necessárias.", 4 },
                // Q5
                { moduloId, "Algoritmos", "Médio", "Qual característica NÃO pertence a um algoritmo?",
                        "Possuir uma sequência de passos.", "Resolver um problema.", "Ser infinito.",
                        "Ter começo e fim.",
                        "C",
                        "Algoritmos são finitos; eles devem obrigatoriamente terminar após um número finito de passos.",
                        5 },
                // Q6
                { moduloId, "Fluxogramas", "Fácil", "Qual símbolo representa uma decisão em um fluxograma?",
                        "Retângulo", "Losango", "Círculo", "Paralelogramo",
                        "B", "O losango é o símbolo padrão para tomada de decisões e desvios condicionais.", 6 },
                // Q7
                { moduloId, "Fluxogramas", "Fácil", "Qual símbolo normalmente representa início e fim?",
                        "Losango", "Oval", "Retângulo", "Seta",
                        "B", "O símbolo oval (terminador) indica os pontos de início e término de um fluxograma.", 7 },
                // Q8
                { moduloId, "Fluxogramas", "Médio", "As setas em um fluxograma indicam:",
                        "O tamanho do algoritmo.", "O fluxo de execução.", "Os comentários.",
                        "A velocidade do programa.",
                        "B", "As setas conectam os símbolos mostrando o caminho e a ordem de execução do programa.",
                        8 },
                // Q9
                { moduloId, "Pseudocódigo", "Fácil", "Qual comando é utilizado para exibir uma mensagem na tela?",
                        "leia()", "escreva()", "inicio()", "algoritmo()",
                        "B", "O comando escreva() envia dados/textos para a saída padrão (tela).", 9 },
                // Q10
                { moduloId, "Pseudocódigo", "Fácil", "Qual será a saída do comando escreva(\"Olá Mundo\")?",
                        "Olá", "Olá Mundo", "Mundo", "Nada",
                        "B", "O comando imprime exatamente o conteúdo delimitado pelas aspas.", 10 },
                // Q11
                { moduloId, "Pseudocódigo", "Médio",
                        "Complete corretamente:\nalgoritmo\n_______\nescreva(\"CodeQuest\")\nfim",
                        "fim", "inicio", "algoritmo", "leia",
                        "B", "A palavra-chave inicio marca o começo do bloco de instruções do algoritmo.", 11 },
                // Q12
                { moduloId, "Pseudocódigo", "Médio", "Qual será a saída?\nescreva(\"Programação\")\nescreva(\"Legal\")",
                        "Programação Legal", "Programação\nLegal", "Legal\nProgramação", "Nada",
                        "B", "Cada instrução escreva exibe o texto na saída.", 12 },
                // Q13
                { moduloId, "Entrada e Saída", "Fácil", "Qual opção representa corretamente o fluxo de um programa?",
                        "Saída -> Entrada -> Processamento", "Entrada -> Processamento -> Saída",
                        "Processamento -> Entrada -> Saída", "Saída -> Processamento -> Entrada",
                        "B",
                        "O ciclo de dados fundamental é receber dados (Entrada), manipulá-los (Processamento) e exibir resultados (Saída).",
                        13 },
                // Q14
                { moduloId, "Entrada e Saída", "Médio",
                        "Ao utilizar uma calculadora: Você digita dois números. Esses números representam:",
                        "Saída", "Entrada", "Processamento", "Resultado",
                        "B", "Os dados fornecidos pelo usuário para o sistema realizar uma ação constituem a Entrada.",
                        14 },
                // Q15
                { moduloId, "Entrada e Saída", "Médio",
                        "Depois que um programa realiza todos os cálculos, o resultado exibido é chamado de:",
                        "Entrada", "Processamento", "Saída", "Algoritmo",
                        "C", "A Saída é a apresentação do resultado final do processamento de dados.", 15 },
                // Q16
                { moduloId, "Pseudocódigo", "Médio",
                        "Qual será a saída?\nalgoritmo\ninicio\nescreva(\"Olá\")\nescreva(\"CodeQuest\")\nfim",
                        "OláCodeQuest", "Olá\nCodeQuest", "CodeQuest\nOlá", "Nada",
                        "B", "As duas mensagens são escritas em sequência na saída.", 16 },
                // Q17
                { moduloId, "Lógica de Programação", "Difícil",
                        "Qual alternativa descreve melhor o papel da lógica de programação?",
                        "Decorar comandos de uma linguagem.", "Aprender apenas a usar computadores.",
                        "Organizar o raciocínio para resolver problemas antes da implementação.",
                        "Criar interfaces gráficas.",
                        "C",
                        "Lógica de programação desenvolve a habilidade de estruturar soluções antes da codificação.",
                        17 },
                // Q18
                { moduloId, "Algoritmos", "Difícil", "Qual destas atividades NÃO pode ser descrita por um algoritmo?",
                        "Preparar um bolo.", "Escovar os dentes.",
                        "Escolher aleatoriamente um número sem qualquer critério.", "Calcular uma média.",
                        "C",
                        "Sem critérios ou passos determinados, uma ação totalmente caótica/sem lógica não forma um algoritmo.",
                        18 },
                // Q19
                { moduloId, "Entrada e Saída", "Difícil",
                        "Qual sequência está correta quanto ao processamento de dados?",
                        "Saída -> Processamento -> Entrada", "Entrada -> Saída -> Processamento",
                        "Entrada -> Processamento -> Saída", "Processamento -> Saída -> Entrada",
                        "C", "A ordem cronológica dos dados é sempre Entrada -> Processamento -> Saída.", 19 },
                // Q20
                { moduloId, "Pseudocódigo", "Difícil", "Qual afirmação está correta?",
                        "Fluxogramas e pseudocódigo servem para representar algoritmos.",
                        "Fluxogramas substituem totalmente os algoritmos.",
                        "Pseudocódigo é uma linguagem de programação compilada.",
                        "Todo algoritmo precisa ser escrito em Java.",
                        "A",
                        "Tanto gráficos (fluxogramas) quanto textuais (pseudocódigo) são formas válidas de representação de algoritmos.",
                        20 }
        };

        try (var stmt = conn.prepareStatement(insertSql)) {
            for (Object[] q : questoesData) {
                stmt.setInt(1, (Integer) q[0]);
                stmt.setString(2, (String) q[1]);
                stmt.setString(3, (String) q[2]);
                stmt.setString(4, (String) q[3]);
                stmt.setString(5, (String) q[4]);
                stmt.setString(6, (String) q[5]);
                stmt.setString(7, (String) q[6]);
                stmt.setString(8, (String) q[7]);
                stmt.setString(9, (String) q[8]);
                stmt.setString(10, (String) q[9]);
                stmt.setInt(11, (Integer) q[10]);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private static void seedModulo2(Connection conn) throws SQLException {
        String checkSql = "SELECT id FROM modulos WHERE numero = 2";
        try (Statement stmt = conn.createStatement();
                var rs = stmt.executeQuery(checkSql)) {
            if (rs.next()) {
                return; // Módulo 2 já existe
            }
        }

        String insertModuloSql = """
                INSERT INTO modulos (numero, titulo, descricao, topicos, disponivel, ordem)
                VALUES (2, 'Módulo 2 (Em Desenvolvimento)', 'Este módulo está em desenvolvimento e logo estará disponível para você.', 0, FALSE, 2)
                """;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(insertModuloSql);
        }
    }

    private static String env(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }
}
