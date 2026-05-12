package domuscontrol;

import domuscontrol.ui.MenuLogin;
import domuscontrol.ui.MenuPrincipal;
import domuscontrol.ui.Sessao;

import java.io.File;

/**
 * Ponto de entrada do DomusControl.
 */
public class Main {

    public static void main(String[] args) {
        DomusControl dc = DomusControl.getInstance();

        // Carregar estado
        File f = new File("domuscontrol.dat");
        if (f.exists()) {
            try {
                dc = DomusControl.carregarEstado();
                System.out.println("Estado carregado do ficheiro.\n");
            } catch (Exception e) {
                System.err.println("Erro ao carregar estado: " + e.getMessage());
                EstadoTeste.inicializar(dc);
            }
        } else {
            EstadoTeste.inicializar(dc);
        }

        // Login — loop para permitir logout e novo login
        Sessao sessao = new Sessao();
        MenuLogin login = new MenuLogin(dc, sessao);

        while (true) {
            if (login.run()) {
                new MenuPrincipal(dc, sessao).run();
                if (!sessao.estaAutenticado()) continue; // fez logout
            }
            break;
        }

        System.out.println("\n  Obrigado por usar o DomusControl. Ate logo!");
    }
}
