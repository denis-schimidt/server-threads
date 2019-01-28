package br.com.alura.server;

import java.io.IOException;
import java.io.PrintWriter;

import static br.com.alura.server.Comando.DESCONECTAR;

class ComandoExecutavelDesconectar implements Runnable {
	private PrintWriter saida;
	private final ServidorExecutor servidorExecutor;

	@Selecionavel
	ComandoExecutavelDesconectar(PrintWriter saida, ServidorExecutor servidorExecutor) {
		this.saida = saida;
		this.servidorExecutor = servidorExecutor;
	}

	@Override
	public void run() {

		try {
			saida.println(DESCONECTAR);
			saida.flush();
			servidorExecutor.encerrar();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
