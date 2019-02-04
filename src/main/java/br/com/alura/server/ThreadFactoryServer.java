package br.com.alura.server;

import java.util.concurrent.ThreadFactory;

public class ThreadFactoryServer implements ThreadFactory {
	private ThreadFactory threadFactory;

	public ThreadFactoryServer(ThreadFactory threadFactory) {
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
