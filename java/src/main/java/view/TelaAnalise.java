package view;

import model.Time;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class TelaAnalise extends JFrame {
    private JPanel painelEstatisticas;
    private JPanel painelGrafico;
    private JButton btnVoltar;
    private JLabel lblTitulo;
    
    public TelaAnalise() {
        initComponents();
        setupLayout();
    }
    
    private void initComponents() {
        setTitle("Análise Detalhada do Time");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        
        lblTitulo = new JLabel("", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(0, 100, 0));
        
        painelEstatisticas = new JPanel();
        painelEstatisticas.setBackground(Color.WHITE);
        painelEstatisticas.setBorder(BorderFactory.createTitledBorder("Estatísticas do Time"));
        
        painelGrafico = new JPanel(new BorderLayout());
        painelGrafico.setBorder(BorderFactory.createTitledBorder("Evolução na Tabela"));
        
        btnVoltar = new JButton("Voltar");
        btnVoltar.setFont(new Font("Arial", Font.PLAIN, 14));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Título no topo
        add(lblTitulo, BorderLayout.NORTH);
        
        // Painel central com estatísticas e gráfico
        JPanel painelCentral = new JPanel(new GridLayout(1, 2, 10, 10));
        painelCentral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        painelCentral.add(painelEstatisticas);
        painelCentral.add(painelGrafico);
        
        add(painelCentral, BorderLayout.CENTER);
        
        // Botão voltar na parte inferior
        JPanel painelBotao = new JPanel(new FlowLayout());
        painelBotao.add(btnVoltar);
        add(painelBotao, BorderLayout.SOUTH);
    }
    
    public void exibirAnalise(Time time, int ano) {
        lblTitulo.setText(String.format("Análise: %s - Campeonato %d", time.getNome(), ano));
        
        // DEBUG: Imprimir histórico no console
        time.imprimirHistorico();
        
        // Atualizar painel de estatísticas
        atualizarEstatisticas(time);
        
        // Atualizar gráfico
        atualizarGrafico(time);
    }
    
    private void atualizarEstatisticas(Time time) {
        painelEstatisticas.removeAll();
        painelEstatisticas.setLayout(new GridLayout(0, 1, 5, 5));
        painelEstatisticas.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Estatísticas do Time"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Criar labels com as estatísticas
        painelEstatisticas.add(criarLabelEstatistica("Time:", time.getNome()));
        painelEstatisticas.add(criarLabelEstatistica("Jogos:", String.valueOf(time.getJogos())));
        painelEstatisticas.add(criarLabelEstatistica("Pontos:", String.valueOf(time.getPontos())));
        painelEstatisticas.add(criarLabelEstatistica("Vitórias:", String.valueOf(time.getVitorias())));
        painelEstatisticas.add(criarLabelEstatistica("Empates:", String.valueOf(time.getEmpates())));
        painelEstatisticas.add(criarLabelEstatistica("Derrotas:", String.valueOf(time.getDerrotas())));
        painelEstatisticas.add(criarLabelEstatistica("Gols Feitos:", String.valueOf(time.getGolsFeitos())));
        painelEstatisticas.add(criarLabelEstatistica("Gols Sofridos:", String.valueOf(time.getGolsSofridos())));
        painelEstatisticas.add(criarLabelEstatistica("Saldo de Gols:", String.valueOf(time.getSaldoGols())));
        painelEstatisticas.add(criarLabelEstatistica("Aproveitamento:", 
            String.format("%.1f%%", time.getAproveitamento())));
        
        painelEstatisticas.revalidate();
        painelEstatisticas.repaint();
    }
    
    private JLabel criarLabelEstatistica(String titulo, String valor) {
        JLabel label = new JLabel(String.format("<html><b>%s</b> %s</html>", titulo, valor));
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        return label;
    }
    
    private void atualizarGrafico(Time time) {
        painelGrafico.removeAll();
        
        // Criar dataset para o gráfico
        XYSeries series = new XYSeries("Posição na Tabela");
        
        List<Integer> rodadas = time.getHistoricoRodadas();
        List<Integer> posicoes = time.getHistoricoPosicoes();
        
        System.out.println("=== DADOS DO GRÁFICO ===");
        System.out.println("Time: " + time.getNome());
        System.out.println("Rodadas: " + rodadas);
        System.out.println("Posições: " + posicoes);
        
        if (rodadas.size() != posicoes.size()) {
            System.err.println("ERRO: Tamanhos diferentes! Rodadas: " + rodadas.size() + 
                             ", Posições: " + posicoes.size());
        }
        
        // Adicionar pontos ao gráfico
        for (int i = 0; i < Math.min(rodadas.size(), posicoes.size()); i++) {
            series.add(rodadas.get(i), posicoes.get(i));
            System.out.printf("Adicionando ponto: Rodada %d, Posição %d%n", 
                            rodadas.get(i), posicoes.get(i));
        }
        
        if (series.getItemCount() == 0) {
            // Se não há dados, mostrar mensagem
            JLabel lblSemDados = new JLabel("Dados não disponíveis para o gráfico", SwingConstants.CENTER);
            lblSemDados.setFont(new Font("Arial", Font.BOLD, 16));
            lblSemDados.setForeground(Color.RED);
            painelGrafico.add(lblSemDados, BorderLayout.CENTER);
        } else {
            XYSeriesCollection dataset = new XYSeriesCollection(series);
            
            // Criar gráfico
            JFreeChart chart = ChartFactory.createXYLineChart(
                "Evolução da Posição na Tabela",
                "Rodada",
                "Posição",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
            );
            
            // Personalizar gráfico
            XYPlot plot = chart.getXYPlot();
            plot.getRangeAxis().setInverted(true); // Inverter eixo Y (1º lugar no topo)
            plot.setBackgroundPaint(Color.WHITE);
            plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
            plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
            
            // Melhorar aparência da linha
            XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
            renderer.setSeriesShapesVisible(0, true);
            renderer.setSeriesShapesFilled(0, true);
            renderer.setSeriesStroke(0, new BasicStroke(2.0f));
            plot.setRenderer(renderer);
            
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(500, 400));
            
            painelGrafico.add(chartPanel, BorderLayout.CENTER);
        }
        
        painelGrafico.revalidate();
        painelGrafico.repaint();
        System.out.println("=== FIM DADOS GRÁFICO ===");
    }
    
    public void setVoltarListener(ActionListener listener) {
        btnVoltar.addActionListener(listener);
    }
}