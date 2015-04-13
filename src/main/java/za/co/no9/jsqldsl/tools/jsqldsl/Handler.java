package za.co.no9.jsqldsl.tools.jsqldsl;

import org.apache.maven.plugin.logging.Log;
import za.co.no9.jdbcdry.drivers.DBDriver;
import za.co.no9.jdbcdry.port.jsqldslmojo.ConfigurationException;
import za.co.no9.jdbcdry.port.jsqldslmojo.TableFilter;
import za.co.no9.jdbcdry.port.jsqldslmojo.Target;
import za.co.no9.jdbcdry.tools.DatabaseMetaData;
import za.co.no9.jdbcdry.tools.GenerationException;
import za.co.no9.jdbcdry.tools.TableMetaData;
import za.co.no9.jdbcdry.tools.ToolHandler;

import java.sql.Connection;
import java.sql.SQLException;

public class Handler implements ToolHandler {
    private Log log;
    private HandlerTarget target;

    @Override
    public void setup(Log log, Target target) {
        this.log = log;
        this.target = HandlerTarget.from(target);
    }

    @Override
    public void process(Connection connection, TableFilter tableFilter) throws SQLException, ConfigurationException, GenerationException {
        DBDriver dbDriver = target.getDBDriver();
        DatabaseMetaData databaseMetaData = dbDriver.databaseMetaData(connection);

        for (TableMetaData table : databaseMetaData.allTables()) {
            if (tableFilter.filter(table)) {
                log.info("JSQLDSL: " + table.tableName());
                new GenerateDSL().generateTable(target.generatorTargetRoot(), target.getTargetPackageName(), table);
            }
        }
    }
}
