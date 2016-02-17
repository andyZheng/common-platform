package cn.org.rapid_framework.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import cn.org.rapid_framework.generator.util.FileHelper;
import cn.org.rapid_framework.generator.util.IOHelper;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
/**
 *
 * @author badqiu
 * @email badqiu(a)gmail.com
 */
public class Generator {
	private static final String GENERATOR_INSERT_LOCATION = "generator-insert-location";
	private List templateRootDirs = new ArrayList();
	private String outRootDir;

	String encoding = "UTF-8";
	public Generator() {
	}

	public void setTemplateRootDir(final File templateRootDir) {
		setTemplateRootDirs(new File[]{templateRootDir});
	}

	public void setTemplateRootDirs(final File[] templateRootDirs) {
		this.templateRootDirs = Arrays.asList(templateRootDirs);
	}

	public void addTemplateRootDir(final File f) {
		templateRootDirs.add(f);
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(final String v) {
		if(v == null) throw new IllegalArgumentException("encoding must be not null");
		this.encoding = v;
	}

	public void setOutRootDir(final String v) {
		if(v == null) throw new IllegalArgumentException("outRootDir must be not null");
		this.outRootDir = v;
	}

	public void generateByModelProvider(final IGeneratorModelProvider modelProvider) throws Exception {
		if(templateRootDirs.size() == 0) throw new IllegalStateException("'templateRootDirs' is empty");

		System.out.println("***************************************************************");
		System.out.println("* BEGIN generate "+modelProvider.getDisaplyText());
		System.out.println("***************************************************************");
		for(int i = 0; i < this.templateRootDirs.size(); i++) {
			final File templateRootDir = (File)templateRootDirs.get(i);
			generateByModelProvider(templateRootDir,modelProvider);
		}
	}

	private void generateByModelProvider(final File templateRootDir, final IGeneratorModelProvider modelProvider) throws Exception {
		if(templateRootDir == null) throw new IllegalStateException("'templateRootDir' must be not null");
		System.out.println("-------------------load template from templateRootDir = '"+templateRootDir.getAbsolutePath()+"'");

		final List templateFiles = new ArrayList();
		FileHelper.listFiles(templateRootDir, templateFiles);

		for(int i = 0; i < templateFiles.size(); i++) {
			final File templateFile = (File)templateFiles.get(i);
			final String templateRelativePath = FileHelper.getRelativePath(templateRootDir, templateFile);
			String outputFilePath = templateRelativePath;
			if(templateFile.isDirectory() || templateFile.isHidden())
				continue;
			if(templateRelativePath.trim().equals(""))
				continue;
			if(templateFile.getName().toLowerCase().endsWith(".include")){
				System.out.println("[skip]\t\t endsWith '.include' template:"+templateRelativePath);
				continue;
			}
			int testExpressionIndex = -1;
			if((testExpressionIndex = templateRelativePath.indexOf('@')) != -1) {
				outputFilePath = templateRelativePath.substring(0, testExpressionIndex);
				final String testExpressionKey = templateRelativePath.substring(testExpressionIndex+1);
				final Map map = getFilePathDataModel(modelProvider);
				final Object expressionValue = map.get(testExpressionKey);
				if(expressionValue == null) {
					System.err.println("[not-generate] WARN: test expression is null by key:["+testExpressionKey+"] on template:["+templateRelativePath+"]");
					continue;
				}
				if(!"true".equals(String.valueOf(expressionValue))) {
					System.out.println("[not-generate]\t test expression '@"+testExpressionKey+"' is false,template:"+templateRelativePath);
					continue;
				}
			}
			try {
				generateNewFileOrInsertIntoFile(modelProvider, newFreeMarkerConfiguration(), templateRelativePath,outputFilePath);
			}catch(final Exception e) {
				throw new RuntimeException("generate '"+modelProvider.getDisaplyText()+"' oucur error,template is:"+templateRelativePath,e);
			}
		}
	}

	private Configuration newFreeMarkerConfiguration() throws IOException {
		final Configuration config = new Configuration();

		final FileTemplateLoader[] templateLoaders = new FileTemplateLoader[templateRootDirs.size()];
		for(int i = 0; i < templateRootDirs.size(); i++) {
			templateLoaders[i] = new FileTemplateLoader((File)templateRootDirs.get(i));
		}
		final MultiTemplateLoader multiTemplateLoader = new MultiTemplateLoader(templateLoaders);

		config.setTemplateLoader(multiTemplateLoader);
		config.setNumberFormat("###############");
		config.setBooleanFormat("true,false");
		config.setDefaultEncoding(encoding);
		return config;
	}

	private void generateNewFileOrInsertIntoFile(final IGeneratorModelProvider modelProvider, final Configuration config, final String templateFile,final String outputFilePath) throws Exception {
		final Template template = config.getTemplate(templateFile);
		template.setOutputEncoding(encoding);

		final String targetFilename = getTargetFilename(modelProvider, outputFilePath);

		final Map templateDataModel = getTemplateDataModel(modelProvider);
		final File absoluteOutputFilePath = getAbsoluteOutputFilePath(targetFilename);
		if(absoluteOutputFilePath.exists()) {
			final StringWriter newFileContentCollector = new StringWriter();
			if(isFoundInsertLocation(template, templateDataModel, absoluteOutputFilePath, newFileContentCollector)) {
				System.out.println("[insert]\t generate content into:"+targetFilename);
				IOHelper.saveFile(absoluteOutputFilePath, newFileContentCollector.toString());
				return;
			}
		}

		System.out.println("[generate]\t template:"+templateFile+" to "+targetFilename);
		saveNewOutputFileContent(template, templateDataModel, absoluteOutputFilePath);
	}

	private String getTargetFilename(final IGeneratorModelProvider modelProvider, final String templateFilepath) throws Exception {
		final Map fileModel = getFilePathDataModel(modelProvider);
		final StringWriter out = new StringWriter();
		final Template template = new Template("filePath",new StringReader(templateFilepath),newFreeMarkerConfiguration());
		try {
			template.process(fileModel, out);
			return out.toString();
		}catch(final Exception e) {
			throw new IllegalStateException("cannot generate filePath from templateFilepath:"+templateFilepath+" cause:"+e,e);
		}
	}
	/**
	 * 得到生成"文件目录/文件路径"的Model
	 **/
	private Map getFilePathDataModel(final IGeneratorModelProvider modelProvider) throws Exception {
		final Map model = new HashMap();
		model.putAll(GeneratorProperties.getProperties()); //generator.properties的内容
		modelProvider.mergeFilePathModel(model);
		return model;
	}
	/**
	 * 得到FreeMarker的Model
	 **/
	private Map getTemplateDataModel(final IGeneratorModelProvider modelProvider) throws Exception {
		final Map model = new HashMap();
		model.putAll(GeneratorProperties.getProperties()); //generator.properties的内容
		modelProvider.mergeTemplateModel(model);
		return model;
	}

	private File getAbsoluteOutputFilePath(final String targetFilename) {
		final String outRoot = getOutRootDir();
		final File outputFile = new File(outRoot,targetFilename);
		outputFile.getParentFile().mkdirs();
		return outputFile;
	}

	private boolean isFoundInsertLocation(final Template template, final Map model, final File outputFile, final StringWriter newFileContent) throws IOException, TemplateException {
		final LineNumberReader reader = new LineNumberReader(new FileReader(outputFile));
		String line = null;
		boolean isFoundInsertLocation = false;

		final PrintWriter writer = new PrintWriter(newFileContent);
		while((line = reader.readLine()) != null) {
			writer.println(line);
			// only insert once
			if(!isFoundInsertLocation && line.indexOf(GENERATOR_INSERT_LOCATION) >= 0) {
				template.process(model,writer);
				writer.println();
				isFoundInsertLocation = true;
			}
		}

		writer.close();
		reader.close();
		return isFoundInsertLocation;
	}

	private void saveNewOutputFileContent(final Template template, final Map model, final File outputFile) throws IOException, TemplateException {
		final Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile),encoding));
		template.process(model,out);
		out.close();
	}

	public void clean() throws IOException {
		final String outRoot = getOutRootDir();
		final File file = new File(outRoot);
		if (file.exists()) {
			FileUtils.deleteDirectory(file);
		}
		System.out.println("[Delete Dir]	"+outRoot);
	}

	private String getOutRootDir() {
		if(outRootDir == null) throw new IllegalStateException("'outRootDir' property must be not null.");
		return outRootDir;
	}

}
