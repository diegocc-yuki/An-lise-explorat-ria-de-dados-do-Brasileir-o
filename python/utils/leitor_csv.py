import csv
from datetime import datetime
from typing import Callable
from model.gerenciador_dados import GerenciadorDados
from model.partida import Partida

class LeitorCSV:
    @staticmethod
    def carregar_dados_completos(caminho_arquivo: str, gerenciador: GerenciadorDados, 
                               callback: Callable[[int, str], None]):
        """Carrega dados completos de um arquivo CSV"""
        try:
            callback(0, "Lendo arquivo...")
            
            with open(caminho_arquivo, 'r', encoding='utf-8') as arquivo:
                # Contar linhas para progresso
                total_linhas = sum(1 for _ in arquivo) - 1  # -1 para descontar cabeçalho
                
            callback(10, "Processando dados...")
            
            linhas_processadas = 0
            
            with open(caminho_arquivo, 'r', encoding='utf-8') as arquivo:
                reader = csv.reader(arquivo)
                headers = next(reader)  # Pular cabeçalho
                
                # Validar cabeçalho
                if len(headers) < 7:
                    raise Exception("Formato de arquivo inválido. Esperado: ano,rodada,data,mandante,visitante,gols_mandante,gols_visitante")
                
                for linha in reader:
                    try:
                        # Parsing dos dados
                        ano = int(linha[0].strip())
                        rodada = int(linha[1].strip())
                        data = datetime.strptime(linha[2].strip(), '%d/%m/%Y').date()
                        mandante = linha[3].strip()
                        visitante = linha[4].strip()
                        gols_mandante = int(linha[5].strip())
                        gols_visitante = int(linha[6].strip())
                        
                        partida = Partida(ano, rodada, data, mandante, visitante,
                                        gols_mandante, gols_visitante)
                        gerenciador.adicionar_partida(partida)
                        
                        linhas_processadas += 1
                        
                        # Atualizar progresso a cada 100 linhas
                        if linhas_processadas % 100 == 0:
                            progresso = min(90, 10 + (linhas_processadas * 80 // total_linhas))
                            callback(progresso, f"Processadas {linhas_processadas} partidas...")
                    
                    except Exception as ex:
                        print(f"Erro na linha {linhas_processadas + 2}: {ex}")
                        continue
            
            callback(95, "Finalizando...")
            
            if linhas_processadas == 0:
                raise Exception("Nenhuma partida válida foi encontrada no arquivo.")
            
            callback(100, f"Carregamento concluído! {linhas_processadas} partidas processadas.")
            
        except FileNotFoundError:
            raise Exception(f"Arquivo não encontrado: {caminho_arquivo}")
        except Exception as e:
            raise Exception(f"Erro ao processar arquivo: {str(e)}")
