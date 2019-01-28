package br.com.alura.server;

import java.io.PrintWriter;
import java.util.concurrent.RejectedExecutionException;

import static br.com.alura.server.Comando.C1;
import static java.lang.String.format;

class ComandoExecutavelC1 implements Runnable {
	private static final String NOME_DO_COMANDO = C1.name();
	private static final String MENSAGEM_SUCESSO = format("Comando %s executado com sucesso!", NOME_DO_COMANDO);

	private PrintWriter saida;

	@Selecionavel
	ComandoExecutavelC1(PrintWriter saida) {
		this.saida = saida;
	}

	@Override
	public void run() {
		System.out.println(format("Executando comando %s...", NOME_DO_COMANDO));

		String mensagem = MENSAGEM_SUCESSO;

		try {
			Thread.sleep(10_000);

		} catch (InterruptedException e) {
			mensagem = format("Comando %s falhou -> Erro: %s", NOME_DO_COMANDO, e.getMessage());
		}

		saida.println(mensagem);
		saida.flush();

		System.out.println(mensagem);
	}
}
