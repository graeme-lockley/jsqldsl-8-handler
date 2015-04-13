package za.co.no9.jsqldsl.tools.jsqldsl;

import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import za.co.no9.jdbcdry.tools.GenerationException;
import za.co.no9.jdbcdry.tools.TableMetaData;
import za.co.no9.util.FreeMarkerUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GenerateDSL {
    private static final String TEMPLATE_NAME = "za.co.no9.jsqldsl.tools.table.ftl";

    public void generateTable(File generatorTargetRoot, String packageName, TableMetaData tableMetaData) throws GenerationException {
        File targetDirectory = new File(generatorTargetRoot, packageName.replace('.', File.separatorChar));
        File targetFile = new File(targetDirectory, tableMetaData.tableName().dbName() + ".java");

        Map<String, Object> dataModel = assembleDataModel(packageName, tableMetaData);

        try {
            FileUtils.forceMkdir(targetDirectory);
            FileUtils.write(targetFile, FreeMarkerUtils.template(dataModel, TEMPLATE_NAME));
        } catch (IOException | TemplateException ex) {
            throw new GenerationException(ex);
        }
    }

    private Map<String, Object> assembleDataModel(String packageName, TableMetaData tableMetaData) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", packageName);
        dataModel.put("tableMetaData", tableMetaData);
        return dataModel;
    }
}
