package br.com.alura.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

class ExecutorComandoDoCliente implements Runnable {
	private final ExecutorService executorService;
	private final Socket socket;
	private final ServidorExecutor servidorExecutor;

	ExecutorComandoDoCliente(ExecutorService executorService, Socket socket, ServidorExecutor servidorExecutor) {
		this.executorService = executorService;
		this.socket = socket;
		this.servidorExecutor = servidorExecutor;
	}

	@Override
	public void run() {

		try (Scanner entrada = new Scanner(socket.getInputStream()); PrintWriter saida = new PrintWriter(socket.getOutputStream())) {

			while (entrada.hasNextLine()) {
				Comando comandoCliente = Comando.of(entrada.nextLine());
				executorService.execute(comandoCliente.getComandoExecutavel(saida, servidorExecutor));
			}

		} catch (RejectedExecutionException e) {
			System.out.println("Nova requisição rejeitada. Servidor encerrado!");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}