from datetime import date

class Partida:
    def __init__(self, ano: int, rodada: int, data: date, mandante: str, 
                 visitante: str, gols_mandante: int, gols_visitante: int):
        self.ano = ano
        self.rodada = rodada
        self.data = data
        self.mandante = mandante
        self.visitante = visitante
        self.gols_mandante = gols_mandante
        self.gols_visitante = gols_visitante
    
    def contem_time(self, nome_time: str) -> bool:
        """Verifica se o time participa desta partida"""
        return self.mandante == nome_time or self.visitante == nome_time
    
    def get_resultado_para(self, nome_time: str) -> str:
        """Retorna V, E ou D para o time especificado"""
        if self.mandante == nome_time:
            if self.gols_mandante > self.gols_visitante:
                return "V"
            elif self.gols_mandante == self.gols_visitante:
                return "E"
            else:
                return "D"
        elif self.visitante == nome_time:
            if self.gols_visitante > self.gols_mandante:
                return "V"
            elif self.gols_visitante == self.gols_mandante:
                return "E"
            else:
                return "D"
        return ""
    
    def get_gols_feitos_por(self, nome_time: str) -> int:
        """Retorna gols feitos pelo time"""
        if self.mandante == nome_time:
            return self.gols_mandante
        elif self.visitante == nome_time:
            return self.gols_visitante
        return 0
    
    def get_gols_sofridos_por(self, nome_time: str) -> int:
        """Retorna gols sofridos pelo time"""
        if self.mandante == nome_time:
            return self.gols_visitante
        elif self.visitante == nome_time:
            return self.gols_mandante
        return 0
