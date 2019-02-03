package br.com.alura.server;

import java.io.PrintWriter;

import static br.com.alura.server.Comando.C3;
import static java.lang.String.format;

class ComandoExecutavelC3 implements Runnable {
	private static final String NOME_DO_COMANDO = C3.name();
	private static final String MENSAGEM_SUCESSO = format("Comando %s executado com sucesso!", NOME_DO_COMANDO);

	private final PrintWriter saida;

	ComandoExecutavelC3(PrintWriter saida) {
		this.saida = saida;
	}

	@Override
	public void run() {
		System.out.println(format("Executando comando %s...", NOME_DO_COMANDO));

		String mensagem = MENSAGEM_SUCESSO;

		try {
			Thread.sleep(5_000);

		} catch (InterruptedException e) {
			mensagem = format("Comando %s falhou -> Erro: %s", NOME_DO_COMANDO, e.getMessage());
		}

		saida.println(mensagem);
		saida.flush();

		System.out.println(mensagem);
	}
}
