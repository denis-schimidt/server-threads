package br.com.alura.server;

import java.io.PrintWriter;

import static br.com.alura.server.Comando.DESCONECTAR;

class ComandoExecutavelDesconectar implements Runnable {
	private final PrintWriter saida;
	private final ServidorController servidorController;

	ComandoExecutavelDesconectar(PrintWriter saida, ServidorController servidorController) {
		this.saida = saida;
		this.servidorController = servidorController;
	}

	@Override
	public void run() {

		try {
			saida.println(DESCONECTAR);
			saida.flush();
			servidorController.encerrar();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
