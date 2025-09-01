from typing import Dict, List, Set
from collections import defaultdict
from .time import Time
from .partida import Partida

class Campeonato:
    def __init__(self, ano: int):
        self.ano = ano
        self.times: Dict[str, Time] = {}
        self.partidas: List[Partida] = []
    
    def adicionar_partida(self, partida: Partida):
        """Adiciona uma partida ao campeonato"""
        self.partidas.append(partida)
        
        # Adicionar times se não existirem
        if partida.mandante not in self.times:
            self.times[partida.mandante] = Time(partida.mandante)
        if partida.visitante not in self.times:
            self.times[partida.visitante] = Time(partida.visitante)
    
    def calcular_estatisticas(self):
        """Calcula todas as estatísticas do campeonato"""
        # Reiniciar estatísticas de todos os times
        for time in self.times.values():
            time.reiniciar()
        
        # Agrupar partidas por rodada
        partidas_por_rodada = defaultdict(list)
        for partida in self.partidas:
            partidas_por_rodada[partida.rodada].append(partida)
        
        # Processar cada rodada em ordem
        rodadas = sorted(partidas_por_rodada.keys())
        
        for rodada in rodadas:
            partidas_rodada = partidas_por_rodada[rodada]
            
            # Atualizar estatísticas dos times para esta rodada
            for partida in partidas_rodada:
                time_mandante = self.times[partida.mandante]
                time_visitante = self.times[partida.visitante]
                
                time_mandante.adicionar_resultado_sem_posicao(
                    partida.gols_mandante,
                    partida.gols_visitante,
                    rodada
                )
                
                time_visitante.adicionar_resultado_sem_posicao(
                    partida.gols_visitante,
                    partida.gols_mandante,
                    rodada
                )
            
            # Calcular e atribuir posições na tabela após a rodada
            self._calcular_e_atualizar_posicoes(rodada)
    
    def _calcular_e_atualizar_posicoes(self, rodada: int):
        """Calcula as posições na tabela após uma rodada"""
        tabela = list(self.times.values())
        
        # Ordenar por critérios do brasileirão
        tabela.sort(key=lambda t: (
            -t.pontos,           # Mais pontos primeiro
            -t.vitorias,         # Mais vitórias primeiro
            -t.get_saldo_gols(), # Melhor saldo primeiro
            -t.gols_feitos       # Mais gols feitos primeiro
        ))
        
        # Atualizar posições no histórico de cada time
        for i, time in enumerate(tabela):
            posicao = i + 1
            time.atualizar_posicao_para_rodada(rodada, posicao)
    
    def get_nomes_dos_times(self) -> Set[str]:
        """Retorna nomes de todos os times"""
        return set(self.times.keys())
    
    def get_time(self, nome: str) -> Time:
        """Retorna um time pelo nome"""
        return self.times.get(nome)
