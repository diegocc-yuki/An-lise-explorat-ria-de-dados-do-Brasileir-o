package model;

import java.util.*;
import java.util.stream.Collectors;

public class Campeonato {
    private int ano;
    private Map<String, Time> times;
    private List<Partida> partidas;
    
    public Campeonato(int ano) {
        this.ano = ano;
        this.times = new HashMap<>();
        this.partidas = new ArrayList<>();
    }
    
    public void adicionarPartida(Partida partida) {
        partidas.add(partida);
        
        // Adicionar times se não existirem
        times.putIfAbsent(partida.getMandante(), new Time(partida.getMandante()));
        times.putIfAbsent(partida.getVisitante(), new Time(partida.getVisitante()));
    }
    
    public void calcularEstatisticas() {
        // Reiniciar estatísticas de todos os times
        for (Time time : times.values()) {
            time.reiniciar();
        }
        
        // Agrupar partidas por rodada
        Map<Integer, List<Partida>> partidasPorRodada = partidas.stream()
            .collect(Collectors.groupingBy(Partida::getRodada));
        
        // Processar cada rodada em ordem
        List<Integer> rodadas = new ArrayList<>(partidasPorRodada.keySet());
        Collections.sort(rodadas);
        
        for (int rodada : rodadas) {
            List<Partida> partidasRodada = partidasPorRodada.get(rodada);
            
            // Atualizar estatísticas dos times para esta rodada
            for (Partida partida : partidasRodada) {
                Time timeMandante = times.get(partida.getMandante());
                Time timeVisitante = times.get(partida.getVisitante());
                
                // CORREÇÃO: Adicionar resultado SEM a posição (será calculada depois)
                timeMandante.adicionarResultadoSemPosicao(
                    partida.getGolsMandante(), 
                    partida.getGolsVisitante(), 
                    rodada
                );
                
                timeVisitante.adicionarResultadoSemPosicao(
                    partida.getGolsVisitante(), 
                    partida.getGolsMandante(), 
                    rodada
                );
            }
            
            // Calcular e atribuir posições na tabela após a rodada
            calcularEAtualizarPosicoes(rodada);
        }
    }
    
    private void calcularEAtualizarPosicoes(int rodada) {
        List<Time> tabela = new ArrayList<>(times.values());
        
        // Ordenar por critérios do brasileirão
        tabela.sort((t1, t2) -> {
            if (t1.getPontos() != t2.getPontos()) {
                return Integer.compare(t2.getPontos(), t1.getPontos());
            }
            if (t1.getVitorias() != t2.getVitorias()) {
                return Integer.compare(t2.getVitorias(), t1.getVitorias());
            }
            if (t1.getSaldoGols() != t2.getSaldoGols()) {
                return Integer.compare(t2.getSaldoGols(), t1.getSaldoGols());
            }
            return Integer.compare(t2.getGolsFeitos(), t1.getGolsFeitos());
        });
        
        // Atualizar posições no histórico de cada time
        for (int i = 0; i < tabela.size(); i++) {
            Time time = tabela.get(i);
            int posicao = i + 1;
            
            // CORREÇÃO: Atualizar a posição para a rodada específica
            time.atualizarPosicaoParaRodada(rodada, posicao);
        }
    }
    
    public Set<String> getNomesDosTimes() {
        return new HashSet<>(times.keySet());
    }
    
    public Time getTime(String nome) {
        return times.get(nome);
    }
    
    // Getters
    public int getAno() { return ano; }
    public List<Partida> getPartidas() { return new ArrayList<>(partidas); }
}