package br.com.alura.server;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

class ComandoExecutavelFactory {

	static Constructor<? extends Runnable> getConstrutorDe(Class<? extends Runnable> comandoExecutavel) {
		return (Constructor<? extends Runnable>) Stream.of(comandoExecutavel.getDeclaredConstructors())
				.filter(constructor -> constructor.isAnnotationPresent(ConstrutorPrioritario.class))
				.findFirst()
				.orElseGet(() -> {
					return Stream.of(comandoExecutavel.getDeclaredConstructors())
							.filter(constructorSemAnnotation -> constructorSemAnnotation.getParameterCount() > 0)
							.findFirst()
							.orElseThrow(() -> new RuntimeException("Não foi possível selecionar nenhum construtor para instanciar o comando"));
				});
	}

	static Runnable getComandoExecutavel(Constructor<? extends Runnable> construtorComandoExecutavel, Object... parametrosParaInstanciarComando) {

		List<Object> parametrosInicializacaoFiltradosPeloConstrutor = Stream.of(construtorComandoExecutavel.getParameterTypes())
				.map(tipoParametroConstrutor -> {

					for (Object parametroParaInstanciarComando : parametrosParaInstanciarComando) {
						if (tipoParametroConstrutor.isAssignableFrom(parametroParaInstanciarComando.getClass())) {
							return parametroParaInstanciarComando;
						}
					}

					throw new IllegalArgumentException("Não está sendo passado um parâmetro do tipo " + tipoParametroConstrutor + " necessário ao construtor -> " + construtorComandoExecutavel);

				}).collect(toList());

		try {
			return construtorComandoExecutavel.newInstance(parametrosInicializacaoFiltradosPeloConstrutor.toArray());

		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e.getCause());
		}
	}
}
