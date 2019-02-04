package br.com.alura.server;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

public enum Comando {
	C1(ComandoExecutavelC1.class),
	C2(ComandoExecutavelC2.class),
	C3(ComandoExecutavelC3.class),
	DESCONECTAR(ComandoExecutavelDesconectar.class, "<EOF>"),
	DESCONHECIDO(ComandoExecutavelDesconhecido.class);

	private final String apelidoDoComando;
	private final Constructor<? extends Runnable> construtorComandoExecutavel;

	Comando(Class<? extends Runnable> classeComandoExecutavel) {
		this(classeComandoExecutavel, null);
	}

	Comando(Class<? extends Runnable> classeComandoExecutavel, String apelidoDoComando) {
		this.construtorComandoExecutavel = ComandoExecutavelFactory.getConstrutorDe(classeComandoExecutavel);
		this.apelidoDoComando = apelidoDoComando;
	}

	public static boolean isDesconectado(String mensagem) {
		return mensagem != null && mensagem.toUpperCase().contains(DESCONECTAR.name());
	}

	public static Comando of(String comandoAsString) {
		String nomeComandoOuApelido = comandoAsString.trim().toUpperCase();

		return Stream.of(values())
				.filter(comando -> comando.name().equals(nomeComandoOuApelido) ||
							ofNullable(comando.apelidoDoComando).map(apelidoComando -> apelidoComando.equals(nomeComandoOuApelido)).orElse(false))
				.findFirst()
				.orElse(DESCONHECIDO);
	}

	public Runnable getComandoExecutavel(PrintWriter writer, ServidorController servidorController, ExecutorService executorService) {
		return ComandoExecutavelFactory.getComandoExecutavel(construtorComandoExecutavel, writer, servidorController, executorService);
	}
}
