package br.com.alura.server;

import java.io.IOException;

public class ServidorMain {

    public static void main(String[] args) throws IOException {
        new ServidorController().rodar();
    }
}
