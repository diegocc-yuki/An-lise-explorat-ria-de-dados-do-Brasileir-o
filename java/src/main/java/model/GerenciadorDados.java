package model;

import java.util.*;
import java.util.stream.Collectors;

public class GerenciadorDados {
    private List<Partida> todasPartidas;
    private Map<Integer, Set<String>> timesPorAno;
    
    public GerenciadorDados() {
        this.todasPartidas = new ArrayList<>();
        this.timesPorAno = new HashMap<>();
    }
    
    public void adicionarPartida(Partida partida) {
        todasPartidas.add(partida);
        
        // Manter registro dos times por ano
        timesPorAno.computeIfAbsent(partida.getAno(), k -> new HashSet<>())
            .add(partida.getMandante());
        timesPorAno.computeIfAbsent(partida.getAno(), k -> new HashSet<>())
            .add(partida.getVisitante());
    }
    
    public Set<Integer> getAnosDisponiveis() {
        return new TreeSet<>(timesPorAno.keySet());
    }
    
    public Set<String> getTimesDoAno(int ano) {
        return timesPorAno.getOrDefault(ano, new HashSet<>());
    }
    
    public Campeonato criarCampeonatoParaAno(int ano) {
        List<Partida> partidasDoAno = todasPartidas.stream()
            .filter(p -> p.getAno() == ano)
            .collect(Collectors.toList());
        
        Campeonato campeonato = new Campeonato(ano);
        for (Partida partida : partidasDoAno) {
            campeonato.adicionarPartida(partida);
        }
        
        campeonato.calcularEstatisticas();
        return campeonato;
    }
    
    public boolean temDadosCarregados() {
        return !todasPartidas.isEmpty();
    }
    
    public int getTotalPartidas() {
        return todasPartidas.size();
    }
}