package World;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

public class ResourceFactory {
	private HashMap<Integer, String> registations;

	public ResourceFactory(String packageName) {
		try {
			String[] classNames;
			this.registations = new HashMap<Integer, String>();
			classNames = this.getClasses(packageName);
			this.registerClasses(classNames);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	private String[] getClasses(String packageName) {
		try {
			ClassLoader classLoader;
			ArrayList<String> retVal;
			ArrayList<File> classFiles;
			Enumeration<URL> resources;
			retVal = new ArrayList<String>();
			classFiles = new ArrayList<File>();
			classLoader = this.getClass().getClassLoader();
			resources = classLoader.getResources(packageName.replace('.', '/'));
			while (resources.hasMoreElements()) {
				File parent = new File(resources.nextElement().getFile());
				if (parent.exists()) {
					if (parent.canRead()) {
						if (parent.isDirectory()) {
							for (File child : parent.listFiles()) {
								if (child.exists()) {
									if (child.canRead()) {
										if (child.isFile()) {
											if (child.getName().endsWith(".class")) {
												classFiles.add(child);
											}
										}
									}
								}
							}
						} else if (parent.isFile()) {
							if (parent.getName().endsWith(".class")) {
								classFiles.add(parent);
							}
						}
					}
				}
			}
			for (File classFile : classFiles) {
				retVal.add(packageName + '.' + classFile.getName().substring(0, classFile.getName().length() - 6));
			}
			return (String[]) retVal.toArray(new String[retVal.size()]);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Resource getTile(int id) {
		try {
			return (Resource) Class.forName(this.registations.get(Integer.valueOf(id))).getConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void registerClasses(String[] classNames) {
		try {
			for (String className : classNames) {
				Resource tile = (Resource) Class.forName(className).getConstructor().newInstance();
				this.registations.put(Integer.valueOf(tile.getID()), className);
			}
			return;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
}
