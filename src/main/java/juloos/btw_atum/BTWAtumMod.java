package juloos.btw_atum;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

import java.io.*;
import java.util.*;
import java.util.logging.*;

public class BTWAtumMod implements ModInitializer {
	public static boolean running = false;
	public static Logger LOGGER = Logger.getLogger("atum");
	public static String seed = "";
	public static int difficulty = 4;
	public static int generatorType = 0;
	public static int rsgAttempts;
	public static int ssgAttempts;
	public static boolean structures = true;
	public static boolean bonusChest = false;
	public static KeyBinding resetKey = new KeyBinding("Reset Key", Keyboard.KEY_F6);
	public static boolean loading = false;
	static Map<String, String> extraProperties = new LinkedHashMap<>();
	static File configFile;

	public static void log(Level level, String message) {
		LOGGER.log(level, message);
	}

	public static String load(File file) {
		try (Scanner scanner = new Scanner(file)) {
			if (scanner.hasNext()) {
				return scanner.nextLine();
			} else {
				return null;
			}
		} catch (FileNotFoundException e) {
			log(Level.SEVERE, "Could not load:\n" + e.getMessage());
			return null;
		}
	}

	static Properties getProperties(File configFile) {
		try (FileInputStream f = new FileInputStream(configFile)) {
			Properties properties = new Properties();
			properties.load(f);
			return properties;
		} catch (IOException e) {
			return null;
		}
	}

	public static void saveProperties() {
		try (FileOutputStream f = new FileOutputStream(configFile)) {
			Properties properties = getProperties();
			properties.putAll(extraProperties);
			properties.store(f, """
				This is the config file for BTW Atum.
				seed: leave empty for a random seed
				difficulty: 0 = STANDARD, 1 = RELAXED, 2 = HOSTILE, 3 = CLASSIC, 4 = HOSTILE_LOCKED
				difficulty (nightmare mode installed): 0 = BAD_DREAM, 2 = NIGHTMARE / BLOODMARE
				generatorType: 0 = DEFAULT, 1 = SUPERFLAT, 2 = LARGE_BIOMES""");
		} catch (IOException e) {
			log(Level.WARNING, "Could not save config file:\n" + e.getMessage());
		}
	}

	@NotNull
	private static Properties getProperties() {
		Properties properties = new Properties();
		properties.put("resetKey", String.valueOf(resetKey.keyCode));
		properties.put("rsgAttempts", String.valueOf(rsgAttempts));
		properties.put("ssgAttempts", String.valueOf(ssgAttempts));
		properties.put("seed", seed);
		properties.put("difficulty", String.valueOf(difficulty));
		properties.put("generatorType", String.valueOf(generatorType));
		properties.put("structures", String.valueOf(structures));
		properties.put("bonusChest", String.valueOf(bonusChest));
		return properties;
	}

	static void loadFromProperties(Properties properties) {
		if (properties != null) {
			for (Map.Entry<Object, Object> entry : properties.entrySet()) {
				if (!entry.getKey().equals("seed") && !entry.getKey().equals("difficulty") && !entry.getKey().equals("generatorType") && !entry.getKey().equals("ssgAttempts") && !entry.getKey().equals("rsgAttempts") && !entry.getKey().equals("structures") && !entry.getKey().equals("bonusChest") && !entry.getKey().equals("resetKey"))
					extraProperties.put((String) entry.getKey(), (String) entry.getValue());
			}
			try {
				resetKey.keyCode = properties.containsKey("resetKey") ? Integer.parseInt(properties.getProperty("resetKey")) : Keyboard.KEY_F6;
			} catch (NumberFormatException e) {
				resetKey.keyCode = Keyboard.KEY_F6;
			}
			seed = !properties.containsKey("seed") ? "" : properties.getProperty("seed");
			if (seed == null)
				seed = "";
			seed = seed.trim();
			try {
				difficulty = !properties.containsKey("difficulty") ? 4 : Integer.parseInt(properties.getProperty("difficulty"));
			} catch (NumberFormatException e) {
				difficulty = 4;
			}
			if (difficulty > 4 || difficulty < 0)
				difficulty = 4;
			if (FabricLoader.getInstance().isModLoaded("nightmare_mode"))
				difficulty = ((difficulty / 2) % 2) * 2;
			try {
				generatorType = !properties.containsKey("generatorType") ? 0 : Integer.parseInt(properties.getProperty("generatorType"));
			} catch (NumberFormatException e) {
				generatorType = 0;
			}
			if (generatorType > 6)
				generatorType = 0;
			try {
				rsgAttempts = !properties.containsKey("rsgAttempts") ? 0 : Integer.parseInt(properties.getProperty("rsgAttempts"));
			} catch (NumberFormatException e) {
				rsgAttempts = 0;
			}
			try {
				ssgAttempts = !properties.containsKey("ssgAttempts") ? 0 : Integer.parseInt(properties.getProperty("ssgAttempts"));
			} catch (NumberFormatException e) {
				ssgAttempts = 0;
			}
			structures = !properties.containsKey("structures") || Boolean.parseBoolean(properties.getProperty("structures"));
			bonusChest = Boolean.parseBoolean(properties.getProperty("bonusChest"));
		}
	}

	@Override
	public void onInitialize() {
		log(Level.INFO, "Initializing");
		new File("config").mkdir();
		new File("config/atum").mkdir();
		configFile = new File("config/atum/atum.properties");
		if (!configFile.exists()) {
			try {
				configFile.createNewFile();
				saveProperties();
			} catch (IOException e) {
				log(Level.SEVERE, "Could not create config file:\n" + e.getMessage());
			}
			File difficultyFile = new File("ardifficulty.txt");
			if (difficultyFile.exists()) {
				String difInput = load(difficultyFile);
				difficulty = difInput == null ? 4 : Integer.parseInt(difInput.trim());
				if (difficulty > 4 || difficulty < 0)
					difficulty = 4;
				if (FabricLoader.getInstance().isModLoaded("nightmare_mode"))
					difficulty = (difficulty % 2) * 2;
				difficultyFile.delete();
			}
			File seedFile = new File("seed.txt");
			if (seedFile.exists()) {
				seed = load(seedFile);
				seed = seed == null ? "" : seed;
				seedFile.delete();
			}
			File attemptsFile = new File("attempts.txt");
			if (attemptsFile.exists()) {
				String s = load(attemptsFile);
				if (s != null) {
					try {
						rsgAttempts = Integer.parseInt(s);
					} catch (NumberFormatException e) {
						rsgAttempts = 0;
					}
				}
				attemptsFile.delete();
			}
		} else {
			loadFromProperties(getProperties(configFile));
		}
	}
}
