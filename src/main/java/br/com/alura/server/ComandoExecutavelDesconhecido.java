package br.com.alura.server;

import java.io.PrintWriter;

class ComandoExecutavelDesconhecido implements Runnable {
	private PrintWriter saida;

	@Selecionavel
	ComandoExecutavelDesconhecido(PrintWriter saida) {
		this.saida = saida;
	}

	@Override
	public void run() {
		String mensagem = "Comando desconhecido! Verifique os comandos disponíveis pelo sistema.";
		System.out.println(mensagem);
		saida.println(mensagem);
		saida.flush();
	}
}
