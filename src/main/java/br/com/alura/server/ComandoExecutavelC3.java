package br.com.alura.server;

import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import static br.com.alura.server.Comando.C3;
import static java.lang.String.format;

class ComandoExecutavelC3 implements Runnable {
	private static final String MENSAGEM_SUCESSO = format("Comando %s adicionado na fila!", C3.name());
	private final PrintWriter escritorMensagemParaCliente;
	private final ExecutorService executorService;
	private final BlockingQueue<ComandoComRespostaAoClienteDto> filaComandos;

	ComandoExecutavelC3(PrintWriter escritorMensagemParaCliente, ExecutorService executorService, BlockingQueue<ComandoComRespostaAoClienteDto> filaComandos) {
		this.escritorMensagemParaCliente = escritorMensagemParaCliente;
		this.executorService = executorService;
		this.filaComandos = filaComandos;
	}

	@Override
	public void run() {
		try {
			filaComandos.put(new ComandoComRespostaAoClienteDto(escritorMensagemParaCliente, C3));
			String mensagem = MENSAGEM_SUCESSO;
			escritorMensagemParaCliente.println(mensagem);
			escritorMensagemParaCliente.flush();

			System.out.println(mensagem);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
