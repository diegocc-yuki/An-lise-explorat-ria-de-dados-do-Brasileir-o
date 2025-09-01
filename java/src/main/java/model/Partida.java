package model;

import java.time.LocalDate;

public class Partida {
    private int ano;
    private int rodada;
    private LocalDate data;
    private String mandante;
    private String visitante;
    private int golsMandante;
    private int golsVisitante;
    
    public Partida(int ano, int rodada, LocalDate data, String mandante, String visitante, 
                   int golsMandante, int golsVisitante) {
        this.ano = ano;
        this.rodada = rodada;
        this.data = data;
        this.mandante = mandante;
        this.visitante = visitante;
        this.golsMandante = golsMandante;
        this.golsVisitante = golsVisitante;
    }
    
    public boolean contemTime(String nomeTime) {
        return mandante.equals(nomeTime) || visitante.equals(nomeTime);
    }
    
    public String getResultadoPara(String nomeTime) {
        if (mandante.equals(nomeTime)) {
            if (golsMandante > golsVisitante) return "V";
            else if (golsMandante == golsVisitante) return "E";
            else return "D";
        } else if (visitante.equals(nomeTime)) {
            if (golsVisitante > golsMandante) return "V";
            else if (golsVisitante == golsMandante) return "E";
            else return "D";
        }
        return "";
    }
    
    public int getGolsFeitosPor(String nomeTime) {
        if (mandante.equals(nomeTime)) return golsMandante;
        else if (visitante.equals(nomeTime)) return golsVisitante;
        return 0;
    }
    
    public int getGolsSofridosPor(String nomeTime) {
        if (mandante.equals(nomeTime)) return golsVisitante;
        else if (visitante.equals(nomeTime)) return golsMandante;
        return 0;
    }
    
    // Getters
    public int getAno() { return ano; }
    public int getRodada() { return rodada; }
    public LocalDate getData() { return data; }
    public String getMandante() { return mandante; }
    public String getVisitante() { return visitante; }
    public int getGolsMandante() { return golsMandante; }
    public int getGolsVisitante() { return golsVisitante; }
}