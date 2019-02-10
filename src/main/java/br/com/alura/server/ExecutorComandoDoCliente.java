package br.com.alura.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

class ExecutorComandoDoCliente implements Runnable {
	private final ExecutorService executorService;
	private final Socket socket;
	private final ServidorController servidorController;
	private final BlockingQueue<ComandoComRespostaAoClienteDto> filaComandos;

	ExecutorComandoDoCliente(ExecutorService executorService, Socket socket, ServidorController servidorController, BlockingQueue<ComandoComRespostaAoClienteDto> filaComandos) {
		this.executorService = executorService;
		this.socket = socket;
		this.servidorController = servidorController;
		this.filaComandos = filaComandos;
	}

	@Override
	public void run() {

		try (Scanner entrada = new Scanner(socket.getInputStream()); PrintWriter saida = new PrintWriter(socket.getOutputStream())) {

			while (entrada.hasNextLine()) {
				Comando comandoCliente = Comando.of(entrada.nextLine());
				Runnable comandoExecutavel = comandoCliente.getComandoExecutavel(saida, servidorController, executorService, filaComandos);

				executorService.execute(comandoExecutavel);
			}

		} catch (RejectedExecutionException e) {
			System.out.println("Servidor bloqueado para novas requisições!");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
