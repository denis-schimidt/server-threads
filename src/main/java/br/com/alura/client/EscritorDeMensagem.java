package br.com.alura.client;

import br.com.alura.server.Comando;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Callable;

import static br.com.alura.server.Comando.DESCONECTAR;

class EscritorDeMensagem implements Callable<Void> {
    private final Socket socket;

    EscritorDeMensagem(Socket socket) {
        this.socket = socket;
    }

    @Override
    public Void call() {
        try (Scanner scanner = new Scanner(System.in)) {

            while (scanner.hasNextLine()) {
                String mensagem = scanner.nextLine();
                enviarMensagem(mensagem);

                if(DESCONECTAR == Comando.of(mensagem)){
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            System.out.println("Thread Escritor de Mensagem finalizada!");
        }

        return null;
    }

    private void enviarMensagem(String messageFomUser) throws IOException {
        PrintStream printStream = new PrintStream(socket.getOutputStream());
        printStream.println(messageFomUser);
        printStream.flush();
    }
}
