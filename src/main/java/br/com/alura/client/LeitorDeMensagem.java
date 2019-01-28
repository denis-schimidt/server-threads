package br.com.alura.client;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.Callable;

import static br.com.alura.server.Comando.isDesconectado;
import static java.lang.String.format;

class LeitorDeMensagem implements Callable<Void> {
	private final Socket socket;

	LeitorDeMensagem(Socket socket) {
		this.socket = socket;
	}

	@Override
	public Void call() {

		try (Scanner scanner = new Scanner(socket.getInputStream())) {

			while (scanner.hasNextLine()) {
				String mensagem = scanner.nextLine();
				System.out.println(format("Recebido em %1$td/%1$tm/%1$tY %1$tH:%1$tM:%1$tS -> %2$s", new Date(), mensagem));

				if (isDesconectado(mensagem)) {
					break;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			System.out.println("Thread Leitor de Mensagem finalizada!");
		}

		return null;
	}
}
