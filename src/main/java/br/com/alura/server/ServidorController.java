package br.com.alura.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

class ServidorController {
	private static final int CAPACIDADE_DA_FILA_DE_COMANDOS = 3;
	private final ServerSocket serverSocket;
	private final ExecutorService executorService;
	private final AtomicBoolean servidorRodando;
	private BlockingQueue<Comando> filaComandos;

	ServidorController() throws IOException {
		serverSocket = new ServerSocket(12345);
		executorService = Executors.newCachedThreadPool(new ThreadFactoryServer(Executors.defaultThreadFactory()));
		servidorRodando = new AtomicBoolean(true);
		filaComandos = new ArrayBlockingQueue<>(CAPACIDADE_DA_FILA_DE_COMANDOS);
	}

	void rodar() throws IOException {

		System.out.println("Servidor iniciado e pronto para requisições!");

		while (servidorRodando.get()) {

			try {
				Socket socket = serverSocket.accept();

				if (socket.isConnected()) {
					System.out.printf("Cliente %s:%s conectado ao servidor\n", socket.getLocalAddress(), socket.getPort());
					executorService.execute(new ExecutorComandoDoCliente(executorService, socket, this, filaComandos));
				}

			} catch (SocketException e) {
				System.out.println("Servidor rodando -> " + servidorRodando);
			}
		}
	}

	void encerrar() throws Exception {
		servidorRodando.set(false);

		if (!serverSocket.isClosed()) {
			executorService.awaitTermination(10, TimeUnit.SECONDS);
			executorService.shutdown();
			serverSocket.close();
			System.out.println("Desligando servidor...");
		}
	}
}
