# Análise Exploratória de Dados - Campeonato Brasileiro de Futebol

## Membros

Diego Carvalho Cavalcante

## Sobre o Projeto

Este projeto implementa uma aplicação de análise exploratória de dados do Campeonato Brasileiro de Futebol em duas linguagens orientadas a objetos: **Java** e **Python**. A aplicação permite visualizar o desempenho dos times ao longo das rodadas de diferentes anos do campeonato, utilizando um **arquivo CSV único** contendo dados de múltiplas temporadas (2010-2024).

## Problema

O Campeonato Brasileiro gera uma grande quantidade de dados a cada temporada. Analisar o desempenho dos times ao longo das rodadas pode fornecer insights valiosos sobre a consistência, evolução e padrões de cada equipe durante a competição. O desafio é criar uma interface intuitiva que permita explorar esses dados históricos de forma visual e interativa, processando eficientemente um grande volume de informações de múltiplas temporadas.

## Solução

### Funcionalidades Implementadas

1. **Carregamento Único**: Sistema que carrega uma única vez um arquivo CSV contendo dados de todas as temporadas
2. **Tela Inicial**: 
   - Botão para carregar dados completos
   - Barra de progresso durante o carregamento
   - Seleção de ano (anos disponíveis são detectados automaticamente)
   - Seleção de time (times são filtrados por ano selecionado)
3. **Tela de Análise**: 
   - Painel esquerdo com estatísticas detalhadas do time no ano selecionado
   - Painel direito com gráfico de evolução da posição na tabela por rodada
4. **Processamento Eficiente**: Sistema otimizado para gerenciar dados de múltiplos anos em memória

### Arquitetura do Sistema

#### Estrutura de Classes Atualizada

**Model:**
- `Time`: Representa um time com suas estatísticas
- `Partida`: Representa uma partida individual (agora com ano incluído)
- `Campeonato`: Representa um campeonato de um ano específico
- `GerenciadorDados`: **NOVO** - Gerencia dados de múltiplos anos

**View:**
- `TelaInicial`: Interface inicial (agora com carregamento de dados)
- `TelaAnalise`: Interface de análise detalhada

**Controller:**
- `ControladorPrincipal`: Lógica de controle principal

**Utils:**
- `LeitorCSV`: Utilitário para leitura do arquivo único com callback de progresso

## Estrutura do Repositório

```
brasileirao-analysis/
├── README.md
├── java/
│   ├── data/
│       └── brasileirao_completo.csv
│   ├── src/
│   │   ├── main/
│   │   │   └── Main.java
│   │   ├── model/
│   │   │   ├── Time.java
│   │   │   ├── Partida.java
│   │   │   ├── Campeonato.java
│   │   │   └── GerenciadorDados.java    
│   │   ├── view/
│   │   │   ├── TelaInicial.java        
│   │   │   └── TelaAnalise.java
│   │   ├── controller/
│   │   │   └── ControladorPrincipal.java 
│   │   └── utils/
│   │       └── LeitorCSV.java           
│   ├── lib/
│   └── build.gradle
└── python/
|   ├── main.py                    
|   ├── requisitos.txt           
|   ├── test_sistema.py           
|   ├── data/                     
|   │   └── brasileirao_completo.csv
|   ├── model/                    
|   │   ├── __init__.py
|   │   ├── time.py              
|   │   ├── partida.py           
|   │   ├── campeonato.py        
|   │   └── gerenciador_dados.py 
|   ├── view/                    
|   │   ├── __init__.py
|   │   ├── tela_inicial.py      
|   │   └── tela_analise.py      
|   ├── controller/              
|   │   ├── __init__.py
|   │   └── controlador_principal.py
|   └── utils/                   
|       ├── __init__.py
|       └── leitor_csv.py        

```

## Como Executar

### Versão Java

#### Pré-requisitos
- Java 11 ou superior
- Gradle

#### Preparação dos Dados
1. Coloque o arquivo `brasileirao_completo.csv` na pasta `data/`
2. O arquivo deve estar no formato especificado abaixo

#### Execução
```bash
cd java
./gradlew run
```

### Versão Python

#### Pré-requisitos
- Python 3.8 ou superior
- Tkinter (geralmente incluído com Python)

#### Instalação das dependências
```bash
cd python
pip install matplotlib
```

#### Execução
```bash
cd python
python main.py
```

## Formato dos Dados CSV

O arquivo `brasileirao_completo.csv` deve conter as seguintes colunas:

```csv
ano,rodada,data,mandante,visitante,gols_mandante,gols_visitante
2024,1,2024-04-13,Flamengo,Palmeiras,2,1
2024,1,2024-04-13,Santos,Corinthians,0,2
2023,38,2023-12-06,São Paulo,Grêmio,1,0
...
```

### Colunas Obrigatórias:
- `ano`: Ano do campeonato (ex: 2024)
- `rodada`: Número da rodada (1-38)
- `data`: Data da partida (formato: YYYY-MM-DD)
- `mandante`: Nome do time mandante
- `visitante`: Nome do time visitante
- `gols_mandante`: Gols marcados pelo mandante
- `gols_visitante`: Gols marcados pelo visitante

## Orientação a Objetos em Python vs Java

### Semelhanças

Ambas as linguagens suportam os pilares fundamentais da POO:
- **Encapsulamento**: Ambas permitem controle de acesso a atributos e métodos
- **Herança**: Suporte a herança simples e múltipla (Python) vs simples (Java)
- **Polimorfismo**: Implementado através de interfaces/classes abstratas

### Diferenças Observadas

#### **Declaração de Classes**

**Java:**
```java
public class GerenciadorDados {
    private List<Partida> todasPartidas;
    private Map<Integer, Set<String>> timesPorAno;
    
    public GerenciadorDados() {
        this.todasPartidas = new ArrayList<>();
        this.timesPorAno = new HashMap<>();
    }
}
```

**Python:**
```python
class GerenciadorDados:
    def __init__(self):
        self._todas_partidas: List[Partida] = []
        self._times_por_ano: Dict[int, Set[str]] = {}
```

#### **Encapsulamento**

- **Java**: Modificadores de acesso explícitos (`private`, `protected`, `public`)
- **Python**: Convenções com underscore (`_atributo` para protegido, `__atributo` para privado)

#### **Interfaces vs Duck Typing**

**Java:**
```java
public interface ProgressCallback {
    void onProgress(int progresso, String mensagem);
}
```

**Python:**
```python
from typing import Protocol

class ProgressCallback(Protocol):
    def on_progress(self, progresso: int, mensagem: str) -> None: ...
```

#### **Tratamento de Exceções**

- **Java**: Checked exceptions obrigatórias, try-with-resources
- **Python**: Apenas unchecked exceptions, context managers com `with`

### Impressões sobre POO em Python

Python oferece uma abordagem mais flexível e pythônica para POO. Algumas observações específicas deste projeto:

**Pontos Positivos:**
- **Flexibilidade**: A tipagem dinâmica permite prototipagem mais rápida
- **Simplicidade**: Menos código boilerplate comparado ao Java
- **Duck Typing**: Permite interfaces implícitas sem declarações formais
- **Properties**: Sistema elegante para getters/setters automáticos
- **List Comprehensions**: Processamento de dados mais conciso

**Desafios Específicos:**
- **Threading**: GIL (Global Interpreter Lock) pode limitar paralelismo real
- **Callbacks**: Menos estruturado que interfaces Java para callbacks de progresso
- **Tipagem**: Type hints são opcionais, podem causar erros em runtime
- **Performance**: Processamento de grandes volumes de dados mais lento

**Exemplo Prático - Filtragem de Dados:**

**Java:**
```java
List<Partida> partidasDoAno = todasPartidas.stream()
    .filter(p -> p.getAno() == ano)
    .collect(Collectors.toList());
```

**Python:**
```python
partidas_do_ano = [p for p in self._todas_partidas if p.ano == ano]
```

**Conclusão:**
Para este projeto específico de análise de dados, Python seria mais adequado devido à sua rica biblioteca de manipulação de dados (pandas, numpy) e sintaxe mais limpa para processamento. Java, por outro lado, oferece melhor performance para grandes volumes e estrutura mais rígida que previne erros de tipo.

## Tecnologias Utilizadas

### Java
- **GUI**: Swing para interface gráfica
- **Gráficos**: JFreeChart para visualizações
- **CSV**: OpenCSV para leitura de arquivos
- **Concorrência**: SwingWorker para processamento assíncrono
- **Build**: Gradle para gerenciamento de dependências

### Python
- **GUI**: Tkinter para interface gráfica
- **Gráficos**: Matplotlib para visualizações
- **Dados**: Pandas para manipulação eficiente de dados
- **CSV**: Pandas CSV reader (mais eficiente que csv padrão)
- **Concorrência**: Threading para processamento assíncrono
