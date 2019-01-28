package br.com.alura.client;

import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Arrays.asList;

class ClienteExecutor {
    private final ExecutorService executorService;

    ClienteExecutor() {
        executorService = Executors.newFixedThreadPool(2);
    }

    void executar() {

        try (Socket socket = new Socket("localhost", 12345)) {

            if (socket.isConnected()) {
                System.out.println("Cliente conectado ao servidor");

                executorService.invokeAll(asList(new LeitorDeMensagem(socket), new EscritorDeMensagem(socket)));
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            executorService.shutdown();
            System.out.println("Socket com o cliente fechado!");
        }
    }
}
