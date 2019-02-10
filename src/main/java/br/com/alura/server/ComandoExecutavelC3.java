package br.com.alura.server;

import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import static br.com.alura.server.Comando.C3;
import static java.lang.String.format;
import static java.util.stream.IntStream.of;

class ComandoExecutavelC3 implements Runnable {
	private static final String NOME_DO_COMANDO = C3.name();
	private static final String MENSAGEM_SUCESSO = format("Comando %s adicionado na fila!", NOME_DO_COMANDO);
	private static final int NUMERO_DE_CONSUMIDORES = 2;

	private final PrintWriter escritorMensagemParaCliente;
	private final ExecutorService executorService;
	private final BlockingQueue<Comando> filaComandos;

	ComandoExecutavelC3(PrintWriter escritorMensagemParaCliente, ExecutorService executorService, BlockingQueue<Comando> filaComandos) {
		this.escritorMensagemParaCliente = escritorMensagemParaCliente;
		this.executorService = executorService;
		this.filaComandos = filaComandos;

		inicializarConsumidoresDeComando();
	}

	private void inicializarConsumidoresDeComando() {
		of(NUMERO_DE_CONSUMIDORES).forEach(i -> executorService.submit(new ConsumidorComando(escritorMensagemParaCliente, filaComandos, C3)));
	}

	@Override
	public void run() {
		try {
			filaComandos.put(C3);
			String mensagem = MENSAGEM_SUCESSO;
			escritorMensagemParaCliente.println(mensagem);
			escritorMensagemParaCliente.flush();

			System.out.println(mensagem);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
