import os
import csv
from datetime import datetime, timedelta
import random

def criar_dados_ficticios():
    """Cria arquivo CSV com dados fictícios para teste"""
    
    # Criar diretório data se não existir
    os.makedirs('data', exist_ok=True)
    
    # Times fictícios
    times = [
        'Flamengo', 'Palmeiras', 'Atlético-MG', 'Corinthians',
        'São Paulo', 'Internacional', 'Grêmio', 'Santos',
        'Botafogo', 'Vasco', 'Fluminense', 'Atlético-PR',
        'Cruzeiro', 'Bahia', 'Goiás', 'Coritiba',
        'América-MG', 'Fortaleza', 'Ceará', 'Sport'
    ]
    
    # Gerar partidas fictícias para 2023
    partidas = []
    data_inicio = datetime(2023, 4, 15)
    
    # Simular um campeonato completo (38 rodadas)
    for r in range(1, 39):
        # Em cada rodada, cada time joga uma vez (10 jogos por rodada)
        times_rodada = times.copy()
        random.shuffle(times_rodada)
        
        for i in range(0, len(times_rodada), 2):
            if i + 1 < len(times_rodada):
                mandante = times_rodada[i]
                visitante = times_rodada[i + 1]
                
                # Gerar resultado aleatório
                gols_mandante = random.randint(0, 4)
                gols_visitante = random.randint(0, 4)
                
                # Data da partida (avançar alguns dias a cada rodada)
                data_partida = data_inicio + timedelta(days=(r-1) * 7 + i//2)
                
                partidas.append([
                    2023, r, data_partida.strftime('%d/%m/%Y'),
                    mandante, visitante, gols_mandante, gols_visitante
                ])
    
    # Escrever arquivo CSV
    with open('data/brasileirao_completo.csv', 'w', newline='', encoding='utf-8') as arquivo:
        writer = csv.writer(arquivo)
        writer.writerow(['ano', 'rodada', 'data', 'mandante', 'visitante', 'gols_mandante', 'gols_visitante'])
        writer.writerows(partidas)
    
    print(f"Arquivo CSV criado com {len(partidas)} partidas!")
    print("Execute 'python main.py' para usar o sistema.")

if __name__ == "__main__":
    criar_dados_ficticios()