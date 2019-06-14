package org.brijframework.jdbc.context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.container.Container;
import org.brijframework.context.Context;
import org.brijframework.jdbc.container.JdbcContainer;
import org.brijframework.model.container.ModelContainer;
import org.brijframework.model.context.ModelContext;
import org.brijframework.support.model.Assignable;
import org.brijframework.support.model.DepandOn;
import org.brijframework.util.reflect.MethodUtil;
import org.brijframework.util.reflect.ReflectionUtils;

@DepandOn(depand=ModelContext.class)
public class JdbcContext implements Context{

	private Properties properties=new Properties();
	
	private Context context;
	
	private ConcurrentHashMap<Object, Container>  cache=new ConcurrentHashMap<>();
	
	public void setProperty(String key, String value) {
		properties.setProperty(key, value);
	}
	
	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	@Override
	public void initialize(Context context) {
		this.context=context;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void startup() {
		System.err.println("JdbcContext loading start.");
		List<Class<? extends JdbcContainer>> classes = new ArrayList<>();
		try {
			ReflectionUtils.getClassListFromExternal().forEach(cls->{
				if(ModelContainer.class.isAssignableFrom(cls)) {
					classes.add((Class<? extends JdbcContainer>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ReflectionUtils.getClassListFromInternal().forEach(cls->{
				if(ModelContainer.class.isAssignableFrom(cls)) {
					classes.add((Class<? extends JdbcContainer>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		classes.stream().sorted((container1, container2) -> {
			if (container1.isAnnotationPresent(DepandOn.class)) {
				DepandOn depandOn = container1.getAnnotation(DepandOn.class);
				if (depandOn.depand().equals(container2)) {
					return 1;
				}
			}
			if (container2.isAnnotationPresent(DepandOn.class)) {
				DepandOn depandOn = container2.getAnnotation(DepandOn.class);
				if (depandOn.depand().equals(container1)) {
					return -1;
				}
			}
			return 0;
		}).forEach((Context) -> {
			loading(Context);
		});
		System.err.println("JdbcContext loading done.");
	}

	private void loading(Class<?>cls) {
		if(cls.isInterface() || cls.getModifiers() == Modifier.ABSTRACT) {
			return ;
		}
		boolean called=false;
		for(Method method:MethodUtil.getAllMethod(cls)) {
			if(method.isAnnotationPresent(Assignable.class)) {
				try {
					System.err.println("Container    : "+cls.getName());
					Container container=(Container) method.invoke(null);
					getContainers().put(cls.getSimpleName(), container);
					called=true;
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		if(!called) {
			try {
				System.err.println("Container    : "+cls.getName());
				Container container=(Container) cls.newInstance();
				container.loadContainer();
				getContainers().put(cls.getSimpleName(), container);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public void destory() {
		
	}

	@Override
	public Context getParent() {
		return this.context;
	}

	@Override
	public ConcurrentHashMap<Object, Container> getContainers() {
		return cache;
	}
}
