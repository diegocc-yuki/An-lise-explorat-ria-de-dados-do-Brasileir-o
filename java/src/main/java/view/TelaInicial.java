package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class TelaInicial extends JFrame {
    private JComboBox<Integer> comboAno;
    private JComboBox<String> comboTime;
    private JButton btnConfirmar;
    private JButton btnCarregarDados;
    private JLabel lblTitulo;
    private JLabel lblStatus;
    private JProgressBar progressBar;
    
    public TelaInicial() {
        initComponents();
        setupLayout();
    }
    
    private void initComponents() {
        setTitle("Análise do Campeonato Brasileiro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        
        // Componentes
        lblTitulo = new JLabel("Análise do Campeonato Brasileiro", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(0, 100, 0));
        
        // Componentes novos para carregamento
        lblStatus = new JLabel("Clique em 'Carregar Dados' para iniciar", SwingConstants.CENTER);
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 12));
        lblStatus.setForeground(Color.GRAY);
        
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);
        
        btnCarregarDados = new JButton("Carregar Dados");
        btnCarregarDados.setFont(new Font("Arial", Font.BOLD, 14));
        btnCarregarDados.setBackground(new Color(0, 100, 150));
        btnCarregarDados.setForeground(Color.WHITE);
        
        // ComboBox para anos (inicialmente vazio, será preenchido após carregar dados)
        comboAno = new JComboBox<>();
        comboAno.setFont(new Font("Arial", Font.PLAIN, 14));
        comboAno.setEnabled(false);
        
        // ComboBox para times (inicialmente vazio)
        comboTime = new JComboBox<>();
        comboTime.setFont(new Font("Arial", Font.PLAIN, 14));
        comboTime.setEnabled(false);
        
        btnConfirmar = new JButton("Analisar Time");
        btnConfirmar.setFont(new Font("Arial", Font.BOLD, 16));
        btnConfirmar.setBackground(new Color(0, 120, 0));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setEnabled(false);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Painel principal
        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Título
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 20, 20, 20);
        painelPrincipal.add(lblTitulo, gbc);
        
        // Status
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 20, 5, 20);
        painelPrincipal.add(lblStatus, gbc);
        
        // Progress Bar
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 50, 15, 50);
        painelPrincipal.add(progressBar, gbc);
        
        // Botão Carregar Dados
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 20, 20, 20);
        painelPrincipal.add(btnCarregarDados, gbc);
        
        // Label e ComboBox Ano
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 20, 10, 10);
        gbc.anchor = GridBagConstraints.EAST;
        painelPrincipal.add(new JLabel("Selecione o ano:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 20);
        painelPrincipal.add(comboAno, gbc);
        
        // Label e ComboBox Time
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 20, 10, 10);
        painelPrincipal.add(new JLabel("Selecione o time:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 20);
        painelPrincipal.add(comboTime, gbc);
        
        // Botão Confirmar
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 20, 20, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        painelPrincipal.add(btnConfirmar, gbc);
        
        add(painelPrincipal, BorderLayout.CENTER);
    }
    
    // Métodos para controlar interface
    public void mostrarCarregamento(boolean mostrar) {
        progressBar.setVisible(mostrar);
        btnCarregarDados.setEnabled(!mostrar);
        if (mostrar) {
            lblStatus.setText("Carregando dados...");
        }
    }
    
    public void setProgressoCarregamento(int progresso, String mensagem) {
        progressBar.setValue(progresso);
        lblStatus.setText(mensagem);
    }
    
    public void dadosCarregados(boolean sucesso, String mensagem) {
        progressBar.setVisible(false);
        lblStatus.setText(mensagem);
        if (sucesso) {
            btnCarregarDados.setVisible(false);
            comboAno.setEnabled(true);
        } else {
            btnCarregarDados.setEnabled(true);
        }
    }
    
    // Métodos para configurar listeners
    public void setCarregarDadosListener(ActionListener listener) {
        btnCarregarDados.addActionListener(listener);
    }
    
    public void setAnoChangeListener(ActionListener listener) {
        comboAno.addActionListener(listener);
    }
    
    public void setConfirmarListener(ActionListener listener) {
        btnConfirmar.addActionListener(listener);
    }
    
    // Métodos para atualizar interface
    public void setAnos(List<Integer> anos) {
        comboAno.removeAllItems();
        for (Integer ano : anos) {
            comboAno.addItem(ano);
        }
    }
    
    public void setTimes(List<String> times) {
        comboTime.removeAllItems();
        for (String time : times) {
            comboTime.addItem(time);
        }
        comboTime.setEnabled(!times.isEmpty());
        btnConfirmar.setEnabled(!times.isEmpty());
    }
    
    public void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }
    
    // Getters
    public Integer getAnoSelecionado() {
        return (Integer) comboAno.getSelectedItem();
    }
    
    public String getTimeSelecionado() {
        return (String) comboTime.getSelectedItem();
    }
}