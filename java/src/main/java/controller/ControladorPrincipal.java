package controller;

import model.Campeonato;
import model.GerenciadorDados;
import model.Time;
import utils.LeitorCSV;
import view.TelaAnalise;
import view.TelaInicial;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ControladorPrincipal {
    private TelaInicial telaInicial;
    private TelaAnalise telaAnalise;
    private GerenciadorDados gerenciadorDados;
    private Campeonato campeonatoAtual;
    
    public ControladorPrincipal() {
        this.telaInicial = new TelaInicial();
        this.telaAnalise = new TelaAnalise();
        this.gerenciadorDados = new GerenciadorDados();
        
        configurarListeners();
    }
    
    public void iniciar() {
        telaInicial.setVisible(true);
    }
    
    private void configurarListeners() {
        // Listener para carregar dados
        telaInicial.setCarregarDadosListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                carregarDadosCompletos();
            }
        });
        
        // Listener para mudança de ano
        telaInicial.setAnoChangeListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                carregarTimesDoAno();
            }
        });
        
        // Listener para confirmar análise
        telaInicial.setConfirmarListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                analisarTime();
            }
        });
        
        // Listener para voltar
        telaAnalise.setVoltarListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                voltarTelaInicial();
            }
        });
    }
    
    private void carregarDadosCompletos() {
        SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    telaInicial.mostrarCarregamento(true);
                    publish("Iniciando carregamento...");
                    
                    // Carregar dados do arquivo único
                    String nomeArquivo = "data/brasileirao_completo.csv";
                    LeitorCSV.carregarDadosCompletos(nomeArquivo, gerenciadorDados, 
                        (progresso, mensagem) -> {
                            setProgress(progresso);
                            publish(mensagem);
                        });
                    
                    return null;
                } catch (Exception ex) {
                    throw ex;
                }
            }
            
            @Override
            protected void process(List<String> chunks) {
                if (!chunks.isEmpty()) {
                    String ultimaMensagem = chunks.get(chunks.size() - 1);
                    telaInicial.setProgressoCarregamento(getProgress(), ultimaMensagem);
                }
            }
            
            @Override
            protected void done() {
                try {
                    get();
                    
                    if (gerenciadorDados.temDadosCarregados()) {
                        // Carregar anos disponíveis
                        List<Integer> anos = new ArrayList<>(gerenciadorDados.getAnosDisponiveis());
                        anos.sort((a, b) -> b.compareTo(a)); // Ordenar decrescente
                        telaInicial.setAnos(anos);
                        
                        telaInicial.dadosCarregados(true, 
                            String.format("Dados carregados com sucesso! %d partidas processadas.", 
                            gerenciadorDados.getTotalPartidas()));
                    } else {
                        telaInicial.dadosCarregados(false, "Nenhum dado foi carregado.");
                    }
                    
                } catch (Exception ex) {
                    telaInicial.dadosCarregados(false, "Erro ao carregar dados: " + ex.getMessage());
                    telaInicial.mostrarErro("Erro ao carregar dados:\n" + ex.getMessage() + 
                        "\n\nVerifique se o arquivo 'data/brasileirao_completo.csv' existe.");
                }
            }
        };
        
        worker.execute();
    }
    
    private void carregarTimesDoAno() {
        Integer ano = telaInicial.getAnoSelecionado();
        if (ano == null || !gerenciadorDados.temDadosCarregados()) return;
        
        try {
            // Criar campeonato para o ano selecionado
            campeonatoAtual = gerenciadorDados.criarCampeonatoParaAno(ano);
            
            // Atualizar combo de times
            List<String> nomesDosTimes = new ArrayList<>(campeonatoAtual.getNomesDosTimes());
            nomesDosTimes.sort(String::compareTo);
            telaInicial.setTimes(nomesDosTimes);
            
        } catch (Exception ex) {
            telaInicial.mostrarErro("Erro ao processar dados do ano " + ano + ": " + ex.getMessage());
            telaInicial.setTimes(new ArrayList<>());
        }
    }
    
    private void analisarTime() {
        String nomeTime = telaInicial.getTimeSelecionado();
        if (nomeTime == null || campeonatoAtual == null) return;
        
        Time time = campeonatoAtual.getTime(nomeTime);
        if (time == null) {
            telaInicial.mostrarErro("Time não encontrado!");
            return;
        }
        
        // Mostrar tela de análise
        telaAnalise.exibirAnalise(time, campeonatoAtual.getAno());
        telaInicial.setVisible(false);
        telaAnalise.setVisible(true);
    }
    
    private void voltarTelaInicial() {
        telaAnalise.setVisible(false);
        telaInicial.setVisible(true);
    }
}