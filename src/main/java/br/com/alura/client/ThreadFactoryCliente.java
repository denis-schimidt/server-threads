package br.com.alura.client;

import java.util.concurrent.ThreadFactory;

public class ThreadFactoryCliente implements ThreadFactory {
	private ThreadFactory threadFactory;

	public ThreadFactoryCliente(ThreadFactory threadFactory) {
		this.threadFactory = threadFactory;
	}

	@Override
	public Thread newThread(Runnable runnable) {
		Thread thread = threadFactory.newThread(runnable);

		thread.setUncaughtExceptionHandler((threadAlvo, exception) -> {
			System.out.println("Erro executado na " + threadAlvo.getName() + " deu erro -> " + exception.getMessage());
		});

		return thread;
	}
}
