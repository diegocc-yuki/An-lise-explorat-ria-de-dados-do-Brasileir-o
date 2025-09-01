import tkinter as tk
from controller.controlador_principal import ControladorPrincipal

def main():
    root = tk.Tk()
    root.withdraw()  # Esconder a janela root principal
    
    controlador = ControladorPrincipal()
    controlador.iniciar()
    
    root.mainloop()

if __name__ == "__main__":
    main()