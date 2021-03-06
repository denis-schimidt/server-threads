package br.com.alura.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.stream.IntStream.rangeClosed;

class ServidorController {
	private static final int CAPACIDADE_DA_FILA_DE_COMANDOS = 3;
	private static final int NUMERO_DE_CONSUMIDORES = 10;
	private final ServerSocket serverSocket;
	private final ExecutorService executorService;
	private final AtomicBoolean servidorRodando;
	private BlockingQueue<ComandoComRespostaAoClienteDto> filaComandos;

	ServidorController() throws IOException {
		serverSocket = new ServerSocket(12345);
		executorService = Executors.newCachedThreadPool(new ThreadFactoryServer(Executors.defaultThreadFactory()));
		servidorRodando = new AtomicBoolean(true);
		filaComandos = new ArrayBlockingQueue<>(CAPACIDADE_DA_FILA_DE_COMANDOS);

		inicializarConsumidoresComando();
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

	private void inicializarConsumidoresComando(){
		rangeClosed(1, NUMERO_DE_CONSUMIDORES)
				.forEach(i -> executorService.submit(new ConsumidorComando(filaComandos)));
	}

	void encerrar() throws Exception {
		servidorRodando.set(false);

		if (!serverSocket.isClosed()) {
			System.out.println("Tentando encerrar todas as threads...");
			executorService.shutdown();
			executorService.awaitTermination(60, TimeUnit.SECONDS);
			serverSocket.close();
			executorService.shutdownNow();
			System.out.println("Encerrando servidor...");
		}
	}
}
