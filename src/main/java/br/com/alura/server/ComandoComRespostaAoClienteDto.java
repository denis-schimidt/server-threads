package br.com.alura.server;

import java.io.PrintWriter;
import java.util.Objects;

class ComandoComRespostaAoClienteDto {
	private final PrintWriter escritorMensagemCliente;
	private final Comando comando;

	ComandoComRespostaAoClienteDto(PrintWriter escritorMensagemCliente, Comando comando) {
		Objects.requireNonNull(escritorMensagemCliente);
		Objects.requireNonNull(comando);

		this.escritorMensagemCliente = escritorMensagemCliente;
		this.comando = comando;
	}

	String getNomeComando() {
		return comando.name();
	}

	void enviarRespostaAoCliente(String mensagem) {
		escritorMensagemCliente.println(mensagem);
		escritorMensagemCliente.flush();
	}
}
