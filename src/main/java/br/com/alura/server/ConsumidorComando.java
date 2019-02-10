package br.com.alura.server;

import java.util.concurrent.BlockingQueue;

import static java.lang.String.format;

class ConsumidorComando implements Runnable {
	private final BlockingQueue<ComandoComRespostaAoClienteDto> filaComandos;

	public ConsumidorComando(BlockingQueue<ComandoComRespostaAoClienteDto> filaComandos) {
		this.filaComandos = filaComandos;
	}

	@Override
	public void run() {

		try {
			ComandoComRespostaAoClienteDto dto;

			while ((dto = filaComandos.take()) != null) {
				System.out.println(format("Consumindo comando %s na Thread id: %s", dto.getNomeComando(), Thread.currentThread().getId()));
				Thread.sleep(20_000);
				enviarMensagemSucessoNoServidorECliente(dto);
			}

		} catch (InterruptedException e) {
			System.out.println("Encerrando a Thread id: " + Thread.currentThread().getId());
		}
	}

	private void enviarMensagemSucessoNoServidorECliente(ComandoComRespostaAoClienteDto dto) {
		String mensagemSucesso = format("Comando %s executado com sucesso na Thread id: %s", dto.getNomeComando(), Thread.currentThread().getId());
		dto.enviarRespostaAoCliente(mensagemSucesso);

		System.out.println(mensagemSucesso);
	}
}
