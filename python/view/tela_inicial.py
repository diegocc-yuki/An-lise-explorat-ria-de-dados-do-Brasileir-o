import tkinter as tk
from tkinter import ttk, messagebox
from typing import List, Callable, Optional

class TelaInicial:
    def __init__(self):
        self.window = tk.Toplevel()
        self.window.title("Análise do Campeonato Brasileiro")
        self.window.geometry("600x500")
        self.window.resizable(False, False)
        
        # Centralizar janela
        self.window.transient()
        self.window.grab_set()
        
        self._init_components()
        self._setup_layout()
        
        # Callbacks
        self._carregar_dados_callback = None
        self._ano_change_callback = None
        self._confirmar_callback = None
    
    def _init_components(self):
        """Inicializa os componentes da interface"""
        # Título
        self.lbl_titulo = tk.Label(
            self.window,
            text="Análise do Campeonato Brasileiro",
            font=("Arial", 24, "bold"),
            fg="#006400"
        )
        
        # Status
        self.lbl_status = tk.Label(
            self.window,
            text="Clique em 'Carregar Dados' para iniciar",
            font=("Arial", 12),
            fg="gray"
        )
        
        # Progress bar
        self.progress_bar = ttk.Progressbar(
            self.window,
            mode='determinate',
            length=400
        )
        
        # Botão carregar dados
        self.btn_carregar_dados = tk.Button(
            self.window,
            text="Carregar Dados",
            font=("Arial", 14, "bold"),
            bg="#006496",
            fg="white",
            width=15,
            height=1,
            command=self._on_carregar_dados
        )
        
        # ComboBox anos
        self.combo_ano = ttk.Combobox(
            self.window,
            font=("Arial", 14),
            state="disabled",
            width=15
        )
        self.combo_ano.bind("<<ComboboxSelected>>", self._on_ano_change)
        
        # ComboBox times
        self.combo_time = ttk.Combobox(
            self.window,
            font=("Arial", 14),
            state="disabled",
            width=25
        )
        
        # Botão confirmar
        self.btn_confirmar = tk.Button(
            self.window,
            text="Analisar Time",
            font=("Arial", 16, "bold"),
            bg="#007800",
            fg="white",
            width=15,
            height=1,
            state="disabled",
            command=self._on_confirmar
        )
    
    def _setup_layout(self):
        """Configura o layout da interface usando pack()"""
        # Título
        self.lbl_titulo.pack(pady=20)
        
        # Status
        self.lbl_status.pack(pady=10)
        
        # Botão carregar dados
        self.btn_carregar_dados.pack(pady=20)
        
        # Frame para seleções
        frame_selecoes = tk.Frame(self.window)
        frame_selecoes.pack(pady=30)
        
        # Ano - Layout horizontal
        frame_ano = tk.Frame(frame_selecoes)
        frame_ano.pack(pady=15, fill="x")
        
        tk.Label(frame_ano, text="Ano:", font=("Arial", 14), width=6, anchor="e").pack(side="left", padx=(0, 10))
        self.combo_ano.pack(side="left")
        
        # Time - Layout horizontal
        frame_time = tk.Frame(frame_selecoes)
        frame_time.pack(pady=15, fill="x")
        
        tk.Label(frame_time, text="Time:", font=("Arial", 14), width=6, anchor="e").pack(side="left", padx=(0, 10))
        self.combo_time.pack(side="left")
        
        # Botão confirmar
        self.btn_confirmar.pack(pady=30)
    
    def _on_carregar_dados(self):
        """Callback para carregar dados"""
        if self._carregar_dados_callback:
            self._carregar_dados_callback()
    
    def _on_ano_change(self, event):
        """Callback para mudança de ano"""
        if self._ano_change_callback:
            self._ano_change_callback()
    
    def _on_confirmar(self):
        """Callback para confirmar análise"""
        if self._confirmar_callback:
            self._confirmar_callback()
    
    def mostrar_carregamento(self, mostrar: bool):
        """Controla exibição do carregamento"""
        if mostrar:
            # Colocar progress bar antes do botão
            self.progress_bar.pack(pady=10)
            self.btn_carregar_dados.config(state="disabled")
            self.lbl_status.config(text="Carregando dados...")
        else:
            self.progress_bar.pack_forget()
            self.btn_carregar_dados.config(state="normal")
    
    def set_progresso_carregamento(self, progresso: int, mensagem: str):
        """Atualiza progresso do carregamento"""
        self.progress_bar['value'] = progresso
        self.lbl_status.config(text=mensagem)
        self.window.update()
    
    def dados_carregados(self, sucesso: bool, mensagem: str):
        """Callback para quando dados são carregados"""
        self.progress_bar.pack_forget()
        self.lbl_status.config(text=mensagem)
        
        if sucesso:
            self.btn_carregar_dados.pack_forget()
            self.combo_ano.config(state="readonly")
        else:
            self.btn_carregar_dados.config(state="normal")
    
    def set_anos(self, anos: List[int]):
        """Define anos disponíveis"""
        self.combo_ano['values'] = anos
        if anos:
            self.combo_ano.set(anos[0])
    
    def set_times(self, times: List[str]):
        """Define times disponíveis"""
        self.combo_time['values'] = times
        if times:
            self.combo_time.set(times[0])
            self.combo_time.config(state="readonly")
            self.btn_confirmar.config(state="normal")
        else:
            self.combo_time.config(state="disabled")
            self.btn_confirmar.config(state="disabled")
    
    def mostrar_erro(self, mensagem: str):
        """Mostra mensagem de erro"""
        messagebox.showerror("Erro", mensagem)
    
    def get_ano_selecionado(self) -> Optional[int]:
        """Retorna ano selecionado"""
        try:
            return int(self.combo_ano.get())
        except:
            return None
    
    def get_time_selecionado(self) -> str:
        """Retorna time selecionado"""
        return self.combo_time.get()
    
    def set_carregar_dados_listener(self, callback: Callable):
        """Define callback para carregar dados"""
        self._carregar_dados_callback = callback
    
    def set_ano_change_listener(self, callback: Callable):
        """Define callback para mudança de ano"""
        self._ano_change_callback = callback
    
    def set_confirmar_listener(self, callback: Callable):
        """Define callback para confirmar"""
        self._confirmar_callback = callback
    
    def show(self):
        """Mostra a janela"""
        self.window.deiconify()
    
    def hide(self):
        """Esconde a janela"""
        self.window.withdraw()