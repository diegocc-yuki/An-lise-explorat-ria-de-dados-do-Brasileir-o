from typing import Dict, List, Set
from collections import defaultdict
from .partida import Partida
from .campeonato import Campeonato

class GerenciadorDados:
    def __init__(self):
        self.todas_partidas: List[Partida] = []
        self.times_por_ano: Dict[int, Set[str]] = defaultdict(set)
    
    def adicionar_partida(self, partida: Partida):
        """Adiciona uma partida ao gerenciador"""
        self.todas_partidas.append(partida)
        
        # Manter registro dos times por ano
        self.times_por_ano[partida.ano].add(partida.mandante)
        self.times_por_ano[partida.ano].add(partida.visitante)
    
    def get_anos_disponiveis(self) -> Set[int]:
        """Retorna anos disponíveis"""
        return set(self.times_por_ano.keys())
    
    def get_times_do_ano(self, ano: int) -> Set[str]:
        """Retorna times de um ano específico"""
        return self.times_por_ano.get(ano, set())
    
    def criar_campeonato_para_ano(self, ano: int) -> Campeonato:
        """Cria um campeonato para um ano específico"""
        partidas_do_ano = [p for p in self.todas_partidas if p.ano == ano]
        
        campeonato = Campeonato(ano)
        for partida in partidas_do_ano:
            campeonato.adicionar_partida(partida)
        
        campeonato.calcular_estatisticas()
        return campeonato
    
    def tem_dados_carregados(self) -> bool:
        """Verifica se há dados carregados"""
        return len(self.todas_partidas) > 0
    
    def get_total_partidas(self) -> int:
        """Retorna total de partidas"""
        return len(self.todas_partidas)
