package org.brijframework.jdbc.context;

import static org.brijframework.support.config.SupportConstants.DATASOURCE_BOOTSTRAP_CONFIG_FILES;
import static org.brijframework.support.config.SupportConstants.DATASOURCE_BOOTSTRAP_CONFIG_PATHS;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.brijframework.asm.context.AbstractModuleContext;
import org.brijframework.asm.factories.FileFactory;
import org.brijframework.bean.context.BeanContext;
import org.brijframework.jdbc.container.JdbcContainer;
import org.brijframework.support.config.DatasourceBootstrap;
import org.brijframework.support.config.DepandOn;
import org.brijframework.support.enums.ResourceType;
import org.brijframework.util.objects.PropertiesUtil;
import org.brijframework.util.reflect.AnnotationUtil;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.reflect.ReflectionUtils;
import org.brijframework.util.resouces.YamlUtil;

@DepandOn(depand=BeanContext.class)
public class JdbcContext extends AbstractModuleContext{

	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		try {
			ReflectionUtils.getClassListFromExternal().forEach(cls->{
				if(JdbcContainer.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends JdbcContainer>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ReflectionUtils.getClassListFromInternal().forEach(cls->{
				if(JdbcContainer.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends JdbcContainer>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setInit(true);
	}
	
	@Override
	public void startup() {
		System.err.println("JdbcContext startup processing...");
		super.startup();
		System.err.println("JdbcContext startup completed....");
	}

	@Override
	public void destory() {
		System.err.println("JdbcContext destory processing...");
		super.destory();
		System.err.println("JdbcContext destory completed....");
	}
	
	protected void findAnnotationConfig(Properties configration) {
		if(configration.containsKey(DATASOURCE_BOOTSTRAP_CONFIG_PATHS)) {
			return;
		}
		try {
			ReflectionUtils.getClassListFromInternal().forEach(cls -> {
				if (cls.isAnnotationPresent(DatasourceBootstrap.class)) {
					DatasourceBootstrap config=(DatasourceBootstrap) AnnotationUtil.getAnnotation(cls, DatasourceBootstrap.class);
					List<File> files=new ArrayList<>();
					FileFactory.getResources(Arrays.asList(config.paths().split("\\|"))).forEach(file -> {
						files.add(file);
					});
					configration.put(DATASOURCE_BOOTSTRAP_CONFIG_PATHS, files);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void findFileLocateConfig(Properties configration) {
		try {
			List<File> files=new ArrayList<>();
			FileFactory.getResources(Arrays.asList(DATASOURCE_BOOTSTRAP_CONFIG_FILES.split("\\|"))).forEach(file -> {
				files.add(file);
			});
			if(!files.isEmpty())
			configration.put(DATASOURCE_BOOTSTRAP_CONFIG_PATHS, files);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	protected void loadFileLocateConfig(Properties configration) {
		try {
			@SuppressWarnings("unchecked")
			List<File> files=(List<File>) configration.get(DATASOURCE_BOOTSTRAP_CONFIG_PATHS);
			for(File filePath :files) {
				System.err.println(DATASOURCE_BOOTSTRAP_CONFIG_PATHS+"="+filePath);
				if(!filePath.exists()) {
					System.err.println("Env configration file not found.");
					continue ;
				}
				if(filePath.toString().endsWith(ResourceType.PROP)) {
					configration.putAll(PropertiesUtil.getProperties(filePath));
				}
				if(filePath.toString().endsWith(ResourceType.YML)||filePath.toString().endsWith(ResourceType.YAML)) {
					configration.putAll(YamlUtil.getEnvProperties(filePath));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void loadConfig() {
		System.err.println("=============================Datasource Configration startup=========================");
		
		Properties configration=getProperties();
		//configration.getProperties().putAll(System.getProperties());
		findAnnotationConfig(configration);
		findFileLocateConfig(configration);
		if(!configration.containsKey(DATASOURCE_BOOTSTRAP_CONFIG_PATHS)) {
			configration.put(DATASOURCE_BOOTSTRAP_CONFIG_PATHS, DATASOURCE_BOOTSTRAP_CONFIG_FILES);
		}
		loadFileLocateConfig(configration);
		configration.entrySet().stream().sorted((entry1,entry2)->((String)entry1.getKey()).compareToIgnoreCase(((String)entry2.getKey()))).forEach(entry->{
			System.err.println(entry.getKey()+"="+entry.getValue());
		});
		
		System.err.println("=============================Datasource Configration started==========================");
	}

}
