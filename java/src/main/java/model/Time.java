package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Time {
    private String nome;
    private int pontos;
    private int jogos;
    private int vitorias;
    private int empates;
    private int derrotas;
    private int golsFeitos;
    private int golsSofridos;
    private List<Integer> historicoRodadas;
    private Map<Integer, Integer> posicoesPorRodada; // NOVA estrutura: rodada -> posição
    
    public Time(String nome) {
        this.nome = nome;
        this.pontos = 0;
        this.jogos = 0;
        this.vitorias = 0;
        this.empates = 0;
        this.derrotas = 0;
        this.golsFeitos = 0;
        this.golsSofridos = 0;
        this.historicoRodadas = new ArrayList<>();
        this.posicoesPorRodada = new HashMap<>();
    }
    
    public void reiniciar() {
        this.pontos = 0;
        this.jogos = 0;
        this.vitorias = 0;
        this.empates = 0;
        this.derrotas = 0;
        this.golsFeitos = 0;
        this.golsSofridos = 0;
        this.historicoRodadas.clear();
        this.posicoesPorRodada.clear();
    }
    
    // MÉTODO CORRIGIDO: Adiciona resultado sem posição
    public void adicionarResultadoSemPosicao(int golsFeitos, int golsSofridos, int rodada) {
        this.jogos++;
        this.golsFeitos += golsFeitos;
        this.golsSofridos += golsSofridos;
        
        // Adicionar rodada se ainda não foi adicionada
        if (!historicoRodadas.contains(rodada)) {
            this.historicoRodadas.add(rodada);
        }
        
        if (golsFeitos > golsSofridos) {
            this.vitorias++;
            this.pontos += 3;
        } else if (golsFeitos == golsSofridos) {
            this.empates++;
            this.pontos += 1;
        } else {
            this.derrotas++;
        }
    }
    
    // NOVO MÉTODO: Atualiza posição para uma rodada específica
    public void atualizarPosicaoParaRodada(int rodada, int posicao) {
        posicoesPorRodada.put(rodada, posicao);
    }
    
    // MÉTODO CORRIGIDO: Retorna posições ordenadas por rodada
    public List<Integer> getHistoricoPosicoes() {
        List<Integer> rodadasOrdenadas = new ArrayList<>(historicoRodadas);
        rodadasOrdenadas.sort(Integer::compareTo);
        
        List<Integer> posicoesOrdenadas = new ArrayList<>();
        for (Integer rodada : rodadasOrdenadas) {
            Integer posicao = posicoesPorRodada.get(rodada);
            posicoesOrdenadas.add(posicao != null ? posicao : 20); // Default 20 se não encontrar
        }
        
        return posicoesOrdenadas;
    }
    
    // Método antigo mantido para compatibilidade (mas não usado)
    @Deprecated
    public void adicionarResultado(int golsFeitos, int golsSofridos, int rodada, int posicao) {
        adicionarResultadoSemPosicao(golsFeitos, golsSofridos, rodada);
        atualizarPosicaoParaRodada(rodada, posicao);
    }
    
    public int getSaldoGols() {
        return golsFeitos - golsSofridos;
    }
    
    public double getAproveitamento() {
        if (jogos == 0) return 0.0;
        return (pontos * 100.0) / (jogos * 3);
    }
    
    // Getters
    public String getNome() { return nome; }
    public int getPontos() { return pontos; }
    public int getJogos() { return jogos; }
    public int getVitorias() { return vitorias; }
    public int getEmpates() { return empates; }
    public int getDerrotas() { return derrotas; }
    public int getGolsFeitos() { return golsFeitos; }
    public int getGolsSofridos() { return golsSofridos; }
    
    public List<Integer> getHistoricoRodadas() { 
        List<Integer> rodadasOrdenadas = new ArrayList<>(historicoRodadas);
        rodadasOrdenadas.sort(Integer::compareTo);
        return rodadasOrdenadas;
    }
    
    // NOVO MÉTODO: Para debug
    public void imprimirHistorico() {
        System.out.println("=== Histórico do " + nome + " ===");
        List<Integer> rodadas = getHistoricoRodadas();
        List<Integer> posicoes = getHistoricoPosicoes();
        
        for (int i = 0; i < rodadas.size(); i++) {
            System.out.printf("Rodada %d: %dª posição%n", rodadas.get(i), posicoes.get(i));
        }
        System.out.println("============================");
    }
}