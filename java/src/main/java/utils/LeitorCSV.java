package utils;

import model.GerenciadorDados;
import model.Partida;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.BiConsumer;

public class LeitorCSV {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    @FunctionalInterface
    public interface ProgressCallback {
        void onProgress(int progresso, String mensagem);
    }
    
    public static void carregarDadosCompletos(String caminhoArquivo, GerenciadorDados gerenciador, 
                                            ProgressCallback callback) throws Exception {
        
        callback.onProgress(0, "Lendo arquivo...");
        
        try (CSVReader reader = new CSVReader(new FileReader(caminhoArquivo))) {
            String[] headers = reader.readNext(); // Ler cabeçalho
            
            // Validar cabeçalho esperado
            if (headers == null || headers.length < 7) {
                throw new Exception("Formato de arquivo inválido. Esperado: ano,rodada,data,mandante,visitante,gols_mandante,gols_visitante");
            }
            
            String[] linha;
            int totalLinhas = 0;
            int linhasProcessadas = 0;
            
            // Primeira passada para contar linhas
            callback.onProgress(10, "Contando registros...");
            while ((linha = reader.readNext()) != null) {
                totalLinhas++;
            }
            
            // Segunda passada para processar dados
            callback.onProgress(20, "Processando dados...");
            
        } catch (Exception e) {
            // Reabrir arquivo para processamento real
        }
        
        // Processar arquivo novamente
        try (CSVReader reader = new CSVReader(new FileReader(caminhoArquivo))) {
            String[] headers = reader.readNext(); // Pular cabeçalho
            String[] linha;
            int linhasProcessadas = 0;
            int totalEstimado = 10000; // Estimativa para progresso
            
            while ((linha = reader.readNext()) != null) {
                try {
                    // Parsing dos dados - formato esperado:
                    // ano,rodada,data,mandante,visitante,gols_mandante,gols_visitante
                    int ano = Integer.parseInt(linha[0].trim());
                    int rodada = Integer.parseInt(linha[1].trim());
                    LocalDate data = LocalDate.parse(linha[2].trim(), DATE_FORMATTER);
                    String mandante = linha[3].trim();
                    String visitante = linha[4].trim();
                    int golsMandante = Integer.parseInt(linha[5].trim());
                    int golsVisitante = Integer.parseInt(linha[6].trim());
                    
                    Partida partida = new Partida(ano, rodada, data, mandante, visitante, 
                                                golsMandante, golsVisitante);
                    gerenciador.adicionarPartida(partida);
                    
                    linhasProcessadas++;
                    
                    // Atualizar progresso a cada 100 linhas
                    if (linhasProcessadas % 100 == 0) {
                        int progresso = Math.min(90, 20 + (linhasProcessadas * 70 / totalEstimado));
                        callback.onProgress(progresso, 
                            String.format("Processadas %d partidas...", linhasProcessadas));
                    }
                    
                } catch (Exception ex) {
                    // Log do erro, mas continua processamento
                    System.err.println("Erro na linha " + (linhasProcessadas + 2) + ": " + ex.getMessage());
                }
            }
            
            callback.onProgress(95, "Finalizando...");
            
            if (linhasProcessadas == 0) {
                throw new Exception("Nenhuma partida válida foi encontrada no arquivo.");
            }
            
            callback.onProgress(100, String.format("Carregamento concluído! %d partidas processadas.", 
                                                  linhasProcessadas));
        }
    }
}