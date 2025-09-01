from typing import List, Dict

class Time:
    def __init__(self, nome: str):
        self.nome = nome
        self.pontos = 0
        self.jogos = 0
        self.vitorias = 0
        self.empates = 0
        self.derrotas = 0
        self.gols_feitos = 0
        self.gols_sofridos = 0
        self.historico_rodadas = []
        self.posicoes_por_rodada = {}  # rodada -> posição
    
    def reiniciar(self):
        """Reinicia todas as estatísticas do time"""
        self.pontos = 0
        self.jogos = 0
        self.vitorias = 0
        self.empates = 0
        self.derrotas = 0
        self.gols_feitos = 0
        self.gols_sofridos = 0
        self.historico_rodadas.clear()
        self.posicoes_por_rodada.clear()
    
    def adicionar_resultado_sem_posicao(self, gols_feitos: int, gols_sofridos: int, rodada: int):
        """Adiciona resultado de uma partida sem calcular posição"""
        self.jogos += 1
        self.gols_feitos += gols_feitos
        self.gols_sofridos += gols_sofridos
        
        # Adicionar rodada se ainda não foi adicionada
        if rodada not in self.historico_rodadas:
            self.historico_rodadas.append(rodada)
        
        # Calcular pontos
        if gols_feitos > gols_sofridos:
            self.vitorias += 1
            self.pontos += 3
        elif gols_feitos == gols_sofridos:
            self.empates += 1
            self.pontos += 1
        else:
            self.derrotas += 1
    
    def atualizar_posicao_para_rodada(self, rodada: int, posicao: int):
        """Atualiza a posição para uma rodada específica"""
        self.posicoes_por_rodada[rodada] = posicao
    
    def get_saldo_gols(self) -> int:
        """Retorna o saldo de gols"""
        return self.gols_feitos - self.gols_sofridos
    
    def get_aproveitamento(self) -> float:
        """Retorna o aproveitamento em percentual"""
        if self.jogos == 0:
            return 0.0
        return (self.pontos * 100.0) / (self.jogos * 3)
    
    def get_historico_rodadas(self) -> List[int]:
        """Retorna as rodadas ordenadas"""
        return sorted(self.historico_rodadas)
    
    def get_historico_posicoes(self) -> List[int]:
        """Retorna as posições ordenadas por rodada"""
        rodadas_ordenadas = self.get_historico_rodadas()
        posicoes_ordenadas = []
        
        for rodada in rodadas_ordenadas:
            posicao = self.posicoes_por_rodada.get(rodada, 20)  # Default 20
            posicoes_ordenadas.append(posicao)
        
        return posicoes_ordenadas
    
    def imprimir_historico(self):
        """Imprime o histórico para debug"""
        print(f"=== Histórico do {self.nome} ===")
        rodadas = self.get_historico_rodadas()
        posicoes = self.get_historico_posicoes()
        
        for rodada, posicao in zip(rodadas, posicoes):
            print(f"Rodada {rodada}: {posicao}ª posição")
        print("============================")
