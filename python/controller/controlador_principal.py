import threading
from typing import Optional
from model.gerenciador_dados import GerenciadorDados
from model.campeonato import Campeonato
from utils.leitor_csv import LeitorCSV
from view.tela_inicial import TelaInicial
from view.tela_analise import TelaAnalise

class ControladorPrincipal:
    def __init__(self):
        self.tela_inicial = TelaInicial()
        self.tela_analise = TelaAnalise()
        self.gerenciador_dados = GerenciadorDados()
        self.campeonato_atual: Optional[Campeonato] = None
        
        self._configurar_listeners()
    
    def iniciar(self):
        """Inicia a aplicação"""
        self.tela_inicial.show()
    
    def _configurar_listeners(self):
        """Configura todos os listeners"""
        self.tela_inicial.set_carregar_dados_listener(self._carregar_dados_completos)
        self.tela_inicial.set_ano_change_listener(self._carregar_times_do_ano)
        self.tela_inicial.set_confirmar_listener(self._analisar_time)
        self.tela_analise.set_voltar_listener(self._voltar_tela_inicial)
    
    def _carregar_dados_completos(self):
        """Carrega dados completos em thread separada"""
        def carregar():
            try:
                self.tela_inicial.mostrar_carregamento(True)
                
                # Carregar dados do arquivo
                nome_arquivo = "data/brasileirao_completo.csv"
                LeitorCSV.carregar_dados_completos(
                    nome_arquivo, 
                    self.gerenciador_dados,
                    self.tela_inicial.set_progresso_carregamento
                )
                
                if self.gerenciador_dados.tem_dados_carregados():
                    # Carregar anos disponíveis
                    anos = sorted(self.gerenciador_dados.get_anos_disponiveis(), reverse=True)
                    self.tela_inicial.set_anos(anos)
                    
                    self.tela_inicial.dados_carregados(
                        True,
                        f"Dados carregados com sucesso! {self.gerenciador_dados.get_total_partidas()} partidas processadas."
                    )
                else:
                    self.tela_inicial.dados_carregados(False, "Nenhum dado foi carregado.")
            
            except Exception as ex:
                self.tela_inicial.dados_carregados(False, f"Erro ao carregar dados: {str(ex)}")
                self.tela_inicial.mostrar_erro(
                    f"Erro ao carregar dados:\n{str(ex)}\n\n"
                    "Verifique se o arquivo 'data/brasileirao_completo.csv' existe."
                )
        
        # Executar em thread separada
        thread = threading.Thread(target=carregar)
        thread.daemon = True
        thread.start()
    
    def _carregar_times_do_ano(self):
        """Carrega times do ano selecionado"""
        ano = self.tela_inicial.get_ano_selecionado()
        if ano is None or not self.gerenciador_dados.tem_dados_carregados():
            return
        
        try:
            # Criar campeonato para o ano selecionado
            self.campeonato_atual = self.gerenciador_dados.criar_campeonato_para_ano(ano)
            
            # Atualizar combo de times
            nomes_dos_times = sorted(list(self.campeonato_atual.get_nomes_dos_times()))
            self.tela_inicial.set_times(nomes_dos_times)
        
        except Exception as ex:
            self.tela_inicial.mostrar_erro(f"Erro ao processar dados do ano {ano}: {str(ex)}")
            self.tela_inicial.set_times([])
    
    def _analisar_time(self):
        """Analisa o time selecionado"""
        nome_time = self.tela_inicial.get_time_selecionado()
        if not nome_time or self.campeonato_atual is None:
            return
        
        time = self.campeonato_atual.get_time(nome_time)
        if time is None:
            self.tela_inicial.mostrar_erro("Time não encontrado!")
            return
        
        # Mostrar tela de análise
        self.tela_analise.exibir_analise(time, self.campeonato_atual.ano)
        self.tela_inicial.hide()
        self.tela_analise.show()
    
    def _voltar_tela_inicial(self):
        """Volta para tela inicial"""
        self.tela_analise.hide()
        self.tela_inicial.show()
