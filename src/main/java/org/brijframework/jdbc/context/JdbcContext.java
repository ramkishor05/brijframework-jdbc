package org.brijframework.jdbc.context;

import java.util.Properties;

import org.brijframework.asm.context.DefaultContainerContext;
import org.brijframework.jdbc.container.JdbcContainer;
import org.brijframework.model.context.ModelContext;
import org.brijframework.support.model.DepandOn;
import org.brijframework.support.util.SupportUtil;
import org.brijframework.util.reflect.ReflectionUtils;

@DepandOn(depand=ModelContext.class)
public class JdbcContext extends DefaultContainerContext{

	private Properties properties=new Properties();
	
	public void setProperty(String key, String value) {
		properties.setProperty(key, value);
	}
	
	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		try {
			ReflectionUtils.getClassListFromExternal().forEach(cls->{
				if(JdbcContainer.class.isAssignableFrom(cls)) {
					register((Class<? extends JdbcContainer>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ReflectionUtils.getClassListFromInternal().forEach(cls->{
				if(JdbcContainer.class.isAssignableFrom(cls)) {
					register((Class<? extends JdbcContainer>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void startup() {
		System.err.println("JdbcContext loading start.");
		SupportUtil.getDepandOnSortedClassList(getClassList()).forEach((cls) -> {
			loadContainer(cls);
		});
		System.err.println("JdbcContext loading done.");
	}

	@Override
	public void destory() {
		
	}

}
