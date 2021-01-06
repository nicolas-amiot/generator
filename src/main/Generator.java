package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;

import content.Content;
import content.UnpairedTag;
import entity.Index;
import entity.Variables;
import content.PairedTag;
import content.Tag;
import content.TagFile;
import content.Text;
import exception.Message;
import exception.TagException;
import utils.Marker;
import utils.TagUtils;

/**
 * Singleton Generator class is used for read and write files
 * 
 * @author Nicolas Amiot
 */
public class Generator {
	
	// Instance
	private static Generator instance;
	
	// Regex
	private final Pattern patternTag;
	private final Pattern patternParams;
	
	// Properties
	private String identifier;
	private String source;
	private String destination;
	private int loop; 
	
	// Location
	private String project;
	private String datafile;
	private String filename;
	
	// Varaibles
	private Variables variables;

	/**
	 * Constructor
	 */
	private Generator() {
		loadProperties();
		// () capturing group / (?:) non capturing group / [] interval / [^] exclusion / \\s caractère d'espacement
		patternTag = Pattern.compile("<" + identifier + "([a-z]*)(?::([a-z]+))?((?:\\s+[a-z]+=\"[^\"]*\")*)\\s*>");
		patternParams = Pattern.compile("\\s+([a-z]+)=\"([^\"]*)\"");
	}

	/**
	 * Create a unique instance
	 * 
	 * @return
	 */
	public static Generator getInstance() {
		if(instance == null) {
			instance = new Generator();
		}
		return instance;
	}
	
	/**
	 * Gets the identifier
	 * 
	 * @return
	 */
	public String getIdentifier() {
		return identifier;
	}
	
	/**
	 * Sets the identifier
	 * 
	 * @param identifier
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Gets the source
	 * 
	 * @return
	 */
	public String getSource() {
		return source;
	}
	
	/**
	 * Sets the source
	 * 
	 * @param source
	 */
	public void setSource(String source) {
		this.source =  source;
	}

	/**
	 * Gets the destination
	 * 
	 * @return
	 */
	public String getDestination() {
		return destination;
	}
	
	/**
	 * Sets the destination
	 * 
	 * @param destination
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	/**
	 * Gets the loop
	 * 
	 * @return
	 */
	public int getLoop() {
		return loop;
	}
	
	/**
	 * Sets the loop
	 * 
	 * @param loop
	 */
	public void setLoop(int loop) {
		this.loop = loop;
	}
	
	/**
	 * Gets the project
	 * 
	 * @return
	 */
	public String getProject() {
		return project;
	}

	/**
	 * Sets the project
	 * 
	 * @param project
	 */
	public void setProject(String project) {
		this.project = project;
	}
	
	/**
	 * Gets the datafile
	 * 
	 * @return
	 */
	public String getDatafile() {
		return datafile;
	}

	/**
	 * Sets the datafile
	 * 
	 * @param datafile
	 */
	public void setDatafile(String datafile) {
		this.datafile = datafile;
	}

	/**
	 * Gets the filename
	 * 
	 * @return
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * Sets the filename
	 * 
	 * @param filename
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	/**
	 * Gets the variables
	 * 
	 * @return
	 */
	public Variables getVariables() {
		return variables;
	}

	/**
	 * Sets the variables
	 * 
	 * @param variables
	 */
	public void setVariables(Variables variables) {
		this.variables = variables;
	}

	/**
	 * Load a properties file
	 */
	private void loadProperties() {
		Properties prop = new Properties();
		try(InputStream input = new FileInputStream("resources/conf.properties")) {
			prop.load(input);
			identifier = prop.getProperty("identifier");
			source = prop.getProperty("source").replace('\\', '/');
			destination = prop.getProperty("destination").replace('\\', '/');
			try {
				loop = Integer.parseInt(prop.getProperty("loop"));
			} catch(NumberFormatException e) {}
		} catch (IOException e) {}
		if(identifier == null) {
			identifier = "@";
		}
		if(source == null) {
			source = "resources/project/source";
		}
		if(destination == null) {
			destination = "resources/project/dest";
		}
		if(loop <= 0 || loop > 1000000) {
			loop = 1000;
		}
	}
	
	/**
	 * Read the json datafile
	 * 
	 * @throws IOException
	 * @throws TagException
	 */
	@SuppressWarnings("unchecked")
	public void readVariables() throws IOException, TagException {
		FileInputStream file = new FileInputStream(TagUtils.join('/', source, datafile));
		JsonReader reader = Json.createReader(file);
		JsonObject json = reader.readObject();
		Object object = jsonToValue(json);
		this.variables = new Variables((Map<String, Object>) object);
	}
	
	/**
	 * Transform JSON to JAVA object
	 * 
	 * @param json
	 * @return
	 * @throws TagException
	 */
	private Object jsonToValue(JsonValue json) throws TagException {
		Object object = null;
		if(json instanceof JsonObject) {
			for(Entry<String, JsonValue> entry : ((JsonObject) json).entrySet()) {
				Object obj = jsonToValue(entry.getValue());
				object = Variables.putObject(object, entry.getKey(), obj);
			}
		} else if(json instanceof JsonArray) {
			for(JsonValue jv : (JsonArray) json) {
				Object obj = jsonToValue(jv);
				object = Variables.putArray(object, obj);
			}
		} else if(json instanceof JsonNumber) {
			if(json.toString().contains(".")) {
				object = ((JsonNumber) json).doubleValue();
			} else {
				long number = ((JsonNumber) json).longValue();
				try {
					object = Math.toIntExact(number);
				} catch (ArithmeticException e) {
					object = (double) number;
				}
			}
		} else if(json instanceof JsonString) {
			String str = json.toString();
			// Supprime les guillements réccupérés
			object = str.substring(1, str.length() - 1);
		} else {
			// Il reste normalement que les valeurs FALSE, TRUE et NULL de possible
			object = Boolean.parseBoolean(json.toString());
		}
		return object;
	}
	
	/**
	 * Read a file to generate and identify the tags
	 * 
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws TagException
	 */
	public List<Content> readContent() throws FileNotFoundException, IOException, TagException {
		String text = new String(Files.readAllBytes(Paths.get(TagUtils.join('/', source, project, filename))));
		List<Content> contents = new ArrayList<>();
		List<Content> currentContents = contents;
		List<PairedTag> fillTags = new ArrayList<>();
		Index index;
		int line = 1;
		int column = 1;
		// Tant qu'on trouve un tag
		while((index = findTag(text, fillTags.isEmpty() ? null : fillTags.get(fillTags.size() - 1))) != null) {
			Tag tag = index.getTag();
			String currentText = text.substring(0, index.getStart());
			int[] pos = TagUtils.locate(currentText, line, column);
			tag.setLine(pos[0]);
			tag.setColumn(pos[1]);
			currentText = TagUtils.indent(currentText, fillTags.size());
			if(!currentText.isEmpty()) {
				currentContents.add(new Text(currentText));
			}
			currentText = text.substring(index.getStart(), index.getEnd());
			pos = TagUtils.locate(currentText, tag.getLine(), tag.getColumn());
			line = pos[0];
			column = pos[1];
			// Si c'est un simple tag on l'enregistre, sinon 
			if(tag instanceof UnpairedTag) {
				// Le tag init si présent doit se trouver dans l'entête du fichier
				if(Marker.TAG_INIT.equals(tag.getName()) && !currentContents.isEmpty()) {
					throw new TagException(Message.misplacedInit());
				}
				currentContents.add(tag);
			} else if(tag instanceof PairedTag) {
				PairedTag filltag = (PairedTag) tag;
				// Ouverture du tag : on l'ajoute à la pile et les contenus suivant sont sauvegardé dans ce tag
				// Fermeture du tag : on le supprime de la pile et reccupère le contenu precedent
				// Sinon on ferme le tag et reouvre le tag avec son nouveau état
				if(Marker.STATE_BEGIN.equals(tag.getState())) {
					fillTags.add(filltag);
					currentContents.add(filltag);
					currentContents = filltag.getContents();
				} else if(Marker.STATE_END.equals(tag.getState())) {
					// Si le nom du tag à fermer ne correspond au dernier tag ouvert, on lève une erreur
					if(!filltag.getName().equals(fillTags.get(fillTags.size() - 1).getName())) {
						throw new TagException(Message.unclosedTag(tag.getName()));
					}
					fillTags.get(fillTags.size() - 1).setLast(true);
					fillTags.remove(fillTags.size() - 1);
					// S'il n'y a plus de tag dans la pile, on recupère le contenu principal
					// Sinon on reccupère le contenu du tag ouvert précédent
					if(fillTags.isEmpty()) {
						currentContents = contents;
					} else {
						currentContents = fillTags.get(fillTags.size() - 1).getContents();
					}
				} else {
					// Si le nom du tag ne correspond au dernier tag ouvert, on lève une erreur
					if(!filltag.getName().equals(fillTags.get(fillTags.size() - 1).getName())) {
						throw new TagException(Message.unmatchedTag(tag.getName()));
					}
					fillTags.remove(fillTags.size() - 1);
					// S'il n'y a plus de tag dans la pile, on recupère le contenu principal
					// Sinon on reccupère le contenu du tag ouvert précédent
					if(fillTags.isEmpty()) {
						currentContents = contents;
					} else {
						currentContents = fillTags.get(fillTags.size() - 1).getContents();
					}
					currentContents.add(filltag);
					fillTags.add(filltag);
					currentContents = filltag.getContents();
				}
			}
			text = text.substring(index.getEnd());
		}
		// S'il y a du texte après le dernier tag traité, on l'enregistre
		if(text.length() != 0 && !text.trim().isEmpty()) {
			currentContents.add(new Text(text));
		}
		// Si tous les tags n'ont pas été fermé, on lève une erreur
		if(!fillTags.isEmpty()) {
			throw new TagException(Message.unclosedTags());
		}
		return contents;
	}
	
	/**
	 * Identify the tags with the different property and locate this
	 * 
	 * @param text
	 * @param lastTag
	 * @return
	 * @throws TagException
	 */
	private Index findTag(String text, PairedTag lastTag) throws TagException {
		Matcher m = patternTag.matcher(text);
		if (m.find()) {
			// Reccupère les différentes données de la regex
			int start = m.start();
			int end = m.end();
			String name = m.group(1);
			if(name.isEmpty()) {
				name = Marker.TAG_INIT;
			}
			String state = m.group(2);
			if(state == null) {
				state = Marker.STATE_BEGIN;
			}
			String params = m.group(3);
			Map<String, String> parameters = new HashMap<>();
			if(params != null) {
				m = patternParams.matcher(params);
				while (m.find()) {
					parameters.put(m.group(1), m.group(2));
				}
			}
			// Reccupère l'état du dernier tag ouvert avant celui ci s'il s'agit du même tag
			String lastState = null;
			if(lastTag != null && name.equals(lastTag.getName()) && !state.equals(Marker.STATE_BEGIN)) {
				lastState = lastTag.getState();
			}
			// Verifie que les données sont correctes
			Marker.validdate(name, state, parameters, lastState);
			// Transforme les données en un tag
			Tag tag;
			if(Marker.isPairedTag(name)) {
				tag = new PairedTag(name, state, parameters);
			} else {
				tag = new UnpairedTag(name, state, parameters);
			}
			return new Index(tag, start, end);
		} else {
			return null;
		}
	}
	
	/**
	 * Write the generated file
	 * 
	 * @param contents
	 * @throws TagException
	 * @throws IOException
	 */
	public void writeFile(List<Content> contents) throws TagException, IOException {
		TagFile tagFile = createTagFile(contents);
		if(tagFile != null) {
			String text = Marker.iterate(contents, variables);
			Path path = Paths.get(TagUtils.join('/', destination, tagFile.getLocation(), tagFile.getFilename()));
			Files.createDirectories(path.getParent());
			Files.write(path, text.getBytes());
		}
	}
	
	/**
	 * Gets the properties for the generated file with the init tag if exist
	 * 
	 * @param contents
	 * @return
	 * @throws TagException
	 */
	private TagFile createTagFile(List<Content> contents) throws TagException {
		Tag tag = null;
		TagFile tagFile;
		if(!contents.isEmpty() && contents.get(0) instanceof Tag) {
			tag = (Tag) contents.get(0);
		}
		if(tag != null && Marker.TAG_INIT.equals(tag.getName())) {
			tagFile = (TagFile) Marker.call(tag, variables);
			contents.remove(0);
			if(tagFile != null) {
				tagFile.setContents(contents);
			}
		} else {
			tagFile = new TagFile(filename, null, contents);
		}
		return tagFile;
	}

}
