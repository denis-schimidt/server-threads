package br.com.alura.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

class ServidorController {
	private final ServerSocket serverSocket;
	private final ExecutorService executorService;
	private final AtomicBoolean servidorRodando;

	ServidorController() throws IOException {
		serverSocket = new ServerSocket(12345);
		executorService = Executors.newCachedThreadPool(new ThreadFactoryServer(Executors.privilegedThreadFactory()));
		servidorRodando = new AtomicBoolean(true);
	}

	void rodar() throws IOException {

		System.out.println("Servidor iniciado e pronto para requisições!");

		while (servidorRodando.get()) {

			try {
				Socket socket = serverSocket.accept();

				if (socket.isConnected()) {
					System.out.printf("Cliente %s:%s conectado ao servidor\n", socket.getLocalAddress(), socket.getPort());
					executorService.execute(new ExecutorComandoDoCliente(executorService, socket, this));
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
