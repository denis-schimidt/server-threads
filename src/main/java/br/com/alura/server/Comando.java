package br.com.alura.server;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public enum Comando {
	C1(ComandoExecutavelC1.class),
	C2(ComandoExecutavelC2.class),
	DESCONECTAR(ComandoExecutavelDesconectar.class, "<EOF>"),
	DESCONHECIDO(ComandoExecutavelDesconhecido.class);

	private String comandoAsString;
	private Constructor<? extends Runnable> construtorExecutavel;

	Comando(Class<? extends Runnable> classeComandoExecutavel) {
		setarConstrutorComandoExecutavel(classeComandoExecutavel);
		this.comandoAsString = name();
	}

	Comando(Class<? extends Runnable> classeComandoExecutavel, String comandoAsString) {
		setarConstrutorComandoExecutavel(classeComandoExecutavel);
		this.comandoAsString = comandoAsString;
	}

	public static boolean isDesconectado(String mensagem) {
		return mensagem != null && mensagem.toUpperCase().contains(DESCONECTAR.name());
	}

	public static Comando of(String comandoAsString) {
		String comandoMaiusculoESemEspacos = comandoAsString.trim().toUpperCase();

		return Stream.of(values())
				.filter(comando -> comando.comandoAsString.equals(comandoMaiusculoESemEspacos))
				.findFirst()
				.orElse(DESCONHECIDO);
	}

	private void setarConstrutorComandoExecutavel(Class<? extends Runnable> comandoExecutavel) {
		construtorExecutavel = asList(comandoExecutavel.getDeclaredConstructors())
				.stream()
				.filter(constructor -> constructor.isAnnotationPresent(Selecionavel.class))
				.map(constructor -> (Constructor<? extends Runnable>) constructor)
				.findFirst()
				.orElseThrow(() -> new IllegalCallerException("Comando " + comandoExecutavel + " não possui um construtor selecionável"));
	}

	public Runnable getComandoExecutavel(PrintWriter writer, ServidorExecutor servidorExecutor) {

		try {
			return construtorExecutavel.getParameterCount() == 1 ? construtorExecutavel.newInstance(writer) :
					construtorExecutavel.newInstance(writer, servidorExecutor);

		} catch (Exception e) {
			throw new IllegalCallerException(e);
		}
	}
}
