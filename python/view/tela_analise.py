import tkinter as tk
from tkinter import ttk
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
from matplotlib.figure import Figure
from typing import Callable
from model.time import Time

class TelaAnalise:
    def __init__(self):
        self.window = tk.Toplevel()
        self.window.title("Análise Detalhada do Time")
        self.window.geometry("1200x700")
        self.window.resizable(True, True)
        
        self._init_components()
        self._setup_layout()
        
        # Callback
        self._voltar_callback = None
    
    def _init_components(self):
        """Inicializa os componentes da interface"""
        # Título
        self.lbl_titulo = tk.Label(
            self.window,
            text="",
            font=("Arial", 20, "bold"),
            fg="#006400"
        )
        
        # Frame principal horizontal
        self.frame_principal = tk.Frame(self.window)
        
        # Frame estatísticas (lado esquerdo)
        self.frame_estatisticas = tk.LabelFrame(
            self.frame_principal,
            text="Estatísticas do Time",
            font=("Arial", 12, "bold"),
            padx=10,
            pady=10
        )
        
        # Frame gráfico (lado direito)
        self.frame_grafico = tk.LabelFrame(
            self.frame_principal,
            text="Evolução na Tabela",
            font=("Arial", 12, "bold"),
            padx=10,
            pady=10
        )
        
        # Botão voltar
        self.btn_voltar = tk.Button(
            self.window,
            text="Voltar",
            font=("Arial", 14),
            width=10,
            command=self._on_voltar
        )
    
    def _setup_layout(self):
        """Configura o layout da interface usando pack()"""
        # Título
        self.lbl_titulo.pack(pady=10)
        
        # Frame principal (horizontal)
        self.frame_principal.pack(fill="both", padx=10, pady=5)
        
        # Frames lado a lado
        self.frame_estatisticas.pack(side="left", fill="y", padx=5)
        self.frame_grafico.pack(side="right", fill="both", expand=True, padx=5)
        
        # Botão voltar
        self.btn_voltar.pack(pady=10)
    
    def _on_voltar(self):
        """Callback para voltar"""
        if self._voltar_callback:
            self._voltar_callback()
    
    def exibir_analise(self, time: Time, ano: int):
        """Exibe análise completa do time"""
        self.lbl_titulo.config(text=f"Análise: {time.nome} - Campeonato {ano}")
        
        # Debug
        time.imprimir_historico()
        
        # Atualizar estatísticas
        self._atualizar_estatisticas(time)
        
        # Atualizar gráfico
        self._atualizar_grafico(time)
    
    def _atualizar_estatisticas(self, time: Time):
        """Atualiza painel de estatísticas"""
        # Limpar frame
        for widget in self.frame_estatisticas.winfo_children():
            widget.destroy()
        
        # Criar labels com estatísticas
        estatisticas = [
            ("Time:", time.nome),
            ("Jogos:", str(time.jogos)),
            ("Pontos:", str(time.pontos)),
            ("Vitórias:", str(time.vitorias)),
            ("Empates:", str(time.empates)),
            ("Derrotas:", str(time.derrotas)),
            ("Gols Feitos:", str(time.gols_feitos)),
            ("Gols Sofridos:", str(time.gols_sofridos)),
            ("Saldo de Gols:", str(time.get_saldo_gols())),
            ("Aproveitamento:", f"{time.get_aproveitamento():.1f}%")
        ]
        
        for i, (titulo, valor) in enumerate(estatisticas):
            frame_stat = tk.Frame(self.frame_estatisticas)
            frame_stat.pack(fill="x", pady=2)
            
            lbl_titulo = tk.Label(
                frame_stat,
                text=titulo,
                font=("Arial", 12, "bold"),
                anchor="w"
            )
            lbl_titulo.pack(side="left")
            
            lbl_valor = tk.Label(
                frame_stat,
                text=valor,
                font=("Arial", 12),
                anchor="w"
            )
            lbl_valor.pack(side="left", padx=(10, 0))
    
    def _atualizar_grafico(self, time: Time):
        """Atualiza o gráfico de evolução"""
        # Limpar frame
        for widget in self.frame_grafico.winfo_children():
            widget.destroy()
        
        rodadas = time.get_historico_rodadas()
        posicoes = time.get_historico_posicoes()
        
        print("=== DADOS DO GRÁFICO ===")
        print(f"Time: {time.nome}")
        print(f"Rodadas: {rodadas}")
        print(f"Posições: {posicoes}")
        
        if not rodadas or len(rodadas) != len(posicoes):
            lbl_sem_dados = tk.Label(
                self.frame_grafico,
                text="Dados não disponíveis para o gráfico",
                font=("Arial", 16, "bold"),
                fg="red"
            )
            lbl_sem_dados.pack(expand=True)
            return
        
        # Criar figura matplotlib
        fig = Figure(figsize=(10, 6), dpi=100)
        ax = fig.add_subplot(111)
        
        # Plotar linha apenas onde há dados
        ax.plot(rodadas, posicoes, 'o-', linewidth=2, markersize=6, color='#1f77b4', label=time.nome)
        
        # Configurar eixos para mostrar escala completa
        ax.set_xlim(0, 39)  # Rodadas de 1 a 38
        ax.set_ylim(21, 0)  # Posições de 1 a 20 (invertido)
        
        # Configurar ticks para mostrar todas as rodadas e posições
        ax.set_xticks(range(1, 39))  # Todas as rodadas de 1 a 38
        ax.set_yticks(range(1, 21))  # Todas as posições de 1 a 20
        
        ax.set_xlabel('Rodada', fontsize=12)
        ax.set_ylabel('Posição', fontsize=12)
        ax.set_title('Evolução da Posição na Tabela', fontsize=14, fontweight='bold')
        ax.grid(True, alpha=0.3)
        ax.legend()
        
        # Adicionar linhas de referência para G4, G6, Z4
        ax.axhline(y=4, color='green', linestyle='--', alpha=0.7)
        ax.axhline(y=6, color='orange', linestyle='--', alpha=0.7)
        ax.axhline(y=17, color='red', linestyle='--', alpha=0.7)
        
        # Adicionar canvas ao frame
        canvas = FigureCanvasTkAgg(fig, self.frame_grafico)
        canvas.draw()
        canvas.get_tk_widget().pack(fill="both", expand=True)
        
        print("=== FIM DADOS GRÁFICO ===")
    
    def set_voltar_listener(self, callback: Callable):
        """Define callback para voltar"""
        self._voltar_callback = callback
    
    def show(self):
        """Mostra a janela"""
        self.window.deiconify()
    
    def hide(self):
        """Esconde a janela"""
        self.window.withdraw()
