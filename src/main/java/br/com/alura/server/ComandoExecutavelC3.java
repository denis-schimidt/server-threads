package br.com.alura.server;

import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

import static br.com.alura.server.Comando.C3;
import static java.lang.String.format;

class ComandoExecutavelC3 implements Runnable {
	private static final String NOME_DO_COMANDO = C3.name();
	private static final String MENSAGEM_SUCESSO = format("Comando %s adicionado na fila!", NOME_DO_COMANDO);
	private static final int NUMERO_DE_CONSUMIDORES = 3;

	private final PrintWriter escritorMensagemParaCliente;
	private final ExecutorService executorService;
	private final BlockingQueue<Comando> filaComandos;
	private volatile AtomicBoolean servidorRodando;

	ComandoExecutavelC3(PrintWriter escritorMensagemParaCliente, ExecutorService executorService, BlockingQueue<Comando> filaComandos, AtomicBoolean servidorRodando) {
		this.escritorMensagemParaCliente = escritorMensagemParaCliente;
		this.executorService = executorService;
		this.filaComandos = filaComandos;
		this.servidorRodando = servidorRodando;

		inicializarConsumidoresDeComando();
	}

	private void inicializarConsumidoresDeComando() {
		IntStream.of(NUMERO_DE_CONSUMIDORES).forEach(i -> executorService.submit(new ConsumidorComando(escritorMensagemParaCliente, filaComandos, C3, servidorRodando)));
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
