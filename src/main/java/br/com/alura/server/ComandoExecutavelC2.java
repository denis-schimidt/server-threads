package br.com.alura.server;

import java.io.PrintWriter;
import java.util.Random;
import java.util.concurrent.*;

import static java.lang.String.format;

class ComandoExecutavelC2 implements Runnable {
	private final PrintWriter saida;
	private final ExecutorService executorService;

	ComandoExecutavelC2(PrintWriter saida, ExecutorService executorService) {
		this.saida = saida;
		this.executorService = executorService;
	}

	@Override
	public void run() {
		System.out.println("Executando comando C2...");

		Future<Integer> futureBancoDados = executorService.submit(new ComandoExecutavelC2BancoDados());
		Future<Integer> futureWebService = executorService.submit(new ComandoExecutavelC2WS());

		executorService.submit(new UnificaResultadoDeWebServiceEBancoDeDados(futureBancoDados, futureWebService, saida));
	}

	private static class UnificaResultadoDeWebServiceEBancoDeDados implements Callable<Void> {

		private final Future<Integer> futureBancoDados;
		private final Future<Integer> futureWebService;
		private final PrintWriter saida;

		UnificaResultadoDeWebServiceEBancoDeDados(Future<Integer> futureBancoDados, Future<Integer> futureWebService, PrintWriter saida) {
			this.futureBancoDados = futureBancoDados;
			this.futureWebService = futureWebService;
			this.saida = saida;
		}

		@Override
		public Void call() {

			System.out.println("Unificando resultado de WS e BD...");

			try {
				Integer resultadoBancoDados = futureBancoDados.get(10, TimeUnit.SECONDS);
				Integer resultadoWebService = futureWebService.get(10, TimeUnit.SECONDS);

				saida.println(format("Numeros calculados -> Banco de Dados %,3d e Webservice %,3d", resultadoBancoDados, resultadoWebService));

			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				tratarErro(e);
			}

			saida.flush();

			return null;
		}

		private void tratarErro(Exception e) {
			String mensagemAoCliente = format("Interrompendo a execução do(s) comando(s): %s e %s devido a %s ", ComandoExecutavelC2WS.NOME_DO_COMANDO,
					ComandoExecutavelC2BancoDados.NOME_DO_COMANDO, e);

			saida.println(mensagemAoCliente);
			futureWebService.cancel(true);
			futureBancoDados.cancel(true);

			System.out.println("Interrompendo a execução do(s) comando(s) -> " + e);
		}
	}

	private static class ComandoExecutavelC2WS implements Callable<Integer> {
		private static final String NOME_DO_COMANDO = ComandoExecutavelC2WS.class.getSimpleName();
		private static final String MENSAGEM_SUCESSO = format("Comando %s executado com sucesso!", NOME_DO_COMANDO);

		@Override
		public Integer call() throws Exception {
			System.out.println(format("Executando comando %s...", NOME_DO_COMANDO));

			System.out.println("Chamando Webservice X...");
			int numeroCalculado = new Random().nextInt(1_000_000);
			Thread.sleep(8_000);

			System.out.println(MENSAGEM_SUCESSO);

			return numeroCalculado;
		}
	}

	private static class ComandoExecutavelC2BancoDados implements Callable<Integer> {
		private static final String NOME_DO_COMANDO = ComandoExecutavelC2BancoDados.class.getSimpleName();
		private static final String MENSAGEM_SUCESSO = format("Comando %s executado com sucesso!", NOME_DO_COMANDO);

		@Override
		public Integer call() throws Exception {
			System.out.println(format("Executando comando %s...", NOME_DO_COMANDO));

			System.out.println("Chamando Banco de Dados Y...");
			int numeroCalculado = new Random().nextInt(5_000_000);
			Thread.sleep(5_000);

			System.out.println(MENSAGEM_SUCESSO);

			return numeroCalculado;
		}
	}
}
