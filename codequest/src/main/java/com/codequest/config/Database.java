package com.codequest.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Configuração central de acesso ao banco de dados PostgreSQL.
 * Lê as credenciais de variáveis de ambiente (.env ou sistema)
 * e executa as migrations (CREATE TABLE IF NOT EXISTS) na inicialização.
 */
public class Database {

        private static HikariDataSource dataSource;
        private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        
                ic static void init() {
                String host = env("DB_HOST", "localho
                String port = env("DB_PORT", "5432");
                String name = env("DB_NAME", "codequest")
                String user = env("DB_USER", "postgres");

                
                HikariConfig config = new HikariConfig();
                config.setJdbcUrl("jdbc:p
                config.setUsername(user);
                config.setPassword(pass);
                config.setMaximumPoolSize(10);

                

                
         

        
                ic static Connection getConnection
         

        
                ate static void migrate() 
                                reateModulos = """
                                CREATE TABLE IF NOT EXISTS
                                    id SERIAL PRIMARY KE
                                    numero INT NOT NULL,
                                    titulo VARCHAR(
                                    descricao TEXT,
                                    topicos INT NOT NULL DEFAULT 0,
                                    disponivel BOOLEAN NOT NULL 
                                 
                                )

                
                                reateQuestoes = """
                                CREATE TABLE IF NOT EXISTS
                                    id SERIAL PRIMARY KEY,
                                    modulo_id INT NOT NULL REFERENCES modul
                                    categoria VARCHAR(100) DEFAULT 'Ge
                                    nivel VARCHAR(50) DEFAUL
                                    enunciado TEXT NOT NULL,
                                    alternativa_a VARCHAR(255) NOT NULL,
                                    alternativa_b VARCHAR(255) NOT NULL,
                                    alternativa_c VARCHAR(255) NOT NULL,
                                    alternativa_d VARCHAR(255
                                    correta CHAR(1) 
                                    explicacao TEXT,
                                 
                                )

                
                                reateEtapas = """
                                CREATE TABLE IF NOT EXISTS
                                    id SERIAL PRIMARY KEY,
                                    modulo_id INT NOT NU
                                    numero INT NOT NULL,
                                    titulo VARCHAR
                                    objetivo TEXT,
                                    explicacao TE
                                    exemplo TEXT,
                                 
                                )

                
                                reateProgresso = """
                                CREATE TABLE IF NOT EXISTS
                                    id SERIAL PRIMARY KEY,
                                    usuario_id VARCHAR(100) NOT NULL DEFAULT 'aluno_demo',
                                    modulo_id INT NOT NULL REFERENCE
                                    xp_total INT NOT NULL DEFAULT 0,
                                    estrelas INT NOT NULL DEFAULT 0,
                                    concluido BOOLEAN NOT NULL DEFAULT FALSE,
                                    nota_maxima BOOLEAN NOT NULL DEFAULT FALSE,
                                    sem_perder_vidas BOOLEAN NOT NULL 
                                    tentativas INT NOT NULL DEFAULT 0,
                                 
                                )

                
                                nection conn = getConnection();
                            Statement stmt = conn.cr
                        stmt.execute(createModulos);
                        stmt.execute(createQuestoes
                        stmt.execute(createEtapas);

                        
                        // Garantir colunas em bancos existentes
                        stmt.execute("ALTER TABLE questoes ADD COLUMN IF NOT EXISTS categoria VARCHAR(100) DEFAULT 'Geral
                        stmt.execute("ALTER TABLE questoes ADD COLUMN IF NOT EXISTS nivel VARCHAR(50) D

                        
                        seedModulo1(conn);
                    seedModulo2(conn);
                        tch (SQLException e) {
                 
         

        
                ate static void seedModulo1(Connection conn) throws SQLExcep
                String checkSql = "SELECT id FROM modulos WHE
                                tement stmt = conn.createStatement();
                            var rs = stm
                                rs.next()) {
                         
                 

                
                                nsertModuloSql = """
                                INSERT INTO modulos (numero, titulo, descricao, topicos, disponivel, ordem)
                                VALUES (1, '
                                RETU

                
                int moduloId;
                                tement stmt = conn.createStatement();
                            var rs = stm
                                rs.next()) {
                            modu
                                se {
                         
                 

                
                                nsertEtapaSql = """
                                INSERT INTO etapas (modulo_i
                                VALU

                
                        (var stmt 
                        // Etapa 1
                        stmt.setInt(1, mod
                        stmt.setInt(2, 1);
                        stmt.setString(3,
                                        String(4,
                                "Mostrar 
                                        String(5,
                                "Antes de
                                        String(6,
                                "Fazer um 
                        stmt.setInt(7, 1

                        
                        // Etapa 2
                        stmt.setInt(1, mod
                        stmt.setInt(2, 2);
                        stmt.setString(3,
                                        String(4,
                                "Explicar
                                        Str
                                                        

                                                        

                                                        

                                                        
                                                        Sempr
                                        "
                                        Str
                                                        
                                                        Problema:

                                                        
                                                        Algoritmo:
                                                        1. Receber a primeira nota
                                                        2. Receber a segunda no
                                                        3. Somar as duas notas.
                                                        4. Dividir o result

                                                        
                                                        Obser
                                        ""
                        stmt.setInt(7, 2

                        
                        // Etapa 3
                        stmt.setInt(1, mod
                        stmt.setInt(2, 3);
                        stmt.setString(3,
                                        String(4,
                                "Mostrar 
                                        Str
                                                        

                                                        

                                                        

                                                        
                                                        Principais símbolos:
                                                        🟢 Início/Fim: Representa onde o algoritmo 
                                                        ▭ Processo: Representa uma ação ou cálculo.
                                                        ▱ Entrada/Saída: Representa informações que entram ou saem do programa.
                                                        ◇ Dec
                                        "
                                        Str
                                                        
                                                        <svg width="250" 
                                                          <!-- Início -->
                                                          <rect x="75" y="20" width="100" height="40" rx="20" fill="#161b22" />
                                                          <text x="125" y="45" text-anchor="middle" 
                                                          <line x1="125" y1="60" x2="125" y2="90" />

                                                        
                                                          <!-- Processo -->
                                                          <rect x="65" y="90" width="120" height="40" fill="#161b22" />
                                                          <text x="125" y="115" text-anchor="middle" f
                                                          <line x1="125" y1="130" x2="125" y2="160" />

                                                        
                                                          <!-- Decisão -->
                                                          <polygon points="125,160 185,200 125,240 65,200" fill="#161b22" />

                                                        
                                                          <!-- Sim (Right) -->
                                                          <line x1="185" y1="200" x2="215" y2="200" />
                                                          <text x="200" y="190" fill="#e6edf3" stroke=
                                                          <line x1="215" y1="200" x2="215" y2="260" />
                                                          <polygon points="210,255 220,255 215,260" fill="#00ff88"/>
                                                          <rect x="165" y="260" width="100" height="40" fill="#161b22" />
                                                          <text x="215" y="285" text-anchor="middle" f
                                                          <line x1="215" y1="300" x2="215" y2="340" />
                                                          <polygon points="210,335 220,335 215,340" fill="#00ff88"/>
                                                          <rect x="175" y="340" width="80" height="40" rx="20" fill="#161b22" />

                                                        
                                                          <!-- Não (Left) -->
                                                          <line x1="65" y1="200" x2="35" y2="200" />
                                                          <text x="50" y="190" fill="#e6edf3" stroke
                                                          <line x1="35" y1="200" x2="35" y2="260" />
                                                          <polygon points="30,255 40,255 35,260" fill="#00ff88"/>
                                                          <rect x="-15" y="260" width="100" height="40" fill="#161b22" />
                                                          <text x="35" y="285" text-anchor="middle" 
                                                          <line x1="35" y1="300" x2="35" y2="340" />
                                                          <polygon points="30,335 40,335 35,340" fill="#00ff88"/>
                                                          <rect x="-5" y="340" width="80" height="40" rx="20" fill="#161b22" />
                                                          <tex
                                                        </svg
                                        ""
                        stmt.setInt(7, 3

                        
                        // Etapa 4
                        stmt.setInt(1, mod
                        stmt.setInt(2, 4);
                        stmt.setString(3,
                                        String(4,
                                "Aprender
                                        Str
                                                        

                                                        
                                                        O que é pseudocódigo?

                                                        
                                                        O simulador:
                                                        Durante este curso você encontrará
                                                        • qual linha está sendo ex
                                                        • quais variáveis mudaram;

                                                        
                                                        Assim
                                        """);
                                        String(6, """

                                        

                                        

                                        
                                        fim
                                """);
                        stmt.setInt(7, 4

                        
                        // Etapa 5
                        stmt.setInt(1, mod
                        stmt.setInt(2, 5);
                        stmt.setString(3,
                                        String(4,
                                "Compreen
                                        Str
                                                        

                                                        
                                                        📥 Entrada
                                                        São todas as informações fornecidas ao programa.

                                                        
                                                        ⚙️ Processamento

                                                        
                                                        📤 Saída
                                                        Depois de processar as informações, o programa apresenta um resultado.
                                                        Exemp
                                        "
                                        ");
                                                        String(6, """

                                                        
                                                        Entrada:

                                                        
                                                        Processamento:

                                                        
                                                        Saída:

                                                        
                                                        Esse 
                                """);
                        stmt.setInt(7, 5

                        
                 

                
         

        
                ate static void seedQu
                                nsertSql = """
                                INSERT INTO questoes (modulo_id, categor
                                VALU

                
                                [] qu
                                // Q1
                                                Id, "Lógica de Programação", "Fá
                                                "Uma linguagem de programação.",
                                                "A capacidade de organizar uma sequência de pas
                                                os para resolver um probl
                                                "Um 
                                                rograma utilizado para escrever códigos.", "Um tipo de computador.",
                                                "B",
                                     
                                // Q2
                                                
                                                Id, "Lógica de Programação", "Fácil", "Qu
                                                "Escolher qualquer ação aleatoriamente.",
                                                "Seguir uma sequência organizada de passos para preparar um 
                                                "Esc
                                                "B",
                                                "Um 
                                     
                                // Q3
                                                Id, "Algoritmos", "Fácil", "Um a
                                                goritmo deve possuir:",
                                                "Ape
                                                as início.", "Apenas fim.", "Uma sequência organizada de instruções.", "Somente cálculos
                                                "C",
                                     
                                // Q4
                                                
                                                Id, "Algoritmos", "Médio", "Qual destas seq
                                                "Escovar -> Colocar pasta -> Pegar escova",
                                                
                                                "Pegar escova -> Colocar pas
                                                "Gua
                                                dar escova -> Escovar",
                                                
                                     
                                // Q5
                                                Id, "Algoritmos", "Médio", "Qual característica NÃO pertence
                                                a um algoritmo?"
                                                "Possuir uma sequênc
                                                "Ter
                                                "C",
                                                "Alg
                                     
                                // Q6
                                                
                                                Id, "Fluxogramas", "Fácil", "Qual símbolo represent
                                                "Ret
                                                ngulo", "Losango", "Círculo", "Paralelogramo",
                                                
                                     
                                // Q7
                                                Id, "Fluxogramas", "Fácil", "Qual símbo
                                                "Los
                                                ngo", "Oval", "Retângulo", "Seta",
                                                
                                     
                                // Q8
                                                Id, "Fluxogramas", "Médio", "As setas em um fluxograma indicam:",
                                                "O tamanho do algoritmo.", "
                                                "A v
                                                locidade do programa.",
                                                "B",
                                     
                                // Q9
                                                
                                                Id, "Pseudocódigo", "Fácil", "Qual comando é util
                                                "lei
                                                ()", "escreva()", "inicio()", "algoritmo()",
                                                
                                      
                                // Q10
                                                
                                                Id, "Pseudocódigo", "Fácil", "Qual s
                                                "Olá", "Olá Mundo", "Mundo", "Nada",
                                                
                                      
                                // Q11
                                                Id, "Pseudocódigo", "Médio",
                                                "Complete corretamente:\nalgoritmo\n_
                                                "fim
                                                , "inicio", "algoritmo", "leia",
                                                
                                      
                                // Q12
                                                
                                                Id, "Pseudocódigo", "Médio", "Qual será a saída?\nescreva(\"Programação\
                                                "Programação Legal", "Programação\nLegal", "Legal\nProgramaç
                                      
                                // Q13
                                                
                                                Id, "Entrada e Saída", "Fácil", "Qua
                                                 opção representa corretamente o flu
                                                "Saída -> Entrada -> Processamento",
                                                "Entrada -> Processamento -> Saída",
                                                "Pro
                                                "B",
                                                "O ci
                                      
                                // Q14
                                                Id, "Entrada e Saída", "Médio",
                                                "Ao utilizar uma calculadora: Você digita dois nú
                                                "Saí
                                                a", "Entrada", "Processamento", "Resultado",
                                                "B", 
                                      
                                // Q15
                                                Id, "Entrada e Saída", "Médio",
                                                "Depois que um programa realiza todos os cálculos
                                                "Ent
                                                ada", "Processamento", "Saída", "Algoritmo",
                                                
                                      
                                // Q16
                                                Id, "Pseudocódigo", "Médio",
                                                "Qual será a saída?\nalgoritmo\ninicio\nescreva(\"Olá\")\ne
                                                "OláCodeQuest", "Olá\nCodeQuest", "CodeQuest\nOlá", "Nada",
                                      
                                // Q17
                                                Id, "Lógica de Programação", "Difícil",
                                                "Qual alternativa descreve melhor o p
                                                pel da lógica de programação?",
                                                "Decorar comandos de uma linguagem.", "Aprender apenas a usar computadore
                                                "Organizar o raciocínio para 
                                                "Cri
                                                "C",
                                                "Lógi
                                      
                                // Q18
                                                
                                                Id, "Algoritmos", "Difícil", "Qual destas 
                                                "Preparar um bolo.", "Escovar os dentes.",
                                                
                                                "Esc
                                                "C",
                                                "Sem 
                                      
                                // Q19
                                                Id, "Entrada e Saída", "Difícil",
                                                "Qual sequência está correta quanto 
                                                o processamento de dados?",
                                                "Saída -> Processamento -> Entrada",
                                                "Entrada -> Saída -> Processamento",
                                                "Ent
                                                ada -> Processamento -> Saída", "Processamento -> Saída -> Entrada",
                                                
                                      
                                // Q20
                                                Id, "Pseudocódigo", "Difícil", "Qual afirmação está correta?",
                                                "Fluxogramas e pseudocódigo servem para representar
                                                "Fluxogramas substituem totalmente os algoritmos.",
                                                "Pseudocódigo é uma linguagem de programação c
                                                "Tod
                                                "A",
                                                "Tan
                  

                
                        (var stmt = conn.prepareStatement
                                (Object[] q : questoesData) {
                                stmt.setInt(1, (Integer) q[0]);
                                stmt.setString(2, (String) q[1]);
                                stmt.setString(3, (String) q[2]);
                                stmt.setString(4, (String) q[3]);
                                stmt.setString(5, (String) q[4]);
                                stmt.setString(6, (String) q[5]);
                                stmt.setString(7, (String) q[6]);
                                stmt.setString(8, (String) q[7]);
                                stmt.setString(9, (String) q[8]);
                                stmt.setString(10, (String) q[9])
                                stmt.setInt(11, 
                         
                        }
                 
         

        
                ate static void seedModulo2(Connection conn) throws SQLExcep
                String checkSql = "SELECT id FROM modulos WHE
                                tement stmt = conn.createStatement();
                            var rs = stm
                                rs.next()) {
                         
                 

                
                                nsertModuloSql = """
                                INSERT INTO modulos (numero, titulo, descricao, topicos, disponivel, ordem)
                                VALU

                
                        (Statement stmt = conn.createS
                 
            }
    }


    public static String env(String key, String defaultValue) {
        String value = null;
        try {
            value = dotenv.get(key);
        } catch (Exception ignored) {
        }
        if (value == null || value.isBlank()) {
            value = System.getenv(key);
        }
        if (value == null || value.isBlank()) {
            value = System.getProperty(key);
        }
        return (value == null || value.isBlank()) ? defaultValue : value.trim();
    }
}
