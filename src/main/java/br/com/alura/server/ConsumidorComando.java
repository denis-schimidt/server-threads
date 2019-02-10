package br.com.alura.server;

import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;

import static java.lang.String.format;

class ConsumidorComando implements Runnable {
	private final PrintWriter escritorMensagemParaCliente;
	private final BlockingQueue<Comando> filaComandos;
	private final Comando comando;

	public ConsumidorComando(PrintWriter escritorMensagemParaCliente, BlockingQueue<Comando> filaComandos, Comando comando) {
		this.escritorMensagemParaCliente = escritorMensagemParaCliente;
		this.filaComandos = filaComandos;
		this.comando = comando;
	}

	@Override
	public void run() {

		try {
			Comando comando = null;

			while((comando = filaComandos.take()) != null){
				String nomeComando = comando.name();
				System.out.println(format("Consumindo comando %s", nomeComando));
				Thread.sleep(20_000);
				enviarMensagemSucessoNoServidorECliente(nomeComando);
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void enviarMensagemSucessoNoServidorECliente(String nomeComando) {
		String mensagemSucesso = format("Comando %s executado com sucesso.", nomeComando);
		escritorMensagemParaCliente.println(mensagemSucesso);
		escritorMensagemParaCliente.flush();

		System.out.println(mensagemSucesso);
	}
}
