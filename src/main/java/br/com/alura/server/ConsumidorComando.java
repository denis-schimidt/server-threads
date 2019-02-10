package br.com.alura.server;

import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.String.format;

class ConsumidorComando implements Runnable {
	private final PrintWriter escritorMensagemParaCliente;
	private final BlockingQueue<Comando> filaComandos;
	private final Comando comando;
	private final AtomicBoolean servidorRodando;

	public ConsumidorComando(PrintWriter escritorMensagemParaCliente, BlockingQueue<Comando> filaComandos, Comando comando, AtomicBoolean servidorRodando) {
		this.escritorMensagemParaCliente = escritorMensagemParaCliente;
		this.filaComandos = filaComandos;
		this.comando = comando;
		this.servidorRodando = servidorRodando;
	}

	@Override
	public void run() {

		try {

			while(servidorRodando.get()){
				Comando comando = filaComandos.take();
				String nomeComando = comando.name();
				System.out.println(format("Consumindo comando %s na Thread id: %s", nomeComando, Thread.currentThread().getId()));
				Thread.sleep(20_000);
				enviarMensagemSucessoNoServidorECliente(nomeComando);
			}

		} catch (InterruptedException e) {
			System.out.println("Encerrando a Thread id: " + Thread.currentThread().getId());
		}
	}

	private void enviarMensagemSucessoNoServidorECliente(String nomeComando) {
		String mensagemSucesso = format("Comando %s executado com sucesso na Thread id: %s", nomeComando, Thread.currentThread().getId());
		escritorMensagemParaCliente.println(mensagemSucesso);
		escritorMensagemParaCliente.flush();

		System.out.println(mensagemSucesso);
	}
}
