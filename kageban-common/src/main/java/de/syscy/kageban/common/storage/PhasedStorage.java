package de.syscy.kageban.common.storage;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public interface PhasedStorage extends IPunishmentStorage {
	static PhasedStorage wrap(IPunishmentStorage storage) {
		Phaser phaser = new Phaser();

		return (PhasedStorage) Proxy.newProxyInstance(PhasedStorage.class.getClassLoader(), new Class[] { PhasedStorage.class }, (proxy,
				method, args) -> invoke(phaser, storage, method, args));
	}

	static Object invoke(Phaser phaser, IPunishmentStorage storage, Method method, Object[] args) throws Throwable {
		switch(method.getName()) {
			case "init":
			case "getDao":
				return method.invoke(storage, args);
		}

		if(method.getName().equals("shutdown")) {
			try {
				phaser.awaitAdvanceInterruptibly(phaser.getPhase(), 10, TimeUnit.SECONDS);
			} catch(InterruptedException | TimeoutException ex) {
				ex.printStackTrace();
			}

			storage.shutdown();
		}

		phaser.register();

		try {
			return method.invoke(storage, args);
		} finally {
			phaser.arriveAndDeregister();
		}
	}
}